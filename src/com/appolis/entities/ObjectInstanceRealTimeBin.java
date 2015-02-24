/**
 * Name: $RCSfile: ObjectInstanceRealTimeBin.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.entities;

import java.io.Serializable;

public class ObjectInstanceRealTimeBin implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String _binDesc;
	private String _binID;
	private String _binNumber;
	public String get_binDesc() {
		return _binDesc;
	}
	public void set_binDesc(String _binDesc) {
		this._binDesc = _binDesc;
	}
	public String get_binID() {
		return _binID;
	}
	public void set_binID(String _binID) {
		this._binID = _binID;
	}
	public String get_binNumber() {
		return _binNumber;
	}
	public void set_binNumber(String _binNumber) {
		this._binNumber = _binNumber;
	}
	public String get_binStatus() {
		return _binStatus;
	}
	public void set_binStatus(String _binStatus) {
		this._binStatus = _binStatus;
	}
	private String _binStatus;
}
