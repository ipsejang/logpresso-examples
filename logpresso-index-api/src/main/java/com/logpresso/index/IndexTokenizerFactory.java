/*
 * Copyright 2013 Eediom Inc. All rights reserved.
 */
package com.logpresso.index;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @since 0.9
 * @author xeraph
 */
public interface IndexTokenizerFactory {
	String getName();

	/**
	 * @since 2.2.0
	 */
	String getDisplayName(Locale locale);

	/**
	 * @since 2.2.0
	 */
	String getDescription(Locale locale);

	List<IndexConfigSpec> getConfigSpecs();

	IndexTokenizer newIndexTokenizer(Map<String, String> config);
}
