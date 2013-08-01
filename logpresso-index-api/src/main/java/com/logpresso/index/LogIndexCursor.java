/*
 * Copyright 2013 Eediom Inc. All rights reserved.
 */
package com.logpresso.index;

import java.util.Iterator;
import java.util.Set;

/**
 * @since 0.9
 * @author xeraph
 */
public interface LogIndexCursor extends Iterator<LogIndexItem> {
	void skip(long offset);

	void close();

	Set<LogIndexQueryQualifier> getQualifiers();
}
