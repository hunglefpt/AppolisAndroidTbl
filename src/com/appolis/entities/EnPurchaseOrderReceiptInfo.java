/**
 * Name: $RCSfile: EnPurchaseOrderReceiptInfo.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.entities;

import java.io.Serializable;

public class EnPurchaseOrderReceiptInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5308373242936629332L;
	
	private int _purchaseOrderItemShipID;
    private int _purchaseOrderItemID;
    private boolean _receivedInd;
    private String _dateReceived;
    private double _quantityReceived;
    private String _quantityReceivedDisplay;
    private double _quantityReceivedConverted;
    private double _quantityMissing;
    private String _quantityMissingDisplay;
    private double _quantityMissingConverted;
    private double _quantityDamaged;
    private String _quantityDamagedDisplay;
    private double _quantityDamagedConverted;
    private int _userID;
    private String _userName;
    private String _lotNumber;
    private String _itemIdentification;
    private String _beginTime;
    private String _endTime;
    private int _receivingStatusID;
    private String _statusDesc;
    private String _dateExported;
    private boolean _saveToInventory;
    private String _status;
    private String _binNumber;
    private int _itemID;
    private String _damageBinNumber;
    private String _expirationDate;
    private double _catchWeight;
    private String _gs1Barcode;
	
    public EnPurchaseOrderReceiptInfo() {
		super();
	}

	public EnPurchaseOrderReceiptInfo(int _purchaseOrderItemShipID,
			int _purchaseOrderItemID, boolean _receivedInd,
			String _dateReceived, double _quantityReceived,
			String _quantityReceivedDisplay, double _quantityReceivedConverted,
			double _quantityMissing, String _quantityMissingDisplay,
			double _quantityMissingConverted, double _quantityDamaged,
			String _quantityDamagedDisplay, double _quantityDamagedConverted,
			int _userID, String _userName, String _lotNumber,
			String _itemIdentification, String _beginTime, String _endTime,
			int _receivingStatusID, String _statusDesc, String _dateExported,
			boolean _saveToInventory, String _status, String _binNumber,
			int _itemID, String _damageBinNumber, String _expirationDate,
			double _catchWeight, String _gs1Barcode) {
		super();
		this._purchaseOrderItemShipID = _purchaseOrderItemShipID;
		this._purchaseOrderItemID = _purchaseOrderItemID;
		this._receivedInd = _receivedInd;
		this._dateReceived = _dateReceived;
		this._quantityReceived = _quantityReceived;
		this._quantityReceivedDisplay = _quantityReceivedDisplay;
		this._quantityReceivedConverted = _quantityReceivedConverted;
		this._quantityMissing = _quantityMissing;
		this._quantityMissingDisplay = _quantityMissingDisplay;
		this._quantityMissingConverted = _quantityMissingConverted;
		this._quantityDamaged = _quantityDamaged;
		this._quantityDamagedDisplay = _quantityDamagedDisplay;
		this._quantityDamagedConverted = _quantityDamagedConverted;
		this._userID = _userID;
		this._userName = _userName;
		this._lotNumber = _lotNumber;
		this._itemIdentification = _itemIdentification;
		this._beginTime = _beginTime;
		this._endTime = _endTime;
		this._receivingStatusID = _receivingStatusID;
		this._statusDesc = _statusDesc;
		this._dateExported = _dateExported;
		this._saveToInventory = _saveToInventory;
		this._status = _status;
		this._binNumber = _binNumber;
		this._itemID = _itemID;
		this._damageBinNumber = _damageBinNumber;
		this._expirationDate = _expirationDate;
		this._catchWeight = _catchWeight;
		this._gs1Barcode = _gs1Barcode;
	}

	public int get_purchaseOrderItemShipID() {
		return _purchaseOrderItemShipID;
	}

	public void set_purchaseOrderItemShipID(int _purchaseOrderItemShipID) {
		this._purchaseOrderItemShipID = _purchaseOrderItemShipID;
	}

	public int get_purchaseOrderItemID() {
		return _purchaseOrderItemID;
	}

	public void set_purchaseOrderItemID(int _purchaseOrderItemID) {
		this._purchaseOrderItemID = _purchaseOrderItemID;
	}

	public boolean is_receivedInd() {
		return _receivedInd;
	}

	public void set_receivedInd(boolean _receivedInd) {
		this._receivedInd = _receivedInd;
	}

	public String get_dateReceived() {
		return _dateReceived;
	}

	public void set_dateReceived(String _dateReceived) {
		this._dateReceived = _dateReceived;
	}

	public double get_quantityReceived() {
		return _quantityReceived;
	}

	public void set_quantityReceived(double _quantityReceived) {
		this._quantityReceived = _quantityReceived;
	}

	public String get_quantityReceivedDisplay() {
		return _quantityReceivedDisplay;
	}

	public void set_quantityReceivedDisplay(String _quantityReceivedDisplay) {
		this._quantityReceivedDisplay = _quantityReceivedDisplay;
	}

	public double get_quantityReceivedConverted() {
		return _quantityReceivedConverted;
	}

	public void set_quantityReceivedConverted(double _quantityReceivedConverted) {
		this._quantityReceivedConverted = _quantityReceivedConverted;
	}

	public double get_quantityMissing() {
		return _quantityMissing;
	}

	public void set_quantityMissing(double _quantityMissing) {
		this._quantityMissing = _quantityMissing;
	}

	public String get_quantityMissingDisplay() {
		return _quantityMissingDisplay;
	}

	public void set_quantityMissingDisplay(String _quantityMissingDisplay) {
		this._quantityMissingDisplay = _quantityMissingDisplay;
	}

	public double get_quantityMissingConverted() {
		return _quantityMissingConverted;
	}

	public void set_quantityMissingConverted(double _quantityMissingConverted) {
		this._quantityMissingConverted = _quantityMissingConverted;
	}

	public double get_quantityDamaged() {
		return _quantityDamaged;
	}

	public void set_quantityDamaged(double _quantityDamaged) {
		this._quantityDamaged = _quantityDamaged;
	}

	public String get_quantityDamagedDisplay() {
		return _quantityDamagedDisplay;
	}

	public void set_quantityDamagedDisplay(String _quantityDamagedDisplay) {
		this._quantityDamagedDisplay = _quantityDamagedDisplay;
	}

	public double get_quantityDamagedConverted() {
		return _quantityDamagedConverted;
	}

	public void set_quantityDamagedConverted(double _quantityDamagedConverted) {
		this._quantityDamagedConverted = _quantityDamagedConverted;
	}

	public int get_userID() {
		return _userID;
	}

	public void set_userID(int _userID) {
		this._userID = _userID;
	}

	public String get_userName() {
		return _userName;
	}

	public void set_userName(String _userName) {
		this._userName = _userName;
	}

	public String get_lotNumber() {
		return _lotNumber;
	}

	public void set_lotNumber(String _lotNumber) {
		this._lotNumber = _lotNumber;
	}

	public String get_itemIdentification() {
		return _itemIdentification;
	}

	public void set_itemIdentification(String _itemIdentification) {
		this._itemIdentification = _itemIdentification;
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

	public int get_receivingStatusID() {
		return _receivingStatusID;
	}

	public void set_receivingStatusID(int _receivingStatusID) {
		this._receivingStatusID = _receivingStatusID;
	}

	public String get_statusDesc() {
		return _statusDesc;
	}

	public void set_statusDesc(String _statusDesc) {
		this._statusDesc = _statusDesc;
	}

	public String get_dateExported() {
		return _dateExported;
	}

	public void set_dateExported(String _dateExported) {
		this._dateExported = _dateExported;
	}

	public boolean is_saveToInventory() {
		return _saveToInventory;
	}

	public void set_saveToInventory(boolean _saveToInventory) {
		this._saveToInventory = _saveToInventory;
	}

	public String get_status() {
		return _status;
	}

	public void set_status(String _status) {
		this._status = _status;
	}

	public String get_binNumber() {
		return _binNumber;
	}

	public void set_binNumber(String _binNumber) {
		this._binNumber = _binNumber;
	}

	public int get_itemID() {
		return _itemID;
	}

	public void set_itemID(int _itemID) {
		this._itemID = _itemID;
	}

	public String get_damageBinNumber() {
		return _damageBinNumber;
	}

	public void set_damageBinNumber(String _damageBinNumber) {
		this._damageBinNumber = _damageBinNumber;
	}

	public String get_expirationDate() {
		return _expirationDate;
	}

	public void set_expirationDate(String _expirationDate) {
		this._expirationDate = _expirationDate;
	}

	public double get_catchWeight() {
		return _catchWeight;
	}

	public void set_catchWeight(double _catchWeight) {
		this._catchWeight = _catchWeight;
	}

	public String get_gs1Barcode() {
		return _gs1Barcode;
	}

	public void set_gs1Barcode(String _gs1Barcode) {
		this._gs1Barcode = _gs1Barcode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
