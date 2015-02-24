/**
 * Name: PutAwayBinAdapter.java
 * Date: Jan 28, 2015 4:24:17 PM
 * Copyright (c) 2014 FPT Software, Inc. All rights reserved.
 */
package com.appolis.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appolis.androidtablet.R;
import com.appolis.entities.EnPutAwayBin;
import com.appolis.putaway.ItemPutAwayBin;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.Logger;
import com.appolis.utilities.StringUtils;

/**
 * @author hoangnh11
 * Process data for list view
 */
public class PutAwayBinAdapter extends BaseAdapter{
	private ArrayList<EnPutAwayBin> _data;
	private EnPutAwayBin items;
	private Activity mActivity;
	private ArrayList<EnPutAwayBin> orignList;
	private FilterList filterList = new FilterList();

	public PutAwayBinAdapter(Activity activity, ArrayList<EnPutAwayBin> data) {
		mActivity = activity;
		_data = data;
		this.orignList = data;
	}

	@Override
	public int getCount() {
		return (_data == null ? 0 : _data.size());
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = new ItemPutAwayBin(mActivity.getApplicationContext());
			
			holder = new ViewHolder();		
			holder.tvBinNumber = (TextView) convertView.findViewById(R.id.tvBinNumber);
			holder.tvBinSeq = (TextView) convertView.findViewById(R.id.tvBinSeq);
			holder.tvBinQty = (TextView) convertView.findViewById(R.id.tvBinQty);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		items = _data.get(position);

		if (items != null) {
			
			holder.tvBinNumber.setText(mActivity.getResources().getString(R.string.BIN_VERTICAL_TWO_DOT) 
					+ GlobalParams.SPACE + items.get_binNumber());
			
			if (items.get_binSeq().equals(GlobalParams.P)) {
				holder.tvBinSeq.setText(GlobalParams.PRIMARY);
			} else if (items.get_binSeq().equals(GlobalParams.S)) {
				holder.tvBinSeq.setText(GlobalParams.SECONDARY);
			}
			
			if (!items.get_binQtyDisplay().contains(GlobalParams.DOT)) {
				holder.tvBinQty.setText(mActivity.getResources().getString(R.string.QTY_VERTICAL_TWO_DOT) 
						+ GlobalParams.SPACE + items.get_binQtyDisplay() + ".00");
			} else {
				holder.tvBinQty.setText(mActivity.getResources().getString(R.string.QTY_VERTICAL_TWO_DOT) 
						+ GlobalParams.SPACE + items.get_binQtyDisplay());
			}
		}

		((ItemPutAwayBin) convertView).set_position(position);
		return convertView;
	}

	/**
	 * instance view 
	 * @author hoangnh11
	 */
	static class ViewHolder {
		LinearLayout linItem;
		TextView tvBinNumber, tvBinSeq, tvBinQty;
	}
	
	/**
	 * Get data filter
	 * @return
	 */
	public FilterList getFilter(){
		return filterList;
	}
	
	/**
	 * Filter data
	 * @author hoangnh11
	 */
	@SuppressLint("DefaultLocale")
	public class FilterList extends Filter{

		@Override
		protected FilterResults performFiltering(CharSequence textSearch) {
			FilterResults results = new FilterResults();
			ArrayList<EnPutAwayBin> resultsList = new ArrayList<>();
			
			if(null == textSearch ||StringUtils.isBlank(textSearch.toString())){
				resultsList = orignList;
				results.count = resultsList.size();
				results.values = resultsList;
				
			} else {
				
				for (EnPutAwayBin enPutAwayBin : orignList) {					
					if(enPutAwayBin.get_binNumber().toLowerCase().contains(textSearch.toString().toLowerCase())){
						resultsList.add(enPutAwayBin);
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
			_data = ((ArrayList<EnPutAwayBin>) results.values);
			notifyDataSetChanged();
		}		
	}
}
