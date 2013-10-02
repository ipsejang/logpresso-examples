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
