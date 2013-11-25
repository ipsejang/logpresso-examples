package com.logpresso.index;

public interface IndexRepairService {
	public BloomFilterFileRepairer newBloomFilterFileRepairer();
	public IndexFileRepairer newInvertedIndexFileRepairer();
}
