/*
 * Copyright 2013 Eediom Inc. All rights reserved.
 */
package com.logpresso.index;

import java.util.Locale;

/**
 * 
 * @author xeraph
 * @since 0.9
 */
public interface IndexConfigSpec {
	String getKey();

	boolean isRequired();

	String getName();

	String getDescription();

	/**
	 * @since 2.2.0
	 */
	String getName(Locale locale);

	/**
	 * @since 2.2.0
	 */
	String getDescription(Locale locale);
}