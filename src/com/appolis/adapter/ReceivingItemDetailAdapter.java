/**
 * Name: $RCSfile: ReceivingItemDetailAdapter.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.appolis.androidtablet.R;
import com.appolis.common.LanguagePreferences;
import com.appolis.entities.EnPurchaseOrderInfo;
import com.appolis.entities.EnPurchaseOrderItemInfo;
import com.appolis.entities.EnPurchaseOrderReceiptInfo;
import com.appolis.receiving.AcReceiveItemDetail;
import com.appolis.utilities.BuManagement;
import com.appolis.utilities.DecimalDigitsInputFilter;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.Logger;
import com.appolis.utilities.StringUtils;
import com.appolis.utilities.Utilities;

/**
 * 
 * @author ThinDV
 * Process data for list view
 */
public class ReceivingItemDetailAdapter extends ArrayAdapter<EnPurchaseOrderReceiptInfo> implements View.OnTouchListener{
	private Context context;
	private ArrayList<EnPurchaseOrderReceiptInfo> listReceiptInfos = new ArrayList<EnPurchaseOrderReceiptInfo>();
	private ArrayList<Integer> listPossitionSelected = new ArrayList<>();
	private boolean lotTraked;
	private boolean enableButtonOk = true;
	private double receiLeft;
	private EnPurchaseOrderInfo enPurchaseOrderInfo;
	private EnPurchaseOrderItemInfo enPurchaseOrderItemInfo;
	private LanguagePreferences languagePrefs;
	
	//multiple language
	private String textLocation;
	private String textPidRrdLot;
	private String textRidGrdQty;
	
	public ReceivingItemDetailAdapter(Context context, EnPurchaseOrderItemInfo enPurchaseOrderItemInfo, EnPurchaseOrderInfo enPurchaseOrderInfo, ArrayList<EnPurchaseOrderReceiptInfo> listReceiptInfos) {
		super(context, R.layout.receve_item_details_item);
		this.context = context;
		this.listReceiptInfos = listReceiptInfos;
		this.enPurchaseOrderItemInfo = enPurchaseOrderItemInfo;
		this.lotTraked = enPurchaseOrderItemInfo.is_lotTrackingInd();
		this.enPurchaseOrderInfo = enPurchaseOrderInfo;
		this.languagePrefs = new LanguagePreferences(context.getApplicationContext());
		if(context instanceof AcReceiveItemDetail){
			receiLeft = ((AcReceiveItemDetail)context).getQtyLeft();
		}
		
		getLanguage();
		/*if(null != listReceiptInfos && listReceiptInfos.size() > 0){
			listPossitionSelected.add(0);
		}*/
	}
	
	/**
	 * get language from language package
	 */
	public void getLanguage(){
		textLocation = languagePrefs.getPreferencesString(GlobalParams.DMG_LBL_LOCATION_KEY, GlobalParams.DMG_LBL_LOCATION_VALUE);
		textPidRrdLot = languagePrefs.getPreferencesString(GlobalParams.PID_GRD_LOT_KEY, GlobalParams.PID_GRD_LOT_VALUE);
		textRidGrdQty = languagePrefs.getPreferencesString(GlobalParams.RID_GRD_QTY_KEY, GlobalParams.RID_GRD_QTY_VALUE);
	}
	
	@Override
	public int getCount() {
		if(null == listReceiptInfos){
			return 0;
		}
		
		return listReceiptInfos.size();
	}
	
	@Override
	public EnPurchaseOrderReceiptInfo getItem(int position) {
		if(null == listReceiptInfos){
			return null;
		}
		return listReceiptInfos.get(position);
	}
	
	public void updateListReciver(ArrayList<EnPurchaseOrderReceiptInfo> list){
		if(null != list){
			this.listReceiptInfos = list;
		}
	}
	
	public boolean isEnableButtonOk() {
		return enableButtonOk;
	}

	public void setEnableButtonOk(boolean enableButtonOk) {
		this.enableButtonOk = enableButtonOk;
	}

	/**
	 * setSelectedPosition
	 * @param position
	 */
	@SuppressLint("UseValueOf") 
	public void setSelectedPosition(int position){
		if(listPossitionSelected.contains(position)){
			//unselected;
			listPossitionSelected.remove(new Integer(position));
		} else {
			listPossitionSelected.add(position);
		}
		
		notifyDataSetChanged();
	}
	
	/**
	 * getSelectedPosion
	 * @return
	 */
	public ArrayList<Integer> getSelectedPosion(){
		return listPossitionSelected;
	}
	
	/**
	 * get list Item
	 * @return
	 */
	public ArrayList<Integer> getListPurchaseOrderItemId(){
		ArrayList<Integer> list = new ArrayList<>();
		for (EnPurchaseOrderReceiptInfo item : listReceiptInfos) {
			if(item.get_purchaseOrderItemShipID() > 0){
				list.add(item.get_purchaseOrderItemShipID());
			}
		}
		
		return list;
	}
	
	/**
	 * get list receipt which is selected
	 * @return
	 */
	public ArrayList<EnPurchaseOrderReceiptInfo> getListReceiptSelected(){
		if(null == listPossitionSelected || listPossitionSelected.size() <= 0){
			return null;
		}
		
		ArrayList<EnPurchaseOrderReceiptInfo> list = new ArrayList<>();
		for (Integer item : listPossitionSelected) {
			list.add(getItem(item));
		}
		
		return list;
	}
	
	/**
	 * getListReceiptSelectedWithOutLastItem
	 * @return
	 */
	public ArrayList<EnPurchaseOrderReceiptInfo> getListReceiptSelectedWithOutLastItem(){
		if(null == listPossitionSelected || listPossitionSelected.size() <= 0){
			return null;
		}
		
		ArrayList<EnPurchaseOrderReceiptInfo> list = new ArrayList<>();
		for (Integer item : listPossitionSelected) {
			if(item != getCount()-1){
				//item not last item
				list.add(getItem(item));
			}
		}
		
		return list;
	}
	
	/**
	 * get item at the end of listview
	 * @return
	 */
	public EnPurchaseOrderReceiptInfo getLastItem(){
		if(null == listReceiptInfos || listReceiptInfos.size() == 0){
			return null;
		} else {
			return listReceiptInfos.get(listReceiptInfos.size()-1);
		}
	}
	
	/**
	 * update scan result to lot field
	 * @param response value to update
	 */
	public void updateScanResult(String response){
		if(StringUtils.isNotBlank(response)){
			listReceiptInfos.get(listReceiptInfos.size()-1).set_lotNumber(response);
			notifyDataSetChanged();
		}
	}
	
	private class ReceiptInfoHolder{
		TextView tvLocation;
		LinearLayout linItemLot;
		EditText edtItemLot;
		TextView tvQtyLable;
		EditText edtItemQty;
	}
	
	@SuppressLint("ClickableViewAccessibility") @Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		if (view instanceof EditText) {
	        EditText editText = (EditText) view;
	        EditText editText2 = (EditText) view.getTag(R.string.COMMON_VIEW_TAG2);
	        editText.setFocusable(true);
	        editText.setFocusableInTouchMode(true);
	        
	        if(null != editText2){
	        	editText2.setFocusable(true);
	        	editText2.setFocusableInTouchMode(true);
	        }
	    } else {
	    	ReceiptInfoHolder holder = (ReceiptInfoHolder) view.getTag();
	        holder.edtItemLot.setFocusable(false);
	        holder.edtItemLot.setFocusableInTouchMode(false);
	        holder.edtItemQty.setFocusable(false);
	        holder.edtItemQty.setFocusableInTouchMode(false);
	    }
	    return false;
	}
	
	@SuppressLint("InflateParams") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ReceiptInfoHolder  receiptInfoHolder;

		if(null == convertView){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.receve_item_details_item, null);
			receiptInfoHolder = new ReceiptInfoHolder();
			convertView.setOnTouchListener(this);
			receiptInfoHolder.tvLocation = (TextView) convertView.findViewById(R.id.tv_location);
			receiptInfoHolder.tvLocation.setTag(position);
			receiptInfoHolder.edtItemLot = (EditText) convertView.findViewById(R.id.edt_item_lot);
			receiptInfoHolder.edtItemLot.setOnTouchListener(this);
			receiptInfoHolder.edtItemLot.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					int viewPosition = (int) receiptInfoHolder.tvLocation.getTag();
					if(viewPosition != getCount() -1){
						return;
					}
					EnPurchaseOrderReceiptInfo receipt = (EnPurchaseOrderReceiptInfo) receiptInfoHolder.edtItemLot.getTag(R.string.COMMON_VIEW_TAG1);
					receipt.set_lotNumber(s.toString());
					double changeValue = receipt.get_quantityReceived();
					if(enableButtonOk){
						if((changeValue <= 0) || StringUtils.isBlank(s.toString())){
							//disable button OK
							((AcReceiveItemDetail)context).setButtonOkStatus(false);
						} else {
							//enable button OK
							((AcReceiveItemDetail)context).setButtonOkStatus(true);
						}
					} else {
						//disable button OK
						((AcReceiveItemDetail)context).setButtonOkStatus(false);
					}
				}
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}
				@Override
				public void afterTextChanged(Editable s) {
				}
			});
			receiptInfoHolder.edtItemLot.setTag(R.string.COMMON_VIEW_TAG1, listReceiptInfos.get(position));
			receiptInfoHolder.edtItemLot.setTag(R.string.COMMON_VIEW_TAG2, receiptInfoHolder.edtItemQty);
			
			receiptInfoHolder.linItemLot = (LinearLayout) convertView.findViewById(R.id.lin_item_lot);
			receiptInfoHolder.tvQtyLable = (TextView) convertView.findViewById(R.id.tv_qty_title_lable);
			receiptInfoHolder.edtItemQty = (EditText) convertView.findViewById(R.id.edt_item_qty);
			receiptInfoHolder.edtItemQty.setOnTouchListener(this);
			receiptInfoHolder.edtItemQty.addTextChangedListener( new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					int viewPosition = (int) receiptInfoHolder.tvLocation.getTag();

					if(null != s && s.length() == 1 && s.toString().equalsIgnoreCase(".")){
						receiptInfoHolder.edtItemQty.setText("");
						s = "";
					}
					
					if(viewPosition != getCount() -1){
						return;
					}
					
					EnPurchaseOrderReceiptInfo receipt = (EnPurchaseOrderReceiptInfo) receiptInfoHolder.edtItemQty.getTag(R.string.COMMON_VIEW_TAG1);
					double value;
					if(null != s && !(s.toString().equalsIgnoreCase(""))){
						value = Double.parseDouble(s.toString());
					} else {
						value = 0;
					}
					receipt.set_quantityReceived(value);
					String lot = receipt.get_lotNumber();
					
					//update status of button OK
					if(lotTraked){
						//item is lot tracked : require lot
						if(enableButtonOk){
							if((value <= 0) || StringUtils.isBlank(lot)){
								//disable button OK
								((AcReceiveItemDetail)context).setButtonOkStatus(false);
							} else {
								//enable button OK
								((AcReceiveItemDetail)context).setButtonOkStatus(true);
							}
						} else {
							//disable button OK
							((AcReceiveItemDetail)context).setButtonOkStatus(false);
						}
					} else {
						//item is lot tracked : NOT require lot
						if(enableButtonOk){
							if((value <= 0)){
								//disable button OK
								((AcReceiveItemDetail)context).setButtonOkStatus(false);
							} else {
								//enable button OK
								((AcReceiveItemDetail)context).setButtonOkStatus(true);
							}
						} else {
							//disable button OK
							((AcReceiveItemDetail)context).setButtonOkStatus(false);
						}
					}
					
					//update left field and Receive field
					float over = enPurchaseOrderInfo.get_vendorReceivePercentOver();
					if(over == -1){
						((AcReceiveItemDetail)context).updateRecvAndLeftField(value);
					} else if(over == 0){
						if(value <= receiLeft){
							((AcReceiveItemDetail)context).updateRecvAndLeftField(value);
						} else {
							value = receiLeft;
							receipt.set_quantityReceived(value);
							((AcReceiveItemDetail)context).updateRecvAndLeftField(value);
						}
					} else if(over > 0) {
						double overWeigh = over * receiLeft;
						if(value <= overWeigh){
							((AcReceiveItemDetail)context).updateRecvAndLeftField(value);
						} else {
							value = overWeigh;
							receipt.set_quantityReceived(value);
							((AcReceiveItemDetail)context).updateRecvAndLeftField(value);
						}
					}
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}
				@Override
				public void afterTextChanged(Editable s) {
				}
			});
			receiptInfoHolder.edtItemQty.setOnEditorActionListener(new TextView.OnEditorActionListener() { 
			    @Override 
			    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) { 
			        if (actionId == EditorInfo.IME_ACTION_DONE) { 
			        	Utilities.hideKeyboard(context);
			        } 
			        return false; 
			    } 
			}); 
			receiptInfoHolder.edtItemQty.setTag(R.string.COMMON_VIEW_TAG1, listReceiptInfos.get(position));
			receiptInfoHolder.edtItemQty.setTag(R.string.COMMON_VIEW_TAG2, receiptInfoHolder.edtItemLot);
			convertView.setTag(receiptInfoHolder);
		} else {
			receiptInfoHolder = (ReceiptInfoHolder) convertView.getTag();
			receiptInfoHolder.tvLocation.setTag(position);
			receiptInfoHolder.edtItemLot.setTag(R.string.COMMON_VIEW_TAG1, listReceiptInfos.get(position));
			receiptInfoHolder.edtItemLot.setTag(R.string.COMMON_VIEW_TAG2, receiptInfoHolder.edtItemQty);
			receiptInfoHolder.edtItemQty.setTag(R.string.COMMON_VIEW_TAG1, listReceiptInfos.get(position));
			receiptInfoHolder.edtItemQty.setTag(R.string.COMMON_VIEW_TAG2, receiptInfoHolder.edtItemLot);
		}
		
		receiptInfoHolder.edtItemLot.setOnEditorActionListener(new OnEditorActionListener() {        
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        if(actionId == EditorInfo.IME_ACTION_DONE){
		        	receiptInfoHolder.edtItemQty.requestFocus();
		        	receiptInfoHolder.edtItemQty.setSelection(receiptInfoHolder.edtItemQty.getText().toString().length());
		        }
		    return false;
		    }
		});
		
		receiptInfoHolder.edtItemQty.setFilters(
			new InputFilter[]{
				new DecimalDigitsInputFilter(enPurchaseOrderItemInfo.get_significantDigits()), 
				new InputFilter.LengthFilter(14)
			}
		);
		EnPurchaseOrderReceiptInfo receiptInfo = getItem(position);
		receiptInfoHolder.edtItemLot.setHint(textPidRrdLot);
		receiptInfoHolder.tvQtyLable.setHint(textRidGrdQty);
		
		if(null != receiptInfo){
			if(StringUtils.isNotBlank(receiptInfo.get_binNumber())){
				receiptInfoHolder.tvLocation.setText(textLocation + ": " + receiptInfo.get_binNumber());
			}
			
			if(listPossitionSelected.contains(position)){
				convertView.setBackgroundColor(context.getResources().getColor(R.color.Blue13));
			} else {
				convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
			}
			if(position != (getCount()-1)){
				receiptInfoHolder.edtItemLot.setClickable(false);
				receiptInfoHolder.edtItemLot.setEnabled(false);
				receiptInfoHolder.edtItemQty.setClickable(false);
				receiptInfoHolder.edtItemQty.setEnabled(false);
				
				if(receiptInfo.get_quantityReceived() >= 0){
					receiptInfoHolder.edtItemQty.setText(BuManagement.formatDecimal(receiptInfo.get_quantityReceived()).trim());
				} else {
					receiptInfoHolder.edtItemQty.setText("0");
				}
				
				if(lotTraked){
					if(StringUtils.isNotBlank(receiptInfo.get_lotNumber())){
						receiptInfoHolder.edtItemLot.setText(receiptInfo.get_lotNumber());
					} else {
						receiptInfoHolder.edtItemLot.setText(""); 
					}
				} else {
					receiptInfoHolder.linItemLot.setVisibility(View.INVISIBLE);
				}
				
			} else {
				//last item
				receiptInfoHolder.edtItemLot.setClickable(true);
				receiptInfoHolder.edtItemQty.setClickable(true);
				receiptInfoHolder.edtItemQty.setEnabled(true);
				receiptInfoHolder.edtItemLot.setEnabled(true);
				
				//set filter
				InputFilter[] filters = new InputFilter[1];
			    filters[0] = new InputFilter(){
			        @Override
			        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
			            if (end > start) {

			                char[] acceptedChars = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 
			                        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
			                        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' '};

			                for (int index = start; index < end; index++) {                                         
			                    if (!new String(acceptedChars).contains(String.valueOf(source.charAt(index)))) { 
			                        return ""; 
			                    }               
			                }
			            }
			            return null;
			        }

			    };
			    receiptInfoHolder.edtItemLot.setFilters(filters);
			    
				if(lotTraked){
					//require lot
					Logger.error("Receive Item Detail Adapter: Last item Lot focus");
					receiptInfoHolder.edtItemLot.requestFocus();
					if(StringUtils.isNotBlank(getItem(listReceiptInfos.size()-1).get_lotNumber())){
						receiptInfoHolder.edtItemQty.requestFocus();
						receiptInfoHolder.edtItemQty.setSelection(receiptInfoHolder.edtItemQty.getText().toString().length());
					}
					
					receiptInfoHolder.edtItemLot.setText(getItem(listReceiptInfos.size()-1).get_lotNumber());
					receiptInfoHolder.edtItemQty.setText(BuManagement.formatDecimal(listReceiptInfos.get(position).get_quantityReceived()).trim());
					if(StringUtils.isNotBlank(getItem(listReceiptInfos.size()-1).get_lotNumber())){
						if(context instanceof AcReceiveItemDetail){
							double changeValue = listReceiptInfos.get(listReceiptInfos.size()-1).get_quantityReceived();
							if(enableButtonOk){
								if(changeValue > 0){
									((AcReceiveItemDetail)context).setButtonOkStatus(true);
								} else {
									((AcReceiveItemDetail)context).setButtonOkStatus(false);
								}
							} else {
								((AcReceiveItemDetail)context).setButtonOkStatus(false);
							}
						}
					} else {
						if(context instanceof AcReceiveItemDetail){
							((AcReceiveItemDetail)context).setButtonOkStatus(false);	
						}
					}
					 
					if(context instanceof AcReceiveItemDetail){
						AcReceiveItemDetail parentActivy = ((AcReceiveItemDetail)context);
						parentActivy.showButtonScan(true);
					}
				} else {
					//don't require lot
					Logger.error("Receive Item Detail Adapter: Last Item Qty focus");
					receiptInfoHolder.linItemLot.setVisibility(View.INVISIBLE);
					receiptInfoHolder.edtItemQty.requestFocus();
					receiptInfoHolder.edtItemQty.setText(BuManagement.formatDecimal(listReceiptInfos.get(position).get_quantityReceived()).trim());
					receiptInfoHolder.edtItemQty.setSelection(receiptInfoHolder.edtItemQty.getText().toString().length());
				}
				
				receiptInfoHolder.edtItemLot.setOnFocusChangeListener(new OnFocusChangeListener() {
					
					@Override
					public void onFocusChange(View view, boolean focus) {
						if(focus){
							if(context instanceof AcReceiveItemDetail){
								AcReceiveItemDetail parentActivy = ((AcReceiveItemDetail)context);
								parentActivy.showButtonScan(true);
							}
						} else {
							if(context instanceof AcReceiveItemDetail){
								AcReceiveItemDetail parentActivy = ((AcReceiveItemDetail)context);
								parentActivy.showButtonScan(false);
							}
						}
					}
				});
			}
		}
		
		return convertView;
	}
}
