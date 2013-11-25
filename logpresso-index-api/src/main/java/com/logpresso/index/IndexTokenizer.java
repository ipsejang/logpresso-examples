/*
 * Copyright 2013 Eediom Inc. All rights reserved.
 */
package com.logpresso.index;

import java.util.Map;
import java.util.Set;


/**
 * @since 0.9
 * @author xeraph
 */
public interface IndexTokenizer {
	Set<String> tokenize(Map<String, Object> m);
	
	Set<String> tokenize(String s);
}
