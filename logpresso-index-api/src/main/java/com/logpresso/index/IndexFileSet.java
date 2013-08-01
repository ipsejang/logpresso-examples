/*
 * Copyright 2013 Eediom Inc. All rights reserved.
 */
package com.logpresso.index;

import java.io.File;

/**
 * @since 0.9
 * @author xeraph
 */
public class IndexFileSet {
	private File indexFile;
	private File dataFile;

	private File bloomFilterIndexFile;
	private File bloomFilterDataFile;
	
	public IndexFileSet(File indexFile, File dataFile, File bfIndexFile, File bfDataFile) {
		this.indexFile = indexFile;
		this.dataFile = dataFile;
		this.bloomFilterIndexFile = bfIndexFile;
		this.bloomFilterDataFile = bfDataFile;
	}

	public File getIndexFile() {
		return indexFile;
	}

	public File getDataFile() {
		return dataFile;
	}

	public File getBFIndexFile() {
		return bloomFilterIndexFile;
	}

	public File getBFDataFile() {
		return bloomFilterDataFile;
	}
}
