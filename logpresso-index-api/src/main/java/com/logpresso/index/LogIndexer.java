/*
 * Copyright 2013 Eediom Inc. All rights reserved.
 */
package com.logpresso.index;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.araqne.logstorage.LogStorage;

/**
 * @since 0.9
 * @author xeraph
 */
public interface LogIndexer {
	void createIndex(LogIndexSchema config);

	void dropIndex(String tableName, String indexName);

	void dropAllIndexes(String tableName);

	Set<String> getIndexNames(String tableName);

	LogIndexSchema getIndexConfig(String tableName, String indexName);

	LogIndexCursor search(LogIndexQuery q) throws IOException;

	List<LogIndexerStatus> getIndexerStatuses();

	List<Date> getIndexedDays(String tableName, String indexName);

	List<BatchIndexingTask> getBatchIndexingTasks();

	BatchIndexingTask getBatchIndexingTask(String tableName, String indexName);

	File getIndexDirectory(String tableName, String indexName);

	Date getPurgeBaseline(String tableName, String indexName);

	void purge(String tableName, String indexName, Date fromDay, Date toDay);

	Set<String> getGlobalConfigKeys();

	String getGlobalConfig(String key);

	void setGlobalConfig(String key, String value);

	void unsetGlobalConfig(String key);
	
	LogStorage getStorage();

	IndexTokenizer newTokenizer(LogIndexSchema schema);

	List<TaskStatus> getBackgroundTasks();

	int enqueueBloomFilterExecutors(String tableName, String indexName, String minDayStr);
	
	int enqueueCompactedIndexBuilders(String tableName, String indexName, String minDayStr);

	void clearBackgroundTasksDone();

	void buildIndex(String tname, String iname, Date minDay, Date maxDay);
}
