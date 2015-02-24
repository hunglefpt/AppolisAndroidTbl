/**
 * Name: $RCSfile: SplashActivity.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.appolis.androidtablet.R;

/**
 * 
 * @author hoangnh11
 * Display Splash screen
 */
public class SplashActivity extends Activity {

	private static String TAG = SplashActivity.class.getName();
	private static long SLEEP_TIME = 3; // Sleep for some time

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		
		// Start timer and launch main activity	
		IntentLauncher launcher = new IntentLauncher();
		launcher.start();
	}
	
	/**
	 * Show Splash screen about 1 second after next to First login screen
	 * @author hoangnh11
	 */
	private class IntentLauncher extends Thread {
		@Override		
		public void run() {
			try {
				Thread.sleep(SLEEP_TIME * 1000);
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}

			// Start main activity
			Intent intent = new Intent(SplashActivity.this, FirstLoginActivity.class);
			SplashActivity.this.startActivity(intent);
			SplashActivity.this.finish();
		}
	}
}