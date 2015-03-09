/**
 * Name: $RCSfile: ReceivingDetailAdapter.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appolis.androidtablet.R;
import com.appolis.common.LanguagePreferences;
import com.appolis.entities.EnPurchaseOrderInfo;
import com.appolis.entities.EnPurchaseOrderItemInfo;
import com.appolis.entities.EnPurchaseOrderReceiptInfo;
import com.appolis.receiving.AcReceiveAcquireBarcode;
import com.appolis.receiving.AcReceivingDetails;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.Logger;
import com.appolis.utilities.StringUtils;

/**
 * 
 * @author ThinDV
 * Process data for list view
 */
public class ReceivingDetailAdapter extends ArrayAdapter<EnPurchaseOrderItemInfo> {
	private Context context;
	private ArrayList<EnPurchaseOrderItemInfo> listPurchaseOrderInfos;
	private EnPurchaseOrderInfo enPurchaseOrderInfo;
	private LanguagePreferences languagePrefs;
	
	//text multiple language
	private String strLot;
	private String strBin;
	private String srtDamage;
	private String lotTracked;
	
	/**
	 * constructor
	 * @param context
	 * @param list
	 * @param enPurchaseOrderInfo
	 */
	public ReceivingDetailAdapter(Context context, ArrayList<EnPurchaseOrderItemInfo> list, EnPurchaseOrderInfo enPurchaseOrderInfo) {
		super(context, R.layout.receive_detail_item_with_swipe);
		this.context = context;
		this.listPurchaseOrderInfos = list;
		this.enPurchaseOrderInfo = enPurchaseOrderInfo;
		languagePrefs = new LanguagePreferences(context.getApplicationContext());
		getLanguage();
	}
	
	/**
	 * get language from language package
	 */
	public void getLanguage(){
		strLot = languagePrefs.getPreferencesString(GlobalParams.RD_LBL_LOTS_KEY, GlobalParams.RD_LBL_LOTS_VALUE);
		lotTracked = languagePrefs.getPreferencesString(GlobalParams.RD_TXT_LOTTRACKEDIND_KEY, GlobalParams.RD_TXT_LOTTRACKEDIND_VALUE);
		srtDamage = languagePrefs.getPreferencesString(GlobalParams.DAMAGED_KEY, GlobalParams.DAMAGED_VALUE);
		strBin = languagePrefs.getPreferencesString(GlobalParams.BIN_KEY, GlobalParams.BIN_KEY);
	}
	
	@Override
	public int getCount() {
		if(null == listPurchaseOrderInfos){
			return 0;
		}
		
		return listPurchaseOrderInfos.size();
	}
	
	@Override
	public EnPurchaseOrderItemInfo getItem(int position) {
		if(null == listPurchaseOrderInfos){
			return null;
		}
		
		return listPurchaseOrderInfos.get(position);
	}
	
	/**
	 * update list
	 * @param list
	 */
	public void updateListReciver(ArrayList<EnPurchaseOrderItemInfo> list){
		if(null != list){
			this.listPurchaseOrderInfos = list;
		}
	}
	
	/**
	 * updateEnPuscharInfo
	 * @param enPurchaseOrderInfo
	 */
	public void updateEnPuscharInfo(EnPurchaseOrderInfo enPurchaseOrderInfo){
		this.enPurchaseOrderInfo = enPurchaseOrderInfo;
	}
	
	private class ReceivingDetailHolder{
		TextView tvReceiveItemNumber;
		TextView tvReceiveItemWeight;
		TextView tvReceiveItemBin;
		TextView tvReceiveItemBinType;
		TextView tvReceiveItemLot;
		TextView tvReceiveItemLotWeight;
		TextView tvReceiveItemExpDate;
		TextView tvReceiveItemDamaged;
		Button swipeBtAcquireBarcode;
		LinearLayout linItemFront;
	}
	
	@SuppressLint("InflateParams") 
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ReceivingDetailHolder receivedetailHolder;
		
		if(null == convertView){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.receive_detail_item_with_swipe, null);
			receivedetailHolder = new ReceivingDetailHolder();
			receivedetailHolder.tvReceiveItemNumber = (TextView) convertView.findViewById(R.id.tv_receive_item_number);
			receivedetailHolder.tvReceiveItemWeight = (TextView) convertView.findViewById(R.id.tv_receive_item_weight);
			receivedetailHolder.tvReceiveItemBin = (TextView) convertView.findViewById(R.id.tv_receive_item_bin);
			receivedetailHolder.tvReceiveItemBinType = (TextView) convertView.findViewById(R.id.tv_receive_item_uom_type);
			receivedetailHolder.tvReceiveItemLot = (TextView) convertView.findViewById(R.id.tv_receive_item_lot);
			receivedetailHolder.tvReceiveItemLotWeight = (TextView) convertView.findViewById(R.id.tv_receive_item_lot_weight);
			receivedetailHolder.tvReceiveItemExpDate = (TextView) convertView.findViewById(R.id.tv_receive_item_exp_date);
			receivedetailHolder.tvReceiveItemDamaged = (TextView) convertView.findViewById(R.id.tv_damaged);
			receivedetailHolder.swipeBtAcquireBarcode = (Button) convertView.findViewById(R.id.swipeBtAcquireBarcode);
			receivedetailHolder.linItemFront = (LinearLayout) convertView.findViewById(R.id.front);
			convertView.setTag(receivedetailHolder);
		} else {
			receivedetailHolder = (ReceivingDetailHolder) convertView.getTag();
		}
		
		EnPurchaseOrderItemInfo item = getItem(position);
		if(null != item){
			if(null != item.get_itemDesc() && StringUtils.isNotBlank(item.get_itemNumber())){
				receivedetailHolder.tvReceiveItemNumber.setText(item.get_itemDesc() + " - " + item.get_itemNumber());
			}
			receivedetailHolder.swipeBtAcquireBarcode.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Logger.error("Appolis click aquire barcode:" + position);
					if(context instanceof AcReceivingDetails){
						((AcReceivingDetails)context).closeAnimation();
					}
					
					Intent intentAcquire = new Intent(context, AcReceiveAcquireBarcode.class);
					intentAcquire.putExtra(GlobalParams.PARAM_EN_PURCHASE_ORDER_ITEM_INFOS, getItem(position));
					((Activity)context).startActivityForResult(intentAcquire, GlobalParams.AC_RECEIVE_ACQUIRE_BARCODE);
				}
			});
			
			receivedetailHolder.tvReceiveItemWeight.setText(String.format("%.2f", item.get_quantityOrdered()));
			ArrayList<EnPurchaseOrderReceiptInfo> listReceptInfo = item.get_receipts();
			String lotNumber = "";
			String expDate = "";
			int lotIndex = 0;
			for (EnPurchaseOrderReceiptInfo enPurchaseOrderReceiptInfo : listReceptInfo) {
				
				if(StringUtils.isNotBlank(enPurchaseOrderReceiptInfo.get_lotNumber())
						&& !lotNumber.contains(enPurchaseOrderReceiptInfo.get_lotNumber())){
					String itemLot = enPurchaseOrderReceiptInfo.get_lotNumber();
					if(lotIndex == 0){
						lotNumber = itemLot;
					} else {
						lotNumber = lotNumber + ", " + itemLot;
					}
					lotIndex++;
				}
				
				if(StringUtils.isNotBlank(enPurchaseOrderReceiptInfo.get_expirationDate())){
					expDate = expDate + enPurchaseOrderReceiptInfo.get_expirationDate() + ", ";
				}
			}
			
			if(null != enPurchaseOrderInfo && StringUtils.isNotBlank(enPurchaseOrderInfo.get_receivingBinNumber())){
				receivedetailHolder.tvReceiveItemBin.setText(strBin + ":" + enPurchaseOrderInfo.get_receivingBinNumber());
			} else {
				receivedetailHolder.tvReceiveItemBin.setText("");
			}
			
			if(StringUtils.isNotBlank(lotNumber)){
				receivedetailHolder.tvReceiveItemLot.setText(strLot + ": " + lotNumber);
			} else {
				if(item.is_lotTrackingInd()){
					receivedetailHolder.tvReceiveItemLot.setText(lotTracked);
				} else {
					receivedetailHolder.tvReceiveItemLot.setText("");
				}
			}
			
			receivedetailHolder.tvReceiveItemExpDate.setVisibility(View.INVISIBLE);
			receivedetailHolder.tvReceiveItemWeight.setText(String.format("%.2f", item.get_quantityOrdered()));
			receivedetailHolder.tvReceiveItemBinType.setText(item.get_uomDesc());
		
			if(item.get_quantityReceived() > 0){
				receivedetailHolder.tvReceiveItemLotWeight.setText(String.format("%.2f", item.get_quantityReceived() ));
				convertView.setBackgroundColor(context.getResources().getColor(R.color.Gray91));
				receivedetailHolder.linItemFront.setBackgroundColor(context.getResources().getColor(R.color.Gray91));
			} else {
				receivedetailHolder.tvReceiveItemLotWeight.setText("");
				convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
				receivedetailHolder.linItemFront.setBackgroundColor(context.getResources().getColor(R.color.white));
			}
			
			if(item.get_quantityDamaged() > 0){
				receivedetailHolder.tvReceiveItemDamaged.setText(srtDamage + ": " + String.format("%.2f", item.get_quantityDamaged()));
				receivedetailHolder.tvReceiveItemWeight.setTextColor(context.getResources().getColor(R.color.Red1));
				receivedetailHolder.tvReceiveItemLotWeight.setTextColor(context.getResources().getColor(R.color.Red1));
				receivedetailHolder.tvReceiveItemDamaged.setTextColor(context.getResources().getColor(R.color.Red1));
			} else {
				receivedetailHolder.tvReceiveItemDamaged.setText("");
				receivedetailHolder.tvReceiveItemWeight.setTextColor(context.getResources().getColor(R.color.Black));
				receivedetailHolder.tvReceiveItemLotWeight.setTextColor(context.getResources().getColor(R.color.Black));
				receivedetailHolder.tvReceiveItemDamaged.setTextColor(context.getResources().getColor(R.color.Black));
			}
		}
		
		return convertView;
	}
}
