/**
 * Name: $RCSfile: ObjectSettingLanguage.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.entities;

import java.io.Serializable;

public class ObjectSettingLanguage implements Serializable {
	/**
	 * variable declaration
	 */
	private static final long serialVersionUID = 1L;
	private String Key;
	private String Value;

	public String getKey() {
		return Key;
	}

	public void setKey(String key) {
		Key = key;
	}

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}
}
