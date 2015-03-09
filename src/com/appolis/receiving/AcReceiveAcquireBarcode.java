/**
 * Name: $RCSfile: AcReceiveAcquireBarcode.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.receiving;

import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.appolis.androidtablet.R;
import com.appolis.common.ErrorCode;
import com.appolis.common.LanguagePreferences;
import com.appolis.entities.EnPurchaseOrderItemInfo;
import com.appolis.entities.EnUom;
import com.appolis.network.NetParameter;
import com.appolis.network.access.HttpNetServices;
import com.appolis.scan.CaptureBarcodeCamera;
import com.appolis.scan.SingleEntryApplication;
import com.appolis.utilities.DataParser;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.Logger;
import com.appolis.utilities.StringUtils;
import com.appolis.utilities.Utilities;

public class AcReceiveAcquireBarcode extends Activity implements OnClickListener{
	private LinearLayout linBack;
	private LinearLayout linScan;
	private TextView tvHeader;
	private TextView tvAcquireScanOrEnterBarCode;
	private TextView tvItemName;
	private TextView tvItemTitle;
	private TextView tvItemDescription;
	private EditText edtBarcodeValue;
	private Spinner spnMoveUOM;
	private Button btCancel;
	private Button btOk;
	
	private EnPurchaseOrderItemInfo enPurchaseOrderItemInfo;
	private LanguagePreferences languagePrefs;
	private ArrayList<EnUom> enUom;
	private String scanFlag = "";
	private ProgressDialog dialog;
	private String textLoading;
	private boolean activityIsRunning = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_receive_acquire_barcode);
		activityIsRunning = true;
		scanFlag = GlobalParams.FLAG_ACTIVE;
		
		Bundle bundle = getIntent().getExtras();
		if (bundle.containsKey(GlobalParams.PARAM_EN_PURCHASE_ORDER_ITEM_INFOS)) {
			enPurchaseOrderItemInfo = (EnPurchaseOrderItemInfo) bundle.get(GlobalParams.PARAM_EN_PURCHASE_ORDER_ITEM_INFOS);
		}
		
		languagePrefs = new LanguagePreferences(getApplicationContext());
		getLanguage();
		initLayout();
	}
	
	/**
	 * initial layout of screen
	 */
	@SuppressLint("DefaultLocale") private void initLayout(){
		linBack = (LinearLayout) findViewById(R.id.lin_buton_home);
		linBack.setVisibility(View.VISIBLE);
		linBack.setOnClickListener(this);
		linScan = (LinearLayout) findViewById(R.id.lin_buton_scan);
		linScan.setOnClickListener(this);
		linScan.setVisibility(View.VISIBLE);
		tvHeader = (TextView) findViewById(R.id.tv_header);
		tvHeader.setText(languagePrefs.getPreferencesString(
				GlobalParams.ACQUIRE_BARCODE_KEY, GlobalParams.ACQUIRE_BARCODE_VALUE));
		
		tvAcquireScanOrEnterBarCode = (TextView) findViewById(R.id.tvAcquireScanOrEnterBarCode);
		tvAcquireScanOrEnterBarCode.setText(languagePrefs.getPreferencesString(
				GlobalParams.SCAN_OR_ENTER_BARCODE_KEY, GlobalParams.SCAN_OR_ENTER_BARCODE_VALUE));
		tvItemTitle = (TextView) findViewById(R.id.tvTitleItemName);
		tvItemTitle.setText(languagePrefs.getPreferencesString(
				GlobalParams.ITEM_KEY, GlobalParams.ITEM_VALUE).toUpperCase() + ": ");
		
		tvItemName = (TextView) findViewById(R.id.tvItemName);
		tvItemDescription = (TextView) findViewById(R.id.tvItemDescription);
		edtBarcodeValue =(EditText) findViewById(R.id.edtBarcodeValue);
		spnMoveUOM = (Spinner) findViewById(R.id.spn_Acquire_UOM);
		btCancel = (Button) findViewById(R.id.btnCancel);
		btCancel.setText(languagePrefs.getPreferencesString(GlobalParams.CANCEL, GlobalParams.CANCEL));
		btCancel.setOnClickListener(this);
		btOk = (Button) findViewById(R.id.btnOK);
		btOk.setText(languagePrefs.getPreferencesString(GlobalParams.ADD_KEY, GlobalParams.ADD_VALUE));
		btOk.setOnClickListener(this);
		
		edtBarcodeValue.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					linScan.setVisibility(View.VISIBLE);
					scanFlag = GlobalParams.FLAG_ACTIVE;
				} else {
					linScan.setVisibility(View.INVISIBLE);
					scanFlag = GlobalParams.FLAG_INACTIVE;
				}
			}
		});
		
		edtBarcodeValue.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(null == s || StringUtils.isBlank(s.toString())){
					btOk.setEnabled(false);
				} else {
					btOk.setEnabled(true);
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		if(null != enPurchaseOrderItemInfo){
			tvItemName.setText(enPurchaseOrderItemInfo.get_itemNumber());
			tvItemDescription.setText(enPurchaseOrderItemInfo.get_itemDesc());
			
			GetDataAsyncTask getDataAsyncTask = new GetDataAsyncTask(this, enPurchaseOrderItemInfo.get_itemNumber());
			getDataAsyncTask.execute();
		} else {
			// get bundle error
		}
	}
	
	/**
	 * get language from language package
	 */
	public void getLanguage(){
		textLoading = languagePrefs.getPreferencesString(GlobalParams.MAINLIST_MENLOADING_KEY, GlobalParams.MAINLIST_MENLOADING_VALUE);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lin_buton_home:
			Utilities.hideKeyboard(this);
			this.finish();
			break;
			
		case R.id.lin_buton_scan:
			Intent intentScan = new Intent(this, CaptureBarcodeCamera.class);
			startActivityForResult(intentScan,
					GlobalParams.CAPTURE_BARCODE_CAMERA_ACTIVITY);
			break;
			
		case R.id.btnCancel:
			this.finish();
			break;
			
		case R.id.btnOK:
			ProcessOkClickAsyn asyn = new ProcessOkClickAsyn(this);
			asyn.execute();
			break;
			
		default:
			break;
		}
		
	}
	
	/**
	 * handler for receiving the notifications coming from 
	 * SingleEntryApplication.
	 * Update the UI accordingly when we receive a notification
	 */
	private final BroadcastReceiver _newItemsReceiver = new BroadcastReceiver() {   
	    
		@Override  
	    public void onReceive(Context context, Intent intent) {
		
	        // a Scanner has connected
	        if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_SCANNER_ARRIVAL))
	        {
	        	linScan.setVisibility(View.GONE);
	        }
	        
	        // a Scanner has disconnected
	        else if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_SCANNER_REMOVAL))
	        {
	        	linScan.setVisibility(View.VISIBLE);
	        }
	        
	        // decoded Data received from a scanner
	        else if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_DECODED_DATA))
	        {
				char[] data = intent.getCharArrayExtra(SingleEntryApplication.EXTRA_DECODEDDATA);
				if (scanFlag.equals(GlobalParams.FLAG_ACTIVE)) {
					String message = new String(data);
					edtBarcodeValue.setText(message.trim());
				}
	        }
	    }
	};
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		activityIsRunning = true;
		// register to receive notifications from SingleEntryApplication
        // these notifications originate from ScanAPI 
        IntentFilter filter;

        filter = new IntentFilter(SingleEntryApplication.NOTIFY_SCANNER_ARRIVAL);
        registerReceiver(this._newItemsReceiver, filter);

        filter = new IntentFilter(SingleEntryApplication.NOTIFY_SCANNER_REMOVAL);   
        registerReceiver(this._newItemsReceiver, filter);

        filter = new IntentFilter(SingleEntryApplication.NOTIFY_DECODED_DATA);
        registerReceiver(this._newItemsReceiver, filter);
        
    	// increasing the Application View count from 0 to 1 will
    	// cause the application to open and initialize ScanAPI
    	SingleEntryApplication.getApplicationInstance().increaseViewCount();
	}
	
	@Override
	protected void onPause() {
		super.onPause();	
		activityIsRunning = false;
        // unregister the notifications
		unregisterReceiver(_newItemsReceiver);        
        // indicate this view has been destroyed
        // if the reference count becomes 0 ScanAPI can
        // be closed if this is not a screen rotation scenario
        SingleEntryApplication.getApplicationInstance().decreaseViewCount();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		activityIsRunning = true;
		switch (requestCode) {
		case GlobalParams.CAPTURE_BARCODE_CAMERA_ACTIVITY:
			if (resultCode == RESULT_OK) {
				String message = data.getStringExtra(GlobalParams.RESULTSCAN);
				edtBarcodeValue.setText(message.trim());
			}
			break;

		default:
			break;
		}
	}
	
	/**
	 * show alert dialog
	 * @param mContext
	 * @param strMessages
	 */
	public void showPopUp(final Context mContext, final String strMessages) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog_NoTitle);
		dialog.setContentView(R.layout.dialogwarning);
		// set the custom dialog components - text, image and button		
		TextView text2 = (TextView) dialog.findViewById(R.id.tvScantitle2);		
		text2.setText(strMessages);
		
		LanguagePreferences langPref = new LanguagePreferences(mContext);
		Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
		dialogButtonOk.setText(langPref.getPreferencesString(GlobalParams.OK, GlobalParams.OK));
		
		dialogButtonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				scanFlag = GlobalParams.FLAG_ACTIVE;
			}
		});
		dialog.show();
	}
	
	/**
	 * 
	 * @author Do Thin
	 *
	 */
	private class ProcessOkClickAsyn extends AsyncTask<Void, Void, Integer> {
		Context context;
		String response;
		public ProcessOkClickAsyn(Context mContext){
			this.context = mContext;
		}
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(context);
			dialog.setMessage(textLoading + "...");
			dialog.show();
			dialog.setCancelable(false); 
			dialog.setCanceledOnTouchOutside(false);
			scanFlag = GlobalParams.FLAG_INACTIVE;
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			int resultCode = ErrorCode.STATUS_FAIL;
			if(!Utilities.isConnected(context)){
				return ErrorCode.STATUS_NETWORK_NOT_CONNECT;
			}
			
			try{
				if(!isCancelled()){
					NetParameter[] netParameters = new NetParameter[3];
					netParameters[0] = new NetParameter("itemNumber", tvItemName.getText().toString());
					netParameters[1] = new NetParameter("uomDesc", spnMoveUOM.getSelectedItem().toString());
					netParameters[2] = new NetParameter("barcodeNumber", edtBarcodeValue.getText().toString().trim());
					
					response = HttpNetServices.Instance.putAcquireBarcode(netParameters);
					Logger.error("Acquire barcode commit respone: " + response);
					if(StringUtils.isBlank(response)){
						resultCode = ErrorCode.STATUS_SUCCESS;
					} else {
						resultCode = ErrorCode.STATUS_FAIL;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				resultCode = ErrorCode.STATUS_EXCEPTION;
			}
			return resultCode;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(null != dialog && activityIsRunning){
				dialog.dismiss();
			}
			
			if(!isCancelled() && activityIsRunning){
				switch (result) {
				case ErrorCode.STATUS_SUCCESS:
					scanFlag = GlobalParams.FLAG_ACTIVE;
					edtBarcodeValue.setText("");
					break;
				case ErrorCode.STATUS_FAIL:
					showPopUp(context, response);
					break;
					
				default:
					String msg = languagePrefs.getPreferencesString(
							GlobalParams.ERRORUNABLETOCONTACTSERVER, GlobalParams.ERROR_INVALID_NETWORK);
					showPopUp(context, msg);
					break;
				}
			} else {
				scanFlag = GlobalParams.FLAG_ACTIVE;
			}
		}
	}
	
	/**
	 * get UOM data of item
	 * @author HoangNH11
	 *
	 */
	private class GetDataAsyncTask extends AsyncTask<Void, Void, String> {
		Context context;
		String itemNumber;
		String dataUOM;
		int errorCode = 0;
		
		public GetDataAsyncTask(Context mContext, String barCode) {
			// TODO Auto-generated constructor stub
			this.context = mContext;
			this.itemNumber = barCode;
		}
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(context);
			dialog.setMessage(textLoading + "...");
			dialog.show();
			dialog.setCancelable(false); 
			dialog.setCanceledOnTouchOutside(false);
		}
		
		@Override
		protected String doInBackground(Void... params) {
			String result;
			
			if(!Utilities.isConnected(context)){
				errorCode = ErrorCode.STATUS_NETWORK_NOT_CONNECT;
				return "false";
			}
			
			if (!isCancelled()) {
				try {
					NetParameter[] netParameterUOM = new NetParameter[1];
					netParameterUOM[0] = new NetParameter("itemNumber", itemNumber);
					dataUOM = HttpNetServices.Instance.getUOMItemNumber(netParameterUOM);
					enUom =  DataParser.getUom(dataUOM);
					result = "true";
				} catch (Exception e) {
					result = "false";
					errorCode = ErrorCode.STATUS_EXCEPTION;
					if (e instanceof SocketTimeoutException
							|| e instanceof SocketException
							|| e instanceof ClientProtocolException
							|| e instanceof ConnectTimeoutException
							|| e instanceof UnknownHostException
							|| e instanceof MalformedURLException) {
						errorCode = ErrorCode.STATUS_NETWORK_NOT_CONNECT;
					} else if (e instanceof URISyntaxException) {
						errorCode = ErrorCode.STATUS_SCAN_ERROR;
					}
				}
			} else {
				result = "false";
				errorCode = ErrorCode.STATUS_NETWORK_NOT_CONNECT;
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if(null != dialog && activityIsRunning){
				dialog.dismiss();
			}
			
			// If not cancel by user
			if (!isCancelled() && activityIsRunning) {
				if (result.equals("true")) {
					if (enUom != null) {
						
						ArrayList<String> listUom = new ArrayList<String>();
						for (int i = 0; i < enUom.size(); i++) {
							listUom.add(enUom.get(i).getUomDescription());
						}
						
						ArrayAdapter<String> uomAdapter = new ArrayAdapter<String>(context,
								R.layout.custom_spinner_item, listUom);
						spnMoveUOM.setAdapter(uomAdapter);
					} else {
						showPopUp(context, getResources().getString(R.string.LOADING_FAIL));
					}
				} else {
					switch (errorCode) {
						case ErrorCode.STATUS_NETWORK_NOT_CONNECT:
							String msg = languagePrefs.getPreferencesString(
									GlobalParams.ERRORUNABLETOCONTACTSERVER, GlobalParams.ERROR_INVALID_NETWORK);
							showPopUp(context, msg);
							break;
						
						case ErrorCode.STATUS_EXCEPTION:
							showPopUp(context, dataUOM);
							break;
							
						default:
							showPopUp(context, getResources().getString(R.string.LOADING_FAIL));
							break;
					}
				}
			}
		}
	}
}
