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
package com.logpresso.example.customsyntax;

import java.util.HashMap;
import java.util.Map;

import org.araqne.logdb.DriverQueryCommand;
import org.araqne.logdb.Row;

public class BlacklistQuery extends DriverQueryCommand {
	private Mode mode;
	private BlacklistService blacklistService;

	public enum Mode {
		Query, Add, Remove, Match
	}

	public BlacklistQuery(BlacklistService blacklistService, Mode mode) {
		this.blacklistService = blacklistService;
		this.mode = mode;
	}

	@Override
	public String getName() {
		return "blacklist";
	}

	@Override
	public void run() {
		if (mode != Mode.Query)
			return;

		for (String ip : blacklistService.getAll()) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("ip", ip);
			pushPipe(new Row(m));
		}
	}

	@Override
	public boolean isDriver() {
		return mode == Mode.Query;
	}

	@Override
	public void onPush(Row row) {
		String ip = (String) row.get("ip");
		if (ip == null)
			return;

		if (mode == Mode.Match) {
			if (blacklistService.contains(ip))
				pushPipe(row);
		} else if (mode == Mode.Add) {
			blacklistService.add(ip);
			pushPipe(row);
		} else if (mode == Mode.Remove) {
			blacklistService.remove(ip);
			pushPipe(row);
		}
	}

	@Override
	public String toString() {
		return "blacklist " + mode.toString().toLowerCase();
	}
}
