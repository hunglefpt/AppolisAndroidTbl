/**
 * Name: $RCSfile: SharedPreferenceManager.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */

package com.appolis.utilities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author HoangNH11
 * Store and retrieve data into/from Preference
 */
public final class SharedPreferenceManager
{
    private static final String CONFIGURATION_NAME = "APL_CONFIG";
    private SharedPreferences _shareRefs = null;

    public SharedPreferenceManager(Activity active)
    {
        _shareRefs = active.getSharedPreferences(CONFIGURATION_NAME, Activity.MODE_PRIVATE);
    }

    /**
     * @param key
     * @param value
     */
    public void saveDouble(String key, double value)
    {
        String dValue = String.valueOf(value);
        Editor editor = _shareRefs.edit();
        editor.putString(key, dValue);
        editor.commit();
    }

    /**
     * @param key
     * @param defVa
     * @return
     */
    public double getDouble(String key, double defVa)
    {
        String strDefVa = String.valueOf(defVa);
        String dValue = _shareRefs.getString(key, strDefVa);
        return (dValue.equals(strDefVa) == true) ? defVa
            : Double.valueOf(dValue);
    }

    /**
     * @param key
     * @param value
     */
    public void saveFloat(String key, float value)
    {
        Editor editor = _shareRefs.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * @param key
     * @param defVa
     * @return
     */
    public float getFloat(String key, float defVa)
    {
        return _shareRefs.getFloat(key, defVa);
    }

    /**
     * @param key
     * @param value
     */
    public void saveBoolean(String key, boolean value)
    {
        Editor editor = _shareRefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * @param key
     * @param defVa
     * @return
     */
    public boolean getBoolean(String key, boolean defVa)
    {
        return _shareRefs.getBoolean(key, defVa);
    }
    
    /**
     * @param key
     * @param value
     */
    public void saveString(String key, String value)
    {
        Editor editor = _shareRefs.edit();
        editor.putString(key, value);
        editor.commit();
    }
    
    /**
     * @param key1
     * @param key2
     * @param value1
     * @param value2
     */
    public void saveStringSetting(String key1, String key2, String value1, String value2)
    {
        Editor editor = _shareRefs.edit();
        editor.putString(key1, value1);
        editor.commit();
    }

    /**
     * @param key
     * @param defVa
     * @return
     */
    public String getString(String key, String defVa)
    {
        return _shareRefs.getString(key, defVa);
    }

    public void saveInt(String key, int value)
    {

        Editor editor = _shareRefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key, int defVa)
    {
        return _shareRefs.getInt(key, defVa);
    }
    
    public void saveLong(String key, long value)
    {

        Editor editor = _shareRefs.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public long getLong(String key, long defVa)
    {
        return _shareRefs.getLong(key, defVa);
    }

    /**
     * clear all sharedPreferences
     */
    public void clearAll()
    {
        Editor editor = _shareRefs.edit();
        editor.clear();
        editor.commit();
    }
    
    /**
     * remove value sharePreference with key
     * @param key
     */
    public void remove(String key)
    {
        Editor editor = _shareRefs.edit();
        editor.remove(key);
        editor.commit();
    }
}