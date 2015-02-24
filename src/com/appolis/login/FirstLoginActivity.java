/**
 * Name: $RCSfile: FirstLoginActivity.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.login;

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
import android.view.inputmethod.EditorInfo;
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
 * 
 * @author hoangnh11
 * Display First login screen for login application
 */
public class FirstLoginActivity extends Activity implements OnClickListener {
		
	private EditText edtLogin;
	private EditText edtPin;
	private Button btnLogin;
	private AppPreferences _appPrefs;
	private LanguagePreferences languagePrefs;
	ProgressDialog dialog;
	private boolean internetConnected = false;
	private ObjectUser itemUser;
	public static List<ObjectSettingLanguage> objectSettingLanguage;
	private LinearLayout linTip;
	private ImageView imgCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.first_login_activity);
	
		itemUser = new ObjectUser();
		dialog = new ProgressDialog(this);
		_appPrefs = new AppPreferences(getApplicationContext());
		languagePrefs = new LanguagePreferences(getApplicationContext());
		initLayout();
	}
	
	/**
	 * This method is used to initial layout for activity
	 * 
	 */
	private void initLayout() {		
		LoginActivity.valueAuthorization = null;
		edtLogin = (EditText) findViewById(R.id.edt_login);
		edtPin = (EditText) findViewById(R.id.edt_pin);
		btnLogin = (Button) findViewById(R.id.btn_login);
		linTip = (LinearLayout) findViewById(R.id.linTip);
		imgCancel = (ImageView) findViewById(R.id.imgCancel);
		
		edtLogin.setText(_appPrefs.getUsername());
		edtPin.setText(_appPrefs.getPIN());
		
		btnLogin.setOnClickListener(this);
		imgCancel.setOnClickListener(this);
	
		//check tip visible
		if (_appPrefs.getTipFirstLogin().equalsIgnoreCase(GlobalParams.TRUE)) {
			linTip.setVisibility(View.GONE);
		} else {
			linTip.setVisibility(View.VISIBLE);
		}
		
		//check value of user name field to focus field
		String userName = edtLogin.getText().toString();
		if(StringUtils.isBlank(userName)) {
			edtLogin.requestFocus();
			Utilities.showKeyboard(FirstLoginActivity.this);
		} else{
			edtPin.requestFocus();
		}
		
		edtPin.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					
					if(checkField(edtLogin, edtPin)){
						
						if(edtPin.getText().toString().equals(GlobalParams.PIN) 
								&& edtLogin.getText().toString().equalsIgnoreCase(GlobalParams.CONFIGURE)) {
							Intent intent = new Intent(FirstLoginActivity.this, SettingActivity.class);
							startActivity(intent);
						} else {
							internetConnected = BuManagement.Instance.isOnline(FirstLoginActivity.this);
							
							if (internetConnected) {
								FirstLoginAsyncTask firstLoginAsyncTask = new FirstLoginAsyncTask();
								firstLoginAsyncTask.execute();
							} else {
								showPopUp(FirstLoginActivity.this, GlobalParams.offlinenetwork, edtLogin);
							}
						}
					}
	            }
				
				return false;
			}
		});
	}

	/**
	 * Process data for views
	 */
	@Override
	public void onClick(View v) {		
		switch (v.getId()) {
		case R.id.btn_login:
			
			if(checkField(edtLogin, edtPin)){
				
				if(edtPin.getText().toString().equals(GlobalParams.PIN) 
						&& edtLogin.getText().toString().equalsIgnoreCase(GlobalParams.CONFIGURE)) {
					startActivity(new Intent(this, SettingActivity.class));
				} else {
					internetConnected = BuManagement.Instance.isOnline(this);
					
					if (internetConnected) {
						FirstLoginAsyncTask firstLoginAsyncTask = new FirstLoginAsyncTask();
						firstLoginAsyncTask.execute();
					} else {
						showPopUp(FirstLoginActivity.this, GlobalParams.offlinenetwork, edtLogin);
					}
				}
			}
			
			break;
			
		case R.id.imgCancel:
			linTip.setVisibility(View.GONE);
			_appPrefs.saveTipFirstLogin(GlobalParams.TRUE);
			break;
		
		default:
			break;
		}
	}
	
	/**
	 * This method is used to check field before login
	 * @param login
	 * @param PIN
	 * @param site
	 * @return result
	 * if all fields is not null, function will return true
	 */
	private boolean checkField(EditText login, EditText PIN) {
		boolean result;
		if (StringUtils.isNotBlank(login.getText().toString())) {
			
			if (StringUtils.isNotBlank(PIN.getText().toString())) {
				String str = GlobalParams.WAREHOUSE + GlobalParams.VERTICAL_TWO_DOT
						+ login.getText().toString().trim() + GlobalParams.VERTICAL_TWO_DOT
						+ PIN.getText().toString().trim();
				LoginActivity.valueAuthorization = GlobalParams.BASIC + Base64.encodeBytes(str.getBytes());			
				result = true;
			} else {
				showPopUp(this, GlobalParams.PIN_BLANK, edtPin);
				result = false;
			}
			
		} else {
			showPopUp(this, GlobalParams.USERNAME_BLANK, edtLogin);
			result = false;
		}
		
		return result;
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
	 * Get result from Login activity return
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GlobalParams.FIRSTLOGINSCREEN_TO_LOGINSCREEN) {		
			LoginActivity.valueAuthorization = null;
			if (resultCode == RESULT_FIRST_USER) {
				finish();
			}
			if (resultCode == RESULT_CANCELED) {				
				edtLogin.setText(_appPrefs.getUsername());
				edtPin.setText(_appPrefs.getPIN());
			}			
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {	
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// myAynstask.cancel(true);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * Check login
	 * @author hoangnh11
	 */
	class FirstLoginAsyncTask extends AsyncTask<Void, Void, String> {
		String data = "";
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(FirstLoginActivity.this);
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
					netParameter[1] = new NetParameter(GlobalParams.WAREHOUSENAME, "");
					data = HttpNetServices.Instance.firstLogin(netParameter);
					Logger.error(data);
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
			dialog.dismiss();
			// If not cancel by user
			if (!isCancelled()) {
				
				if (result.equals(GlobalParams.TRUE)) {
					try {
						if (data.substring(1, data.length() - 1).equalsIgnoreCase
								(getResources().getString(R.string.PLEASE_ENTER_SITE_NAME))) {	
							
							Utilities.startActivityForResultWithParam(FirstLoginActivity.this, LoginActivity.class,
									GlobalParams.FIRSTLOGINSCREEN_TO_LOGINSCREEN,
									new String[] { GlobalParams.PARAM_MESSAGES_LOGIN, GlobalParams.PARAM_USER_LOGIN,
										GlobalParams.PARAM_PIN_LOGIN },
									new String[] {data.replace("\"", GlobalParams.SPACE),
													edtLogin.getText().toString(),edtPin.getText().toString() });
							
						} else if (!data.substring(1, data.length() - 1).equalsIgnoreCase
								(getResources().getString(R.string.PLEASE_ENTER_SITE_NAME)) 
								&& data.substring(1, data.length() - 1).length() > 0) {
							
							GetUserAsyncTask getUserAsyncTask = new GetUserAsyncTask();
							getUserAsyncTask.execute();	
							
						} else {
							Utilities.showPopUp(FirstLoginActivity.this, LoginActivity.class, data);
						}	
					} catch (Exception e) {
						showPopUp(FirstLoginActivity.this, GlobalParams.NETWORK_ERROR, edtLogin);
					}					
				} else if (data.equals(GlobalParams.BLANK_CHARACTER)) {
					showPopUp(FirstLoginActivity.this, GlobalParams.WRONG_USER, edtLogin);
				} else {
					showPopUp(FirstLoginActivity.this, GlobalParams.NETWORK_ERROR, edtLogin);
				}
			}
		}
	}
	
	/**
	 * createLanguagePackage
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
	
	/**
	 * Get user profile and get culture information
	 * @author hoangnh11
	 */
	class GetUserAsyncTask extends AsyncTask<Void, Void, String> {
		String data, dataCultureInfo;
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(FirstLoginActivity.this);
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
					Logger.error(data + " : " + itemUser.get_PIN());

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
			dialog.dismiss();
			// If not cancel by user
			if (!isCancelled()) {
				if (result.equals(GlobalParams.TRUE)) {
					if (itemUser != null && itemUser.get_PIN() != null) {
						
						LoginActivity.listItemMain = itemUser.get_userRoles();
						LoginActivity.itemUser = itemUser;
						Intent mainIntent = new Intent(FirstLoginActivity.this, MainActivity.class);
						startActivityForResult(mainIntent, GlobalParams.LOGINSCREEN_TO_MAINSCREEN);
						_appPrefs.saveUsername(edtLogin.getText().toString());
						_appPrefs.savePIN(edtPin.getText().toString());
						_appPrefs.saveSite(GlobalParams.BLANK_CHARACTER);
						
					} else {
						showPopUp(FirstLoginActivity.this, GlobalParams.WRONG_PIN, edtPin);
					}
				} else {
					showPopUp(FirstLoginActivity.this, GlobalParams.NETWORK_ERROR, edtPin);
				}
			}
		}
	}	
}
