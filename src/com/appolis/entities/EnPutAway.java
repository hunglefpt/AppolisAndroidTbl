/**
 * Name: EnPutAway.java
 * Date: Jan 26, 2015 2:26:00 PM
 * Copyright (c) 2014 FPT Software, Inc. All rights reserved.
 */
package com.appolis.entities;

import java.io.Serializable;

/**
 * @author hoangnh11
 */
public class EnPutAway implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6906950970231442692L;
	private int _itemID;
	private String _itemNumber;
	private String _itemDesc;
	private String _lotNumber;
	private int _lotTrackingInd;
	private double _qty;
	private String _qtyDisplay;
	private boolean _forcedPutAwayInd;
	private String _binNumber;
	private String _uomDescription;
	private int _significantDigits;
	
	/**
	 * @return the _itemID
	 */
	public int get_itemID() {
		return _itemID;
	}
	/**
	 * @param _itemID the _itemID to set
	 */
	public void set_itemID(int _itemID) {
		this._itemID = _itemID;
	}
	/**
	 * @return the _itemNumber
	 */
	public String get_itemNumber() {
		return _itemNumber;
	}
	/**
	 * @param _itemNumber the _itemNumber to set
	 */
	public void set_itemNumber(String _itemNumber) {
		this._itemNumber = _itemNumber;
	}
	/**
	 * @return the _itemDesc
	 */
	public String get_itemDesc() {
		return _itemDesc;
	}
	/**
	 * @param _itemDesc the _itemDesc to set
	 */
	public void set_itemDesc(String _itemDesc) {
		this._itemDesc = _itemDesc;
	}
	/**
	 * @return the _lotNumber
	 */
	public String get_lotNumber() {
		return _lotNumber;
	}
	/**
	 * @param _lotNumber the _lotNumber to set
	 */
	public void set_lotNumber(String _lotNumber) {
		this._lotNumber = _lotNumber;
	}
	/**
	 * @return the _lotTrackingInd
	 */
	public int get_lotTrackingInd() {
		return _lotTrackingInd;
	}
	/**
	 * @param _lotTrackingInd the _lotTrackingInd to set
	 */
	public void set_lotTrackingInd(int _lotTrackingInd) {
		this._lotTrackingInd = _lotTrackingInd;
	}
	/**
	 * @return the _qty
	 */
	public double get_qty() {
		return _qty;
	}
	/**
	 * @param _qty the _qty to set
	 */
	public void set_qty(double _qty) {
		this._qty = _qty;
	}
	/**
	 * @return the _qtyDisplay
	 */
	public String get_qtyDisplay() {
		return _qtyDisplay;
	}
	/**
	 * @param _qtyDisplay the _qtyDisplay to set
	 */
	public void set_qtyDisplay(String _qtyDisplay) {
		this._qtyDisplay = _qtyDisplay;
	}
	/**
	 * @return the _forcedPutAwayInd
	 */
	public boolean is_forcedPutAwayInd() {
		return _forcedPutAwayInd;
	}
	/**
	 * @param _forcedPutAwayInd the _forcedPutAwayInd to set
	 */
	public void set_forcedPutAwayInd(boolean _forcedPutAwayInd) {
		this._forcedPutAwayInd = _forcedPutAwayInd;
	}
	/**
	 * @return the _binNumber
	 */
	public String get_binNumber() {
		return _binNumber;
	}
	/**
	 * @param _binNumber the _binNumber to set
	 */
	public void set_binNumber(String _binNumber) {
		this._binNumber = _binNumber;
	}
	/**
	 * @return the _uomDescription
	 */
	public String get_uomDescription() {
		return _uomDescription;
	}
	/**
	 * @param _uomDescription the _uomDescription to set
	 */
	public void set_uomDescription(String _uomDescription) {
		this._uomDescription = _uomDescription;
	}
	/**
	 * @return the _significantDigits
	 */
	public int get_significantDigits() {
		return _significantDigits;
	}
	/**
	 * @param _significantDigits the _significantDigits to set
	 */
	public void set_significantDigits(int _significantDigits) {
		this._significantDigits = _significantDigits;
	}	
}
