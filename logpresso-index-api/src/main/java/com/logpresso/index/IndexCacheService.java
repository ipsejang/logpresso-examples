/*
 * Copyright 2013 Eediom Inc. All rights reserved.
 */
package com.logpresso.index;

import java.util.List;
import java.util.Set;

public interface IndexCacheService {
	Set<String> getStorageNames();

	List<IndexCacheItem> getItems(String storage);

	IndexCacheItem find(String storage, IndexCacheKey key);

	void put(String storage, IndexCacheKey key, IndexCacheItem item);

	void clear(String storage);

	List<IndexCacheConfig> getConfigs(String storage);

	void setConfig(String storage, String key, String value);

	void unsetConfig(String storage, String key);
}
