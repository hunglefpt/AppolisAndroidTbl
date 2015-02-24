/**
 * Name: $RCSfile: Logger.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.utilities;

import android.util.Log;

/**
 * Create the common logging mechanism for the whole application.
 * 
 * @author HoangNH11
 */
public class Logger
{
    private static final String TAG = "AppolisAndroid";
    private static final String DEFAULT_STRING_NULL = "null";

    /**
     * print out a debug with message
     */
    public static void debug(String message)
    {
        if (message == null)
        {
            // println does not handle null message while logging.
            message = DEFAULT_STRING_NULL;
        }
        
        Log.d(TAG, message);
    }

    /**
     * print out a debug with tag name and message
     */
    public static void debug(String tagName, String message)
    {
        if (message == null)
        {
            // println does not handle null message while logging.
            message = DEFAULT_STRING_NULL;
        }

        if (StringUtils.isBlank(tagName))
        {
            tagName = TAG;
        }
        
        Log.d(tagName, message);
    }

    /**
     * print out an info with message
     */
    public static void info(String message)
    {
        if (message == null)
        {
            // println does not handle null message while logging.
            message = DEFAULT_STRING_NULL;
        }
        
        Log.i(TAG, message);
    }
    
    /**
     * print out an info with tag name and message
     */
    public static void info(String tagName, String message)
    {
        if (message == null)
        {
            // println does not handle null message while logging.
            message = DEFAULT_STRING_NULL;
        }

        if (StringUtils.isBlank(tagName))
        {
            tagName = TAG;
        }
        
        Log.i(tagName, message);
    }

    /**
     * print out a warning with message
     */
    public static void warn(String message)
    {
        if (message == null)
        {
            // println does not handle null message while logging.
            message = DEFAULT_STRING_NULL;
        }
        
        Log.w(TAG, message);
    }

    /**
     * print out a warning with tag name and message
     */
    public static void warn(String tagName, String message)
    {
        if (message == null)
        {
            // println does not handle null message while logging.
            message = DEFAULT_STRING_NULL;
        }

        if (StringUtils.isBlank(tagName))
        {
            tagName = TAG;
        }
        
        Log.w(tagName, message);
    }

    /**
     * print out an error with message
     */
    public static void error(String message)
    {
        if (message == null)
        {
            // println does not handle null message while logging.
            message = DEFAULT_STRING_NULL;
        }
        
        Log.e(TAG, message);
    }
    
    /**
     * print out an error with tag name and message
     */
    public static void error(String tagName, String message)
    {
        if (message == null)
        {
            // println does not handle null message while logging.
            message = DEFAULT_STRING_NULL;
        }

        if (StringUtils.isBlank(tagName))
        {
            tagName = TAG;
        }
        
        Log.e(tagName, message);
    }

    /**
     * print out Throwable
     */
    public static void error(Throwable ex)
    {
    	Log.e(TAG, GlobalParams.BLANK_CHARACTER, ex);
    }
}
