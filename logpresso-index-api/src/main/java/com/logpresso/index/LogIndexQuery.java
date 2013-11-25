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
	private BoundedExpression expr;
	private Long limit;
	private Long offset;
	private boolean idOnly;
	private String parserName;
	private Collection<String> accessibleTables;
	private boolean tokenizeTerm = false;

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

	public void setExpr(BoundedExpression expr) {
		this.expr = expr;
	}

	public BoundedExpression getExpr() {
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

	public String getParserName() {
		return parserName;
	}

	public void setParserName(String parserName) {
		this.parserName = parserName;
	}

	public void setAccesibleTables(Collection<String> tables) {
		this.accessibleTables = tables;
	}

	public Collection<String> getAccessibleTables() {
		return this.accessibleTables;
	}
	
	public boolean getTokenizeTerm() {
		return this.tokenizeTerm;
	}

	public void setTokenizeTerm(boolean b) {
		this.tokenizeTerm = true;
	}
}
