/**
 * Name: $RCSfile: ObjectCycleLp.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.entities;

public class ObjectCycleLp {
	private double _parentBinID;
	private String _barcodeNumber;
	private double _uomTypeID;
	private double _JOBID;
	private boolean _active;
	private String _holdReason;
	private double _itemID;
	private boolean _manufacturedInd;
	private String _binNumber;
	private boolean _JOBAssociated;
	private int _significantDigits;
	private int _binID;
	private double _qtyPerLP;
	private boolean _onHold;
	private boolean _hasInventory;
	private double _topBinTypeID;
	private double _recJobId;
	private boolean _adminRequiredToClear;

	public double get_parentBinID() {
		return _parentBinID;
	}

	public void set_parentBinID(double _parentBinID) {
		this._parentBinID = _parentBinID;
	}

	public String get_barcodeNumber() {
		return _barcodeNumber;
	}

	public void set_barcodeNumber(String _barcodeNumber) {
		this._barcodeNumber = _barcodeNumber;
	}

	public double get_uomTypeID() {
		return _uomTypeID;
	}

	public void set_uomTypeID(double _uomTypeID) {
		this._uomTypeID = _uomTypeID;
	}

	public double get_JOBID() {
		return _JOBID;
	}

	public void set_JOBID(double _JOBID) {
		this._JOBID = _JOBID;
	}

	public boolean is_active() {
		return _active;
	}

	public void set_active(boolean _active) {
		this._active = _active;
	}

	public String get_holdReason() {
		return _holdReason;
	}

	public void set_holdReason(String _holdReason) {
		this._holdReason = _holdReason;
	}

	public double get_itemID() {
		return _itemID;
	}

	public void set_itemID(double _itemID) {
		this._itemID = _itemID;
	}

	public boolean is_manufacturedInd() {
		return _manufacturedInd;
	}

	public void set_manufacturedInd(boolean _manufacturedInd) {
		this._manufacturedInd = _manufacturedInd;
	}

	public String get_binNumber() {
		return _binNumber;
	}

	public void set_binNumber(String _binNumber) {
		this._binNumber = _binNumber;
	}

	public boolean is_JOBAssociated() {
		return _JOBAssociated;
	}

	public void set_JOBAssociated(boolean _JOBAssociated) {
		this._JOBAssociated = _JOBAssociated;
	}

	public int get_significantDigits() {
		return _significantDigits;
	}

	public void set_significantDigits(int _significantDigits) {
		this._significantDigits = _significantDigits;
	}

	public int get_binID() {
		return _binID;
	}

	public void set_binID(int _binID) {
		this._binID = _binID;
	}

	public double get_qtyPerLP() {
		return _qtyPerLP;
	}

	public void set_qtyPerLP(double _qtyPerLP) {
		this._qtyPerLP = _qtyPerLP;
	}

	public boolean is_onHold() {
		return _onHold;
	}

	public void set_onHold(boolean _onHold) {
		this._onHold = _onHold;
	}

	public boolean is_hasInventory() {
		return _hasInventory;
	}

	public void set_hasInventory(boolean _hasInventory) {
		this._hasInventory = _hasInventory;
	}

	public double get_topBinTypeID() {
		return _topBinTypeID;
	}

	public void set_topBinTypeID(double _topBinTypeID) {
		this._topBinTypeID = _topBinTypeID;
	}

	public double get_recJobId() {
		return _recJobId;
	}

	public void set_recJobId(double _recJobId) {
		this._recJobId = _recJobId;
	}

	public boolean is_adminRequiredToClear() {
		return _adminRequiredToClear;
	}

	public void set_adminRequiredToClear(boolean _adminRequiredToClear) {
		this._adminRequiredToClear = _adminRequiredToClear;
	}

}
