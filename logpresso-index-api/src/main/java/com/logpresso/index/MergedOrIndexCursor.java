package com.logpresso.index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;

public class MergedOrIndexCursor implements BooleanIndexCursor {
	private static class CursorPeak implements Comparable<CursorPeak> {
		PeekableIndexCursor cursor;

		public CursorPeak(BooleanIndexCursor c) {
			if (c instanceof PeekableIndexCursor) {
				cursor = (PeekableIndexCursor) c;
			} else {
				cursor = new PeekableIndexCursor(c);
			}
		}

		@Override
		public int compareTo(CursorPeak o) {
			cursor.hasNext();
			o.cursor.hasNext();
			if (!cursor.hasNext() && !o.cursor.hasNext())
				return 0;
			if (cursor.hasNext() && !o.cursor.hasNext())
				return -1;
			else if (!cursor.hasNext() && o.cursor.hasNext())
				return 1;
			else
				return cursor.peek().compareTo(o.cursor.peek()) * -1;
		}
	}

	private LinkedList<CursorPeak> cursors = new LinkedList<CursorPeak>();

	private boolean fetchStarted = false;
	private LogIndexItem next;

	private String toStringCache;

	private MergedOrIndexCursor(BooleanIndexCursor lhs, BooleanIndexCursor rhs) {
		add(lhs);
		add(rhs);
		// Collections.sort(cursors);
	}

	public void add(BooleanIndexCursor cursor) {
		if (fetchStarted)
			throw new IllegalArgumentException("cannot add after cursor started");
		toStringCache = null;
		if (cursor instanceof MergedOrIndexCursor) {
			cursors.addAll(((MergedOrIndexCursor) cursor).cursors);
			// Collections.sort(cursors);
		} else {
			cursors.add(new CursorPeak(cursor));
			// Collections.sort(cursors);
		}
	}

	@Override
	public void skip(long offset) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void close() {
		CursorPeak poll = cursors.poll();
		while (poll != null) {
			poll.cursor.close();
			poll = cursors.poll();
		}
	}

	@Override
	public boolean hasNext() {
		if (next != null)
			return true;

		if (!fetchStarted) {
			fetchStarted = true;
			Collections.sort(cursors);
		}
		CursorPeak peek = cursors.poll();
		if (peek.cursor.hasNext()) {
			// remove all same logId
			while (cursors.peek() != null && cursors.peek().cursor.hasNext()) {
				CursorPeak p = cursors.peek();
				if (peek.compareTo(p) == 0 && p.cursor.hasNext()) {
					cursors.poll();
					p.cursor.next();
					sortedAdd(cursors, p);
				} else {
					break;
				}
			}

			next = peek.cursor.next();
			sortedAdd(cursors, peek);
			return true;
		} else {
			sortedAdd(cursors, peek);
			return false;
		}
	}

	private void sortedAdd(LinkedList<CursorPeak> list, CursorPeak peek) {
		int idx = 0;
		for (CursorPeak p : list) {
			int cmpResult = peek.compareTo(p);
			if (cmpResult > 0) {
				idx++;
				continue;
			} else {
				break;
			}
		}
		list.add(idx, peek);
	}

	@Override
	public LogIndexItem next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
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
		Set<LogIndexQueryQualifier> result = new HashSet<LogIndexQueryQualifier>();
		for (CursorPeak c : cursors) {
			result.addAll(c.cursor.getQualifiers());
		}
		return result;
	}

	@Override
	public BooleanIndexCursor negate() {
		throw new UnsupportedOperationException();
	}

	@Override
	public BooleanIndexCursor optimize() {
		return this;
	}

	@Override
	public String toString() {
		if (toStringCache != null)
			return toStringCache;
		StringBuilder sb = null;
		ArrayList<String> strs = new ArrayList<String>(cursors.size());
		for (CursorPeak c : cursors) {
			strs.add(c.cursor.toString());
		}
		Collections.sort(strs);
		for (String c : strs) {
			if (sb == null) {
				sb = new StringBuilder();
				sb.append("union(");
			} else {
				sb.append(", ");
			}
			sb.append(c);
		}
		sb.append(")");
		toStringCache = sb.toString();
		return toStringCache;
	}

	public static MergedOrIndexCursor union(BooleanIndexCursor optLhs, BooleanIndexCursor optRhs) {
		if (!(optLhs instanceof MergedOrIndexCursor) && !(optRhs instanceof MergedOrIndexCursor)) {
			return new MergedOrIndexCursor(optLhs, optRhs);
		} else {
			MergedOrIndexCursor ucursor = null;
			if (optLhs instanceof MergedOrIndexCursor) {
				ucursor = (MergedOrIndexCursor) optLhs;
				ucursor.add(optRhs);
			} else if (optRhs instanceof MergedOrIndexCursor) {
				ucursor = (MergedOrIndexCursor) optRhs;
				ucursor.add(optLhs);
			}
			return ucursor;
		}
	}

	public static Set<String> getTables(Set<LogIndexQueryQualifier> quals) {
		HashSet<String> result = new HashSet<String>();
		for (LogIndexQueryQualifier q : quals) {
			result.add(q.getTableName());
		}
		return result;
	}

}
