/**
 * Name: $RCSfile: SingleEntryActivity.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.scan;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appolis.androidtablet.R;

/**
 * 
 * @author hoangnh11
 * handler for receiving the notifications coming from SingleEntryApplication
 */
public class SingleEntryActivity extends Activity {
	private EditText _decodedData;
	private TextView _status;
	private Context _context;
	
	/**
	 * Update the UI accordingly when we receive a notification
	 */
	private final BroadcastReceiver _newItemsReceiver = new BroadcastReceiver() {   
	    
		@Override  
	    public void onReceive(Context context, Intent intent) {
			
			// ScanAPI is initialized
	        if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_SCANPI_INITIALIZED))
	        {
	        	_status.setText("Waiting for scanner...");
	            Button btn=(Button)findViewById(R.id.buttonEzPair);
	            if(btn!=null){
	            	btn.setVisibility(View.VISIBLE);
	            }        	
	        }
	        
	        // a Scanner has connected
	        else if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_SCANNER_ARRIVAL))
	        {
	        	String text=intent.getStringExtra(SingleEntryApplication.EXTRA_DEVICENAME);
	        	_status.setText(text);
	            Button btn=(Button)findViewById(R.id.buttonEzPair);
	            if(btn!=null){
	            	btn.setVisibility(View.INVISIBLE);
	            }
	        }
	        
	        // a Scanner has disconected
	        else if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_SCANNER_REMOVAL))
	        {
	        	_status.setText("Waiting for scanner...");
	            Button btn=(Button)findViewById(R.id.buttonEzPair);
	            if(btn!=null){
	            	btn.setVisibility(View.VISIBLE);
	            }
	        }
	        
	        // decoded Data received from a scanner
	        else if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_DECODED_DATA))
	        {
				char[] data=intent.getCharArrayExtra(SingleEntryApplication.EXTRA_DECODEDDATA);
				_decodedData.setText(new String(data));
	        }
	        
	        // an error has occurred
	        else if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_ERROR_MESSAGE))
	        {
	        	String text=intent.getStringExtra(SingleEntryApplication.EXTRA_ERROR_MESSAGE);
//	        	Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	        }
	    }  
	};
	private OnClickListener _onPairToScanner = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// retrieve the scanner name and start the EZ Pair Process
			Intent intent=new Intent(_context, EzPairActivity.class);
			startActivity(intent);
		}
	}; 
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        _context=getApplicationContext();
        // get the different UI fields
        _decodedData = (EditText)findViewById(R.id.editText1);
        _status = (TextView)findViewById(R.id.textViewStatus);
        
        Button btn=(Button)findViewById(R.id.buttonEzPair);
        if(btn!=null){
        	btn.setOnClickListener(_onPairToScanner);
        	btn.setVisibility(View.INVISIBLE);
        }
        
        // register to receive notifications from SingleEntryApplication
        // these notifications originate from ScanAPI 
        IntentFilter filter;   
        filter = new IntentFilter(SingleEntryApplication.NOTIFY_SCANPI_INITIALIZED);   
        registerReceiver(this._newItemsReceiver, filter); 

        filter = new IntentFilter(SingleEntryApplication.NOTIFY_SCANNER_ARRIVAL);   
        registerReceiver(this._newItemsReceiver, filter); 

        filter = new IntentFilter(SingleEntryApplication.NOTIFY_SCANNER_REMOVAL);   
        registerReceiver(this._newItemsReceiver, filter); 

        filter = new IntentFilter(SingleEntryApplication.NOTIFY_DECODED_DATA);   
        registerReceiver(this._newItemsReceiver, filter);

        filter = new IntentFilter(SingleEntryApplication.NOTIFY_ERROR_MESSAGE);   
        registerReceiver(this._newItemsReceiver, filter);
        
        filter = new IntentFilter(SingleEntryApplication.NOTIFY_EZ_PAIR_COMPLETED);
		registerReceiver(this._newItemsReceiver, filter);
		
        filter = new IntentFilter(SingleEntryApplication.NOTIFY_CLOSE_ACTIVITY);   
        registerReceiver(this._newItemsReceiver, filter); 
        
        
    	// increasing the Application View count from 0 to 1 will
    	// cause the application to open and initialize ScanAPI
    	SingleEntryApplication.getApplicationInstance().increaseViewCount();
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
        // unregister the notifications
		unregisterReceiver(_newItemsReceiver);
        
        // indicate this view has been destroyed
        // if the reference count becomes 0 ScanAPI can
        // be closed if this is not a screen rotation scenario
        SingleEntryApplication.getApplicationInstance().decreaseViewCount();
	}
		
}