/*
 * Copyright 2013 Eediom Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.logpresso.example.index;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.logpresso.index.IndexTokenizer;

public class DelimiterIndexTokenizer implements IndexTokenizer {

	private String delimiters;

	public DelimiterIndexTokenizer(String delimiters) {
		this.delimiters = delimiters;
	}

	@Override
	public Set<String> tokenize(Map<String, Object> m) {
		Set<String> tokens = new HashSet<String>();

		String line = (String) m.get("line");
		if (line == null)
			return tokens;

		StringTokenizer tok = new StringTokenizer(line, delimiters);
		while (tok.hasMoreTokens())
			tokens.add(tok.nextToken());

		return tokens;
	}

}
