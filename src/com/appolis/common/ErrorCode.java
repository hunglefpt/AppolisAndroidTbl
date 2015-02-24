/**
 * Name: $RCSfile: ErrorCode.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.common;

/**
 * 
 * @author HoangNH11
 * Interface for process status
 */
public interface ErrorCode {
	
	public static final int STATUS_FAIL = -1;
	public static final int STATUS_SUCCESS = 0;
	public static final int STATUS_NETWORK_NOT_CONNECT = 1;
	public static final int STATUS_EXCEPTION = 2;
	public static final int STATUS_SCAN_ERROR = 3;
	public static final int STATUS_SCAN_UNSUPPORTED_BARCODE = 4;
	public static final int STATUS_JSON_EXCEPTION = 5;
}
