/**
 * Name: PutAwayAdapter.java
 * Date: Jan 26, 2015 3:05:03 PM
 * Copyright (c) 2014 FPT Software, Inc. All rights reserved.
 */
package com.appolis.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appolis.androidtablet.R;
import com.appolis.entities.EnPutAway;
import com.appolis.putaway.ItemPutAway;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.StringUtils;

/**
 * @author hoangnh11
 * Process data for list view
 */
public class PutAwayAdapter extends BaseAdapter{
	private ArrayList<EnPutAway> _data;
	private EnPutAway items;
	private Activity mActivity;
	private ArrayList<EnPutAway> orignList;
	private OnClickListener _onItemClick;
	private FilterList filterList = new FilterList();
	DecimalFormat df = new DecimalFormat("#0.00");

	public PutAwayAdapter(Activity activity, ArrayList<EnPutAway> data) {
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
		return _data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = new ItemPutAway(mActivity.getApplicationContext());
			((ItemPutAway) convertView).setOnThisItemClickHandler(onItemClickHandler);
			
			holder = new ViewHolder();
			holder.tvItemDes = (TextView) convertView.findViewById(R.id.tvItemDes);
			holder.tvQty = (TextView) convertView.findViewById(R.id.tvQty);
			holder.tvBinNumber = (TextView) convertView.findViewById(R.id.tvBinNumber);
			holder.tvUomDes = (TextView) convertView.findViewById(R.id.tvUomDes);
			holder.tvLotNumber = (TextView) convertView.findViewById(R.id.tvLotNumber);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		items = _data.get(position);

		if (items != null) {
			holder.tvItemDes.setText(items.get_itemDesc() + " - " + items.get_itemNumber());
			holder.tvQty.setText(String.valueOf(df.format(items.get_qty())));
			holder.tvBinNumber.setText(mActivity.getResources().getString(R.string.loc)
					+ GlobalParams.SPACE + items.get_binNumber());
			holder.tvUomDes.setText(items.get_uomDescription());
			
			if (StringUtils.isBlank(items.get_lotNumber())) {
				holder.tvLotNumber.setVisibility(View.INVISIBLE);
			} else {
				holder.tvLotNumber.setText(mActivity.getResources().getString(R.string.LOTS)
						+ GlobalParams.SPACE + items.get_lotNumber());
				holder.tvLotNumber.setVisibility(View.VISIBLE);
			}
		}
		
		((ItemPutAway) convertView).set_position(position);
		return convertView;
	}

	/**
	 * instance view 
	 * @author hoangnh11
	 */
	static class ViewHolder {
		LinearLayout linItem;
		TextView tvItemDes, tvQty, tvBinNumber, tvUomDes, tvLotNumber;
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
			ArrayList<EnPutAway> resultsList = new ArrayList<EnPutAway>();
			
			if(null == textSearch ||StringUtils.isBlank(textSearch.toString())){
				resultsList = orignList;
				results.count = resultsList.size();
				results.values = resultsList;
				
			} else {
				for (EnPutAway enPutAway : orignList) {
					if(enPutAway.get_binNumber().toLowerCase().contains(textSearch.toString().toLowerCase())){
						resultsList.add(enPutAway);
					} else if (enPutAway.get_itemDesc().toLowerCase().contains(textSearch.toString().toLowerCase())) {
						resultsList.add(enPutAway);
					} else if (enPutAway.get_itemNumber().toLowerCase().contains(textSearch.toString().toLowerCase())) {
						resultsList.add(enPutAway);
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
			_data = (ArrayList<EnPutAway>) results.values;			
			notifyDataSetChanged();
		}		
	}
	
	OnClickListener onItemClickHandler = new OnClickListener()
	{

        @Override
        public void onClick(View v)
        {
            if (_onItemClick != null)
                _onItemClick.onClick(v);
        }
    };
    
    public void setOnItemClickHandler(OnClickListener itemClick)
    {
        _onItemClick = itemClick;
    }
}
