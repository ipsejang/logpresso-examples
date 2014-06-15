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

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.araqne.logdb.AbstractQueryCommandParser;
import org.araqne.logdb.QueryCommand;
import org.araqne.logdb.QueryContext;
import org.araqne.logdb.QueryParseException;
import org.araqne.logdb.QueryParserService;

import com.logpresso.example.customsyntax.BlacklistQuery.Mode;

@Component(name = "blacklist-query-parser")
public class BlacklistQueryParser extends AbstractQueryCommandParser {
	@Requires
	private QueryParserService queryParserService;

	@Requires
	private BlacklistService blacklistService;

	@Validate
	public void start() {
		queryParserService.addCommandParser(this);
	}

	@Invalidate
	public void stop() {
		if (queryParserService != null)
			queryParserService.removeCommandParser(this);
	}

	@Override
	public String getCommandName() {
		return "blacklist";
	}

	@Override
	public QueryCommand parse(QueryContext context, String commandString) {
		BlacklistQuery.Mode mode;
		String s = commandString.substring(getCommandName().length()).trim();
		if (s.equals("query"))
			mode = Mode.Query;
		else if (s.equals("add"))
			mode = Mode.Add;
		else if (s.equals("remove"))
			mode = Mode.Remove;
		else if (s.equals("match"))
			mode = Mode.Match;
		else
			throw new QueryParseException("invalid-blacklist-mode", -1);

		return new BlacklistQuery(blacklistService, mode);
	}

}
