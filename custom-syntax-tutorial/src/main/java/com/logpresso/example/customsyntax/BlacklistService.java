package com.logpresso.example.customsyntax;

import java.util.Set;

public interface BlacklistService {
	Set<String> getAll();

	boolean contains(String ip);

	void add(String ip);

	void remove(String ip);
}
