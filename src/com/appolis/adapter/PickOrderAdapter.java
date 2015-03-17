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
import com.appolis.common.LanguagePreferences;
import com.appolis.entities.EnOrderPickSwitchInfo;
import com.appolis.utilities.BuManagement;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.StringUtils;

public class PickOrderAdapter extends ArrayAdapter<EnOrderPickSwitchInfo>{
	private ArrayList<EnOrderPickSwitchInfo> listPickOrders = new ArrayList<>();
	private ArrayList<EnOrderPickSwitchInfo> orignList = new ArrayList<EnOrderPickSwitchInfo>();
	private Context context;
	private LanguagePreferences languagPrf;
	private String strZone;
	private FilterList filterList = new FilterList();
	
	public PickOrderAdapter(Context context, ArrayList<EnOrderPickSwitchInfo> list) {
		super(context, R.layout.pick_list_item);
		languagPrf = new LanguagePreferences(context);
		this.context = context;
		this.listPickOrders = list;
		strZone  = languagPrf.getPreferencesString(GlobalParams.ZONE_KEY, GlobalParams.ZONE_VALUE);
	}

	@Override
	public int getCount() {
		if(null == listPickOrders){
			return -1;
		} else {
			return listPickOrders.size();
		}
	}
	
	@Override
	public EnOrderPickSwitchInfo getItem(int position) {
		return listPickOrders.get(position);
	}
	
	class PickOrderHolder{
		TextView tvPickOrderNumber;
		TextView tvPickOrderDate;
		TextView tvPickOrderComplete;
		TextView tvPickOrderSubName;
		TextView tvPickOrderZone;
	}
	
	@SuppressLint("InflateParams") 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PickOrderHolder pickOrderHolder = null;
		if(null == convertView){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.pick_list_item, null);
			pickOrderHolder = new PickOrderHolder();
			
			pickOrderHolder.tvPickOrderNumber = (TextView) convertView.findViewById(R.id.tv_pick_order_number);
			pickOrderHolder.tvPickOrderDate = (TextView) convertView.findViewById(R.id.tv_pick_order_date);
			pickOrderHolder.tvPickOrderComplete = (TextView) convertView.findViewById(R.id.tv_pick_order_complete);
			pickOrderHolder.tvPickOrderSubName = (TextView) convertView.findViewById(R.id.tv_pick_order_sub_name);
			pickOrderHolder.tvPickOrderZone = (TextView) convertView.findViewById(R.id.tv_pick_order_zone);
			
			convertView.setTag(pickOrderHolder);
		} else {
			pickOrderHolder = (PickOrderHolder) convertView.getTag();
		}
		
		EnOrderPickSwitchInfo item = getItem(position);
		if(null != item){
			pickOrderHolder.tvPickOrderNumber.setText(item.get_orderNumber());
			
			String dateDeliver = item.get_requestedDeliveryDate();
			dateDeliver = BuManagement.converDatePatern(dateDeliver, GlobalParams.dateFormatYYYY_MM_DD_T_HHMMSS, 
					GlobalParams.dateFormatMMDDYYYY);
			pickOrderHolder.tvPickOrderDate.setText(dateDeliver);
			
			pickOrderHolder.tvPickOrderSubName.setText(item.get_customerName());
			
			int percenComplete = item.get_orderContainerPercentComplete();
			if(percenComplete > 0){
				String completeStr = languagPrf.getPreferencesString(GlobalParams.PICK_TXT_COMPLETE_KEY, 
					GlobalParams.PICK_TXT_COMPLETE_VALUE);
				pickOrderHolder.tvPickOrderComplete.setText(percenComplete + "% " + completeStr);
			}else {
				pickOrderHolder.tvPickOrderComplete.setVisibility(View.INVISIBLE);
			}
			pickOrderHolder.tvPickOrderZone.setText(strZone + ": " + item.get_zoneDescription());
		}
		
		return convertView;
	}

	/**
	 * update list PO
	 * @param list
	 */
	public void updateListPickOrder(ArrayList<EnOrderPickSwitchInfo> list){
		if(null != list){
			this.listPickOrders = new ArrayList<EnOrderPickSwitchInfo>();
			this.orignList = new ArrayList<EnOrderPickSwitchInfo>();
			this.listPickOrders = list;
			this.orignList = list;
		} else {
			this.listPickOrders = new ArrayList<EnOrderPickSwitchInfo>();
		}
	}
	
	/**
	 * getFilter
	 */
	public FilterList getFilter(){
		return filterList;
	}
	
	/**
	 * filter list Order
	 * @author Do Thin
	 *
	 */
	@SuppressLint("DefaultLocale") 
	public class FilterList extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence textSearch) {
			FilterResults results = new FilterResults();
			ArrayList<EnOrderPickSwitchInfo> resultsList = new ArrayList<EnOrderPickSwitchInfo>();
			if(null == textSearch ||StringUtils.isBlank(textSearch.toString())){
				resultsList = orignList;
				results.count = resultsList.size();
				results.values = resultsList;
			} else {
				for (EnOrderPickSwitchInfo item : orignList) {
					if(item.get_orderNumber().toLowerCase().contains(textSearch.toString().toLowerCase())){
						resultsList.add(item);
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
			listPickOrders = (ArrayList<EnOrderPickSwitchInfo>) results.values;
			notifyDataSetChanged();
		}
	}
}
