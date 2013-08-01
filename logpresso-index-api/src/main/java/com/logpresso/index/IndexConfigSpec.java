/*
 * Copyright 2013 Eediom Inc. All rights reserved.
 */
package com.logpresso.index;

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
}