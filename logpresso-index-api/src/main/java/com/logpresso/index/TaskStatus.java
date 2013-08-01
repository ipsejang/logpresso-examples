package com.logpresso.index;

public interface TaskStatus {
	String getDescription();

	void setCompletionStatus(boolean succeeded, Throwable t);

	void setRunning();

	int getTaskId();

	boolean isDone();
}