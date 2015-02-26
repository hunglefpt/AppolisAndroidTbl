/**
 * Name: $RCSfile: AcReceiveOptionDamage.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.receiving;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appolis.androidtablet.R;
import com.appolis.common.LanguagePreferences;
import com.appolis.entities.EnPurchaseOrderInfo;
import com.appolis.entities.EnPurchaseOrderItemInfo;
import com.appolis.entities.EnPurchaseOrderReceiptInfo;
import com.appolis.network.NetParameter;
import com.appolis.network.access.HttpNetServices;
import com.appolis.utilities.BuManagement;
import com.appolis.utilities.CommontDialog;
import com.appolis.utilities.DecimalDigitsInputFilter;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.StringUtils;
import com.appolis.utilities.Utilities;

/**
 * @author HoangNH11
 * Handle Damage option
 */
public class AcReceiveOptionDamage extends Activity implements OnClickListener{
	private LinearLayout linBack;
	private LinearLayout linScan;
	private TextView tvHeader;
	private TextView tvDamageReceiveRequest;
	private TextView tvDamageReceiveName;
	private TextView tvDamageReceiveLot;
	private TextView tvDamageReceiveUom;
	private TextView tvDamageReceiveLocation;
	private TextView tvDamageReceiveMaxQty;
	private TextView tvQtyToDamageLalbe;
	private EditText edtQuantityToReceive;
	private Button btnDamageReceiveOK;
	private Button btnDamageReceiveCancel;
	
	private EnPurchaseOrderItemInfo enPurchaseOrderItemInfo;
	private EnPurchaseOrderReceiptInfo enPurchaseOrderReceiptInfo;
	private EnPurchaseOrderInfo enPurchaseOrderInfo;
	private double maxQty = 0d;
	private LanguagePreferences languagePrefes;
	
	//key to support multiple language
	private String textLot;
	private String textUOM;
	private String textLocation;
	private String textMaxQty;
	private String strLoading;
	private String textQtyToDamageLable;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_receive_option_damage);
		languagePrefes = new LanguagePreferences(getApplicationContext());
		Bundle bundle = getIntent().getExtras();
		
		if(bundle.containsKey(GlobalParams.PARAM_EN_PURCHASE_ORDER_INFOS)){
			enPurchaseOrderInfo = (EnPurchaseOrderInfo) bundle.get(GlobalParams.PARAM_EN_PURCHASE_ORDER_INFOS);
		}
		
		if(bundle.containsKey(GlobalParams.PARAM_EN_PURCHASE_ORDER_ITEM_INFOS)){
			enPurchaseOrderItemInfo = (EnPurchaseOrderItemInfo) bundle.get(GlobalParams.PARAM_EN_PURCHASE_ORDER_ITEM_INFOS);
		}
		
		if(bundle.containsKey(GlobalParams.PARAM_EN_PURCHASE_ORDER_RECEIPT_INFO)){
			enPurchaseOrderReceiptInfo = (EnPurchaseOrderReceiptInfo) bundle.get(GlobalParams.PARAM_EN_PURCHASE_ORDER_RECEIPT_INFO);
		}
		
		getLanguage();
		initLayout();
	}
	
	public void initLayout(){
		linBack = (LinearLayout) findViewById(R.id.lin_buton_home);
		linBack.setVisibility(View.GONE);
		linScan = (LinearLayout) findViewById(R.id.lin_buton_scan);
		linScan.setOnClickListener(this);
		linScan.setVisibility(View.GONE);
		tvHeader = (TextView) findViewById(R.id.tv_header);
		String header = languagePrefes.getPreferencesString(GlobalParams.DMG_TITLE_DAMAGE_KEY, GlobalParams.DMG_TITLE_DAMAGE_VALUE);
		tvHeader.setText(header);
		
		tvDamageReceiveRequest = (TextView) findViewById(R.id.tvDamageReceiveRequest);
		if(null != enPurchaseOrderInfo && StringUtils.isNotBlank(enPurchaseOrderInfo.get_poNumber())){
			String requestKey = languagePrefes.getPreferencesString( GlobalParams.DMG_LBL_RECEIVEREQUEST_KEY,
					GlobalParams.DMG_LBL_RECEIVEREQUEST_TEXT);
			tvDamageReceiveRequest.setText(requestKey + ": " + enPurchaseOrderInfo.get_poNumber());
		}
		
		tvDamageReceiveName = (TextView) findViewById(R.id.tvDamageReceiveName);
		tvDamageReceiveLot = (TextView) findViewById(R.id.tvDamageReceiveLot);
		tvDamageReceiveUom = (TextView) findViewById(R.id.tvDamageReceiveUom);
		tvDamageReceiveLocation = (TextView) findViewById(R.id.tvDamageReceiveLocation);
		tvQtyToDamageLalbe = (TextView) findViewById(R.id.tv_quanity_to_damage_lable);
		tvQtyToDamageLalbe.setText(textQtyToDamageLable);
		
		if(null != enPurchaseOrderItemInfo){
			if(StringUtils.isNotBlank(enPurchaseOrderItemInfo.get_itemDesc()) &&
					StringUtils.isNotBlank(enPurchaseOrderItemInfo.get_itemNumber())){
				tvDamageReceiveName.setText(enPurchaseOrderItemInfo .get_itemDesc()
						+ "-" + enPurchaseOrderItemInfo.get_itemNumber());
			}
			
			tvDamageReceiveUom.setText(textUOM + ": " + enPurchaseOrderItemInfo.get_uomDesc());
		}
		
		edtQuantityToReceive = (EditText) findViewById(R.id.edtQuantityToDamage);
		edtQuantityToReceive.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int end, int cont) {
				if(null != s && s.length() == 1 && s.toString().equalsIgnoreCase(".")){
					edtQuantityToReceive.setText("");
					s = "";
				}
				
				if(null == s || StringUtils.isBlank(s.toString())){
					btnDamageReceiveOK.setEnabled(false);
				} else {
					btnDamageReceiveOK.setEnabled(true);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		if(null != enPurchaseOrderItemInfo){
			edtQuantityToReceive.setFilters(
				new InputFilter[]{
						new DecimalDigitsInputFilter(enPurchaseOrderItemInfo.get_significantDigits()),
						new InputFilter.LengthFilter(14)
				}
			);
		}
		
		tvDamageReceiveMaxQty = (TextView) findViewById(R.id.tvDamageReceiveMaxQty);
		if(null != enPurchaseOrderReceiptInfo){

			if(StringUtils.isNotBlank(enPurchaseOrderReceiptInfo.get_binNumber())){
				tvDamageReceiveLocation.setText(textLocation + ":" + enPurchaseOrderReceiptInfo.get_binNumber());
			} else {
				tvDamageReceiveLocation.setText("");
			}
			
			if(StringUtils.isNotBlank(enPurchaseOrderReceiptInfo.get_lotNumber())){
				tvDamageReceiveLot.setText(textLot + ": " + enPurchaseOrderReceiptInfo.get_lotNumber());
			} else {
				tvDamageReceiveLot.setText("");
			}
			
			maxQty = enPurchaseOrderReceiptInfo.get_quantityReceived();
			tvDamageReceiveMaxQty.setText(textMaxQty + ": " + BuManagement.formatDecimal(maxQty).trim()); 
		}
		
		btnDamageReceiveOK =(Button) findViewById(R.id.btnDamageReceiveOK);
		btnDamageReceiveOK.setText(languagePrefes.getPreferencesString(GlobalParams.OK_KEY, GlobalParams.OK_KEY));
		btnDamageReceiveOK.setOnClickListener(this);
		btnDamageReceiveOK.setEnabled(false);
		btnDamageReceiveCancel =(Button) findViewById(R.id.btnDamageReceiveCancel);
		btnDamageReceiveCancel.setText(languagePrefes.getPreferencesString(GlobalParams.SETTINGS_BTN_CANCEL_KEY, GlobalParams.SETTINGS_BTN_CANCEL_VALUE));
		btnDamageReceiveCancel.setOnClickListener(this);
	}
	
	public void getLanguage(){
		textLot = languagePrefes.getPreferencesString(GlobalParams.REST_GRD_LOT_KEY, GlobalParams.REST_GRD_LOT_VALUE); 
		textUOM = languagePrefes.getPreferencesString(GlobalParams.UNIT_OF_MEASURE_KEY, GlobalParams.UNIT_OF_MEASURE_VALUE);
		textLocation = languagePrefes.getPreferencesString(GlobalParams.DMG_LBL_LOCATION_KEY, GlobalParams.DMG_LBL_LOCATION_VALUE);
		textMaxQty = languagePrefes.getPreferencesString(GlobalParams.DMG_LBL_MAXQTY_KEY, GlobalParams.DMG_LBL_MAXQTY_VALUE);
		strLoading = languagePrefes.getPreferencesString(GlobalParams.MAINLIST_MENLOADING_KEY, GlobalParams.MAINLIST_MENLOADING_VALUE);
		textQtyToDamageLable = languagePrefes.getPreferencesString(GlobalParams.DMG_LBL_QUANTITYTODAMAGE_KEY, GlobalParams.DMG_LBL_QUANTITYTODAMAGE_VALUE);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnDamageReceiveCancel:
			setResult(RESULT_CANCELED);
			this.finish();
			break;
			
		case R.id.btnDamageReceiveOK:
			double value = 0;
			try{
				if(StringUtils.isNotBlank(edtQuantityToReceive.getText().toString())){
					value = Double.parseDouble(edtQuantityToReceive.getText().toString());
				} else {
					value = 0;
				}
			} catch (Exception e) {
				e.printStackTrace();
				CommontDialog.showErrorDialog(AcReceiveOptionDamage.this, 
						languagePrefes.getPreferencesString(GlobalParams.MNP_MSG_INVALIDQTY_KEY, GlobalParams.MNP_MSG_INVALIDQTY_VALUE),null);
				return;
			}
			
			if(value <= 0 || value > maxQty){
				CommontDialog.showErrorDialog(AcReceiveOptionDamage.this, 
						languagePrefes.getPreferencesString(GlobalParams.RID_INPUTQUANTITYERROR_MSG_KEY,GlobalParams.RID_INPUTQUANTITYERROR_MSG_VALUE),null);
				return;
			} 
			
			int purchaseOrderItemId = enPurchaseOrderReceiptInfo.get_purchaseOrderItemShipID();
			String quantity = edtQuantityToReceive.getText().toString();

			NetParameter[] netParameters = new NetParameter[2];
			netParameters[0] = new NetParameter("purchaseOrderItemShipId", String.valueOf(purchaseOrderItemId));
			netParameters[1] = new NetParameter("quantity", quantity); 
			commitData(netParameters);
			break;
			
		default:
			break;
		}
	}
	
	/**
	 * commit item details
	 * @param netParameters
	 */
	private void commitData(NetParameter[] netParameters) {
		ReceiveClikOKAsyn mLoadDataTask = new ReceiveClikOKAsyn(AcReceiveOptionDamage.this, netParameters);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mLoadDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			mLoadDataTask.execute();
		}
	}
	
	
	/**
	 * ReceiveClikOKAsyn
	 * @author HUNGLV1
	 *
	 */
	private class ReceiveClikOKAsyn extends AsyncTask<Void, Void, Integer>{
		Context context;
		ProgressDialog progressDialog;
		String response;
		NetParameter[] netParameters;
		
		public ReceiveClikOKAsyn(Context mContext, NetParameter[] netParameters){
			this.context = mContext;
			this.netParameters = netParameters;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage(strLoading + "...");
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
		
		@Override
		protected Integer doInBackground(Void... arg0) {
			int errorCode = 0;
			if(!Utilities.isConnected(context)){
				return 1; 
			}
			
			try{
				if(!isCancelled()){
					response = HttpNetServices.Instance.commitReceiveOptionDamage(netParameters);
					if(StringUtils.isNotBlank(response)){
						errorCode = 3;
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
			
			if(!isCancelled()){
				switch (result) {
				case 0: //success
					setResult(RESULT_OK);
					AcReceiveOptionDamage.this.finish();
					break;
				
				case 1: //no network
					String msg = languagePrefes.getPreferencesString(GlobalParams.ERRORUNABLETOCONTACTSERVER, GlobalParams.ERROR_INVALID_NETWORK);
					CommontDialog.showErrorDialog(context, msg, null);
					break;
				
				case 3: //message from server
					String message = response;
					CommontDialog.showErrorDialog(context, message, null);
					break;
				default:
					String msgs = languagePrefes.getPreferencesString("Error", GlobalParams.ERROR_INVALID_NETWORK);
					CommontDialog.showErrorDialog(context, msgs, null);
					break;
				}
			}
		}
	}
}
