/**
 * Name: $RCSfile: BuManagement.java,v $
 * Version: $Revision: 1.20 $
 * Date: $Date: 2013/03/19 02:48:48 $
 *
 * Copyright (C) 2013 FPT Software, Inc. All rights reserved.
 */

package com.appolis.utilities;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.appolis.androidtablet.R;


/**
 * Business management: include common methods
 * 
 * @author HoangNH11
 */
public final class BuManagement
{
    public final static BuManagement Instance = new BuManagement();
    private static String appVersionName;

    private BuManagement()
    {}

	/**
	 * decryption UserName and Password for login function
	 * @param data
	 * @return
	 */
	public static String decrypt(String data)
	{
		int map;
		String orig = "";
		String from[] = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
				"S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
				"m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6",
				"7", "8", "9", "0", };
		String to[] = { "b", "1", "G", "e", "4", "h", "d", "8", "9", "a", "A", "B", "D", "C", "c", "7", "F", "3", "E",
				"n", "0", "f", "2", "6", "g", "H", "J", "i", "I", "j", "5", "k", "K", "M", "L", "l", "m", "N", "o",
				"O", "u", "P", "Q", "X", "R", "r", "x", "S", "t", "U", "T", "p", "v", "q", "Y", "w", "V", "W", "s",
				"y", "Z", "z" };
		/* Decrypt Logic */
		for (int i = 0; i < data.length(); i++)
		{
			map = -1;

			for (int j = 0; j < 62; j++)
			{
				if (to[j].equals(data.substring(i, i + 1)))
				{

					map = j;
					orig = orig.trim() + from[j];

				}
			}
			
			if (map == -1)
			{
				orig = orig.trim() + data.substring(i, i + 1);

			}
		}
		return orig;
	}

	/**
	 * encryption UserName and Password for login function
	 * @param data
	 * @return
	 */
	public static String encrypt(String data)
	{
		String orig = "";
		String from[] = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
				"S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
				"m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6",
				"7", "8", "9", "0", };
		String to[] = { "b", "1", "G", "e", "4", "h", "d", "8", "9", "a", "A", "B", "D", "C", "c", "7", "F", "3", "E",
				"n", "0", "f", "2", "6", "g", "H", "J", "i", "I", "j", "5", "k", "K", "M", "L", "l", "m", "N", "o",
				"O", "u", "P", "Q", "X", "R", "r", "x", "S", "t", "U", "T", "p", "v", "q", "Y", "w", "V", "W", "s",
				"y", "Z", "z" };
		int i, map;

		for (i = 0; i < data.length(); i++)
		{
			map = -1;

			for (int j = 0; j < 62; j++)
			{
				if (from[j].equals(data.substring(i, i + 1)))
				{

					map = j;
					orig = orig.trim() + to[j];

				}
			}
			
			if (map == -1)
			{
				orig = orig.trim() + data.substring(i, i + 1);

			}
		}
		return orig;
	}
	
    /**
	 * expand view animation
	 * @param v
	 */
	public static void expand(final View v) {
	    v.measure(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	    final int measuredHeight = v.getMeasuredHeight();
	 
	    v.getLayoutParams().height = 0;
	    v.setVisibility(View.VISIBLE);
	    Animation a = new Animation()
	    {
	        @Override
	        protected void applyTransformation(float interpolatedTime, Transformation t) {
	            v.getLayoutParams().height = interpolatedTime == 1
	                    ? android.view.ViewGroup.LayoutParams.WRAP_CONTENT
	                    : (int)(measuredHeight * interpolatedTime);
	            v.requestLayout();
	        }
	 
	        @Override
	        public boolean willChangeBounds() {
	            return true;
	        }
	    };
	 
	    // 1dp per milliseconds
	    a.setDuration((int)(measuredHeight / v.getContext().getResources().getDisplayMetrics().density));
	    v.startAnimation(a);
	}
	
	/**
	 * collapse view animation 
	 * @param v
	 */
	public static void collapse(final View v) {
		final int initialHeight = v.getMeasuredHeight();

		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				if (interpolatedTime == 1) {
					v.setVisibility(View.GONE);
				} else {
					v.getLayoutParams().height = initialHeight
							- (int) (initialHeight * interpolatedTime);
					v.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		// 1dp per milliseconds
		a.setDuration((int) (initialHeight / v.getContext().getResources()
				.getDisplayMetrics().density));
		v.startAnimation(a);
	}
	
    /**
     * increase days in calendar
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days); // minus number would decrement the days
		return cal.getTime();
	}
    
    /**
     * compareDate
     * @param date1
     * @param date2
     * @return 1 if date1 after date2, 0 if equae, -1 if before
     */
    @SuppressWarnings("deprecation")
	public static int compareDate(Date date1, Date date2) {
        if (date1.getYear() == date2.getYear() &&
            date1.getMonth() == date2.getMonth() &&
            date1.getDate() == date2.getDate()) {
          return 0 ;
        } 
        else if (date1.getYear() < date1.getYear() ||
                 (date1.getYear() == date2.getYear() &&
                  date1.getMonth() < date2.getMonth()) ||
                 (date1.getYear() == date2.getYear() &&
                  date1.getMonth() == date2.getMonth() &&
                  date1.getDate() < date2.getDate())) {
          return -1 ;
       }
       else {
         return 1 ;
       }
    }
    
    /** 
     *  Returns a string that describes the number of days
     *  between dateOne and dateTwo.  
     *
     */ 

    public static int getDateDiff(Date dateOne, Date dateTwo)
    {
       long timeOne = dateOne.getTime();
       long timeTwo = dateTwo.getTime();
       long oneDay = 1000 * 60 * 60 * 24;
       int delta = (int) ((timeTwo - timeOne) / oneDay);

       if (delta > 0) {
           return delta;
       }
       else {
           delta *= -1;
           return delta;
       }
    }
   
    /**
	 * updateConnectedFlags
	 */
	public static boolean updateConnectedFlags(final Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
		if (activeInfo != null && activeInfo.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isOnline(Context context) {
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}
	
    /**
     * getListViewSize
     * @param myListView
     */
    public static void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
      //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
    }
    
	public static String getStringResourceByKey(Context mContext, String key){
		try {
			int i = mContext.getResources().getIdentifier(
					key, "string", mContext.getPackageName());
			String msg = mContext.getString(i);
			return msg;
		} catch (Exception e) {
			String msg = mContext.getResources().getString(R.string.COMMON_ERROR);
			return msg;
		}
	}
	
	/**
	 * getUserAgent
	 * @return
	 */
	public static String getUserAgent(){
		try{
			String ua = System.getProperty("http.agent");
			return ua;
		} catch (Exception e) {
			e.printStackTrace();
			return "Mozilla/5.0 (Linux; U; Android 4.0.3; ko-kr; LG-L160L Build/IML74K)";
		}
	}
	
	/**
     * @return the userAgentString
     */
    public static String getUserAgentString()
    {
        StringBuilder userAgentString = new StringBuilder();
        userAgentString.append(GlobalParams.SETTING_MESSAGE_APP_VERSION
            + appVersionName + GlobalParams.COMMA_SPACE_SEPARATOR
            + GlobalParams.SETTING_MESSAGE_DEVICE_MODEL
            + android.os.Build.MODEL + GlobalParams.COMMA_SPACE_SEPARATOR 
            + GlobalParams.SETTING_MESSAGE_SYSTEM_NAME
            + "Android OS" + GlobalParams.COMMA_SPACE_SEPARATOR
            + GlobalParams.SETTING_MESSAGE_SYSTEM_VERSION + android.os.Build.VERSION.RELEASE);
        return userAgentString.toString();
    }
    
    /**
     * @return the appVersionName
     */
    public static String getAppVersionName()
    {
        return appVersionName;
    }

    /**
     * @param appVersionName the appVersionName to set
     */
    public static void setAppVersionName(String appVersionName)
    {
        BuManagement.appVersionName = appVersionName;
    }
    
    public static void appendLog(String text)
    {       
       File logFile = new File("sdcard/log.file");
       if (!logFile.exists())
       {
          try
          {
             logFile.createNewFile();
          } 
          catch (IOException e)
          {        
             e.printStackTrace();
          }
       }
       try
       {
          //BufferedWriter for performance, true to set append to file flag
          BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true)); 
          buf.append(text);
          buf.newLine();
          buf.close();
       }
       catch (IOException e)
       {
          e.printStackTrace();
       }
    }
    
    /**
     * function to convert a View to image
     * image will be save to DCIM folder of device
     * @param ctx
     * @param v
     * @param appName
     * @return
     */
    @SuppressLint({ "SimpleDateFormat", "NewApi" })
	public final static boolean shootView(final Context ctx, final View v, final String appName)
    {
        // Get the bitmap from the view
        v.setDrawingCacheEnabled(true);

        boolean isOK = false;

        final Bitmap bmp = v.getDrawingCache();

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd@HHmmss");
        final Calendar cal = Calendar.getInstance();

        // Set file properties
        final String fileJPG = appName + "_" + sdf.format(cal.getTime());

		/*
		 * Create a path where we will place our picture in the user's public
		 * pictures directory. Note that you should be careful about what you
		 * place here, since the user often manages these files. For pictures
		 * and other media owned by the application, consider
		 * Context.getExternalMediaDir().
		 */
		final File path = Environment.getExternalStoragePublicDirectory(
		// Environment.DIRECTORY_PICTURES
				Environment.DIRECTORY_DCIM);

		// Make sure the Pictures directory exists.
		if (!path.exists()) {
			path.mkdirs();
		}

		final File file = new File(path, fileJPG + ".jpg");

		try {
			final FileOutputStream fos = new FileOutputStream(file);

			final BufferedOutputStream bos = new BufferedOutputStream(fos, 8192);

			bmp.compress(CompressFormat.JPEG, 45, bos);

			bos.flush();
			bos.close();

			isOK = true;
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return isOK;
	}
    
    /**
     * function to convert a scrollview to image
     * image will be save to DCIM folder of device
     * @param ctx
     * @param v
     * @param appName
     * @return
     */
    @SuppressLint({ "SimpleDateFormat", "NewApi" })
	public final static boolean shootScrollView(final Context ctx, final ScrollView v, final String appName)
    {
        // Get the bitmap from the view
        v.setDrawingCacheEnabled(true);

        boolean isOK = false;

        final Bitmap bmp = getScrollViewBitmap(ctx, v);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd@HHmmss");
        final Calendar cal = Calendar.getInstance();

        // Set file properties
        final String fileJPG = appName + "_" + sdf.format(cal.getTime());

		/*
		 * Create a path where we will place our picture in the user's public
		 * pictures directory. Note that you should be careful about what you
		 * place here, since the user often manages these files. For pictures
		 * and other media owned by the application, consider
		 * Context.getExternalMediaDir().
		 */
		final File path = Environment.getExternalStoragePublicDirectory(
		// Environment.DIRECTORY_PICTURES
				Environment.DIRECTORY_DCIM);

		// Make sure the Pictures directory exists.
		if (!path.exists()) {
			path.mkdirs();
		}

		final File file = new File(path, fileJPG + ".jpg");

		try {
			final FileOutputStream fos = new FileOutputStream(file);

			final BufferedOutputStream bos = new BufferedOutputStream(fos, 8192);

			bmp.compress(CompressFormat.JPEG, 45, bos);

			bos.flush();
			bos.close();

			isOK = true;
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return isOK;
	}
    
    /**
     * get bitmap of view
     * @param v
     * @return
     */
    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap( 540, 960, Bitmap.Config.ARGB_8888);                
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }
    
    /**
     * get bitmap from scroll view
     * @param ctx
     * @param scrollView
     * @return
     */
    public static Bitmap getScrollViewBitmap(Context ctx, ScrollView scrollView)
    {
    	Bitmap bitmap = Bitmap.createBitmap(scrollView.getChildAt(0).getWidth(), scrollView.getChildAt(0).getHeight(), Bitmap.Config.ARGB_8888);
    	Canvas c = new Canvas(bitmap);
    	 //Get the view's background
        Drawable bgDrawable = scrollView.getBackground();
        if (bgDrawable!=null){ 
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(c);
        } else {
            //does not have background drawable, then draw white background on the canvas
            c.drawColor(ctx.getResources().getColor(R.color.GrayE5));
        }
    	scrollView.getChildAt(0).draw(c);
        return bitmap;
    }
    
    /**
     * return check update
     */
    public static String getResultScan(Activity activity)
    {
    	SharedPreferenceManager share = new SharedPreferenceManager(activity);    	
    	String firstLoad = null;
    	String tmp = GlobalParams.BLANK_CHARACTER;
    	
    	tmp = share.getString(GlobalParams.RESULTSCAN, GlobalParams.BLANK_CHARACTER);
    	
    	if (!GlobalParams.BLANK_CHARACTER.equalsIgnoreCase(tmp))
    	{
    		firstLoad = tmp;
    	}
    	
    	return firstLoad;    	
    }
    
    /**
     * normalization time
     * @param inputTime : input time to normalize
     * @param inputTimeFomart :format time input Example: yyyy-MM-dd'T'HH:mm:ss
     * @param outputTimeFormat :format time out\ Example: yyyy-MM-dd
     * @return time normalized
     */
    public static String normalizationTime(String inputTime, String inputTimeFomart, String outputTimeFormat){
    	String timeOutput = "";
    	try{
    		SimpleDateFormat sdfInput = new SimpleDateFormat(inputTimeFomart);
        	SimpleDateFormat sdfOutput = new SimpleDateFormat(outputTimeFormat);
    		Date date = sdfInput.parse(inputTime);
    		timeOutput = sdfOutput.format(date);
    	} catch (Exception e) {
			e.printStackTrace();
			timeOutput = inputTime;
		}
    	return timeOutput;
    }
    
    /**
     * format decimal
     * @param number
     * @return
     */
    public static String formatDecimal(double number) {
    	  float epsilon = 0.004f; // 4 tenths of a cent
    	  if (Math.abs(Math.round(number) - number) < epsilon) {
    	     return String.format("%10.0f", number); // sdb
    	  } else {
    	     return String.format("%10.2f", number); // dj_segfault
    	  }
    	}
    
    /**
     * This method converts dp unit to equivalent pixels, depending on device density. 
     * 
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }
    
    /**
     * this function will cut \" characters in string
     * @param s :string format "abc"
     * @return abc
     */
	public static String getDataBarcode(String s) {
		if (s.indexOf("\"") > -1) {
			s = s.substring(1, s.length() - 1);
		}

		return s;
	}
    /**
     * converDatePatern
     * @param strDate date to convert
     * @param paternIn patern in put
     * @param paternOut patern out put
     * @return
     */
    @SuppressLint("SimpleDateFormat") 
    public static String converDatePatern(String strDate, String paternIn, String paternOut){
    	try{
    		SimpleDateFormat sdfIn = new SimpleDateFormat(paternIn);
    		SimpleDateFormat sdfOut = new SimpleDateFormat(paternOut);
    		Date date = sdfIn.parse(strDate);
    		String result = sdfOut.format(date);
    		return result;
    	} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
    }
    
}
