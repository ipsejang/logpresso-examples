/*
 * Copyright 2013 Eediom Inc. All rights reserved.
 */
package com.logpresso.index;

public class IndexCacheConfig {
	private String key;
	private String value;

	public IndexCacheConfig(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return key + ": " + value;
	}

}
