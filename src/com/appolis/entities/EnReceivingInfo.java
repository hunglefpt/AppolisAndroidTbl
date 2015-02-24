/**
 * Name: $RCSfile: EnReceivingInfo.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.entities;

import java.io.Serializable;

public class EnReceivingInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5022964931884265950L;
	private float _warehouseID;
	private float _purchaseOrderID;
	private String _shipmentNumber;
	private String _poNumber;
	private String _dateShipped;
	private String _vendorName;
	private float _vendorID;
	private String _purchaseOrderTypeCode;
	private String _statusDescription;
	public EnReceivingInfo() {
		super();
	}
	
	public EnReceivingInfo(float _warehouseID, float _purchaseOrderID,
			String _shipmentNumber, String _poNumber, String _dateShipped,
			String _vendorName, float _vendorID, String _purchaseOrderTypeCode,
			String _statusDescription) {
		super();
		this._warehouseID = _warehouseID;
		this._purchaseOrderID = _purchaseOrderID;
		this._shipmentNumber = _shipmentNumber;
		this._poNumber = _poNumber;
		this._dateShipped = _dateShipped;
		this._vendorName = _vendorName;
		this._vendorID = _vendorID;
		this._purchaseOrderTypeCode = _purchaseOrderTypeCode;
		this._statusDescription = _statusDescription;
	}

	public float get_warehouseID() {
		return _warehouseID;
	}

	public void set_warehouseID(float _warehouseID) {
		this._warehouseID = _warehouseID;
	}

	public float get_purchaseOrderID() {
		return _purchaseOrderID;
	}

	public void set_purchaseOrderID(float _purchaseOrderID) {
		this._purchaseOrderID = _purchaseOrderID;
	}

	public String get_shipmentNumber() {
		return _shipmentNumber;
	}

	public void set_shipmentNumber(String _shipmentNumber) {
		this._shipmentNumber = _shipmentNumber;
	}

	public String get_poNumber() {
		return _poNumber;
	}

	public void set_poNumber(String _poNumber) {
		this._poNumber = _poNumber;
	}

	public String get_dateShipped() {
		return _dateShipped;
	}

	public void set_dateShipped(String _dateShipped) {
		this._dateShipped = _dateShipped;
	}

	public String get_vendorName() {
		return _vendorName;
	}

	public void set_vendorName(String _vendorName) {
		this._vendorName = _vendorName;
	}

	public float get_vendorID() {
		return _vendorID;
	}

	public void set_vendorID(float _vendorID) {
		this._vendorID = _vendorID;
	}

	public String get_purchaseOrderTypeCode() {
		return _purchaseOrderTypeCode;
	}

	public void set_purchaseOrderTypeCode(String _purchaseOrderTypeCode) {
		this._purchaseOrderTypeCode = _purchaseOrderTypeCode;
	}

	public String get_statusDescription() {
		return _statusDescription;
	}

	public void set_statusDescription(String _statusDescription) {
		this._statusDescription = _statusDescription;
	}
}
