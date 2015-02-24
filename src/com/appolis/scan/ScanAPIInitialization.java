/**
 * Name: $RCSfile: ScanAPIInitialization.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.scan;

import com.SocketMobile.ScanAPI.ISktScanApi;

/**
 * @author hoangnh11
 * Initiate Scan API
 */
public class ScanAPIInitialization extends Thread {

	private ICallback _callback=null;
	private ISktScanApi _scanAPI=null;
	public interface ICallback
	{
		void completed(long result);
	}
	
	public ScanAPIInitialization(ISktScanApi scanApi,ICallback callback){
		_scanAPI=scanApi;
		_callback=callback;
	}
	
	public void run() {
		long result=_scanAPI.Open(null);
		_callback.completed(result);
	}
}
