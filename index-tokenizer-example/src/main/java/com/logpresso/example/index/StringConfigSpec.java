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

import com.logpresso.index.IndexConfigSpec;

public class StringConfigSpec implements IndexConfigSpec {
	private String key;
	private String name;
	private String description;
	private boolean required;

	public StringConfigSpec(String key, String name, String description, boolean required) {
		this.key = key;
		this.name = name;
		this.description = description;
		this.required = required;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public boolean isRequired() {
		return required;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}
}
