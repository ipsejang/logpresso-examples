package com.logpresso.example.customsyntax;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;

@Component(name = "blacklist-service")
@Provides
public class BlacklistServiceImpl implements BlacklistService {
	private Set<String> set;

	public BlacklistServiceImpl() {
		set = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
	}

	@Override
	public Set<String> getAll() {
		return Collections.unmodifiableSet(set);
	}

	@Override
	public boolean contains(String ip) {
		return set.contains(ip);
	}

	@Override
	public void add(String ip) {
		set.add(ip);
	}

	@Override
	public void remove(String ip) {
		set.remove(ip);
	}

}
