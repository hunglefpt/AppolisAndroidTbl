/**
 * Name: $RCSfile: CycleUomAdapter.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appolis.androidtablet.R;
import com.appolis.entities.ObjectCycleUom;

/**
 * 
 * @author CongLT
 * Process data for list view
 */
public class CycleUomAdapter extends ArrayAdapter<ObjectCycleUom> {
	private Activity context;
	ArrayList<ObjectCycleUom> data = null;

	public CycleUomAdapter(Activity context, int resource, ArrayList<ObjectCycleUom> data) {
		super(context, resource, data);
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) { 

		return initView(position, convertView, parent);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) { 
		return initView(position, convertView, parent);
	}

	private View initView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(R.layout.cycle_uom_item, parent, false);
		}

		ObjectCycleUom item = data.get(position);
		if (item != null) {
			TextView tvItemUom = (TextView) row.findViewById(R.id.tvItemUom);

			if (tvItemUom != null) {
				tvItemUom.setText(item.getUomDescription());
			}
		}

		return row;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public int getPosition(ObjectCycleUom item) {
		int index = 0;
		for(int i = 0; i < data.size(); i++) {
			if(data.get(i).getUomDescription().trim().equalsIgnoreCase(item.getUomDescription().trim())) {
				return i;
			}
		}
		
		return index;
	}

	@Override
	public ObjectCycleUom getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
