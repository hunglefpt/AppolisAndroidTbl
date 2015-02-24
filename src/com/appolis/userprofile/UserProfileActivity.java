/**
 * Name: $RCSfile: UserProfileActivity.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.userprofile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appolis.androidtablet.R;
import com.appolis.common.AppPreferences;
import com.appolis.common.Base64;
import com.appolis.common.LanguagePreferences;
import com.appolis.login.LoginActivity;
import com.appolis.model.UserProfileModel;
import com.appolis.network.NetParameter;
import com.appolis.network.access.HttpNetServices;
import com.appolis.utilities.CommontDialog;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.Utilities;

/**
 * 
 * @author hoangnh11
 * Display User profile screen when change pin
 */
public class UserProfileActivity extends Activity implements OnClickListener, OnKeyListener {
	
	UserProfileModel userInfoModel = new UserProfileModel();
	public static String valueAuthorization;
	private AppPreferences _appPrefs;
	private LinearLayout lin_buton_home;
	private ImageView imgScan;
	private String versionName;
	private TextView tvVersionApp, tvScreenTitle, tvScreenTitleExplane;
	private LanguagePreferences languagePrefs;
	private LinearLayout linTip;
	private ImageView imgCancel;
	
	/**
	 * instance layout
	 */
	private void initLayout() {
		languagePrefs = new LanguagePreferences(getApplicationContext());
		lin_buton_home = (LinearLayout) findViewById(R.id.lin_buton_home);
		imgScan = (ImageView) findViewById(R.id.imgScan);		
		tvVersionApp = (TextView) findViewById(R.id.tvVersionApp);
		tvScreenTitle = (TextView) findViewById(R.id.tvScreenTitle);
		tvScreenTitleExplane = (TextView) findViewById(R.id.tvScreenTitleExplane);
		linTip = (LinearLayout) findViewById(R.id.linTip);
		imgCancel = (ImageView) findViewById(R.id.imgCancel);
		imgCancel.setOnClickListener(this);
		
		//check tip visible
		if (_appPrefs.getTipUserProfile().equalsIgnoreCase(GlobalParams.TRUE)) {
			linTip.setVisibility(View.GONE);
		} else {
			linTip.setVisibility(View.VISIBLE);
		}
		
		lin_buton_home.setVisibility(View.VISIBLE);
		imgScan.setVisibility(View.GONE);
		
		userInfoModel.setBtnCofirm((Button) findViewById(R.id.btn_user_profile_Ok));
		userInfoModel.setBtnCancel((Button) findViewById(R.id.btn_user_profile_Cancel));
		userInfoModel.setTvVersion((TextView) findViewById(R.id.tvVersion));
		userInfoModel.setTvScreenHeader((TextView) findViewById(R.id.tv_header));
		userInfoModel.setEdtProfileCurrentPass((EditText) findViewById(R.id.edtProfileCurrentPass));
		userInfoModel.setEdtProfileNewPass((EditText) findViewById(R.id.edtProfileNewPass));
		userInfoModel.setEdtProfileReNewPass((EditText) findViewById(R.id.edtProfileReNewPass));
		
		//set data initial
		try {
			userInfoModel.getTvVersion().setText(getVersion());
		} catch (Exception e) {
			e.printStackTrace();
			userInfoModel.getTvVersion().setText(GlobalParams.BLANK_CHARACTER);
		}
		
		tvScreenTitle.setText(getLanguage(GlobalParams.USERPROFILE_LBL_EDIT, GlobalParams.EDIT_USER_INFORMATION));
		tvScreenTitleExplane.setText(GlobalParams.WITHOUTWIRE_ANDROID_EDITION);
		
		userInfoModel.getTvScreenHeader().setText(getLanguage(GlobalParams.MAINLIST_MENUSERPROFILE,
				GlobalParams.USER_PROFILE));
		userInfoModel.getBtnCofirm().setEnabled(false);
		userInfoModel.getBtnCofirm().setOnClickListener(this);
		userInfoModel.getBtnCancel().setOnClickListener(this);
		lin_buton_home.setOnClickListener(this);
		
		userInfoModel.getBtnCofirm().setText(getLanguage(GlobalParams.OK, GlobalParams.OK));
		userInfoModel.getBtnCancel().setText(getLanguage(GlobalParams.CANCEL, GlobalParams.CANCEL));
		userInfoModel.getEdtProfileCurrentPass().setHint(getLanguage(GlobalParams.USERPROFILE_LBL_CURRPASS,
				GlobalParams.CURRENT_PASSWORD));
		userInfoModel.getEdtProfileNewPass().setHint(getLanguage(GlobalParams.USERPROFILE_LBL_NEWPASS,
				GlobalParams.NEW_PASSWORD));
		userInfoModel.getEdtProfileReNewPass().setHint(getLanguage(GlobalParams.USERPROFILE_LBL_RENEWPASS,
				GlobalParams.RE_ENTER_NEW_PASSWORD));
		
		userInfoModel.getEdtProfileCurrentPass().addTextChangedListener(checkDisplay);
		userInfoModel.getEdtProfileNewPass().addTextChangedListener(checkDisplay);
		userInfoModel.getEdtProfileReNewPass().addTextChangedListener(checkDisplay);
		
		try {
			versionName = getApplicationContext().getPackageManager().getPackageInfo
					(getApplicationContext().getPackageName(), 0).versionName;
			tvVersionApp.setText(GlobalParams.APP_VERSION + GlobalParams.SPACE + versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		refreshData();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		_appPrefs = new AppPreferences(getApplicationContext());
		setContentView(R.layout.user_profile_activity);
		initLayout();
	}

	/**
	 * get language from language package
	 */
	public String getLanguage(String key, String value){
		return languagePrefs.getPreferencesString(key, value);
	}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {	
		return false;
	}

	/**
	 * Process event for views
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {		
			case R.id.lin_buton_home:
				Utilities.hideKeyboard(this);
				finish();
				break;
				
			case R.id.btn_user_profile_Cancel:
				finish();
				break;
			
			case R.id.btn_user_profile_Ok:
				if (validInput()) {
					
					try {
						refreshDataPinChange();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				} else {
					CommontDialog.showErrorDialog(this, GlobalParams.NEW_PASSWORD_AND_RENEW_PASS_NOT_MATCHED,
							GlobalParams.PASSWORD_NOT_MATCHED);
				}
				
				break;
				
			case R.id.imgCancel:
				linTip.setVisibility(View.GONE);
				_appPrefs.saveTipUserProfile(GlobalParams.TRUE);
				break;
				
			default:
				break;
		}
		
	}
	
	/**
	 * Check button OK enabled
	 */
	private TextWatcher checkDisplay = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if (userInfoModel.getEdtProfileCurrentPass().getText().length() == 4
					&& userInfoModel.getEdtProfileNewPass().getText().length() == 4
					&& userInfoModel.getEdtProfileReNewPass().getText().length() == 4) {
				userInfoModel.getBtnCofirm().setEnabled(true);
			} else {
				userInfoModel.getBtnCofirm().setEnabled(false);
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			
		}
	};
	
	/**
	 * Validate data
	 * @return
	 */
	private boolean validInput() {
		if (userInfoModel.getEdtProfileNewPass().getText().toString().equals
				(userInfoModel.getEdtProfileReNewPass().getText().toString())) {
			return true;
		}
		return false;
	}
	
	/**
	 * Update user name, password and site
	 */
	private void updateAppPrefs() {
		StringBuffer strBuffAppPre = new StringBuffer();
		String site = _appPrefs.getSite();
		String userName = _appPrefs.getUsername();
		String profilePin = _appPrefs.getPIN();
		if (site != null && !site.trim().equals(GlobalParams.BLANK_CHARACTER)) {
			strBuffAppPre.append(site + ":");
		} else {
			strBuffAppPre.append(GlobalParams.WAREHOUSE + ":");
		}
		
		if (userName != null && !userName.trim().equals(GlobalParams.BLANK_CHARACTER)) {
			strBuffAppPre.append(userName + ":");
			
		}
		if (profilePin != null && !profilePin.trim().equals(GlobalParams.BLANK_CHARACTER)) {
			strBuffAppPre.append(profilePin);
			
		}
		
		LoginActivity.valueAuthorization = GlobalParams.BASIC + Base64.encodeBytes(strBuffAppPre.toString().getBytes());
	}
	
	/**
	 * Load data
	 */
	private void refreshData() {
		LoadProfileVersionAsyn mLoadDataTask = new LoadProfileVersionAsyn(UserProfileActivity.this);
		//mLoadDataTask.setMoveTopOnComplete(isMoveToTop);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mLoadDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			mLoadDataTask.execute();
		}
	}
	
	/**
	 * Get Profile version
	 * @return
	 * @throws Exception
	 */
	private String getVersion() throws Exception {
		return HttpNetServices.Instance.getProfileVersion();
	}
	
	/**
	 * Refresh data pin when change
	 */
	private void refreshDataPinChange() {
		SubmitPinChangenAsyn mLoadDataTask = new SubmitPinChangenAsyn(UserProfileActivity.this);
		//mLoadDataTask.setMoveTopOnComplete(isMoveToTop);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mLoadDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			mLoadDataTask.execute();
		}
	}
	
	/**
	 * Submit pin after change
	 * @author hoangnh11
	 */
	private class SubmitPinChangenAsyn extends AsyncTask<Void, Void, Integer>{
		
		Context context;
		ProgressDialog progressDialog;
		int errorCode = 0;
		
		public SubmitPinChangenAsyn(Context mContext){
			this.context = mContext;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage(getLanguage(GlobalParams.LOADING_MSG, GlobalParams.LOADING_DATA));
			progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					cancel(true);
				}
			});
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(true);
			progressDialog.show();
			
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			if(!Utilities.isConnected(context)){
				return 1; 
			}
			
			try{
				if(!isCancelled()) {
					NetParameter[] netParameters = new NetParameter[1];
					netParameters[0] = new NetParameter("pin", userInfoModel.getEdtProfileNewPass().getText().toString());
					String response=  HttpNetServices.Instance.updateProfilePin(netParameters);				
				}
			} catch (Exception e) {
				errorCode = 2;
			}
			
			return errorCode;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			
			if (null != progressDialog) {
				progressDialog.dismiss();
			}
			
			if (!isCancelled()){
				if (errorCode == 1) {
					CommontDialog.showErrorDialog(context, GlobalParams.NETWORK_ERROR, GlobalParams.NETWORK_ERROR);
				} else if (userInfoModel.getEdtProfileCurrentPass().getText().toString().equalsIgnoreCase
						(userInfoModel.getEdtProfileNewPass().getText().toString())) {
					Log.e("1111111111111", ": " + userInfoModel.getEdtProfileCurrentPass().getText().toString()
							+ ": " + userInfoModel.getEdtProfileNewPass().getText().toString());
					CommontDialog.showErrorDialog(context, GlobalParams.INVALID_CURRENT_PASSWORD, GlobalParams.ERROR_STRING);
				} else if (errorCode == 2) {
					CommontDialog.showErrorDialog(context, GlobalParams.PASSWORD_NOT_MATCHED, GlobalParams.ERROR_STRING);
				} else {
					CommontDialog.showErrorDialog(context, GlobalParams.PIN_CHANGED_SUCCESS, GlobalParams.CHANGE_SUCCESS);
					_appPrefs.savePIN(userInfoModel.getEdtProfileNewPass().getText().toString());
					updateAppPrefs();
					userInfoModel.getEdtProfileCurrentPass().setText(GlobalParams.BLANK_CHARACTER);
					userInfoModel.getEdtProfileNewPass().setText(GlobalParams.BLANK_CHARACTER);
					userInfoModel.getEdtProfileReNewPass().setText(GlobalParams.BLANK_CHARACTER);
				}
			}
		}
	}
	
	/**
	 * Load Profile version
	 * @author hoangnh11
	 */
	private class LoadProfileVersionAsyn extends AsyncTask<Void, Void, Integer>{
		
		Context context;
		ProgressDialog progressDialog;
		String profileVersion;
		
		public LoadProfileVersionAsyn(Context mContext){
			this.context = mContext;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();			
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage(getLanguage(GlobalParams.LOADING_MSG, GlobalParams.LOADING_DATA));
			progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					cancel(true);
				}
			});
			
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(true);
			progressDialog.show();			
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			int errorCode = 0;
			if(!Utilities.isConnected(context)){
				return 1; 
			}
			
			try{
				if(!isCancelled()) {
					profileVersion = getVersion();
				}
			} catch (Exception e) {
				profileVersion = GlobalParams.BLANK_CHARACTER;
				errorCode = 2;
			}
			
			return errorCode;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			userInfoModel.getTvVersion().setText(getLanguage(GlobalParams.USERPROFILE_LBL_VERSION, GlobalParams.VERSION)
					+ GlobalParams.SPACE + profileVersion);
			if (null != progressDialog) {
				progressDialog.dismiss();
			}
		}
	}
}
