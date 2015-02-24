/**
 * Name: $RCSfile: AcReceiveOptionMove.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.receiving;

import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

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
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.appolis.androidtablet.R;
import com.appolis.common.AppolisException;
import com.appolis.common.ErrorCode;
import com.appolis.common.LanguagePreferences;
import com.appolis.entities.EnBarcodeExistences;
import com.appolis.entities.EnBinTransfer;
import com.appolis.entities.EnPurchaseOrderInfo;
import com.appolis.entities.EnPurchaseOrderItemInfo;
import com.appolis.entities.EnPurchaseOrderReceiptInfo;
import com.appolis.entities.EnUom;
import com.appolis.network.NetParameter;
import com.appolis.network.access.HttpNetServices;
import com.appolis.scan.CaptureBarcodeCamera;
import com.appolis.scan.SingleEntryApplication;
import com.appolis.utilities.BuManagement;
import com.appolis.utilities.CommontDialog;
import com.appolis.utilities.DataParser;
import com.appolis.utilities.DecimalDigitsInputFilter;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.Logger;
import com.appolis.utilities.StringUtils;
import com.appolis.utilities.Utilities;

/**
 * @author HoangNH11
 * Handle Move option
 */
public class AcReceiveOptionMove extends Activity implements OnClickListener{
	private ImageView linBack;
	private ImageView linScan;
	private TextView tvHeader;
	private TextView tvmaxQty;
	private TextView tvItemDescription;
	private TextView tvTransfer;
	private Spinner spnMoveUOM;
	private EditText edtLotValue;
	private EditText edtMoveFrom;
	private EditText edtMoveQty;
	private EditText edtMoveTo;
	private Button btMoveOk;
	private Button btMoveCancel;
	private TextView tvMoveFunctionTitle;
	private TextView tvTitleTransfer;
	private TextView tvTitleMaxQty;
	private TextView tvUOM, tvLot, tvFrom, tvQtyView, tvTo;
	
	private ArrayList<EnPurchaseOrderReceiptInfo> listReceiptInfos = new ArrayList<>();
	private EnPurchaseOrderItemInfo enPurchaseOrderItemInfo;
	private EnPurchaseOrderInfo enPurchaseOrderInfo;
	private ProgressDialog dialog;
	private ArrayList<EnUom> enUom;
	private String uom;
	private double maxQty = 0;
	private LanguagePreferences languagePrefs;
	
	//define string multiple language
	String scanQty;
	String limitQtyMsg;
	String scanLocationMsg;
	String textLoading;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.move_details_layout);
		languagePrefs = new LanguagePreferences(getApplicationContext());
		
		Bundle bundle = getIntent().getExtras();
		if(bundle.containsKey(GlobalParams.PARAM_EN_PURCHASE_ORDER_INFOS)){
			enPurchaseOrderInfo = (EnPurchaseOrderInfo) bundle.get(GlobalParams.PARAM_EN_PURCHASE_ORDER_INFOS);
		}
		
		if(bundle.containsKey(GlobalParams.PARAM_EN_PURCHASE_ORDER_ITEM_INFOS)){
			enPurchaseOrderItemInfo = (EnPurchaseOrderItemInfo) bundle.get(GlobalParams.PARAM_EN_PURCHASE_ORDER_ITEM_INFOS);
		}
		
		if(bundle.containsKey(GlobalParams.PARAM_LIST_EN_PURCHASE_ORDER_RECEIPT_INFO)){
			listReceiptInfos = (ArrayList<EnPurchaseOrderReceiptInfo>) bundle.get(GlobalParams.PARAM_LIST_EN_PURCHASE_ORDER_RECEIPT_INFO);
		}
		
		getMultipleLanguage();
		
		initLayout();
	}
	
	/**
	 * function to initial layout
	 */
	private void initLayout(){
		linBack = (ImageView) findViewById(R.id.imgHome);
		linBack.setVisibility(View.GONE);
		linScan = (ImageView) findViewById(R.id.img_main_menu_scan_barcode);
		linScan.setOnClickListener(this);
		linScan.setVisibility(View.GONE);
		tvHeader = (TextView) findViewById(R.id.tvHeader);
		tvHeader.setText(languagePrefs.getPreferencesString(GlobalParams.RID_LBL_MOVE_KEY, GlobalParams.RID_LBL_MOVE_VALUE));
		
		tvMoveFunctionTitle = (TextView) findViewById(R.id.textView_move);
		tvMoveFunctionTitle.setText(languagePrefs.getPreferencesString(
				GlobalParams.MV_MSG_SELECTORSCAN_KEY, GlobalParams.MV_MSG_SELECTORSCAN_VALUE));
		tvTitleTransfer = (TextView) findViewById(R.id.tvTitleTransfer);
		tvTitleTransfer.setText(languagePrefs.getPreferencesString(GlobalParams.TRANSFER, GlobalParams.TRANSFER) + GlobalParams.VERTICAL_TWO_DOT);
		tvTitleMaxQty = (TextView) findViewById(R.id.tvTitleMaxQty);
		tvTitleMaxQty.setText(languagePrefs.getPreferencesString(GlobalParams.MV_LBL_MAXQTY, GlobalParams.DMG_LBL_MAXQTY_VALUE) + ": ");
		tvUOM = (TextView) findViewById(R.id.tvUOM);
		tvUOM.setText(languagePrefs.getPreferencesString(GlobalParams.MV_LBL_UOM, GlobalParams.UNIT_OF_MEASURE_VALUE)
				+ GlobalParams.VERTICAL_TWO_DOT);
		tvLot = (TextView) findViewById(R.id.tvLot);
		tvLot.setText(languagePrefs.getPreferencesString(GlobalParams.REST_GRD_LOT_KEY, GlobalParams.LOT) + GlobalParams.VERTICAL_TWO_DOT);
		tvFrom = (TextView) findViewById(R.id.tvFrom);
		tvFrom.setText(languagePrefs.getPreferencesString(GlobalParams.MV_LBL_FROM, GlobalParams.FROM) + GlobalParams.VERTICAL_TWO_DOT);
		tvQtyView = (TextView) findViewById(R.id.tvQtyView);
		tvQtyView.setText(languagePrefs.getPreferencesString(GlobalParams.RID_GRD_QTY_KEY, GlobalParams.QTY));
		tvTo = (TextView) findViewById(R.id.tvTo);
		tvTo.setText(languagePrefs.getPreferencesString(GlobalParams.MV_LBL_TO, GlobalParams.TO) + GlobalParams.VERTICAL_TWO_DOT);
		
		tvmaxQty = (TextView) findViewById(R.id.tvmaxQty);
		tvItemDescription = (TextView) findViewById(R.id.tvItemDescription);
		tvTransfer = (TextView) findViewById(R.id.tvTransfer);
		spnMoveUOM = (Spinner) findViewById(R.id.spn_Move_UOM);
		edtLotValue = (EditText) findViewById(R.id.edtLotValue);
		edtMoveFrom = (EditText) findViewById(R.id.edt_move_from);
		edtMoveQty = (EditText) findViewById(R.id.et_move_qty);
		edtMoveQty.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(enPurchaseOrderItemInfo.get_significantDigits())});
		edtMoveTo = (EditText) findViewById(R.id.et_move_to);
		btMoveOk =(Button) findViewById(R.id.btnOK);
		btMoveOk.setText(languagePrefs.getPreferencesString(GlobalParams.OK, GlobalParams.OK));
		btMoveOk.setOnClickListener(this);
		btMoveOk.setEnabled(true);
		btMoveCancel =(Button) findViewById(R.id.btnCancel);
		btMoveCancel.setText(languagePrefs.getPreferencesString(GlobalParams.CANCEL, GlobalParams.CANCEL));
		btMoveCancel.setOnClickListener(this);
		
		edtMoveTo.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					linScan.setVisibility(View.VISIBLE);
				} else {
					linScan.setVisibility(View.GONE);						
				}
			}
		});
		
		if(null != enPurchaseOrderItemInfo){
			tvTransfer.setText(enPurchaseOrderItemInfo.get_itemNumber());
			tvItemDescription.setText(enPurchaseOrderItemInfo.get_itemDesc());
		}
		
		if(null != enPurchaseOrderInfo){
			if(StringUtils.isNotBlank(enPurchaseOrderInfo.get_receivingBinNumber())){
				edtMoveFrom.setText(enPurchaseOrderInfo.get_receivingBinNumber());
			} else {
				edtMoveFrom.setText("");
			}
			edtMoveFrom.setEnabled(false);
		}
		
		if(null != listReceiptInfos){
			if(listReceiptInfos.size() == 1){
				Log.e("Appolis", "single record was selected from the previous screen");
				EnPurchaseOrderReceiptInfo recipt = listReceiptInfos.get(0);
				maxQty = recipt.get_quantityReceived();
				tvmaxQty.setText(BuManagement.formatDecimal(recipt.get_quantityReceived()).trim());
				edtLotValue.setText(recipt.get_lotNumber());
				edtLotValue.setEnabled(false);
				edtMoveQty.setText(BuManagement.formatDecimal(recipt.get_quantityReceived()).trim());
				edtMoveQty.setEnabled(true);
				spnMoveUOM.setEnabled(true);
				edtMoveTo.requestFocus();
				
				GetDataAsyncTask getDataAsyncTask = new GetDataAsyncTask(this, enPurchaseOrderItemInfo.get_itemNumber());
				getDataAsyncTask.execute();
			} else {
				Logger.error("Appolis: multiple records have been selected on the previous screen");
				String strlot = "";
				int index = 0;
				
				for (EnPurchaseOrderReceiptInfo item : listReceiptInfos) {
					maxQty += item.get_quantityReceived();
					
					if(StringUtils.isNotBlank(item.get_lotNumber())){
						if(index == 0){
							strlot = strlot + item.get_lotNumber(); 
						} else {
							strlot = strlot + ", " + item.get_lotNumber(); 
						}
						index++;
					}
				}
				
				tvmaxQty.setText(BuManagement.formatDecimal(maxQty).trim());
				edtMoveTo.requestFocus();
				edtLotValue.setText(strlot);
				edtLotValue.setEnabled(false);
				edtMoveQty.setText(BuManagement.formatDecimal(maxQty).trim());
				edtMoveQty.setEnabled(false);
				spnMoveUOM.setEnabled(false);
				uom = enPurchaseOrderItemInfo.get_uomDesc();
				ArrayList<String> listUom = new ArrayList<String>();
				listUom.add(uom);
				ArrayAdapter<String> uomAdapter = new ArrayAdapter<String>(this, R.layout.custom_spinner_item, listUom);
				spnMoveUOM.setAdapter(uomAdapter);
			}
		}
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case GlobalParams.CAPTURE_BARCODE_CAMERA_ACTIVITY:
			if(resultCode == RESULT_OK){
				String message = data.getStringExtra(GlobalParams.RESULTSCAN);
				edtMoveTo.setText(message);
				//checkAndCommitData();
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
				edtMoveTo.setText(message);
				//checkAndCommitData();
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
	 * get language from language package
	 */
	public void getMultipleLanguage(){
		scanQty = languagePrefs.getPreferencesString(GlobalParams.ENTER_OR_SCAN_QTY_KEY, GlobalParams.ENTER_OR_SCAN_QTY_VALUE);
		limitQtyMsg = languagePrefs.getPreferencesString(GlobalParams.MV_LIMITQTY_MSG_KEY, GlobalParams.MV_LIMITQTY_MSG_VALUE);
		scanLocationMsg = languagePrefs.getPreferencesString(GlobalParams.SCAN_SELECTLOC_SEARCH_KEY, GlobalParams.SCAN_SELECTLOC_SEARCH_VALUE);
		textLoading = languagePrefs.getPreferencesString(GlobalParams.MAINLIST_MENLOADING_KEY, GlobalParams.MAINLIST_MENLOADING_VALUE);
	}
	
	@Override
	public void onClick(View v) {
		Intent intent;
		
		switch (v.getId()) {
		case R.id.img_main_menu_scan_barcode:
			intent = new Intent(this, CaptureBarcodeCamera.class);		
			startActivityForResult(intent, GlobalParams.CAPTURE_BARCODE_CAMERA_ACTIVITY);
			break;
			
		case R.id.btnOK:
			//validate and commit data
			checkAndCommitData();
			break;
		
		case R.id.btnCancel:
			setResult(RESULT_CANCELED);
			this.finish();
			break;
		default:
			break;
		}
	}
	
	/**
	 * check data validation and commit data
	 */
	public void checkAndCommitData(){
		String strQty = edtMoveQty.getText().toString();
		if(StringUtils.isBlank(strQty)){
			CommontDialog.showErrorDialog(this, scanQty, GlobalParams.WARMING);
			return;
		}
		
		double dbQty = 0;
		try{
			dbQty = Double.parseDouble(strQty);
		} catch (Exception e) {
			CommontDialog.showErrorDialog(this, languagePrefs.getPreferencesString(
					GlobalParams.MNP_MSG_INVALIDQTY_KEY, GlobalParams.MNP_MSG_INVALIDQTY_VALUE), null);
			return;
		}
		
		if(dbQty <= 0){
			CommontDialog.showErrorDialog(this, languagePrefs.getPreferencesString(
					GlobalParams.PID_EMPTY_QTY_MOVE_KEY, GlobalParams.PID_EMPTY_QTY_MOVE_VALUE), null);
			return;
		}
		
		if(dbQty > maxQty){
			CommontDialog.showErrorDialog(this, limitQtyMsg, GlobalParams.WARMING);
			return;
		}
		
		String strMoveTo = edtMoveTo.getText().toString();
		if(StringUtils.isBlank(strMoveTo)){
			CommontDialog.showErrorDialog(this, scanLocationMsg, GlobalParams.WARMING);
			return;
		}
		
		if(strMoveTo.equalsIgnoreCase(edtMoveFrom.getEditableText().toString())){
			CommontDialog.showErrorDialog(this, languagePrefs.getPreferencesString(
					GlobalParams.BINTRANSFER_MSGLPMOVETOSELFERROR_KEY, GlobalParams.BINTRANSFER_MSGLPMOVETOSELFERROR_VALUE), null);
			return;
		}
		
		commitData(strMoveTo);
		
	}
	
	/**
	 * Set value to Object EnBinTransfer
	 * @throws JSONException
	 */
	private String prepareJsonForm(boolean isLicensePlate) throws JSONException {	
		List<EnBinTransfer> listBinTransferString = new ArrayList<EnBinTransfer>();
		EnBinTransfer enBinTransfer = new EnBinTransfer();
		String binTransferFunction = "";
		try{
			enBinTransfer.setFromBinNumber(edtMoveFrom.getEditableText().toString());
			enBinTransfer.setIsLicensePlate(isLicensePlate);
			enBinTransfer.setItemNumber(tvTransfer.getText().toString());
			enBinTransfer.setLotNumber(edtLotValue.getEditableText().toString());
			enBinTransfer.setQuantity(Double.parseDouble(edtMoveQty.getEditableText().toString()));
			enBinTransfer.setToBinNumber(edtMoveTo.getEditableText().toString());
			enBinTransfer.setTransactionType("Bin Transfer");
			enBinTransfer.setUomDescription(uom);
			listBinTransferString.add(enBinTransfer);
			binTransferFunction = DataParser.convertObjectToString(listBinTransferString);
			Logger.error("binTransferFunction: " + binTransferFunction);
		} catch (Exception e) {
			e.printStackTrace();
			binTransferFunction = "";
		}
		
		return  binTransferFunction;
	}
	
	/**
	 * commit item details
	 * @param locationTo location to move
	 */
	private void commitData(String locationTo) {
		OptionMoveAsyn mLoadDataTask = new OptionMoveAsyn(this, locationTo);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mLoadDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			mLoadDataTask.execute();
		}
	}
	
	/**
	 * OptionMoveAsyn
	 * @author HoangNH11
	 *
	 */
	private class OptionMoveAsyn extends AsyncTask<Void, Void, Integer>{
		Context context;
		String locationTo;
		String response;
		
		public OptionMoveAsyn(Context mContext, String locationTo) {
			this.context = mContext;
			this.locationTo = locationTo;
		}
		
		@Override
		protected void onPreExecute() {
			if(null != dialog){
				dialog = null;
			}
			dialog = new ProgressDialog(context);
			dialog.setMessage(textLoading + "...");
			dialog.show();
			dialog.setCancelable(false); 
			dialog.setCanceledOnTouchOutside(false);
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			Integer resultCode = ErrorCode.STATUS_SUCCESS;
			
			if(!Utilities.isConnected(context)){
				return ErrorCode.STATUS_NETWORK_NOT_CONNECT; 
			}
			try{
				if (!isCancelled()) {
					Log.e("Appolis", "################## OptionMoveAsyn : SATRT #########################");
					NetParameter[] netParameters = new NetParameter[1];
					netParameters[0] = new NetParameter("barcode", URLEncoder.encode(locationTo, GlobalParams.UTF_8));
					response = HttpNetServices.Instance.getBarcode(netParameters);
					Log.e("Appolis", "getBarcode #response:" + response);
					
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
						} else if (enBarcodeExistences.getBinOnlyCount() != 0 || enBarcodeExistences.getLPCount() != 0) {
							//LP or Bin then call is good
							if(!isCancelled()){
								boolean isLP = false;
								if(enBarcodeExistences.getLPCount() != 0){
									isLP = true;
								} else {
									isLP = false;
								}
								String strEntity = prepareJsonForm(isLP);
								response = HttpNetServices.Instance.postItem(strEntity);
								Log.e("Appolis", "OptionMoveAsyn #postItem #response:" + response);
							}
						} else {
							//create a new license plate?
							return ErrorCode.STATUS_SCAN_UNSUPPORTED_BARCODE;
						}
					} else {
						resultCode =  ErrorCode.STATUS_EXCEPTION;; // exception
					}
					
					Log.e("Appolis", "################## OptionMoveAsyn : END #########################");
				}
			} catch (Exception e) {
				e.printStackTrace();
				resultCode = ErrorCode.STATUS_EXCEPTION;
				if(e instanceof JSONException){
					resultCode = ErrorCode.STATUS_JSON_EXCEPTION;
				} else if( e instanceof ClientProtocolException || e instanceof UnknownHostException
						|| e instanceof SocketTimeoutException || e instanceof SocketException
						|| e instanceof ConnectTimeoutException || e instanceof MalformedURLException
						|| e instanceof SSLException){
					resultCode = ErrorCode.STATUS_NETWORK_NOT_CONNECT;
				}
			}
			return resultCode;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			if(null != dialog){
				dialog.dismiss();
			}
			
			// If not cancel by user
			if (!isCancelled()) {
				switch (result) {
				case ErrorCode.STATUS_SUCCESS:
					if (response.equalsIgnoreCase(GlobalParams.TRUE)) {
						setResult(RESULT_OK);
						finish();
					} else {
						if(StringUtils.isNotBlank(response)){
							CommontDialog.showErrorDialog(context, response, null);
						} else {
							String msg = languagePrefs.getPreferencesString(GlobalParams.MV_MOVEFAILED_KEY, GlobalParams.MV_MOVEFAILED_VALUE);
							CommontDialog.showErrorDialog(context, msg, null);
						}
					}
					break;
				
				case ErrorCode.STATUS_NETWORK_NOT_CONNECT:
				case ErrorCode.STATUS_JSON_EXCEPTION:
					String msg = languagePrefs.getPreferencesString(GlobalParams.ERRORUNABLETOCONTACTSERVER, GlobalParams.ERROR_INVALID_NETWORK);
					CommontDialog.showErrorDialog(context, msg, null);
					break;
					
				case ErrorCode.STATUS_SCAN_ERROR:
				case ErrorCode.STATUS_SCAN_UNSUPPORTED_BARCODE:
					String confirmMsg = languagePrefs.getPreferencesString(GlobalParams.MV_LOCATION_NOT_FOUND_MSG_KEY, GlobalParams.MV_LOCATION_NOT_FOUND_MSG_VALUE);
					confirmMsg = MessageFormat.format(confirmMsg, locationTo);
					DialogInterface.OnClickListener onOkClickListener = new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							CreateNewPlateAsyn createNewPalte = new CreateNewPlateAsyn(context, locationTo);
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
								createNewPalte.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
							} else {
								createNewPalte.execute();
							}
						}
					};
					CommontDialog.createConfirmAlerDialog(context, "Confirm", confirmMsg, onOkClickListener, null);
					break;
					
				default:
					String msgFail = languagePrefs.getPreferencesString(GlobalParams.MV_MOVEFAILED_KEY, GlobalParams.MV_MOVEFAILED_VALUE);
					CommontDialog.showErrorDialog(context, msgFail, null);
					break;
				}
			}
		}
	}
	
	/**
	 * CreateNewPlateAsyn
	 * @author HoangNH11
	 *
	 */
	private class CreateNewPlateAsyn extends AsyncTask<Void, Void, Integer>{
		Context contextAsyn;
		String newLicensePalte = "";
		String requestRespone;
		
		public CreateNewPlateAsyn(Context mContext, String newLicensePalte) {
			// TODO Auto-generated constructor stub
			this.contextAsyn = mContext;
			this.newLicensePalte = newLicensePalte;
		}
		
		@Override
		protected void onPreExecute() {
			if(dialog != null){
				dialog = null;
			}
			dialog = new ProgressDialog(contextAsyn);
			dialog.setMessage(textLoading + "...");
			dialog.show();
			dialog.setCancelable(false); 
			dialog.setCanceledOnTouchOutside(false);
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			Integer resultCode = ErrorCode.STATUS_SUCCESS;
			if(!Utilities.isConnected(contextAsyn)){
				return ErrorCode.STATUS_NETWORK_NOT_CONNECT; 
			}
			
			if (!isCancelled()) {
				try {
					Logger.error("################ CreateNewPlateAsyn : SATRT ##################");
					NetParameter[] netParameters = new NetParameter[2];
					netParameters[0] = new NetParameter("parentBinNumber", enPurchaseOrderInfo.get_receivingBinNumber());
					netParameters[1] = new NetParameter("licensePlateNumber", URLEncoder.encode(newLicensePalte, GlobalParams.UTF_8));
					requestRespone = HttpNetServices.Instance.createNewLisecePlate(netParameters);
					Logger.error("CreateNewPlateAsyn #requestRespone:" + requestRespone);
					if(StringUtils.isNotBlank(requestRespone)){
						requestRespone = BuManagement.getDataBarcode(requestRespone);
						if(newLicensePalte.equalsIgnoreCase(requestRespone)){
							resultCode = ErrorCode.STATUS_SUCCESS;
						} else {
							resultCode = ErrorCode.STATUS_FAIL;
						}
					} else {
						resultCode = ErrorCode.STATUS_FAIL;
					}
					Logger.error("################ CreateNewPlateAsyn : END ##################");
				} catch (Exception e) {
					e.printStackTrace();
					resultCode = ErrorCode.STATUS_EXCEPTION;
				}
			} 
			return resultCode;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			if(null != dialog){
				dialog.dismiss();
			}
			
			// If not cancel by user
			if (!isCancelled()) {
				switch (result) {
				case ErrorCode.STATUS_SUCCESS:
					commitData(newLicensePalte);
					break;
				case ErrorCode.STATUS_FAIL:
					String message = languagePrefs.getPreferencesString(GlobalParams.BIN_MESSAGE_BOX_CREATE_LP_ERROR_KEY
							, GlobalParams.BIN_MESSAGE_BOX_CREATE_LP_ERROR_VALUE);
					CommontDialog.showErrorDialog(contextAsyn, message, "Warning");
					break;
					
				default:
					String messageDefault = languagePrefs.getPreferencesString(GlobalParams.ERROR_CONNECTING_TO_WEB_SERVICE_KEY
							, GlobalParams.ERROR_CONNECTING_TO_WEB_SERVICE_KEY);
					CommontDialog.showErrorDialog(contextAsyn, messageDefault, "Warning");
					break;
				}
			}
		}
	}
	
	/**
	 * get UOM data of item
	 * @author HoangNH11
	 *
	 */
	class GetDataAsyncTask extends AsyncTask<Void, Void, String> {
		Context context;
		String barcode;
		String data, dataUOM;
		Intent intent;
		
		public GetDataAsyncTask(Context mContext, String barCode) {
			// TODO Auto-generated constructor stub
			this.context = mContext;
			this.barcode = barCode;
		}
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(context);
			dialog.setMessage(textLoading + "...");
			dialog.show();
			dialog.setCancelable(false); 
			dialog.setCanceledOnTouchOutside(false);
		}
		
		@Override
		protected String doInBackground(Void... params) {
			String result;
			if (!isCancelled()) {
				try {
					NetParameter[] netParameterUOM = new NetParameter[1];
					netParameterUOM[0] = new NetParameter("itemNumber", barcode);
					dataUOM = HttpNetServices.Instance.getUOMItemNumber(netParameterUOM);
					enUom =  DataParser.getUom(dataUOM);
					Logger.error(dataUOM);
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
			if(null != dialog){
				dialog.dismiss();
			}
			
			// If not cancel by user
			if (!isCancelled()) {
				if (result.equals("true")) {
					if (enUom != null) {
						
						ArrayList<String> listUom = new ArrayList<String>();
						for (int i = 0; i < enUom.size(); i++) {
							listUom.add(enUom.get(i).getUomDescription());
						}
						
						ArrayAdapter<String> uomAdapter = new ArrayAdapter<String>(context,
								R.layout.custom_spinner_item, listUom);
						spnMoveUOM.setAdapter(uomAdapter);
						spnMoveUOM.setOnItemSelectedListener(new OnItemSelectedListener() {
							
							@Override
							public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
								uom = spnMoveUOM.getSelectedItem().toString();
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
							}
						});
					} else {
						Utilities.showPopUp(context, null, getResources().getString(R.string.LOADING_FAIL));
					}
				} else {
					Utilities.showPopUp(context, null, getResources().getString(R.string.LOADING_FAIL));
				}
			}
		}
	}
}
