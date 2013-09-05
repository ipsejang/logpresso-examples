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

import org.junit.Test;
import static org.junit.Assert.*;

public class LoadAvgParserTest {
	@Test
	public void testParse() {
		String line = "0.01 0.02 0.03 1/76 13435";
		Map<String, Object> log = new HashMap<String, Object>();
		log.put("line", line);
		Map<String, Object> m = new LoadAvgParser(null).parse(log);
		assertEquals(0.01f, (Double) m.get("avg_1m"), 0.0001f);
		assertEquals(0.02f, (Double) m.get("avg_5m"), 0.0001f);
		assertEquals(0.03f, (Double) m.get("avg_15m"), 0.0001f);
		assertEquals(1, m.get("running_threads"));
		assertEquals(76, m.get("scheduled_threads"));
		assertEquals(13435, m.get("next_pid"));
	}

	@Test
	public void testCustomFields() {
		String line = "0.01 0.02 0.03 1/76 13435";
		Map<String, Object> log = new HashMap<String, Object>();
		log.put("line", line);
		Map<String, Object> m = new LoadAvgParser("a,b,c,d,e,f").parse(log);
		assertEquals(0.01f, (Double) m.get("a"), 0.0001f);
		assertEquals(0.02f, (Double) m.get("b"), 0.0001f);
		assertEquals(0.03f, (Double) m.get("c"), 0.0001f);
		assertEquals(1, m.get("d"));
		assertEquals(76, m.get("e"));
		assertEquals(13435, m.get("f"));
	}
}
