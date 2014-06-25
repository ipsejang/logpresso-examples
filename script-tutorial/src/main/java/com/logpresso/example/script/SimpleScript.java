/*
 * Copyright 2014 Eediom Inc.
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
package com.logpresso.example.script;

import org.araqne.api.Script;
import org.araqne.api.ScriptArgument;
import org.araqne.api.ScriptContext;
import org.araqne.api.ScriptUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleScript implements Script {
	private final Logger logger = LoggerFactory.getLogger(SimpleScript.class);
	private ScriptContext context;
	private final SimpleService service;

	public SimpleScript(SimpleService service) {
		this.service = service;
	}

	@Override
	public void setScriptContext(ScriptContext context) {
		this.context = context;
	}
	
	public void getMessage(String[] args) {
		context.println("Simple Service Message : " + service.getMessage());
	}
	
	@ScriptUsage(description = "set simple service message")
	public void setMessage(String[] args) throws InterruptedException {
		context.print("Simple Service Message? ");
		String line = context.readLine().trim();
		service.setMessage(line);
		logger.info("message set : " + line);
		context.println("message set : " + line);
	}
	
	public void getInterval(String[] args) {
		context.println("Log write interval : " + service.getInterval());
	}
	
	@ScriptUsage(description = "set log interval", arguments = {
			@ScriptArgument(name = "interval", type = "int", description = "set log write interval in milliseconds") })
	public void setInterval(String[] args) {
		int interval = Integer.valueOf(args[0]);
		service.setInterval(interval);
		logger.info("interval set : " + interval);
		context.println("interval set : " + interval);
	}
	
	@ScriptUsage(description = "string argument example", arguments = {
		@ScriptArgument(name = "first", type = "string", description = "first argument"),
		@ScriptArgument(name = "first", type = "string", description = "first argument"),
		@ScriptArgument(name = "first", type = "string", description = "first argument") })
	public void dummpyScript(String[] args) {
		context.println("first - " + args[0]);
		context.println("second - " + args[1]);
		context.println("first - " + args[2]);
	}

}
