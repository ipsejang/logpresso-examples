/*
 * Copyright 2013 Eediom Inc. All rights reserved.
 */
package com.logpresso.index;

import java.io.File;

public class IndexFileFixReport {
	private File indexPath;
	private File dataPath;
	private long truncatedIndexBytes;
	private long truncatedDataBytes;
	private long totalSegmentCount;
	private boolean fixed = false;
	
	public boolean isFixed() {
		return fixed;
	}
	
	public void setFixed() {
		this.fixed = true;
	}

	public File getIndexPath() {
		return indexPath;
	}

	public void setIndexPath(File indexPath) {
		this.indexPath = indexPath;
	}

	public File getDataPath() {
		return dataPath;
	}

	public void setDataPath(File dataPath) {
		this.dataPath = dataPath;
	}

	public long getTruncatedDataBytes() {
		return truncatedDataBytes;
	}

	public void setTruncatedDataBytes(long truncatedDataBytes) {
		this.truncatedDataBytes = truncatedDataBytes;
	}

	public long getTruncatedIndexBytes() {
		return truncatedIndexBytes;
	}

	public void setTruncatedIndexBytes(long truncatedIndexBytes) {
		this.truncatedIndexBytes = truncatedIndexBytes;
	}
	
	@Override
	public String toString() {
		return "index path: " + indexPath.getAbsolutePath() + "\ndata path: " + dataPath.getAbsolutePath()
				+ "\ntotal segment count: " + totalSegmentCount
				+ "\ntruncated index bytes: " + truncatedIndexBytes + "\ntruncated data bytes: " + truncatedDataBytes;
	}

	public long getTotalSegmentCount() {
		return totalSegmentCount;
	}

	public void setTotalSegmentCount(long totalSegmentCount) {
		this.totalSegmentCount = totalSegmentCount;
	}
}
