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
