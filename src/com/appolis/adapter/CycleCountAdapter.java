/**
 * Name: $RCSfile: CycleCountAdapter.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appolis.androidtablet.R;
import com.appolis.common.CommonData;
import com.appolis.common.LanguagePreferences;
import com.appolis.entities.ObjectInstanceRealTimeBin;
import com.appolis.utilities.GlobalParams;

/**
 * 
 * @author hoangnh11
 * Process data for list view
 */
@SuppressLint({ "ViewHolder", "DefaultLocale" })
public class CycleCountAdapter extends BaseAdapter {

	private Context context;
	private List<ObjectInstanceRealTimeBin> binList;
	private ArrayList<ObjectInstanceRealTimeBin> arrayList;
	private LanguagePreferences languagePrefs;
	
	public CycleCountAdapter(Context context, List<ObjectInstanceRealTimeBin> binList) {
		super();
		this.context = context;
		this.binList = binList;
		this.arrayList = new ArrayList<ObjectInstanceRealTimeBin>();
		this.arrayList.addAll(binList);
		languagePrefs = new LanguagePreferences(context);
	}

	@Override
	public int getCount() {
		return binList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return binList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.cycle_count_item, parent, false);
		
		final ObjectInstanceRealTimeBin bin = binList.get(position); 
		LinearLayout lnItem = (LinearLayout) rowView.findViewById(R.id.lnCycleCountItem);
		TextView tvBinNumber = (TextView) rowView.findViewById(R.id.tvBinNumber);
		TextView tvBinDes = (TextView) rowView.findViewById(R.id.tvBinDescription);
		TextView tvBinStatus = (TextView) rowView.findViewById(R.id.tvBinStatus);
		TextView tvLot = (TextView) rowView.findViewById(R.id.tvLot);
		tvLot.setVisibility(View.INVISIBLE);

		if(null != bin) {
			String binStatus = bin.get_binStatus();
			tvBinStatus.setText(languagePrefs.getPreferencesString(GlobalParams.COMPLETED_VALUE, GlobalParams.COMPLETED_VALUE));
			
			if(!CommonData.COMPLETED.equalsIgnoreCase(binStatus.trim())) {
				tvBinStatus.setVisibility(View.GONE);
				lnItem.setBackgroundColor(context.getResources().getColor(R.color.white));
			}
			
			tvBinNumber.setText(bin.get_binNumber());
			tvBinDes.setText(bin.get_binDesc());
		}
		return rowView;
	}

	@SuppressLint("DefaultLocale")
	public void filter(String charText) {
		binList.clear();
		if (charText.length() == 0) {
			binList.addAll(arrayList);
		} else {
			for (ObjectInstanceRealTimeBin bin : arrayList) {
				String textSearch = charText.toUpperCase();
				if (bin.get_binNumber().toUpperCase().contains(textSearch)) {
					binList.add(bin);
				}
			}
		}
		notifyDataSetChanged();
	}
	
}
