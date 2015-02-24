/**
 * Name: $RCSfile: SettingActivity.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.login;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appolis.androidtablet.R;
import com.appolis.common.AppPreferences;
import com.appolis.network.access.HttpNetServices;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.StringUtils;
import com.appolis.utilities.Utilities;

/**
 * 
 * @author hoangnh11
 * Display Setting screen for changing host
 */
public class SettingActivity extends Activity implements OnClickListener {
	private Button btnOK;
	private Button btnCancel;
	private EditText edtURL;
	private AppPreferences _appPrefs;
	private AsyncTask<?, ?, ?> myAynstask;
	private String URL;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		_appPrefs = new AppPreferences(getApplicationContext());
		setContentView(R.layout.setting_activity);
		initLayout();
		super.onCreate(savedInstanceState);
	}

	/**
	 * Instance Layout
	 */
	private void initLayout() {
		btnOK = (Button) findViewById(R.id.btn_Ok);
		btnCancel = (Button) findViewById(R.id.btn_Cancel);
		edtURL=(EditText)findViewById(R.id.edtURL);
		edtURL.setText(_appPrefs.getURLFirstLogin());
		btnOK.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		edtURL.requestFocus();
		Utilities.showKeyboard(SettingActivity.this);
	}

	/**
	 * Process event for view
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_Ok:
			URL = edtURL.getText().toString().trim();
			
			if(StringUtils.isNotBlank(URL)) {				
				(new ProcessData(this)).execute();
			} else {			
				showPopUpSetting(SettingActivity.this, getResources().getString(R.string.INVALID_URL));
			}	
			
			break;
			
		case R.id.btn_Cancel:
            finish();
			break;
			
		default:
			break;
		}
	}
	
	/**
	 * This method is used to show pop up
	 * @param mContext
	 * @param strMessages
	 */
	private void showPopUpSetting(Context mContext, String strMessages) {
		String message;
		
		if (strMessages.equals(GlobalParams.BLANK)) {
			message = GlobalParams.WRONG_USER;
		} else {
			message = strMessages;
		}
		
		final Dialog dialog = new Dialog(mContext, R.style.Dialog_NoTitle);
		dialog.setContentView(R.layout.dialogwarning);
		
		// set the custom dialog components - text, image and button		
		TextView text2 = (TextView) dialog.findViewById(R.id.tvScantitle2);		
		text2.setText(message);
		Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
		dialogButtonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				edtURL.requestFocus();
				Utilities.showKeyboard(SettingActivity.this);
			}
		});
		dialog.show();
	}
	
	/**
	 * Check url
	 * @author hoangnh11
	 */
	private class ProcessData {
		private final Context context;
		private ProgressDialog dialog;

		public ProcessData(Context c) {
			context = c;
		}

		private void execute() {
			myAynstask = new ShowItems().execute(new Void[] { null });
		}

		public class ShowItems extends AsyncTask<Void, Void, Integer> {

			@Override
			protected Integer doInBackground(Void... arg0) {
			int AuthenticateCode = 0;
			try {
				 AuthenticateCode = HttpNetServices.AuthenticateURL(URL);
			} catch (Exception e) {
				AuthenticateCode = 0;				
			}
				return AuthenticateCode;
			}

			@Override
			protected void onPreExecute() {
				dialog = new ProgressDialog(context);
				dialog.setCancelable(true);
				dialog.setMessage(GlobalParams.LOADING);
				dialog.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						
						if(myAynstask!=null) {
							myAynstask.cancel(true);
							myAynstask = null;
						}
					}
				});
				dialog.show();
			}

			@Override
			protected void onCancelled() {
				super.onCancelled();
				
				if(myAynstask!=null) {
					myAynstask.cancel(true);
					myAynstask = null;
				}
			}

			@Override
			protected void onPostExecute(Integer result) {
				myAynstask = null;
				dialog.dismiss();
				
				if(result==200)	{
					_appPrefs.saveURLFirstLogin(URL);
					finish();
				} else {
					showPopUpSetting(SettingActivity.this, getResources().getString(R.string.INVALID_URL));
				}
			}
		}
	}
}
