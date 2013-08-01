package com.logpresso.index;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BooleanOrIndexCursor implements BooleanIndexCursor {
	private final Logger logger = LoggerFactory.getLogger("com.logpresso.index.BooleanOrIndexCursor");

	private final PeekableIndexCursor rhs;
	private final PeekableIndexCursor lhs;

	public BooleanOrIndexCursor(BooleanIndexCursor lo, BooleanIndexCursor ro) {
		if (lo instanceof PeekableIndexCursor)
			this.lhs = (PeekableIndexCursor) lo;
		else
			this.lhs = new PeekableIndexCursor(lo);
		if (ro instanceof PeekableIndexCursor)
			this.rhs = (PeekableIndexCursor) ro;
		else
			this.rhs = new PeekableIndexCursor(ro);
	}

	@Override
	public String toString() {
		return String.format("(%s or %s)", lhs, rhs);
	}

	@Override
	public void skip(long offset) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void close() {
		lhs.close();
		rhs.close();
	}

	private LogIndexItem next = null;

	@Override
	public boolean hasNext() {
		if (next != null)
			return true;

		if (logger.isDebugEnabled())
			logger.debug("{}: hasNext executed", this);

		// ensure both side being prefetched
		boolean lhsHasNext = lhs.hasNext();
		boolean rhsHasNext = rhs.hasNext();

		if (!lhsHasNext && !rhsHasNext) {
			return false;
		}

		while (lhsHasNext && rhsHasNext) {
			LogIndexItem lhsPeek = lhs.peek();
			LogIndexItem rhsPeek = rhs.peek();

			if (lhsPeek.equals(rhsPeek)) {
				next = lhsPeek;
				// consume both
				lhs.pop();
				rhs.pop();
				break;
			} else {
				// consume larger first
				int sgn = Integer.signum(lhsPeek.compareTo(rhsPeek));
				if (sgn > 0) {
					next = lhsPeek;
					lhs.pop();
					break;
				} else if (sgn < 0) {
					next = rhsPeek;
					rhs.pop();
					break;
				}
			}
		}
		if (next != null)
			return true;
		else {
			lhsHasNext = lhs.hasNext();
			rhsHasNext = rhs.hasNext();
			if (lhsHasNext && !rhsHasNext) {
				next = lhs.next();
				return true;
			} else if (rhsHasNext && !lhsHasNext) {
				next = rhs.next();
				return true;
			} else
				return false;
		}
	}

	@Override
	public LogIndexItem next() {
		if (logger.isDebugEnabled())
			logger.debug("{}: next called", this);
		if (!hasNext())
			throw new NoSuchElementException();

		LogIndexItem ret = next;
		next = null;
		return ret;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<LogIndexQueryQualifier> getQualifiers() {
		Set<LogIndexQueryQualifier> set = new HashSet<LogIndexQueryQualifier>();
		set.addAll(lhs.getQualifiers());
		set.addAll(rhs.getQualifiers());
		return set;
	}

	@Override
	public BooleanIndexCursor negate() {
		return new BooleanAndIndexCursor(lhs.negate(), rhs.negate());
	}

	@Override
	public BooleanIndexCursor optimize() {
		BooleanIndexCursor optLhs = lhs.optimize();
		BooleanIndexCursor optRhs = rhs.optimize();

		// Set<LogIndexQueryQualifier> qualLhs = optLhs.getQualifiers();
		// Set<LogIndexQueryQualifier> qualRhs = optRhs.getQualifiers();
		//
		// // obtain intersection
		// Set<String> lhsTables = MergedOrIndexCursor.getTables(qualLhs);
		// Set<String> rhsTables = MergedOrIndexCursor.getTables(qualRhs);
		// lhsTables.retainAll(rhsTables);
		//
		// // for OR cursor, if intersecion of both children is empty, can be
		// executed in serialized order.
		// if (lhsTables.isEmpty()) {
		return MergedOrIndexCursor.union(optLhs, optRhs);
		// } else {
		// return new BooleanOrIndexCursor(optLhs, optRhs);
		// }
	}

}
