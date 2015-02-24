/**
 * Name: $RCSfile: AcCycleAdjustmentOptionDetail.java,v $
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
import com.appolis.common.CommonData;
import com.appolis.common.LanguagePreferences;
import com.appolis.entities.EnBarcodeExistences;
import com.appolis.entities.ObjectBinList;
import com.appolis.entities.ObjectCountCycleCurrent;
import com.appolis.entities.ObjectCycleItemDetail;
import com.appolis.entities.ObjectInstanceRealTimeBin;
import com.appolis.login.LoginActivity;
import com.appolis.network.NetParameter;
import com.appolis.network.access.HttpNetServices;
import com.appolis.scan.CaptureBarcodeCamera;
import com.appolis.scan.SingleEntryApplication;
import com.appolis.utilities.CommontDialog;
import com.appolis.utilities.DataParser;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.StringUtils;
import com.appolis.utilities.Utilities;

/**
 * @author hoangnh11
 * Display Cycle adjustment option detail screen
 */
public class AcCycleAdjustmentOptionDetail extends Activity implements OnClickListener {
	private LinearLayout linScan;
	private LinearLayout linBack;
	private LinearLayout linItem;
	private TextView tvHeader;
	private TextView tvCycleLocation;
	private TextView tvLoc;
	private TextView tvItem;
	private EditText txtCycleAdjustmentLoc;
	private EditText txtCycleAdjustmentItem;
	private Button btnCancel;
	private Button btnOk;
	
	private boolean isLocationScreen;
	private boolean isScanLot;
	private boolean isFirst = true;
	
	private String newLocation;
	private String path;
	private ObjectInstanceRealTimeBin bin;
	private List<ObjectCountCycleCurrent> binList;
	
	private int typeScan;
	private boolean isConnectSocket;
	private LanguagePreferences languagePrefs;
	private ObjectCycleItemDetail itemDetail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_cycle_adjustment_option);
		newLocation = getIntent().getStringExtra("location");
		bin = (ObjectInstanceRealTimeBin) getIntent().getSerializableExtra("BinCycleCount");
		ObjectBinList bins = (ObjectBinList) getIntent().getSerializableExtra("binList");
		binList = bins.getBinList();
		languagePrefs = new LanguagePreferences(getApplicationContext());
		isLocationScreen = getIntent().getBooleanExtra("isLocationScreen", false);
		if(!isLocationScreen) {
			path = getIntent().getStringExtra("path");
		}
		initLayout();
		setText();
	}

	/**
	 * instance layout
	 */
	private void initLayout() {
		tvHeader = (TextView) findViewById(R.id.tv_header);

		String dafaultHeader = getResources().getString(
				R.string.cycle_count_default_adjustment);
		String keyHeader = getResources().getString(
				R.string.cycle_count_key_adjustment);
		
		tvHeader.setText(languagePrefs.getPreferencesString(keyHeader,
				dafaultHeader));
		tvCycleLocation = (TextView) findViewById(R.id.tvLocation);
		tvLoc = (TextView) findViewById(R.id.tvLoc);
		tvItem = (TextView) findViewById(R.id.tvItem);
		linScan = (LinearLayout) findViewById(R.id.lin_buton_scan);
		linItem = (LinearLayout) findViewById(R.id.linCycleItem);
		linItem.setVisibility(View.VISIBLE);
		linScan.setOnClickListener(this);
		linBack = (LinearLayout) findViewById(R.id.lin_buton_home);
		linBack.setOnClickListener(this);
		txtCycleAdjustmentLoc = (EditText) findViewById(R.id.txtCycleLocation);
		txtCycleAdjustmentItem = (EditText) findViewById(R.id.txtCycleItem);
		txtCycleAdjustmentItem.setVisibility(View.VISIBLE);
		btnCancel = (Button) findViewById(R.id.btnCycleAdjCancel);
		btnCancel.setOnClickListener(this);
		btnOk = (Button) findViewById(R.id.btnCycleAdjOk);
		btnOk.setOnClickListener(this);
		txtCycleAdjustmentLoc.setText(newLocation);
		txtCycleAdjustmentLoc.setEnabled(false);
		//setData
		if(isLocationScreen) {
			tvCycleLocation.setText(bin.get_binNumber());
		} else {
			tvCycleLocation.setText(path);
		}
		
		txtCycleAdjustmentItem.setOnEditorActionListener(new EditText.OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        if (actionId == EditorInfo.IME_ACTION_DONE) {
		            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		            imm.hideSoftInputFromWindow(txtCycleAdjustmentItem.getWindowToken(), 0);
		            String barcode = txtCycleAdjustmentItem.getText().toString().trim();
		            if(StringUtils.isNotBlank(barcode)) {
		            	processScanData(barcode);
		            	btnOk.setEnabled(true);
		            }
		            return true;
		        }
		        return false;
		    }
		});

		txtCycleAdjustmentItem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
		    @Override
		    public void onFocusChange(View v, boolean hasFocus) {
		        if (!hasFocus) {
		        	if(!isFirst) {
			            String barcode = txtCycleAdjustmentItem.getText().toString().trim();
			            if(StringUtils.isNotBlank(barcode)) {
			            	processScanData(barcode);
			            	btnOk.setEnabled(true);
			            }
			            
		        	} else {
		        		isFirst = false;
		        	}
		        }
		    }
		});
		
		if(LoginActivity.itemUser.get_isForceCycleCountScan()) { 
			txtCycleAdjustmentItem.setEnabled(false);
		}
	}
	
	/**
	 * Set text for header, buttons and views
	 */
	private void setText() {
		tvHeader.setText(languagePrefs.getPreferencesString(GlobalParams.CYCLE_ADJUSTMENT_KEY, GlobalParams.CYCLE_ADJUSTMENT_VALUE));
		String loc = languagePrefs.getPreferencesString(GlobalParams.PID_LBL_LOCATION, GlobalParams.LOC) + ":";
		tvLoc.setText(loc);
		String item = languagePrefs.getPreferencesString(GlobalParams.PICK_ITEM, GlobalParams.ITEM) + ":";
		tvItem.setText(item);
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
				if(!isScanLot) {
					String barcode = txtCycleAdjustmentItem.getText().toString().trim();
					processScanData(barcode);
				}
				break;
				
			case R.id.lin_buton_scan:
				Intent intentScan = new Intent(AcCycleAdjustmentOptionDetail.this,
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
					
					if(!isScanLot) {
						processScanData(barcode);
					} else {
						processScanDataLot(barcode);
					}
				}
				break;
				
			case GlobalParams.AC_CYCLE_COUNT_ADJUSMENT_ACTIVITY:
				if (resultCode == RESULT_OK) {
					setResult(RESULT_OK);
					finish();
				}
				break;	
				
			default:
				break;
		}
	}
	
	/**
	 * Process data scanned
	 * @param barcode
	 */
	private void processScanData(String barcode) {
		ProcessScanDataAsyn mLoadDataTask = new ProcessScanDataAsyn(AcCycleAdjustmentOptionDetail.this, barcode);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mLoadDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			mLoadDataTask.execute();
		}
	}
	
	/**
	 * Process Lot data scanned
	 * @param barcode
	 */
	private void processScanDataLot(String barcode) {
		ProcessScanDataLotAsyn mLoadDataTask = new ProcessScanDataLotAsyn(AcCycleAdjustmentOptionDetail.this, barcode);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mLoadDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			mLoadDataTask.execute();
		}
	}
	
	/**
	 * handle data after scan Lot
	 * */
	private class ProcessScanDataLotAsyn extends AsyncTask<Void, Void, Integer> {
		Context context;
		ProgressDialog progressDialog;
		String response;
		String barcode;

		public ProcessScanDataLotAsyn(Context mContext, String barcode) {
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
					Log.e("Appolis",
							"ProcessScanDataLotAsyn #getBarcode #response:"
									+ response);
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
						} else if (enBarcodeExistences.getLotOnlyCount() != 0) {
							errorCode = 0;
						}  else {
							// Unsupported barcode
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
			if (null != progressDialog && (progressDialog.isShowing())) {
				progressDialog.dismiss();
			}

			String[] barcodes = new String[1];
			barcodes[0] = barcode;
			if (!isCancelled()) {
				switch (result) {
				case 0:
					itemDetail.set_LotNumber(barcode);
					ObjectCountCycleCurrent current = getCountCycleCurrent();
					//check item exist in the first list
					if(current != null) {
						startActivityCycleAdjustment(current);
					} else {
						Utilities.showPopUp(context, null, 
								languagePrefs.getPreferencesString(GlobalParams.ERRORINVALIDLOTNUMFORITEM, 
										GlobalParams.INVALID_LOT));
					}
					
					break;

				case 3:// error scan
					Utilities.showPopUp(context, null, 
							languagePrefs.getPreferencesString(GlobalParams.PRINTRECLPS_MSGENTERVALIDLOTNUM_KEY, 
									GlobalParams.PRINTRECLPS_MSGENTERVALIDLOTNUM_VALUE));
					break;

				case 4:// Unsupported barcode
					Utilities.showPopUp(context, null, 
							languagePrefs.getPreferencesString(GlobalParams.PRINTRECLPS_MSGENTERVALIDLOTNUM_KEY, 
									GlobalParams.PRINTRECLPS_MSGENTERVALIDLOTNUM_VALUE));
					break;

				case 1: // no network
					String msg = languagePrefs.getPreferencesString(
							GlobalParams.ERRORUNABLETOCONTACTSERVER,
							GlobalParams.ERROR_INVALID_NETWORK);
					CommontDialog.showErrorDialog(context, msg, null);
					break;

				default:
					String msgs = languagePrefs.getPreferencesString("error",
							"error");
					CommontDialog.showErrorDialog(context, msgs, null);
					Log.e("Appolis", "LoadReceiveListAsyn #onPostExecute: "
							+ result);
					break;
				}
			} else {
				String msgs = languagePrefs.getPreferencesString("error",
						"error");
				CommontDialog.showErrorDialog(context, msgs, null);
				Log.e("Appolis", "ProcessScanDataLotAsyn #onPostExecute: "
						+ result);
			}
		}
	}
	
	/**
	 * 
	 * handle data after first scan
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
					response = HttpNetServices.Instance.getCycleQuanity(netParameters);
					Log.e("Appolis", "AcCycleAdjustmentOptionDeatail #getBarcode #response:" + response);
					itemDetail = DataParser.getCycleItemDetail(response);
					errorCode = 0;
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

			if (!isCancelled()) {
				if(result == 0) {
					if(StringUtils.isNotBlank(itemDetail.get_itemNumber())) {
						if(itemDetail.is_LotTrackingInd()) {
							String lotNumber = itemDetail.get_LotNumber();
							
							if(StringUtils.isNotBlank(lotNumber)) {
								ObjectCountCycleCurrent current = getCountCycleCurrent();
								//check item exist in the first list
								if(current != null) {
									startActivityCycleAdjustment(current);
								} else {
									Utilities.showPopUp(context, null, languagePrefs.getPreferencesString(GlobalParams.REPLENISH_ITEMMATCHERROR_KEY, 
											GlobalParams.REPLENISH_ITEMMATCHERROR_VALUE));
								}
								
							} else {
								//Scan lot number
								//message base on Message key on multi languages file
								isScanLot = true;
								txtCycleAdjustmentItem.setText(itemDetail.get_itemNumber());
								String mss = languagePrefs.getPreferencesString(GlobalParams.USER_SCAN_LOTNUMBER_KEY,
													GlobalParams.USER_SCAN_LOTNUMBER_VALUE);
								dialogShowMessage(mss, AcCycleAdjustmentOptionDetail.this);
							}
						} else {
							ObjectCountCycleCurrent current = getCountCycleCurrent();
							//check item exist in the first list
							if(current != null) {
								startActivityCycleAdjustment(current);
							} else {
								Utilities.showPopUp(context, null, languagePrefs.getPreferencesString(GlobalParams.REPLENISH_ITEMMATCHERROR_KEY, 
										GlobalParams.REPLENISH_ITEMMATCHERROR_VALUE));
							}
						}
					} else {
						Utilities.showPopUp(context, null, languagePrefs.getPreferencesString(GlobalParams.MES_INVALID_ITEM, 
												GlobalParams.MESSAGE_SCAN_ITEM));
					}
					
				} else {
					String msg = languagePrefs.getPreferencesString(
							GlobalParams.ERRORUNABLETOCONTACTSERVER,
							GlobalParams.ERROR_INVALID_NETWORK);
					CommontDialog.showErrorDialog(context, msg, null);
				}
			} else {
				Utilities.showPopUp(context, null, languagePrefs.getPreferencesString(GlobalParams.MES_INVALID_ITEM, 
						GlobalParams.MESSAGE_SCAN_ITEM));
			}
		}
	}
	
//	private ObjectCountCycleCurrent convertFromDetailToCycleCurrent() {
//		ObjectCountCycleCurrent current = new ObjectCountCycleCurrent();
//		current.set_itemNumber(itemDetail.get_itemNumber());
//		current.set_lotNumber(itemDetail.get_LotNumber());
//	}
	
	/**
	 * Show message on dialog
	 * @param message
	 * @param activity
	 */
	public void dialogShowMessage(String message, Activity activity) {
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
				
				if(isConnectSocket) {
					typeScan = CommonData.SCAN_LOT;
				} else {
					Intent intentScan = new Intent(AcCycleAdjustmentOptionDetail.this,
							CaptureBarcodeCamera.class);
					startActivityForResult(intentScan,
							GlobalParams.CAPTURE_BARCODE_CAMERA_ACTIVITY);
				}
			}
		});

		dialog.show();
	}
	
	/**
	 * Get data Count cycle current
	 * @return
	 */
	private ObjectCountCycleCurrent getCountCycleCurrent() {
		for(ObjectCountCycleCurrent current : binList) {
			if(current.get_itemNumber().equalsIgnoreCase(itemDetail.get_itemNumber())) {
				
				if(StringUtils.isNotBlank(itemDetail.get_LotNumber())) {
					if(current.get_lotNumber().equalsIgnoreCase(itemDetail.get_LotNumber())) {
						return current;
					}
				} else {
					return current;
				}	 
			}	
		}
		
		return null;
	}
	
	/**
	 * Start activity
	 * @param currentSelected
	 */
	private void startActivityCycleAdjustment(ObjectCountCycleCurrent currentSelected) {
		if(isLocationScreen) {
			path = bin.get_binNumber();
		}
		Intent intentCycleAdjustment = new Intent(AcCycleAdjustmentOptionDetail.this,
				AcCycleCountAdjusment.class);
		intentCycleAdjustment.putExtra("cycleItemCurrent", currentSelected);
		intentCycleAdjustment.putExtra("BinCycleCount", bin);
		intentCycleAdjustment.putExtra("path", path);
		//isUpdateCycleCount is False
		intentCycleAdjustment.putExtra("isUpdateCycleCount", false);
		intentCycleAdjustment.putExtra("isLp", false);
		intentCycleAdjustment.putExtra("newBinNumber", newLocation);
		startActivityForResult(intentCycleAdjustment,
				GlobalParams.AC_CYCLE_COUNT_ADJUSMENT_ACTIVITY);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// register to receive notifications from SingleEntryApplication
        // these notifications originate from ScanAPI
		typeScan = CommonData.SCAN_ITEM;
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
	        	isConnectSocket = true;
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
				
				if(typeScan == CommonData.SCAN_ITEM) {
					processScanData(barcode);
				} if(typeScan == CommonData.SCAN_LOT) {
					processScanDataLot(barcode);
				}
	        }
	    }
	};
}
