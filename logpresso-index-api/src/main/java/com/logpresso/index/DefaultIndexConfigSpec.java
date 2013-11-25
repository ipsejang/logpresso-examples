package com.logpresso.index;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @since 2.2.0
 * @author xeraph
 * 
 */
public class DefaultIndexConfigSpec implements IndexConfigSpec {

	private String key;
	private boolean required;
	private Map<Locale, String> names = new HashMap<Locale, String>();
	private Map<Locale, String> descriptions = new HashMap<Locale, String>();

	@Override
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	@Override
	public String getName() {
		return names.get(Locale.ENGLISH);
	}

	public void setName(String name) {
		this.names.put(Locale.ENGLISH, name);
	}

	public void setName(Locale locale, String name) {
		this.names.put(locale, name);
	}

	@Override
	public String getDescription() {
		return descriptions.get(Locale.ENGLISH);
	}

	public void setDescription(String description) {
		this.descriptions.put(Locale.ENGLISH, description);
	}

	public void setDescription(Locale locale, String description) {
		this.descriptions.put(locale, description);
	}

	@Override
	public String getName(Locale locale) {
		if (locale == null)
			locale = Locale.ENGLISH;

		String name = names.get(locale);
		if (name == null)
			return names.get(Locale.ENGLISH);
		return name;
	}

	@Override
	public String getDescription(Locale locale) {
		if (locale == null)
			locale = Locale.ENGLISH;

		String description = descriptions.get(locale);
		if (description == null)
			return descriptions.get(Locale.ENGLISH);
		return description;
	}
}
