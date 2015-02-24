/**
 * Name: $RCSfile: ObjectCountCycleCurrent.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.entities;

import java.io.Serializable;

public class ObjectCountCycleCurrent implements Serializable {

	private static final long serialVersionUID = 4498292286011860785L;
	
	private int _cycleCountItemInstanceID;
	private int _itemID;
	private String _itemNumber;
	private String _itemDesc;
	private boolean _lotTrackingInd;
	private String _lotNumber;
	private double _count;
	private String _countDisplay;
	private String _beginTime;
	private String _endTime;
	private int _userID;
	private int _cycleCountInstanceID;
	private int _itemStatusID;
	private String _itemStatus;
	private boolean _lpInd;
	private boolean _singleScanEnabled;
	private int _lpBinID; 
	private String _uomDescription;
	private int _k__BackingField;
	
	public int get_cycleCountItemInstanceID() {
		return _cycleCountItemInstanceID;
	}
	public void set_cycleCountItemInstanceID(int _cycleCountItemInstanceID) {
		this._cycleCountItemInstanceID = _cycleCountItemInstanceID;
	}
	public int get_itemID() {
		return _itemID;
	}
	public void set_itemID(int _itemID) {
		this._itemID = _itemID;
	}
	public String get_itemNumber() {
		return _itemNumber;
	}
	public void set_itemNumber(String _itemNumber) {
		this._itemNumber = _itemNumber;
	}
	public String get_itemDesc() {
		return _itemDesc;
	}
	public void set_itemDesc(String _itemDesc) {
		this._itemDesc = _itemDesc;
	}
	public String get_lotNumber() {
		return _lotNumber;
	}
	public void set_lotNumber(String _lotNumber) {
		this._lotNumber = _lotNumber;
	}
	public int get_lpBinID() {
		return _lpBinID;
	}
	public void set_lpBinID(int _lpBinID) {
		this._lpBinID = _lpBinID;
	}
	public boolean is_lotTrackingInd() {
		return _lotTrackingInd;
	}
	public void set_lotTrackingInd(boolean _lotTrackingInd) {
		this._lotTrackingInd = _lotTrackingInd;
	}
	public double get_count() {
		return _count;
	}
	public void set_count(double _count) {
		this._count = _count;
	}
	public String get_countDisplay() {
		return _countDisplay;
	}
	public void set_countDisplay(String _countDisplay) {
		this._countDisplay = _countDisplay;
	}
	public String get_beginTime() {
		return _beginTime;
	}
	public void set_beginTime(String _beginTime) {
		this._beginTime = _beginTime;
	}
	public String get_endTime() {
		return _endTime;
	}
	public void set_endTime(String _endTime) {
		this._endTime = _endTime;
	}
	public int get_userID() {
		return _userID;
	}
	public void set_userID(int _userID) {
		this._userID = _userID;
	}
	public int get_cycleCountInstanceID() {
		return _cycleCountInstanceID;
	}
	public void set_cycleCountInstanceID(int _cycleCountInstanceID) {
		this._cycleCountInstanceID = _cycleCountInstanceID;
	}
	public int get_itemStatusID() {
		return _itemStatusID;
	}
	public void set_itemStatusID(int _itemStatusID) {
		this._itemStatusID = _itemStatusID;
	}
	public String get_itemStatus() {
		return _itemStatus;
	}
	public void set_itemStatus(String _itemStatus) {
		this._itemStatus = _itemStatus;
	}
	public boolean is_lpInd() {
		return _lpInd;
	}
	public void set_lpInd(boolean _lpInd) {
		this._lpInd = _lpInd;
	}
	public boolean is_singleScanEnabled() {
		return _singleScanEnabled;
	}
	public void set_singleScanEnabled(boolean _singleScanEnabled) {
		this._singleScanEnabled = _singleScanEnabled;
	}
	public String get_uomDescription() {
		return _uomDescription;
	}
	public void set_uomDescription(String _uomDescription) {
		this._uomDescription = _uomDescription;
	}
	public int get_k__BackingField() {
		return _k__BackingField;
	}
	public void set_k__BackingField(int _k__BackingField) {
		this._k__BackingField = _k__BackingField;
	}
}
