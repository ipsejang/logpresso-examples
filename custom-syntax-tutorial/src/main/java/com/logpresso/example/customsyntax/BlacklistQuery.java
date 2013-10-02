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

import org.araqne.logdb.LogMap;
import org.araqne.logdb.LogQueryCommand;

public class BlacklistQuery extends LogQueryCommand {
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
	public void start() {
		if (mode != Mode.Query)
			return;

		try {
			status = Status.Running;

			for (String ip : blacklistService.getAll()) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("ip", ip);
				write(new LogMap(m));
			}

			eof(false);
		} catch (Throwable t) {
			eof(true);
		}
	}

	@Override
	public void push(LogMap m) {
		String ip = (String) m.get("ip");
		if (ip == null)
			return;

		if (mode == Mode.Match) {
			if (blacklistService.contains(ip))
				write(m);
		} else if (mode == Mode.Add) {
			blacklistService.add(ip);
			write(m);
		} else if (mode == Mode.Remove) {
			blacklistService.remove(ip);
			write(m);
		}
	}

	@Override
	public boolean isReducer() {
		return false;
	}
}
