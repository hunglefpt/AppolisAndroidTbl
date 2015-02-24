/**
 * Scanner Settings Debug tracing
 * (c) 2011 Socket Mobile, inc.
 */
package com.appolis.scan;

import android.util.Log;

/**
 * @author HoangNH11
 * 
 */
public final class Debug {
	public static final int kLevelTrace=1;
	public static final int kLevelWarning=2;
	public static final int kLevelError=3;
	private static String kTag="SingleEntry";
	public static void MSG(int level,String expression){
		if(level==Debug.kLevelTrace)
			Log.d(kTag,expression);
		else if (level==Debug.kLevelWarning)
			Log.w(kTag, expression);
		else if(level==Debug.kLevelError)
			Log.e(kTag,expression);
		else
			Log.v(kTag,expression);
	}
}
