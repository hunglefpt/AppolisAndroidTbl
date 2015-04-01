/**
 * Name: $RCSfile: AcMoveDetails.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.move;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

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
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.appolis.androidtablet.R;
import com.appolis.common.AppolisException;
import com.appolis.common.LanguagePreferences;
import com.appolis.entities.EnBarcodeExistences;
import com.appolis.entities.EnBinTransfer;
import com.appolis.entities.EnItemNumber;
import com.appolis.entities.EnLPNumber;
import com.appolis.entities.EnUom;
import com.appolis.login.MainActivity;
import com.appolis.network.NetParameter;
import com.appolis.network.access.HttpNetServices;
import com.appolis.scan.CaptureBarcodeCamera;
import com.appolis.scan.SingleEntryApplication;
import com.appolis.utilities.DataParser;
import com.appolis.utilities.DecimalDigitsInputFilter;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.Logger;
import com.appolis.utilities.StringUtils;
import com.appolis.utilities.Utilities;

/**
 * @author hoangnh11
 * Display Move detail and complete move Items
 */
public class AcMoveDetails extends Activity implements OnClickListener {

	private TextView tvHeader;
	private ImageView imgHome, imgScan;
	private Button btnCancel, btnOK;
	Bundle bundle;
	private String barCode, checkLP, message, binTransfer;
	private ProgressDialog dialog, diaglogPost;
	private EnItemNumber itemNumber;
	private TextView tvTransfer, tvItemDescription, tvmaxQty;
	private Spinner spn_Move_UOM;
	private ArrayList<EnUom> enUom;
	private EditText edtLotValue, edt_move_from, et_move_qty, et_move_to;
	private LinearLayout linLot, linMaxQty, linUOM, LinSelectOrScan;
	private String uom;
	private EnBarcodeExistences enBarcodeExistences;
	private List<EnBinTransfer> listBinTransfer;
	private EnBinTransfer enBinTransfer;
	private EnLPNumber enLPNumber;
	private boolean checkLotFocus = false;
	private boolean checkFromFocus = false;
	private boolean checkToFocus = false;
	private boolean checkSocket = false;
	private boolean checkFirstUom = false;
	private LanguagePreferences languagePrefs;
	private TextView textView_move, tvTitleTransfer, tvTitleMaxQty, tvUOM, tvLot, tvFrom, tvQtyView, tvTo;
	private boolean activityIsRunning = false;
	private String scanFlag;	
	DecimalFormat df;
	String _significantDigits;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		languagePrefs = new LanguagePreferences(getApplicationContext());
		setContentView(R.layout.move_details_layout);
		scanFlag = GlobalParams.BLANK_CHARACTER;
		activityIsRunning = true;
		initLayout();
		if (bundle.containsKey(GlobalParams.BARCODE_MOVE)) {
			barCode = bundle.getString(GlobalParams.BARCODE_MOVE);		
			checkLP = bundle.getString(GlobalParams.CHECK_LP_OR_NOT_LP);
			
			if (checkLP.equalsIgnoreCase(GlobalParams.TRUE)) {
				GetLPDataAsyncTask getLPDataAsyncTask = new GetLPDataAsyncTask();
				getLPDataAsyncTask.execute();
				
				et_move_to.setOnFocusChangeListener(new OnFocusChangeListener() {
					
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (!checkSocket) {	
							if (hasFocus) {
								imgScan.setVisibility(View.VISIBLE);
								checkLotFocus = false;
								checkFromFocus = false;
								checkToFocus = true;
							} else {
								imgScan.setVisibility(View.GONE);						
							}
						} else {
							if (hasFocus) {								
								checkLotFocus = false;
								checkFromFocus = false;
								checkToFocus = true;
							}
						}
					}
				});
			} else {
				GetDataAsyncTask getDataAsyncTask = new GetDataAsyncTask();
				getDataAsyncTask.execute();

				edtLotValue.setOnFocusChangeListener(new OnFocusChangeListener() {
					
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (!checkSocket) {		
							if (hasFocus) {
								imgScan.setVisibility(View.VISIBLE);
								checkLotFocus = true;
								checkFromFocus = false;
								checkToFocus = false;
							} else {
								imgScan.setVisibility(View.GONE);					
							}
						} else {
							if (hasFocus) {								
								checkLotFocus = true;
								checkFromFocus = false;
								checkToFocus = false;
							}
						}
					}
				});
				
				edt_move_from.setOnFocusChangeListener(new OnFocusChangeListener() {
					
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (!checkSocket) {	
							if (hasFocus) {
								imgScan.setVisibility(View.VISIBLE);
								checkLotFocus = false;
								checkFromFocus = true;
								checkToFocus = false;
							} else {
								imgScan.setVisibility(View.GONE);						
							}
						} else {
							if (hasFocus) {								
								checkLotFocus = false;
								checkFromFocus = true;
								checkToFocus = false;
							}
						}
					}
				});
				
				et_move_to.setOnFocusChangeListener(new OnFocusChangeListener() {
					
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (!checkSocket) {	
							if (hasFocus) {
								imgScan.setVisibility(View.VISIBLE);
								checkLotFocus = false;
								checkFromFocus = false;
								checkToFocus = true;
							} else {
								imgScan.setVisibility(View.GONE);						
							}
						} else {
							if (hasFocus) {								
								checkLotFocus = false;
								checkFromFocus = false;
								checkToFocus = true;
							}
						}
					}
				});
			}			
		}
	}

	/**
	 * instance layout
	 */
	public void initLayout() {
		enLPNumber = new EnLPNumber();
		itemNumber = new EnItemNumber();
		enUom = new ArrayList<>();
		enBarcodeExistences = new EnBarcodeExistences();		
		bundle = this.getBundle();
		
		tvHeader = (TextView) findViewById(R.id.tvHeader);
		tvHeader.setText(getLanguage(GlobalParams.MV_TITLE_MOVE, GlobalParams.MOVE));
		imgHome = (ImageView) findViewById(R.id.imgHome);
		imgHome.setVisibility(View.VISIBLE);
		imgScan = (ImageView) findViewById(R.id.img_main_menu_scan_barcode);
		imgScan.setVisibility(View.GONE);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnOK = (Button) findViewById(R.id.btnOK);	
		tvTransfer = (TextView) findViewById(R.id.tvTransfer);
		tvItemDescription = (TextView) findViewById(R.id.tvItemDescription);
		spn_Move_UOM = (Spinner) findViewById(R.id.spn_Move_UOM);
		edtLotValue = (EditText) findViewById(R.id.edtLotValue);
		edt_move_from = (EditText) findViewById(R.id.edt_move_from);
		tvmaxQty = (TextView) findViewById(R.id.tvmaxQty);
		et_move_qty = (EditText) findViewById(R.id.et_move_qty);
		et_move_to = (EditText) findViewById(R.id.et_move_to);
		linLot = (LinearLayout) findViewById(R.id.linLot);
		linMaxQty = (LinearLayout) findViewById(R.id.linMaxQty);
		linUOM = (LinearLayout) findViewById(R.id.linUOM);
		LinSelectOrScan = (LinearLayout) findViewById(R.id.LinSelectOrScan);
		textView_move = (TextView) findViewById(R.id.textView_move);
		tvTitleTransfer = (TextView) findViewById(R.id.tvTitleTransfer);
		tvTitleMaxQty = (TextView) findViewById(R.id.tvTitleMaxQty);
		tvUOM = (TextView) findViewById(R.id.tvUOM);
		tvLot = (TextView) findViewById(R.id.tvLot);
		tvFrom = (TextView) findViewById(R.id.tvFrom);
		tvQtyView = (TextView) findViewById(R.id.tvQtyView);
		tvTo = (TextView) findViewById(R.id.tvTo);
		
		textView_move.setText(getLanguage(GlobalParams.MV_MSG_SELECTORSCAN_KEY, GlobalParams.MV_MSG_SELECTORSCAN_VALUE));
		tvTitleTransfer.setText(getLanguage(GlobalParams.TRANSFER, GlobalParams.TRANSFER) + GlobalParams.VERTICAL_TWO_DOT);
		tvTitleMaxQty.setText(getLanguage(GlobalParams.MV_LBL_MAXQTY, GlobalParams.DMG_LBL_MAXQTY_VALUE));
		tvUOM.setText(getLanguage(GlobalParams.MV_LBL_UOM, GlobalParams.UNIT_OF_MEASURE_VALUE)
				+ GlobalParams.VERTICAL_TWO_DOT);
		tvLot.setText(getLanguage(GlobalParams.REST_GRD_LOT_KEY, GlobalParams.LOT) + GlobalParams.VERTICAL_TWO_DOT);		
		edtLotValue.setHint(getLanguage(GlobalParams.LOTNUMBER, GlobalParams.LOT_NUMBER_STRING));
		tvFrom.setText(getLanguage(GlobalParams.MV_LBL_FROM, GlobalParams.FROM) + GlobalParams.VERTICAL_TWO_DOT);
		edt_move_from.setHint(getLanguage(GlobalParams.BINNUMBER, GlobalParams.BIN_NUMBER_LOWER));
		tvQtyView.setText(getLanguage(GlobalParams.RID_GRD_QTY_KEY, GlobalParams.QTY));
		et_move_qty.setHint(getLanguage(GlobalParams.REST_GRD_QTY, GlobalParams.QUANTITY));
		tvTo.setText(getLanguage(GlobalParams.MV_LBL_TO, GlobalParams.TO) + GlobalParams.VERTICAL_TWO_DOT);
		et_move_to.setHint(getLanguage(GlobalParams.BINNUMBER, GlobalParams.BINNUMBER));
		
		btnOK.setText(getLanguage(GlobalParams.OK, GlobalParams.OK));
		btnCancel.setText(getLanguage(GlobalParams.CANCEL, GlobalParams.CANCEL));
		
		imgHome.setOnClickListener(this);		
		btnCancel.setOnClickListener(this);
		btnOK.setOnClickListener(this);
		imgScan.setOnClickListener(this);
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
			intent = new Intent(AcMoveDetails.this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(intent, GlobalParams.LOGINSCREEN_TO_MAINSCREEN);
			break;
			
		case R.id.img_main_menu_scan_barcode:
			intent = new Intent(this, CaptureBarcodeCamera.class);		
			startActivityForResult(intent, GlobalParams.AC_MOVE_LEVEL_TWO);
			break;
			
		case R.id.btnCancel:
			finish();
			break;
			
		case R.id.btnOK:	
			try {	
				PostItemAsyncTask postItemAsyncTask = new PostItemAsyncTask();				
		        postItemAsyncTask.execute();		
			} catch (Exception e) {
				
			}
			
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
		activityIsRunning = true;
		switch (requestCode) {
		case GlobalParams.AC_MOVE_LEVEL_TWO:
			if(resultCode == RESULT_OK){
			   message = data.getStringExtra(GlobalParams.RESULTSCAN);
			   if (checkLotFocus) {
				   edtLotValue.setText(message);
				   CheckLotNumberAsyncTask checkLotNumberAsyncTask = new CheckLotNumberAsyncTask();
				   checkLotNumberAsyncTask.execute();
			   } else if (checkFromFocus) {
				   edt_move_from.setText(message);
				   CheckFromBinAsyncTask checkFromBinAsyncTask = new CheckFromBinAsyncTask();
				   checkFromBinAsyncTask.execute();
			   } else if (checkToFocus){
				   et_move_to.setText(message);
				   BarcodeAsyncTask barcodeAsyncTask = new BarcodeAsyncTask();
		           barcodeAsyncTask.execute();
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
	        	checkSocket = true;	        	
	        }
	        
	        // a Scanner has disconnected
	        else if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_SCANNER_REMOVAL))
	        {
	        	imgScan.setVisibility(View.VISIBLE);
	        	checkSocket = false;
	        }
	        
	        // decoded Data received from a scanner
	        else if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_DECODED_DATA))
	        {
				char[] data = intent.getCharArrayExtra(SingleEntryApplication.EXTRA_DECODEDDATA);
				if (scanFlag.equals(GlobalParams.FLAG_ACTIVE)) {
					if (checkLotFocus) {
					   edtLotValue.setText(new String(data));
					   CheckLotNumberAsyncTask checkLotNumberAsyncTask = new CheckLotNumberAsyncTask();
					   checkLotNumberAsyncTask.execute();
				    } else if (checkFromFocus) { 
					   edt_move_from.setText(new String(data));
					   CheckFromBinAsyncTask checkFromBinAsyncTask = new CheckFromBinAsyncTask();
					   checkFromBinAsyncTask.execute();
				    } else if (checkToFocus){
					   et_move_to.setText(new String(data));
					   BarcodeAsyncTask barcodeAsyncTask = new BarcodeAsyncTask();
			           barcodeAsyncTask.execute();
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
		activityIsRunning = true;
		onRegisterReceiver();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		activityIsRunning = false;
		onUnregisterReceiver();		
	}
	
	/**
	 * Get data License plate
	 * @author hoangnh11
	 */
	class GetLPDataAsyncTask extends AsyncTask<Void, Void, String> {
		String data, dataUOM;
		Intent intent;
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(AcMoveDetails.this);
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
					netParameter[0] = new NetParameter("barcode", 
							URLEncoder.encode(barCode, GlobalParams.UTF_8));
					data = HttpNetServices.Instance.getLpByBarcode(netParameter);
					enLPNumber = DataParser.getBinLPNumber(data);
					Logger.error(data);					
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
			dialog.dismiss();
			// If not cancel by user
			if (!isCancelled()) {
				if (result.equals("true")) {
					if (enLPNumber != null) {
						LinSelectOrScan.setVisibility(View.INVISIBLE);
						linMaxQty.setVisibility(View.INVISIBLE);
						linUOM.setVisibility(View.INVISIBLE);
						linLot.setVisibility(View.INVISIBLE);
						edt_move_from.setEnabled(false);
						edt_move_from.setBackgroundResource(R.color.transparent);
						et_move_qty.setEnabled(false);
						et_move_qty.setBackgroundResource(R.color.transparent);
						et_move_qty.setText("1");
						et_move_to.setOnEditorActionListener(new OnEditorActionListener() {
							
							@Override
							public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
								if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) 
										|| (actionId == EditorInfo.IME_ACTION_DONE)) {
									BarcodeAsyncTask barcodeAsyncTask = new BarcodeAsyncTask();
						            barcodeAsyncTask.execute();
					            }
								return false;
							}
						});
						tvTransfer.setText(enLPNumber.get_binNumber());
						tvItemDescription.setText(languagePrefs.getPreferencesString
								(GlobalParams.LICENSE_PLATE, GlobalParams.LICENSE_PLATE));
						edt_move_from.setText(enLPNumber.get_topBinNumber());
						scanFlag = GlobalParams.FLAG_ACTIVE;
					} else {
						showPopUp(AcMoveDetails.this, null, getResources().getString(R.string.LOADING_FAIL));
					}
				} else {
					String msg = languagePrefs.getPreferencesString
							(GlobalParams.ERRORUNABLETOCONTACTSERVER, GlobalParams.ERROR_INVALID_NETWORK);				
					showPopUp(AcMoveDetails.this, null, msg);
				}
			} else {
				scanFlag = GlobalParams.FLAG_ACTIVE;
			}
		}
	}
	
	/**
	 * Get list of available UOM�s 
	 * @author hoangnh11
	 */
	class GetDataAsyncTask extends AsyncTask<Void, Void, String> {
		String data, dataUOM;
		Intent intent;
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(AcMoveDetails.this);
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
					netParameter[0] = new NetParameter("barcode", URLEncoder.encode(barCode, GlobalParams.UTF_8));
					data = HttpNetServices.Instance.getItemBarcode(netParameter);
					itemNumber = DataParser.getItemNumber(data);
					Logger.error(data);
					
					if (StringUtils.isNotBlank(itemNumber.get_LotNumber())) {
						NetParameter[] netParameterUOM = new NetParameter[1];
						netParameterUOM[0] = new NetParameter("itemNumber", 
								URLEncoder.encode(itemNumber.get_itemNumber(), GlobalParams.UTF_8));
						dataUOM = HttpNetServices.Instance.getUOMItemNumber(netParameterUOM);
						enUom =  DataParser.getUom(dataUOM);
					} else {
						NetParameter[] netParameterUOM = new NetParameter[1];
						netParameterUOM[0] = new NetParameter("itemNumber", URLEncoder.encode(barCode, GlobalParams.UTF_8));
						dataUOM = HttpNetServices.Instance.getUOMItemNumber(netParameterUOM);
						enUom =  DataParser.getUom(dataUOM);
					}
					
					Logger.error(dataUOM);
					
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
					if (itemNumber != null && enUom != null) {
						tvTransfer.setText(itemNumber.get_itemNumber());
						tvItemDescription.setText(itemNumber.get_itemDescription());
						ArrayList<String> listUom = new ArrayList<String>();
						for (int i = 0; i < enUom.size(); i++) {
							listUom.add(enUom.get(i).getUomDescription());
						}
						
						ArrayAdapter<String> uomAdapter = new ArrayAdapter<String>(AcMoveDetails.this,
								R.layout.custom_spinner_item, listUom);
						spn_Move_UOM.setAdapter(uomAdapter);
						
						int checkIndexUOM = 0;						
						for (int i = 0; i < enUom.size(); i++) {
							if (itemNumber.get_uomTypeID() == enUom.get(i).getUomId()) {
								checkIndexUOM = i;
							}
						}
						
						spn_Move_UOM.setSelection(checkIndexUOM);
						spn_Move_UOM.setOnItemSelectedListener(new OnItemSelectedListener() {
							
							@Override
							public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
								uom = spn_Move_UOM.getSelectedItem().toString();
								if (checkFirstUom) {
									UpdateMaxQtyAsyncTask updateMaxQtyAsyncTask = new UpdateMaxQtyAsyncTask();
									updateMaxQtyAsyncTask.execute();
								}
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
							}
						});
						
						if (!itemNumber.is_LotTrackingInd()) {
							linLot.setVisibility(View.INVISIBLE);
							edt_move_from.setOnEditorActionListener(new OnEditorActionListener() {
								
								@Override
								public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
									if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) 
											|| (actionId == EditorInfo.IME_ACTION_DONE)) {
										CheckFromBinAsyncTask checkFromBinAsyncTask = new CheckFromBinAsyncTask();
										checkFromBinAsyncTask.execute();
						            }
									return false;
								}
							});							
						} else if (itemNumber.is_LotTrackingInd() && StringUtils.isNotBlank(itemNumber.get_LotNumber())){
							linLot.setVisibility(View.VISIBLE);
							edtLotValue.setEnabled(false);
							edtLotValue.setBackgroundResource(R.color.transparent);
							edtLotValue.setText(itemNumber.get_LotNumber());
							edt_move_from.setOnEditorActionListener(new OnEditorActionListener() {
								
								@Override
								public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
									if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) 
											|| (actionId == EditorInfo.IME_ACTION_DONE)) {
										CheckFromBinAsyncTask checkFromBinAsyncTask = new CheckFromBinAsyncTask();
										checkFromBinAsyncTask.execute();
						            }
									return false;
								}
							});							
						} else {
							linLot.setVisibility(View.VISIBLE);
							edtLotValue.setOnEditorActionListener(new OnEditorActionListener() {
								
								@Override
								public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
									if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) 
											|| (actionId == EditorInfo.IME_ACTION_DONE)) {
										CheckLotNumberAsyncTask checkLotNumberAsyncTask = new CheckLotNumberAsyncTask();
										checkLotNumberAsyncTask.execute();
						            }
									return false;
								}
							});
							
						}
						scanFlag = GlobalParams.FLAG_ACTIVE;
					} else {
						showPopUp(AcMoveDetails.this, null, getResources().getString(R.string.LOADING_FAIL));
					}
				} else {
					if (tvTransfer.getText().toString().equals(GlobalParams.BLANK_CHARACTER)) {
						String msg = languagePrefs.getPreferencesString
								(GlobalParams.ARITHMETIC_OVERFLOW, GlobalParams.ARITHMETIC_OVERFLOW);
						showPopUp(AcMoveDetails.this, null, msg);
					} else {
						String msg = languagePrefs.getPreferencesString
								(GlobalParams.ERRORUNABLETOCONTACTSERVER, GlobalParams.ERROR_INVALID_NETWORK);
						showPopUp(AcMoveDetails.this, null, msg);
					}					
				}
			} else {
				scanFlag = GlobalParams.FLAG_ACTIVE;
			}
		}
	}
	
	/**
	 * Validate Lot number
	 * @author hoangnh11
	 */
	class CheckLotNumberAsyncTask extends AsyncTask<Void, Void, String> {
		String data;
		Intent intent;
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(AcMoveDetails.this);
			dialog.setMessage(GlobalParams.CHECKING_DATA);
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
					netParameter[0] = new NetParameter("barcode", barCode +
							URLEncoder.encode(edtLotValue.getEditableText().toString().trim(), GlobalParams.UTF_8));
					data = HttpNetServices.Instance.getItemBarcode(netParameter);
					itemNumber = DataParser.getItemNumber(data);
					Logger.error(data);
					
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
			dialog.dismiss();
			// If not cancel by user
			if (!isCancelled()) {
				if (result.equals("true")) {
					if (itemNumber != null && itemNumber.get_LotNumber() != null 
							&& StringUtils.isNotBlank(itemNumber.get_LotNumber())) {
						edt_move_from.requestFocus();
						edt_move_from.setOnEditorActionListener(new OnEditorActionListener() {
							
							@Override
							public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
								if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) 
										|| (actionId == EditorInfo.IME_ACTION_DONE)) {
									CheckFromBinAsyncTask checkFromBinAsyncTask = new CheckFromBinAsyncTask();
									checkFromBinAsyncTask.execute();
					            }
								return false;
							}
						});
						scanFlag = GlobalParams.FLAG_ACTIVE;
					} else {
						showPopUp(AcMoveDetails.this, null,
								getLanguage(GlobalParams.ERRORINVALIDLOTNUMFORITEM,	GlobalParams.INVALID_LOT));
						edt_move_from.setText(GlobalParams.BLANK_CHARACTER);		
					}
				} else {
					showPopUp(AcMoveDetails.this, null,
							getLanguage(GlobalParams.ERRORINVALIDLOTNUMFORITEM,	GlobalParams.INVALID_LOT));
					edt_move_from.setText(GlobalParams.BLANK_CHARACTER);
				}
			} else {
				scanFlag = GlobalParams.FLAG_ACTIVE;
			}
		}
	}
	
	/**
	 * Validate From location
	 * @author hoangnh11
	 */
	class CheckFromBinAsyncTask extends AsyncTask<Void, Void, String> {
		String data;
		Intent intent;		
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(AcMoveDetails.this);
			dialog.setMessage(GlobalParams.CHECKING_DATA);
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
					NetParameter[] netParameter = new NetParameter[4];
					if (StringUtils.isNotBlank(itemNumber.get_LotNumber())) {					
						netParameter[0] = new NetParameter("itemNumber", URLEncoder.encode(itemNumber.get_itemNumber(), GlobalParams.UTF_8));
					} else {
						netParameter[0] = new NetParameter("itemNumber", URLEncoder.encode(barCode, GlobalParams.UTF_8));
					}
					
					netParameter[1] = new NetParameter("lotnumber", URLEncoder.encode(edtLotValue.getEditableText().toString(), GlobalParams.UTF_8));
					netParameter[2] = new NetParameter("uom", URLEncoder.encode(uom, GlobalParams.UTF_8));
					netParameter[3] = new NetParameter("binNumber", edt_move_from.getEditableText().toString());
					data = HttpNetServices.Instance.getBins(netParameter);
					itemNumber = DataParser.getItemNumber(data);
					Logger.error(data);
					
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
			dialog.dismiss();
			// If not cancel by user
			if (!isCancelled()) {
				if (result.equals("true")) {
					if (itemNumber != null) {
						_significantDigits = Utilities.getSignificantDigits(itemNumber.get_significantDigits());
						df = new DecimalFormat(_significantDigits);
						tvmaxQty.setText(df.format(itemNumber.get_quantityOnHand()));					
						et_move_qty.setText(String.valueOf(tvmaxQty.getText()));
						et_move_qty.setEnabled(true);
						et_move_qty.requestFocus();
						et_move_to.setOnEditorActionListener(new OnEditorActionListener() {
							
							@Override
							public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
								if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) 
										|| (actionId == EditorInfo.IME_ACTION_DONE)) {
									BarcodeAsyncTask barcodeAsyncTask = new BarcodeAsyncTask();
						            barcodeAsyncTask.execute();
					            }
								return false;
							}
						});
						
						et_move_qty.addTextChangedListener(new TextWatcher() {
							
							@Override
							public void onTextChanged(CharSequence s, int start, int before, int count) {							
								if (s.toString().contains(GlobalParams.DOT) &&  s.length() == 1) {
									et_move_qty.setText(GlobalParams.BLANK_CHARACTER);
								} else {
									if (s.length() > 0 && Double.parseDouble(s.toString()) 
											> Double.parseDouble(String.valueOf(tvmaxQty.getText()))) {								
										Utilities.showPopUp(AcMoveDetails.this, null,
												getLanguage(GlobalParams.MV_LIMITQTY_MSG_VALUE,
														GlobalParams.MV_LIMITQTY_MSG_VALUE));
										et_move_qty.setText(String.valueOf(tvmaxQty.getText()));
									} else {
										et_move_qty.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(
												itemNumber.get_significantDigits())});
									}
								}
								
								if (et_move_qty.getText().toString().equals("0") 
										|| StringUtils.isBlank(et_move_qty.getText().toString())) {
									btnOK.setEnabled(false);
								}
							}
							
							@Override
							public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
							}
							
							@Override
							public void afterTextChanged(Editable s) {
							}
						});
						
						checkFirstUom = true;
						scanFlag = GlobalParams.FLAG_ACTIVE;
					} else {
						showPopUp(AcMoveDetails.this, null,
								getLanguage(GlobalParams.BIN_MESSAGEBOXINVALIDLOCATIONSCAN,
										GlobalParams.INVALID_LOCATION_SCAN_PLEASE_SCAN_A_VALID_LOCATION));
						if (edtLotValue.isEnabled()) {
							edtLotValue.setText(GlobalParams.BLANK_CHARACTER);	
						}
						edt_move_from.setText(GlobalParams.BLANK_CHARACTER);
					}
				} else {
					showPopUp(AcMoveDetails.this, null,
							getLanguage(GlobalParams.BIN_MESSAGEBOXINVALIDLOCATIONSCAN,
									GlobalParams.INVALID_LOCATION_SCAN_PLEASE_SCAN_A_VALID_LOCATION));
					if (edtLotValue.isEnabled()) {
						edtLotValue.setText(GlobalParams.BLANK_CHARACTER);	
					}
					edt_move_from.setText(GlobalParams.BLANK_CHARACTER);
				}
			} else {
				scanFlag = GlobalParams.FLAG_ACTIVE;
			}
		}
	}
	
	/**
	 * Validate To location
	 * @author hoangnh11
	 */
	class BarcodeAsyncTask extends AsyncTask<Void, Void, String> {
		String data;
		Intent intent;
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(AcMoveDetails.this);
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
					netParameter[0] = new NetParameter("barcode", et_move_to.getEditableText().toString());
					data = HttpNetServices.Instance.getBarcode(netParameter);
					enBarcodeExistences = DataParser.getBarcode(data);
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
			dialog.dismiss();
			// If not cancel by user
			if (!isCancelled()) {
				if (result.equals("true")) {
					if (edt_move_from.getEditableText().toString().trim().equalsIgnoreCase
							(et_move_to.getEditableText().toString().trim())) {
						showPopUp(AcMoveDetails.this, null,
								getLanguage(GlobalParams.BINTRANSFER_MSGLPMOVETOSELFERROR_VALUE,
										GlobalParams.BINTRANSFER_MSGLPMOVETOSELFERROR_VALUE));
					} else {
						if (enBarcodeExistences != null) {
							if (enBarcodeExistences.getBinOnlyCount() != 0 || enBarcodeExistences.getLPCount() != 0) {
								
								try {
									prepareJsonForm();
								} catch (JSONException e) {
									Logger.error(e);
								}
								
								if (et_move_qty.getText().toString().equals("0") 
										|| StringUtils.isBlank(et_move_qty.getText().toString())) {
									btnOK.setEnabled(false);
								} else {
									btnOK.setEnabled(true);
								}
								scanFlag = GlobalParams.FLAG_ACTIVE;
							} else {
								showPopUp(AcMoveDetails.this, null,
										getLanguage(GlobalParams.BIN_MESSAGEBOXINVALIDLOCATIONSCAN,
												GlobalParams.INVALID_LOCATION_SCAN_PLEASE_SCAN_A_VALID_LOCATION));
							}					
						} else {
							showPopUp(AcMoveDetails.this, null,
									getLanguage(GlobalParams.BIN_MESSAGEBOXINVALIDLOCATIONSCAN,
											GlobalParams.INVALID_LOCATION_SCAN_PLEASE_SCAN_A_VALID_LOCATION));
							et_move_to.setText(GlobalParams.BLANK_CHARACTER);
						}
					}					
				} else {
					showPopUp(AcMoveDetails.this, null,
							getLanguage(GlobalParams.BIN_MESSAGEBOXINVALIDLOCATIONSCAN,
									GlobalParams.INVALID_LOCATION_SCAN_PLEASE_SCAN_A_VALID_LOCATION));
					et_move_to.setText(GlobalParams.BLANK_CHARACTER);
				}
			} else {
				scanFlag = GlobalParams.FLAG_ACTIVE;
			}
		}
	}
	
	/**
	 * Summit All field
	 * @author hoangnh11
	 */
	class PostItemAsyncTask extends AsyncTask<Void, Void, String> {
		String data;
		Intent intent;
		
		@Override
		protected void onPreExecute() {
			diaglogPost = new ProgressDialog(AcMoveDetails.this);
			diaglogPost.setMessage(GlobalParams.PROCESS_DATA);
			diaglogPost.setCancelable(false); 
			diaglogPost.setCanceledOnTouchOutside(false);
			diaglogPost.show();
			scanFlag = GlobalParams.FLAG_INACTIVE;
		}
		
		@Override
		protected String doInBackground(Void... params) {
			String result;
			if (!isCancelled()) {
				try {				
					data = HttpNetServices.Instance.postItem(binTransfer);
					Logger.error(data);					
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
			if (activityIsRunning) {
				diaglogPost.dismiss();
				// If not cancel by user
				if (!isCancelled()) {
					if (result.equals("true")) {
						if (data != null && data.equalsIgnoreCase(GlobalParams.TRUE)) {
							AcMoveDetails.this.finish();
							scanFlag = GlobalParams.FLAG_ACTIVE;
						} else if (data.equalsIgnoreCase(GlobalParams.SELECTED_BIN_TO_DOES_NOT_ALLOW_TRANSFERS_TO)) {
							showPopUp(AcMoveDetails.this, null,
									getLanguage(GlobalParams.CANNOT_TRANSFER_ITEMS,
											GlobalParams.CANNOT_TRANSFER_ITEMS));
						} else {
							showPopUp(AcMoveDetails.this, null, ""+data);
						}
					} else {
						showPopUp(AcMoveDetails.this, null, ""+data);
					}
				} else {
					scanFlag = GlobalParams.FLAG_ACTIVE;
				}
			}
		}
	}
	
	/**
	 * Update max quantity
	 * @author hoangnh11
	 */
	class UpdateMaxQtyAsyncTask extends AsyncTask<Void, Void, String> {
		String data;
		Intent intent;
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(AcMoveDetails.this);
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
					NetParameter[] netParameter = new NetParameter[4];
					netParameter[0] = new NetParameter("itemNumber", URLEncoder.encode(barCode, GlobalParams.UTF_8));
					netParameter[1] = new NetParameter("lotnumber", URLEncoder.encode(edtLotValue.getEditableText().toString().trim(), GlobalParams.UTF_8));
					netParameter[2] = new NetParameter("uom", URLEncoder.encode(uom, GlobalParams.UTF_8));
					netParameter[3] = new NetParameter("binNumber", edt_move_from.getEditableText().toString());
					data = HttpNetServices.Instance.getBins(netParameter);
					itemNumber = DataParser.getItemNumber(data);
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
					if (itemNumber != null) {
						_significantDigits = Utilities.getSignificantDigits(itemNumber.get_significantDigits());
						df = new DecimalFormat(_significantDigits);
						tvmaxQty.setText(df.format(itemNumber.get_quantityOnHand()));
						et_move_qty.setText(GlobalParams.BLANK_CHARACTER);
						scanFlag = GlobalParams.FLAG_ACTIVE;
					} else {
						String msg = languagePrefs.getPreferencesString
								(GlobalParams.ERRORUNABLETOCONTACTSERVER, GlobalParams.ERROR_INVALID_NETWORK);				
						showPopUp(AcMoveDetails.this, null, msg);						
					}
				} else {
					String msg = languagePrefs.getPreferencesString
							(GlobalParams.ERRORUNABLETOCONTACTSERVER, GlobalParams.ERROR_INVALID_NETWORK);				
					showPopUp(AcMoveDetails.this, null, msg);					
				}
			}
		}
	}
	
	/**
	 * Set value to Object EnBinTransfer
	 * @throws JSONException
	 */
	private void prepareJsonForm() throws JSONException {
		String encodeUom = uom;
		
		try {
			encodeUom = URLEncoder.encode(uom, GlobalParams.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		enBinTransfer = new EnBinTransfer();	
		enBinTransfer.setFromBinNumber(edt_move_from.getEditableText().toString());
		enBinTransfer.setIsLicensePlate(itemNumber.is_licensePlateInd());
		enBinTransfer.setItemNumber(tvTransfer.getText().toString());
		enBinTransfer.setLotNumber(edtLotValue.getEditableText().toString());
		if (StringUtils.isNotBlank(et_move_qty.getEditableText().toString())) {
			enBinTransfer.setQuantity(Double.parseDouble(et_move_qty.getEditableText().toString()));			
		} else {
			et_move_qty.setText("0");
			enBinTransfer.setQuantity(0);		
		}
		enBinTransfer.setToBinNumber(et_move_to.getEditableText().toString());
		enBinTransfer.setTransactionType("Bin Transfer");
		enBinTransfer.setUomDescription(encodeUom);
		listBinTransfer = new ArrayList<EnBinTransfer>();
		listBinTransfer.add(enBinTransfer);
		binTransfer = DataParser.convertObjectToString(listBinTransfer);
		Logger.error(binTransfer);
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
