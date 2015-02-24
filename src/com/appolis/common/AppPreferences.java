/**
 * Name: $RCSfile: AppPreferences.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.common;

import com.appolis.utilities.GlobalParams;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author hoangnh11
 * Handling Preferences
 */
public class AppPreferences {
	public static final String KEY_PREFS_USERNAME = "username";
	public static final String KEY_PREFS_PIN = "pin";
	public static final String KEY_PREFS_SITE = "site";
	public static final String KEY_URL_FIRST_LOGIN = "key_url_first_login";
	public static final String KEY_TIP_LOGIN = "key tip Login";
	public static final String KEY_TIP_FIRST_LOGIN = "key tip First login";
	public static final String KEY_TIP_MAIN = "key tip Main";
	public static final String KEY_TIP_MAIN_TWO = "key tip Main two";
	public static final String KEY_SOCKET_CONNECT = "key socket connect";
	public static final String KEY_TIP_USER_PROFILE = "key tip User profile";
	
	private static final String APP_SHARED_PREFS = AppPreferences.class.getSimpleName(); // Name of the file -.xml
	private static SharedPreferences _sharedPrefs;
	private Editor _prefsEditor;	

	@SuppressWarnings("static-access")
	public AppPreferences(Context context) {
		this._sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
		this._prefsEditor = _sharedPrefs.edit();
	}
	
	public void savePreference(String key, String value){
		_prefsEditor.putString(key, value);
		_prefsEditor.commit();
	}
	
	public String getPreferencesString(String key, String valueExpect){
		return _sharedPrefs.getString(key, valueExpect);
	}
	
	public String getUsername() {
		return _sharedPrefs.getString(KEY_PREFS_USERNAME, GlobalParams.BLANK_CHARACTER);
	}

	public void saveUsername(String Username) {
		_prefsEditor.putString(KEY_PREFS_USERNAME, Username);
		_prefsEditor.commit();
	}

	public String getPIN() {
		return _sharedPrefs.getString(KEY_PREFS_PIN, GlobalParams.BLANK_CHARACTER);
	}

	public void savePIN(String PIN) {
		_prefsEditor.putString(KEY_PREFS_PIN, PIN);
		_prefsEditor.commit();
	}
	
	public String getSite() {
		return _sharedPrefs.getString(KEY_PREFS_SITE, GlobalParams.BLANK_CHARACTER);
	}

	public void saveSite(String Site) {
		_prefsEditor.putString(KEY_PREFS_SITE, Site);
		_prefsEditor.commit();
	}
	
	public static String getURLFirstLogin() {
		//return _sharedPrefs.getString(KEY_URL_FIRST_LOGIN, "http://demo.appolis.com/ioswebapi");
//		return _sharedPrefs.getString(KEY_URL_FIRST_LOGIN, "http://withoutwire.appolis.com/webapi_appolis");
//		return _sharedPrefs.getString(KEY_URL_FIRST_LOGIN, "http://10.16.36.95/webapi_v7");
		return _sharedPrefs.getString(KEY_URL_FIRST_LOGIN, "http://withoutwire.appolis.com/webapi_android");
	}

	public void saveURLFirstLogin(String URL) {
		_prefsEditor.putString(KEY_URL_FIRST_LOGIN, URL);
		_prefsEditor.commit();
	}
	
	public String getTipLogin() {
		return _sharedPrefs.getString(KEY_TIP_LOGIN, GlobalParams.BLANK_CHARACTER);
	}

	public void saveTipLogin(String Site) {
		_prefsEditor.putString(KEY_TIP_LOGIN, Site);
		_prefsEditor.commit();
	}
	
	public String getTipFirstLogin() {
		return _sharedPrefs.getString(KEY_TIP_FIRST_LOGIN, GlobalParams.BLANK_CHARACTER);
	}

	public void saveTipFirstLogin(String Site) {
		_prefsEditor.putString(KEY_TIP_FIRST_LOGIN, Site);
		_prefsEditor.commit();
	}
	
	public String getTipMain() {
		return _sharedPrefs.getString(KEY_TIP_MAIN, GlobalParams.BLANK_CHARACTER);
	}

	public void saveTipMain(String Site) {
		_prefsEditor.putString(KEY_TIP_MAIN, Site);
		_prefsEditor.commit();
	}
	
	public String getTipMainTwo() {
		return _sharedPrefs.getString(KEY_TIP_MAIN_TWO, GlobalParams.BLANK_CHARACTER);
	}

	public void saveTipMainTwo(String Site) {
		_prefsEditor.putString(KEY_TIP_MAIN_TWO, Site);
		_prefsEditor.commit();
	}
	
	public String getTipUserProfile() {
		return _sharedPrefs.getString(KEY_TIP_USER_PROFILE, GlobalParams.BLANK_CHARACTER);
	}
	
	public void saveTipUserProfile(String Site) {
		_prefsEditor.putString(KEY_TIP_USER_PROFILE, Site);
		_prefsEditor.commit();
	}	
	
	public String getSocketConnect() {
		return _sharedPrefs.getString(KEY_SOCKET_CONNECT, GlobalParams.BLANK_CHARACTER);
	}

	public void saveSocketConnect(String Site) {
		_prefsEditor.putString(KEY_SOCKET_CONNECT, Site);
		_prefsEditor.commit();
	}
}