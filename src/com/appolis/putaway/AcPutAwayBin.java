/**
 * Name: AcPutAwayBin.java
 * Date: Jan 28, 2015 2:25:15 PM
 * Copyright (c) 2014 FPT Software, Inc. All rights reserved.
 */
package com.appolis.putaway;

import java.net.URLEncoder;
import java.util.ArrayList;

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
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.appolis.adapter.PutAwayBinAdapter;
import com.appolis.androidtablet.R;
import com.appolis.common.AppolisException;
import com.appolis.common.LanguagePreferences;
import com.appolis.entities.EnBarcodeExistences;
import com.appolis.entities.EnPassPutAway;
import com.appolis.entities.EnPutAway;
import com.appolis.entities.EnPutAwayBin;
import com.appolis.network.NetParameter;
import com.appolis.network.access.HttpNetServices;
import com.appolis.scan.CaptureBarcodeCamera;
import com.appolis.scan.SingleEntryApplication;
import com.appolis.utilities.DataParser;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.Logger;
import com.appolis.utilities.Utilities;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * @author hoangnh11
 */
public class AcPutAwayBin extends Activity implements OnClickListener, OnItemClickListener{
	
	private TextView tvHeader;
	private ImageView imgHome, imgScan;
	private Button btnCancel, btnOK;
	private String message;
	private ProgressDialog dialog;
	private EditText edtItem;
	private ImageView imgClear;
	private PullToRefreshListView lsPutAway;
	private PutAwayBinAdapter adapterPutAway;
	private EnBarcodeExistences enBarcodeExistences;
	private ArrayList<EnPutAwayBin> enPutAwayBin;
	private int positonItem;
	private Bundle bundle;
	private String itemNumber;
	private LanguagePreferences languagePrefs;
	private TextView tvSelect;
	private int checkPos = -1;
	private EnPutAway passPutAway;
	private EnPassPutAway enPassPutAway;
	private String scanFlag;
	
	private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds
	long lastClickTime = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		languagePrefs = new LanguagePreferences(getApplicationContext());
		setContentView(R.layout.put_away);		
		intLayout();
	}
	
	/**
	 * instance layout
	 */
	@SuppressWarnings("unchecked")
	private void intLayout() {
		passPutAway = new EnPutAway();
		enPassPutAway = new EnPassPutAway();
		enBarcodeExistences = new EnBarcodeExistences();	
		bundle = this.getBundle();
		enPutAwayBin = new ArrayList<>();
		enPutAwayBin = ((ArrayList<EnPutAwayBin>) bundle.getSerializable(GlobalParams.PUT_AWAY_BIN));
		itemNumber = bundle.getString(GlobalParams._ITEMNUMBER);
		passPutAway = (EnPutAway) bundle.getSerializable(GlobalParams.PUT_AWAY_BIN_DATA);
		enPassPutAway = (EnPassPutAway) bundle.getSerializable(GlobalParams.PUT_PASS_AWAY_BIN_DATA);
		
		tvHeader = (TextView) findViewById(R.id.tvHeader);
		tvHeader.setText(GlobalParams.PUT_AWAY_BINS);
		imgHome = (ImageView) findViewById(R.id.imgHome);
		imgHome.setVisibility(View.VISIBLE);
		imgScan = (ImageView) findViewById(R.id.img_main_menu_scan_barcode);		
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnOK = (Button) findViewById(R.id.btnOK);
		edtItem = (EditText) findViewById(R.id.edtItem);
		edtItem.setHint(getResources().getString(R.string.SCAN_BIN));
		imgClear = (ImageView) findViewById(R.id.imgClear);
		tvSelect = (TextView) findViewById(R.id.tvSelect);
		
		tvSelect.setText(getLanguage(GlobalParams.SELECT, GlobalParams.SELECT));
		edtItem.setHint(getLanguage(GlobalParams.SCANITEM, GlobalParams.SCAN_BIN));
		
		imgHome.setOnClickListener(this);
		imgScan.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnOK.setOnClickListener(this);
		imgClear.setOnClickListener(this);
		
		edtItem.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 0) {
					imgClear.setVisibility(View.VISIBLE);
					if(null != adapterPutAway){
						adapterPutAway.getFilter().filter(s.toString());
					}
					
				} else {
					imgClear.setVisibility(View.GONE);
					adapterPutAway.getFilter().filter(GlobalParams.BLANK_CHARACTER);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		lsPutAway = (PullToRefreshListView) findViewById(R.id.lsPutAway);
		lsPutAway.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		lsPutAway.setOnItemClickListener(this);
		lsPutAway.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {				
				String label = DateUtils.formatDateTime(AcPutAwayBin.this,	System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE	| DateUtils.FORMAT_ABBREV_ALL);
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				PutawayAsyncTask putawayAsyncTask = new PutawayAsyncTask();
		        putawayAsyncTask.execute();
			}
		});

		adapterPutAway = new PutAwayBinAdapter(AcPutAwayBin.this, enPutAwayBin);
		lsPutAway.setAdapter(adapterPutAway);
		edtItem.setText(GlobalParams.BLANK_CHARACTER);
		adapterPutAway.getFilter().filter(GlobalParams.BLANK_CHARACTER);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parenView, View view, int position, long id) {
//		Logger.error(String.valueOf(position));
//		positonItem = position - 1;
//		if (checkPos == positonItem) {
//			checkPos = -1;
//		} else {
//			BarcodeAsyncTask barcodeAsyncTask = new BarcodeAsyncTask(enPutAwayBin.get(positonItem).get_binNumber());
//			barcodeAsyncTask.execute();
//			checkPos = positonItem;
//		}
		
		long clickTime = System.currentTimeMillis();
		
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
        	Logger.error("1111111111111111111111111111111:    " + clickTime);
        } else {
        	Logger.error("2222222222222222222222222222222222222    " + lastClickTime);
        	BarcodeAsyncTask barcodeAsyncTask = new BarcodeAsyncTask(enPutAwayBin.get(positonItem).get_binNumber());
			barcodeAsyncTask.execute();			
        }
        
        lastClickTime = clickTime;
	}
	
	/**
	 * get language from language package
	 */
	public String getLanguage(String key, String value){
		return languagePrefs.getPreferencesString(key, value);
	}
	
	/**
	 * Process event for views
	 */
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		
		case R.id.imgHome:
			Utilities.hideKeyboard(this);
			finish();
			break;
			
		case R.id.btnCancel:
			Utilities.hideKeyboard(this);
			finish();
			break;
			
		case R.id.btnOK:

			break;
			
		case R.id.img_main_menu_scan_barcode:
			intent = new Intent(this, CaptureBarcodeCamera.class);		
			startActivityForResult(intent, GlobalParams.AC_PUT_AWAY_BIN_LEVEL_ONE);
			break;
			
		case R.id.imgClear:
			edtItem.setText(GlobalParams.BLANK_CHARACTER);
			adapterPutAway.getFilter().filter(GlobalParams.BLANK_CHARACTER);
			break;

		default:
			break;
		}
	}
	
	/**
	 * Process activity
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case GlobalParams.AC_PUT_AWAY_BIN_LEVEL_ONE:
			
			if(resultCode == RESULT_OK){
			   message = data.getStringExtra(GlobalParams.RESULTSCAN);			 
			   BarcodeAsyncTask barcodeAsyncTask = new BarcodeAsyncTask(message);
			   barcodeAsyncTask.execute();
			}
			
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
	        	imgScan.setVisibility(View.GONE);	
	        	scanFlag = GlobalParams.FLAG_ACTIVE;
	        }
	        
	        // a Scanner has disconnected
	        else if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_SCANNER_REMOVAL))
	        {
	        	imgScan.setVisibility(View.VISIBLE);
	        }
	        
	        // decoded Data received from a scanner
	        else if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_DECODED_DATA))
	        {
				char[] data = intent.getCharArrayExtra(SingleEntryApplication.EXTRA_DECODEDDATA);
				if (scanFlag.equals(GlobalParams.FLAG_ACTIVE)) {
					BarcodeAsyncTask barcodeAsyncTask = new BarcodeAsyncTask(new String(data));
		            barcodeAsyncTask.execute();
				}
	        }
	    }
	};

	/**
	 * Register Receiver
	 */
	public void onRegisterReceiver() {
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
	
	/**
	 * Unregister Receiver
	 */
	public void onUnregisterReceiver() {
		// unregister the notifications
		unregisterReceiver(_newItemsReceiver);        
        // indicate this view has been destroyed
        // if the reference count becomes 0 ScanAPI can
        // be closed if this is not a screen rotation scenario
        SingleEntryApplication.getApplicationInstance().decreaseViewCount();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		onRegisterReceiver();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		onUnregisterReceiver();
	}
	
	/**
	 * Check Item or License Plate
	 * @author hoangnh11
	 */
	class BarcodeAsyncTask extends AsyncTask<Void, Void, String> {
		String data, _barCode;
		Intent intent;
		
		private BarcodeAsyncTask(String barCode){
			_barCode = barCode;			
		}
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(AcPutAwayBin.this);
			dialog.setMessage(getLanguage(GlobalParams.LOADING_MSG, GlobalParams.LOADING_DATA));
			dialog.show();
			dialog.setCancelable(false); 
			dialog.setCanceledOnTouchOutside(false);
			scanFlag = GlobalParams.FLAG_INACTIVE;
		}
		
		@Override
		protected String doInBackground(Void... params) {
			String result;
			if (!isCancelled()) {
				try {
					NetParameter[] netParameter = new NetParameter[1];
					netParameter[0] = new NetParameter("barcode", URLEncoder.encode(_barCode, GlobalParams.UTF_8));
					data = HttpNetServices.Instance.getBarcode(netParameter);
					enBarcodeExistences = DataParser.getBarcode(data);					
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
					if (enBarcodeExistences != null) {
						if (enBarcodeExistences.getBinOnlyCount() == 0 
								&& enBarcodeExistences.getGtinCount() == 0
								&& enBarcodeExistences.getItemIdentificationCount() == 0 
								&& enBarcodeExistences.getItemOnlyCount() == 0
								&& enBarcodeExistences.getLotOnlyCount() == 0
								&& enBarcodeExistences.getLPCount() == 0
								&& enBarcodeExistences.getOrderCount() == 0
								&& enBarcodeExistences.getPoCount() == 0
								&& enBarcodeExistences.getUOMBarcodeCount() == 0) {
							showPopUp(AcPutAwayBin.this, null,
									getLanguage(GlobalParams.SCAN_NOTFOUND_VALUE,
											GlobalParams.SCAN_NOTFOUND_VALUE));
						} else if (enBarcodeExistences.getOrderCount() != 0 || enBarcodeExistences.getLotOnlyCount() != 0
								|| enBarcodeExistences.getPoCount() != 0) {
							showPopUp(AcPutAwayBin.this, null,
									getLanguage(GlobalParams.PLEASE_SCAN_BIN_OR_LP,
											GlobalParams.PLEASE_SCAN_BIN_OR_LP));
						} else if (enBarcodeExistences.getBinOnlyCount() != 0 || enBarcodeExistences.getLPCount() != 0) {
							intent = new Intent(AcPutAwayBin.this, AcPutAwayDetails.class);
							intent.putExtra(GlobalParams.BARCODE_MOVE, _barCode);
							intent.putExtra(GlobalParams.CHECK_LP_OR_NOT_LP, GlobalParams.FALSE);
							intent.putExtra(GlobalParams.CHECK_BIN_OR_NOT_BIN, GlobalParams.TRUE);
							intent.putExtra(GlobalParams.PUT_AWAY_BIN_DATA, passPutAway);
							intent.putExtra(GlobalParams.PUT_PASS_AWAY_BIN_DATA, enPassPutAway);
							startActivity(intent);
							scanFlag = GlobalParams.FLAG_ACTIVE;
						} else {
							showPopUp(AcPutAwayBin.this, null,
									getLanguage(GlobalParams.PLEASE_SCAN_BIN_OR_LP,
											GlobalParams.PLEASE_SCAN_BIN_OR_LP));
						}
						
					} else {
						showPopUp(AcPutAwayBin.this, null, GlobalParams.INVALID_SCAN);
					}
				} else {
					showPopUp(AcPutAwayBin.this, null, GlobalParams.INVALID_SCAN);
				}
			} else {
				scanFlag = GlobalParams.FLAG_ACTIVE;
			}
		}
	}
	
	/**
	 * 
	 * @param mContext
	 * @param newClass
	 * @param strMessages
	 */
	public void showPopUp(final Context mContext,
			final Class<?> newClass, final String strMessages) {
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
	 * Check Item or License Plate
	 * @author hoangnh11
	 */
	class PutawayAsyncTask extends AsyncTask<Void, Void, String> {
				
		@Override
		protected void onPreExecute() {
		}
		
		@Override
		protected String doInBackground(Void... params) {
			String result;
			result = GlobalParams.TRUE;
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			lsPutAway.onRefreshComplete();
			adapterPutAway = new PutAwayBinAdapter(AcPutAwayBin.this, enPutAwayBin);
			lsPutAway.setAdapter(adapterPutAway);
			edtItem.setText(GlobalParams.BLANK_CHARACTER);
			adapterPutAway.getFilter().filter(GlobalParams.BLANK_CHARACTER);
		}
	}
	
	/**
	 * instance bundle
	 * @return
	 */
	public Bundle getBundle()
    {
        Bundle bundle = this.getIntent().getExtras();
        
        if (bundle == null)
        {
            bundle = new Bundle();
        }
        
        return bundle;
    }
}
