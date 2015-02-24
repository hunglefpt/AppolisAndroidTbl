/**
 * Name: $RCSfile: LanguagePreferences.java,v $
 * Version: $Revision: 1.20 $
 * Date: $Date: 2013/03/19 02:48:48 $
 *
 * Copyright (C) 2013 FPT Software, Inc. All rights reserved.
 */
package com.appolis.common;

import com.appolis.utilities.GlobalParams;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * @author hoangnh11
 */
public class LanguagePreferences {
	private static final String LANGUAGE_SHARED_PREFS = LanguagePreferences.class.getSimpleName(); // Name of the file -.xml
	private static final String LANGUAGE_MODE_PREFS = "APPOLIS_LANGUAGE_MODE_PREFS";
	private static SharedPreferences _sharedPrefs;
	private Editor _prefsEditor;
	
	@SuppressWarnings("static-access")
	public LanguagePreferences(Context context) {
		this._sharedPrefs = context.getSharedPreferences(LANGUAGE_SHARED_PREFS, Activity.MODE_PRIVATE);
		this._prefsEditor = _sharedPrefs.edit();
	}

	public void savePreference(String key, String value){
		_prefsEditor.putString(key, value);
		_prefsEditor.commit();
	}
	
	public String getPreferencesString(String key, String valueExpect){
		return _sharedPrefs.getString(key, valueExpect);
	}
	
	public String getLanguageMode() {
		return _sharedPrefs.getString(LANGUAGE_MODE_PREFS, GlobalParams.LANGUAGE_MODE_DEFAULT);
	}

	public void saveLanguageMode(String languageMode) {
		_prefsEditor.putString(LANGUAGE_MODE_PREFS, languageMode);
		_prefsEditor.commit();
	}
}
