/*
 * Copyright 2013 Eediom Inc. All rights reserved.
 */
package com.logpresso.index;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @since 0.9
 * @author xeraph
 */
public class BatchIndexingStatus implements Comparable<BatchIndexingStatus> {
	private BatchIndexingTask task;

	private Date day;

	private IndexFileSet files;

	private long tokenCount;
	private long logCount;
	private boolean done;

	public BatchIndexingTask getTask() {
		return task;
	}

	public void setTask(BatchIndexingTask task) {
		this.task = task;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public File getIndexFile() {
		return files.getIndexFile();
	}

	public File getDataFile() {
		return files.getDataFile();
	}

	public File getBloomFilterIndexFile() {
		return files.getBFIndexFile();
	}

	public File getBloomFilterDataFile() {
		return files.getBFDataFile();
	}
	
	public IndexFileSet getFiles() {
		return files;
	}

	public void setFiles(IndexFileSet files) {
		this.files = files;
	}

	public long getTokenCount() {
		return tokenCount;
	}

	public void setTokenCount(long tokenCount) {
		this.tokenCount = tokenCount;
	}

	public void addTokenCount(long value) {
		this.tokenCount += value;
	}

	public long getLogCount() {
		return logCount;
	}

	public void setLogCount(long logCount) {
		this.logCount = logCount;
	}

	public void addLogCount(long value) {
		this.logCount += value;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	@Override
	public int compareTo(BatchIndexingStatus o) {
		if (o == null)
			return 1;

		return (int) (day.getTime() - o.day.getTime());
	}

	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return String.format("table=%s, index=%s, day=%s", task.getTableName(), task.getIndexName(), dateFormat.format(day));
	}
}
