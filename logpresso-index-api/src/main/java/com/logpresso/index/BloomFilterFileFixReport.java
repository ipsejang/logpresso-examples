package com.logpresso.index;

import java.io.File;

public class BloomFilterFileFixReport {
	private File indexPath;
	private File dataPath;
	private long truncatedIndexBytes;
	private long truncatedDataBytes;

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
				+ "\ntruncated index bytes: " + truncatedIndexBytes + "\ntruncated data bytes: " + truncatedDataBytes;
	}

}
