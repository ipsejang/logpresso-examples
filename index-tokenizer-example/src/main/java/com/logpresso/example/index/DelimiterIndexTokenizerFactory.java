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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;

import com.logpresso.index.IndexConfigSpec;
import com.logpresso.index.IndexTokenizer;
import com.logpresso.index.IndexTokenizerFactory;
import com.logpresso.index.IndexTokenizerRegistry;

@Component(name = "logpresso-example-delimiter-index-tokenizer")
public class DelimiterIndexTokenizerFactory implements IndexTokenizerFactory {

	@Requires
	private IndexTokenizerRegistry registry;

	@Validate
	public void start() {
		registry.registerFactory(this);
	}

	@Invalidate
	public void stop() {
		if (registry != null)
			registry.unregisterFactory(this);
	}

	@Override
	public String getName() {
		return "delimiter-example";
	}

	@Override
	public List<IndexConfigSpec> getConfigSpecs() {
		IndexConfigSpec delimiters = new StringConfigSpec("delimiters", "delimiter chars", "split line by delimiter chars", true);
		return Arrays.asList(delimiters);
	}

	@Override
	public IndexTokenizer newIndexTokenizer(Map<String, String> config) {
		String delimiters = config.get("delimiters");
		return new DelimiterIndexTokenizer(delimiters);
	}

	@Override
	public String toString() {
		return "*** sample delimiter index tokenizer ***";
	}

}
