package com.logpresso.index;

import java.util.List;

import org.araqne.logdb.query.expr.Expression;

public interface BoundedExpression extends Expression {
	List<String> getQualifiers();
	
	String toExpressionString();
}
