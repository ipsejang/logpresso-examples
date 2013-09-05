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
package com.logpresso.example.query;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.araqne.logdb.client.LogCursor;
import org.araqne.logdb.client.LogDbClient;
import org.araqne.logdb.client.LogQuery;

public class PagingQueryExample {
	private static final int PAGE_SIZE = 5000;

	public static void main(String[] args) throws IOException {
		long begin = System.currentTimeMillis();
		LogDbClient client = null;
		LogCursor cursor = null;
		long count = 0;
		int queryId = 0;

		try {
			client = new LogDbClient();
			client.connect("localhost", 8888, "araqne", "");

			queryId = client.createQuery("table limit=100000 iis");
			client.startQuery(queryId);

			long last = 0;

			while (true) {
				LogQuery query = client.getQuery(queryId);
				String status = query.getStatus();
				long loaded = query.getLoadedCount();

				if (count == loaded && (status.equals("Ended") || status.equals("Cancelled")))
					break;

				int limit = (int) (loaded - last);
				if (limit <= 0)
					continue;

				limit = Math.min(PAGE_SIZE, limit);

				Map<String, Object> resultSet = client.getResult(queryId, last, limit);

				@SuppressWarnings("unchecked")
				List<Map<String, Object>> logs = (List<Map<String, Object>>) resultSet.get("result");

				for (Map<String, Object> log : logs) {
					long id = (Long) log.get("_id");
					String line = (String) log.get("line");
					System.out.println(id + ":" + line);
				}

				count += logs.size();
				last = last + logs.size();
			}
		} finally {
			if (cursor != null)
				cursor.close();

			if (client != null) {
				client.removeQuery(queryId);
				client.close();
			}

			long end = System.currentTimeMillis();
			System.out.println("total " + count + " rows in " + (end - begin) + " ms");
		}
	}
}
