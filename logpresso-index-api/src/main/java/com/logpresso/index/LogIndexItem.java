/*
 * Copyright 2013 Eediom Inc. All rights reserved.
 */
package com.logpresso.index;

import java.util.Date;

/**
 * @since 0.9
 * @author xeraph
 */
public interface LogIndexItem extends Comparable<LogIndexItem> {
	String getTableName();
	
	Date getDay();

	long getLogId();
}
