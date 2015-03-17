/**
 * Name: $RCSfile: AcPick.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/03/16 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.pick;

import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.appolis.adapter.PickOrderAdapter;
import com.appolis.adapter.ReceivingListAdapter;
import com.appolis.androidtablet.R;
import com.appolis.common.ErrorCode;
import com.appolis.common.LanguagePreferences;
import com.appolis.entities.EnOrderPickSwitchInfo;
import com.appolis.network.access.HttpNetServices;
import com.appolis.receiving.AcRecevingList;
import com.appolis.utilities.DataParser;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.StringUtils;
import com.appolis.utilities.Utilities;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class AcPick extends Activity implements OnClickListener, OnItemClickListener{
	private LinearLayout linBack;
	private LinearLayout linScan;
	private TextView tvHeader;
	private TextView tvPickTitle;
	private TextView edtPickOrderSearch;
	private LanguagePreferences languagePrefs;
	private ImageView imgClearTextSearch;
	private PullToRefreshListView lvPickOrder;
	
	//flag and variable
	private ArrayList<EnOrderPickSwitchInfo> listOrderPickSwitchInfos = new ArrayList<EnOrderPickSwitchInfo>();
	private PickOrderAdapter pickOrderAdapter = null;
	private boolean activityIsRunning;
	private String scanFlag = "";
	private String strLoading;
	private String strPoEmpty;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_receive_list);
		activityIsRunning = true;
		languagePrefs = new LanguagePreferences(getApplicationContext());
		
		getLanguage();
		initLayout();
	}
	
	/**
	 * get language from language package
	 */
	public void getLanguage(){
		strLoading= languagePrefs.getPreferencesString(GlobalParams.MAINLIST_MENLOADING_KEY,
				GlobalParams.MAINLIST_MENLOADING_VALUE);
		strPoEmpty = languagePrefs.getPreferencesString(GlobalParams.RE_PO_EMPTY_MSG_KEY, 
				GlobalParams.RE_PO_EMPTY_MSG_VALUE);
	}
	
	/**
	 * initial screen layout
	 */
	private void initLayout() {
		linBack = (LinearLayout) findViewById(R.id.lin_buton_home);
		linBack.setOnClickListener(this);
		linScan = (LinearLayout) findViewById(R.id.lin_buton_scan);
		linScan.setOnClickListener(this);
		linScan.setVisibility(View.VISIBLE);
		tvHeader = (TextView) findViewById(R.id.tv_header);
		tvHeader.setText(languagePrefs.getPreferencesString(GlobalParams.PICK_TITLE_PICK_KEY,
				GlobalParams.PICK_TITLE_PICK_VALUE));
		
		tvPickTitle = (TextView) findViewById(R.id.tv_receive_tile);
		tvPickTitle.setText(languagePrefs.getPreferencesString(GlobalParams.PICK_TITLE_PICK_KEY,
				GlobalParams.PICK_TITLE_PICK_VALUE) + ": ");
		edtPickOrderSearch = (TextView) findViewById(R.id.txt_receiver_search);
		imgClearTextSearch = (ImageView) findViewById(R.id.imgClearTextSearch);
		imgClearTextSearch.setVisibility(View.GONE);
		imgClearTextSearch.setOnClickListener(this);
		edtPickOrderSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence text, int start, int before, int count) {
				if (text.length() > 0) {
					imgClearTextSearch.setVisibility(View.VISIBLE);
					if(null != pickOrderAdapter){
						pickOrderAdapter.getFilter().filter(text.toString());
					}
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		lvPickOrder = (PullToRefreshListView) findViewById(R.id.lvPickOrderList);
		lvPickOrder.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		lvPickOrder.setOnItemClickListener(this);
		lvPickOrder.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(AcPick.this,
						System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy()
						.setLastUpdatedLabel(label);
				refreshData();
			}
		});
		pickOrderAdapter = new PickOrderAdapter(this, listOrderPickSwitchInfos);
		lvPickOrder.setAdapter(pickOrderAdapter);
		
		refreshData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lin_buton_home:
			Utilities.hideKeyboard(this);
			this.finish();
			break;

		case R.id.imgClearTextSearch:
			imgClearTextSearch.setVisibility(View.INVISIBLE);
			edtPickOrderSearch.setText("");
			pickOrderAdapter.getFilter().filter("");
			break;
			
		default:
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parentView, View arg1, int arg2, long arg3) {
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		activityIsRunning = false;
	}
	
	/**
	 * show alert dialog
	 * @param mContext
	 * @param strMessages
	 */
	public void showPopUp(final Context mContext, final String strMessages) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog_NoTitle);
		dialog.setContentView(R.layout.dialogwarning);
		// set the custom dialog components - text, image and button		
		TextView text2 = (TextView) dialog.findViewById(R.id.tvScantitle2);		
		text2.setText(strMessages);
		
		LanguagePreferences langPref = new LanguagePreferences(mContext);
		Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
		dialogButtonOk.setText(langPref.getPreferencesString(GlobalParams.OK, GlobalParams.OK));
		
		dialogButtonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				scanFlag = GlobalParams.FLAG_ACTIVE;
			}
		});
		dialog.show();
	}
	
	/**
	 * function to get data from service
	 */
	private void refreshData() {
		LoadPickOrderListAsyn mLoadDataTask = new LoadPickOrderListAsyn(AcPick.this);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mLoadDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			mLoadDataTask.execute();
		}
	}
	
	/**
	 * load order list data from service
	 * @author HoangNH11
	 *
	 */
	private class LoadPickOrderListAsyn extends AsyncTask<Void, Void, Integer>{
		Context context;
		ProgressDialog progressDialog;
		String response;
		
		public LoadPickOrderListAsyn(Context mContext){
			context = mContext;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(!isCancelled() && activityIsRunning){
				progressDialog = new ProgressDialog(context);
				progressDialog.setMessage(strLoading + "...");
				progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
	
							@Override
							public void onCancel(DialogInterface dialog) {
								cancel(true);
							}
						});
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.setCancelable(false);
				progressDialog.show();
				scanFlag = GlobalParams.FLAG_INACTIVE;
			}
		}
		
		@Override
		protected Integer doInBackground(Void... arg0) {
			int errorCode = ErrorCode.STATUS_SUCCESS;
			if(!Utilities.isConnected(context)){
				return ErrorCode.STATUS_NETWORK_NOT_CONNECT; 
			}
			
			try{
				if(!isCancelled() && activityIsRunning){
					response = HttpNetServices.Instance.getPickOrderList("");
					if(StringUtils.isBlank(response)){
						return ErrorCode.STATUS_RESPONSE_BLANK;
					} else {
						//parse to list order
						listOrderPickSwitchInfos = DataParser.getListOrderPickSwitchInfos(response);
						errorCode = ErrorCode.STATUS_SUCCESS;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				errorCode = ErrorCode.STATUS_EXCEPTION;
				if(e instanceof SocketTimeoutException || e instanceof  SocketException ||
						e instanceof ClientProtocolException || e instanceof ConnectTimeoutException ||
						e instanceof UnknownHostException || e instanceof MalformedURLException ){
					errorCode = ErrorCode.STATUS_NETWORK_NOT_CONNECT;
				}
			}
			
			return errorCode;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(null != progressDialog && (progressDialog.isShowing()) && activityIsRunning){
				progressDialog.dismiss();
			}
			
			if(null != lvPickOrder && activityIsRunning){
				lvPickOrder.onRefreshComplete();
			}
			
			if(!isCancelled() && activityIsRunning){
				switch (result) {
				case ErrorCode.STATUS_SUCCESS:
					scanFlag = GlobalParams.FLAG_ACTIVE;
					pickOrderAdapter.updateListPickOrder(listOrderPickSwitchInfos);
					pickOrderAdapter.notifyDataSetChanged();
					break;
				
				case ErrorCode.STATUS_RESPONSE_BLANK:
					showPopUp(context, strPoEmpty);
					scanFlag = GlobalParams.FLAG_ACTIVE;
					pickOrderAdapter.updateListPickOrder(listOrderPickSwitchInfos);
					pickOrderAdapter.notifyDataSetChanged();
					break;
				}
			} else {
				scanFlag = GlobalParams.FLAG_ACTIVE;
			}
		}
	}
}
