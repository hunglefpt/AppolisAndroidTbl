/**
 * Name: $RCSfile: AcReceiveItemDetail.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.receiving;

import java.text.MessageFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.appolis.adapter.ReceivingItemDetailAdapter;
import com.appolis.androidtablet.R;
import com.appolis.common.ErrorCode;
import com.appolis.common.LanguagePreferences;
import com.appolis.entities.EnItemLotInfo;
import com.appolis.entities.EnPurchaseOrderInfo;
import com.appolis.entities.EnPurchaseOrderItemInfo;
import com.appolis.entities.EnPurchaseOrderReceiptInfo;
import com.appolis.network.NetParameter;
import com.appolis.network.access.HttpNetServices;
import com.appolis.scan.CaptureBarcodeCamera;
import com.appolis.scan.SingleEntryApplication;
import com.appolis.utilities.BuManagement;
import com.appolis.utilities.CommontDialog;
import com.appolis.utilities.DataParser;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.Logger;
import com.appolis.utilities.StringUtils;
import com.appolis.utilities.Utilities;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * @author HoangNh11
 *
 */
public class AcReceiveItemDetail extends Activity implements OnClickListener, OnItemClickListener {
	private LinearLayout linBack;
	private LinearLayout linScan;
	private TextView tvHeader;
	private TextView tvReceiveItemDetailNumber;
	private TextView tvReceiveItemDetailName;
	private TextView tvItemDetailLot;
	private TextView tvItemDetailUom;
	private TextView tvItemDetailLocation;
	private TextView tvItemDetailQtyToReceive;
	private TextView tvReceiveItemDetailReceived;
	private TextView tvReceiveItemDetailLeft;
	private ListView lvReceiveItemDetail;
	private Button btReceiveItemDetailOK;
	private Button btReceiveItemDetailCanCel;
	private Button btReceiveItemDetailOption;
	private ImageView imgReceiveItemDetailInfo;
	
	//dialog
	private Dialog dialogOptions;
	private TextView tvDialogCancel;
	private TextView tvDialogOptionLable;
	private LinearLayout linLayoutPrint;
	private TextView tvPrintOption;
	private LinearLayout linLayoutDamage;
	private TextView tvDamageOption;
	private LinearLayout linLayoutUndo;
	private TextView tvUndoOption;
	private LinearLayout linLayoutMove;
	private TextView tvMoveOption;
	private ImageView imgPrintDialog;
	private ImageView imgDamageDialog;
	private ImageView imgUndoDialog;
	private ImageView imgMoveDialog;
	
	private EnPurchaseOrderInfo enPurchaseOrderInfo;
	private EnPurchaseOrderItemInfo enPurchaseOrderItemInfo;
	private ArrayList<EnPurchaseOrderReceiptInfo> listReceptInfo = new ArrayList<EnPurchaseOrderReceiptInfo>();
	private ReceivingItemDetailAdapter receivingItemDetailAdapter;
	private EnItemLotInfo enItemLotInfo;
	private LanguagePreferences languagePrefs;
	private boolean enableButtonOK = true;
	private boolean canCommitReceiptInfo = true;
	private boolean hadChangeFlag = false;
	private boolean canShowScanButton = true;
	private double itemQtyReceived;
	private double itemQtyLeft;
	private double itemQtyToReceive;
	
	//text multiple language
	private String textUOM;
	private String textLotTracked;
	private String textLocation;
	private String textLeft;
	private String textQtyToRecive;
	private String textRecv;
	private String strLoading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_receive_item_details);
		languagePrefs = new LanguagePreferences(getApplicationContext());
		
		Bundle bundle = getIntent().getExtras();
		
		if(bundle.containsKey(GlobalParams.PARAM_EN_PURCHASE_ORDER_ITEM_INFOS)){
			enPurchaseOrderItemInfo = (EnPurchaseOrderItemInfo) bundle.get(GlobalParams.PARAM_EN_PURCHASE_ORDER_ITEM_INFOS);
		}
		
		if(bundle.containsKey(GlobalParams.PARAM_EN_PURCHASE_ORDER_INFOS)){
			enPurchaseOrderInfo = (EnPurchaseOrderInfo) bundle.get(GlobalParams.PARAM_EN_PURCHASE_ORDER_INFOS);
		}
		
		if(bundle.containsKey(GlobalParams.PARAM_EN_ITEM_LOT_INFO)){
			enItemLotInfo = (EnItemLotInfo) bundle.get(GlobalParams.PARAM_EN_ITEM_LOT_INFO);
		}
		
		getMultipleLanguage();
		initLayout();
	}
	
	/**
	 * initial layout of screen
	 */
	public void initLayout(){
		linBack = (LinearLayout) findViewById(R.id.lin_buton_home);
		linBack.setVisibility(View.GONE);
		linScan = (LinearLayout) findViewById(R.id.lin_buton_scan);
		linScan.setOnClickListener(this);
		linScan.setVisibility(View.GONE);
		tvHeader = (TextView) findViewById(R.id.tv_header);
		tvHeader.setText(languagePrefs.getPreferencesString(GlobalParams.RID_TITLE_RECEIVE_ITEM_DETAIL_KEY, 
				GlobalParams.RID_TITLE_RECEIVE_ITEM_DETAIL_VALUE));
		tvReceiveItemDetailNumber = (TextView) findViewById(R.id.tvReceiveItemDetailNumber);
		imgReceiveItemDetailInfo = (ImageView) findViewById(R.id.imgReceiveItemDetailInfo);
		
		if(null != enPurchaseOrderInfo && StringUtils.isNotBlank(enPurchaseOrderInfo.get_poNumber())){
			String requestKey = languagePrefs.getPreferencesString( GlobalParams.DMG_LBL_RECEIVEREQUEST_KEY,
					GlobalParams.DMG_LBL_RECEIVEREQUEST_TEXT);
			tvReceiveItemDetailNumber.setText(requestKey + ": " + enPurchaseOrderInfo.get_poNumber());
			
		}
		
		if(null != enPurchaseOrderInfo && StringUtils.isNotBlank(enPurchaseOrderInfo.get_comments())){
			imgReceiveItemDetailInfo.setVisibility(View.VISIBLE);
			imgReceiveItemDetailInfo.setOnClickListener(this);
		} else {
			imgReceiveItemDetailInfo.setVisibility(View.GONE);
		}
		
		tvReceiveItemDetailName = (TextView) findViewById(R.id.tvReceiveItemDetailName);
		tvItemDetailLot = (TextView) findViewById(R.id.tv_item_detail_lot);
		tvItemDetailUom = (TextView) findViewById(R.id.tv_item_detail_uom);
		tvItemDetailLocation = (TextView) findViewById(R.id.tv_item_detail_location);
		tvItemDetailQtyToReceive = (TextView) findViewById(R.id.tv_item_detail_qty_to_receive);
		tvReceiveItemDetailReceived = (TextView) findViewById(R.id.tvReceiveItemDetailPicked);
		tvReceiveItemDetailLeft = (TextView) findViewById(R.id.tvReceiveItemDetailLeft);
		
		lvReceiveItemDetail = (ListView) findViewById(R.id.lvReceiveItemDetail);
		lvReceiveItemDetail.setOnItemClickListener(this);
		btReceiveItemDetailOK = (Button) findViewById(R.id.btn_receive_iem_detail_Ok);
		btReceiveItemDetailOK.setText(languagePrefs.getPreferencesString(GlobalParams.OK_KEY, GlobalParams.OK_KEY));
		btReceiveItemDetailOK.setOnClickListener(this);
		setButtonOkStatus(false);
		
		btReceiveItemDetailCanCel = (Button) findViewById(R.id.btn_receive_item_detail_Cancel);
		btReceiveItemDetailCanCel.setText(languagePrefs.getPreferencesString(
				GlobalParams.SETTINGS_BTN_CANCEL_KEY, GlobalParams.SETTINGS_BTN_CANCEL_VALUE));
		btReceiveItemDetailCanCel.setOnClickListener(this);
		
		btReceiveItemDetailOption = (Button) findViewById(R.id.btn_receive_item_detail_Option);
		btReceiveItemDetailOption.setText(languagePrefs.getPreferencesString(
				GlobalParams.PID_BTN_OPTIONS_KEY, GlobalParams.PID_BTN_OPTIONS_VALUE));
		btReceiveItemDetailOption.setOnClickListener(this);
		
		if(null != enPurchaseOrderInfo && StringUtils.isNotBlank(enPurchaseOrderInfo.get_receivingBinNumber())){
			tvItemDetailLocation.setText(textLocation + ":" + enPurchaseOrderInfo.get_receivingBinNumber());
		} else {
			tvItemDetailLocation.setText("");
		}
		
		if (null != enPurchaseOrderItemInfo) {
			listReceptInfo = enPurchaseOrderItemInfo.get_receipts();
			Gson gson = new Gson();
			String str = gson.toJson(listReceptInfo);
			Logger.error("AcReceiveItemDetail #listReceptInfo: " + str);
			
			if(StringUtils.isNotBlank(enPurchaseOrderItemInfo.get_itemDesc()) &&
					StringUtils.isNotBlank(enPurchaseOrderItemInfo.get_itemNumber())){
				tvReceiveItemDetailName.setText(enPurchaseOrderItemInfo.get_itemDesc() + "-" + enPurchaseOrderItemInfo.get_itemNumber());
			}
			
			if(enPurchaseOrderItemInfo.is_lotTrackingInd()){
				tvItemDetailLot.setText(textLotTracked);
			} else {
				tvItemDetailLot.setText("");
			}
			
			tvItemDetailUom.setText(textUOM + ": " + enPurchaseOrderItemInfo.get_uomDesc());
			itemQtyToReceive = enPurchaseOrderItemInfo.get_quantityOrdered();
			tvItemDetailQtyToReceive.setText(textQtyToRecive + ": " + BuManagement.formatDecimal(itemQtyToReceive).trim());
			itemQtyReceived = enPurchaseOrderItemInfo.get_quantityReceived();
			tvReceiveItemDetailReceived.setText(BuManagement.formatDecimal(itemQtyReceived).trim());
			
			itemQtyLeft = itemQtyToReceive - itemQtyReceived;
			if(itemQtyLeft < 0){
				itemQtyLeft = 0;
			}
			tvReceiveItemDetailLeft.setText(textLeft + ": " + 0);
			
			EnPurchaseOrderReceiptInfo enPurchaseOrderReceiptInfo = new EnPurchaseOrderReceiptInfo();
			enPurchaseOrderReceiptInfo.set_binNumber(enPurchaseOrderInfo.get_receivingBinNumber());
			
			if(null != enItemLotInfo){
				if(StringUtils.isNotBlank(enItemLotInfo.get_LotNumber())){
					enPurchaseOrderReceiptInfo.set_lotNumber(enItemLotInfo.get_LotNumber());
				}
				
				enPurchaseOrderReceiptInfo.set_itemID(enItemLotInfo.get_itemID());
				enPurchaseOrderReceiptInfo.set_purchaseOrderItemShipID(-1);
				enPurchaseOrderReceiptInfo.set_quantityReceived(enItemLotInfo.get_quantityOnHand());
				// fix bug : Bug #13020 - START 
				//remove quantity on hand
				//enPurchaseOrderReceiptInfo.set_quantityReceived(enItemLotInfo.get_quantityOnHand());
				// END
			} else {
				enPurchaseOrderReceiptInfo.set_purchaseOrderItemShipID(-1);
				enPurchaseOrderReceiptInfo.set_quantityReceived(itemQtyLeft);
			}
			
			listReceptInfo.add(enPurchaseOrderReceiptInfo);
			receivingItemDetailAdapter = new ReceivingItemDetailAdapter(this, enPurchaseOrderItemInfo, enPurchaseOrderInfo,  listReceptInfo);
			lvReceiveItemDetail.setAdapter(receivingItemDetailAdapter);
			
			//setup option dialog
			dialogOptions = CommontDialog.createDialogNoTitleCenter(this, R.layout.dialog_receve_item_details_option);
			tvDialogCancel = (TextView) dialogOptions.findViewById(R.id.tvDialogCancel);
			tvDialogCancel.setText(languagePrefs.getPreferencesString(GlobalParams.SETTINGS_BTN_CANCEL_KEY, GlobalParams.SETTINGS_BTN_CANCEL_VALUE));
			tvDialogCancel.setOnClickListener(this);
			tvDialogOptionLable = (TextView) dialogOptions.findViewById(R.id.tv_option_dialog_lable);
			tvDialogOptionLable.setText(languagePrefs.getPreferencesString(
					GlobalParams.PID_BTN_OPTIONS_KEY, GlobalParams.PID_BTN_OPTIONS_VALUE));
			
			linLayoutPrint = (LinearLayout) dialogOptions.findViewById(R.id.lin_layout_print);
			imgPrintDialog = (ImageView) dialogOptions.findViewById(R.id.img_print_labels);
			tvPrintOption = (TextView) dialogOptions.findViewById(R.id.tv_print_option);
			tvPrintOption.setText(languagePrefs.getPreferencesString(GlobalParams.RID_LBL_PRINTLABELS_KEY, GlobalParams.RID_LBL_PRINTLABELS_VALUE));
			linLayoutPrint.setOnClickListener(this);
			
			linLayoutDamage = (LinearLayout) dialogOptions.findViewById(R.id.lin_layout_damage);
			imgDamageDialog = (ImageView) dialogOptions.findViewById(R.id.img_damage_labels);
			tvDamageOption = (TextView) dialogOptions.findViewById(R.id.tv_damage_option);
			tvDamageOption.setText(languagePrefs.getPreferencesString(GlobalParams.DMG_TITLE_DAMAGE_KEY, GlobalParams.DMG_TITLE_DAMAGE_VALUE));
			linLayoutDamage.setOnClickListener(this);
			
			linLayoutUndo = (LinearLayout) dialogOptions.findViewById(R.id.lin_layout_undo);
			imgUndoDialog = (ImageView) dialogOptions.findViewById(R.id.img_undo_labels);
			tvUndoOption = (TextView) dialogOptions.findViewById(R.id.tv_undo_option);
			tvUndoOption.setText(languagePrefs.getPreferencesString(GlobalParams.PID_LBL_UNDO_KEY, GlobalParams.PID_LBL_UNDO_VALUE)); 
			linLayoutUndo.setOnClickListener(this);
			
			linLayoutMove = (LinearLayout) dialogOptions.findViewById(R.id.lin_layout_move);
			imgMoveDialog = (ImageView) dialogOptions.findViewById(R.id.img_move_labels);
			tvMoveOption = (TextView) dialogOptions.findViewById(R.id.tv_move_option);
			tvMoveOption.setText(languagePrefs.getPreferencesString(GlobalParams.RID_LBL_MOVE_KEY, GlobalParams.RID_LBL_MOVE_VALUE));
			linLayoutMove.setOnClickListener(this);
		}
	}
	
	/**
	 * getMultipleLanguage
	 */
	public void getMultipleLanguage(){
		textUOM = languagePrefs.getPreferencesString(GlobalParams.UNIT_OF_MEASURE_KEY, GlobalParams.UNIT_OF_MEASURE_VALUE);
		textLotTracked = languagePrefs.getPreferencesString(GlobalParams.RD_TXT_LOTTRACKEDIND_KEY, GlobalParams.RD_TXT_LOTTRACKEDIND_VALUE);
		textLocation = languagePrefs.getPreferencesString(GlobalParams.DMG_LBL_LOCATION_KEY, GlobalParams.DMG_LBL_LOCATION_VALUE);
		textLeft = languagePrefs.getPreferencesString(GlobalParams.RID_LBL_LEFT_KEY, GlobalParams.RID_LBL_LEFT_VALUE);
		textQtyToRecive = languagePrefs.getPreferencesString(GlobalParams.RID_LBL_QTY_TO_RECEIVE_KEY, GlobalParams.RID_LBL_QTY_TO_RECEIVE_VALUE);
		textRecv = languagePrefs.getPreferencesString(GlobalParams.RID_LBL_RECEIVED_KEY, GlobalParams.RID_LBL_RECEIVED_VALUE);
		strLoading = languagePrefs.getPreferencesString(GlobalParams.MAINLIST_MENLOADING_KEY, GlobalParams.MAINLIST_MENLOADING_VALUE);
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
	        	canShowScanButton = false;
	        }
	        
	        // a Scanner has disconnected
	        else if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_SCANNER_REMOVAL))
	        {
	        	linScan.setVisibility(View.VISIBLE);
	        	canShowScanButton = true;
	        }
	        
	        // decoded Data received from a scanner
	        else if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_DECODED_DATA))
	        {
				char[] data = intent.getCharArrayExtra(SingleEntryApplication.EXTRA_DECODEDDATA);
				String message = new String(data);
				receivingItemDetailAdapter.updateScanResult(message);
				//edtLp.setText(new String(data));
				//processScanData(message);
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
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(hadChangeFlag){
			setResult(RESULT_OK);
		} else {
			setResult(RESULT_CANCELED);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_receive_item_detail_Cancel:
			if(hadChangeFlag){
				setResult(RESULT_OK);
			} else {
				setResult(RESULT_CANCELED);
			}
			this.finish();
			break;
		
		case R.id.imgReceiveItemDetailInfo:
			if (null != enPurchaseOrderInfo && StringUtils.isNotBlank(enPurchaseOrderInfo.get_comments())) {
				CommontDialog.showErrorDialog(this, enPurchaseOrderInfo.get_comments(), null);
			}
			break;
			
		case R.id.btn_receive_iem_detail_Ok:
			String poNumber = enPurchaseOrderInfo.get_poNumber();
			int purchaseItemId = enPurchaseOrderItemInfo.get_purchaseOrderItemID();
			String binNumber = enPurchaseOrderInfo.get_receivingBinNumber();
			EnPurchaseOrderReceiptInfo receiptInfo = receivingItemDetailAdapter.getItem(listReceptInfo.size()-1);
			NetParameter[] netParameters = new NetParameter[8];
			netParameters[0] = new NetParameter("ponumber", poNumber);
			netParameters[1] = new NetParameter("purchaseOrderItemId", String.valueOf(purchaseItemId));
			netParameters[2] = new NetParameter("binnumber", binNumber);
			netParameters[3] = new NetParameter("quantity", String.valueOf(receiptInfo.get_quantityReceived()));
			netParameters[4] = new NetParameter("lotnumber", StringUtils.isNotBlank(receiptInfo.get_lotNumber()) ? receiptInfo.get_lotNumber() : "");
			netParameters[5] = new NetParameter("expirationDate", "1900-01-01");
			netParameters[6] = new NetParameter("gtinNumber", "");
			netParameters[7] = new NetParameter("originalBarcodeNumber", "");
			commitData(netParameters);
			break;
		
		case R.id.lin_buton_scan:
			Intent intent = new Intent(this, CaptureBarcodeCamera.class);
			intent.putExtra(GlobalParams.SCREEN_TO_SCREEN, GlobalParams.AC_RECEIVING_ITEM_DETAILS_ACTIVITY);
			startActivityForResult(intent, GlobalParams.CAPTURE_BARCODE_CAMERA_ACTIVITY);
			break;
		
		case R.id.btn_receive_item_detail_Option:
			if(null != dialogOptions){
				ArrayList<Integer> listPositionSelected = receivingItemDetailAdapter.getSelectedPosion();
				
				if(null == listPositionSelected || listPositionSelected.size() == 0 ){
					// if user do not choice item, default select top item
					receivingItemDetailAdapter.setSelectedPosition(0);
				}
				
				//check condition to configure option
				if(receivingItemDetailAdapter.getCount() > 1){
					if(listPositionSelected.size() == 1) {
						Log.e("Appolis", "configLayoutOption: false true true true");
						configLayoutOption(false, true, true, true);
					} else {
						Log.e("Appolis", "configLayoutOption: false true true true");
						configLayoutOption(false, false, true, true);
					}
				} else {
					// Adapter have only one item
					EnPurchaseOrderReceiptInfo receiptInfoLast = receivingItemDetailAdapter.getLastItem();
					if (enPurchaseOrderItemInfo.is_lotTrackingInd()){
						if(null == receiptInfoLast || (StringUtils.isBlank(receiptInfoLast.get_lotNumber()))
								|| (receiptInfoLast.get_quantityReceived() <= 0)){
							Log.e("Appolis", "configLayoutOption: false false false false");
							configLayoutOption(false, false, false, false);
						} else {
							Log.e("Appolis", "configLayoutOption: false true true true");
							configLayoutOption(false, true, true, true);
						}
					} else {
						if(null == receiptInfoLast || (receiptInfoLast.get_quantityReceived() <= 0)){
							Log.e("Appolis", "configLayoutOption: false false false false");
							configLayoutOption(false, false, false, false);
						} else {
							Log.e("Appolis", "configLayoutOption: false true true true");
							configLayoutOption(false, true, true, true);
						}
					}
				}
				
				dialogOptions.show();
			}
			break;
		
		case R.id.tvDialogCancel:
			if(null != dialogOptions){
				Log.e("Appolis", "Dialog cancel #Cancel");
				dialogOptions.dismiss();
			}
			break;
		
		case R.id.lin_layout_print:
			if(null != dialogOptions){
				Log.e("Appolis", "Dialog cancel #lin_layout_print");
				dialogOptions.dismiss();
			}
			break;
		
		case R.id.lin_layout_damage:
			if(null != dialogOptions){
				Log.e("Appolis", "Dialog cancel #lin_layout_damage");
				dialogOptions.dismiss();
				processDamageClick();
			}
			break;
			
		case R.id.lin_layout_undo:
			if(null != dialogOptions){
				Log.e("Appolis", "Dialog cancel #lin_layout_damage");
				dialogOptions.dismiss();
				createDialogConfirmUndo();
			}
			break;
		
		case R.id.lin_layout_move:
			if(null != dialogOptions){
				Log.e("Appolis", "Dialog cancel #lin_layout_move");
				dialogOptions.dismiss();
				processMoveClick();
			}
			break;
			
		default:
			break;
		}
	}
	
	/**
	 * process when user click damage function
	 */
	public void processDamageClick(){
		EnPurchaseOrderReceiptInfo receiptInfoLast = receivingItemDetailAdapter.getLastItem();
		
		if(null != receiptInfoLast){
			if(enPurchaseOrderItemInfo.is_lotTrackingInd() && StringUtils.isBlank(receiptInfoLast.get_lotNumber())){
				String msgs = languagePrefs.getPreferencesString(GlobalParams.MISS_LOT_MSG_KEY, GlobalParams.MISS_LOT_MSG_VALUE);
				CommontDialog.showErrorDialog(AcReceiveItemDetail.this, msgs, null);
			} else if(receiptInfoLast.get_quantityReceived() <=0){
				String msgs = languagePrefs.getPreferencesString(GlobalParams.RECEIVINGPO_LBLENTERQTYRECEIVED_KEY, 
						GlobalParams.RECEIVINGPO_LBLENTERQTYRECEIVED_VALUE);
				CommontDialog.showErrorDialog(AcReceiveItemDetail.this, msgs, null);
			} else {
				//Process Damage: commit data -> damage
				EnPurchaseOrderReceiptInfo receiptInfoSelected = receivingItemDetailAdapter.getListReceiptSelected().get(0);
				
				if(canCommitReceiptInfo){
					// last item don't commit yet -> commit data and go to damage screen
					String poDamageNumber = enPurchaseOrderInfo.get_poNumber();
					int purchaseItemIdDamage = enPurchaseOrderItemInfo.get_purchaseOrderItemID();
					String binNumberDamage = enPurchaseOrderInfo.get_receivingBinNumber();
					NetParameter[] netParametersDamage = new NetParameter[8];
					netParametersDamage[0] = new NetParameter("ponumber", poDamageNumber);
					netParametersDamage[1] = new NetParameter("purchaseOrderItemId", String.valueOf(purchaseItemIdDamage));
					netParametersDamage[2] = new NetParameter("binnumber", binNumberDamage);
					netParametersDamage[3] = new NetParameter("quantity", String.valueOf(receiptInfoLast.get_quantityReceived()));
					netParametersDamage[4] = new NetParameter("lotnumber", StringUtils.isNotBlank(receiptInfoLast.get_lotNumber()) ? receiptInfoLast.get_lotNumber() : "");
					netParametersDamage[5] = new NetParameter("expirationDate", "1900-01-01");
					netParametersDamage[6] = new NetParameter("gtinNumber", "");
					netParametersDamage[7] = new NetParameter("originalBarcodeNumber", "");
					commitData(netParametersDamage, receiptInfoSelected);
				} else {
					// last item had commited, don't commit, go to damage screen
					if(receiptInfoSelected.get_purchaseOrderItemShipID() < 0){
						EnPurchaseOrderReceiptInfo newObjectReceipt = getEnPurchaseOrderReceiptInfoHaveJustCommited(listReceptInfo);
						Intent intentDamage = new Intent(AcReceiveItemDetail.this, AcReceiveOptionDamage.class);
						intentDamage.putExtra(GlobalParams.PARAM_EN_PURCHASE_ORDER_INFOS, enPurchaseOrderInfo);
						intentDamage.putExtra(GlobalParams.PARAM_EN_PURCHASE_ORDER_ITEM_INFOS, enPurchaseOrderItemInfo);
						intentDamage.putExtra(GlobalParams.PARAM_EN_PURCHASE_ORDER_RECEIPT_INFO, newObjectReceipt);
						startActivityForResult(intentDamage, GlobalParams.AC_RECEIVE_OPTION_DAMAGE_ACTIVITY);
					} else {
						Intent intentDamage = new Intent(AcReceiveItemDetail.this, AcReceiveOptionDamage.class);
						intentDamage.putExtra(GlobalParams.PARAM_EN_PURCHASE_ORDER_INFOS, enPurchaseOrderInfo);
						intentDamage.putExtra(GlobalParams.PARAM_EN_PURCHASE_ORDER_ITEM_INFOS, enPurchaseOrderItemInfo);
						intentDamage.putExtra(GlobalParams.PARAM_EN_PURCHASE_ORDER_RECEIPT_INFO, receiptInfoSelected);
						startActivityForResult(intentDamage, GlobalParams.AC_RECEIVE_OPTION_DAMAGE_ACTIVITY);
					}
				}
			}
		} else {
			Log.e("Appolis", "#lin_layout_damage receiptInfoLast NULL");
		}
	}
	
	/**
	 * process when user click move function
	 */
	public void processMoveClick(){
		EnPurchaseOrderReceiptInfo receiptInfoLast = receivingItemDetailAdapter.getLastItem();
		if(null != receiptInfoLast){
			if(enPurchaseOrderItemInfo.is_lotTrackingInd() && StringUtils.isBlank(receiptInfoLast.get_lotNumber())){
				String msgs = languagePrefs.getPreferencesString(GlobalParams.MISS_LOT_MSG_KEY, GlobalParams.MISS_LOT_MSG_VALUE);
				CommontDialog.showErrorDialog(AcReceiveItemDetail.this, msgs, null);
			} else if(receiptInfoLast.get_quantityReceived() <=0){
				String msgs = languagePrefs.getPreferencesString(GlobalParams.RECEIVINGPO_LBLENTERQTYRECEIVED_KEY, 
						GlobalParams.RECEIVINGPO_LBLENTERQTYRECEIVED_VALUE);
				CommontDialog.showErrorDialog(AcReceiveItemDetail.this, msgs, null);
			} else {
				//Process Damage: commit data -> move
				ArrayList<EnPurchaseOrderReceiptInfo> ListReceiptInfoSelected = receivingItemDetailAdapter.getListReceiptSelected();
				
				if(canCommitReceiptInfo){
					//last item not commit -> commit item and go to move screen
					String poDamageNumber = enPurchaseOrderInfo.get_poNumber();
					int purchaseItemIdDamage = enPurchaseOrderItemInfo.get_purchaseOrderItemID();
					String binNumberDamage = enPurchaseOrderInfo.get_receivingBinNumber();
					NetParameter[] netParametersDamage = new NetParameter[8];
					netParametersDamage[0] = new NetParameter("ponumber", poDamageNumber);
					netParametersDamage[1] = new NetParameter("purchaseOrderItemId", String.valueOf(purchaseItemIdDamage));
					netParametersDamage[2] = new NetParameter("binnumber", binNumberDamage);
					netParametersDamage[3] = new NetParameter("quantity", String.valueOf(receiptInfoLast.get_quantityReceived()));
					netParametersDamage[4] = new NetParameter("lotnumber", StringUtils.isNotBlank(receiptInfoLast.get_lotNumber()) ? receiptInfoLast.get_lotNumber() : "");
					netParametersDamage[5] = new NetParameter("expirationDate", "1900-01-01");
					netParametersDamage[6] = new NetParameter("gtinNumber", "");
					netParametersDamage[7] = new NetParameter("originalBarcodeNumber", "");
					commitData(netParametersDamage, ListReceiptInfoSelected);
				} else {
					Intent intentMove = new Intent(AcReceiveItemDetail.this, AcReceiveOptionMove.class);
					intentMove.putExtra(GlobalParams.PARAM_EN_PURCHASE_ORDER_INFOS, enPurchaseOrderInfo);
					intentMove.putExtra(GlobalParams.PARAM_EN_PURCHASE_ORDER_ITEM_INFOS, enPurchaseOrderItemInfo);
					intentMove.putExtra(GlobalParams.PARAM_LIST_EN_PURCHASE_ORDER_RECEIPT_INFO, ListReceiptInfoSelected);
					startActivityForResult(intentMove, GlobalParams.AC_RECEIVE_OPTION_MOVE_ACTIVITY);
				}
			}
		} else {
			//do nothing
			Log.e("Appolis", "#lin_layout_damage receiptInfoLast NULL");
		}
	}
	
	@Override
	public void onItemClick(android.widget.AdapterView<?> parentView, View view, int position, long id) {
		Log.e("Appolis", "AcReceiveItemDetail #onItemClick:" + position);
		receivingItemDetailAdapter.setSelectedPosition(position);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case GlobalParams.CAPTURE_BARCODE_CAMERA_ACTIVITY:
			String  message = data.getStringExtra(GlobalParams.RESULTSCAN);
			if(resultCode == RESULT_OK){
				Log.e("Uniphy", "#onActivityResult OK");
				receivingItemDetailAdapter.updateScanResult(message);
			} else {
				Log.e("Uniphy", "#onActivityResult CANCEL");
			}
			break;
			
		case GlobalParams.AC_RECEIVE_OPTION_DAMAGE_ACTIVITY:
			if(resultCode == RESULT_OK){
				setResult(RESULT_OK);
				finish();
			} else {
				enableButtonOK = false;
				setButtonOkStatus(enableButtonOK);
				receivingItemDetailAdapter.setEnableButtonOk(enableButtonOK);
			}
			break;
			
		case GlobalParams.AC_RECEIVE_OPTION_MOVE_ACTIVITY:
			if(resultCode == RESULT_OK){
				setResult(RESULT_OK);
				finish();
			} else {
				enableButtonOK = false;
				setButtonOkStatus(enableButtonOK);
				receivingItemDetailAdapter.setEnableButtonOk(false);
				receivingItemDetailAdapter.notifyDataSetChanged();
			}
			break;
			
		default:
			break;
		}
	}
	
	public void createDialogConfirmUndo(){
		String vendorName = "";
		if(StringUtils.isNotBlank(enPurchaseOrderItemInfo.get_itemDesc()) &&
				StringUtils.isNotBlank(enPurchaseOrderItemInfo.get_itemNumber())){
			vendorName = enPurchaseOrderItemInfo.get_itemDesc() + "-" + enPurchaseOrderItemInfo.get_itemNumber();
		}
		
		String lot = "";
		double quantity = 0;
		int index = 0;
		
		ArrayList<EnPurchaseOrderReceiptInfo> listReceift = receivingItemDetailAdapter.getListReceiptSelected();
		for (int i = 0; i < listReceift.size(); i++) {
			EnPurchaseOrderReceiptInfo enPurchaseOrderReceiptInfo = listReceift.get(i);
			if(StringUtils.isNotBlank(enPurchaseOrderReceiptInfo.get_lotNumber())){
				if(index == 0){
					lot = lot + enPurchaseOrderReceiptInfo.get_lotNumber();
				} else {
					lot = lot + ", " + enPurchaseOrderReceiptInfo.get_lotNumber();
				}
				index ++;
			}
			
			quantity += enPurchaseOrderReceiptInfo.get_quantityReceived();
		}
		
		DialogInterface.OnClickListener onOkClickListener = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Log.e("Uniphy", "Undo item");
				ReceiveClikUndoAsyn receiveClikUndoAsyn = new ReceiveClikUndoAsyn(AcReceiveItemDetail.this);
				receiveClikUndoAsyn.execute();
			}
		};
		
		String message = languagePrefs.getPreferencesString(GlobalParams.RID_UNRECEIPT_ALERT_MSG_KEY, GlobalParams.RID_UNRECEIPT_ALERT_MSG_VALUE);
		message = MessageFormat.format(message, "\n" + vendorName + "\n" , lot + "\n", quantity + "?");
		CommontDialog.createConfirmAlerDialog(AcReceiveItemDetail.this, "Undo", message, "OK", "Cancel", onOkClickListener, null);
	}
	
	/**
	 * function to enable or disable options
	 * @param enalblePrint enable/disable print function
	 * @param enableDamage enable/disable damage function
	 * @param enableUndo enable/disable undo function
	 * @param enableMove enable/disable move function
	 */
	public void configLayoutOption(boolean enalblePrint, boolean enableDamage, boolean enableUndo, boolean enableMove){
		if(enalblePrint){
			linLayoutPrint.setEnabled(true);
			imgPrintDialog.setImageResource(R.drawable.ic_print_labels_pressed);
		} else {
			linLayoutPrint.setEnabled(false);
			imgPrintDialog.setImageResource(R.drawable.ic_print_labels);
		}
		
		if(enableDamage){
			linLayoutDamage.setEnabled(true);
			imgDamageDialog.setImageResource(R.drawable.ic_damage_pressed);
		} else {
			linLayoutDamage.setEnabled(false);
			imgDamageDialog.setImageResource(R.drawable.ic_damage);
		}
		
		if(enableUndo){
			linLayoutUndo.setEnabled(true);
			imgUndoDialog.setImageResource(R.drawable.ic_undo_pressed);
		} else {
			linLayoutUndo.setEnabled(false);
			imgUndoDialog.setImageResource(R.drawable.ic_undo);
		}
		
		if(enableMove){
			linLayoutMove.setEnabled(true);
			imgMoveDialog.setImageResource(R.drawable.ic_move_pressed);
		} else {
			linLayoutMove.setEnabled(false);
			imgMoveDialog.setImageResource(R.drawable.ic_move);
		}
	}
	
	/**
	 * function to update recv field and left field
	 * @param receiveChane
	 */
	public void updateRecvAndLeftField(double receiveChane){
		double recv = itemQtyReceived + receiveChane;
		itemQtyLeft = itemQtyToReceive - recv;
		if(itemQtyLeft < 0){
			itemQtyLeft = 0;
		}
		tvReceiveItemDetailReceived.setText(textRecv + ": " + BuManagement.formatDecimal(recv).trim());
		tvReceiveItemDetailLeft.setText(textLeft + ": " + BuManagement.formatDecimal(itemQtyLeft).trim());
	}
	
	/**
	 * getQtyLeft
	 * @return
	 */
	public double getQtyLeft(){
		return itemQtyLeft;
	}
	
	public double getItemQtyReceived() {
		return itemQtyReceived;
	}

	public void setItemQtyReceived(double itemQtyReceived) {
		this.itemQtyReceived = itemQtyReceived;
	}

	public double getItemQtyToReceive() {
		return itemQtyToReceive;
	}

	public void setItemQtyToReceive(double itemQtyToReceive) {
		this.itemQtyToReceive = itemQtyToReceive;
	}

	/**
	 * set enable or disable button OK
	 * @param state state of button
	 */
	public void setButtonOkStatus(boolean state){
		btReceiveItemDetailOK.setEnabled(state);
	}
	
	/**
	 * showButtonScan
	 * @param state state is VISIBLE or INVISIBLE
	 */
	public void showButtonScan(boolean state){
		if(canShowScanButton && state){
			linScan.setVisibility(View.VISIBLE);
		} else {
			linScan.setVisibility(View.INVISIBLE);
		}
	}
	
	public boolean isCanShowScanButton() {
		return canShowScanButton;
	}

	public void setCanShowScanButton(boolean canShowScanButton) {
		this.canShowScanButton = canShowScanButton;
	}
	
	public EnPurchaseOrderReceiptInfo getEnPurchaseOrderReceiptInfoHaveJustCommited(ArrayList<EnPurchaseOrderReceiptInfo> listReceptInfoArry){
		EnPurchaseOrderReceiptInfo enPurchaseOrderReceiptInfo = null;
		ArrayList<Integer> listReceiptHaveId = receivingItemDetailAdapter.getListPurchaseOrderItemId();
		for (EnPurchaseOrderReceiptInfo item : listReceptInfoArry) {
			if(!listReceiptHaveId.contains(item.get_purchaseOrderItemShipID())){
				enPurchaseOrderReceiptInfo = item;
				break;
			}
		}
		
		return enPurchaseOrderReceiptInfo;
	}
	/**
	 * commit item details
	 * @param netParameters
	 */
	private void commitData(NetParameter[] netParameters) {
		ReceiveClikOKAsyn mLoadDataTask = new ReceiveClikOKAsyn(AcReceiveItemDetail.this, netParameters);
		//mLoadDataTask.setMoveTopOnComplete(isMoveToTop);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mLoadDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			mLoadDataTask.execute();
		}
	}
	
	/**
	 * commit item details
	 * @param netParameters
	 */
	private void commitData(NetParameter[] netParameters, EnPurchaseOrderReceiptInfo receiptInfoSelected) {
		ReceiveClikOKAsyn mLoadDataTask = new ReceiveClikOKAsyn(AcReceiveItemDetail.this, netParameters, receiptInfoSelected);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mLoadDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			mLoadDataTask.execute();
		}
	}
	
	/**
	 * commit item details
	 * @param netParameters
	 */
	private void commitData(NetParameter[] netParameters, ArrayList<EnPurchaseOrderReceiptInfo> listReceiptInfoSelected) {
		ReceiveClikOKAsyn mLoadDataTask = new ReceiveClikOKAsyn(AcReceiveItemDetail.this, netParameters, listReceiptInfoSelected);

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
		EnPurchaseOrderReceiptInfo receiptInfoSelectedAsyn;
		ArrayList<EnPurchaseOrderReceiptInfo> listReceiptSelectedAsyn;
		
		public ReceiveClikOKAsyn(Context mContext, NetParameter[] netParameters){
			this.context = mContext;
			this.netParameters = netParameters;
			this.receiptInfoSelectedAsyn = null;
			this.listReceiptSelectedAsyn = null;
		}
		
		public ReceiveClikOKAsyn(Context mContext, NetParameter[] netParameters, EnPurchaseOrderReceiptInfo receiptInfoSelected){
			this.context = mContext;
			this.netParameters = netParameters;
			this.receiptInfoSelectedAsyn = receiptInfoSelected;
			this.listReceiptSelectedAsyn = null;
		}
		
		public ReceiveClikOKAsyn(Context mContext, NetParameter[] netParameters2, ArrayList<EnPurchaseOrderReceiptInfo> listReceiptInfoSelected) {
			this.context = mContext;
			this.netParameters = netParameters2;
			this.listReceiptSelectedAsyn = listReceiptInfoSelected;
			this.receiptInfoSelectedAsyn = null;
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
					Log.e("Appolis", "################## ReceiveClikOKAsyn : SATRT #########################");
					for (NetParameter net : netParameters) {
						Log.e("Appolis", "key: " + net.getName() + "-" + "value: " + net.getValue());
					}
					response = HttpNetServices.Instance.commitReceiveItemDetail(netParameters);
					Log.e("Appolis", "ReceiveClikOKAsyn #response:" + response);
					listReceptInfo  = DataParser.getListEnPurchaseOrderReceiptInfo(response);
					Log.e("Appolis", "################## ReceiveClikOKAsyn : END #########################");
					//listReceiveInfo = DataParser.getListReceiveInfo(response);
				}
			} catch (Exception e) {
				e.printStackTrace();
				errorCode = 2;
				if(e instanceof JsonSyntaxException){
					errorCode = ErrorCode.STATUS_JSON_EXCEPTION;
				}
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
					canCommitReceiptInfo = false;
					hadChangeFlag = true;
					if(null == receiptInfoSelectedAsyn && null == listReceiptSelectedAsyn){
						setResult(RESULT_OK);
						finish();
					} else if(null != receiptInfoSelectedAsyn){
						if(receiptInfoSelectedAsyn.get_purchaseOrderItemShipID() < 0){
							//this is item that user enter lot and quantity
							EnPurchaseOrderReceiptInfo itemReceipt = getEnPurchaseOrderReceiptInfoHaveJustCommited(listReceptInfo);
							Intent intentDamage = new Intent(AcReceiveItemDetail.this, AcReceiveOptionDamage.class);
							intentDamage.putExtra(GlobalParams.PARAM_EN_PURCHASE_ORDER_INFOS, enPurchaseOrderInfo);
							intentDamage.putExtra(GlobalParams.PARAM_EN_PURCHASE_ORDER_ITEM_INFOS, enPurchaseOrderItemInfo);
							intentDamage.putExtra(GlobalParams.PARAM_EN_PURCHASE_ORDER_RECEIPT_INFO, itemReceipt);
							startActivityForResult(intentDamage, GlobalParams.AC_RECEIVE_OPTION_DAMAGE_ACTIVITY);
						} else{
							Intent intentDamage = new Intent(AcReceiveItemDetail.this, AcReceiveOptionDamage.class);
							intentDamage.putExtra(GlobalParams.PARAM_EN_PURCHASE_ORDER_INFOS, enPurchaseOrderInfo);
							intentDamage.putExtra(GlobalParams.PARAM_EN_PURCHASE_ORDER_ITEM_INFOS, enPurchaseOrderItemInfo);
							intentDamage.putExtra(GlobalParams.PARAM_EN_PURCHASE_ORDER_RECEIPT_INFO, receiptInfoSelectedAsyn);
							startActivityForResult(intentDamage, GlobalParams.AC_RECEIVE_OPTION_DAMAGE_ACTIVITY);
						}
					} else {
						Intent intentMove = new Intent(AcReceiveItemDetail.this, AcReceiveOptionMove.class);
						intentMove.putExtra(GlobalParams.PARAM_EN_PURCHASE_ORDER_INFOS, enPurchaseOrderInfo);
						intentMove.putExtra(GlobalParams.PARAM_EN_PURCHASE_ORDER_ITEM_INFOS, enPurchaseOrderItemInfo);
						intentMove.putExtra(GlobalParams.PARAM_LIST_EN_PURCHASE_ORDER_RECEIPT_INFO, listReceiptSelectedAsyn);
						startActivityForResult(intentMove, GlobalParams.AC_RECEIVE_OPTION_MOVE_ACTIVITY);
					}
					
					break;
				
				case 1: //no network
					String msg = languagePrefs.getPreferencesString(GlobalParams.ERRORUNABLETOCONTACTSERVER, GlobalParams.ERROR_INVALID_NETWORK);
					CommontDialog.showErrorDialog(context, msg, null);
					break;
					
				default:
					String msgs = languagePrefs.getPreferencesString("Error", GlobalParams.ERROR_INVALID_NETWORK);
					CommontDialog.showErrorDialog(context, msgs, null);
					Log.e("Appolis", "LoadReceiveListAsyn #onPostExecute: " + result);
					break;
				}
			}
		}
	}
	
	/**
	 * ReceiveClikOKAsyn
	 * @author HUNGLV1
	 *
	 */
	private class ReceiveClikUndoAsyn extends AsyncTask<Void, Void, Integer>{
		Context context;
		ProgressDialog progressDialog;
		String response;
		String netParameters;
		
		public ReceiveClikUndoAsyn(Context mContext){
			this.context = mContext;
			
			String removeList = "";
			int indexRemoList = 0;
			ArrayList<EnPurchaseOrderReceiptInfo> listReceift = receivingItemDetailAdapter.getListReceiptSelectedWithOutLastItem();
			
			for (int i = 0; i < listReceift.size(); i++) {
				EnPurchaseOrderReceiptInfo enPurchaseOrderReceiptInfo = listReceift.get(i);
				
				if(indexRemoList == 0){
					removeList = removeList + enPurchaseOrderReceiptInfo.get_purchaseOrderItemShipID();
				} else {
					removeList = removeList +  "," + enPurchaseOrderReceiptInfo.get_purchaseOrderItemShipID();
				}
				indexRemoList++;
			}
			
			removeList = "[" + removeList + "]";
			this.netParameters = removeList;
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
					Log.e("Appolis", "################## ReceiveClikUnDoAsyn : SATRT #########################");
					response = HttpNetServices.Instance.undoReceiveItemDetail(netParameters);
					Log.e("Appolis", "ReceiveClikOKAsyn #response:" + response);
					if(StringUtils.isNotBlank(response)){
						errorCode = ErrorCode.STATUS_FAIL;
					}
					Log.e("Appolis", "################## ReceiveClikUnDoAsyn : END #########################");
					//listReceiveInfo = DataParser.getListReceiveInfo(response);
				}
			} catch (Exception e) {
				e.printStackTrace();
				errorCode = ErrorCode.STATUS_EXCEPTION;
			}
			
			return errorCode;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(null != progressDialog && (progressDialog.isShowing())){
				progressDialog.dismiss();
			}
			
			if(!isCancelled()){
				switch (result) {
				case 0: //success
					setResult(RESULT_OK);
					finish();
					break;
				
				case ErrorCode.STATUS_FAIL: //undo fails
					String msgFail = response;
					CommontDialog.showErrorDialog(context, msgFail, null);
					break;
					
				case 1: //no network
					String msg = languagePrefs.getPreferencesString(GlobalParams.ERRORUNABLETOCONTACTSERVER, GlobalParams.ERROR_INVALID_NETWORK);
					CommontDialog.showErrorDialog(context, msg, null);
					break;
					
				default:
					String msgs = languagePrefs.getPreferencesString("Error", GlobalParams.ERROR_INVALID_NETWORK);
					CommontDialog.showErrorDialog(context, msgs, null);
					Log.e("Appolis", "LoadReceiveListAsyn #onPostExecute: " + result);
					break;
				}
			}
		}
	}
}
