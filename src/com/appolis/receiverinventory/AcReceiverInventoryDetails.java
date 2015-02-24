/**
 * Name: $RCSfile: AcReceiverInventoryDetails.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.receiverinventory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.appolis.adapter.ReceiveInventoryAdapter;
import com.appolis.androidtablet.R;
import com.appolis.common.AppolisException;
import com.appolis.common.LanguagePreferences;
import com.appolis.entities.EnPO;
import com.appolis.network.NetParameter;
import com.appolis.network.access.HttpNetServices;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.Logger;
import com.appolis.utilities.Utilities;

/**
 * @author hoangnh11
 * Display receive details
 */
public class AcReceiverInventoryDetails extends Activity implements OnClickListener{

	private TextView tvHeader;
	private ImageView imgHome, imgScan;
	private Button btnCancel, btnOK;
	private ProgressDialog dialog;
	Bundle bundle;
	private EnPO po;
	private ListView lsPo;
	private ReceiveInventoryAdapter adapter;
	private TextView tvTotal, tvPO, tvTitlePO, tvTitleTotal;
	private LanguagePreferences languagePrefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		languagePrefs = new LanguagePreferences(getApplicationContext());
		setContentView(R.layout.receiver_inventory_details_layout);
		intLayout();
		
		GetLPNumberAsyncTask getLPNumberAsyncTask = new GetLPNumberAsyncTask();
		getLPNumberAsyncTask.execute();
	}

	/**
	 * instance layout
	 */
	private void intLayout() {
		tvHeader = (TextView) findViewById(R.id.tvHeader);
		tvHeader.setText(getResources().getString(R.string.RECEIVE_INVENTORY));
		imgHome = (ImageView) findViewById(R.id.imgHome);
		imgHome.setVisibility(View.VISIBLE);
		imgScan = (ImageView) findViewById(R.id.img_main_menu_scan_barcode);
		imgScan.setVisibility(View.GONE);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnOK = (Button) findViewById(R.id.btnOK);
		lsPo = (ListView) findViewById(R.id.lsPo);
		tvPO = (TextView) findViewById(R.id.tvPO);
		tvTitleTotal = (TextView) findViewById(R.id.tvTitleTotal);
		tvTitlePO = (TextView) findViewById(R.id.tvTitlePO);
		tvTotal = (TextView) findViewById(R.id.tvTotal);
		btnOK.setText(getLanguage(GlobalParams.OK, GlobalParams.OK));
		btnCancel.setText(getLanguage(GlobalParams.CANCEL, GlobalParams.CANCEL));
		tvTitlePO.setText(getLanguage(GlobalParams.PO, GlobalParams.PO) + GlobalParams.VERTICAL_TWO_DOT);
		tvTitleTotal.setText(getLanguage(GlobalParams.REST_LBL_TOTALLP, GlobalParams.TOTAL_LP_RECEIVED)
				+ GlobalParams.VERTICAL_TWO_DOT);
		
		imgHome.setOnClickListener(this);
		imgScan.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnOK.setOnClickListener(this);
		bundle = this.getBundle();
		po = new EnPO();		
	}

	/**
	 * Process event for views
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgHome:
			Utilities.hideKeyboard(this);
			finish();
			break;
		case R.id.btnCancel:
			finish();
			break;
		case R.id.btnOK:			
			SubmitReceiveInventoryAsyncTask submitReceiveInventoryAsyncTask = new SubmitReceiveInventoryAsyncTask();
			submitReceiveInventoryAsyncTask.execute();
			break;
		default:
			break;
		}
	}
	
	/**
	 * get language from language package
	 */
	public String getLanguage(String key, String value){
		return languagePrefs.getPreferencesString(key, value);
	}
	
	/**
	 * Pass object
	 * @author hoangnh11
	 */
	class GetLPNumberAsyncTask extends AsyncTask<Void, Void, String> {
		String data;
		Intent intent;
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(AcReceiverInventoryDetails.this);
			dialog.setMessage(GlobalParams.LOADING_DATA);
			dialog.show();
			dialog.setCancelable(false); 
			dialog.setCanceledOnTouchOutside(false);
		}
		
		@Override
		protected String doInBackground(Void... params) {
			String result;
			if (!isCancelled()) {
				try {					
					po = (EnPO) bundle.getSerializable(GlobalParams.PO_OBJECT);
					result = "true";
				} catch (AppolisException e) {
					result = "false";
				} catch (Exception e) {
					result = "false";
				}
			} else {
				result = "false";
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			// If not cancel by user
			if (!isCancelled()) {
				if (result.equals("true")) {
					if (po != null && po.getLpNumber() != null) {
						tvPO.setText(po.getPONumber());
						tvTotal.setText(po.getTotalLpReceived() + GlobalParams.SPACE 
								+ getResources().getString(R.string.OF) + GlobalParams.SPACE
								+ po.getTotalLpShipped());
						adapter = new ReceiveInventoryAdapter(AcReceiverInventoryDetails.this, po);
						lsPo.setAdapter(adapter);
					} else {
						Utilities.showPopUp(AcReceiverInventoryDetails.this, null, getResources().getString(R.string.INVALID_LICENSE_PLATE));
					}
				} else {
					Utilities.showPopUp(AcReceiverInventoryDetails.this, null, getResources().getString(R.string.INVALID_LICENSE_PLATE));
				}
			}
		}
	}
	
	/**
	 * Submit receipts
	 * @author hoangnh11
	 */
	class SubmitReceiveInventoryAsyncTask extends AsyncTask<Void, Void, String> {
		String data;
		Intent intent;
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(AcReceiverInventoryDetails.this);
			dialog.setMessage(GlobalParams.PROCESS_DATA);
			dialog.show();
			dialog.setCancelable(false); 
			dialog.setCanceledOnTouchOutside(false);
		}
		
		@Override
		protected String doInBackground(Void... params) {
			String result;
			if (!isCancelled()) {
				try {					
					NetParameter[] netParameter = new NetParameter[3];
					netParameter[0] = new NetParameter("POID", String.valueOf(po.getPOID()));
					netParameter[1] = new NetParameter("PONumber", po.getPONumber());
					netParameter[2] = new NetParameter("LPNumber", po.getLpNumber());
					data = HttpNetServices.Instance.submitReceiveInventory(netParameter);
					Logger.error(data);
					result = "true";
				} catch (AppolisException e) {
					result = "false";
				} catch (Exception e) {
					result = "false";
				}
			} else {
				result = "false";
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			// If not cancel by user
			if (!isCancelled()) {
				if (result.equals("true")) {
					finish();
					intent = new Intent(AcReceiverInventoryDetails.this, AcReceiverScanLP.class);
	    			startActivity(intent);
				} else {
					Utilities.showPopUp(AcReceiverInventoryDetails.this, null, getResources().getString(R.string.SUBMIT_FAILE));
				}
			}
		}
	}
	
	/**
	 * instance bundle
	 * @return
	 */
	public Bundle getBundle()
    {
        Bundle bundle = this.getIntent().getExtras();
        
        if (bundle == null)
        {
            bundle = new Bundle();
        }
        
        return bundle;
    }
}
