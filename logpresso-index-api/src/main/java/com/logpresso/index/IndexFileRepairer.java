package com.logpresso.index;

import java.io.File;
import java.io.IOException;

public interface IndexFileRepairer {
	public IndexFileFixReport fix(File idxFile, File datFile) throws IOException;
	public IndexFileFixReport fix(File idxFile, File datFile, long logMaxId) throws IOException;
}
