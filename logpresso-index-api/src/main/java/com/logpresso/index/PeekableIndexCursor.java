package com.logpresso.index;

import java.util.NoSuchElementException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PeekableIndexCursor implements BooleanIndexCursor {
	private final Logger logger = LoggerFactory.getLogger("com.logpresso.index.PeekableIndexCursor");

	LogIndexItem peekCache = null;
	BooleanIndexCursor cursor = null;

	@Override
	public String toString() {
		return cursor.toString();
	}

	public PeekableIndexCursor(BooleanIndexCursor cursor) {
		super();
		this.cursor = cursor;
	}

	public BooleanIndexCursor getCursor() {
		return this.cursor;
	}

	@Override
	public boolean hasNext() {
		if (peekCache != null)
			return true;

		if (cursor.hasNext()) {
			peekCache = cursor.next();
			return true;
		} else
			return false;
	}

	public LogIndexItem peek() {
		if (!hasNext())
			throw new NoSuchElementException();

		return peekCache;
	}

	public void pop() {
		if (peekCache == null && !hasNext())
			throw new NoSuchElementException();

		peekCache = null;
	}

	@Override
	public LogIndexItem next() {
		if (logger.isDebugEnabled())
			logger.debug("{}: next called", this);

		if (!hasNext())
			throw new NoSuchElementException();
		LogIndexItem ret = peekCache;
		peekCache = null;
		return ret;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void skip(long offset) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void close() {
		cursor.close();
	}

	@Override
	public Set<LogIndexQueryQualifier> getQualifiers() {
		return cursor.getQualifiers();
	}

	@Override
	public BooleanIndexCursor negate() {
		return new PeekableIndexCursor(cursor.negate());
	}

	@Override
	public BooleanIndexCursor optimize() {
		return cursor.optimize();
	}
}
