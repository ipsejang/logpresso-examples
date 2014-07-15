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
package com.logpresso.example.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.araqne.api.Script;
import org.araqne.api.ScriptArgument;
import org.araqne.api.ScriptContext;
import org.araqne.api.ScriptUsage;
import org.logpresso.jdbc.JdbcProfile;
import org.logpresso.jdbc.JdbcProfileRegistry;

public class JdbcConsoleScript implements Script {

	private JdbcProfileRegistry jdbcProfileRegistry;
	private ScriptContext context;

	public JdbcConsoleScript(JdbcProfileRegistry jdbcProfileRegistry) {
		this.jdbcProfileRegistry = jdbcProfileRegistry;
	}

	@Override
	public void setScriptContext(ScriptContext context) {
		this.context = context;
	}

	public void profiles(String[] args) {
		context.println("JDBC Profiles");
		context.println("---------------");

		for (JdbcProfile profile : jdbcProfileRegistry.getProfiles()) {
			context.println(profile);
		}
	}

	@ScriptUsage(description = "run jdbc console", arguments = { @ScriptArgument(name = "profile name", type = "string", description = "jdbc profile name") })
	public void console(String[] args) throws InterruptedException {
		String name = args[0];
		JdbcProfile profile = jdbcProfileRegistry.getProfile(name);
		if (profile == null) {
			context.println("profile [" + name + "] not found");
			return;
		}

		while (true) {
			context.print("sql@" + name + "> ");
			String sql = readQueryString();
			runSql(profile, sql);
		}
	}

	private String readQueryString() throws InterruptedException {
		List<String> lines = new ArrayList<String>();
		String queryString = context.readLine().trim();
		lines.add(queryString);
		if (queryString.endsWith("\\")) {
			while (true) {
				queryString = context.readLine().trim();
				lines.add(queryString);
				if (!queryString.endsWith("\\"))
					break;
			}
		}

		int i = 0;
		StringBuilder sb = new StringBuilder();
		for (String line : lines) {
			if (i++ != 0)
				sb.append(" ");

			if (line.endsWith("\\"))
				sb.append(line.substring(0, line.length() - 1));
			else
				sb.append(line);
		}

		return sb.toString();
	}

	private void runSql(JdbcProfile profile, String sql) {
		Connection conn = null;
		Statement stmt = null;
		long count = 0;
		try {
			// Use profile.connect() to get connection object. Direct
			// DriverManager.getConnection() will fail if jdbc driver package
			// did not imported by OSGi manifest.
			conn = profile.connect();

			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			rs.setFetchSize(2000);

			ResultSetMetaData metadata = rs.getMetaData();
			int columnCount = metadata.getColumnCount();

			while (rs.next()) {
				Map<String, Object> row = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					String key = metadata.getColumnLabel(i);
					Object value = rs.getObject(i);
					row.put(key, value);
				}

				context.println(row.toString());
				count++;
			}

		} catch (Throwable t) {
			context.println(t.toString());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}

		context.println("total " + count + " row(s)");
	}
}
