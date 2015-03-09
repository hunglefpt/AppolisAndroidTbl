/**
 * Name: $RCSfile: ReceivingListAdapter.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.appolis.androidtablet.R;
import com.appolis.entities.EnReceivingInfo;
import com.appolis.utilities.BuManagement;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.StringUtils;

/**
 * 
 * @author ThinDV
 * Process data for list view
 */
public class ReceivingListAdapter extends ArrayAdapter<EnReceivingInfo> {
	private Context context;
	private ArrayList<EnReceivingInfo> listReceive;
	private ArrayList<EnReceivingInfo> orignList;
	private FilterList filterList = new FilterList();
	
	public ReceivingListAdapter(Context context, ArrayList<EnReceivingInfo> listReceiveIn) {
		super(context, R.layout.receive_list_item);
		this.context = context;
		this.listReceive = listReceiveIn;
		this.orignList = listReceiveIn;
	}

	@Override
	public int getCount() {
		if(null == listReceive){
			return 0;
		}
		
		return listReceive.size();
	}
	
	@Override
	public EnReceivingInfo getItem(int position) {
		if(null == listReceive){
			return null;
		}
		return listReceive.get(position);
	}
	
	/**
	 * update list PO
	 * @param list
	 */
	public void updateListReciver(ArrayList<EnReceivingInfo> list){
		if(null != list){
			this.listReceive = new ArrayList<EnReceivingInfo>();
			this.orignList = new ArrayList<EnReceivingInfo>();
			this.listReceive = list;
			this.orignList = list;
		}
	}
	
	private class ReceiveListHolder{
		TextView tvReceivePoNumber;
		TextView tvReceiveStatus;
		TextView tvReceiveShipedDate;
		TextView tvReceiveVendorName;
	}
	
	@SuppressLint("InflateParams") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ReceiveListHolder receiveListHolder;
		
		if(null == convertView){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.receive_list_item, null);
			receiveListHolder = new ReceiveListHolder();
			receiveListHolder.tvReceivePoNumber = (TextView) convertView.findViewById(R.id.tv_receive_po_number);
			receiveListHolder.tvReceiveShipedDate = (TextView) convertView.findViewById(R.id.tv_receive_date);
			receiveListHolder.tvReceiveStatus = (TextView) convertView.findViewById(R.id.tv_receive_status);
			receiveListHolder.tvReceiveVendorName = (TextView) convertView.findViewById(R.id.tv_receive_vendor_name);
			
			convertView.setTag(receiveListHolder);
		} else {
			receiveListHolder = (ReceiveListHolder) convertView.getTag();
		}
		
		EnReceivingInfo item = getItem(position);
		if(null != item){
			receiveListHolder.tvReceivePoNumber.setText(item.get_poNumber());
			receiveListHolder.tvReceiveStatus.setText(item.get_statusDescription());
			receiveListHolder.tvReceiveShipedDate.setText(BuManagement.normalizationTime(item.get_dateShipped()
					, GlobalParams.dateFormatYYYY_MM_DD_T_HHMMSS, GlobalParams.dateFormatMM_DD_YYYY));
			receiveListHolder.tvReceiveVendorName.setText(item.get_vendorName());
			
			if(GlobalParams.RECEIVING_IN_PROCESS.equalsIgnoreCase(item.get_statusDescription())){
				convertView.setBackgroundColor(context.getResources().getColor(R.color.Gray91));
			} else {
				convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
			}
		}
		return convertView;
	}
	
	/**
	 * getFilter
	 */
	public FilterList getFilter(){
		return filterList;
	}
	
	/**
	 * filter list PO
	 * @author Do Thin
	 *
	 */
	@SuppressLint("DefaultLocale") 
	public class FilterList extends Filter {
		
		@Override
		protected FilterResults performFiltering(CharSequence textSearch) {
			FilterResults results = new FilterResults();
			ArrayList<EnReceivingInfo> resultsList = new ArrayList<EnReceivingInfo>();
			if(null == textSearch ||StringUtils.isBlank(textSearch.toString())){
				resultsList = orignList;
				results.count = resultsList.size();
				results.values = resultsList;
			} else {
				for (EnReceivingInfo enReceivingInfo : orignList) {
					if(enReceivingInfo.get_poNumber().toLowerCase().contains(textSearch.toString().toLowerCase())){
						resultsList.add(enReceivingInfo);
					}
				}
				results.count = resultsList.size();
				results.values = resultsList;
			}
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence text, FilterResults results) {
			listReceive = (ArrayList<EnReceivingInfo>) results.values;
			notifyDataSetChanged();
		}
		
	}
}
