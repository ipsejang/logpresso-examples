/*
 * Copyright 2013 Eediom Inc. All rights reserved.
 */
package com.logpresso.index;

import java.util.List;
import java.util.Map;


/**
 * @since 0.9
 * @author xeraph
 */
public interface IndexTokenizerRegistry {
	List<IndexTokenizerFactory> getFactories();

	IndexTokenizerFactory getFactory(String name);

	void registerFactory(IndexTokenizerFactory factory);

	void unregisterFactory(IndexTokenizerFactory factory);

	IndexTokenizer newTokenizer(String name, Map<String, String> config);
}
