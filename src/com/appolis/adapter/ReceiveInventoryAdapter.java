/**
 * Name: $RCSfile: ReceiveInventoryAdapter.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.appolis.androidtablet.R;
import com.appolis.entities.EnPO;
import com.appolis.entities.EnPODetails;
import com.appolis.receiverinventory.ItemReceiveInventoryDetails;
import com.appolis.utilities.GlobalParams;

/**
 * @author ThinDV
 * Process data for list view Receive inventory
 */
public class ReceiveInventoryAdapter extends BaseAdapter{
	private EnPO _data;
	private EnPODetails items;
	private Activity mActivity;

	public ReceiveInventoryAdapter(Activity activity, EnPO data) {
		mActivity = activity;
		_data = data;
	}

	@Override
	public int getCount() {
		return (_data == null ? 0 : _data.getPODetails().size());
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
			convertView = new ItemReceiveInventoryDetails(mActivity.getApplicationContext());
			
			holder = new ViewHolder();
			holder.tvItemNumber = (TextView) convertView.findViewById(R.id.tvItemNumber);
			holder.tvQty = (TextView) convertView.findViewById(R.id.tvQty);
			holder.tvLotNumber = (TextView) convertView.findViewById(R.id.tvLotNumber);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		items = _data.getPODetails().get(position);

		if (items != null) {			
			holder.tvItemNumber.setText(items.getItem().getItemNumber() + GlobalParams.TWO_DOT_VERTICAL);
			holder.tvQty.setText(items.getQty() + GlobalParams.SPACE + mActivity.getResources().getString(R.string.Qty));
			if (!items.getItem().getLotNumber().equals(GlobalParams.BLANK_CHARACTER)) {
				holder.tvLotNumber.setText(mActivity.getResources().getString(R.string.txt_add_job_parts_lot)
						+ GlobalParams.SPACE + items.getItem().getLotNumber());
			} else {
				holder.tvLotNumber.setText(GlobalParams.BLANK_CHARACTER);
			}
		}

		((ItemReceiveInventoryDetails) convertView).set_position(position);
		return convertView;
	}

	/**
	 * instance view 
	 * @author hoangnh11
	 */
	static class ViewHolder {
		TextView tvItemNumber, tvQty, tvLotNumber;		
	}
}
