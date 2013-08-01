package com.logpresso.index;

import java.util.Set;

public interface BooleanIndexCursor extends LogIndexCursor {
	Set<LogIndexQueryQualifier> getQualifiers();

	BooleanIndexCursor negate();
	
	BooleanIndexCursor optimize();
}
