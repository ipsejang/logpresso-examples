package com.logpresso.index;

public class LogIndexQueryQualifier {

	private final String tableName;
	private final String indexName;

	public LogIndexQueryQualifier(String tableName, String indexName) {
		this.tableName = tableName;
		this.indexName = indexName;
	}

	@Override
	public String toString() {
		return "[tableName=" + tableName + ", indexName=" + indexName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((indexName == null) ? 0 : indexName.hashCode());
		result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
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
		LogIndexQueryQualifier other = (LogIndexQueryQualifier) obj;
		if (indexName == null) {
			if (other.indexName != null)
				return false;
		} else if (!indexName.equals(other.indexName))
			return false;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		return true;
	}

	public String getTableName() {
		return tableName;
	}

}
