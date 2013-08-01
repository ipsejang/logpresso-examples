/*
 * Copyright 2013 Eediom Inc. All rights reserved.
 */
package com.logpresso.index;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.araqne.confdb.CollectionName;

/**
 * @since 0.9
 * @author xeraph
 */
@CollectionName("index")
public class LogIndexSchema {
	public static final float BF_LV1_DEFAULT_ERROR_RATE = 0.02f;
	public static final int BF_LV1_DEFAULT_CAPACITY = 10000000;
	public static final float BF_LV0_DEFAULT_ERROR_RATE = 0.001f;
	public static final int BF_LV0_DEFAULT_CAPACITY = 1250000;

	// index id (global unique)
	private int id;

	// associated table name
	private String tableName;

	// unique per each log table (not global unique)
	private String indexName;

	// need to build old index when create this index?
	private boolean buildPastIndex;

	// min indexing date, inclusive (yyyy-MM-dd only)
	private Date minIndexDay;

	// index tokenizer name (profile has tokenizer configuration)
	private String tokenizerName;

	// index partition base path (optional)
	private String basePath;

	private Map<String, String> tokenizerConfigs;

	private boolean useBloomFilter;

	private int bloomFilterCapacity0 = BF_LV0_DEFAULT_CAPACITY;
	private double bloomFilterErrorRate0 = BF_LV0_DEFAULT_ERROR_RATE;
	private int bloomFilterCapacity1 = BF_LV1_DEFAULT_CAPACITY;
	private double bloomFilterErrorRate1 = BF_LV1_DEFAULT_ERROR_RATE;
	private Date maxIndexDay;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogIndexSchema other = (LogIndexSchema) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public Date getMinIndexDay() {
		return minIndexDay;
	}

	public void setMinIndexDay(Date minIndexDay) {
		this.minIndexDay = minIndexDay;
	}

	public Date getMaxIndexDay() {
		return this.maxIndexDay; 
	}
	
	public void setMaxIndexDay(Date maxIndexDay) {
		this.maxIndexDay = maxIndexDay;
	}

	public boolean isBuildPastIndex() {
		return buildPastIndex;
	}

	public void setBuildPastIndex(boolean buildPastIndex) {
		this.buildPastIndex = buildPastIndex;
	}

	public String getTokenizerName() {
		return tokenizerName;
	}

	public void setTokenizerName(String tokenizerName) {
		this.tokenizerName = tokenizerName;
	}

	public Map<String, String> getTokenizerConfigs() {
		return tokenizerConfigs;
	}

	public void setTokenizerConfigs(Map<String, String> tokenizerConfigs) {
		this.tokenizerConfigs = tokenizerConfigs;
	}

	public String getBasePath() {
		return basePath;
	}

	public File getBasePathFile() {
		if (basePath == null)
			return null;
		return new File(basePath);
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public boolean isUseBloomFilter() {
		return useBloomFilter;
	}

	public void setUseBloomFilter(boolean useBloomFilter) {
		this.useBloomFilter = useBloomFilter;
	}

	public int[] getBloomFilterCapacities() {
		return new int[] { bloomFilterCapacity0, bloomFilterCapacity1 };
	}

	public double[] getBloomFilterErrorRates() {
		return new double[] { bloomFilterErrorRate0, bloomFilterErrorRate1 };
	}

	public int getBloomFilterCapacity0() {
		return bloomFilterCapacity0;
	}

	public void setBloomFilterCapacity0(int bloomFilterCapacity0) {
		this.bloomFilterCapacity0 = bloomFilterCapacity0;
	}

	public double getBloomFilterErrorRate0() {
		return bloomFilterErrorRate0;
	}

	public void setBloomFilterErrorRate0(double bloomFilterErrorRate0) {
		this.bloomFilterErrorRate0 = bloomFilterErrorRate0;
	}

	public int getBloomFilterCapacity1() {
		return bloomFilterCapacity1;
	}

	public void setBloomFilterCapacity1(int bloomFilterCapacity1) {
		this.bloomFilterCapacity1 = bloomFilterCapacity1;
	}

	public double getBloomFilterErrorRate1() {
		return bloomFilterErrorRate1;
	}

	public void setBloomFilterErrorRate1(double bloomFilterErrorRate1) {
		this.bloomFilterErrorRate1 = bloomFilterErrorRate1;
	}

	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		String min = "unbound";
		if (minIndexDay != null)
			min = "from " + dateFormat.format(minIndexDay);

		String bloomFilterConfig = "bloomfilter=" + useBloomFilter;
		if (useBloomFilter) {
			bloomFilterConfig += "[lv0: " + bloomFilterCapacity0 + ", " + bloomFilterErrorRate0 + ", ";
			bloomFilterConfig += "lv1: " + bloomFilterCapacity1 + ", " + bloomFilterErrorRate1 + "]";
		}

		return "id=" + id + ", table=" + tableName + ", index=" + indexName + ", period (" + min + "), " + bloomFilterConfig
				+ ", tokenizer=" + tokenizerName + ", base path=" + basePath;
	}

}
