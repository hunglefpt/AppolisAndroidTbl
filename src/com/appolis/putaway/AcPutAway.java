/**
 * Name: AcPutAway.java
 * Date: Jan 26, 2015 11:05:15 AM
 * Copyright (c) 2014 FPT Software, Inc. All rights reserved.
 */
package com.appolis.putaway;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

import com.appolis.adapter.PutAwayAdapter;
import com.appolis.androidtablet.R;
import com.appolis.common.AppolisException;
import com.appolis.common.LanguagePreferences;
import com.appolis.entities.EnBarcodeExistences;
import com.appolis.entities.EnItemNumber;
import com.appolis.entities.EnPassPutAway;
import com.appolis.entities.EnPutAway;
import com.appolis.entities.EnPutAwayBin;
import com.appolis.login.LoginActivity;
import com.appolis.network.NetParameter;
import com.appolis.network.access.HttpNetServices;
import com.appolis.scan.CaptureBarcodeCamera;
import com.appolis.scan.SingleEntryApplication;
import com.appolis.utilities.DataParser;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.Logger;
import com.appolis.utilities.StringUtils;
import com.appolis.utilities.Utilities;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * @author hoangnh11
 * Display Put away screen for search items
 */
public class AcPutAway extends Activity implements OnClickListener, OnItemClickListener {
	
	private TextView tvHeader;
	private ImageView imgHome, imgScan;
	private Button btnCancel, btnOK;
	private String message;
	private ProgressDialog dialog;
	private EditText edtItem;
	private ImageView imgClear;
	private ArrayList<EnPutAway> enPutAway; 
	private PullToRefreshListView lsPutAway;
	private PutAwayAdapter adapterPutAway;
	private EnBarcodeExistences enBarcodeExistences;
	private ArrayList<EnPutAwayBin> enPutAwayBin;
	private int checkPos = -1;
	private LanguagePreferences languagePrefs;
	private TextView tvSelect;
	private ArrayList<EnPutAway> listPutAway;
	private EnPutAway passPutAway;
	private EnItemNumber itemNumber;
	private EnPassPutAway enPassPutAway; 
	private int positonItem;
	private String scanFlag;
	
	private static final long DOUBLE_CLICK_TIME_DELTA = 1000;//milliseconds
	long lastClickTime = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		languagePrefs = new LanguagePreferences(getApplicationContext());
		setContentView(R.layout.put_away);
		scanFlag = GlobalParams.BLANK_CHARACTER;
		intLayout();
		PutawayAsyncTask putawayAsyncTask = new PutawayAsyncTask();
        putawayAsyncTask.execute();
	}
	
	/**
	 * instance layout
	 */
	private void intLayout() {
		enPutAwayBin = new ArrayList<>();
		enPutAway = new ArrayList<EnPutAway>();
		enBarcodeExistences = new EnBarcodeExistences();	
		listPutAway = new ArrayList<>();
		passPutAway = new EnPutAway();
		itemNumber = new EnItemNumber();
		enPassPutAway = new EnPassPutAway();
		
		tvHeader = (TextView) findViewById(R.id.tvHeader);
		tvHeader.setText(getLanguage(GlobalParams.PUTAWAY_TITLE_PUTAWAY, GlobalParams.PUT_AWAY));
		imgHome = (ImageView) findViewById(R.id.imgHome);
		imgHome.setVisibility(View.VISIBLE);
		imgScan = (ImageView) findViewById(R.id.img_main_menu_scan_barcode);		
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnOK = (Button) findViewById(R.id.btnOK);
		edtItem = (EditText) findViewById(R.id.edtItem);
		imgClear = (ImageView) findViewById(R.id.imgClear);
		tvSelect = (TextView) findViewById(R.id.tvSelect);
		
		tvSelect.setText(getLanguage(GlobalParams.SELECT, GlobalParams.SELECT));
		edtItem.setHint(getLanguage(GlobalParams.SCANITEM, GlobalParams.SCAN_ITEM_OR_LICENCE_PLATE));
		
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
				String label = DateUtils.formatDateTime(AcPutAway.this,	System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE	| DateUtils.FORMAT_ABBREV_ALL);
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				PutawayAsyncTask putawayAsyncTask = new PutawayAsyncTask();
		        putawayAsyncTask.execute();
			}
		});
	}

	/**
	 * Process click event in List view
	 */
	@Override
	public void onItemClick(AdapterView<?> parenView, View view, int position, long id) {	
		positonItem = position - 1;
		long clickTime = System.currentTimeMillis();		
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
        	Logger.error("clickTime:    " + clickTime);
        } else {
        	Logger.error("lastClickTime:    " + lastClickTime);
			BarcodeAsyncTask barcodeAsyncTask = new BarcodeAsyncTask
			(((EnPutAway) adapterPutAway.getItem(positonItem)).get_itemNumber(), positonItem,
			((EnPutAway) adapterPutAway.getItem(positonItem)).get_binNumber());
			barcodeAsyncTask.execute();
			passPutAway = ((EnPutAway) adapterPutAway.getItem(positonItem));	
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
			edtItem.setText(GlobalParams.BLANK_CHARACTER);
			adapterPutAway.getFilter().filter(GlobalParams.BLANK_CHARACTER);
			intent = new Intent(this, CaptureBarcodeCamera.class);		
			startActivityForResult(intent, GlobalParams.AC_PUT_AWAY_LEVEL_ONE);
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
		case GlobalParams.AC_PUT_AWAY_LEVEL_ONE:
			
			if(resultCode == RESULT_OK){			
			   message = data.getStringExtra(GlobalParams.RESULTSCAN);
			   BarcodeAsyncTask barcodeAsyncTask = new BarcodeAsyncTask(message, -1, GlobalParams.BLANK_CHARACTER);
			   barcodeAsyncTask.execute();			   
			   for (int i = 0; i < enPutAway.size(); i++) {
					if (((EnPutAway) adapterPutAway.getItem(i)).get_itemNumber().equalsIgnoreCase(message)) {
						passPutAway = ((EnPutAway) adapterPutAway.getItem(i));						
						break;
					}
			   }
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
					String barcodeScaned = new String(data);
					BarcodeAsyncTask barcodeAsyncTask = new BarcodeAsyncTask(barcodeScaned, -1, GlobalParams.BLANK_CHARACTER);
		            barcodeAsyncTask.execute();
		            
		            for (int i = 0; i < enPutAway.size(); i++) {
						if (((EnPutAway) adapterPutAway.getItem(i)).get_itemNumber().equalsIgnoreCase(barcodeScaned)) {					
							passPutAway = ((EnPutAway) adapterPutAway.getItem(i));					
							break;
						}
		            }
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
	 * Unregister Receiver
	 */
	public void unRegisterReceiver(){
		// unregister the notifications
		unregisterReceiver(_newItemsReceiver);        
        // indicate this view has been destroyed
        // if the reference count becomes 0 ScanAPI can
        // be closed if this is not a screen rotation scenario
        SingleEntryApplication.getApplicationInstance().decreaseViewCount();
	}
	
	/**
	 * 
	 * @param l
	 */
	public ArrayList<EnPutAway> removeDuplicates(ArrayList<EnPutAway> list) {
		Logger.error("list size before:" + list.size());
		// Store unique items in result.
		ArrayList<EnPutAway> result = new ArrayList<>();
		
		// Loop over argument list.
		for (int i=0;  i < list.size(); i++) {
			EnPutAway item = list.get(i);
			Logger.error("list result size:" + result.size() + "**:" + item.get_lotNumber());
			
			if(!checObjectExitsInList(result, item)){
				result.add(item);
			}
		}
		
		Logger.error("list size after:" + result.size());
		return result;
	}
	
	private boolean checObjectExitsInList(ArrayList<EnPutAway> list, EnPutAway enPutAway){
		boolean check = false;
		if(null == list || list.size() == 0){
			return false;
		}
		
		for (int i = 0; i < list.size(); i++) {
			EnPutAway item = list.get(i);
			if((item.get_itemID() == enPutAway.get_itemID()) && 
					(item.get_itemNumber().equalsIgnoreCase(item.get_itemNumber())) &&
					(item.get_itemDesc().equalsIgnoreCase(enPutAway.get_itemDesc())) &&
					(StringUtils.isBlank(enPutAway.get_lotNumber()) ||item.get_lotNumber().equalsIgnoreCase(enPutAway.get_lotNumber())) &&
					(item.get_lotTrackingInd() == enPutAway.get_lotTrackingInd()) &&
					(item.get_qty() == enPutAway.get_qty()) &&
					(StringUtils.isBlank(enPutAway.get_qtyDisplay()) || item.get_qtyDisplay().equalsIgnoreCase(enPutAway.get_qtyDisplay())) &&
					(item.is_forcedPutAwayInd() == enPutAway.is_forcedPutAwayInd()) &&
					(StringUtils.isBlank(enPutAway.get_binNumber()) || item.get_binNumber().equalsIgnoreCase(enPutAway.get_binNumber()))){
				check = true;
				break;
			}
		}
		
		return check;
	}
	
	/**
	 * Check Item or License Plate
	 * @author hoangnh11
	 */
	class PutawayAsyncTask extends AsyncTask<Void, Void, String> {
		String data;
		Intent intent;
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(AcPutAway.this);
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
					data = HttpNetServices.Instance.getPutAway();
					ArrayList<EnPutAway> listPaser = new ArrayList<EnPutAway>();
					listPaser = DataParser.getPutAway(data);
					enPutAway = removeDuplicates(listPaser);
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
			try {
				dialog.dismiss();
				// If not cancel by user
				if (!isCancelled()) {
					lsPutAway.onRefreshComplete();
					
					if (result.equals(GlobalParams.TRUE)) {
						if (enPutAway != null) {
							DecimalFormat df = new DecimalFormat("#0.00");
							
							for (int i = 0; i < enPutAway.size(); i++) {
								if (String.valueOf(df.format(enPutAway.get(i).get_qty())).equals("0.00")) {
									enPutAway.remove(i);
								}
							}
							
							listPutAway = new ArrayList<EnPutAway>(enPutAway);
							Collections.sort(listPutAway, new PutAwayComparator());						
							adapterPutAway = new PutAwayAdapter(AcPutAway.this, listPutAway);
							lsPutAway.setAdapter(adapterPutAway);						
							edtItem.setText(GlobalParams.BLANK_CHARACTER);
							adapterPutAway.getFilter().filter(GlobalParams.BLANK_CHARACTER);	
							scanFlag = GlobalParams.FLAG_ACTIVE;
						} else {
							showPopUp(AcPutAway.this, null, getResources().getString(R.string.LOADING_FAIL));
						}
						
					} else {
						String msg = languagePrefs.getPreferencesString
								(GlobalParams.ERRORUNABLETOCONTACTSERVER, GlobalParams.ERROR_INVALID_NETWORK);				
						showPopUp(AcPutAway.this, null, msg);
					}
				} else {
					scanFlag = GlobalParams.FLAG_ACTIVE;
				}
			} catch (Exception e) {			
			}			
		}
	}
	
	/**
	 * Check Item or License Plate
	 * @author hoangnh11
	 */
	class BarcodeAsyncTask extends AsyncTask<Void, Void, String> {
		String data, _barCode, _bin;
		int _pos;
		Intent intent;
		
		private BarcodeAsyncTask(String barCode, int pos, String bin){
			_barCode = barCode;
			_pos = pos;
			_bin = bin;
		}
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(AcPutAway.this);
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
			try {
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
								showPopUp(AcPutAway.this, null,
										getLanguage(GlobalParams.SCAN_NOTFOUND_VALUE,
												GlobalParams.SCAN_NOTFOUND_VALUE));
							} else if (enBarcodeExistences.getOrderCount() != 0 || enBarcodeExistences.getLotOnlyCount() != 0
									|| enBarcodeExistences.getPoCount() != 0) {
								showPopUp(AcPutAway.this, null,
										getLanguage(GlobalParams.JOBPART_VALIDATE_ITEM_OR_LP_VALUE,
												GlobalParams.JOBPART_VALIDATE_ITEM_OR_LP_VALUE));
							} else if ((enBarcodeExistences.getItemOnlyCount() != 0 && !LoginActivity.itemUser.is_showPutAwayBins())
									|| (enBarcodeExistences.getItemIdentificationCount() != 0 && !LoginActivity.itemUser.is_showPutAwayBins())
									|| (enBarcodeExistences.getUOMBarcodeCount() != 0 && !LoginActivity.itemUser.is_showPutAwayBins())) {						
								intent = new Intent(AcPutAway.this, AcPutAwayDetails.class);
								intent.putExtra(GlobalParams.BARCODE_MOVE, _barCode);
								intent.putExtra(GlobalParams.CHECK_LP_OR_NOT_LP, GlobalParams.FALSE);
								intent.putExtra(GlobalParams.CHECK_BIN_OR_NOT_BIN, GlobalParams.FALSE);
								
								if (_pos == -1) {
									intent.putExtra(GlobalParams.LOT_NUMBER, GlobalParams.BLANK_CHARACTER);
									intent.putExtra(GlobalParams.BIN_NUMBER, GlobalParams.BLANK_CHARACTER);
									intent.putExtra(GlobalParams.QTY_NUMBER, GlobalParams.BLANK_CHARACTER);
								} else {
									intent.putExtra(GlobalParams.LOT_NUMBER, enPutAway.get(_pos).get_lotNumber());
									intent.putExtra(GlobalParams.BIN_NUMBER, enPutAway.get(_pos).get_binNumber());
									intent.putExtra(GlobalParams.QTY_NUMBER, String.valueOf(enPutAway.get(_pos).get_qty()));
								}
								
								startActivity(intent);
								scanFlag = GlobalParams.FLAG_ACTIVE;
							} else if ((enBarcodeExistences.getItemOnlyCount() != 0 && LoginActivity.itemUser.is_showPutAwayBins())								
									|| (enBarcodeExistences.getUOMBarcodeCount() != 0 && LoginActivity.itemUser.is_showPutAwayBins())) {					
								GetNotLPDataAsyncTask getNotLPDataAsyncTask = new GetNotLPDataAsyncTask(_barCode, _bin);
								getNotLPDataAsyncTask.execute();
							} else if (enBarcodeExistences.getItemIdentificationCount() != 0 && LoginActivity.itemUser.is_showPutAwayBins()) {
								CheckItemLotNumberAsyncTask checkItemLotNumberAsyncTask = new CheckItemLotNumberAsyncTask(_barCode);
								checkItemLotNumberAsyncTask.execute();
							} else if (enBarcodeExistences.getLPCount() != 0 && LoginActivity.itemUser.is_showPutAwayBins()) {		
								GetLPDataAsyncTask getLPDataAsyncTask = new GetLPDataAsyncTask(_barCode, _bin);
								getLPDataAsyncTask.execute();
							} else if (enBarcodeExistences.getBinOnlyCount() != 0) {
								showPopUp(AcPutAway.this, null,
										getLanguage(GlobalParams.JOBPART_VALIDATE_ITEM_OR_LP_VALUE,
												GlobalParams.JOBPART_VALIDATE_ITEM_OR_LP_VALUE));
							} else {
								Logger.error(data);
							}
							
						} else {
							showPopUp(AcPutAway.this, null, GlobalParams.INVALID_SCAN);
						}
					} else {
						showPopUp(AcPutAway.this, null, GlobalParams.INVALID_SCAN);
					}
				} else {
					scanFlag = GlobalParams.FLAG_ACTIVE;
				}
			} catch (Exception e) {				
			}
		}
	}
	
	/**
	 * Validate Lot number
	 * @author hoangnh11
	 */
	class CheckItemLotNumberAsyncTask extends AsyncTask<Void, Void, String> {
		String data, _barCode, dataTwo;
		Intent intent;
		
		private CheckItemLotNumberAsyncTask(String barCode){
			_barCode = barCode;		
		}
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(AcPutAway.this);
			dialog.setMessage(GlobalParams.LOADING_DATA);
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
					netParameter[0] = new NetParameter("barcode",
							URLEncoder.encode(_barCode.trim(), GlobalParams.UTF_8));
					data = HttpNetServices.Instance.getItemBarcode(netParameter);
					itemNumber = DataParser.getItemNumber(data);
					Logger.error(data);
					
					for (int i = 0; i < enPutAway.size(); i++) {
						if (((EnPutAway) adapterPutAway.getItem(i)).get_itemNumber().equalsIgnoreCase
								(itemNumber.get_itemNumber()) 
								&& ((EnPutAway) adapterPutAway.getItem(i)).get_lotNumber().equalsIgnoreCase
								(itemNumber.get_LotNumber())) {
							passPutAway = ((EnPutAway) adapterPutAway.getItem(i));					
							break;
						}
				    }
					
					result = "true";
				} catch (AppolisException e) {
					result = "false";
				} catch (Exception e) {
					result = "false";
				}
			} else {
				result = "false";
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				dialog.dismiss();
				// If not cancel by user
				if (!isCancelled()) {
					if (result.equals("true")) {
						if (itemNumber != null && itemNumber.get_LotNumber() != null 
								&& StringUtils.isNotBlank(itemNumber.get_LotNumber())) {
							GetPutAwayBinAsyncTask getPutAwayBinAsyncTask = new GetPutAwayBinAsyncTask(itemNumber.get_itemNumber(),
									GlobalParams.BLANK_CHARACTER);
							getPutAwayBinAsyncTask.execute();
						} else {
							showPopUp(AcPutAway.this, null,
									getLanguage(GlobalParams.INVALID_SCAN,	GlobalParams.INVALID_SCAN));
						}
					} else {
						String msg = languagePrefs.getPreferencesString
								(GlobalParams.ERRORUNABLETOCONTACTSERVER, GlobalParams.ERROR_INVALID_NETWORK);				
						showPopUp(AcPutAway.this, null, msg);	
					}
				} else {
					scanFlag = GlobalParams.FLAG_ACTIVE;
				}
			} catch (Exception e) {				
			}			
		}
	}
	
	/**
	 * Get data License plate
	 * @author hoangnh11
	 */
	class GetLPDataAsyncTask extends AsyncTask<Void, Void, String> {
		String data, _barCode, _bin, dataTwo;	
		Intent intent;
		
		private GetLPDataAsyncTask(String barCode, String bin){
			_barCode = barCode;
			_bin = bin;
		}
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(AcPutAway.this);
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
					netParameter[0] = new NetParameter("licensePlateNumber", URLEncoder.encode(_barCode, GlobalParams.UTF_8));
					data = HttpNetServices.Instance.getLpByBarcode(netParameter);
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
			try {
				dialog.dismiss();
				// If not cancel by user
				if (!isCancelled()) {
					if (result.equals(GlobalParams.TRUE)) {
						if (data != null && StringUtils.isNotBlank(data.replace("\"", ""))) {
							GetPutAwayBinAsyncTask getPutAwayBinAsyncTask = new GetPutAwayBinAsyncTask(data.replace("\"", ""), _bin);
							getPutAwayBinAsyncTask.execute();
						} else if (data != null && StringUtils.isBlank(data.replace("\"", ""))) {
							intent = new Intent(AcPutAway.this, AcPutAwayDetails.class);
							intent.putExtra(GlobalParams.BARCODE_MOVE, _barCode);
							intent.putExtra(GlobalParams.CHECK_LP_OR_NOT_LP, GlobalParams.TRUE);
							intent.putExtra(GlobalParams.CHECK_BIN_OR_NOT_BIN, GlobalParams.FALSE);	
							intent.putExtra(GlobalParams.CHECK_LP_BLANK, GlobalParams.TRUE);
							startActivity(intent);
							scanFlag = GlobalParams.FLAG_ACTIVE;
						} else {
							scanFlag = GlobalParams.FLAG_ACTIVE;
						}
						
					} else {
						String msg = languagePrefs.getPreferencesString
								(GlobalParams.ERRORUNABLETOCONTACTSERVER, GlobalParams.ERROR_INVALID_NETWORK);				
						showPopUp(AcPutAway.this, null, msg);
					}
				} else {
					scanFlag = GlobalParams.FLAG_ACTIVE;
				}
			} catch (Exception e) {		
			}			
		}
	}
	
	/**
	 * Get data License plate
	 * @author hoangnh11
	 */
	class GetNotLPDataAsyncTask extends AsyncTask<Void, Void, String> {
		String data, _barCode, _bin, dataTwo;	
		Intent intent;
		
		private GetNotLPDataAsyncTask(String barCode, String bin){
			_barCode = barCode;
			_bin = bin;
		}
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(AcPutAway.this);
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
					netParameter[0] = new NetParameter("licensePlateNumber", URLEncoder.encode(_barCode, GlobalParams.UTF_8));
					data = HttpNetServices.Instance.getLpByBarcode(netParameter);
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
			try {
				dialog.dismiss();
				// If not cancel by user
				if (!isCancelled()) {
					if (result.equals(GlobalParams.TRUE)) {
						if (data != null && StringUtils.isNotBlank(data.replace("\"", ""))) {
							GetPutAwayBinAsyncTask getPutAwayBinAsyncTask = new GetPutAwayBinAsyncTask(data.replace("\"", ""), _bin);
							getPutAwayBinAsyncTask.execute();
						} else if (data != null && StringUtils.isBlank(data.replace("\"", ""))) {
							GetPutAwayBinAsyncTask getPutAwayBinAsyncTask = new GetPutAwayBinAsyncTask(_barCode, _bin);
							getPutAwayBinAsyncTask.execute();
						} else {
							scanFlag = GlobalParams.FLAG_ACTIVE;
						}
						
					} else {
						String msg = languagePrefs.getPreferencesString
								(GlobalParams.ERRORUNABLETOCONTACTSERVER, GlobalParams.ERROR_INVALID_NETWORK);				
						showPopUp(AcPutAway.this, null, msg);
					}
				} else {
					scanFlag = GlobalParams.FLAG_ACTIVE;
				}
			} catch (Exception e) {			
			}
		}
	}
	
	class PutAwayComparator implements Comparator<EnPutAway> {
		
	    @Override
	    public int compare(EnPutAway put1, EnPutAway put2) {
	        return put1.get_itemNumber().compareTo(put2.get_itemNumber());
	    }
	}

	/**
	 * Get data License plate
	 * @author hoangnh11
	 */
	class GetPutAwayBinAsyncTask extends AsyncTask<Void, Void, String> {
		String data, _itemNumber, _binNumber;
		Intent intent;
		
		private GetPutAwayBinAsyncTask(String itemNumber, String binNumber){
			_itemNumber = itemNumber;
			_binNumber = binNumber;
		}
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(AcPutAway.this);
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
					NetParameter[] netParameter = new NetParameter[2];
					netParameter[0] = new NetParameter("itemNumber", _itemNumber);
					netParameter[1] = new NetParameter("binNumber", _binNumber);
					data = HttpNetServices.Instance.getPutAway(netParameter);
					enPutAwayBin = DataParser.getPutAwayBinTrue(data);
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
			try {
				dialog.dismiss();
				// If not cancel by user
				if (!isCancelled()) {
					if (result.equals(GlobalParams.TRUE)) {
						
						intent = new Intent(AcPutAway.this, AcPutAwayBin.class);
						intent.putExtra(GlobalParams.PUT_AWAY_BIN, enPutAwayBin);
						intent.putExtra(GlobalParams.PUT_AWAY_BIN_DATA, passPutAway);
						intent.putExtra(GlobalParams.PUT_PASS_AWAY_BIN_DATA, enPassPutAway);
						startActivity(intent);	
						scanFlag = GlobalParams.FLAG_ACTIVE;
					} else {
						String msg = languagePrefs.getPreferencesString
								(GlobalParams.ERRORUNABLETOCONTACTSERVER, GlobalParams.ERROR_INVALID_NETWORK);				
						showPopUp(AcPutAway.this, null, msg);
					}
				} else {
					scanFlag = GlobalParams.FLAG_ACTIVE;
				}
			} catch (Exception e) {				
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
}
