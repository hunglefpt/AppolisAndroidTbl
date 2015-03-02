/**
 * Name: $RCSfile: AcCycleCount.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.cyclecount;

import java.net.URLEncoder;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.appolis.adapter.CycleCountAdapter;
import com.appolis.androidtablet.R;
import com.appolis.common.CommonData;
import com.appolis.common.LanguagePreferences;
import com.appolis.entities.EnBarcodeExistences;
import com.appolis.entities.ObjectCycleCount;
import com.appolis.entities.ObjectInstanceRealTimeBin;
import com.appolis.login.LoginActivity;
import com.appolis.network.NetParameter;
import com.appolis.network.access.HttpNetServices;
import com.appolis.scan.CaptureBarcodeCamera;
import com.appolis.scan.SingleEntryApplication;
import com.appolis.utilities.DataParser;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.Utilities;

/**
 * @author CongLT
 * Display Cycle count screen
 */
public class AcCycleCount extends Activity implements OnClickListener,
		OnItemClickListener {

	private LinearLayout linBack;
	private LinearLayout linScan;
	private TextView tvHeader;
	private TextView tvCycleSelect;
	private EditText txtCycleSearch;
	private ImageView imgClearTextSearch;
	private ImageView imgSearch;
	private ListView lvCycleCountList;

	private CycleCountAdapter cycleCountAdapter;
	// data variable
	private ObjectCycleCount objectCycleCount;
	private List<ObjectInstanceRealTimeBin> binList;
	private LanguagePreferences languagePrefs;
	public static boolean isPhysicalInventoryCycleCount;
	public static int cycleCountInstanceID;
	private boolean isScanning = true;
	private boolean isActivityRuning = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isActivityRuning = true;
		setContentView(R.layout.ac_cycle_count);
		languagePrefs = new LanguagePreferences(getApplicationContext());
		initLayout();
		setText();
		new LoadCycleCountListAsyn(this).execute();
	}

	/**
	 * instance layout
	 */
	private void initLayout() {
		linBack = (LinearLayout) findViewById(R.id.lin_buton_home);
		linBack.setOnClickListener(this);
		linScan = (LinearLayout) findViewById(R.id.lin_buton_scan);
		linScan.setOnClickListener(this);
		tvHeader = (TextView) findViewById(R.id.tv_header);
		tvCycleSelect = (TextView) findViewById(R.id.tvCycleSelect);
		txtCycleSearch = (EditText) findViewById(R.id.txt_cycle_search);
		imgClearTextSearch = (ImageView) findViewById(R.id.img_clear_cycle_search);
		imgSearch = (ImageView) findViewById(R.id.imgSearch);
		imgClearTextSearch.setVisibility(View.GONE);
		imgClearTextSearch.setOnClickListener(this);
		lvCycleCountList = (ListView) findViewById(R.id.lvCycleCountList);
		if((!LoginActivity.itemUser.get_isForceCycleCountScan()) && LoginActivity.itemUser != null) {
			lvCycleCountList.setOnItemClickListener(this);
		}
		
		// Capture Text in EditText
		txtCycleSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String text = txtCycleSearch.getText().toString();
				
				if(text.length() > 0) {
					imgClearTextSearch.setVisibility(View.VISIBLE);
					imgSearch.setVisibility(View.INVISIBLE);
				}
				
				if(null != cycleCountAdapter) {
					cycleCountAdapter.filter(text);
				}
			}
		});
		
	}
	
	/**
	 * Set text for header, buttons and views
	 */
	private void setText() {
		tvHeader.setText(languagePrefs.getPreferencesString(GlobalParams.CYCLE_COUNT_KEY, GlobalParams.CYCLE_COUNT_VALUE));
		tvCycleSelect.setText(languagePrefs.getPreferencesString(GlobalParams.SELECT, GlobalParams.SELECT));
		txtCycleSearch.setHint(languagePrefs.getPreferencesString(GlobalParams.SCAN_SELECTLOC_SEARCH_KEY, GlobalParams.SCAN_SELECTLOC_SEARCH_VALUE));
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		isActivityRuning = false;
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
		isActivityRuning = true;
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
				String barcode = new String(data);
				if(isScanning) {
					isScanning =  false;
					processScanData(barcode);
				}
	        }
	    }
	};

	/**
	 * Process data Cycle count
	 * @author hoangnh11
	 */
	private class LoadCycleCountListAsyn extends AsyncTask<Void, Void, Integer> {
		Context context;
		ProgressDialog progressDialog;
		String data;

		public LoadCycleCountListAsyn(Context mContext) {
			this.context = mContext;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (!isCancelled() && isActivityRuning) {
				progressDialog = new ProgressDialog(context);
				progressDialog.setMessage(languagePrefs.getPreferencesString(GlobalParams.LOADING_DATA + "...",
						"Loading..."));
				progressDialog
						.setOnCancelListener(new DialogInterface.OnCancelListener() {
	
							@Override
							public void onCancel(DialogInterface dialog) {
								cancel(true);
							}
						});
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.setCancelable(false);
				progressDialog.show();
			}

		}

		@Override
		protected Integer doInBackground(Void... arg0) {
			Integer result = 0;
			if (!isCancelled()) {
				try {
					data = HttpNetServices.Instance.getCycleCount();
					objectCycleCount = DataParser.getCycleCount(data);

					if (null != objectCycleCount) {
						binList = objectCycleCount.get_countInstanceRealTimeBin();
						isPhysicalInventoryCycleCount = objectCycleCount.is_physicalInventoryCycleCount();
						cycleCountInstanceID = objectCycleCount.get_cycleCountInstanceID();
						if (null == binList || binList.size() == 0) {
							result = 1;
						}
					} else {
						result = 1;
					}
				} catch (Exception e) {
					result = 2;
				}
			}
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);

			if (null != progressDialog && (progressDialog.isShowing()) && isActivityRuning) {
				progressDialog.dismiss();
			}

			String errorMss = languagePrefs.getPreferencesString(GlobalParams.NO_MORE_COUNTS_KEY, GlobalParams.NO_MORE_COUNTS_VALUE);
			
			if (!isCancelled()) {
				if (result == 0) {
					//new Adapter without Current Location
					cycleCountAdapter = new CycleCountAdapter(getApplicationContext(), binList);
					lvCycleCountList.setAdapter(cycleCountAdapter);
					cycleCountAdapter.notifyDataSetChanged();
				} else {
					Utilities.dialogShow(errorMss, AcCycleCount.this);
				}
			} else {
				Utilities.dialogShow(errorMss, AcCycleCount.this);
			}
		}
	}

	/**
	 * Process click event for list view
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		ObjectInstanceRealTimeBin bin = binList.get(position);
		Intent intentAcCycleLocation = new Intent(AcCycleCount.this, AcCycleCountLocation.class);
		intentAcCycleLocation.putExtra("ObjectCycleCount", objectCycleCount);
		intentAcCycleLocation.putExtra("BinCycleCount", bin);
		startActivityForResult(intentAcCycleLocation, GlobalParams.AC_CYCLE_COUNT_LOCATION_ACTIVITY);
	}
	
	/**
	 * Process activity
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		isScanning = true;
		isActivityRuning = true;
		switch (requestCode) {
			case GlobalParams.AC_CYCLE_COUNT_LOCATION_ACTIVITY:
				if(RESULT_OK == resultCode || resultCode == RESULT_CANCELED){
					new LoadCycleCountListAsyn(AcCycleCount.this).execute();
				}
				break;
				
			case GlobalParams.CAPTURE_BARCODE_CAMERA_ACTIVITY:
				if(resultCode == RESULT_OK){
					String message = data.getStringExtra(GlobalParams.RESULTSCAN);
					Log.e("Appolis", "CAPTURE_BARCODE_CAMERA_ACTIVITY #RESULT_OK:" + message);
					processScanData(message);
				}
				break;	
			
			default:
				break;
		}
	}

	/**
	 * Process event for views
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
			case R.id.lin_buton_home:
				Utilities.hideKeyboard(this);
				onClickHomeButton(AcCycleCount.this);
				break;
				
			case R.id.img_clear_cycle_search:
				removeTextSearchCycle();
				break;
			case R.id.lin_buton_scan:
				Intent intentScan = new Intent(AcCycleCount.this, CaptureBarcodeCamera.class);
				startActivityForResult(intentScan, GlobalParams.CAPTURE_BARCODE_CAMERA_ACTIVITY);
				break;
			default:
				break;
		}
	}

	/**
	 * Show dialog message
	 * 
	*/
    public void dialogShowMessage(String message, final Activity activity)
    {
    	final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogwarning);
        dialog.setCanceledOnTouchOutside(false);
        TextView tvScantitle2 = (TextView) dialog.findViewById(R.id.tvScantitle2);
        tvScantitle2.setText(message);
        Button btDialogOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
        btDialogOk.setVisibility(View.INVISIBLE);
		Button btDialogYes = (Button) dialog.findViewById(R.id.dialogButtonYes);
		btDialogYes.setVisibility(View.VISIBLE);
		btDialogYes.setText(languagePrefs.getPreferencesString(GlobalParams.YES_TEXT, GlobalParams.YES_TEXT));
		Button btDialogNo = (Button) dialog.findViewById(R.id.dialogButtonNo);
		btDialogNo.setVisibility(View.VISIBLE);
		btDialogNo.setText((languagePrefs.getPreferencesString(GlobalParams.NO_TEXT, GlobalParams.NO_TEXT)));
		
		btDialogNo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				activity.finish();
			}
		});
		
		btDialogYes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				new BinCompletedAsyn(AcCycleCount.this).execute();
			}
		});
		
		dialog.show();    		
    }
    
	//handle when user click home button
	private void onClickHomeButton(Activity activity) {
		if(checkAllCompletedCount()) {
			dialogShowMessage(languagePrefs.getPreferencesString(GlobalParams.CYCLE_QUES_COMPLETE_KEY, GlobalParams.CYCLE_QUES_COMPLETE_VALUE),
									AcCycleCount.this);
		} else {
			activity.finish();
		}
	}
	
	//check status all of list bin
	private boolean checkAllCompletedCount() {
		if(binList != null && binList.size() > 0) {
			for(ObjectInstanceRealTimeBin bin : binList) {
				if(!CommonData.COMPLETED.equalsIgnoreCase(bin.get_binStatus().trim())) {
					return false;
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Remove text
	 */
	private void removeTextSearchCycle() {
		txtCycleSearch.setText("");
		imgSearch.setVisibility(View.VISIBLE);
		imgClearTextSearch.setVisibility(View.INVISIBLE);
	}
	
	//completed status all of bins
	private class BinCompletedAsyn extends AsyncTask<Void, Void, Integer> {
		Context context;
		ProgressDialog progressDialog;

		public BinCompletedAsyn(Context mContext) {
			this.context = mContext;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (!isCancelled() && isActivityRuning) {
				progressDialog = new ProgressDialog(context);
				progressDialog.setMessage(languagePrefs.getPreferencesString(GlobalParams.LOADING_DATA + "...",
						"Loading..."));
				progressDialog
						.setOnCancelListener(new DialogInterface.OnCancelListener() {
	
							@Override
							public void onCancel(DialogInterface dialog) {
								cancel(true);
							}
						});
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.setCancelable(false);
				progressDialog.show();
			}

		}

		@Override
		protected Integer doInBackground(Void... arg0) {
			Integer result = 0;
			if (!isCancelled()) {
				try {
					NetParameter[] netParameters = new NetParameter[1];
					netParameters[0] = new NetParameter("cycleCountInstanceID", String.valueOf(objectCycleCount.get_cycleCountInstanceID()));
					HttpNetServices.Instance.setCompletedBins(netParameters);
				} catch (Exception e) {
					result = 1;
				}
			}
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);

			if (null != progressDialog && (progressDialog.isShowing()) && isActivityRuning) {
				progressDialog.dismiss();
			}
			
			String errorMss = languagePrefs.getPreferencesString(GlobalParams.ERROR_COMPLETING_COUNT_KEY, GlobalParams.ERROR_COMPLETING_COUNT_VALUE);
			
			if (!isCancelled()) {
				if (result == 0) {
					finish();
				} else {
					Utilities.dialogShow(errorMss, AcCycleCount.this);
				}
			} else {
				Utilities.dialogShow(errorMss, AcCycleCount.this);
			}
		}
	}
	
	/**
	 * process scan data
	 */
	private void processScanData(String barcode) {
		ProcessScanDataAsyn mLoadDataTask = new ProcessScanDataAsyn(AcCycleCount.this, barcode);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mLoadDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			mLoadDataTask.execute();
		}
	}
	
	/**
	 * Process data scanned
	 * @author hoangnh11
	 */
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
			if(!isCancelled() && isActivityRuning){
				progressDialog = new ProgressDialog(context);
				progressDialog.setMessage(languagePrefs.getPreferencesString(GlobalParams.LOADING_DATA + "...",
						"Loading..."));
				progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
	
							@Override
							public void onCancel(DialogInterface dialog) {
								cancel(true);
							}
						});
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.setCancelable(false);
				progressDialog.show();
			}
		}
		
		@Override
		protected Integer doInBackground(Void... arg0) {
			int errorCode = -1;
			if(!Utilities.isConnected(context)){
				return 1; 
			}
			
			try{
				if(!isCancelled()){
					NetParameter[] netParameters = new NetParameter[1];
					netParameters[0] = new NetParameter("barcode", URLEncoder.encode(barcode.trim(), GlobalParams.UTF_8));
					response = HttpNetServices.Instance.getBarcode(netParameters);
					Log.e("Appolis", "LoadReceiveDetailAsyn #getBarcode #response:" + response);
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
							return 3;
						} else if (enBarcodeExistences.getBinOnlyCount() != 0 ) {
							//Item Number, UOM or ItemIdentification
							errorCode = 0;
						} else {
							//Unsupported barcode 
							return 4;
						}
					} else {
						errorCode = 2; // exception
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				errorCode = 2;
			}
			
			return errorCode;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			
			if(null != progressDialog && (progressDialog.isShowing()) && isActivityRuning){
				progressDialog.dismiss();
			}
			
			if(!isCancelled()){
				switch (result) {
				case 0: //success
					ObjectInstanceRealTimeBin bin = getBinFromBarcode(barcode);
					if(bin != null) {
						Intent intentAcCycleLocation = new Intent(AcCycleCount.this, AcCycleCountLocation.class);
						intentAcCycleLocation.putExtra("ObjectCycleCount", objectCycleCount);
						intentAcCycleLocation.putExtra("BinCycleCount", bin);
						startActivityForResult(intentAcCycleLocation, GlobalParams.AC_CYCLE_COUNT_LOCATION_ACTIVITY);
					} else {
						String mss = languagePrefs.getPreferencesString(GlobalParams.ERRORBINNOTFOUND_KEY, GlobalParams.ERRORBINNOTFOUND_VALUE);
						Utilities.showPopUp(context, null, mss);
						isScanning = true;
					}
					break;
					
				case 3:// error scan
					String mss = languagePrefs.getPreferencesString(GlobalParams.ERRORBINNOTFOUND_KEY, GlobalParams.ERRORBINNOTFOUND_VALUE);
					Utilities.showPopUp(context, null, mss);
					isScanning = true;
					break;
				
				case 4:// Unsupported barcode 
					String mss1 = languagePrefs.getPreferencesString(GlobalParams.BIN_NOT_EXIST_KEY, GlobalParams.BIN_NOT_EXIST_VALUE);
					Utilities.showPopUp(context, null, mss1);
					isScanning = true;
					break;
					
				case 1: //no network
					String msg = languagePrefs.getPreferencesString(GlobalParams.ERRORUNABLETOCONTACTSERVER, GlobalParams.ERROR_INVALID_NETWORK);
					Utilities.showPopUp(context, null, msg);
					isScanning = true;
					break;
					
				default:
					String msgs = languagePrefs.getPreferencesString("error", "error");
					Utilities.showPopUp(context, null, msgs);
					Log.e("Appolis", "LoadReceiveListAsyn #onPostExecute: " + result);
					isScanning = true;
					break;
				}
			}
		}
	}
	
	/**
	 * Instance object
	 * @param barcode
	 * @return
	 */
	private ObjectInstanceRealTimeBin getBinFromBarcode(String barcode) {
		if(binList != null && binList.size() > 0) {
			for(ObjectInstanceRealTimeBin bin : binList) {
				if(bin.get_binNumber().equalsIgnoreCase(barcode)) {
					return bin;
				}
			}
		}
		return null;
	}

}
