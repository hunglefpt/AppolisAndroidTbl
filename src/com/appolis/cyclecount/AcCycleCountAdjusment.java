/**
 * Name: $RCSfile: AcCycleCountAdjusment.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.cyclecount;

import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.appolis.adapter.CycleUomAdapter;
import com.appolis.androidtablet.R;
import com.appolis.common.CommonData;
import com.appolis.common.LanguagePreferences;
import com.appolis.entities.ObjectCountCycleCurrent;
import com.appolis.entities.ObjectCycleLocation;
import com.appolis.entities.ObjectCycleUom;
import com.appolis.entities.ObjectInstanceRealTimeBin;
import com.appolis.network.NetParameter;
import com.appolis.network.access.HttpNetServices;
import com.appolis.utilities.BuManagement;
import com.appolis.utilities.CommontDialog;
import com.appolis.utilities.DataParser;
import com.appolis.utilities.DecimalDigitsInputFilter;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.Logger;
import com.appolis.utilities.StringUtils;
import com.appolis.utilities.Utilities;

/**
 * Display Cycle count adjustment screen
 * @author hoangnh11
 */
public class AcCycleCountAdjusment extends Activity implements OnClickListener, OnItemSelectedListener, TextWatcher {
	
	private ObjectCountCycleCurrent objCurrentPre;
	private LinearLayout linLot;
	private LinearLayout linScan;
	private LinearLayout linBack;
	private TextView tvHeader;
	private TextView tvLoc;
	private TextView tvLocationLabel;
	private TextView tvCurrentLocaction;
	private TextView tvItem;
	private TextView tvBinPath;
	private TextView tvUOM;
	private EditText txtCycleAdjustmentLoc;
	private EditText txtCycleAdjustmentItem;
	private EditText txtCycleAdjustmentLot;
	private TextView tvCycleItemDes;
	private Spinner spUom;
	private EditText txtCycleAdjustmentQty;
	private TextView tvCycleItemCurrentCount;
	private TextView tvCycleItemChangedCount;
	private TextView tvQty;
	private TextView tvLot;
	private TextView tvCycleCurrentCount;
	private TextView tvCycleChangedCount;
	private Button btnCancel;
	private Button btnOk;
	
	private ArrayList<ObjectCycleUom> uomList;
	private CycleUomAdapter uomAdapter;
	
	private ObjectCycleUom objectCycleUomSelect;
	private ObjectInstanceRealTimeBin bin;
	
	private String path;
	private String spValuePre;
	private String fromUom;
	private String newBinNumber;
	private double changeCount;
	private double countConverted;
	private boolean isUpdateCycleCount;
	private boolean isBinPath;
	private boolean isExistInList;
	private boolean isActivityRunning = false;
	
	private LanguagePreferences languagePrefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isActivityRunning = true;
		setContentView(R.layout.ac_cycle_adjustment);
		Intent intent =  this.getIntent();
		objCurrentPre = (ObjectCountCycleCurrent) intent.getSerializableExtra("cycleItemCurrent");
		spValuePre = objCurrentPre.get_uomDescription().trim();
		fromUom = spValuePre;
		bin = (ObjectInstanceRealTimeBin) intent.getSerializableExtra("BinCycleCount");
		isBinPath = intent.getBooleanExtra("isLp", false);
		isUpdateCycleCount = (boolean) intent.getBooleanExtra("isUpdateCycleCount", false);
		isExistInList = (boolean) intent.getBooleanExtra("isExistInList", false);
		newBinNumber = intent.getStringExtra("newBinNumber");
		countConverted = objCurrentPre.get_count();
		changeCount = objCurrentPre.get_count();
		path = intent.getStringExtra("path");
		languagePrefs = new LanguagePreferences(getApplicationContext());
		initLayout();
		setText();
		new LoadAjustmentAsyn(AcCycleCountAdjusment.this).execute();
	}

	/**
	 * instance layout
	 */
	private void initLayout() {
		tvHeader = (TextView) findViewById(R.id.tv_header);
		tvCurrentLocaction = (TextView) findViewById(R.id.tvCurrentLocaction);
		tvBinPath = (TextView) findViewById(R.id.tvBinPath);
		linLot = (LinearLayout) findViewById(R.id.linLot);
		tvLoc = (TextView) findViewById(R.id.tvLoc);
		linScan = (LinearLayout) findViewById(R.id.lin_buton_scan);
		linScan.setVisibility(View.GONE);
		linBack = (LinearLayout) findViewById(R.id.lin_buton_home);
		linBack.setVisibility(View.GONE);
		tvUOM = (TextView) findViewById(R.id.tvUOM);
		tvQty = (TextView) findViewById(R.id.tvQty);
		tvLot = (TextView) findViewById(R.id.tvLot);
		tvCycleChangedCount = (TextView) findViewById(R.id.tvCycleChangedCount);
		tvCycleCurrentCount = (TextView) findViewById(R.id.tvCycleCurrentCount);
		tvLocationLabel = (TextView) findViewById(R.id.tvLocationLabel);
		tvCycleItemDes = (TextView) findViewById(R.id.tvCycleItemDes);
		tvItem = (TextView) findViewById(R.id.tvItem);
		txtCycleAdjustmentItem = (EditText) findViewById(R.id.txtCycleAdjustmentItem);
		txtCycleAdjustmentLoc = (EditText) findViewById(R.id.txtCycleAdjustmentLoc);
		spUom = (Spinner) findViewById(R.id.spCycleItemUom);
		spUom.setOnItemSelectedListener(this);
		txtCycleAdjustmentQty = (EditText) findViewById(R.id.txtCycleAdjustmentQty);
		
		tvCycleItemCurrentCount = (TextView) findViewById(R.id.tvCycleItemCurrentCount); 
		tvCycleItemCurrentCount.setText(BuManagement.formatDecimal(objCurrentPre.get_count()) + " " + objCurrentPre.get_uomDescription());
		tvCycleItemChangedCount = (TextView) findViewById(R.id.tvCycleItemChangedCount);
		tvCycleItemChangedCount.setText(BuManagement.formatDecimal(objCurrentPre.get_count()) + " " + objCurrentPre.get_uomDescription());
		tvCurrentLocaction.setVisibility(View.VISIBLE);
		
		if(!isBinPath) {
			
			if(isUpdateCycleCount) {
				txtCycleAdjustmentLoc.setText(path);
			} else {
				txtCycleAdjustmentLoc.setText(newBinNumber);
			}
			tvCurrentLocaction.setText(path);
			tvCurrentLocaction.setTextColor(getResources().getColor(R.color.Orange4));
			tvCurrentLocaction.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
			tvBinPath.setVisibility(View.GONE);
		} else {
			String data[] = path.split(CommonData.PATH);
			ObjectCycleLocation location = getCycleLocation(data);
			tvCurrentLocaction.setText(location.getPath() + " ");
			tvBinPath.setText(location.getSubPath());
			tvBinPath.setTextColor(getResources().getColor(R.color.Orange4));
			tvBinPath.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
			tvBinPath.setVisibility(View.VISIBLE);
			txtCycleAdjustmentLoc.setText(location.getSubPath());
			
			if(isUpdateCycleCount) {
				txtCycleAdjustmentLoc.setText(location.getSubPath());
			} else {
				txtCycleAdjustmentLoc.setText(newBinNumber);
			}
		}
		
		txtCycleAdjustmentItem.setText(objCurrentPre.get_itemNumber());
		tvCycleItemDes.setText(objCurrentPre.get_itemDesc());
		txtCycleAdjustmentLot = (EditText) findViewById(R.id.txtCycleAdjustmentLot);
		btnCancel = (Button) findViewById(R.id.btnCycleAdjCancel);
		btnOk = (Button) findViewById(R.id.btnCycleAdjOk);
		btnCancel.setOnClickListener(this);
		btnOk.setOnClickListener(this);
		txtCycleAdjustmentQty.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				if(arg0.toString().contains(GlobalParams.DOT) && arg0.length() == 1) {
					txtCycleAdjustmentQty.setText(GlobalParams.BLANK);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				double qty = 0;
				try {
					String qtyText = txtCycleAdjustmentQty.getText().toString().trim();
					if(StringUtils.isNotBlank(qtyText)) {
						qty = Double.parseDouble(qtyText);
					}
					
					changeCount = countConverted + qty;
			        
			        tvCycleItemChangedCount.setText(BuManagement.formatDecimal(changeCount) + " " + objectCycleUomSelect.getUomDescription());
				} catch (NumberFormatException e) {
					Logger.error(e.getCause().getMessage());
				}
				
				if(checkVisibleButtonOk()) {
					btnOk.setEnabled(true);
				} else {
					btnOk.setEnabled(false);
				}
			}
		});
		txtCycleAdjustmentLot.addTextChangedListener(this);
		
		if(objCurrentPre.is_lotTrackingInd() && StringUtils.isNotBlank(objCurrentPre.get_lotNumber())) {
			linLot.setVisibility(View.VISIBLE);
			txtCycleAdjustmentLot.setText(objCurrentPre.get_lotNumber());
			
			if(isUpdateCycleCount) {
				txtCycleAdjustmentLot.setEnabled(false);
			}
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
		String uom = languagePrefs.getPreferencesString(GlobalParams.UNIT_OF_MEASURE_KEY, GlobalParams.UNIT_OF_MEASURE_VALUE) + ":";
		tvUOM.setText(uom);
		String lot = languagePrefs.getPreferencesString(GlobalParams.REST_GRD_LOT_KEY, GlobalParams.REST_GRD_LOT_VALUE) + ":";
		tvLot.setText(lot);
		String qty = languagePrefs.getPreferencesString(GlobalParams.PID_GRD_QTY_VALUE, GlobalParams.PID_GRD_QTY_VALUE) + ":";
		tvQty.setText(qty);
		String currentCount = languagePrefs.getPreferencesString(GlobalParams.CYCLE_CURRENT_COUNT_KEY, GlobalParams.CYCLE_CURRENT_COUNT_VALUE) + ":";
		tvCycleCurrentCount.setText(currentCount);
		String changedCount = languagePrefs.getPreferencesString(GlobalParams.CYCLE_CHANGE_COUNT_KEY, GlobalParams.CYCLE_CHANGE_COUNT_VALUE) + ":";
		tvCycleChangedCount.setText(changedCount);
		String labelLocation = languagePrefs.getPreferencesString(GlobalParams.CURRENT_LOCATION_KEY, GlobalParams.CURRENT_LOCATION_VALUE) + ":";
		tvLocationLabel.setText(labelLocation);
	}
	
	/**
	 * Process data Adjustment from api
	 * @author hoangnh11
	 */
	private class LoadAjustmentAsyn extends AsyncTask<Void, Void, Integer> {
		Context context;
		ProgressDialog progressDialog;
		String data;

		public LoadAjustmentAsyn(Context mContext) {
			this.context = mContext;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (!isCancelled() && isActivityRunning) {
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
					netParameters[0] = new NetParameter("itemNumber", URLEncoder.encode(objCurrentPre.get_itemNumber().trim(), GlobalParams.UTF_8));
					data = HttpNetServices.Instance.getCycleAdjustment(netParameters);
					uomList =  DataParser.getCycleUomList(data);
				} catch (Exception e) {
					result = 1;
				}
			}
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);

			if (null != progressDialog && (progressDialog.isShowing()) && isActivityRunning) {
				progressDialog.dismiss();
			}
			
			if (!isCancelled()) {
				if (result == 0) {
					uomAdapter = new CycleUomAdapter(AcCycleCountAdjusment.this, android.R.layout.simple_spinner_dropdown_item, uomList);
					uomAdapter.notifyDataSetChanged();
					ObjectCycleUom selectUom = new ObjectCycleUom();
					selectUom.setUomDescription(objCurrentPre.get_uomDescription());
					int selection = uomAdapter.getPosition(selectUom); 
					spUom.setAdapter(uomAdapter);
					spUom.setSelection(selection);
					selectUom = uomAdapter.getItem(selection);
					txtCycleAdjustmentQty.setFilters(new InputFilter[]
												{new DecimalDigitsInputFilter(selectUom.getSignificantDigits()),
												new InputFilter.LengthFilter(14)});
					
					if(checkVisibleButtonOk()) {
						btnOk.setEnabled(true);
					} else {
						btnOk.setEnabled(false);
					}
				} else {
					String msgs = languagePrefs.getPreferencesString("error",
							"error");
					CommontDialog.showErrorDialog(context, msgs, null);
					Log.e("Appolis", "ProcessScanDataLotAsyn #onPostExecute: "
							+ result);
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
	 * Check visible button OK
	 * @return
	 */
	private boolean checkVisibleButtonOk() {
		String quanity = txtCycleAdjustmentQty.getText().toString();
		if (StringUtils.isNotBlank(quanity)) {
			return true;
		}

		return false;
	}
	
	/**
	 * Process event for views
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnCycleAdjCancel:
			Utilities.hideKeyboard(this);
			setResult(RESULT_CANCELED);
			this.finish();
			break;
		case R.id.btnCycleAdjOk:
			onClickOklistener();
			break;
		default:
			break;
		}
	}

	/**
	 * Process select item for list view
	 */
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		objectCycleUomSelect = uomList.get(arg2);
		
		if(!spValuePre.equalsIgnoreCase(objectCycleUomSelect.getUomDescription().trim())) {
			fromUom = spValuePre;
			spValuePre = objectCycleUomSelect.getUomDescription();
			new LoadAjustmentQuanityAsyn(AcCycleCountAdjusment.this).execute();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
	
	//change UOM 
	private class LoadAjustmentQuanityAsyn extends AsyncTask<Void, Void, Integer> {

		Context context;
		ProgressDialog progressDialog;
		String data;

		public LoadAjustmentQuanityAsyn(Context mContext) {
			this.context = mContext;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (!isCancelled() && isActivityRunning) {
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
					NetParameter[] netParameters = new NetParameter[4];
					netParameters[0] = new NetParameter("itemNumber", URLEncoder.encode(objCurrentPre.get_itemNumber().trim(), GlobalParams.UTF_8));
					netParameters[1] = new NetParameter("toUomDesc", objectCycleUomSelect.getUomDescription());
					//Type of UOM Previous
					netParameters[2] = new NetParameter("fromUomDesc", fromUom);
					netParameters[3] = new NetParameter("quantity", String.valueOf(countConverted));
					data = HttpNetServices.Instance.getCycleQuanity(netParameters);
				} catch (Exception e) {
					result = 1;
				}
			}
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);

			if (null != progressDialog && (progressDialog.isShowing()) && isActivityRunning) {
				progressDialog.dismiss();
			}
			
			if (!isCancelled()) {
				if (result == 0) {
					countConverted = Float.parseFloat(data);
					
					tvCycleItemCurrentCount.setText(BuManagement.formatDecimal(countConverted) + " " + objectCycleUomSelect.getUomDescription());
					String qtyText = txtCycleAdjustmentQty.getText().toString().trim();
					float qty = 0;
					
					if(StringUtils.isNotBlank(qtyText)) {
						qty = Float.parseFloat(qtyText);
					}
					changeCount = qty + countConverted;
					
					tvCycleItemChangedCount.setText(BuManagement.formatDecimal(changeCount) + " " + objectCycleUomSelect.getUomDescription());
					txtCycleAdjustmentQty.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(objectCycleUomSelect.getSignificantDigits())});
				} else {
					String msgs = languagePrefs.getPreferencesString("error",
							"error");
					CommontDialog.showErrorDialog(context, msgs, null);
					Log.e("Appolis", "LoadAjustmentQuanityAsyn #onPostExecute: "
							+ result);
				}
			} else {
				String msgs = languagePrefs.getPreferencesString("error",
						"error");
				CommontDialog.showErrorDialog(context, msgs, null);
				Log.e("Appolis", "LoadAjustmentQuanityAsyn #onPostExecute: "
						+ result);
			}
		}
	}
	
	/**
	 * Process click event for button OK
	 */
	private void onClickOklistener() {
		new LoadTrackAsyn(AcCycleCountAdjusment.this).execute();
	}

	@Override
	public void afterTextChanged(Editable s) {
		
		if(checkVisibleButtonOk()) {
			btnOk.setEnabled(true);
		} else {
			btnOk.setEnabled(false);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
	
	//Update data when click ok
	private class LoadTrackAsyn extends AsyncTask<Void, Void, Integer> {

		Context context;
		ProgressDialog progressDialog;
		String data = "";
		NetParameter []netParameters;
		
		public LoadTrackAsyn(Context mContext) {
			this.context = mContext;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (!isCancelled() && isActivityRunning) {
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
					if(!objCurrentPre.is_lpInd()) {
						// if update Item
						 if(isUpdateCycleCount) {
							 netParameters = new NetParameter[4];
							 netParameters[0] = new NetParameter("cycleCountItemInstanceID", String.valueOf(objCurrentPre.get_cycleCountItemInstanceID()));
						 } 
						 else {
							 String locationPath[] = path.split("/");
							 if(locationPath.length > 0) {
								 if(locationPath[locationPath.length - 1].equalsIgnoreCase(newBinNumber)) {
									 if(!isExistInList) {
										 netParameters = new NetParameter[4];
										 netParameters[0] = new NetParameter("cycleCountItemInstanceID", String.valueOf(objCurrentPre.get_cycleCountItemInstanceID()));
									 } else {
										 netParameters = new NetParameter[9];
										 netParameters[0] = new NetParameter("cycleCountInstanceID", String.valueOf(AcCycleCount.cycleCountInstanceID));
									 }
								 } else {
									 netParameters = new NetParameter[9];
									 netParameters[0] = new NetParameter("cycleCountInstanceID", String.valueOf(objCurrentPre.get_cycleCountInstanceID()));
								 }
							 } else {
								 netParameters = new NetParameter[9];
								 netParameters[0] = new NetParameter("cycleCountInstanceID", String.valueOf(objCurrentPre.get_cycleCountInstanceID()));
							 }
							 
						 }
					}
					
					netParameters[1] = new NetParameter("count", BuManagement.formatDecimal(changeCount));
					netParameters[2] = new NetParameter("uomDesc", objectCycleUomSelect.getUomDescription());
					netParameters[3] = new NetParameter("itemNumber", URLEncoder.encode(objCurrentPre.get_itemNumber().trim(), GlobalParams.UTF_8));
					
					if(netParameters.length > 4) {
						netParameters[4] = new NetParameter("lotNumber", URLEncoder.encode(objCurrentPre.get_lotNumber().trim(), GlobalParams.UTF_8));
						if(bin != null) {
							//netParameters[5] = new NetParameter("binNumber", bin.get_binNumber());
							netParameters[5] = new NetParameter("binNumber", URLEncoder.encode(newBinNumber.trim(), GlobalParams.UTF_8));
						}
						
						netParameters[6] = new NetParameter("expirationDate", "01/01/1900");
						netParameters[7] = new NetParameter("gtinNumber", "");
						netParameters[8] = new NetParameter("originalGs1Barcode", "");
					}
					if(isUpdateCycleCount) { 
						data = HttpNetServices.Instance.updateCycleCountItem(netParameters);
					} else {
						if(AcCycleCount.isPhysicalInventoryCycleCount) {
							data = HttpNetServices.Instance.updateCycleCountItem(netParameters);
						}
					}
				} catch (Exception e) {
					result = 1;
					e.printStackTrace();
				}
			}
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);

			if (null != progressDialog && (progressDialog.isShowing()) && isActivityRunning) {
				progressDialog.dismiss();
			}
			
			if (!isCancelled()) {
				if (result == 0) {
					//Back to previous activity
					if(netParameters.length > 4) {
						String dataMessage = Utilities.getDataBarcode(data);
						
						if(dataMessage.equalsIgnoreCase("success")) {
							setResult(RESULT_OK);
							AcCycleCountAdjusment.this.finish();
						} else {
							Log.e("Appolis", "LoadReceiveListAsyn #onPostExecute: "
									+ dataMessage);
							//Utilities.dialogShow(dataMessage, AcCycleCountAdjusment.this);
							setResult(RESULT_OK);
							AcCycleCountAdjusment.this.finish();
						}
					} else {
						setResult(RESULT_OK);
						AcCycleCountAdjusment.this.finish();
					}
					
				} else {
					/*String msgs = languagePrefs.getPreferencesString("error",
							"error");
					CommontDialog.showErrorDialog(context, msgs, null);*/
					Log.e("Appolis", "LoadReceiveListAsyn #onPostExecute: "
							+ result);
					setResult(RESULT_OK);
					AcCycleCountAdjusment.this.finish();
				}
			} else {
				/*String msgs = languagePrefs.getPreferencesString("error",
						"error");
				CommontDialog.showErrorDialog(context, msgs, null);*/
				Log.e("Appolis", "LoadReceiveListAsyn #onPostExecute: "
						+ result);
				setResult(RESULT_OK);
				AcCycleCountAdjusment.this.finish();
			}
		}
	} 
	
	//get url Location
	private ObjectCycleLocation getCycleLocation(String data[]) {
		ObjectCycleLocation location = new ObjectCycleLocation();
		String path = "";
		String subPath = "";
		
		for(int i = 0; i < data.length; i++) {
			if(i < data.length - 1) {
				path = path + data[i] + CommonData.PATH;
			} else {
				subPath = data[i];
			}
		}
		
		location.setPath(path);
		location.setSubPath(subPath);
		return location;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		isActivityRunning = false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isActivityRunning = true;
	}
	
	
}
