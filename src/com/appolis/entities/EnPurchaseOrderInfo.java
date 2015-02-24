/**
 * Name: $RCSfile: EnPurchaseOrderInfo.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.entities;

import java.io.Serializable;
import java.util.ArrayList;

public class EnPurchaseOrderInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8085690874624470663L;
	private int  _purchaseOrderID;
	private int _warehouseID;
	private String _poNumber;
	private int _vendorID;
	private String _vendorName;
	private float _vendorReceivePercentOver;
	private String _orderDate;
	private String _schedDeliveryDate;
	private int _poStatusID;
	private String _statusDesc;
	private String _completedDate;
	private int _userID;
	private String _poTypeCode;
	private int _poTypeID;
	private String _comments;
	private String _receivingBinNumber;
	private boolean _requireItemScan;
	
	private ArrayList<EnPurchaseOrderItemInfo> _items;
	
	public EnPurchaseOrderInfo() {
		super();
	}

	public EnPurchaseOrderInfo(int _purchaseOrderID, int _warehouseID,
			String _poNumber, int _vendorID, String _vendorName,
			float _vendorReceivePercentOver, String _orderDate,
			String _schedDeliveryDate, int _poStatusID, String _statusDesc,
			String _completedDate, int _userID, String _poTypeCode,
			int _poTypeID, ArrayList<EnPurchaseOrderItemInfo> _items) {
		super();
		this._purchaseOrderID = _purchaseOrderID;
		this._warehouseID = _warehouseID;
		this._poNumber = _poNumber;
		this._vendorID = _vendorID;
		this._vendorName = _vendorName;
		this._vendorReceivePercentOver = _vendorReceivePercentOver;
		this._orderDate = _orderDate;
		this._schedDeliveryDate = _schedDeliveryDate;
		this._poStatusID = _poStatusID;
		this._statusDesc = _statusDesc;
		this._completedDate = _completedDate;
		this._userID = _userID;
		this._poTypeCode = _poTypeCode;
		this._poTypeID = _poTypeID;
		this._items = _items;
	}

	public int get_purchaseOrderID() {
		return _purchaseOrderID;
	}

	public void set_purchaseOrderID(int _purchaseOrderID) {
		this._purchaseOrderID = _purchaseOrderID;
	}

	public int get_warehouseID() {
		return _warehouseID;
	}

	public void set_warehouseID(int _warehouseID) {
		this._warehouseID = _warehouseID;
	}

	public String get_poNumber() {
		return _poNumber;
	}

	public void set_poNumber(String _poNumber) {
		this._poNumber = _poNumber;
	}

	public int get_vendorID() {
		return _vendorID;
	}

	public void set_vendorID(int _vendorID) {
		this._vendorID = _vendorID;
	}

	public String get_vendorName() {
		return _vendorName;
	}

	public void set_vendorName(String _vendorName) {
		this._vendorName = _vendorName;
	}

	public float get_vendorReceivePercentOver() {
		return _vendorReceivePercentOver;
	}

	public void set_vendorReceivePercentOver(float _vendorReceivePercentOver) {
		this._vendorReceivePercentOver = _vendorReceivePercentOver;
	}

	public String get_orderDate() {
		return _orderDate;
	}

	public void set_orderDate(String _orderDate) {
		this._orderDate = _orderDate;
	}

	public String get_schedDeliveryDate() {
		return _schedDeliveryDate;
	}

	public void set_schedDeliveryDate(String _schedDeliveryDate) {
		this._schedDeliveryDate = _schedDeliveryDate;
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

	public int get_userID() {
		return _userID;
	}

	public void set_userID(int _userID) {
		this._userID = _userID;
	}

	public String get_poTypeCode() {
		return _poTypeCode;
	}

	public void set_poTypeCode(String _poTypeCode) {
		this._poTypeCode = _poTypeCode;
	}

	public int get_poTypeID() {
		return _poTypeID;
	}

	public void set_poTypeID(int _poTypeID) {
		this._poTypeID = _poTypeID;
	}

	public ArrayList<EnPurchaseOrderItemInfo> get_items() {
		return _items;
	}

	public void set_items(ArrayList<EnPurchaseOrderItemInfo> _items) {
		this._items = _items;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String get_receivingBinNumber() {
		return _receivingBinNumber;
	}

	public void set_receivingBinNumber(String _receivingBinNumber) {
		this._receivingBinNumber = _receivingBinNumber;
	}

	public boolean is_requireItemScan() {
		return _requireItemScan;
	}

	public void set_requireItemScan(boolean _requireItemScan) {
		this._requireItemScan = _requireItemScan;
	}

	public String get_comments() {
		return _comments;
	}

	public void set_comments(String _comments) {
		this._comments = _comments;
	}

}
