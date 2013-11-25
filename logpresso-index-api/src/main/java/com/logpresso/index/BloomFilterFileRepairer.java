package com.logpresso.index;

import java.io.File;
import java.io.IOException;

public interface BloomFilterFileRepairer {
	public BloomFilterFileFixReport fix(File idxFile, File datFile) throws IOException;
	public BloomFilterFileFixReport fix(File idxFile, File datFile, long segCount) throws IOException;
}
