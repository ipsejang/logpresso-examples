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
package com.logpresso.example.parser;

import java.util.HashMap;
import java.util.Map;

import org.araqne.log.api.V1LogParser;

public class LoadAvgParser extends V1LogParser {

	private String[] names = new String[] { "avg_1m", "avg_5m", "avg_15m", "running_threads", "scheduled_threads", "next_pid" };

	public LoadAvgParser(String fields) {
		if (fields != null) {
			String[] tokens = fields.split(",");
			for (int i = 0; i < tokens.length && i < 6; i++) {
				names[i] = tokens[i].trim();
			}
		}
	}

	@Override
	public Map<String, Object> parse(Map<String, Object> params) {
		String line = (String) params.get("line");
		if (line == null)
			return params;

		try {
			String[] tokens = line.split(" ");

			Map<String, Object> m = new HashMap<String, Object>();
			m.put(names[0], Double.parseDouble(tokens[0]));
			m.put(names[1], Double.parseDouble(tokens[1]));
			m.put(names[2], Double.parseDouble(tokens[2]));

			String[] pair = tokens[3].split("/");
			m.put(names[3], Integer.parseInt(pair[0]));
			m.put(names[4], Integer.parseInt(pair[1]));
			m.put(names[5], Integer.parseInt(tokens[4]));

			return m;
		} catch (Throwable t) {
			return params;
		}
	}

}
