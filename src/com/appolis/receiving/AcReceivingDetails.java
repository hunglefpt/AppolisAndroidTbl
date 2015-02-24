/**
 * Name: $RCSfile: AcReceivingDetails.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.receiving;

import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appolis.adapter.ReceivingDetailAdapter;
import com.appolis.androidtablet.R;
import com.appolis.common.ErrorCode;
import com.appolis.common.LanguagePreferences;
import com.appolis.entities.EnBarcodeExistences;
import com.appolis.entities.EnItemLotInfo;
import com.appolis.entities.EnPurchaseOrderInfo;
import com.appolis.entities.EnPurchaseOrderItemInfo;
import com.appolis.network.NetParameter;
import com.appolis.network.access.HttpNetServices;
import com.appolis.scan.CaptureBarcodeCamera;
import com.appolis.scan.SingleEntryApplication;
import com.appolis.utilities.CommontDialog;
import com.appolis.utilities.DataParser;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.Logger;
import com.appolis.utilities.StringUtils;
import com.appolis.utilities.Utilities;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshSwipeListView;

/**
 * @author Hoangnh11 Display Receiving details screen
 * 
 */
public class AcReceivingDetails extends Activity implements OnClickListener,
		OnItemClickListener {
	private LinearLayout linBack;
	private LinearLayout linScan;
	private TextView tvHeader;
	private TextView tvReceiveNumber;
	private TextView tvReceiveDetailName;
	private ImageView imgReceiveDetailInfo;
	private PullToRefreshSwipeListView lvReceiveDetailList;
	private SwipeListView swipeList;
	private Button btOk;
	private Button btCancel;
	private String poNumber;
	private ArrayList<EnPurchaseOrderItemInfo> listEnPurchaseOrderItemInfo = new ArrayList<EnPurchaseOrderItemInfo>();
	private EnPurchaseOrderInfo enPurchaseOrderInfos;
	private ReceivingDetailAdapter receivingDetailAdapter;
	private EnItemLotInfo enItemLotInfo;
	private LanguagePreferences languagePrefs;

	// multiple language
	private String strTextOk;
	private String strTextCancel;
	private String strLoading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_receive_details);

		Bundle bundle = getIntent().getExtras();
		if (bundle.containsKey(GlobalParams.PARAM_EN_RECIVING_INFO_PO_NUMBER)) {
			poNumber = bundle
					.getString(GlobalParams.PARAM_EN_RECIVING_INFO_PO_NUMBER);
		}

		languagePrefs = new LanguagePreferences(getApplicationContext());
		getLanguage();
		initLayout();
	}

	/**
	 * initial layout of screen
	 */
	public void initLayout() {
		linBack = (LinearLayout) findViewById(R.id.lin_buton_home);
		linBack.setVisibility(View.GONE);
		linScan = (LinearLayout) findViewById(R.id.lin_buton_scan);
		linScan.setOnClickListener(this);
		linScan.setVisibility(View.VISIBLE);
		tvHeader = (TextView) findViewById(R.id.tv_header);
		tvHeader.setText(languagePrefs.getPreferencesString(
				GlobalParams.RD_TITLE_RECEIVEDETAIL_KEY,
				GlobalParams.RD_TITLE_RECEIVEDETAIL_VALUE));

		tvReceiveNumber = (TextView) findViewById(R.id.tvReceiveNumber);
		if (StringUtils.isNotBlank(poNumber)) {
			String requestKey = languagePrefs.getPreferencesString(
					GlobalParams.DMG_LBL_RECEIVEREQUEST_KEY,
					GlobalParams.DMG_LBL_RECEIVEREQUEST_TEXT);
			tvReceiveNumber.setText(requestKey + ": " + poNumber);
		}

		tvReceiveDetailName = (TextView) findViewById(R.id.tvReceiveDetailName);
		imgReceiveDetailInfo = (ImageView) findViewById(R.id.img_receive_detail_info);
		imgReceiveDetailInfo.setOnClickListener(this);
		imgReceiveDetailInfo.setVisibility(View.INVISIBLE);
		lvReceiveDetailList = (PullToRefreshSwipeListView) findViewById(R.id.tv_receive_detail_list);
		lvReceiveDetailList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		lvReceiveDetailList.setOnItemClickListener(this);
		swipeList = lvReceiveDetailList.getRefreshableView();
		setListData();
		lvReceiveDetailList
				.setOnRefreshListener(new OnRefreshListener2<SwipeListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<SwipeListView> refreshView) {
						// TODO Auto-generated method stub
						String label = DateUtils.formatDateTime(
								AcReceivingDetails.this,
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);
						// Update the LastUpdatedLabel
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						refreshData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<SwipeListView> refreshView) {
					}
				});

		btCancel = (Button) findViewById(R.id.btn_receive_detail_Cancel);
		btCancel.setText(strTextCancel);
		btCancel.setOnClickListener(this);

		btOk = (Button) findViewById(R.id.btn_receive_detail_Ok);
		btOk.setText(strTextOk);
		btOk.setOnClickListener(this);

		refreshData();
	}

	private void setListData() {
		receivingDetailAdapter = new ReceivingDetailAdapter(this,
				listEnPurchaseOrderItemInfo, enPurchaseOrderInfos);
		lvReceiveDetailList.setAdapter(receivingDetailAdapter);
		swipeList.setSwipeListViewListener(new BaseSwipeListViewListener() {
			@Override
			public void onClickFrontView(final int position) {
				Logger.error("Appolis: AcReceivingDetails #onClickFrontView: " + position);
				EnPurchaseOrderItemInfo item = receivingDetailAdapter
						.getItem(position - 1);
				Intent itemDetailsIntent = new Intent(AcReceivingDetails.this,
						AcReceiveItemDetail.class);
				itemDetailsIntent.putExtra(
						GlobalParams.PARAM_EN_PURCHASE_ORDER_ITEM_INFOS, item);
				itemDetailsIntent.putExtra(GlobalParams.PARAM_EN_PURCHASE_ORDER_INFOS,
						enPurchaseOrderInfos);
				startActivityForResult(itemDetailsIntent,
						GlobalParams.AC_RECEIVING_ITEM_DETAILS_ACTIVITY);
			}

			@Override
			public void onClickBackView(final int position) {
				Logger.error("Appolis: AcReceivingDetails #onClickBackView: " + position);
			}

			@Override
			public void onOpened(int position, boolean toRight) {
				// TODO Auto-generated method stub
				super.onOpened(position - 1, toRight);
			}

			@Override
			public void onMove(int position, float x) {
				// TODO Auto-generated method stub
				super.onMove(position - 1, x);
			}

			@Override
			public int onChangeSwipeMode(int position) {
				// TODO Auto-generated method stub
				return SwipeListView.SWIPE_MODE_DEFAULT;
			}

			@Override
			public void onStartOpen(int position, int action, boolean right) {
				// TODO Auto-generated method stub
				super.onStartOpen(position - 1, action, right);

			}
		});
		lvReceiveDetailList.setLongClickable(true);
		swipeList.setSwipeOpenOnLongPress(false);
	}

	/**
	 * get language from language package
	 */
	public void getLanguage() {
		strTextOk = languagePrefs.getPreferencesString(GlobalParams.OK_KEY,
				GlobalParams.OK_KEY);
		strTextCancel = languagePrefs.getPreferencesString(
				GlobalParams.SETTINGS_BTN_CANCEL_KEY,
				GlobalParams.SETTINGS_BTN_CANCEL_VALUE);
		strLoading = languagePrefs.getPreferencesString(
				GlobalParams.MAINLIST_MENLOADING_KEY,
				GlobalParams.MAINLIST_MENLOADING_VALUE);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// register to receive notifications from SingleEntryApplication
		// these notifications originate from ScanAPI
		IntentFilter filter;

		filter = new IntentFilter(SingleEntryApplication.NOTIFY_SCANNER_ARRIVAL);
		registerReceiver(this._newItemsReceiver, filter);

		filter = new IntentFilter(SingleEntryApplication.NOTIFY_SCANNER_REMOVAL);
		registerReceiver(this._newItemsReceiver, filter);

		filter = new IntentFilter(SingleEntryApplication.NOTIFY_DECODED_DATA);
		registerReceiver(this._newItemsReceiver, filter);

		// increasing the Application View count from 0 to 1 will
		// cause the application to open and initialize ScanAPI
		SingleEntryApplication.getApplicationInstance().increaseViewCount();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lin_buton_scan:
			Intent intentScan = new Intent(this, CaptureBarcodeCamera.class);
			startActivityForResult(intentScan,
					GlobalParams.CAPTURE_BARCODE_CAMERA_ACTIVITY);
			break;

		case R.id.btn_receive_detail_Cancel:
			this.finish();
			break;

		case R.id.img_receive_detail_info:
			if (null != enPurchaseOrderInfos
					&& StringUtils.isNotBlank(enPurchaseOrderInfos
							.get_comments())) {
				CommontDialog.showErrorDialog(this,
						enPurchaseOrderInfos.get_comments(), "Alert");
			}
			break;

		case R.id.btn_receive_detail_Ok:
			if (null != enPurchaseOrderInfos) {
				processOkClick(enPurchaseOrderInfos.get_purchaseOrderID());
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(android.widget.AdapterView<?> parentView,
			View view, int position, long id) {
		Log.e("Appolis", "AcReceivingDetails #onItemClick: " + position);
		EnPurchaseOrderItemInfo item = receivingDetailAdapter
				.getItem(position - 1);
		Intent itemDetailsIntent = new Intent(AcReceivingDetails.this,
				AcReceiveItemDetail.class);
		itemDetailsIntent.putExtra(
				GlobalParams.PARAM_EN_PURCHASE_ORDER_ITEM_INFOS, item);
		itemDetailsIntent.putExtra(GlobalParams.PARAM_EN_PURCHASE_ORDER_INFOS,
				enPurchaseOrderInfos);
		startActivityForResult(itemDetailsIntent,
				GlobalParams.AC_RECEIVING_ITEM_DETAILS_ACTIVITY);
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case GlobalParams.AC_RECEIVING_ITEM_DETAILS_ACTIVITY:
			if (resultCode == RESULT_OK) {
				refreshData();
			}
			break;

		case GlobalParams.CAPTURE_BARCODE_CAMERA_ACTIVITY:
			if (resultCode == RESULT_OK) {
				String message = data.getStringExtra(GlobalParams.RESULTSCAN);
				Log.e("Appolis", "CAPTURE_BARCODE_CAMERA_ACTIVITY #RESULT_OK:"
						+ message);
				processScanData(message);
			}
			break;

		default:
			break;
		}
	}

	/**
	 * handler for receiving the notifications coming from
	 * SingleEntryApplication. Update the UI accordingly when we receive a
	 * notification
	 */
	private final BroadcastReceiver _newItemsReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			// a Scanner has connected
			if (intent.getAction().equalsIgnoreCase(
					SingleEntryApplication.NOTIFY_SCANNER_ARRIVAL)) {
				linScan.setVisibility(View.GONE);
			}

			// a Scanner has disconnected
			else if (intent.getAction().equalsIgnoreCase(
					SingleEntryApplication.NOTIFY_SCANNER_REMOVAL)) {
				linScan.setVisibility(View.VISIBLE);
			}

			// decoded Data received from a scanner
			else if (intent.getAction().equalsIgnoreCase(
					SingleEntryApplication.NOTIFY_DECODED_DATA)) {
				char[] data = intent
						.getCharArrayExtra(SingleEntryApplication.EXTRA_DECODEDDATA);
				String message = new String(data);
				// edtLp.setText(new String(data));
				processScanData(message);
			}
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		// unregister the notifications
		unregisterReceiver(_newItemsReceiver);
		// indicate this view has been destroyed
		// if the reference count becomes 0 ScanAPI can
		// be closed if this is not a screen rotation scenario
		SingleEntryApplication.getApplicationInstance().decreaseViewCount();
	}

	/**
	 * get EnPurchaseOrderItemInfo from list object
	 * 
	 * @param listToSearch
	 * @param infoSearch
	 * @return
	 */
	public EnPurchaseOrderItemInfo getEnPurchaseOrderItemInfoFromList(
			ArrayList<EnPurchaseOrderItemInfo> listToSearch,
			EnItemLotInfo infoSearch) {
		EnPurchaseOrderItemInfo item = null;

		if (null == listToSearch || (listToSearch.size() == 0)) {
			return null;
		}

		for (EnPurchaseOrderItemInfo enPurchaseOrderItemInfo : listToSearch) {
			if (infoSearch.get_itemID() == enPurchaseOrderItemInfo.get_itemID()) {
				item = enPurchaseOrderItemInfo;
			}
		}

		return item;
	}

	/**
	 * set enable or disable button OK
	 * 
	 * @param state
	 *            state of button
	 */
	public void setButtonOkStatus(boolean state) {
		btOk.setEnabled(state);
	}

	/**
	 * refreshData
	 */
	private void refreshData() {
		LoadReceiveDetailAsyn mLoadDataTask = new LoadReceiveDetailAsyn(
				AcReceivingDetails.this, poNumber);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mLoadDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			mLoadDataTask.execute();
		}
	}

	private class LoadReceiveDetailAsyn extends AsyncTask<Void, Void, Integer> {
		Context context;
		ProgressDialog progressDialog;
		String response;
		String receiveItemID;

		public LoadReceiveDetailAsyn(Context mContext, String poID) {
			this.context = mContext;
			this.receiveItemID = poID;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage(strLoading);
			progressDialog
					.setOnCancelListener(new DialogInterface.OnCancelListener() {

						@Override
						public void onCancel(DialogInterface dialog) {
							cancel(true);
						}
					});
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(true);
			progressDialog.show();

		}

		@Override
		protected Integer doInBackground(Void... arg0) {
			int errorCode = 0;
			if (!Utilities.isConnected(context)) {
				return ErrorCode.STATUS_NETWORK_NOT_CONNECT;
			}

			try {
				if (!isCancelled()) {
					response = HttpNetServices.Instance
							.getReceiveDetail(URLEncoder.encode(receiveItemID,
									GlobalParams.UTF_8));
					Log.e("Appolis", "LoadReceiveDetailAsyn #response:"
							+ response);
					enPurchaseOrderInfos = DataParser
							.getEnPurchaseOrderInfo(response);
					listEnPurchaseOrderItemInfo = enPurchaseOrderInfos
							.get_items();
				}
			} catch (Exception e) {
				e.printStackTrace();
				errorCode = 2;
				if (e instanceof SocketException
						|| e instanceof UnknownHostException
						|| e instanceof SocketTimeoutException
						|| e instanceof ConnectTimeoutException
						|| e instanceof ClientProtocolException) {
					errorCode = ErrorCode.STATUS_NETWORK_NOT_CONNECT;
				}
			}

			return errorCode;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (null != progressDialog && (progressDialog.isShowing())) {
				progressDialog.dismiss();
			}

			if (!isCancelled()) {
				lvReceiveDetailList.onRefreshComplete();
				switch (result) {
				case 0: // success
					if (null != enPurchaseOrderInfos
							&& null != listEnPurchaseOrderItemInfo) {
						if (StringUtils.isNotBlank(enPurchaseOrderInfos
								.get_comments())) {
							imgReceiveDetailInfo.setVisibility(View.VISIBLE);
						} else {
							imgReceiveDetailInfo.setVisibility(View.INVISIBLE);
						}
						tvReceiveDetailName.setText(enPurchaseOrderInfos
								.get_vendorName());
						receivingDetailAdapter
								.updateListReciver(listEnPurchaseOrderItemInfo);
						receivingDetailAdapter
								.updateEnPuscharInfo(enPurchaseOrderInfos);
						receivingDetailAdapter.notifyDataSetChanged();
					}
					break;

				case ErrorCode.STATUS_NETWORK_NOT_CONNECT: // no network
					String msg = languagePrefs.getPreferencesString(
							GlobalParams.ERRORUNABLETOCONTACTSERVER,
							GlobalParams.ERROR_INVALID_NETWORK);
					CommontDialog.showErrorDialog(context, msg, null);
					break;

				default:
					Log.e("Appolis", "LoadReceiveListAsyn #onPostExecute: "
							+ result);
					CommontDialog.showErrorDialog(context, response, null);
					break;
				}
			}
		}
	}

	/**
	 * process scan data
	 */
	private void processScanData(String barcode) {
		ProcessScanDataAsyn mLoadDataTask = new ProcessScanDataAsyn(
				AcReceivingDetails.this, barcode);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mLoadDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			mLoadDataTask.execute();
		}
	}

	private class ProcessScanDataAsyn extends AsyncTask<Void, Void, Integer> {
		Context context;
		ProgressDialog progressDialog;
		String response;
		String barcode;

		public ProcessScanDataAsyn(Context mContext, String barcode) {
			this.context = mContext;
			this.barcode = barcode;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (!isCancelled()) {
				progressDialog = new ProgressDialog(context);
				progressDialog.setMessage(strLoading);
				progressDialog
						.setOnCancelListener(new DialogInterface.OnCancelListener() {

							@Override
							public void onCancel(DialogInterface dialog) {
								cancel(true);
							}
						});
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.setCancelable(true);
				progressDialog.show();
			}
		}

		@Override
		protected Integer doInBackground(Void... arg0) {
			int errorCode = 0;
			if (!Utilities.isConnected(context)) {
				return ErrorCode.STATUS_NETWORK_NOT_CONNECT;
			}

			try {
				if (!isCancelled()) {
					NetParameter[] netParameters = new NetParameter[1];
					netParameters[0] = new NetParameter("barcode",
							URLEncoder.encode(barcode, GlobalParams.UTF_8));
					response = HttpNetServices.Instance
							.getBarcode(netParameters);
					Log.e("Appolis",
							"LoadReceiveDetailAsyn #getBarcode #response:"
									+ response);
					EnBarcodeExistences enBarcodeExistences = DataParser
							.getBarcode(response);
					if (null != enBarcodeExistences) {
						if (enBarcodeExistences.getBinOnlyCount() == 0
								&& enBarcodeExistences.getGtinCount() == 0
								&& enBarcodeExistences
										.getItemIdentificationCount() == 0
								&& enBarcodeExistences.getItemOnlyCount() == 0
								&& enBarcodeExistences.getLotOnlyCount() == 0
								&& enBarcodeExistences.getLPCount() == 0
								&& enBarcodeExistences.getOrderCount() == 0
								&& enBarcodeExistences.getPoCount() == 0
								&& enBarcodeExistences.getUOMBarcodeCount() == 0) {
							// errorscan
							return ErrorCode.STATUS_SCAN_ERROR;
						} else if (enBarcodeExistences.getItemOnlyCount() != 0
								|| (enBarcodeExistences
										.getItemIdentificationCount() != 0)
								|| (enBarcodeExistences.getUOMBarcodeCount() != 0)) {
							// Item Number, UOM or ItemIdentification
							response = HttpNetServices.Instance
									.getReceiveItemDetailWithBarcode(URLEncoder
											.encode(barcode, GlobalParams.UTF_8));
							Log.e("Appolis",
									"LoadReceiveDetailAsyn #getReceiveItemDetailWithBarcode #response:"
											+ response);

							enItemLotInfo = DataParser
									.getEnItemLotInfo(response);
							if (null == enItemLotInfo) {
								return ErrorCode.STATUS_EXCEPTION;
							}
						} else {
							return ErrorCode.STATUS_SCAN_UNSUPPORTED_BARCODE;
						}
					} else {
						errorCode = ErrorCode.STATUS_EXCEPTION; // exception
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				errorCode = ErrorCode.STATUS_EXCEPTION;
				if (e instanceof SocketTimeoutException
						|| e instanceof SocketException
						|| e instanceof ClientProtocolException
						|| e instanceof ConnectTimeoutException
						|| e instanceof UnknownHostException
						|| e instanceof MalformedURLException) {
					errorCode = ErrorCode.STATUS_NETWORK_NOT_CONNECT;
				} else if (e instanceof URISyntaxException) {
					errorCode = ErrorCode.STATUS_SCAN_ERROR;
				}
			}

			return errorCode;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (null != progressDialog && (progressDialog.isShowing())) {
				progressDialog.dismiss();
			}

			if (!isCancelled()) {
				switch (result) {
				case 0: // success
					EnPurchaseOrderItemInfo item = getEnPurchaseOrderItemInfoFromList(
							listEnPurchaseOrderItemInfo, enItemLotInfo);
					if (null != item) {
						// item in PO list
						if (checkItemIsCompleted(item)) {
							// item is completed
							Toast.makeText(
									context,
									languagePrefs
											.getPreferencesString(
													GlobalParams.CMM_COMPLETE_MSG_KEY,
													GlobalParams.CMM_COMPLETE_MSG_VALUE),
									GlobalParams.TOAT_TIME).show();
						} else {
							// item is not complete
							Intent itemDetailsIntent = new Intent(
									AcReceivingDetails.this,
									AcReceiveItemDetail.class);
							itemDetailsIntent
									.putExtra(
											GlobalParams.PARAM_EN_PURCHASE_ORDER_ITEM_INFOS,
											item);
							itemDetailsIntent.putExtra(
									GlobalParams.PARAM_EN_PURCHASE_ORDER_INFOS,
									enPurchaseOrderInfos);
							itemDetailsIntent.putExtra(
									GlobalParams.PARAM_EN_ITEM_LOT_INFO,
									enItemLotInfo);
							startActivityForResult(
									itemDetailsIntent,
									GlobalParams.AC_RECEIVING_ITEM_DETAILS_ACTIVITY);
						}
					} else {
						// item not in PO list
						String strErrorScan = languagePrefs
								.getPreferencesString(
										GlobalParams.ITEM_NOT_IN_PO_KEY,
										GlobalParams.ITEM_NOT_IN_PO_VALUE);
						Utilities.showPopUp(context, null, strErrorScan);
					}

					break;

				case ErrorCode.STATUS_SCAN_ERROR:
					// error scan
					String strErrorScan = languagePrefs.getPreferencesString(
							GlobalParams.SCAN_NOTFOUND_KEY,
							GlobalParams.SCAN_NOTFOUND_VALUE);
					Utilities.showPopUp(context, null, strErrorScan);
					break;

				case ErrorCode.STATUS_SCAN_UNSUPPORTED_BARCODE:
					// Unsupported barcode
					String strUnSupport = languagePrefs.getPreferencesString(
							GlobalParams.RD_INVALID_BARCODE_MSG_KEY,
							GlobalParams.RD_INVALID_BARCODE_MSG_VALUE);
					Utilities.showPopUp(context, null, strUnSupport);
					break;

				case ErrorCode.STATUS_NETWORK_NOT_CONNECT: // no network
					String msg = languagePrefs.getPreferencesString(
							GlobalParams.ERRORUNABLETOCONTACTSERVER,
							GlobalParams.ERROR_INVALID_NETWORK);
					CommontDialog.showErrorDialog(context, msg, null);
					break;

				default:
					String msgFail = response;
					// languagePrefs.getPreferencesString(GlobalParams.ERRORUNABLETOCONTACTSERVER,
					// GlobalParams.ERROR_INVALID_NETWORK);
					CommontDialog.showErrorDialog(context, msgFail, null);
					break;
				}
			}
		}
	}

	/**
	 * check item is complete or not complete
	 * 
	 * @param item
	 * @return
	 */
	public boolean checkItemIsCompleted(EnPurchaseOrderItemInfo item) {
		if (null != enPurchaseOrderInfos) {
			float over = enPurchaseOrderInfos.get_vendorReceivePercentOver();
			if (over < 0) {
				// input unlimited
				return false;
			} else if (over == 0) {
				// don't allow input over ordered quatity
				if (item.get_quantityReceived() >= item.get_quantityOrdered()) {
					return true;
				} else {
					return false;
				}
			} else {
				// don't allow input over 'percentOver'
				if (item.get_quantityReceived() >= (item.get_quantityOrdered() * over)) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			return true;
		}
	}

	/**
	 * process data when user click button OK
	 * 
	 * @param purchaseOrderId
	 */
	private void processOkClick(int purchaseOrderId) {
		ProcessOkClickAsyn mLoadDataTask = new ProcessOkClickAsyn(
				AcReceivingDetails.this, purchaseOrderId);
		// mLoadDataTask.setMoveTopOnComplete(isMoveToTop);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mLoadDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			mLoadDataTask.execute();
		}
	}

	/**
	 * 
	 * @author
	 * 
	 */
	private class ProcessOkClickAsyn extends AsyncTask<Void, Void, Integer> {
		Context context;
		ProgressDialog progressDialog;
		String response;
		int purChaseId;

		public ProcessOkClickAsyn(Context mContext, int purChaseId) {
			this.context = mContext;
			this.purChaseId = purChaseId;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (!isCancelled()) {
				progressDialog = new ProgressDialog(context);
				progressDialog.setMessage(strLoading);
				progressDialog
						.setOnCancelListener(new DialogInterface.OnCancelListener() {

							@Override
							public void onCancel(DialogInterface dialog) {
								cancel(true);
							}
						});
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.setCancelable(true);
				progressDialog.show();
			}
		}

		@Override
		protected Integer doInBackground(Void... arg0) {
			int errorCode = 0;
			if (!Utilities.isConnected(context)) {
				return 1;
			}

			try {
				if (!isCancelled()) {
					response = HttpNetServices.Instance
							.commitReceiveDetail(purChaseId);
					Log.e("Appolis", "ProcessOkClickAsyn with ID:" + purChaseId
							+ " #response:" + response);
					if (StringUtils.isBlank(response)) {
						errorCode = 0;
					} else {
						errorCode = 2;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				errorCode = 2;
			}

			return errorCode;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (null != progressDialog && (progressDialog.isShowing())) {
				progressDialog.dismiss();
			}

			if (!isCancelled()) {
				switch (result) {
				case 0: // success
					setResult(RESULT_OK);
					AcReceivingDetails.this.finish();
					break;

				case 1: // no network
					String msg = languagePrefs.getPreferencesString(
							GlobalParams.ERRORUNABLETOCONTACTSERVER,
							GlobalParams.ERROR_INVALID_NETWORK);
					CommontDialog.showErrorDialog(context, msg, null);
					break;

				default:
					Log.e("Appolis", "LoadReceiveListAsyn #onPostExecute: "
							+ result);
					String mssg = languagePrefs.getPreferencesString(
							GlobalParams.ERRORUNABLETOCONTACTSERVER,
							GlobalParams.ERROR_INVALID_NETWORK);
					CommontDialog.showErrorDialog(context, mssg, null);
					break;
				}
			}
		}
	}
}
