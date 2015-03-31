/**
 * Name: $RCSfile: SingleEntryApplication.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.scan;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.SocketMobile.ScanAPI.ISktScanDecodedData;
import com.SocketMobile.ScanAPI.ISktScanObject;
import com.SocketMobile.ScanAPI.ISktScanProperty;
import com.SocketMobile.ScanAPI.SktScanApiOwnership;
import com.SocketMobile.ScanAPI.SktScanApiOwnership.Notification;
import com.SocketMobile.ScanAPI.SktScanErrors;
import com.appolis.androidtablet.R;
import com.appolis.scan.ScanApiHelper.ScanApiHelperNotification;
import com.appolis.utilities.Logger;

/**
 * This application class is the holder of ScanAPI. 
 * ScanAPI cannot be attached to an Activity because it will
 * get close and reopen each time the user rotate the screen.
 * 
 * The ScanApiHelper provides a way to post a command to a scanner
 * asynchronously, and to receive the response into a callback. That
 * way, the UI thread is not blocked while communicating to the scanner.
 * 
 * The ScanApiOwnership makes this application friendly with the other
 * application using ScanAPI. (ScannerSettings or EZPair).
 * 
 * @author EricG
 *
 */
@ReportsCrashes(
		reportType = org.acra.sender.HttpSender.Type.JSON,
		formUri = "https://collector.tracepot.com/9034dc1f",
		formUriBasicAuthLogin = "bu9.ss5@gmail.com",
		formUriBasicAuthPassword = "bu9@12345",
		mode = ReportingInteractionMode.SILENT,
		customReportContent = { 
				ReportField.REPORT_ID,
				ReportField.APP_VERSION_CODE,
				ReportField.APP_VERSION_NAME,
				ReportField.PACKAGE_NAME,
				ReportField.STACK_TRACE,
				ReportField.USER_APP_START_DATE,
				ReportField.USER_CRASH_DATE,
				ReportField.FILE_PATH,
				ReportField.PHONE_MODEL,
				ReportField.BRAND,
				ReportField.PRODUCT,
				ReportField.ANDROID_VERSION,
				ReportField.LOGCAT
		})//doximitydroid@gmail.com
public class SingleEntryApplication extends Application {

	/**
	 * simple synchronized event
	 * @author ericg
	 *
	 */
	class Event{
		private boolean _set;
		public Event(boolean set){
			_set=set;
		}
		public synchronized void  set()
		{
			_set=true;
			notify();
		}
		public synchronized void reset(){
			_set=false;
		}
		public synchronized boolean waitFor(long timeoutInMilliseconds)
		{
			long t1,t2=0;
			for(;_set==false;){
				t1=System.currentTimeMillis();
				try {
					wait(timeoutInMilliseconds);
				} catch (InterruptedException e) {
					break;
				}
				t2=System.currentTimeMillis();
				if(_set==false)
				{
					if(t2>=(t1+timeoutInMilliseconds))
						break;
					else
						timeoutInMilliseconds=(t1+timeoutInMilliseconds)-t2;
				}
				else
					break;
			}
			return _set;
		}
	}

	public static final String DEFAULT_SCANAPI_CONFIGURATION="Server:ScanAPI-1";
	
	public static final String START_EZ_PAIR=SingleEntryApplication.class.getName()+".StartEzPair";
	public static final String STOP_EZ_PAIR=SingleEntryApplication.class.getName()+".StopEzPair";
	public static final String NOTIFY_EZ_PAIR_COMPLETED=SingleEntryApplication.class.getName()+".NotifyEzPairCompleted";
	public static final String EXTRA_EZ_PAIR_DEVICE=SingleEntryApplication.class.getName()+".EzPairDevice";
	public static final String EXTRA_EZ_PAIR_HOST_ADDRESS=SingleEntryApplication.class.getName()+".EzPairHostAddress";
	
	public static final String NOTIFY_SCANPI_INITIALIZED = SingleEntryApplication.class.getName() + ".NotifyScanApiInitialized";   
	public static final String NOTIFY_SCANNER_ARRIVAL = SingleEntryApplication.class.getName() + ".NotifyScannerArrival";   
	public static final String NOTIFY_SCANNER_REMOVAL = SingleEntryApplication.class.getName() + ".NotifyScannerRemoval";
	public static final String NOTIFY_DECODED_DATA = SingleEntryApplication.class.getName() + ".NotifyDecodedData";
	public static final String NOTIFY_ERROR_MESSAGE=SingleEntryApplication.class.getName()+".NotifyErrorMessage";
	public static final String NOTIFY_CLOSE_ACTIVITY=SingleEntryApplication.class.getName()+".NotifyCloseActivity";
	
	public static final String EXTRA_ERROR_MESSAGE=SingleEntryApplication.class.getName()+".ErrorMessage";
	public static final String EXTRA_DEVICENAME=SingleEntryApplication.class.getName()+".DeviceName";
	public static final String EXTRA_SYMBOLOGY_NAME=SingleEntryApplication.class.getName()+".SymbologyName";
	public static final String EXTRA_DECODEDDATA=SingleEntryApplication.class.getName()+".DecodedData";

	protected static final int defaultConnectedTimeout = 0;

	private final int CLOSE_SCAN_API=1;

	private static SingleEntryApplication _singleton;
	private ScanApiHelper _scanApiHelper;
	private SktScanApiOwnership _scanApiOwnership;
	private Event _consumerTerminatedEvent;// event to know when the ScanAPI terminate event has been received
	private int _viewCount;// View counter (each activity increase or decrease this count when created or destroyed respectively)
	private boolean _forceCloseUI;// flag to force to close the UI
	// when a Activity rotates, it gets destroyed and recreated. By keeping the last
	// state that has been broadcasted, when the Activity is recreated, the application
	// will broadcast this last state to update the Activity
	private Intent _lastBroadcastedState;// last state that has been broadcast to the Activity
	
	// keep the original ScanAPI Configuration
	// this is used for the EZ Pair process, where
	// the original ScanAPI Serial Ports configuration is saved
	// before changing it with the Scanner friendly name
	// So that ScanAPI will connect to this scanner, to instruct
	// the scanner to connect back to this host.
	// Once this is done, the original ScanAPI configuration
	// has to be restored.
	private String _originalScanAPIConfiguration="Server:ScanAPI-1";
	private String _ezPairDeviceName;
	private String _ezPairHostAddress;
	private boolean _ezPairInProgress=false;
	
	private BroadcastReceiver _broadcastReceiver=new BroadcastReceiver() {
		
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().contains(START_EZ_PAIR)){
				if(_ezPairInProgress==false){
					_ezPairDeviceName=intent.getStringExtra(EXTRA_EZ_PAIR_DEVICE);
					_ezPairHostAddress=intent.getStringExtra(EXTRA_EZ_PAIR_HOST_ADDRESS);
					_ezPairInProgress=true;
					
					// backup the original ScanAPI configuration
					_scanApiHelper.postGetScanAPIConfiguration(
							ISktScanProperty.values.configuration.kSktScanConfigSerialComPort,
							_onGetScanApiConfiguration);
					
					// change the ScanAPI configuration to make it connect
					// to a specific scanner
					_scanApiHelper.postSetScanAPIConfiguration(
							ISktScanProperty.values.configuration.kSktScanConfigSerialComPort,
							"client:"+_ezPairDeviceName,
							_onSetScanApiConfiguration);
				}
			}
			else if (intent.getAction().contains(STOP_EZ_PAIR)){
				if(_ezPairInProgress==true){
					_ezPairInProgress=false;
					// restore the original ScanAPI configuration
					_scanApiHelper.postSetScanAPIConfiguration(
							ISktScanProperty.values.configuration.kSktScanConfigSerialComPort,
							_originalScanAPIConfiguration,
							_onSetScanApiConfiguration);
				}
			}
		}
	};
	
	private Handler _messageHandler=new Handler(new Handler.Callback() {
		
		public boolean handleMessage(Message msg) {
			switch(msg.what){
			case CLOSE_SCAN_API:
				Debug.MSG(Debug.kLevelTrace,"Receive a CLOSE SCAN API Message and View Count="+_viewCount+"ScanAPI open:"+_scanApiHelper.isScanApiOpen());
				// if we receive this message and the view count is 0
				// and ScanAPI is open then we should close it
				if((_viewCount==0)&&(_scanApiHelper.isScanApiOpen()==true)){
					unregisterScanApiOwnership();
					closeScanApi();
				}
				break;
			}
			return false;
		}
	});


	@Override
	public void onCreate() {
		super.onCreate();
		
		// The following line triggers the initialization of ACRA
        try {
            // The following line triggers the initialization of ACRA
            ACRA.init(this);
        } catch (Exception e) {
        	Logger.error("ACRA: initial exception");
            e.printStackTrace();
        }  
        
		_singleton=this;
		_viewCount=0;// there is no view created for this application yet
		_forceCloseUI=false;
		_lastBroadcastedState=null;
		
		_consumerTerminatedEvent=new Event(true);
		
		Debug.MSG(Debug.kLevelTrace,"Application onCreate");
		
		// create a ScanAPI Helper 
		_scanApiHelper=new ScanApiHelper();
		_scanApiHelper.setNotification(_scanApiHelperNotification);
		
		// create a ScanAPI ownership
		_scanApiOwnership=new SktScanApiOwnership(_scanApiOwnershipNotification,
				getString(R.string.app_name));

		IntentFilter filter;
		filter=new IntentFilter(START_EZ_PAIR);
		registerReceiver(_broadcastReceiver, filter);
		
		filter=new IntentFilter(STOP_EZ_PAIR);
		registerReceiver(_broadcastReceiver, filter);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();

		unregisterReceiver(_broadcastReceiver);
	}


	public static SingleEntryApplication getApplicationInstance(){
		return _singleton;
	}

	/**
	 * increase the view count.
	 * <br>this is called typically on each Activity.onCreate
	 * <br>If the view count was 0 then it asks this application object
	 * to register for ScanAPI ownership notification and to open ScanAPI  
	 */
	public void increaseViewCount(){
		if(_scanApiHelper.isScanApiOpen()==false){
			if(_viewCount==0){
				registerScanApiOwnership();
				openScanApi();
			}
			else{
				Debug.MSG(Debug.kLevelWarning,"There is more View created without ScanAPI opened??");				
			}
		}
		else{
			if(_lastBroadcastedState!=null){
				sendBroadcast(_lastBroadcastedState);
			}
		}
		++_viewCount;
		Debug.MSG(Debug.kLevelTrace,"Increase View count, New view count: "+_viewCount);
	}
	
	/**
	 * decrease the view count.
	 * <br> this is called typically on each Activity.onDestroy
	 * <br> If the view Count comes to 0 then it will try to close
	 * ScanAPI and unregister for ScanAPI ownership notification unless
	 * this decreaseViewCount is happening because of a screen rotation
	 */
	public void decreaseViewCount(){
		// if the view count is going to be 0
		// and ScanAPI is open and there hasn't
		// been a screen rotation then close ScanApi
		if((_viewCount==1)&&(_scanApiHelper.isScanApiOpen()==true)){
			// it's probably OK to close ScanAPI now, but
			// just send a CLOSE_SCAN_API request delayed by .5s
			// to give the View a chance to be recreated 
			// if it was just a screen rotation
			Debug.MSG(Debug.kLevelTrace,"Post a differed request to close ScanAPI");
			_messageHandler.sendEmptyMessageDelayed(CLOSE_SCAN_API,500);
		}
		--_viewCount;
		if(_viewCount<0){
			_viewCount=0;
			Debug.MSG(Debug.kLevelWarning,"try to decrease more view count that possible");
		}
		Debug.MSG(Debug.kLevelTrace,"Decrease View count, New view count: "+_viewCount);
	}
	

	/**
	 * Notification helping to manage ScanAPI ownership.
	 * Only one application at a time can have access to ScanAPI.
	 * When another application is claiming ScanAPI ownership, this
	 * callback is called with release set to true asking this application
	 * to release scanAPI. When the other application is done with ScanAPI 
	 * it calls releaseOwnership, causing this callback to be called again
	 * but this time with release set to false. At that moment this application
	 * can reclaim the ScanAPI ownership.
	 */
	private Notification _scanApiOwnershipNotification=new Notification() {
		
		public void onScanApiOwnershipChange(Context context, boolean release) {
			if(release==true){
				closeScanApi();
			}
			else{
				openScanApi();
			}
		}
	};


	/**
	 * register for ScanAPI ownership
	 */
	private void registerScanApiOwnership(){
		_scanApiOwnership.register(this);
	}
	
	/**
	 * unregister from ScanAPI ownership
	 */
	private void unregisterScanApiOwnership(){
		_scanApiOwnership.unregister();
	}

	/**
	 * open ScanAPI by first claiming its ownership
	 * then checking if the previous instance of ScanAPI has
	 * been correctly close. ScanAPI initialization is done in a
	 * separate thread, because it performs some internal testing
	 * that requires some time to complete and we want the UI to be
	 * responsive and present on the screen during that time.
	 */
	private void openScanApi(){
		_scanApiOwnership.claimOwnership();
		// check this event to be sure the previous 
		// ScanAPI consumer has been shutdown
		Debug.MSG(Debug.kLevelTrace,"Wait for the previous terminate event to be set");
		
		if(_consumerTerminatedEvent.waitFor(3000)==true){
			Debug.MSG(Debug.kLevelTrace,"the previous terminate event has been set");
			_consumerTerminatedEvent.reset();
			_scanApiHelper.removeCommands(null);// remove all the commands
			_scanApiHelper.open();
		}
		else{
			Debug.MSG(Debug.kLevelTrace,"the previous terminate event has NOT been set");
			Intent intent=new Intent(NOTIFY_ERROR_MESSAGE);
			intent.putExtra(EXTRA_ERROR_MESSAGE,"Unable to start ScanAPI because the previous close hasn't been completed. Restart this application.");
			sendBroadcast(intent);
		}
	}
	
	/**
	 * close ScanAPI by first releasing its ownership and 
	 * by sending an abort. This allows ScanAPI to shutdown 
	 * gracefully by asking to close any Scanner Object if 
	 * they were opened. When ScanAPI is done a kSktScanTerminate event
	 * is received in the ScanObject consumer timer thread.
	 */
	private void closeScanApi(){
		_scanApiOwnership.releaseOwnership();
		_scanApiHelper.close();
	}
	
	private ScanApiHelperNotification _scanApiHelperNotification=new ScanApiHelperNotification() {
		/**
		 * receive a notification indicating ScanAPI has terminated,
		 * then send an intent to finish the activity if it is still
		 * running
		 */
		public void onScanApiTerminated() {
			_consumerTerminatedEvent.set();
			if(_forceCloseUI){
				Intent intent=new Intent(NOTIFY_CLOSE_ACTIVITY);
				sendBroadcast(intent);
			}
		}
		/**
		 * ScanAPI is now initialized, if there is an error
		 * then ask the activity to display it
		 */
		public void onScanApiInitializeComplete(long result) {
			// if ScanAPI couldn't be initialized
			// then display an error
			if(!SktScanErrors.SKTSUCCESS(result)){
				_consumerTerminatedEvent.set();
	    		_scanApiOwnership.releaseOwnership();
				String text="ScanAPI failed to initialize with error: "+result;
	    		Intent intent=new Intent(NOTIFY_ERROR_MESSAGE);
	    		intent.putExtra(EXTRA_ERROR_MESSAGE,text);
	        	sendBroadcast(intent);
	        	_lastBroadcastedState=intent;
			}
			else{
	    		Intent intent=new Intent(NOTIFY_SCANPI_INITIALIZED);
	        	sendBroadcast(intent);
	        	_lastBroadcastedState=intent;
	        	
	        	// check if the ScanAPI configuration is correct.
	        	// if not then put the default configuration.
	        	_scanApiHelper.postGetScanAPIConfiguration(
	        			ISktScanProperty.values.configuration.kSktScanConfigSerialComPort,
	        			_onGetScanApiConfiguration);
			}
		}
		/**
		 * ask the activity to display any asynchronous error
		 * received from ScanAPI
		 */
		public void onError(long result) {
			Debug.MSG(Debug.kLevelError,"receive an error:"+result);
			String text="ScanAPI is reporting an error: "+result;
			if(result==SktScanErrors.ESKT_UNABLEINITIALIZE)
				text="Unable to initialize the scanner. Please power cycle the scanner.";
    		Intent intent=new Intent(NOTIFY_ERROR_MESSAGE);
    		intent.putExtra(EXTRA_ERROR_MESSAGE,text);
        	sendBroadcast(intent);
        	_lastBroadcastedState=intent;
        	if(_ezPairInProgress==true){
        		// make sure to restore ScanAPI configuration
        		_scanApiHelper.postSetScanAPIConfiguration(
        				ISktScanProperty.values.configuration.kSktScanConfigSerialComPort, 
        				_originalScanAPIConfiguration,_onSetScanApiConfiguration);
        	}
        	_ezPairInProgress=false;// no longer in EZ Pair mode
		}
		
		/**
		 * a device has disconnected. Update the UI accordingly
		 */
		public void onDeviceRemoval(DeviceInfo deviceRemoved) {
			if(_ezPairInProgress==false){
	        	Intent intent=new Intent(NOTIFY_SCANNER_REMOVAL);
	        	intent.putExtra(EXTRA_DEVICENAME,deviceRemoved.getName());
	        	sendBroadcast(intent);
	        	_lastBroadcastedState=intent;
			}
			// in ez pair mode, restore the original ScanAPI configuration
			else{
				_scanApiHelper.postSetScanAPIConfiguration(
						ISktScanProperty.values.configuration.kSktScanConfigSerialComPort,
						_originalScanAPIConfiguration,
						_onSetScanApiConfiguration);
				
				Intent intent=new Intent(NOTIFY_EZ_PAIR_COMPLETED);
				sendBroadcast(intent);
				_ezPairInProgress=false;// no longer in EZ Pair mode
			}
		}
		
		/**
		 * a device is connecting, update the UI accordingly
		 */
		public void onDeviceArrival(long result, DeviceInfo newDevice) {
			Intent intent=null;
			if(SktScanErrors.SKTSUCCESS(result)){
				if(_ezPairInProgress==false){
					intent=new Intent(NOTIFY_SCANNER_ARRIVAL);
					intent.putExtra(EXTRA_DEVICENAME,newDevice.getName());
					
					// retrieve the device Timers information to check if it needs to be changed
					_scanApiHelper.postGetTimersDevice(newDevice, _onGetTimersDevice);		
				}
				else{
					_scanApiHelper.postSetProfileConfigDevice(newDevice,_ezPairHostAddress,_onSetProfileConfigDevice);
					_scanApiHelper.postSetDisconnectDevice(newDevice,_onSetDisconnectDevice);
				}
			}
			else
			{
				String text="Error "+result+
					" during device arrival notification";
				intent=new Intent(NOTIFY_ERROR_MESSAGE);
				intent.putExtra(EXTRA_ERROR_MESSAGE,text);
			}
			if(intent!=null)
				sendBroadcast(intent);
        	_lastBroadcastedState=intent;
		}
		/**
		 * ScanAPI is delivering some decoded data
		 * as the activity to display them
		 */
		public void onDecodedData(DeviceInfo deviceInfo,
				ISktScanDecodedData decodedData) {
			Intent intent=new Intent(NOTIFY_DECODED_DATA);
			intent.putExtra(EXTRA_SYMBOLOGY_NAME,decodedData.getSymbologyName());
			intent.putExtra(EXTRA_DECODEDDATA,decodedData.getData());
			sendBroadcast(intent);
		}
		/**
		 * an error occurs during the retrieval of ScanObject
		 * from ScanAPI, this is critical error and only a restart
		 * can fix this.
		 */
		public void onErrorRetrievingScanObject(long result) {
			Intent intent=new Intent(NOTIFY_ERROR_MESSAGE);
			String text="Error unable to retrieve ScanAPI message: ";
			text+="("+result+")";
			text+="Please close this application and restart it";
			intent.putExtra(EXTRA_ERROR_MESSAGE,text);
			sendBroadcast(intent);
        	_lastBroadcastedState=intent;
		}
	};

	protected ICommandContextCallback _onGetScanApiConfiguration=new ICommandContextCallback() {
		
		@Override
		public void run(ISktScanObject scanObj) {
			long result=scanObj.getMessage().getResult();
			if(SktScanErrors.SKTSUCCESS(result)){
				_originalScanAPIConfiguration=scanObj.getProperty().getString().getValue();
				if(_originalScanAPIConfiguration.toLowerCase().contains("server")==false){
					_originalScanAPIConfiguration=DEFAULT_SCANAPI_CONFIGURATION;
					if(_ezPairInProgress==false)
						_scanApiHelper.postSetScanAPIConfiguration(
								ISktScanProperty.values.configuration.kSktScanConfigSerialComPort,
								_originalScanAPIConfiguration,
								_onSetScanApiConfiguration);
				}
			}
			else{
				String text="Error "+result+
						" getting ScanAPI configuration";
					Intent intent=new Intent(NOTIFY_ERROR_MESSAGE);
					intent.putExtra(EXTRA_ERROR_MESSAGE,text);
			}
		}
	};
	
	protected ICommandContextCallback _onSetScanApiConfiguration=new ICommandContextCallback() {
		
		@Override
		public void run(ISktScanObject scanObj) {
			long result=scanObj.getMessage().getResult();
			if(!SktScanErrors.SKTSUCCESS(result)){
				String text="Error "+result+
						" setting ScanAPI configuration";
					Intent intent=new Intent(NOTIFY_ERROR_MESSAGE);
					intent.putExtra(EXTRA_ERROR_MESSAGE,text);
					sendBroadcast(intent);
			}
		}
	};
	
	protected ICommandContextCallback _onSetProfileConfigDevice=new ICommandContextCallback() {
		
		@Override
		public void run(ISktScanObject scanObj) {
			long result=scanObj.getMessage().getResult();
			if(!SktScanErrors.SKTSUCCESS(result)){
				String text="Error "+result+
						" setting Device profile Configuration";
					Intent intent=new Intent(NOTIFY_ERROR_MESSAGE);
					intent.putExtra(EXTRA_ERROR_MESSAGE,text);
					sendBroadcast(intent);
			}
		}
	};
	
	protected ICommandContextCallback _onSetDisconnectDevice=new ICommandContextCallback() {
		
		@Override
		public void run(ISktScanObject scanObj) {
			long result=scanObj.getMessage().getResult();
			if(!SktScanErrors.SKTSUCCESS(result)){
				String text="Error "+result+
						" disconnecting the device";
					Intent intent=new Intent(NOTIFY_ERROR_MESSAGE);
					intent.putExtra(EXTRA_ERROR_MESSAGE,text);
			}
		}
	};

	protected ICommandContextCallback _onSetTimersDevice=new ICommandContextCallback() {
		
		@Override
		public void run(ISktScanObject scanObj) {
			long result=scanObj.getMessage().getResult();
			if(!SktScanErrors.SKTSUCCESS(result)){
				String text="Error "+result+
						" setting the device timers";
					Intent intent=new Intent(NOTIFY_ERROR_MESSAGE);
					intent.putExtra(EXTRA_ERROR_MESSAGE,text);
			}
		}
	};
	
	protected ICommandContextCallback _onGetTimersDevice=new ICommandContextCallback() {
		
		@Override
		public void run(ISktScanObject scanObj) {
			long result=scanObj.getMessage().getResult();
			if(!SktScanErrors.SKTSUCCESS(result)){
				String text="Error "+result+
						" getting the device timers information";
					Intent intent=new Intent(NOTIFY_ERROR_MESSAGE);
					intent.putExtra(EXTRA_ERROR_MESSAGE,text);
			}
			else{
				char[] deviceTimers= scanObj.getProperty().getArray().getValue();
				int value=0;
				if(scanObj.getProperty().getArray().getLength()>=8){
					value=deviceTimers[6];
					value<<=8;
					value+=deviceTimers[7];
					
					if(value>defaultConnectedTimeout){
						CommandContext context=(CommandContext)scanObj.getProperty().getContext();
						_scanApiHelper.postSetTimersDevice(context.getDeviceInfo(),
								ISktScanProperty.values.timers.kSktScanTimerPowerOffConnected, 
								0,0,defaultConnectedTimeout,_onSetTimersDevice);
					}
				}
				else{
					String text="the device timers information has an incorrect format";
						Intent intent=new Intent(NOTIFY_ERROR_MESSAGE);
						intent.putExtra(EXTRA_ERROR_MESSAGE,text);
				}
			}
		}
	};

}
