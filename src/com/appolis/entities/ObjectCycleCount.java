/**
 * Name: $RCSfile: ObjectCycleCount.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.entities;

import java.io.Serializable;
import java.util.ArrayList;

public class ObjectCycleCount implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int _cycleCountID;
	private int _cycleCountInstanceID;
	private int _cycleCountStatusID;
	private String _cycleCountStatusDesc;
	private int _cycleCountTypeID;
	private boolean _physicalInventoryCycleCount;
	private boolean _requireScan;
	private int _userID;
	
	ArrayList<ObjectInstanceRealTimeBin> _countInstanceRealTimeBin;

	public int get_cycleCountID() {
		return _cycleCountID;
	}

	public void set_cycleCountID(int _cycleCountID) {
		this._cycleCountID = _cycleCountID;
	}

	public int get_cycleCountInstanceID() {
		return _cycleCountInstanceID;
	}

	public void set_cycleCountInstanceID(int _cycleCountInstanceID) {
		this._cycleCountInstanceID = _cycleCountInstanceID;
	}

	public int get_cycleCountStatusID() {
		return _cycleCountStatusID;
	}

	public void set_cycleCountStatusID(int _cycleCountStatusID) {
		this._cycleCountStatusID = _cycleCountStatusID;
	}

	public String get_cycleCountStatusDesc() {
		return _cycleCountStatusDesc;
	}

	public void set_cycleCountStatusDesc(String _cycleCountStatusDesc) {
		this._cycleCountStatusDesc = _cycleCountStatusDesc;
	}

	public int get_cycleCountTypeID() {
		return _cycleCountTypeID;
	}

	public void set_cycleCountTypeID(int _cycleCountTypeID) {
		this._cycleCountTypeID = _cycleCountTypeID;
	}

	public boolean is_physicalInventoryCycleCount() {
		return _physicalInventoryCycleCount;
	}

	public void set_physicalInventoryCycleCount(boolean _physicalInventoryCycleCount) {
		this._physicalInventoryCycleCount = _physicalInventoryCycleCount;
	}

	public boolean is_requireScan() {
		return _requireScan;
	}

	public void set_requireScan(boolean _requireScan) {
		this._requireScan = _requireScan;
	}

	public int get_userID() {
		return _userID;
	}

	public void set_userID(int _userID) {
		this._userID = _userID;
	}

	public ArrayList<ObjectInstanceRealTimeBin> get_countInstanceRealTimeBin() {
		return _countInstanceRealTimeBin;
	}

	public void set_countInstanceRealTimeBin(
			ArrayList<ObjectInstanceRealTimeBin> _countInstanceRealTimeBin) {
		this._countInstanceRealTimeBin = _countInstanceRealTimeBin;
	}
}
