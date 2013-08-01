package com.logpresso.index;

public interface IndexCacheItem {

	// estimated byte count
	int getWeight();

	void onRemoval();
}
