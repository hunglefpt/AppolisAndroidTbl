/**
 * Name: $RCSfile: AcMove.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.move;

import java.net.URLEncoder;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.appolis.androidtablet.R;
import com.appolis.common.AppolisException;
import com.appolis.common.LanguagePreferences;
import com.appolis.entities.EnBarcodeExistences;
import com.appolis.network.NetParameter;
import com.appolis.network.access.HttpNetServices;
import com.appolis.scan.CaptureBarcodeCamera;
import com.appolis.scan.SingleEntryApplication;
import com.appolis.utilities.DataParser;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.Utilities;

/**
 * @author hoangnh11
 * Display Move screen for search items
 */
public class AcMove extends Activity implements OnClickListener{

	private TextView tvHeader;
	private ImageView imgHome, imgScan;
	private Button btnCancel, btnOK;
	private String message;
	private ProgressDialog dialog;
	private EditText edtItem;
	private EnBarcodeExistences enBarcodeExistences;
	private ImageView imgClear;
	private LanguagePreferences languagePrefs;
	private TextView tvSelect;
	private boolean activityIsRunning = false;
	private String scanFlag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		languagePrefs = new LanguagePreferences(getApplicationContext());
		setContentView(R.layout.move_layout);
		activityIsRunning = true;
		intLayout();
	}

	/**
	 * instance layout
	 */
	private void intLayout() {
		tvHeader = (TextView) findViewById(R.id.tvHeader);
		tvHeader.setText(getLanguage(GlobalParams.MV_TITLE_MOVE, GlobalParams.MOVE));
		imgHome = (ImageView) findViewById(R.id.imgHome);
		imgHome.setVisibility(View.VISIBLE);
		imgScan = (ImageView) findViewById(R.id.img_main_menu_scan_barcode);		
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnOK = (Button) findViewById(R.id.btnOK);
		edtItem = (EditText) findViewById(R.id.edtItem);
		imgClear = (ImageView) findViewById(R.id.imgClear);
		tvSelect = (TextView) findViewById(R.id.tvSelect);
		
		tvSelect.setText(getLanguage(GlobalParams.SELECT, GlobalParams.SELECT));
		edtItem.setHint(getLanguage(GlobalParams.SCANITEM, GlobalParams.SCAN_ITEM_OR_LICENCE_PLATE));
		btnOK.setText(getLanguage(GlobalParams.OK, GlobalParams.OK));
		btnCancel.setText(getLanguage(GlobalParams.CANCEL, GlobalParams.CANCEL));

		imgHome.setOnClickListener(this);
		imgScan.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnOK.setOnClickListener(this);
		imgClear.setOnClickListener(this);
		
		enBarcodeExistences = new EnBarcodeExistences();
		edtItem.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					BarcodeAsyncTask barcodeAsyncTask = new BarcodeAsyncTask();
			        barcodeAsyncTask.execute();
	            }
				return false;
			}
		});	
	}
	
	/**
	 * get language from language package
	 */
	public String getLanguage(String key, String value){
		return languagePrefs.getPreferencesString(key, value);
	}
	
	/**
	 * Process event for views
	 */
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		
		case R.id.imgHome:
			Utilities.hideKeyboard(this);
			finish();
			break;
			
		case R.id.btnCancel:
			finish();
			break;
			
		case R.id.btnOK:
			BarcodeAsyncTask barcodeAsyncTask = new BarcodeAsyncTask();
	        barcodeAsyncTask.execute();			
			break;
			
		case R.id.img_main_menu_scan_barcode:
			intent = new Intent(this, CaptureBarcodeCamera.class);		
			startActivityForResult(intent, GlobalParams.AC_MOVE_LEVEL_ONE);
			break;
			
		case R.id.imgClear:
			edtItem.setText(GlobalParams.BLANK_CHARACTER);
			break;

		default:
			break;
		}
	}
	
	/**
	 * Process activity
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		activityIsRunning = true;
		switch (requestCode) {
		case GlobalParams.AC_MOVE_LEVEL_ONE:
			if(resultCode == RESULT_OK){
			   message = data.getStringExtra(GlobalParams.RESULTSCAN);
			   edtItem.setText(message);
               BarcodeAsyncTask barcodeAsyncTask = new BarcodeAsyncTask();
               barcodeAsyncTask.execute();
			}
			
			break;
	
		default:
			break;
		}
	}
	
	/**
	 * Register Receiver
	 */
	public void onRegisterReceiver() {
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
	
	/**
	 * Unregister Receiver
	 */
	public void onUnregisterReceiver() {
		// unregister the notifications
		unregisterReceiver(_newItemsReceiver);        
        // indicate this view has been destroyed
        // if the reference count becomes 0 ScanAPI can
        // be closed if this is not a screen rotation scenario
        SingleEntryApplication.getApplicationInstance().decreaseViewCount();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		activityIsRunning = true;
		onRegisterReceiver();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		activityIsRunning = false;
		onUnregisterReceiver();
	}
	
	/**
	 * Check Item or License Plate
	 * @author hoangnh11
	 */
	class BarcodeAsyncTask extends AsyncTask<Void, Void, String> {
		String data;
		Intent intent;
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(AcMove.this);
			dialog.setMessage(getLanguage(GlobalParams.LOADING_MSG, GlobalParams.LOADING_DATA));
			dialog.show();
			dialog.setCancelable(false); 
			dialog.setCanceledOnTouchOutside(false);
			scanFlag = GlobalParams.FLAG_INACTIVE;
		}
		
		@Override
		protected String doInBackground(Void... params) {
			String result;
			if (!isCancelled()) {
				try {
					NetParameter[] netParameter = new NetParameter[1];
					netParameter[0] = new NetParameter("barcode", 
							URLEncoder.encode(edtItem.getEditableText().toString().trim(), GlobalParams.UTF_8));
					data = HttpNetServices.Instance.getBarcode(netParameter);
					enBarcodeExistences = DataParser.getBarcode(data);
					result = GlobalParams.TRUE;
				} catch (AppolisException e) {
					result = GlobalParams.FALSE;
				} catch (Exception e) {
					result = GlobalParams.FALSE;
				}
			} else {
				result = GlobalParams.FALSE;
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (activityIsRunning) {
				dialog.dismiss();
				// If not cancel by user
				if (!isCancelled()) {
					if (result.equals(GlobalParams.TRUE)) {
						if (enBarcodeExistences != null) {
							if (enBarcodeExistences.getBinOnlyCount() == 0 
									&& enBarcodeExistences.getGtinCount() == 0
									&& enBarcodeExistences.getItemIdentificationCount() == 0 
									&& enBarcodeExistences.getItemOnlyCount() == 0
									&& enBarcodeExistences.getLotOnlyCount() == 0
									&& enBarcodeExistences.getLPCount() == 0
									&& enBarcodeExistences.getOrderCount() == 0
									&& enBarcodeExistences.getPoCount() == 0
									&& enBarcodeExistences.getUOMBarcodeCount() == 0) {
								showPopUp(AcMove.this, null,
										getLanguage(GlobalParams.JOBPART_VALIDATE_ITEM_OR_LP_VALUE,
												GlobalParams.JOBPART_VALIDATE_ITEM_OR_LP_VALUE));
							} else if (enBarcodeExistences.getOrderCount() != 0 || enBarcodeExistences.getBinOnlyCount() != 0
									|| enBarcodeExistences.getLotOnlyCount() != 0 || enBarcodeExistences.getPoCount() != 0) {
								showPopUp(AcMove.this, null,
										getLanguage(GlobalParams.JOBPART_VALIDATE_ITEM_OR_LP_VALUE,
												GlobalParams.JOBPART_VALIDATE_ITEM_OR_LP_VALUE));	
							} else if (enBarcodeExistences.getItemOnlyCount() != 0								
									|| enBarcodeExistences.getUOMBarcodeCount() != 0) {							
								intent = new Intent(AcMove.this, AcMoveDetails.class);
								intent.putExtra(GlobalParams.BARCODE_MOVE, edtItem.getEditableText().toString().trim());
								intent.putExtra(GlobalParams.CHECK_LP_OR_NOT_LP, GlobalParams.FALSE);
								startActivity(intent);
								scanFlag = GlobalParams.FLAG_ACTIVE;
							} else if (enBarcodeExistences.getItemIdentificationCount() != 0) {
								intent = new Intent(AcMove.this, AcMoveDetails.class);
								intent.putExtra(GlobalParams.BARCODE_MOVE, edtItem.getEditableText().toString().trim());
								intent.putExtra(GlobalParams.CHECK_LP_OR_NOT_LP, GlobalParams.FALSE);
								startActivity(intent);
								scanFlag = GlobalParams.FLAG_ACTIVE;
							} else if (enBarcodeExistences.getLPCount() != 0) {
								intent = new Intent(AcMove.this, AcMoveDetails.class);
								intent.putExtra(GlobalParams.BARCODE_MOVE, edtItem.getEditableText().toString().trim());
								intent.putExtra(GlobalParams.CHECK_LP_OR_NOT_LP, GlobalParams.TRUE);
								startActivity(intent);
								scanFlag = GlobalParams.FLAG_ACTIVE;
							} else {
								showPopUp(AcMove.this, null,
										getLanguage(GlobalParams.AMBIGUOUS_BARCODE_SCAN,
												GlobalParams.AMBIGUOUS_BARCODE_SCAN));
							}
							
						} else {
							showPopUp(AcMove.this, null, GlobalParams.INVALID_SCAN);
						}
					} else {
						showPopUp(AcMove.this, null, GlobalParams.INVALID_SCAN);
					}
				} else {
					scanFlag = GlobalParams.FLAG_ACTIVE;
				}
			}			
		}
	}
	
	/**
	 * handler for receiving the notifications coming from 
	 * SingleEntryApplication.
	 * Update the UI accordingly when we receive a notification
	 */
	private final BroadcastReceiver _newItemsReceiver = new BroadcastReceiver() {   
	    
		@Override  
	    public void onReceive(Context context, Intent intent) {
		
	        // a Scanner has connected
	        if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_SCANNER_ARRIVAL))
	        {
	        	imgScan.setVisibility(View.GONE);
	        	scanFlag = GlobalParams.FLAG_ACTIVE;
	        }
	        
	        // a Scanner has disconnected
	        else if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_SCANNER_REMOVAL))
	        {
	        	imgScan.setVisibility(View.VISIBLE);
	        }
	        
	        // decoded Data received from a scanner
	        else if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_DECODED_DATA))
	        {
				char[] data = intent.getCharArrayExtra(SingleEntryApplication.EXTRA_DECODEDDATA);
				if (scanFlag.equals(GlobalParams.FLAG_ACTIVE)) {
					edtItem.setText(new String(data));
					BarcodeAsyncTask barcodeAsyncTask = new BarcodeAsyncTask();
		            barcodeAsyncTask.execute();
				}
	        }
	    }
	};
	
	/**
	 * 
	 * @param mContext
	 * @param newClass
	 * @param strMessages
	 */
	public void showPopUp(final Context mContext, final Class<?> newClass, final String strMessages) {
		String message;
		
		if (strMessages.equals(GlobalParams.BLANK)) {
			message = GlobalParams.WRONG_USER;
		} else {
			message = strMessages;
		}
		
		final Dialog dialog = new Dialog(mContext, R.style.Dialog_NoTitle);
		dialog.setContentView(R.layout.dialogwarning);
		// set the custom dialog components - text, image and button		
		TextView text2 = (TextView) dialog.findViewById(R.id.tvScantitle2);		
		text2.setText(message);
		
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
}
