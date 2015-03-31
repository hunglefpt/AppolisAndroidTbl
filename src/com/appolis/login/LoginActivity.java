/**
 * Name: $RCSfile: LoginActivity.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.login;

import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.appolis.androidtablet.R;
import com.appolis.common.AppPreferences;
import com.appolis.common.AppolisException;
import com.appolis.common.Base64;
import com.appolis.common.LanguagePreferences;
import com.appolis.entities.ObjectSettingLanguage;
import com.appolis.entities.ObjectUser;
import com.appolis.network.NetParameter;
import com.appolis.network.access.HttpNetServices;
import com.appolis.utilities.BuManagement;
import com.appolis.utilities.DataParser;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.Logger;
import com.appolis.utilities.StringUtils;
import com.appolis.utilities.Utilities;

/**
 * @author hoangnh11
 * Display Login screen
 */
public class LoginActivity extends Activity implements OnClickListener {
	
	/**
	 * variable declaration to initial layout
	 */
	private EditText edtLogin;
	private EditText edtPin;
	private EditText edtSite;
	private Button btnLogin, btnCancel;
	
	/**
	 * variable declaration
	 */
	private AppPreferences _appPrefs;
	private LanguagePreferences languagePrefs;
	public static String[] listItemMain;
	public static List<ObjectSettingLanguage> objectSettingLanguage;
	public static ObjectUser itemUser;
	public static String valueAuthorization;
	public static String DEFAULTBINNUMBER;
	public static String URLDefault;
	ProgressDialog dialog;
	private boolean internetConnected = false;
	private LinearLayout linTip;
	private ImageView imgCancel;
	private boolean activityIsRunning = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_appPrefs = new AppPreferences(getApplicationContext());
		languagePrefs = new LanguagePreferences(getApplicationContext());
		setContentView(R.layout.login_acivity);
		activityIsRunning = true;
		itemUser = new ObjectUser();
		dialog = new ProgressDialog(this);
		initLayout();		
	
		showPopUpLogin(LoginActivity.this, getIntent().getStringExtra(GlobalParams.PARAM_MESSAGES_LOGIN));
	}
	
	/**
	 * This method is used to initial layout for activity
	 */
	private void initLayout() {	
		edtLogin = (EditText) findViewById(R.id.edt_login);
		edtPin = (EditText) findViewById(R.id.edt_pin);
		edtSite = (EditText) findViewById(R.id.edt_site);
		edtSite.setText(_appPrefs.getSite());
		edtPin.setText(getIntent().getStringExtra(GlobalParams.PARAM_PIN_LOGIN));
		edtLogin.setText(getIntent().getStringExtra(GlobalParams.PARAM_USER_LOGIN));
		linTip = (LinearLayout) findViewById(R.id.linTip);
		imgCancel = (ImageView) findViewById(R.id.imgCancel);

		btnLogin = (Button) findViewById(R.id.btn_login);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
		btnLogin.setOnClickListener(this);	
		imgCancel.setOnClickListener(this);
		
		//check tip visible
		if (_appPrefs.getTipLogin().equalsIgnoreCase(GlobalParams.TRUE)) {
			linTip.setVisibility(View.GONE);
		} else {
			linTip.setVisibility(View.VISIBLE);
		}
		
		edtSite.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				/* Download data and process data in here */
				Utilities.hideKeyboard(LoginActivity.this);
				if (checkField(edtLogin, edtPin, edtSite)) {
					
					internetConnected = BuManagement.Instance.isOnline(LoginActivity.this);				
					if (internetConnected) {
						
						if(edtPin.getText().toString().equals(GlobalParams.PIN) 
								&& edtLogin.getText().toString().equalsIgnoreCase(GlobalParams.CONFIGURE)) {
							startActivity(new Intent(LoginActivity.this, SettingActivity.class));
						} else {
							LoginAsyncTask loginAsyncTask = new LoginAsyncTask();
							loginAsyncTask.execute();
						}
						
					} else {
						Utilities.showPopUp(LoginActivity.this, null, GlobalParams.offlinenetwork);
					}
				}
				return false;
			}
		});		
	}
	
	/**
	 * This method is used to show pop up
	 * @param mContext
	 * @param strMessages
	 */
	private void showPopUpLogin(Context mContext, String strMessages) {
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
				String siteName = edtSite.getText().toString();
				
				if(StringUtils.isBlank(siteName)){
					edtSite.requestFocus();
					Utilities.showKeyboard(LoginActivity.this);
				}
			}
		});
		
		dialog.show();
	}
	
	/**
	 * This method is used to check field before login
	 * @param login
	 * @param PIN
	 * @param site
	 * @return result
	 * if all fields is not null, function will return true
	 */
	private boolean checkField(EditText login, EditText PIN, EditText site) {
		boolean result;
		
		if (StringUtils.isNotBlank(login.getText().toString())) {
			
			if (StringUtils.isNotBlank(PIN.getText().toString())) {
				
				if (StringUtils.isNotBlank(site.getText().toString())) {
					String str = site.getText().toString().trim() + ":"
							+ login.getText().toString().trim() + ":"
							+ PIN.getText().toString().trim();
					valueAuthorization = GlobalParams.BASIC + Base64.encodeBytes(str.getBytes());
					Logger.error(valueAuthorization);
					result = true;
				} else {
					Utilities.showPopUpWithAction(this, null, GlobalParams.SITE_BLANK, site);
					result = false;
				}
				
			} else {
				Utilities.showPopUpWithAction(this, null, GlobalParams.PIN_BLANK, PIN);
				result = false;
			}
			
		} else {
			Utilities.showPopUpWithAction(this, null, GlobalParams.USERNAME_BLANK, login);
			result = false;
		}
		
		return result;
	}

	/**
	 * process for data views
	 */
	@Override
	public void onClick(View v) {	
		switch (v.getId()) {
		case R.id.btn_login:
			
			/* Download data and process data in here */
			Utilities.hideKeyboard(this);
			if (checkField(edtLogin, edtPin, edtSite)) {
				
				internetConnected = BuManagement.Instance.isOnline(this);				
				if (internetConnected) {
					
					if(edtPin.getText().toString().equals(GlobalParams.PIN) 
							&& edtLogin.getText().toString().equalsIgnoreCase(GlobalParams.CONFIGURE)) {
						startActivity(new Intent(this, SettingActivity.class));
					} else {
						LoginAsyncTask loginAsyncTask = new LoginAsyncTask();
						loginAsyncTask.execute();
					}
					
				} else {
					Utilities.showPopUp(LoginActivity.this, null, GlobalParams.offlinenetwork);
				}
			}
			
			break;
			
		case R.id.btnCancel:
			finish();
			break;
			
		case R.id.imgCancel:
			linTip.setVisibility(View.GONE);
			_appPrefs.saveTipLogin(GlobalParams.TRUE);
			break;
			
		default:
			break;
		}
	}
	
	/**
	 * Set event for button Back
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {	
		
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			LoginActivity.this.setResult(RESULT_FIRST_USER,new Intent());
			LoginActivity.this.finish();
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * Process activity
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		activityIsRunning = true;
		if (requestCode == GlobalParams.LOGINSCREEN_TO_MAINSCREEN) {
			
			if (resultCode == RESULT_FIRST_USER) {
				setResult(RESULT_FIRST_USER, new Intent());
				finish();
			}
			
			if (resultCode == RESULT_CANCELED) {
				finish();
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		activityIsRunning = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		activityIsRunning = false;
	}

	/**
	 * Check Login
	 * @author hoangnh11
	 */
	class LoginAsyncTask extends AsyncTask<Void, Void, String> {
		String data;
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(LoginActivity.this);
			dialog.setMessage(GlobalParams.LOADING);
			dialog.show();
			dialog.setCancelable(false); 
			dialog.setCanceledOnTouchOutside(false);
		}
		
		@Override
		protected String doInBackground(Void... params) {
			String result;
			
			if (!isCancelled()) {
				try {
					NetParameter[] netParameter = new NetParameter[2];
					netParameter[0] = new NetParameter(GlobalParams.USERNAME, edtLogin.getText().toString());
					netParameter[1] = new NetParameter(GlobalParams.WAREHOUSENAME, 
							URLEncoder.encode(edtSite.getText().toString(), GlobalParams.UTF_8));
					data = HttpNetServices.Instance.login(netParameter);
					Logger.error(data + "==========");
					result = GlobalParams.TRUE;
					
				} catch (AppolisException e) {
					result = GlobalParams.FALSE;
					
				} catch (Exception e) {
					result = GlobalParams.FALSE;
					
				}
				
			} else {
				result = GlobalParams.FALSE;
			}
			
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (activityIsRunning) {
				dialog.dismiss();			
				// If not cancel by user
				if (!isCancelled()) {
					
					if (result.equals(GlobalParams.TRUE) && data != null) {
						
						if (data.substring(1, data.length() - 1).length() > 0) {
							GetUserAsyncTask getUserAsyncTask = new GetUserAsyncTask();
							getUserAsyncTask.execute();
							
						} else {
							if (edtSite.getEditableText().toString().equalsIgnoreCase(GlobalParams.WAREHOUSE)
									|| edtSite.getEditableText().toString().equalsIgnoreCase(GlobalParams.SKYLINE_SNOWBOARD_REPAIR)
									|| edtSite.getEditableText().toString().equalsIgnoreCase(GlobalParams.COPPERHEAD_MTN_SKY_LODGE)) {
								showPopUp(LoginActivity.this, GlobalParams.WRONG_USER, edtLogin);
							} else {							
								showPopUp(LoginActivity.this, GlobalParams.WRONG_SITE, edtLogin);
							}
						}
						
					} else if (data.equals(GlobalParams.BLANK_CHARACTER) && data != null) {
						
						if (edtSite.getEditableText().toString().equalsIgnoreCase(GlobalParams.WAREHOUSE)
								|| edtSite.getEditableText().toString().equalsIgnoreCase(GlobalParams.SKYLINE_SNOWBOARD_REPAIR)
								|| edtSite.getEditableText().toString().equalsIgnoreCase(GlobalParams.COPPERHEAD_MTN_SKY_LODGE)) {
							showPopUp(LoginActivity.this, GlobalParams.WRONG_USER, edtLogin);
						} else {							
							showPopUp(LoginActivity.this, GlobalParams.WRONG_SITE, edtLogin);
						}
						
					} else {
						showPopUp(LoginActivity.this, GlobalParams.NETWORK_ERROR, edtLogin);
					}
				}
			}			
		}
	}
	
	/**
	 * Get user profile and get culture information
	 * @author hoangnh11
	 */
	class GetUserAsyncTask extends AsyncTask<Void, Void, String> {
		String data, dataCultureInfo;
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(LoginActivity.this);
			dialog.setMessage(GlobalParams.LOADING_DATA);
			dialog.show();
			dialog.setCancelable(false); 
			dialog.setCanceledOnTouchOutside(false);
		}
		
		@Override
		protected String doInBackground(Void... params) {
			String result;
			if (!isCancelled()) {
				
				try {
					data = HttpNetServices.Instance.getUser();
					itemUser = DataParser.getUser(data);
					Logger.error(data);
					
					String languageInputMode = Locale.getDefault().getLanguage();
					String languageMode = languagePrefs.getLanguageMode();
					if(!languageMode.equalsIgnoreCase(languageInputMode)){
						Logger.error("loading language==languageMode:" + languageMode + "*languageInputMode:" + languageInputMode );
						NetParameter[] netParameter = new NetParameter[1];
						netParameter[0] = new NetParameter(GlobalParams.CULTUREINFO, languageInputMode);
						dataCultureInfo = HttpNetServices.Instance.getCultureInfo(netParameter);
						objectSettingLanguage = DataParser.getCultureInfo(dataCultureInfo);
						createLanguagePackage();
						languagePrefs.saveLanguageMode(languageInputMode);
					}

					result = GlobalParams.TRUE;
					
				} catch (AppolisException e) {
					result = GlobalParams.FALSE;
					
				} catch (Exception e) {					
					result = GlobalParams.FALSE;
					
				}
				
			} else {
				result = GlobalParams.FALSE;
				
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (activityIsRunning) {
				dialog.dismiss();			
				// If not cancel by user
				if (!isCancelled()) {
					
					if (result.equals(GlobalParams.TRUE)) {
						
						if (itemUser != null && itemUser.get_PIN() != null) {
							listItemMain = itemUser.get_userRoles();
							Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
							startActivityForResult(mainIntent, GlobalParams.LOGINSCREEN_TO_MAINSCREEN);
							_appPrefs.saveUsername(edtLogin.getText().toString());
							_appPrefs.savePIN(edtPin.getText().toString());
							_appPrefs.saveSite(edtSite.getText().toString());
							
						} else {
							showPopUp(LoginActivity.this, GlobalParams.WRONG_PIN, edtPin);
						}
						
					} else {
						showPopUp(LoginActivity.this, GlobalParams.NETWORK_ERROR, edtPin);
					}
				}
			}			
		}
	}
	
	/**
	 * This method is used to show pop up
	 *
	 */
	public static void showPopUp(final Context mContext, String strMessages, final TextView focus) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog_NoTitle);
		dialog.setContentView(R.layout.dialogwarning);
		
		// set the custom dialog components - text, image and button		
		TextView text2 = (TextView) dialog.findViewById(R.id.tvScantitle2);		
		text2.setText(strMessages);
		Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
		
		dialogButtonOk.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				focus.requestFocus();
				Utilities.showKeyboard(mContext);
			}
		});
		
		dialog.show();
	}
		
	/**
	 * Save language
	 */
	public void createLanguagePackage(){
		if(null == objectSettingLanguage || objectSettingLanguage.size() == 0){
			return;
		}
		
		for (ObjectSettingLanguage item : objectSettingLanguage) {
			try{
				languagePrefs.savePreference(item.getKey(), item.getValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
