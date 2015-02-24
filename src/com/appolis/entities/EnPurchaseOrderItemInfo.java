/**
 * Name: $RCSfile: EnPurchaseOrderItemInfo.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.entities;

import java.io.Serializable;
import java.util.ArrayList;

public class EnPurchaseOrderItemInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4089535502019486457L;
	private int _purchaseOrderID;
	private int _purchaseOrderItemID;
	private int _lineNumber;
	private int  _itemID;
	private String _itemNumber;
	private String _lotNumber;
	private String _itemDesc;
	private String _itemGenericDescription;
	private String _barcodeNumber;
	private double  _quantityOrdered;
	private String _quantityOrderedDisplay;
	private int _poStatusID;
	private String _statusDesc;
	private String _completedDate;
	private int _purchaseOrderItemShipID;
	private boolean _receivedInd;
	private boolean _lotTrackingInd;
	private String _dateReceived;
	private double _quantityReceived;
	private String _quantityMissingDisplay;
	private String _quantityDamagedDisplay;
	private String _quantityReceivedDisplay;
	private int _userID;
	private String _itemIdentification;
	private String _beginTime;
	private String _endTime;
	private double _quantityMissing;
	private double _quantityDamaged;
	private String _status;
	private boolean _receivingPlacementRestrictionInd;
	private int _uomTypeID;
	private String _uomDesc;
	private double _baseConversionFactor;
	private ArrayList<EnPurchaseOrderReceiptInfo> _receipts;
	private ArrayList<Object> _attributes;
	private boolean _expirationDateIndicator;
	private int _shelfLife;
	private float _catchWeight;
	private int _significantDigits;
	private String _comments;
	private float _startingLotNumber;
	private float _endingLotNumber;
	
	public EnPurchaseOrderItemInfo() {
		super();
	}

	public EnPurchaseOrderItemInfo(int _purchaseOrderID,
			int _purchaseOrderItemID, int _lineNumber, int _itemID,
			String _itemNumber, String _lotNumber, String _itemDesc,
			String _itemGenericDescription, String _barcodeNumber,
			double _quantityOrdered, String _quantityOrderedDisplay,
			int _poStatusID, String _statusDesc, String _completedDate,
			int _purchaseOrderItemShipID, boolean _receivedInd,
			boolean _lotTrackingInd, String _dateReceived,
			double _quantityReceived, String _quantityMissingDisplay,
			String _quantityDamagedDisplay, String _quantityReceivedDisplay,
			int _userID, String _itemIdentification, String _beginTime,
			String _endTime, double _quantityMissing, double _quantityDamaged,
			String _status, boolean _receivingPlacementRestrictionInd,
			int _uomTypeID, String _uomDesc, double _baseConversionFactor,
			ArrayList<EnPurchaseOrderReceiptInfo> _receipts,
			ArrayList<Object> _attributes, boolean _expirationDateIndicator,
			int _shelfLife, float _catchWeight, int _significantDigits,
			String _comments, float _startingLotNumber, float _endingLotNumber) {
		super();
		this._purchaseOrderID = _purchaseOrderID;
		this._purchaseOrderItemID = _purchaseOrderItemID;
		this._lineNumber = _lineNumber;
		this._itemID = _itemID;
		this._itemNumber = _itemNumber;
		this._lotNumber = _lotNumber;
		this._itemDesc = _itemDesc;
		this._itemGenericDescription = _itemGenericDescription;
		this._barcodeNumber = _barcodeNumber;
		this._quantityOrdered = _quantityOrdered;
		this._quantityOrderedDisplay = _quantityOrderedDisplay;
		this._poStatusID = _poStatusID;
		this._statusDesc = _statusDesc;
		this._completedDate = _completedDate;
		this._purchaseOrderItemShipID = _purchaseOrderItemShipID;
		this._receivedInd = _receivedInd;
		this._lotTrackingInd = _lotTrackingInd;
		this._dateReceived = _dateReceived;
		this._quantityReceived = _quantityReceived;
		this._quantityMissingDisplay = _quantityMissingDisplay;
		this._quantityDamagedDisplay = _quantityDamagedDisplay;
		this._quantityReceivedDisplay = _quantityReceivedDisplay;
		this._userID = _userID;
		this._itemIdentification = _itemIdentification;
		this._beginTime = _beginTime;
		this._endTime = _endTime;
		this._quantityMissing = _quantityMissing;
		this._quantityDamaged = _quantityDamaged;
		this._status = _status;
		this._receivingPlacementRestrictionInd = _receivingPlacementRestrictionInd;
		this._uomTypeID = _uomTypeID;
		this._uomDesc = _uomDesc;
		this._baseConversionFactor = _baseConversionFactor;
		this._receipts = _receipts;
		this._attributes = _attributes;
		this._expirationDateIndicator = _expirationDateIndicator;
		this._shelfLife = _shelfLife;
		this._catchWeight = _catchWeight;
		this._significantDigits = _significantDigits;
		this._comments = _comments;
		this._startingLotNumber = _startingLotNumber;
		this._endingLotNumber = _endingLotNumber;
	}

	public int get_purchaseOrderID() {
		return _purchaseOrderID;
	}

	public void set_purchaseOrderID(int _purchaseOrderID) {
		this._purchaseOrderID = _purchaseOrderID;
	}

	public int get_purchaseOrderItemID() {
		return _purchaseOrderItemID;
	}

	public void set_purchaseOrderItemID(int _purchaseOrderItemID) {
		this._purchaseOrderItemID = _purchaseOrderItemID;
	}

	public int get_lineNumber() {
		return _lineNumber;
	}

	public void set_lineNumber(int _lineNumber) {
		this._lineNumber = _lineNumber;
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

	public String get_lotNumber() {
		return _lotNumber;
	}

	public void set_lotNumber(String _lotNumber) {
		this._lotNumber = _lotNumber;
	}

	public String get_itemDesc() {
		return _itemDesc;
	}

	public void set_itemDesc(String _itemDesc) {
		this._itemDesc = _itemDesc;
	}

	public String get_itemGenericDescription() {
		return _itemGenericDescription;
	}

	public void set_itemGenericDescription(String _itemGenericDescription) {
		this._itemGenericDescription = _itemGenericDescription;
	}

	public String get_barcodeNumber() {
		return _barcodeNumber;
	}

	public void set_barcodeNumber(String _barcodeNumber) {
		this._barcodeNumber = _barcodeNumber;
	}

	public double get_quantityOrdered() {
		return _quantityOrdered;
	}

	public void set_quantityOrdered(double _quantityOrdered) {
		this._quantityOrdered = _quantityOrdered;
	}

	public String get_quantityOrderedDisplay() {
		return _quantityOrderedDisplay;
	}

	public void set_quantityOrderedDisplay(String _quantityOrderedDisplay) {
		this._quantityOrderedDisplay = _quantityOrderedDisplay;
	}

	public int get_poStatusID() {
		return _poStatusID;
	}

	public void set_poStatusID(int _poStatusID) {
		this._poStatusID = _poStatusID;
	}

	public String get_statusDesc() {
		return _statusDesc;
	}

	public void set_statusDesc(String _statusDesc) {
		this._statusDesc = _statusDesc;
	}

	public String get_completedDate() {
		return _completedDate;
	}

	public void set_completedDate(String _completedDate) {
		this._completedDate = _completedDate;
	}

	public int get_purchaseOrderItemShipID() {
		return _purchaseOrderItemShipID;
	}

	public void set_purchaseOrderItemShipID(int _purchaseOrderItemShipID) {
		this._purchaseOrderItemShipID = _purchaseOrderItemShipID;
	}

	public boolean is_receivedInd() {
		return _receivedInd;
	}

	public void set_receivedInd(boolean _receivedInd) {
		this._receivedInd = _receivedInd;
	}

	public boolean is_lotTrackingInd() {
		return _lotTrackingInd;
	}

	public void set_lotTrackingInd(boolean _lotTrackingInd) {
		this._lotTrackingInd = _lotTrackingInd;
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

	public String get_quantityMissingDisplay() {
		return _quantityMissingDisplay;
	}

	public void set_quantityMissingDisplay(String _quantityMissingDisplay) {
		this._quantityMissingDisplay = _quantityMissingDisplay;
	}

	public String get_quantityDamagedDisplay() {
		return _quantityDamagedDisplay;
	}

	public void set_quantityDamagedDisplay(String _quantityDamagedDisplay) {
		this._quantityDamagedDisplay = _quantityDamagedDisplay;
	}

	public String get_quantityReceivedDisplay() {
		return _quantityReceivedDisplay;
	}

	public void set_quantityReceivedDisplay(String _quantityReceivedDisplay) {
		this._quantityReceivedDisplay = _quantityReceivedDisplay;
	}

	public int get_userID() {
		return _userID;
	}

	public void set_userID(int _userID) {
		this._userID = _userID;
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

	public double get_quantityMissing() {
		return _quantityMissing;
	}

	public void set_quantityMissing(double _quantityMissing) {
		this._quantityMissing = _quantityMissing;
	}

	public double get_quantityDamaged() {
		return _quantityDamaged;
	}

	public void set_quantityDamaged(double _quantityDamaged) {
		this._quantityDamaged = _quantityDamaged;
	}

	public String get_status() {
		return _status;
	}

	public void set_status(String _status) {
		this._status = _status;
	}

	public boolean is_receivingPlacementRestrictionInd() {
		return _receivingPlacementRestrictionInd;
	}

	public void set_receivingPlacementRestrictionInd(
			boolean _receivingPlacementRestrictionInd) {
		this._receivingPlacementRestrictionInd = _receivingPlacementRestrictionInd;
	}

	public int get_uomTypeID() {
		return _uomTypeID;
	}

	public void set_uomTypeID(int _uomTypeID) {
		this._uomTypeID = _uomTypeID;
	}

	public String get_uomDesc() {
		return _uomDesc;
	}

	public void set_uomDesc(String _uomDesc) {
		this._uomDesc = _uomDesc;
	}

	public double get_baseConversionFactor() {
		return _baseConversionFactor;
	}

	public void set_baseConversionFactor(double _baseConversionFactor) {
		this._baseConversionFactor = _baseConversionFactor;
	}

	public ArrayList<EnPurchaseOrderReceiptInfo> get_receipts() {
		return _receipts;
	}

	public void set_receipts(ArrayList<EnPurchaseOrderReceiptInfo> _receipts) {
		this._receipts = _receipts;
	}

	public ArrayList<Object> get_attributes() {
		return _attributes;
	}

	public void set_attributes(ArrayList<Object> _attributes) {
		this._attributes = _attributes;
	}

	public boolean is_expirationDateIndicator() {
		return _expirationDateIndicator;
	}

	public void set_expirationDateIndicator(boolean _expirationDateIndicator) {
		this._expirationDateIndicator = _expirationDateIndicator;
	}

	public int get_shelfLife() {
		return _shelfLife;
	}

	public void set_shelfLife(int _shelfLife) {
		this._shelfLife = _shelfLife;
	}

	public float get_catchWeight() {
		return _catchWeight;
	}

	public void set_catchWeight(float _catchWeight) {
		this._catchWeight = _catchWeight;
	}

	public int get_significantDigits() {
		return _significantDigits;
	}

	public void set_significantDigits(int _significantDigits) {
		this._significantDigits = _significantDigits;
	}

	public String get_comments() {
		return _comments;
	}

	public void set_comments(String _comments) {
		this._comments = _comments;
	}

	public float get_startingLotNumber() {
		return _startingLotNumber;
	}

	public void set_startingLotNumber(float _startingLotNumber) {
		this._startingLotNumber = _startingLotNumber;
	}

	public float get_endingLotNumber() {
		return _endingLotNumber;
	}

	public void set_endingLotNumber(float _endingLotNumber) {
		this._endingLotNumber = _endingLotNumber;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
