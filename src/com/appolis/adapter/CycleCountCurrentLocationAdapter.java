/**
 * Name: $RCSfile: CycleCountCurrentAdapter.java,v $
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
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appolis.androidtablet.R;
import com.appolis.common.CommonData;
import com.appolis.common.LanguagePreferences;
import com.appolis.cyclecount.AcCycleCountAdjusment;
import com.appolis.cyclecount.AcCycleCountBinPath;
import com.appolis.cyclecount.AcCycleCountLocation;
import com.appolis.entities.ObjectCountCycleCurrent;
import com.appolis.entities.ObjectCycleCount;
import com.appolis.entities.ObjectInstanceRealTimeBin;
import com.appolis.login.LoginActivity;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.StringUtils;

/**
 * 
 * @author CongLT
 * Process data for list view
 */
@SuppressLint("ViewHolder")
public class CycleCountCurrentLocationAdapter extends BaseAdapter {

	private Context context;
	private List<ObjectCountCycleCurrent> binList;
	private ArrayList<ObjectCountCycleCurrent> arrayList;
	private ObjectInstanceRealTimeBin bin;
	private ObjectCycleCount objectCycleCount;
	private AcCycleCountLocation activity;
	private LanguagePreferences languagePrefs;
	
	public CycleCountCurrentLocationAdapter(Context context, List<ObjectCountCycleCurrent> binList,
			ObjectInstanceRealTimeBin bin, ObjectCycleCount objectCycleCount,AcCycleCountLocation activity) {
		super();
		this.context = context;
		this.binList = binList;
		this.arrayList = new ArrayList<ObjectCountCycleCurrent>();
		this.arrayList.addAll(binList);
		this.bin = bin;
		this.objectCycleCount = objectCycleCount;
		this.activity = activity;
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
		
		final ObjectCountCycleCurrent currentSelected = binList.get(position); 
		LinearLayout lnItem = (LinearLayout) rowView.findViewById(R.id.lnCycleCountItem);
		TextView tvBinNumber = (TextView) rowView.findViewById(R.id.tvBinNumber);
		TextView tvBinDes = (TextView) rowView.findViewById(R.id.tvBinDescription);
		TextView tvBinStatus = (TextView) rowView.findViewById(R.id.tvBinStatus);
		TextView tvLot = (TextView) rowView.findViewById(R.id.tvLot);
		
		tvBinDes.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		tvBinDes.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		tvBinStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		
		ImageView imgNext = (ImageView) rowView.findViewById(R.id.imgCycleNextScreen);
		imgNext.setVisibility(View.VISIBLE);
		
		ImageView imgCycleInfor = (ImageView) rowView.findViewById(R.id.imgCycleInfor);
		imgCycleInfor.setVisibility(View.VISIBLE);
		
		if(!LoginActivity.itemUser.get_isForceCycleCountScan()) {
			imgCycleInfor.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (currentSelected.is_lpInd()) {
						startActivityBinPath(currentSelected);
					} else {
						startActivityCycleAdjustment(currentSelected);
					}
				}
			});
		}
		
		if(!LoginActivity.itemUser.get_isForceCycleCountScan()) {
			imgNext.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (currentSelected.is_lpInd()) {
						startActivityBinPath(currentSelected);
					} else {
						startActivityCycleAdjustment(currentSelected);
					}
				}
			});
		}

		
		if(null != bin) {
			String binStatus = currentSelected.get_itemStatus();
			tvBinStatus.setText(languagePrefs.getPreferencesString(GlobalParams.COMPLETED_VALUE, GlobalParams.COMPLETED_VALUE));
			
			if(!CommonData.COMPLETED.equalsIgnoreCase(binStatus.trim())) {
				tvBinStatus.setVisibility(View.INVISIBLE);
				lnItem.setBackgroundColor(context.getResources().getColor(R.color.white));
			} else {
				tvBinStatus.setVisibility(View.VISIBLE);
				lnItem.setBackgroundColor(context.getResources().getColor(R.color.Gray73));
			}
			
			tvBinNumber.setText(currentSelected.get_itemNumber());
			tvBinDes.setText(currentSelected.get_itemDesc());
			
			if(StringUtils.isNotBlank(currentSelected.get_lotNumber())) {
				tvLot.setVisibility(View.VISIBLE);
				String lot = languagePrefs.getPreferencesString(GlobalParams.REST_GRD_LOT_KEY, GlobalParams.LOT);
				tvLot.setText(lot + ": " + currentSelected.get_lotNumber());
			} else {
				tvLot.setVisibility(View.INVISIBLE);
			}
		}
		return rowView;
	}

	public void filter(String charText) {
		binList.clear();
		if (charText.length() == 0) {
			binList.addAll(arrayList);
		} else {
			for (ObjectCountCycleCurrent bin : arrayList) {
				String binAndLot = bin.get_itemNumber().toUpperCase() + bin.get_lotNumber().toUpperCase();
				if (binAndLot.contains(charText.toUpperCase())) {
					binList.add(bin);
				}
			}
		}
		notifyDataSetChanged();
	}

	private void startActivityBinPath(ObjectCountCycleCurrent currentSelected) {
		String path = bin.get_binNumber();
		Intent intentCycleBin = new Intent(context, AcCycleCountBinPath.class);
		intentCycleBin.putExtra("path", path);
		intentCycleBin.putExtra("BinCycleCount", bin);
		intentCycleBin.putExtra("objectCycleCount", objectCycleCount);
		intentCycleBin.putExtra("cycleItemCurrent", currentSelected);
		activity.startActivityForResult(intentCycleBin,
				GlobalParams.AC_CYCLE_COUNT_LOCATION_ACTIVITY);
	}

	private void startActivityCycleAdjustment(ObjectCountCycleCurrent currentSelected) {
		String path = bin.get_binNumber();
		Intent intentCycleAdjustment = new Intent(context, AcCycleCountAdjusment.class);
		intentCycleAdjustment.putExtra("cycleItemCurrent", currentSelected);
		intentCycleAdjustment.putExtra("BinCycleCount", bin);
		intentCycleAdjustment.putExtra("path", path);
		intentCycleAdjustment.putExtra("isUpdateCycleCount", true);
		intentCycleAdjustment.putExtra("isLp", false);
		activity.startActivityForResult(intentCycleAdjustment, GlobalParams.AC_CYCLE_COUNT_ADJUSMENT_ACTIVITY);
	}
}
