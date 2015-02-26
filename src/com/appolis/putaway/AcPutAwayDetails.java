/**
 * Name: AcPutAwayDetails.java
 * Date: Jan 27, 2015 10:42:04 AM
 * Copyright (c) 2014 FPT Software, Inc. All rights reserved.
 */
package com.appolis.putaway;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
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
import com.appolis.entities.EnItemPODetails;
import com.appolis.entities.EnLPNumber;
import com.appolis.entities.EnPassPutAway;
import com.appolis.entities.EnPutAway;
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
public class AcPutAwayDetails extends Activity implements OnClickListener{

	private TextView tvHeader;
	private ImageView imgHome, imgScan;
	private Button btnCancel, btnOK;
	Bundle bundle;
	private String barCode, checkLP, checkBin, message, binTransfer;
	private ProgressDialog dialog;
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
	private EnItemPODetails enPutaway;
	private String lotNumber, binNumber, qtyNumber, lpNumber;
	private boolean checkSocket = false;
	private boolean checkFirstUom = false;
	private LanguagePreferences languagePrefs;
	private TextView textView_move, tvTitleTransfer, tvTitleMaxQty, tvUOM, tvLot, tvFrom, tvQtyView, tvTo;
	private EnPutAway passPutAway;
	private EnPassPutAway enPassPutAway;
	DecimalFormat df = new DecimalFormat("#0.00");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		languagePrefs = new LanguagePreferences(getApplicationContext());
		setContentView(R.layout.move_details_layout);
		initLayout();
		if (bundle.containsKey(GlobalParams.BARCODE_MOVE)) {
			barCode = bundle.getString(GlobalParams.BARCODE_MOVE);
			checkLP = bundle.getString(GlobalParams.CHECK_LP_OR_NOT_LP);
			checkBin = bundle.getString(GlobalParams.CHECK_BIN_OR_NOT_BIN);
			passPutAway = (EnPutAway) bundle.getSerializable(GlobalParams.PUT_AWAY_BIN_DATA);
			enPassPutAway = (EnPassPutAway) bundle.getSerializable(GlobalParams.PUT_PASS_AWAY_BIN_DATA);		
			if (bundle.containsKey(GlobalParams.LOT_NUMBER) && bundle.containsKey(GlobalParams.BIN_NUMBER)
					&& bundle.containsKey(GlobalParams.QTY_NUMBER)) {
				lotNumber = bundle.getString(GlobalParams.LOT_NUMBER);
				binNumber = bundle.getString(GlobalParams.BIN_NUMBER);
				qtyNumber = bundle.getString(GlobalParams.QTY_NUMBER);
			}			
		
			if (bundle.containsKey(GlobalParams.LP_NUMBER)) {
				lpNumber = bundle.getString(GlobalParams.LP_NUMBER);
			}
			
			if (checkLP.equalsIgnoreCase(GlobalParams.TRUE)) {
				GetLPDataAsyncTask getLPDataAsyncTask = new GetLPDataAsyncTask();
				getLPDataAsyncTask.execute();
				
				et_move_to.setOnFocusChangeListener(new OnFocusChangeListener() {
					
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (!checkSocket) {							
							if (hasFocus) {
								imgScan.setVisibility(View.VISIBLE);							
							} else {
								imgScan.setVisibility(View.GONE);						
							}							
						}
					}
				});
				
			} else if (checkBin.equalsIgnoreCase(GlobalParams.TRUE)) {				
				GetBinDataAsyncTask getBinDataAsyncTask = new GetBinDataAsyncTask();
				getBinDataAsyncTask.execute();
				
				et_move_to.setOnFocusChangeListener(new OnFocusChangeListener() {
					
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (!checkSocket) {							
							if (hasFocus) {
								imgScan.setVisibility(View.VISIBLE);							
							} else {
								imgScan.setVisibility(View.GONE);						
							}							
						}
					}
				});
			} else {
				GetDataAsyncTask getDataAsyncTask = new GetDataAsyncTask();
				getDataAsyncTask.execute();
				
				et_move_to.setOnFocusChangeListener(new OnFocusChangeListener() {
					
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (!checkSocket) {							
							if (hasFocus) {
								imgScan.setVisibility(View.VISIBLE);							
							} else {
								imgScan.setVisibility(View.GONE);						
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
		enPutaway = new EnItemPODetails();
		enLPNumber = new EnLPNumber();
		itemNumber = new EnItemNumber();
		enUom = new ArrayList<>();
		enBarcodeExistences = new EnBarcodeExistences();
		listBinTransfer = new ArrayList<EnBinTransfer>();
		passPutAway = new EnPutAway();
		bundle = this.getBundle();
		
		tvHeader = (TextView) findViewById(R.id.tvHeader);
		tvHeader.setText(getLanguage(GlobalParams.PUTAWAY_TITLE_PUTAWAY, GlobalParams.MOVE));
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
			intent = new Intent(AcPutAwayDetails.this, MainActivity.class);
			startActivityForResult(intent, GlobalParams.LOGINSCREEN_TO_MAINSCREEN);
			break;
			
		case R.id.img_main_menu_scan_barcode:
			intent = new Intent(this, CaptureBarcodeCamera.class);		
			startActivityForResult(intent, GlobalParams.AC_PUT_AWAY_LEVEL_TWO);
			break;
			
		case R.id.btnCancel:
			finish();
			break;
			
		case R.id.btnOK:
			try {
				prepareJsonForm();
			} catch (JSONException e) {
				
			}
			PostItemAsyncTask postItemAsyncTask = new PostItemAsyncTask();
	        postItemAsyncTask.execute();		
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
		case GlobalParams.AC_PUT_AWAY_LEVEL_TWO:
			if(resultCode == RESULT_OK){
			   message = data.getStringExtra(GlobalParams.RESULTSCAN);			
			   et_move_to.setText(message);
			   BarcodeAsyncTask barcodeAsyncTask = new BarcodeAsyncTask();
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
				et_move_to.setText(new String(data));
				BarcodeAsyncTask barcodeAsyncTask = new BarcodeAsyncTask();
	            barcodeAsyncTask.execute();
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
	 * Get data License plate
	 * @author hoangnh11
	 */
	class GetLPDataAsyncTask extends AsyncTask<Void, Void, String> {
		String data, dataUOM;
		Intent intent;
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(AcPutAwayDetails.this);
			dialog.setMessage(getLanguage(GlobalParams.LOADING_MSG, GlobalParams.LOADING_DATA));
			dialog.show();
			dialog.setCancelable(false); 
			dialog.setCanceledOnTouchOutside(false);
		}
		
		@Override
		protected String doInBackground(Void... params) {
			String result;
			if (!isCancelled()) {
				try {
					NetParameter[] netParameter = new NetParameter[1];
					netParameter[0] = new NetParameter("barcode", barCode);
					data = HttpNetServices.Instance.getLpByBarcode(netParameter);
					enLPNumber = DataParser.getBinLPNumber(data);
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
					if (enLPNumber != null) {
						edt_move_from.setEnabled(false);
						edt_move_from.setBackgroundResource(R.color.transparent);
						ArrayList<String> listUom = new ArrayList<String>();
						spn_Move_UOM.setEnabled(false);
						spn_Move_UOM.setBackgroundColor(getResources().getColor(R.color.transparent));
						et_move_qty.setEnabled(true);
						
//						if (passPutAway.get_itemDesc() != null) {
							tvTransfer.setText(passPutAway.get_itemNumber());
							tvItemDescription.setText(passPutAway.get_itemDesc());
							edt_move_from.setText(passPutAway.get_binNumber());
							tvmaxQty.setText(df.format(passPutAway.get_qty()));
							listUom.add(passPutAway.get_uomDescription());
							if (passPutAway.get_lotNumber() != null && StringUtils.isNotBlank(passPutAway.get_lotNumber())) {
								linLot.setVisibility(View.VISIBLE);
								edtLotValue.setEnabled(false);
								edtLotValue.setBackgroundResource(R.color.transparent);
								edtLotValue.setText(passPutAway.get_lotNumber());
							} else {
								linLot.setVisibility(View.INVISIBLE);
							}
							et_move_qty.setText(df.format(passPutAway.get_qty()));
//						} 
//						else {
//							tvTransfer.setText(enPassPutAway.getItemNumber());
//							tvItemDescription.setText(enPassPutAway.getItemDescription());
//							edt_move_from.setText("RcvBin1");
//							tvmaxQty.setText(df.format(enPassPutAway.getQuantityOnHand()));
//							listUom.add(enPassPutAway.getUomDescription());
//							if (enPassPutAway.getLotNumber() != null && StringUtils.isNotBlank(enPassPutAway.getLotNumber())) {
//								linLot.setVisibility(View.VISIBLE);
//								edtLotValue.setEnabled(false);
//								edtLotValue.setBackgroundResource(R.color.transparent);
//								edtLotValue.setText(enPassPutAway.getLotNumber());
//							} else {
//								linLot.setVisibility(View.INVISIBLE);
//							}
//							et_move_qty.setText(df.format(enPassPutAway.getQuantityOnHand()));
//						}
						
						try {
							ArrayAdapter<String> uomAdapter = new ArrayAdapter<String>(AcPutAwayDetails.this,
									R.layout.custom_spinner_false, listUom);
							spn_Move_UOM.setAdapter(uomAdapter);
							spn_Move_UOM.setOnItemSelectedListener(new OnItemSelectedListener() {
								
								@Override
								public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
									uom = spn_Move_UOM.getSelectedItem().toString();								
								}

								@Override
								public void onNothingSelected(AdapterView<?> arg0) {
								}
							});
						} catch (NullPointerException e) {
							Logger.error(e);
						} catch (Exception e) {
							Logger.error(e);
						}						
						
						et_move_qty.addTextChangedListener(new TextWatcher() {
							
							@Override
							public void onTextChanged(CharSequence s, int start, int before, int count) {
								if (s.toString().contains(GlobalParams.DOT) &&  s.length() == 1) {
									et_move_qty.setText(GlobalParams.BLANK_CHARACTER);
								} else {
									if (s.length() > 0 && Double.parseDouble(s.toString())
											> Double.parseDouble(String.valueOf(tvmaxQty.getText()))) {
										Utilities.showPopUp(AcPutAwayDetails.this, null, 
												getResources().getString(R.string.QTY_IS_NOT_GREATER_THAN_MAX_QTY));
										et_move_qty.setText(""+Double.parseDouble(String.valueOf(tvmaxQty.getText())));
										
//										if (passPutAway.get_itemDesc() != null) {
											et_move_qty.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(
													passPutAway.get_significantDigits())});
//										} else {
//											et_move_qty.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(
//													enPassPutAway.getSignificantDigits())});
//										}										
									} else {
//										if (passPutAway.get_itemDesc() != null) {
											et_move_qty.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(
													passPutAway.get_significantDigits())});
//										} else {
//											et_move_qty.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(
//													enPassPutAway.getSignificantDigits())});
//										}
									}
								}								
							}
							
							@Override
							public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
							}
							
							@Override
							public void afterTextChanged(Editable s) {
							}
						});
						
						btnOK.setEnabled(true);
						et_move_to.setText(barCode);
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
					} else {
						Utilities.showPopUp(AcPutAwayDetails.this, null, getResources().getString(R.string.LOADING_FAIL));
					}
				} else {
					Utilities.showPopUp(AcPutAwayDetails.this, null, getResources().getString(R.string.LOADING_FAIL));
				}
			}
		}
	}
	
	/**
	 * Get data License plate
	 * @author hoangnh11
	 */
	class GetBinDataAsyncTask extends AsyncTask<Void, Void, String> {
		String data, dataUOM;
		Intent intent;
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(AcPutAwayDetails.this);
			dialog.setMessage(getLanguage(GlobalParams.LOADING_MSG, GlobalParams.LOADING_DATA));
			dialog.show();
			dialog.setCancelable(false); 
			dialog.setCanceledOnTouchOutside(false);
		}
		
		@Override
		protected String doInBackground(Void... params) {
			String result;
			if (!isCancelled()) {
				try {
					NetParameter[] netParameter = new NetParameter[1];
					netParameter[0] = new NetParameter("barcode", barCode);
					data = HttpNetServices.Instance.getLpByBarcode(netParameter);
					enLPNumber = DataParser.getBinLPNumber(data);
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
					if (enLPNumber != null) {					
						edt_move_from.setEnabled(false);
						edt_move_from.setBackgroundResource(R.color.transparent);
						ArrayList<String> listUom = new ArrayList<String>();
						spn_Move_UOM.setEnabled(false);
						spn_Move_UOM.setBackgroundColor(getResources().getColor(R.color.transparent));
						et_move_qty.setEnabled(true);
						
//						if (passPutAway.get_itemDesc() != null) {							
							tvTransfer.setText(passPutAway.get_itemNumber());
							tvItemDescription.setText(passPutAway.get_itemDesc());
							edt_move_from.setText(passPutAway.get_binNumber());
							tvmaxQty.setText(df.format(passPutAway.get_qty()));
							listUom.add(passPutAway.get_uomDescription());
							if (passPutAway.get_lotNumber() != null && StringUtils.isNotBlank(passPutAway.get_lotNumber())) {
								linLot.setVisibility(View.VISIBLE);
								edtLotValue.setEnabled(false);
								edtLotValue.setBackgroundResource(R.color.transparent);
								edtLotValue.setText(passPutAway.get_lotNumber());
							} else {
								linLot.setVisibility(View.INVISIBLE);
							}
							et_move_qty.setText(df.format(passPutAway.get_qty()));
//						} else {							
//							tvTransfer.setText(enPassPutAway.getItemNumber());
//							tvItemDescription.setText(enPassPutAway.getItemDescription());
//							edt_move_from.setText("Manufbin1");
//							tvmaxQty.setText(df.format(enPassPutAway.getQuantityOnHand()));
//							listUom.add(enPassPutAway.getUomDescription());
//							if (enPassPutAway.getLotNumber() != null && StringUtils.isNotBlank(enPassPutAway.getLotNumber())) {
//								linLot.setVisibility(View.VISIBLE);
//								edtLotValue.setEnabled(false);
//								edtLotValue.setBackgroundResource(R.color.transparent);
//								edtLotValue.setText(enPassPutAway.getLotNumber());
//							} else {
//								linLot.setVisibility(View.INVISIBLE);
//							}
//							et_move_qty.setText(df.format(enPassPutAway.getQuantityOnHand()));
//						}	
							
						try {
							ArrayAdapter<String> uomAdapter = new ArrayAdapter<String>(AcPutAwayDetails.this,
									R.layout.custom_spinner_false, listUom);
							spn_Move_UOM.setAdapter(uomAdapter);
							spn_Move_UOM.setOnItemSelectedListener(new OnItemSelectedListener() {
								
								@Override
								public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
									uom = spn_Move_UOM.getSelectedItem().toString();								
								}

								@Override
								public void onNothingSelected(AdapterView<?> arg0) {
								}
							});	
						} catch (NullPointerException e) {
							Logger.error(e);
						} catch (Exception e) {
							Logger.error(e);
						}
						
						et_move_qty.addTextChangedListener(new TextWatcher() {
							
							@Override
							public void onTextChanged(CharSequence s, int start, int before, int count) {
								if (s.toString().contains(GlobalParams.DOT) &&  s.length() == 1) {
									et_move_qty.setText(GlobalParams.BLANK_CHARACTER);
								} else {
									if (s.length() > 0 && Double.parseDouble(s.toString())
											> Double.parseDouble(String.valueOf(tvmaxQty.getText()))) {
										Utilities.showPopUp(AcPutAwayDetails.this, null, 
												getResources().getString(R.string.QTY_IS_NOT_GREATER_THAN_MAX_QTY));
										et_move_qty.setText(""+Double.parseDouble(String.valueOf(tvmaxQty.getText())));
										
//										if (passPutAway.get_itemDesc() != null) {
											et_move_qty.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(
													passPutAway.get_significantDigits())});
//										} else {
//											et_move_qty.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(
//													enPassPutAway.getSignificantDigits())});
//										}
									} else {
//										if (passPutAway.get_itemDesc() != null) {
											et_move_qty.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(
													passPutAway.get_significantDigits())});
//										} else {
//											et_move_qty.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(
//													enPassPutAway.getSignificantDigits())});
//										}
									}
								}								
							}
							
							@Override
							public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
							}
							
							@Override
							public void afterTextChanged(Editable s) {
							}
						});
						
						btnOK.setEnabled(true);
						et_move_to.setText(barCode);
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
					} else {
						Utilities.showPopUp(AcPutAwayDetails.this, null, getResources().getString(R.string.LOADING_FAIL));
					}
				} else {
					Utilities.showPopUp(AcPutAwayDetails.this, null, getResources().getString(R.string.LOADING_FAIL));
				}
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
			dialog = new ProgressDialog(AcPutAwayDetails.this);
			dialog.setMessage(getLanguage(GlobalParams.LOADING_MSG, GlobalParams.LOADING_DATA));
			dialog.show();
			dialog.setCancelable(false); 
			dialog.setCanceledOnTouchOutside(false);
		}
		
		@Override
		protected String doInBackground(Void... params) {
			String result;
			if (!isCancelled()) {
				try {
					NetParameter[] netParameter = new NetParameter[1];
					netParameter[0] = new NetParameter("barcode", barCode);
					data = HttpNetServices.Instance.getPutAway(netParameter);
					enPutaway = DataParser.getPutAwayDetails(data);
					Logger.error(data);
					
					NetParameter[] netParameterUOM = new NetParameter[1];
					netParameterUOM[0] = new NetParameter("itemNumber", barCode);
					dataUOM = HttpNetServices.Instance.getUOMItemNumber(netParameterUOM);
					enUom =  DataParser.getUom(dataUOM);
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
					if (enPutaway != null) {						
						tvTransfer.setText(enPutaway.getItemNumber());
						tvItemDescription.setText(enPutaway.getItemDescription());
						ArrayList<String> listUom = new ArrayList<String>();
						for (int i = 0; i < enUom.size(); i++) {
							listUom.add(enUom.get(i).getUomDescription());
						}
						
						try {
							ArrayAdapter<String> uomAdapter = new ArrayAdapter<String>(AcPutAwayDetails.this,
									R.layout.custom_spinner_item, listUom);
							spn_Move_UOM.setAdapter(uomAdapter);
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
						} catch (NullPointerException e) {
							Logger.error(e);
						} catch (Exception e) {
							Logger.error(e);
						}
						
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
						
						if (!enPutaway.isIsLotTracked()) {
							linLot.setVisibility(View.INVISIBLE);
							edt_move_from.setEnabled(false);
							edt_move_from.setBackgroundResource(R.color.transparent);
							edt_move_from.setText(binNumber);
							tvmaxQty.setText(df.format(qtyNumber));
							et_move_qty.setEnabled(true);
							et_move_qty.setText(df.format(qtyNumber));
							
						} else if (enPutaway.isIsLotTracked() && StringUtils.isNotBlank(enPutaway.getLotNumber())){
							linLot.setVisibility(View.VISIBLE);
							edtLotValue.setEnabled(false);
							edtLotValue.setBackgroundResource(R.color.transparent);
							edtLotValue.setText(lotNumber);
							edt_move_from.setEnabled(false);
							edt_move_from.setBackgroundResource(R.color.transparent);
							edt_move_from.setText(binNumber);
							tvmaxQty.setText(df.format(qtyNumber));
							et_move_qty.setEnabled(true);
							et_move_qty.setText(df.format(qtyNumber));
							
						} else {
							linLot.setVisibility(View.VISIBLE);
							edtLotValue.setEnabled(false);
							edtLotValue.setBackgroundResource(R.color.transparent);
							edtLotValue.setText(lotNumber);
							edt_move_from.setEnabled(false);
							edt_move_from.setBackgroundResource(R.color.transparent);
							edt_move_from.setText(binNumber);
							tvmaxQty.setText(df.format(qtyNumber));
							et_move_qty.setEnabled(true);
							et_move_qty.setText(df.format(qtyNumber));
						}
						
						et_move_qty.addTextChangedListener(new TextWatcher() {
							
							@Override
							public void onTextChanged(CharSequence s, int start, int before, int count) {
								if (s.toString().contains(GlobalParams.DOT) &&  s.length() == 1) {
									et_move_qty.setText(GlobalParams.BLANK_CHARACTER);
								} else {
									if (s.length() > 0 && Double.parseDouble(s.toString())
											> Double.parseDouble(String.valueOf(tvmaxQty.getText()))) {
										Utilities.showPopUp(AcPutAwayDetails.this, null, 
												getResources().getString(R.string.QTY_IS_NOT_GREATER_THAN_MAX_QTY));
										et_move_qty.setText(""+Double.parseDouble(String.valueOf(tvmaxQty.getText())));
										et_move_qty.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(
												enUom.get(0).getSignificantDigits())});
									} else {
										et_move_qty.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(
												itemNumber.get_significantDigits())});
									}
								}								
							}
							
							@Override
							public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
							}
							
							@Override
							public void afterTextChanged(Editable s) {
							}
						});
						
					} else {
						Utilities.showPopUp(AcPutAwayDetails.this, null, getResources().getString(R.string.LOADING_FAIL));
					}
					
				} else {
					Utilities.showPopUp(AcPutAwayDetails.this, null, getResources().getString(R.string.LOADING_FAIL));
				}
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
			dialog = new ProgressDialog(AcPutAwayDetails.this);
			dialog.setMessage(GlobalParams.CHECKING_DATA);
			dialog.show();
			dialog.setCancelable(false); 
			dialog.setCanceledOnTouchOutside(false);
		}
		
		@Override
		protected String doInBackground(Void... params) {
			String result;
			if (!isCancelled()) {
				try {
					NetParameter[] netParameter = new NetParameter[1];
					netParameter[0] = new NetParameter("barcode", barCode + edtLotValue.getEditableText().toString());
					data = HttpNetServices.Instance.getItemBarcode(netParameter);
					itemNumber = DataParser.getItemNumber(data);
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
					if (itemNumber != null && StringUtils.isNotBlank(itemNumber.get_LotNumber())) {					
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
						Utilities.showPopUp(AcPutAwayDetails.this, null,
								getLanguage(GlobalParams.BIN_MESSAGEBOXINVALIDLOCATIONSCAN,
										GlobalParams.INVALID_LOCATION_SCAN_PLEASE_SCAN_A_VALID_LOCATION));
						edtLotValue.setText(GlobalParams.BLANK_CHARACTER);						
					}
				} else {
					Utilities.showPopUp(AcPutAwayDetails.this, null,
							getLanguage(GlobalParams.BIN_MESSAGEBOXINVALIDLOCATIONSCAN,
									GlobalParams.INVALID_LOCATION_SCAN_PLEASE_SCAN_A_VALID_LOCATION));
					edtLotValue.setText(GlobalParams.BLANK_CHARACTER);				
				}
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
			dialog = new ProgressDialog(AcPutAwayDetails.this);
			dialog.setMessage(GlobalParams.CHECKING_DATA);
			dialog.show();
			dialog.setCancelable(false); 
			dialog.setCanceledOnTouchOutside(false);
		}
		
		@Override
		protected String doInBackground(Void... params) {
			String result;
			if (!isCancelled()) {
				try {
					NetParameter[] netParameter = new NetParameter[4];
					netParameter[0] = new NetParameter("itemNumber", barCode);
					netParameter[1] = new NetParameter("lotnumber", edtLotValue.getEditableText().toString());
					netParameter[2] = new NetParameter("uom", uom);
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
						tvmaxQty.setText(df.format(itemNumber.get_quantityOnHand()));
						et_move_qty.setText(String.valueOf(tvmaxQty.getText()));
						et_move_qty.setEnabled(true);
						et_move_qty.addTextChangedListener(new TextWatcher() {
							
							@Override
							public void onTextChanged(CharSequence s, int start, int before, int count) {
								if (s.toString().contains(GlobalParams.DOT) &&  s.length() == 1) {
									et_move_qty.setText(GlobalParams.BLANK_CHARACTER);
								} else {
									if (s.length() > 0 && Double.parseDouble(s.toString()) 
											> Double.parseDouble(String.valueOf(tvmaxQty.getText()))) {
										Utilities.showPopUp(AcPutAwayDetails.this, null, 
												getResources().getString(R.string.QTY_IS_NOT_GREATER_THAN_MAX_QTY));
										et_move_qty.setText(String.valueOf(tvmaxQty.getText()));
										et_move_qty.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(
												itemNumber.get_significantDigits())});
									} else {
										et_move_qty.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(
												itemNumber.get_significantDigits())});
									}									
								}								
							}
							
							@Override
							public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
							}
							
							@Override
							public void afterTextChanged(Editable s) {
							}
						});
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
						
						checkFirstUom = true;
					} else {
						Utilities.showPopUp(AcPutAwayDetails.this, null,
								getLanguage(GlobalParams.ERRORINVALIDLOTNUMFORITEM,	GlobalParams.INVALID_LOT));
						edt_move_from.setText(GlobalParams.BLANK_CHARACTER);						
					}
				} else {
					Utilities.showPopUp(AcPutAwayDetails.this, null,
							getLanguage(GlobalParams.ERRORINVALIDLOTNUMFORITEM,	GlobalParams.INVALID_LOT));
					edt_move_from.setText(GlobalParams.BLANK_CHARACTER);
				}
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
			dialog = new ProgressDialog(AcPutAwayDetails.this);
			dialog.setMessage(getLanguage(GlobalParams.LOADING_MSG, GlobalParams.LOADING_DATA));
			dialog.show();
			dialog.setCancelable(false); 
			dialog.setCanceledOnTouchOutside(false);
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
						if (enBarcodeExistences.getBinOnlyCount() != 0 || enBarcodeExistences.getLPCount() != 0) {
							Logger.error(data + "==========");
							try {
								prepareJsonForm();
							} catch (JSONException e) {		
								Logger.error(e);
							}
//							btnOK.setEnabled(true);
						} else {							
							Utilities.showPopUp(AcPutAwayDetails.this, null, GlobalParams.INVALID_SCAN);
						}
					
					} else {
						Utilities.showPopUp(AcPutAwayDetails.this, null,
								getLanguage(GlobalParams.BIN_MESSAGEBOXINVALIDLOCATIONSCAN,
										GlobalParams.INVALID_LOCATION_SCAN_PLEASE_SCAN_A_VALID_LOCATION));
					}
				} else {
					Utilities.showPopUp(AcPutAwayDetails.this, null,
							getLanguage(GlobalParams.BIN_MESSAGEBOXINVALIDLOCATIONSCAN,
									GlobalParams.INVALID_LOCATION_SCAN_PLEASE_SCAN_A_VALID_LOCATION));
				}
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
			dialog = new ProgressDialog(AcPutAwayDetails.this);
			dialog.setMessage(GlobalParams.PROCESS_DATA);
			dialog.show();
			dialog.setCancelable(false); 
			dialog.setCanceledOnTouchOutside(false);
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
			dialog.dismiss();
			// If not cancel by user
			if (!isCancelled()) {
				if (result.equals("true")) {				
					if (data.equalsIgnoreCase(GlobalParams.TRUE)) {
						finish();
						intent = new Intent(AcPutAwayDetails.this, AcPutAway.class);
						startActivity(intent);
					} else {
						Utilities.showPopUp(AcPutAwayDetails.this, null, getResources().getString(R.string.SUBMIT_FAILE));
					}
				} else {
					Utilities.showPopUp(AcPutAwayDetails.this, null, getResources().getString(R.string.SUBMIT_FAILE));
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
			dialog = new ProgressDialog(AcPutAwayDetails.this);
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
					NetParameter[] netParameter = new NetParameter[4];
					netParameter[0] = new NetParameter("itemNumber", barCode);
					netParameter[1] = new NetParameter("lotnumber", edtLotValue.getEditableText().toString());
					netParameter[2] = new NetParameter("uom", uom);
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
						tvmaxQty.setText(df.format(itemNumber.get_quantityOnHand()));
						et_move_qty.setText(GlobalParams.BLANK_CHARACTER);					
					} else {
						Utilities.showPopUp(AcPutAwayDetails.this, null, GlobalParams.NETWORK_ERROR);										
					}
				} else {
					Utilities.showPopUp(AcPutAwayDetails.this, null, GlobalParams.NETWORK_ERROR);					
				}
			}
		}
	}
	
	/**
	 * Set value to Object EnBinTransfer
	 * @throws JSONException
	 */
	private void prepareJsonForm() throws JSONException {	
		enBinTransfer = new EnBinTransfer();	
		enBinTransfer.setFromBinNumber(edt_move_from.getEditableText().toString());
		enBinTransfer.setIsLicensePlate(itemNumber.is_licensePlateInd());
		enBinTransfer.setItemNumber(tvTransfer.getText().toString());
		enBinTransfer.setLotNumber(edtLotValue.getEditableText().toString());		
		if (StringUtils.isNotBlank(et_move_qty.getEditableText().toString())) {
			enBinTransfer.setQuantity(Double.parseDouble(et_move_qty.getEditableText().toString()));
			btnOK.setEnabled(true);
		} else {
			et_move_qty.setText("0");
			enBinTransfer.setQuantity(0);
			btnOK.setEnabled(false);
		}
		enBinTransfer.setToBinNumber(et_move_to.getEditableText().toString());
		enBinTransfer.setTransactionType("Bin Transfer");
		enBinTransfer.setUomDescription(uom);
		listBinTransfer.add(enBinTransfer);
		binTransfer = DataParser.convertObjectToString(listBinTransfer);
		Logger.error(binTransfer);
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
