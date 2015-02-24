/**
 * Name: $RCSfile: AcCycleAdjustmentOption.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.cyclecount;

import java.net.URLEncoder;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appolis.androidtablet.R;
import com.appolis.common.LanguagePreferences;
import com.appolis.entities.EnBarcodeExistences;
import com.appolis.entities.ObjectBinList;
import com.appolis.entities.ObjectCountCycleCurrent;
import com.appolis.entities.ObjectInstanceRealTimeBin;
import com.appolis.login.LoginActivity;
import com.appolis.network.NetParameter;
import com.appolis.network.access.HttpNetServices;
import com.appolis.scan.CaptureBarcodeCamera;
import com.appolis.scan.SingleEntryApplication;
import com.appolis.utilities.CommontDialog;
import com.appolis.utilities.DataParser;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.Utilities;

/**
 * 
 * @author hoangnh11
 * Display Cycle adjustment option screen
 */
public class AcCycleAdjustmentOption extends Activity implements OnClickListener{

	private LinearLayout linScan;
	private LinearLayout linBack;
	private TextView tvHeader;
	private TextView tvCycleLocation;
	private TextView tvLoc;
	private EditText txtCycleAdjustmentLoc;
	private Button btnCancel;
	private Button btnOk;
	
	private boolean isFirst = true;
	private boolean isLocationScreen;
	private String loc;
	private ObjectInstanceRealTimeBin bin;
	private ObjectCountCycleCurrent binCurrent;
	
	private LanguagePreferences languagePrefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_cycle_adjustment_option);
		loc = getIntent().getStringExtra("path");
		bin = (ObjectInstanceRealTimeBin) getIntent().getSerializableExtra("BinCycleCount");
		binCurrent = (ObjectCountCycleCurrent) getIntent().getSerializableExtra("objectCycleCurrent");
		isLocationScreen = getIntent().getBooleanExtra("isLocationScreen", false);
		languagePrefs = new LanguagePreferences(getApplicationContext());
		//BTLrequireScan = LoginActivity.itemUser.get_isForceCycleCountScan();
		initLayout();
		setText();
	}

	/**
	 * instance layout
	 */
	private void initLayout() {
		tvHeader = (TextView) findViewById(R.id.tv_header);
		tvCycleLocation = (TextView) findViewById(R.id.tvLocation);
		linScan = (LinearLayout) findViewById(R.id.lin_buton_scan);
		tvLoc = (TextView) findViewById(R.id.tvLoc);
		linScan.setOnClickListener(this);
		linBack = (LinearLayout) findViewById(R.id.lin_buton_home);
		linBack.setOnClickListener(this);
		txtCycleAdjustmentLoc = (EditText) findViewById(R.id.txtCycleLocation);
		btnCancel = (Button) findViewById(R.id.btnCycleAdjCancel);
		btnCancel.setOnClickListener(this);
		btnOk = (Button) findViewById(R.id.btnCycleAdjOk);
		btnOk.setOnClickListener(this);
		
		if(LoginActivity.itemUser.get_isForceCycleCountScan()) {
			txtCycleAdjustmentLoc.setEnabled(false);
		} else {
			txtCycleAdjustmentLoc.setEnabled(true);
		}
		//setData
		if(isLocationScreen) {
			tvCycleLocation.setText(bin.get_binNumber());
		} else {
			tvCycleLocation.setText(loc);
		}
		
		txtCycleAdjustmentLoc.setOnEditorActionListener(new EditText.OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        if (actionId == EditorInfo.IME_ACTION_DONE) {
		            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		            imm.hideSoftInputFromWindow(txtCycleAdjustmentLoc.getWindowToken(), 0);
		            String barcode = txtCycleAdjustmentLoc.getText().toString().trim();
		            processScanData(barcode);
		            return true;
		        }
		        return false;
		    }
		});

		txtCycleAdjustmentLoc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
		    @Override
		    public void onFocusChange(View v, boolean hasFocus) {
		        if (!hasFocus) {
		        	if(!isFirst) {
			            String barcode = txtCycleAdjustmentLoc.getText().toString().trim();
			            processScanData(barcode);
		        	} else {
		        		isFirst = false;
		        	}
		        }
		    }
		});
		
		if(LoginActivity.itemUser.get_isForceCycleCountScan()) { 
			txtCycleAdjustmentLoc.setEnabled(false);
		}
	}
	
	/**
	 * Set text for header, buttons and views
	 */
	private void setText() {
		tvHeader.setText(languagePrefs.getPreferencesString(GlobalParams.CYCLE_ADJUSTMENT_KEY, GlobalParams.CYCLE_ADJUSTMENT_VALUE));
		String loc = languagePrefs.getPreferencesString(GlobalParams.PID_LBL_LOCATION, GlobalParams.LOC) + ":";
		tvLoc.setText(loc);
		btnOk.setText(languagePrefs.getPreferencesString(GlobalParams.PD_BTN_OK, GlobalParams.OK));
		btnCancel.setText(languagePrefs.getPreferencesString(GlobalParams.SETTINGS_BTN_CANCEL, GlobalParams.CANCEL));
	}

	/**
	 * Process event for views
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
			case R.id.lin_buton_home:
				Utilities.hideKeyboard(this);
				this.finish();
				break;
				
			case R.id.btnCycleAdjCancel:
				this.finish();
				break;
			
			case R.id.btnCycleAdjOk:
				String barcode = txtCycleAdjustmentLoc.getText().toString().trim();
				processScanData(barcode);
				break;
				
			case R.id.lin_buton_scan:
				Intent intentScan = new Intent(AcCycleAdjustmentOption.this,
						CaptureBarcodeCamera.class);
				startActivityForResult(intentScan,
						GlobalParams.CAPTURE_BARCODE_CAMERA_ACTIVITY);
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
			case GlobalParams.CAPTURE_BARCODE_CAMERA_ACTIVITY:
				if (resultCode == RESULT_OK) {
					String barcode = data.getStringExtra(GlobalParams.RESULTSCAN);
					Log.e("Appolis", "CAPTURE_BARCODE_CAMERA_ACTIVITY #RESULT_OK:"
							+ barcode);
					
					processScanData(barcode);
				}
				break;
			case GlobalParams.AC_CYCLE_OPTION_DETAIL:
				if (resultCode == RESULT_OK) {
					setResult(RESULT_OK);
					finish();
				}
	
			default:
				break;
		}
	}
	
	/**
	 * Process data scanned
	 * @param barcode
	 */
	private void processScanData(String barcode) {
		ProcessScanDataAsyn mLoadDataTask = new ProcessScanDataAsyn(AcCycleAdjustmentOption.this, barcode);
		// mLoadDataTask.setMoveTopOnComplete(isMoveToTop);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mLoadDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			mLoadDataTask.execute();
		}
	}
	
	/**
	 * 
	 * handle data after first scan for Location is scanned
	 * 
	 * */
	private class ProcessScanDataAsyn extends AsyncTask<Void, Void, Integer> {
		Context context;
		ProgressDialog progressDialog;
		String response;
		String barcode;

		public ProcessScanDataAsyn(Context mContext, String barcode) {
			this.context = mContext;
			this.barcode = barcode;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (!isCancelled()) {
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
				progressDialog.setCancelable(true);
				progressDialog.show();
			}
		}

		@Override
		protected Integer doInBackground(Void... arg0) {
			int errorCode = -1;
			if (!Utilities.isConnected(context)) {
				return 1;
			}

			try {
				if (!isCancelled()) {
					NetParameter[] netParameters = new NetParameter[1];
					netParameters[0] = new NetParameter("barcode", URLEncoder.encode(barcode.trim(), GlobalParams.UTF_8));
					response = HttpNetServices.Instance
							.getBarcode(netParameters);
					Log.e("Appolis", "AcCycleAdjustmentOption #getBarcode #response:" + response);
									
					EnBarcodeExistences enBarcodeExistences = DataParser
							.getBarcode(response);
					if (null != enBarcodeExistences) {
						if (enBarcodeExistences.getBinOnlyCount() == 0
								&& enBarcodeExistences.getGtinCount() == 0
								&& enBarcodeExistences
										.getItemIdentificationCount() == 0
								&& enBarcodeExistences.getItemOnlyCount() == 0
								&& enBarcodeExistences.getLotOnlyCount() == 0
								&& enBarcodeExistences.getLPCount() == 0
								&& enBarcodeExistences.getOrderCount() == 0
								&& enBarcodeExistences.getPoCount() == 0
								&& enBarcodeExistences.getUOMBarcodeCount() == 0) {
							// errorscan
							return 3;
						} 
						// if barcode is of LP or BIn
						else if (enBarcodeExistences.getLPCount() != 0 || enBarcodeExistences.getBinOnlyCount() != 0) {
							errorCode = 0;
						}  else {
							return 4;
						}
					} else {
						errorCode = 1; // exception
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
			if (null != progressDialog && (progressDialog.isShowing())) {
				progressDialog.dismiss();
			}

			String[] barcodes = new String[1];
			barcodes[0] = barcode;
			if (!isCancelled()) {
				switch (result) {
				case 0: // if LP or Bin
					new LoadLpOrBinAsyn(AcCycleAdjustmentOption.this).execute(barcodes);
					break;

				case 3:
					//pending to next phrase
					String mss1 = languagePrefs.getPreferencesString(GlobalParams.MESSAGE_PRUMPT_CREATE_BIN_OR_LP_KEY,
							GlobalParams.MESSAGE_PRUMPT_CREATE_BIN_OR_LP_VALUE);
					String content1 = mss1.replace("{0}", barcode);
					String params1[] = new String[1];
					params1[0] = barcode;
					dialogShowMessage(content1, AcCycleAdjustmentOption.this, params1);
					break;

				case 4:
					String mss = languagePrefs.getPreferencesString(GlobalParams.BIN_MESSAGEBOXINVALIDLOCATIONSCAN,
							GlobalParams.INVALID_LOCATION_SCAN_PLEASE_SCAN_A_VALID_LOCATION);
					Utilities.showPopUp(context, null, mss);
					
					break;

				case 1: // no network
					String msg = languagePrefs.getPreferencesString(
							GlobalParams.ERRORUNABLETOCONTACTSERVER,
							GlobalParams.ERROR_INVALID_NETWORK);
					CommontDialog.showErrorDialog(context, msg, null);
					break;

				case 5: // If call was ambiguous, PONumber, OrderNumber,
						// LotOnly, BinOnly
					String msgScanError = languagePrefs.getPreferencesString(
							GlobalParams.ERRORAMBIGUOUSSCAN,
							GlobalParams.ERRORAMBIGUOUSSCAN_DEFAULT);
					CommontDialog.showErrorDialog(context, msgScanError, null);
					break;

				default:
					String msgs = languagePrefs.getPreferencesString("error",
							"error");
					CommontDialog.showErrorDialog(context, msgs, null);
					Log.e("Appolis", "AcCycleAdjustmentOption #onPostExecute: "
							+ result);
					break;
				}
			} else {
				String mss2 = languagePrefs.getPreferencesString(GlobalParams.ERROR_SCAN_LP_KEY, GlobalParams.ERROR_SCAN_LP_VALUE);
				Utilities.showPopUp(context, null, mss2);
			}
		}
	}
	
	/**
	 * Load data LP or Bin from api
	 * @author hoangnh11
	 */
	private class LoadLpOrBinAsyn extends AsyncTask<String, Void, Integer> {

		Context context;
		ProgressDialog progressDialog;
		String data;
		String barcode;
		
		public LoadLpOrBinAsyn(Context mContext) {
			this.context = mContext;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
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
			progressDialog.setCancelable(true);
			progressDialog.show();

		}

		@Override
		protected Integer doInBackground(String... params) {
			Integer result = 0;
			if (!isCancelled()) {
				try {
					barcode = params[0];
					NetParameter[] netParameters = new NetParameter[2];
					netParameters[0] = new NetParameter("barcodeNumber", URLEncoder.encode(params[0], GlobalParams.UTF_8));
					if(isLocationScreen) {
						netParameters[1] = new NetParameter("binNumber", URLEncoder.encode(bin.get_binNumber().trim(), GlobalParams.UTF_8));
					} else {
						netParameters[1] = new NetParameter("binNumber", URLEncoder.encode(binCurrent.get_itemNumber().trim(), GlobalParams.UTF_8));
					}
					data = HttpNetServices.Instance.getDataBinOrLpOption(netParameters);
				} catch (Exception e) {
					result = 1;
				}
			}
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);

			if (null != progressDialog && (progressDialog.isShowing())) {
				progressDialog.dismiss();
			}
			
			String errorKey = languagePrefs.getPreferencesString(GlobalParams.BIN_MESSAGEBOXINVALIDLOCATIONSCAN, GlobalParams.INVALID_LOCATION_SCAN_PLEASE_SCAN_A_VALID_LOCATION);
			
			if (!isCancelled()) {
				if (result == 0) {
					if(data.equalsIgnoreCase("true")) {
						startAcAdjustmentOptionDetail(barcode);
					} else {
						CommontDialog.showErrorDialog(context, errorKey, null);
					}
				} else {
					CommontDialog.showErrorDialog(context, errorKey, null);
				}
			} else {
				CommontDialog.showErrorDialog(context, errorKey, null);
			}
		}
		
	}
	
	/**
	 * Message is used to confirm to create new LP or Bin
	 * 
	 * @param message
	 * @param activity
	 * @param barcode
	 */
	
	public void dialogShowMessage(String message, Activity activity, final String []barcode) {
		final Dialog dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.dialogwarning);
		dialog.setCanceledOnTouchOutside(false);
		TextView tvScantitle2 = (TextView) dialog
				.findViewById(R.id.tvScantitle2);
		tvScantitle2.setText(message);
		Button btDialogOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
		btDialogOk.setVisibility(View.INVISIBLE);
		Button btDialogYes = (Button) dialog.findViewById(R.id.dialogButtonYes);
		btDialogYes.setVisibility(View.VISIBLE);
		Button btDialogNo = (Button) dialog.findViewById(R.id.dialogButtonNo);
		btDialogNo.setVisibility(View.VISIBLE);
		btDialogYes.setText(languagePrefs.getPreferencesString(GlobalParams.YES_TEXT, GlobalParams.YES_TEXT));
		btDialogNo.setText(languagePrefs.getPreferencesString(GlobalParams.NO_TEXT, GlobalParams.NO_TEXT));

		btDialogNo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		btDialogYes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				new LoadCreatingLpOrBinAsyn(AcCycleAdjustmentOption.this).execute(barcode);
			}
		});

		dialog.show();
	}
		
	/**
	 * Create new LP or Bin
	 * @author hoangnh11
	 */
	private class LoadCreatingLpOrBinAsyn extends AsyncTask<String, Void, Integer> {

		Context context;
		ProgressDialog progressDialog;
		String data;
		String barcode;
		
		public LoadCreatingLpOrBinAsyn(Context mContext) {
			this.context = mContext;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
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
			progressDialog.setCancelable(true);
			progressDialog.show();

		}

		@Override
		protected Integer doInBackground(String... params) {
			Integer result = 0;
			if (!isCancelled()) {
				try {
					NetParameter[] netParameters = new NetParameter[2];
					if(isLocationScreen) {
						netParameters[0] = new NetParameter("parentBinNumber", URLEncoder.encode(bin.get_binNumber().trim(), GlobalParams.UTF_8));
					} else {
						netParameters[0] = new NetParameter("parentBinNumber", URLEncoder.encode(binCurrent.get_itemNumber().trim(), GlobalParams.UTF_8));
					}
					netParameters[1] = new NetParameter("licensePlateNumber", params[0].trim());
					barcode = params[0].trim();
					data = HttpNetServices.Instance.creareLpOption(netParameters);
					data = getDataBarcode(data);
				} catch (Exception e) {
					result = 1;
				}
			}
			return result;
		}
		
		private String getDataBarcode(String s) {
			if(s.indexOf("\"") > -1) {
				s = s.substring(1, s.length() - 1);
			}
			
			return s;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);

			if (null != progressDialog && (progressDialog.isShowing())) {
				progressDialog.dismiss();
			}
			
			String errorKey = languagePrefs.getPreferencesString(GlobalParams.CREATELICENSEPLATE_CREATIONFAILURE_KEY,
						GlobalParams.CREATELICENSEPLATE_CREATIONFAILURE_VALUE);
			
			if (!isCancelled()) {
				if (result == 0) {
					if(data.equalsIgnoreCase(barcode)) {
						startAcAdjustmentOptionDetail(barcode);
					} else {
						Utilities.dialogShow(data, AcCycleAdjustmentOption.this);
					}
				} else {
					Utilities.dialogShow(errorKey, AcCycleAdjustmentOption.this);
				}
			} else {
				Utilities.dialogShow(errorKey, AcCycleAdjustmentOption.this);
			}
		}
		
	}
	
	/**
	 * 
	 * @param location
	 */
	private void startAcAdjustmentOptionDetail(String location) {
		txtCycleAdjustmentLoc.setText(location);
		btnOk.setEnabled(true);
		Intent intent = new Intent(AcCycleAdjustmentOption.this, AcCycleAdjustmentOptionDetail.class);
		if(isLocationScreen) {
			ObjectBinList binList = (ObjectBinList) getIntent().getSerializableExtra("binList");
			intent.putExtra("BinCycleCount", bin);
			intent.putExtra("path", loc);
			intent.putExtra("isLocationScreen", isLocationScreen);
			intent.putExtra("binCurrent", binCurrent);
			intent.putExtra("binList", binList);
			intent.putExtra("location", location);
			startActivityForResult(intent, GlobalParams.AC_CYCLE_OPTION_DETAIL);
		} else {
			ObjectBinList binList = (ObjectBinList) getIntent().getSerializableExtra("binList");
			intent.putExtra("BinCycleCount", bin);
			intent.putExtra("path", loc);
			intent.putExtra("isLocationScreen", isLocationScreen);
			intent.putExtra("binCurrent", binCurrent);
			intent.putExtra("binList", binList);
			intent.putExtra("location", location);
			startActivityForResult(intent, GlobalParams.AC_CYCLE_OPTION_DETAIL);
		}
	}
	
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

	@Override
	protected void onResume() {
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



	/**
	 * handler for receiving the notifications coming from 
	 * SingleEntryApplication.
	 * Update the UI accordingly when we receive a notification
	 */
	private final BroadcastReceiver _newItemsReceiver = new BroadcastReceiver() {   
	    
		@Override  
	    public void onReceive(Context context, Intent intent) {
		
	        // a Scanner has connected
	        if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_SCANNER_ARRIVAL)) {
	        	linScan.setVisibility(View.GONE);
	        }
	        
	        // a Scanner has disconnected
	        else if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_SCANNER_REMOVAL)) {
	        	linScan.setVisibility(View.VISIBLE);
	        }
	        
	        // decoded Data received from a scanner
	        else if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_DECODED_DATA)) {
				char[] data = intent.getCharArrayExtra(SingleEntryApplication.EXTRA_DECODEDDATA);
				String barcode = new String(data);
				processScanData(barcode);
	        }
	    }
	};
}
