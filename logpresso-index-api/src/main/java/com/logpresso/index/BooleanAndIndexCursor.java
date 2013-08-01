package com.logpresso.index;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

public class BooleanAndIndexCursor implements BooleanIndexCursor {
	private final PeekableIndexCursor lhs;
	private final PeekableIndexCursor rhs;

	public BooleanAndIndexCursor(BooleanIndexCursor lhs, BooleanIndexCursor rhs) {
		if (lhs instanceof PeekableIndexCursor)
			this.lhs = (PeekableIndexCursor) lhs;
		else
			this.lhs = new PeekableIndexCursor(lhs);
		if (rhs instanceof PeekableIndexCursor)
			this.rhs = (PeekableIndexCursor) rhs;
		else
			this.rhs = new PeekableIndexCursor(rhs);
	}

	@Override
	public String toString() {
		return "(" + lhs + " and " + rhs + ")";
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

		// ensure both side being prefetched
		boolean lhsHasNext = lhs.hasNext();
		boolean rhsHasNext = rhs.hasNext();

		if (!lhsHasNext || !rhsHasNext) {
			return false;
		}

		while (lhs.hasNext() && rhs.hasNext()) {
			LogIndexItem lhsPeek = lhs.peek();
			LogIndexItem rhsPeek = rhs.peek();

			if (lhsPeek.equals(rhsPeek)) {
				next = lhsPeek;
				// consume both
				lhs.pop();
				rhs.pop();
				break;
			} else {
				// consume larger only
				int sgn = Integer.signum(lhsPeek.compareTo(rhsPeek));
				if (sgn > 0)
					lhs.pop();
				else if (sgn < 0)
					rhs.pop();
			}
		}
		if (next != null)
			return true;
		else
			return false;
	}

	@Override
	public LogIndexItem next() {
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
		Set<LogIndexQueryQualifier> merged = new HashSet<LogIndexQueryQualifier>();
		merged.addAll(lhs.getQualifiers());
		merged.addAll(rhs.getQualifiers());
		return merged;
	}

	@Override
	public BooleanIndexCursor negate() {
		return new BooleanOrIndexCursor(lhs.negate(), rhs.negate());
	}

	@Override
	public BooleanIndexCursor optimize() {
		BooleanIndexCursor optLhs = lhs.optimize();
		BooleanIndexCursor optRhs = rhs.optimize();

		return new BooleanAndIndexCursor(optLhs, optRhs);
	}
}
