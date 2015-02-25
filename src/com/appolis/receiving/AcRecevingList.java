/**
 * Name: $RCSfile: AcRecevingList.java,v $
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
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.appolis.adapter.ReceivingListAdapter;
import com.appolis.androidtablet.R;
import com.appolis.common.ErrorCode;
import com.appolis.common.LanguagePreferences;
import com.appolis.entities.EnBarcodeExistences;
import com.appolis.entities.EnReceivingInfo;
import com.appolis.network.NetParameter;
import com.appolis.network.access.HttpNetServices;
import com.appolis.scan.CaptureBarcodeCamera;
import com.appolis.scan.SingleEntryApplication;
import com.appolis.utilities.CommontDialog;
import com.appolis.utilities.DataParser;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.Logger;
import com.appolis.utilities.Utilities;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * @author HoangNH11
 * handle receiving list
 */
public class AcRecevingList extends Activity implements OnClickListener, OnItemClickListener{
	private LinearLayout linBack;
	private LinearLayout linScan;
	private TextView tvHeader;
	private TextView tvReceiveTitle;
	private EditText edtReceiverSearch;
	private ImageView imgClearTextSearch;
	private PullToRefreshListView lvReceiveList;
	
	private ArrayList<EnReceivingInfo> listReceiveInfo = new ArrayList<EnReceivingInfo>();
	private ReceivingListAdapter receivingListAdapter = null;
	private LanguagePreferences languagePrefs;
	private int checkPos = -1;
	
	//text multiple language
	private String strLoading;
	private String strPoEmpty;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_receive_list);
		languagePrefs = new LanguagePreferences(getApplicationContext());
		getLanguage();
		initLayout();
	}
	
	/**
	 * initial layout of screen
	 */
	public void initLayout(){
		linBack = (LinearLayout) findViewById(R.id.lin_buton_home);
		linBack.setOnClickListener(this);
		linScan = (LinearLayout) findViewById(R.id.lin_buton_scan);
		linScan.setOnClickListener(this);
		tvHeader = (TextView) findViewById(R.id.tv_header);
		tvHeader.setText(languagePrefs.getPreferencesString(GlobalParams.RECEIVING_PO_ITEMS_LBLSECTION_KEY,
				GlobalParams.RECEIVING_PO_ITEMS_LBLSECTION_VALUE));
		
		tvReceiveTitle = (TextView) findViewById(R.id.tv_receive_tile);
		tvReceiveTitle.setText(languagePrefs.getPreferencesString(GlobalParams.RE_LBL_RECEIVE1_KEY, 
				GlobalParams.RE_LBL_RECEIVE1_VALUE) + ": ");
		
		edtReceiverSearch = (EditText) findViewById(R.id.txt_receiver_search);
		edtReceiverSearch.setHint(languagePrefs.getPreferencesString(GlobalParams.PICK_TXT_SEARCH_KEY, 
				GlobalParams.PICK_TXT_SEARCH_VALUE));
		edtReceiverSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence text, int start, int before, int count) {
				if (text.length() > 0) {
					imgClearTextSearch.setVisibility(View.VISIBLE);
					if(null != receivingListAdapter){
						receivingListAdapter.getFilter().filter(text.toString());
					}
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		imgClearTextSearch = (ImageView) findViewById(R.id.imgClearTextSearch);
		imgClearTextSearch.setVisibility(View.GONE);
		imgClearTextSearch.setOnClickListener(this);
		
		lvReceiveList = (PullToRefreshListView) findViewById(R.id.lvReceiveList);
		lvReceiveList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		lvReceiveList.setOnItemClickListener(this);
		lvReceiveList.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(AcRecevingList.this,
						System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy()
						.setLastUpdatedLabel(label);
				refreshData();
			}
		});
		
		receivingListAdapter = new ReceivingListAdapter(this, listReceiveInfo);
		lvReceiveList.setAdapter(receivingListAdapter);
		refreshData();
	}
	
	/**
	 * get language from language package
	 */
	public void getLanguage(){
		strLoading = languagePrefs.getPreferencesString(GlobalParams.MAINLIST_MENLOADING_KEY
				, GlobalParams.MAINLIST_MENLOADING_VALUE);
		strPoEmpty = languagePrefs.getPreferencesString(GlobalParams.RE_PO_EMPTY_MSG_KEY, GlobalParams.RE_PO_EMPTY_MSG_VALUE);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.lin_buton_home:
				Utilities.hideKeyboard(this);
				this.finish();
				break;
			
			case R.id.lin_buton_scan:
				Intent intentScan = new Intent(this, CaptureBarcodeCamera.class);
				startActivityForResult(intentScan, GlobalParams.CAPTURE_BARCODE_CAMERA_ACTIVITY);
				break;
			
			case R.id.imgClearTextSearch:
				edtReceiverSearch.setText("");
				receivingListAdapter.getFilter().filter("");
				break;
				
			default:
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parenView, View view, int position, long id) {
		Logger.info("AcRecevingList #onItemClick:" + position);
		if (checkPos == position) {
			checkPos = -1;
		} else {
			EnReceivingInfo enReceivingInfo = receivingListAdapter.getItem(position-1);
			Intent intentReceiveDeail = new Intent(AcRecevingList.this, AcReceivingDetails.class);
			intentReceiveDeail.putExtra(GlobalParams.PARAM_EN_RECIVING_INFO_PO_NUMBER, enReceivingInfo.get_poNumber());
			startActivityForResult(intentReceiveDeail, GlobalParams.AC_RECEIVING_DETAILS_ACTIVITY);
			checkPos = position;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case GlobalParams.CAPTURE_BARCODE_CAMERA_ACTIVITY:
			if(resultCode == RESULT_OK){
				String message = data.getStringExtra(GlobalParams.RESULTSCAN);
				processScanData(message);
			}
			break;
		
		case GlobalParams.AC_RECEIVING_DETAILS_ACTIVITY:
			//if(resultCode == RESULT_OK){
				refreshData();
			//}
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
				String message = new String(data);
				//edtLp.setText(new String(data));
				processScanData(message);
	        }
	    }
	};

	@Override
	protected void onPause() {
		super.onPause();		
        // unregister the notifications
		unregisterReceiver(_newItemsReceiver);        
        // indicate this view has been destroyed
        // if the reference count becomes 0 ScanAPI can
        // be closed if this is not a screen rotation scenario
        SingleEntryApplication.getApplicationInstance().decreaseViewCount();
	}
	
	/**
	 * function to get data from service
	 */
	private void refreshData() {
		LoadReceiveListAsyn mLoadDataTask = new LoadReceiveListAsyn(AcRecevingList.this);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mLoadDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			mLoadDataTask.execute();
		}
	}
	
	/**
	 * load receive data from service
	 * @author HoangNH11
	 *
	 */
	private class LoadReceiveListAsyn extends AsyncTask<Void, Void, Integer>{
		Context context;
		ProgressDialog progressDialog;
		String response;
		
		public LoadReceiveListAsyn(Context mContext){
			this.context = mContext;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(!isCancelled()){
				progressDialog = new ProgressDialog(context);
				progressDialog.setMessage(strLoading + "...");
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
			
		}
		
		@Override
		protected Integer doInBackground(Void... arg0) {
			int errorCode = ErrorCode.STATUS_SUCCESS;
			if(!Utilities.isConnected(context)){
				return ErrorCode.STATUS_NETWORK_NOT_CONNECT; 
			}
			
			try{
				if(!isCancelled()){
					response = HttpNetServices.Instance.getReceiveList();
					listReceiveInfo = DataParser.getListReceiveInfo(response);
					if(null == listReceiveInfo || listReceiveInfo.size() == 0){
						errorCode = ErrorCode.STATUS_FAIL;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				errorCode = ErrorCode.STATUS_EXCEPTION;
				/*if(e instanceof JsonSyntaxException){
					errorCode = ErrorCode.STATUS_NETWORK_NOT_CONNECT;
				}*/
			}
			
			return errorCode;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if(null != progressDialog && (progressDialog.isShowing())){
				progressDialog.dismiss();
			}
			
			if(null != lvReceiveList){
				lvReceiveList.onRefreshComplete();
			}
			
			if(!isCancelled()){
				switch (result) {
				case ErrorCode.STATUS_SUCCESS: //success
					receivingListAdapter.updateListReciver(listReceiveInfo);
					receivingListAdapter.notifyDataSetChanged();
					break;
				
				case ErrorCode.STATUS_FAIL: //Po list empty
					CommontDialog.showErrorDialog(context, strPoEmpty, null);
					break;
					
				case ErrorCode.STATUS_NETWORK_NOT_CONNECT: //no network
					String msg = languagePrefs.getPreferencesString(GlobalParams.ERRORUNABLETOCONTACTSERVER, GlobalParams.ERROR_INVALID_NETWORK);
					CommontDialog.showErrorDialog(context, msg, null);
					break;
					
				case ErrorCode.STATUS_EXCEPTION:
					String dialogMsg = languagePrefs.getPreferencesString( GlobalParams.RD_RETRIEVEPO_MSG_KEY, 
							GlobalParams.RD_RETRIEVEPO_MSG_VALUE);
					CommontDialog.showErrorDialog(context, dialogMsg, null);
					break;
				}
			}
		}
	}
	
	/**
	 * process scan data
	 */
	private void processScanData(String barcode) {
		ProcessScanDataAsyn mLoadDataTask = new ProcessScanDataAsyn(AcRecevingList.this, barcode);
		//mLoadDataTask.setMoveTopOnComplete(isMoveToTop);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mLoadDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			mLoadDataTask.execute();
		}
	}
	
	private class ProcessScanDataAsyn extends AsyncTask<Void, Void, Integer>{
		Context context;
		ProgressDialog progressDialog;
		String response;
		String barcode;
		
		public ProcessScanDataAsyn(Context mContext, String barcode){
			this.context = mContext;
			this.barcode = barcode;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(!isCancelled()){
				progressDialog = new ProgressDialog(context);
				progressDialog.setMessage(strLoading);
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
		}
		
		@Override
		protected Integer doInBackground(Void... arg0) {
			int errorCode = ErrorCode.STATUS_FAIL;
			if(!Utilities.isConnected(context)){
				return ErrorCode.STATUS_NETWORK_NOT_CONNECT; 
			}
			
			try{
				if(!isCancelled()){
					NetParameter[] netParameters = new NetParameter[1];
					netParameters[0] = new NetParameter("barcode", URLEncoder.encode(barcode, GlobalParams.UTF_8));
					response = HttpNetServices.Instance.getBarcode(netParameters);
					Log.e("ProcessScanDataAsyn", "LoadReceiveDetailAsyn #getBarcode #response:" + response);
					EnBarcodeExistences enBarcodeExistences = DataParser.getBarcode(response);	
					if(null != enBarcodeExistences){
						if (enBarcodeExistences.getBinOnlyCount() == 0 
								&& enBarcodeExistences.getGtinCount() == 0
								&& enBarcodeExistences.getItemIdentificationCount() == 0 
								&& enBarcodeExistences.getItemOnlyCount() == 0
								&& enBarcodeExistences.getLotOnlyCount() == 0
								&& enBarcodeExistences.getLPCount() == 0
								&& enBarcodeExistences.getOrderCount() == 0
								&& enBarcodeExistences.getPoCount() == 0
								&& enBarcodeExistences.getUOMBarcodeCount() == 0) {
							//errorscan
							return ErrorCode.STATUS_SCAN_ERROR;
						} else if (enBarcodeExistences.getPoCount() != 0 ) {
							//Item Number, UOM or ItemIdentification
							errorCode = 0;
						} else {
							//Unsupported barcode 
							return ErrorCode.STATUS_SCAN_UNSUPPORTED_BARCODE;
						}
					} else {
						errorCode = ErrorCode.STATUS_EXCEPTION; // exception
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				errorCode = ErrorCode.STATUS_EXCEPTION;
				if(e instanceof SocketTimeoutException || e instanceof  SocketException ||
						e instanceof ClientProtocolException || e instanceof ConnectTimeoutException ||
						e instanceof UnknownHostException || e instanceof MalformedURLException ){
					errorCode = ErrorCode.STATUS_NETWORK_NOT_CONNECT;
				} else if (e instanceof URISyntaxException){
					errorCode = ErrorCode.STATUS_SCAN_ERROR;
				}
			}
			
			return errorCode;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(null != progressDialog && (progressDialog.isShowing())){
				progressDialog.dismiss();
			}
			
			if(!isCancelled()){
				switch (result) {
				case ErrorCode.STATUS_SUCCESS: //success
					Intent intentReceiveDeail = new Intent(AcRecevingList.this, AcReceivingDetails.class);
					intentReceiveDeail.putExtra(GlobalParams.PARAM_EN_RECIVING_INFO_PO_NUMBER, barcode);
					startActivityForResult(intentReceiveDeail, GlobalParams.AC_RECEIVING_DETAILS_ACTIVITY);
					break;
					
				case ErrorCode.STATUS_SCAN_ERROR:
					// error scan
					String strUnSupport = languagePrefs.getPreferencesString(GlobalParams.SCAN_BARCODE_UNSUPPORTED_KEY
							, GlobalParams.SCAN_BARCODE_UNSUPPORTED_VALUE);
					Utilities.showPopUp(context, null, strUnSupport);
					break;
				
				case ErrorCode.STATUS_SCAN_UNSUPPORTED_BARCODE:
					// Unsupported barcode 
					String strErrorScan = languagePrefs.getPreferencesString(
							GlobalParams.RE_SCANPO_MSG_KEY, GlobalParams.RE_SCANPO_MSG_VALUE);
					Utilities.showPopUp(context, null, strErrorScan);
					break;
					
				case ErrorCode.STATUS_NETWORK_NOT_CONNECT: //no network
					String msg = languagePrefs.getPreferencesString(GlobalParams.ERRORUNABLETOCONTACTSERVER, GlobalParams.ERROR_INVALID_NETWORK);
					CommontDialog.showErrorDialog(context, msg, null);
					break;
					
				case ErrorCode.STATUS_EXCEPTION:
					String msgs = response;
					//languagePrefs.getPreferencesString(GlobalParams.ERROR_OCCURRED_KEY, GlobalParams.ERROR_OCCURRED_VALUE);
					CommontDialog.showErrorDialog(context, msgs, null);
					break;
				}
			}
		}
	}
}
