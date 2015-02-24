/**
 * Name: $RCSfile: AcCycleCountBinPath.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.cyclecount;

import java.net.URLEncoder;
import java.util.ArrayList;
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
import android.util.TypedValue;
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

import com.appolis.adapter.CycleCountCurrentAdapter;
import com.appolis.androidtablet.R;
import com.appolis.common.CommonData;
import com.appolis.common.LanguagePreferences;
import com.appolis.entities.EnBarcodeExistences;
import com.appolis.entities.ObjectBinList;
import com.appolis.entities.ObjectCountCycleCurrent;
import com.appolis.entities.ObjectCycleCount;
import com.appolis.entities.ObjectCycleItem;
import com.appolis.entities.ObjectCycleLp;
import com.appolis.entities.ObjectInstanceRealTimeBin;
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
 * Display Cycle Count Bin path screen
 * @author hoangnh11
 */
public class AcCycleCountBinPath extends Activity implements OnClickListener,
		OnItemClickListener {

	private LinearLayout linBack;
	private LinearLayout linScan;
	private LinearLayout linSearch;
	private TextView tvHeader;
	private EditText txtCycleSearch;
	private ImageView imgClearTextSearch;
	private ImageView imgSearch;
	private ImageView imgSeachIcon;
	private ListView lvCycleCountList;
	private LinearLayout lnCyCleCurrentLocation;
	private LinearLayout lnCycleLocationButton;
	private TextView tvCurrentLocaction;
	private TextView tvBinPath;
	private Button btnOption;
	private Button btnBack;
	
	//dialog
	//dialog
	private Dialog dialogOptions;
	private TextView tvDialogCancel;
	private TextView tvDialogOptions;
	private LinearLayout linLayoutPrint;
	private LinearLayout linLayoutAdjustment;
	private LinearLayout linLayoutZeroCount;
	
	private ImageView imgPrintDialog;
	private ImageView imgAdjustmentDialog;
	private ImageView imgZeroCountDialog;
	
	private TextView tvDialogPrint;
	private TextView tvDialogAdjusment;
	private TextView tvDialogZeroCount;

	private CycleCountCurrentAdapter cycleCountCurrentAdapter;
	// data variable
	private ObjectCountCycleCurrent binPrev;
	private ObjectInstanceRealTimeBin bin;
	private ObjectCycleCount objectCycleCount;
	private String isFisrtCall = "true";
	private List<ObjectCountCycleCurrent> binList;
	private LoadCycleCountBinPathListAsyn loadBinPathAsyn;

	private String path;
	private ObjectCycleLp objectCycleLp;
	// used to save data after scan Item and be used to filter by lot
	private List<ObjectCountCycleCurrent> countCycleCurrents;
	
	//Select in listview
	private View currentSelectedView;
	private ObjectCountCycleCurrent objectCurrentPreSelected;
	private ObjectCountCycleCurrent objectCurrentSelected;
	
	private boolean isConnectSocket;
	private int typeScan;
	
	private LanguagePreferences languagePrefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_cycle_location);
		Intent intent = getIntent();
		path = intent.getStringExtra("path");
		bin = (ObjectInstanceRealTimeBin) intent.getSerializableExtra("BinCycleCount");
		binPrev = (ObjectCountCycleCurrent) intent
				.getSerializableExtra("cycleItemCurrent");
		objectCycleCount = (ObjectCycleCount) intent.getSerializableExtra("objectCycleCount");
		languagePrefs = new LanguagePreferences(getApplicationContext());
		initLayout();
		setText();
		loadBinPathAsyn = new LoadCycleCountBinPathListAsyn(
				AcCycleCountBinPath.this);
		loadBinPathAsyn.execute();
	}

	/**
	 * instance layout
	 */
	private void initLayout() {
		linBack = (LinearLayout) findViewById(R.id.lin_buton_home);
		linBack.setVisibility(View.GONE);
		linBack.setOnClickListener(this);
		linScan = (LinearLayout) findViewById(R.id.lin_buton_scan);
		linScan.setOnClickListener(this);
		linSearch = (LinearLayout) findViewById(R.id.lin_search);
		tvHeader = (TextView) findViewById(R.id.tv_header);
		tvBinPath = (TextView) findViewById(R.id.tvBinPath);
		String dafaultHeader = getResources().getString(
				R.string.cycle_count_default_header);
		String keyHeader = getResources().getString(
				R.string.cycle_count_key_header);
		tvHeader.setText(languagePrefs.getPreferencesString(keyHeader,
				dafaultHeader));

		txtCycleSearch = (EditText) findViewById(R.id.txt_cycle_search);
		imgClearTextSearch = (ImageView) findViewById(R.id.img_clear_cycle_search);
		imgSearch = (ImageView) findViewById(R.id.imgSearch);
		imgClearTextSearch.setVisibility(View.GONE);
		imgClearTextSearch.setOnClickListener(this);
		lvCycleCountList = (ListView) findViewById(R.id.lvCycleCountList);
		lvCycleCountList.setOnItemClickListener(this);
		imgSeachIcon = (ImageView) findViewById(R.id.imgSearchIcon);
		imgSeachIcon.setOnClickListener(this);
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
				if (text.length() > 0) {
					imgClearTextSearch.setVisibility(View.VISIBLE);
					imgSearch.setVisibility(View.INVISIBLE);
				}

				if (null != cycleCountCurrentAdapter) {
					cycleCountCurrentAdapter.filter(text);
				}
			}
		});

		// Init for Cycle Current Location
		lnCyCleCurrentLocation = (LinearLayout) findViewById(R.id.lnCyCleCurrentLocation);
		lnCyCleCurrentLocation.setVisibility(View.VISIBLE);
		lnCycleLocationButton = (LinearLayout) findViewById(R.id.lnCycleLocationButton);
		lnCycleLocationButton.setVisibility(View.VISIBLE);
		tvCurrentLocaction = (TextView) findViewById(R.id.tvCurrentLocaction);
		btnOption = (Button) findViewById(R.id.btnOption);
		btnOption.setOnClickListener(this);
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		// setData

		tvCurrentLocaction
				.setTextColor(getResources().getColor(R.color.Gray91));
		tvCurrentLocaction.setText(path + CommonData.PATH);
		tvCurrentLocaction.setVisibility(View.VISIBLE);
		tvCurrentLocaction.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

		tvBinPath.setTextColor(getResources().getColor(R.color.Orange4));
		tvBinPath.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
		tvBinPath.setText(binPrev.get_itemNumber());
		tvBinPath.setVisibility(View.VISIBLE);
		
		//setup option dialog
		dialogOptions = CommontDialog.createDialogNoTitleCenter(this, R.layout.common_layout_dialog_option_center);
		tvDialogCancel = (TextView) dialogOptions.findViewById(R.id.tvDialogCancel);
		tvDialogCancel.setOnClickListener(this);
		tvDialogOptions = (TextView) dialogOptions.findViewById(R.id.tvOptions);
		linLayoutPrint = (LinearLayout) dialogOptions.findViewById(R.id.lin_layout_print);
		imgPrintDialog = (ImageView) dialogOptions.findViewById(R.id.img_print_labels);
		tvDialogPrint = (TextView) dialogOptions.findViewById(R.id.tv_cycle_print_option);
		linLayoutPrint.setOnClickListener(this);
		linLayoutAdjustment = (LinearLayout) dialogOptions.findViewById(R.id.lin_layout_Cycle_Adjustment);
		imgAdjustmentDialog = (ImageView) dialogOptions.findViewById(R.id.img_adjustment_labels);
		tvDialogAdjusment = (TextView) dialogOptions.findViewById(R.id.tv_cycle_adjustment_option);
		linLayoutZeroCount = (LinearLayout) dialogOptions.findViewById(R.id.lin_layout_cycle_zero);
		imgZeroCountDialog = (ImageView) dialogOptions.findViewById(R.id.img_zero_count_labels);
		tvDialogZeroCount = (TextView) dialogOptions.findViewById(R.id.tv_cycle_zero_option);
	}
	
	/**
	 * Set text for header, buttons and views
	 */
	private void setText() {
		tvHeader.setText(languagePrefs.getPreferencesString(GlobalParams.CYCLE_COUNT_KEY, GlobalParams.CYCLE_COUNT_VALUE));
		//tvCurrentLocaction.setText(languagePrefs.getPreferencesString(GlobalParams.SELECT, GlobalParams.SELECT));
		txtCycleSearch.setHint(languagePrefs.getPreferencesString(GlobalParams.SCAN_SELECTLOC_SEARCH_KEY, GlobalParams.SCAN_SELECTLOC_SEARCH_VALUE));
		btnOption.setText(languagePrefs.getPreferencesString(GlobalParams.PID_BTN_OPTIONS, GlobalParams.OPTIONS));
		btnBack.setText(languagePrefs.getPreferencesString(GlobalParams.SETTINGS_BTN_CANCEL, GlobalParams.CANCEL));
		tvDialogOptions.setText(languagePrefs.getPreferencesString(GlobalParams.PID_BTN_OPTIONS, GlobalParams.OPTIONS));
		tvDialogCancel.setText(languagePrefs.getPreferencesString(GlobalParams.SETTINGS_BTN_CANCEL, GlobalParams.CANCEL));
		tvDialogZeroCount.setText(languagePrefs.getPreferencesString(GlobalParams.CYCLE_ZERO_KEY, GlobalParams.CYCLE_ZERO_VALUE));
		tvDialogAdjusment.setText(languagePrefs.getPreferencesString(GlobalParams.CYCLE_ADJUSTMENT_KEY, GlobalParams.CYCLE_ADJUSTMENT_VALUE));
		tvDialogPrint.setText(languagePrefs.getPreferencesString(GlobalParams.PRINTLABELS_KEY, GlobalParams.PRINTLABELS_VALUE));
	}
	
	/**
	 * Process data cycle count bin path from api
	 * @author hoangnh11
	 */
	private class LoadCycleCountBinPathListAsyn extends
			AsyncTask<Void, Void, Integer> {
		Context context;
		ProgressDialog progressDialog;
		String data;

		public LoadCycleCountBinPathListAsyn(Context mContext) {
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
		protected Integer doInBackground(Void... arg0) {
			Integer result = 0;
			if (!isCancelled()) {
				try {
					NetParameter[] netParameter = new NetParameter[3];
					netParameter[0] = new NetParameter("cyclecountInstanceID",
							String.valueOf(binPrev.get_cycleCountInstanceID()));
					netParameter[1] = new NetParameter("binNumber", URLEncoder.encode(binPrev.get_itemNumber().trim(), GlobalParams.UTF_8));
					netParameter[2] = new NetParameter("isFirstCall",
							isFisrtCall);
					data = HttpNetServices.Instance
							.getCycleCountCurrentList(netParameter);
					binList = DataParser.getCycleCountCurrentList(data);
					// initTest();
					//can check IOS de xem back ra ngoai va goi isFirstCall man hinh truoc la false;
					if (null == binList || binList.size() == 0) {
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
			if (null != progressDialog && (progressDialog.isShowing())) {
				progressDialog.dismiss();
			}

			String errorMss = languagePrefs.getPreferencesString(GlobalParams.NO_MORE_COUNTS_KEY, GlobalParams.NO_MORE_COUNTS_VALUE);

			if (!isCancelled()) {
				if (result == 0) {
					// if Adapter within Current Location
					//25-01 -2015 :: check lai IOS
					if(isFisrtCall.equals("true")) {
						cycleCountCurrentAdapter = new CycleCountCurrentAdapter(
								getApplicationContext(), binList, bin, objectCycleCount, path, binPrev, AcCycleCountBinPath.this);
						lvCycleCountList.setAdapter(cycleCountCurrentAdapter);
						cycleCountCurrentAdapter.notifyDataSetChanged();
						objectCurrentSelected = binList.get(0);
					} else {
						setResult(RESULT_OK);
						AcCycleCountBinPath.this.finish();
					}
				} else if (result == 1) {
					Intent i = new Intent(AcCycleCountBinPath.this,
							AcCycleCount.class);
					startActivity(i);
					finish();

				} else {
					Utilities.dialogShow(errorMss, AcCycleCountBinPath.this);
				}
			} else {
				Utilities.dialogShow(errorMss, AcCycleCountBinPath.this);
			}
		}
	}

	/**
	 * Process click event
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		ObjectCountCycleCurrent currentSelected = binList.get(arg2);
		objectCurrentSelected = currentSelected;
		configDialogOption();
		
		if (currentSelectedView != null && currentSelectedView != v) {
	        unhighlightCurrentRow(currentSelectedView);
	    }
		objectCurrentPreSelected = currentSelected;
		currentSelectedView = v;
	    highlightCurrentRow(currentSelectedView);
	}

	/**
	 * 
	 * @param currentSelected
	 */
	private void startActivityBinPath(ObjectCountCycleCurrent currentSelected) {
		String loc = path + CommonData.PATH + binPrev.get_itemNumber();
		Intent intentLocation = new Intent(AcCycleCountBinPath.this,
				AcCycleCountBinPath.class);
		intentLocation.putExtra("path", loc);
		intentLocation.putExtra("BinCycleCount", bin);
		intentLocation.putExtra("objectCycleCount", objectCycleCount);
		intentLocation.putExtra("cycleItemCurrent", currentSelected);
		startActivityForResult(intentLocation,
				GlobalParams.AC_CYCLE_COUNT_BINPATH_ACTIVITY);
	}

	/**
	 * 
	 * @param currentSelected
	 */
	private void startActivityCycleAdjustment(ObjectCountCycleCurrent currentSelected) {
		String loc = path + CommonData.PATH + binPrev.get_itemNumber();
		Intent intentCycleAdjustment = new Intent(AcCycleCountBinPath.this,
				AcCycleCountAdjusment.class);
		intentCycleAdjustment.putExtra("cycleItemCurrent", currentSelected);
		intentCycleAdjustment.putExtra("path", loc);
		intentCycleAdjustment.putExtra("isUpdateCycleCount", true);
		intentCycleAdjustment.putExtra("isLp", true);
		startActivityForResult(intentCycleAdjustment,
				GlobalParams.AC_CYCLE_COUNT_ADJUSMENT_ACTIVITY);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case GlobalParams.AC_CYCLE_COUNT_ADJUSMENT_ACTIVITY:
			
			if (RESULT_OK == resultCode || resultCode == RESULT_CANCELED) {
				//isFisrtCall = "false";
				new LoadCycleCountBinPathListAsyn(AcCycleCountBinPath.this)
						.execute();
			}
			
			break;
			
		case GlobalParams.CAPTURE_BARCODE_CAMERA_ACTIVITY:
			
			if (resultCode == RESULT_OK) {
				String message = data.getStringExtra(GlobalParams.RESULTSCAN);
				Log.e("Appolis", "CAPTURE_BARCODE_CAMERA_ACTIVITY #RESULT_OK:"
						+ message);
				processScanData(message);
			}
			
			break;
			
		case GlobalParams.CAPTURE_BARCODE_CAMERA_ACTIVITY_WITH_LOT:
			
			if (resultCode == RESULT_OK) {
				String message = data.getStringExtra(GlobalParams.RESULTSCAN);
				Log.e("Appolis", "CAPTURE_BARCODE_CAMERA_ACTIVITY #RESULT_OK:"
						+ message);
				processScanDataWithLot(message);
			}
			
			break;
		
		default:
			break;
		}
	}
	
	/**
	 * Config dialog
	 */
	private void configDialogOption() {
		//by document 1.0.3 page 11
		if(objectCycleCount.get_cycleCountTypeID() == 3) {
			imgAdjustmentDialog.setImageResource(R.drawable.ic_cycle_count_adjustment_pressed);
			linLayoutAdjustment.setOnClickListener(this);
		} 
		
		if(objectCurrentSelected != null) {
			if(!CommonData.COMPLETED.equalsIgnoreCase(objectCurrentSelected.get_itemStatus().trim())) {
				imgZeroCountDialog.setImageResource(R.drawable.ic_zero_count_pressed);
				linLayoutZeroCount.setOnClickListener(this);
			} else {
				imgZeroCountDialog.setImageResource(R.drawable.ic_zero_count);
				linLayoutZeroCount.setOnClickListener(null);
			}
		}
	}
	
	/**
	 * process scan data with Lot
	 */
	private void processScanDataWithLot(String barcode) {
		ProcessScanDataWithLotAsyn mLoadDataTask = new ProcessScanDataWithLotAsyn(
				AcCycleCountBinPath.this, barcode);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mLoadDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			mLoadDataTask.execute();
		}
	}
	
	/**
	 * 
	 * handle data after scan Lot
	 * 
	 * */
	private class ProcessScanDataWithLotAsyn extends
			AsyncTask<Void, Void, Integer> {
		Context context;
		ProgressDialog progressDialog;
		String response;
		String barcode;

		public ProcessScanDataWithLotAsyn(Context mContext, String barcode) {
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
							"LoadReceiveDetailAsyn #getBarcode #response:"
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
						} else {
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
				case 0: // if it is Lot
					//find single Item after second scan by lot
					ObjectCountCycleCurrent countCycleCurrent = getBarcodeInListFromLot();
					startActivityCycleAdjustment(countCycleCurrent);
					break;

				case 3:// error scan
					String messUnSupported = languagePrefs.getPreferencesString(GlobalParams.SCAN_BARCODE_UNSUPPORTED_VALUE,
							GlobalParams.SCAN_BARCODE_UNSUPPORTED_KEY);
					Utilities.showPopUp(context, null, messUnSupported);
					break;

				case 4:// Unsupported barcode
					String messLot = languagePrefs.getPreferencesString(GlobalParams.USER_SCAN_LOTNUMBER_KEY,
							GlobalParams.USER_SCAN_LOTNUMBER_VALUE);
					Utilities.showPopUp(context, null, messLot);
					break;

				case 1: // no network
					String msg = languagePrefs.getPreferencesString(
							GlobalParams.ERRORUNABLETOCONTACTSERVER,
							GlobalParams.ERROR_INVALID_NETWORK);
					Utilities.showPopUp(context, null, msg);
					break;

				default:
					String msgs = languagePrefs.getPreferencesString("error",
							"error");
					CommontDialog.showErrorDialog(context, msgs, null);
					Log.e("Appolis", "ProcessScanDataWithLotAsyn #onPostExecute: "
							+ result);
					break;
				}
			}
		}

		// get Object from List after second scan by lot
		private ObjectCountCycleCurrent getBarcodeInListFromLot() {
			for (ObjectCountCycleCurrent objectCountCycleCurrent : countCycleCurrents) {
				if (objectCountCycleCurrent.get_lotNumber().equalsIgnoreCase(
						barcode))
					return objectCountCycleCurrent;
			}

			return null;
		}
	}
	
	/**
	 * process scan data
	 */
	private void processScanData(String barcode) {
		ProcessScanDataAsyn mLoadDataTask = new ProcessScanDataAsyn(AcCycleCountBinPath.this, barcode);

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
			if(!isCancelled()){
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
				progressDialog.setCancelable(true);
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
						} else if (enBarcodeExistences.getPoCount() != 0 ||
								   enBarcodeExistences.getOrderCount() != 0 ||
								   enBarcodeExistences.getLotOnlyCount() != 0 ||
								   enBarcodeExistences.getBinOnlyCount() != 0) {
							errorCode = 5;
						} else if(enBarcodeExistences.getLPCount() != 0) {
							errorCode = 0;
						} else if(enBarcodeExistences.getItemOnlyCount() != 0 ||
								  enBarcodeExistences.getUOMBarcodeCount() != 0 ||
								  enBarcodeExistences.getItemIdentificationCount() != 0) {
								  errorCode = 6;	
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
			if(null != progressDialog && (progressDialog.isShowing())){
				progressDialog.dismiss();
			}
			
			String []barcodes = new String[1];
			barcodes[0] = barcode;
			if(!isCancelled()){
				if(checkBarcodeInList()) {
					switch (result) {
					case 0: //if LP
						new LoadLPAsyn(AcCycleCountBinPath.this).execute(barcodes);
						break;
						
					case 6:	 //If Item Number, UOM or ItemIdentification
						new LoadItemAsyn(AcCycleCountBinPath.this).execute(barcodes);
						break;
						
					case 3:// error scan
						String messUnSupported = languagePrefs.getPreferencesString(GlobalParams.SCAN_BARCODE_UNSUPPORTED_VALUE,
								GlobalParams.SCAN_BARCODE_UNSUPPORTED_KEY);
						Utilities.showPopUp(context, null, messUnSupported);
						break;
					
					case 4:// Unsupported barcode 
						String messNotLPOrItem = languagePrefs.getPreferencesString(GlobalParams.JOBPART_VALIDATEITEMORLP_KEY,
								GlobalParams.JOBPART_VALIDATEITEMORLP_VALUE);
						Utilities.showPopUp(context, null, messNotLPOrItem);
						break;
						
					case 1: //no network
						String msg = languagePrefs.getPreferencesString(GlobalParams.ERRORUNABLETOCONTACTSERVER, GlobalParams.ERROR_INVALID_NETWORK);
						CommontDialog.showErrorDialog(context, msg, null);
						break;
						
					case 5: //If call was ambiguous, PONumber, OrderNumber, LotOnly, BinOnly 
						String messNotLPOrItem1 = languagePrefs.getPreferencesString(GlobalParams.JOBPART_VALIDATEITEMORLP_KEY,
								GlobalParams.JOBPART_VALIDATEITEMORLP_VALUE);
							Utilities.showPopUp(context, null, messNotLPOrItem1);;
						break;	
						
					default:
						String msgs = languagePrefs.getPreferencesString("error", "error");
						CommontDialog.showErrorDialog(context, msgs, null);
						Log.e("Appolis", "LoadReceiveListAsyn #onPostExecute: " + result);
						break;
					}
				} else {
					String msg = languagePrefs.getPreferencesString(
							GlobalParams.MESSAGE_SCAN_LP_OR_ITEM_KEY,
							GlobalParams.MESSAGE_SCAN_LP_OR_ITEM_VALUE);
					Utilities.showPopUp(context, null, msg);
				}
			}
		}
		
		//check barcode is in list data ?
		private boolean checkBarcodeInList() {
			for(ObjectCountCycleCurrent objectCountCycleCurrent : binList) {
				if(objectCountCycleCurrent.get_itemNumber().equalsIgnoreCase(barcode)) 
					return true;
			}
			
			return false;
		}
	}
	
	
	/**
	 * LoadLPAsyn If LP is scanned to will call this class
	 **/
	private class LoadLPAsyn extends AsyncTask<String, Void, Integer> {
		Context context;
		ProgressDialog progressDialog;
		String data;

		public LoadLPAsyn(Context mContext) {
			this.context = mContext;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
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
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		@Override
		protected Integer doInBackground(String... barcode) {
			Integer result = 0;
			if (!isCancelled()) {
				try {
					NetParameter[] netParameter = new NetParameter[2];
					netParameter[0] = new NetParameter("barcode", URLEncoder.encode(barcode[0].trim(), GlobalParams.UTF_8));
					netParameter[1] = new NetParameter("isLp", "true");
					data = HttpNetServices.Instance
							.getLpByBarcode(netParameter);
					objectCycleLp = DataParser.getCycleLP(data);
				} catch (Exception e) {
					result = 2;
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

			if (!isCancelled()) {
				if (result == 0) {
					ObjectCountCycleCurrent currentSelected = getLPFromItemNumber();
					if (StringUtils.isNotBlank(currentSelected.get_itemNumber())) {
						startActivityBinPath(currentSelected);
					}

				} else if (result == 1) {
					setResult(RESULT_OK);
					AcCycleCountBinPath.this.finish();
				} else {
					String msgs = languagePrefs.getPreferencesString("error",
							"error");
					CommontDialog.showErrorDialog(context, msgs, null);
					Log.e("Appolis", "LoadLPAsyn #onPostExecute: " + result);
				}
			} else {
				String msgs = languagePrefs.getPreferencesString("error", "error");
				CommontDialog.showErrorDialog(context, msgs, null);
				Log.e("Appolis", "LoadLPAsyn #onPostExecute: " + result);
			}
		}

		/**
		 * Get LP from List Item when isFirstCall = true
		 * 
		 * */
		private ObjectCountCycleCurrent getLPFromItemNumber() {
			for (ObjectCountCycleCurrent objectCountCycleCurrent : binList) {
				if (objectCountCycleCurrent.get_itemNumber().equalsIgnoreCase(
						objectCycleLp.get_binNumber()))
					return objectCountCycleCurrent;
			}

			return null;
		}
	}

	/**
	 * LoadItemAsyn If item is scanned to will call this class
	 **/
	private class LoadItemAsyn extends AsyncTask<String, Void, Integer> {
		Context context;
		ProgressDialog progressDialog;
		String data;
		String barcode;
		
		public LoadItemAsyn(Context mContext) {
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
		protected Integer doInBackground(String... barcodes) {
			Integer result = 0;
			if (!isCancelled()) {
				try {
					NetParameter[] netParameter = new NetParameter[1];
					netParameter[0] = new NetParameter("barcode", URLEncoder.encode(barcodes[0].trim(), GlobalParams.UTF_8));
					barcode = barcodes[0];
					data = HttpNetServices.Instance
							.getItemByBarcode(netParameter);
					ObjectCycleItem objectCycleItem = DataParser
							.getCycleItem(data);
					countCycleCurrents = getListItem(objectCycleItem);

				} catch (Exception e) {
					result = 2;
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

			if (!isCancelled()) {
				if (result == 0) {

					if (countCycleCurrents.size() > 0) {
						// if size > 1 then ItemNumber is single in first list
						if (countCycleCurrents.size() == 1) {
							startActivityCycleAdjustment(countCycleCurrents
									.get(0));
						}
						// if size > 1 then ItemNumber has two more than and it
						// only has different Lot in first list
						else {
							String message = languagePrefs.getPreferencesString(GlobalParams.MESSAGE_SCAN_LOCATION_KEY,
									GlobalParams.MESSAGE_SCAN_LOCATION_VALUE);
							String contentMessage = message.replace("{0}", barcode);
							dialogShowMessage(contentMessage, AcCycleCountBinPath.this);
						}
					}

				} else if (result == 1) {
					setResult(RESULT_OK);
					AcCycleCountBinPath.this.finish();
				} else {
					String msgs = languagePrefs.getPreferencesString("error",
							"error");
					CommontDialog.showErrorDialog(context, msgs, null);
					Log.e("Appolis", "LoadItemAsyn #onPostExecute: " + result);
				}
			} else {
				String msgs = languagePrefs.getPreferencesString("error", "error");
				CommontDialog.showErrorDialog(context, msgs, null);
				Log.e("Appolis", "LoadItemAsyn #onPostExecute: " + result);
			}
		}

		private List<ObjectCountCycleCurrent> getListItem(
				ObjectCycleItem objectCycleItem) {
			List<ObjectCountCycleCurrent> cycleCurrents = new ArrayList<>();

			for (ObjectCountCycleCurrent countCycleCurrent : binList) {
				if (countCycleCurrent.get_itemNumber().equalsIgnoreCase(
						objectCycleItem.get_itemNumber())) {
					cycleCurrents.add(countCycleCurrent);
				}
			}

			return cycleCurrents;
		}

	}

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
		btDialogYes.setText(languagePrefs.getPreferencesString(GlobalParams.YES_TEXT, GlobalParams.YES_TEXT));
		Button btDialogNo = (Button) dialog.findViewById(R.id.dialogButtonNo);
		btDialogNo.setVisibility(View.VISIBLE);
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
				if(!isConnectSocket) {
					Intent intentScan = new Intent(AcCycleCountBinPath.this,
							CaptureBarcodeCamera.class);
					startActivityForResult(intentScan,
							GlobalParams.CAPTURE_BARCODE_CAMERA_ACTIVITY_WITH_LOT);
				} else {
					typeScan = CommonData.SCAN_LOT;
				}
			}
		});

		dialog.show();
	}
	
	/**
	 * Process event for views
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_clear_cycle_search:
			removeTextSearchCycle();

		case R.id.btnBack:
			isFisrtCall = "false";
			new LoadCycleCountBinPathListAsyn(AcCycleCountBinPath.this)
					.execute();
			break;
		case R.id.imgSearchIcon:
			imgSeachIcon.setVisibility(View.GONE);
			linSearch.setVisibility(View.VISIBLE);
			break;

		case R.id.lin_buton_scan:
			Intent intentScan = new Intent(AcCycleCountBinPath.this,
					CaptureBarcodeCamera.class);
			startActivityForResult(intentScan,
					GlobalParams.CAPTURE_BARCODE_CAMERA_ACTIVITY);
			break;
			
		case R.id.btnOption:
			dialogOptions.show();
			configDialogOption();
			break;	
			
		case R.id.tvDialogCancel:
			dialogOptions.dismiss();
			break;
			
		case R.id.lin_layout_Cycle_Adjustment:
			dialogOptions.dismiss();
			Intent intent = new Intent(AcCycleCountBinPath.this, AcCycleAdjustmentOption.class);
			intent.putExtra("objectCycleCurrent", binPrev);
			String loc = path + CommonData.PATH + binPrev.get_itemNumber();
			intent.putExtra("path", loc);
			ObjectBinList objectBinList = new ObjectBinList();
			intent.putExtra("isLocationScreen", false);
			objectBinList.setBinList(binList);
			intent.putExtra("BinCycleCount", bin);
			intent.putExtra("binList", objectBinList);
			startActivityForResult(intent, GlobalParams.AC_CYCLE_COUNT_ADJUSMENT_ACTIVITY);
			break;	
			
		case R.id.lin_layout_cycle_zero:
			dialogOptions.dismiss();
			String mes = languagePrefs.getPreferencesString(GlobalParams.CYCLE_MESS_ZEROQUESTION_KEY, GlobalParams.CYCLE_MESS_ZEROQUESTION_VALUE);
			String mesConfirm = mes.replace("{0}", objectCurrentSelected.get_itemNumber());
			dialogConfirmZeroCount(mesConfirm, AcCycleCountBinPath.this);
			break;
			
		default:
			break;
		}
	}

	/**
	 * Remove text
	 */
	private void removeTextSearchCycle() {
		txtCycleSearch.setText("");
		imgSeachIcon.setVisibility(View.VISIBLE);
		linSearch.setVisibility(View.GONE);
	}

	/**
	 * Remove highlight
	 * @param rowView
	 */
	private void unhighlightCurrentRow(View rowView) {
		if(CommonData.COMPLETED.equalsIgnoreCase(objectCurrentPreSelected.get_itemStatus().trim())) {
			rowView.setBackgroundColor(getResources().getColor(R.color.Gray73));
		} else {
			rowView.setBackgroundColor(getResources().getColor(R.color.white));
		}
	}

	/**
	 * Add highlight
	 * @param rowView
	 */
	private void highlightCurrentRow(View rowView) {
	    rowView.setBackgroundColor(getResources().getColor(
	            R.color.Blue12));

	}
	
	//Confirm zero count
	public void dialogConfirmZeroCount(String message, Activity activity) {
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
		btDialogYes.setText(languagePrefs.getPreferencesString(GlobalParams.YES_TEXT, GlobalParams.YES_TEXT));
		Button btDialogNo = (Button) dialog.findViewById(R.id.dialogButtonNo);
		btDialogNo.setText(languagePrefs.getPreferencesString(GlobalParams.NO_TEXT, GlobalParams.NO_TEXT));
		btDialogNo.setVisibility(View.VISIBLE);

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
				new ZeroCountAsyn(AcCycleCountBinPath.this).execute();
			}
		});

		dialog.show();
	}
	
	/**
	 * ZeroCount LP or Item
	 **/
	private class ZeroCountAsyn extends AsyncTask<Void, Void, Integer> {
		Context context;
		ProgressDialog progressDialog;
		String data;
		
		public ZeroCountAsyn(Context mContext) {
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
		protected Integer doInBackground(Void... barcodes) {
			Integer result = 0;
			if (!isCancelled()) {
				try {
					NetParameter[] netParameters = null;
					if(objectCurrentSelected.is_lpInd()) {
						netParameters = new NetParameter[2];
						netParameters[0] = new NetParameter("cyclecountInstanceID", objectCurrentSelected.get_cycleCountInstanceID() + "");
						netParameters[1] = new NetParameter("binNumber", URLEncoder.encode(objectCurrentSelected.get_itemNumber().trim(), GlobalParams.UTF_8));
					} else {
						netParameters = new NetParameter[4];
						netParameters[0] = new NetParameter("cycleCountItemInstanceID", objectCurrentSelected.get_cycleCountItemInstanceID() + "");
						netParameters[1] = new NetParameter("count", objectCurrentSelected.get_count() + "");
						netParameters[2] = new NetParameter("uomDesc", objectCurrentSelected.get_uomDescription());
						netParameters[3] = new NetParameter("itemNumber", URLEncoder.encode(objectCurrentSelected.get_itemNumber().trim(), GlobalParams.UTF_8));
					}
					
					data = HttpNetServices.Instance.updateCycleCountItem(netParameters);

				} catch (Exception e) {
					result = 2;
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

			if (!isCancelled()) {
				if (result == 0) {
					new LoadCycleCountBinPathListAsyn(AcCycleCountBinPath.this).execute();
				}  else {
					String msgs = languagePrefs.getPreferencesString("error", "error");
					CommontDialog.showErrorDialog(context, msgs, null);
					Log.e("Appolis", "ZeroCountAsyn #onPostExecute: " + result);
				}
			} else {
				String msgs = languagePrefs.getPreferencesString("error", "error");
				CommontDialog.showErrorDialog(context, msgs, null);
				Log.e("Appolis", "ZeroCountAsyn #onPostExecute: " + result);
			}
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
		typeScan = CommonData.SCAN_LP;
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
	        	isConnectSocket = true;
	        	linScan.setVisibility(View.INVISIBLE);
	        }
	        
	        // a Scanner has disconnected
	        else if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_SCANNER_REMOVAL))
	        {
	        	isConnectSocket = false;
	        	linScan.setVisibility(View.VISIBLE);
	        }
	        
	        // decoded Data received from a scanner
	        else if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_DECODED_DATA))
	        {
				char[] data = intent.getCharArrayExtra(SingleEntryApplication.EXTRA_DECODEDDATA);
				String barcode = new String(data);
				if(typeScan == CommonData.SCAN_LP) {
					processScanData(barcode);
				} else {
					processScanDataWithLot(barcode);
				}
	        }
	    }
	};
}
