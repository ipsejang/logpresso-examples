/*
 * Copyright 2013 Eediom Inc. All rights reserved.
 */
package com.logpresso.index;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @since 0.9
 * @author xeraph
 */
public class LogIndexerStatus {
	private String tableName;
	private String indexName;
	private Date day;
	private Date lastFlush;
	private long queueCount;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public Date getLastFlush() {
		return lastFlush;
	}

	public void setLastFlush(Date lastFlush) {
		this.lastFlush = lastFlush;
	}

	public long getQueueCount() {
		return queueCount;
	}

	public void setQueueCount(long queueCount) {
		this.queueCount = queueCount;
	}

	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return "table=" + tableName + ", index=" + indexName + " (" + dateFormat.format(day) + "), waiting logs for being indexed=" + queueCount;
	}

}
