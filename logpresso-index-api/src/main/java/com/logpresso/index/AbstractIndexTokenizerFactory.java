package com.logpresso.index;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @since 2.2.0
 * @author xeraph
 * 
 */
public abstract class AbstractIndexTokenizerFactory implements IndexTokenizerFactory {
	private Map<Locale, String> displayNames = new HashMap<Locale, String>();
	private Map<Locale, String> descriptions = new HashMap<Locale, String>();

	@Override
	public String getDisplayName(Locale locale) {
		String s = displayNames.get(locale);
		if (s == null)
			return displayNames.get(Locale.ENGLISH);
		return s;
	}

	@Override
	public String getDescription(Locale locale) {
		String s = descriptions.get(locale);
		if (s == null)
			return descriptions.get(Locale.ENGLISH);
		return s;
	}

	protected void setDisplayName(Locale locale, String displayName) {
		displayNames.put(locale, displayName);
	}

	protected void setDescription(Locale locale, String description) {
		descriptions.put(locale, description);
	}
}
