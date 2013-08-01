/*
 * Copyright 2013 Eediom Inc. All rights reserved.
 */
package com.logpresso.index;

import java.util.Collection;
import java.util.Date;

import org.araqne.logdb.query.expr.Expression;

/**
 * @since 0.9
 * @author xeraph
 */
public class LogIndexQuery {
	private Date minDay;
	private Date maxDay;
	private Expression expr;
	private Long limit;
	private Long offset;
	private boolean idOnly;
	private Collection<String> accessibleTables;

	public Date getMinDay() {
		return minDay;
	}

	public void setMinDay(Date minDay) {
		this.minDay = minDay;
	}

	public Date getMaxDay() {
		return maxDay;
	}

	public void setMaxDay(Date maxDay) {
		this.maxDay = maxDay;
	}

	public void setExpr(Expression expr) {
		this.expr = expr;
	}

	public Expression getExpr() {
		return expr;
	}

	public void setLimit(Long limit) {
		this.limit = limit;
	}

	public Long getLimit() {
		return this.limit;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public Long getOffset() {
		return this.offset;
	}

	public boolean isIdOnly() {
		return idOnly;
	}

	public void setIdOnly(boolean idOnly) {
		this.idOnly = idOnly;
	}
	
	public void setAccesibleTables(Collection<String> tables) {
		this.accessibleTables = tables;
	}
	
	public Collection<String> getAccesibleTables() {
		return this.accessibleTables;
	}
}
