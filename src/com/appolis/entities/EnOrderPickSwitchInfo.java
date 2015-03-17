package com.appolis.entities;

import java.io.Serializable;
import java.util.ArrayList;

public class EnOrderPickSwitchInfo implements Serializable{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -337774687588708183L;
	private int _zoneID;
	private String _zoneDescription;
	private int _orderID;
	private String _orderNumber;
	private int _orderPercentComplete;
	private int _orderContainerID;
	private int _orderContainerPercentComplete;
	private String _customerPONumber;
	private ArrayList<EnZoneInfo> _zones;
	private String _orders;
	private String _customerName;
	private String _orderTypeCode;
	private String _requestedDeliveryDate;
	
	public EnOrderPickSwitchInfo(int _zoneID, String _zoneDescription,
			int _orderID, String _orderNumber, int _orderPercentComplete,
			int _orderContainerID, int _orderContainerPercentComplete,
			String _customerPONumber, ArrayList<EnZoneInfo> _zones,
			String _orders, String _customerName, String _orderTypeCode,
			String _requestedDeliveryDate) {
		super();
		this._zoneID = _zoneID;
		this._zoneDescription = _zoneDescription;
		this._orderID = _orderID;
		this._orderNumber = _orderNumber;
		this._orderPercentComplete = _orderPercentComplete;
		this._orderContainerID = _orderContainerID;
		this._orderContainerPercentComplete = _orderContainerPercentComplete;
		this._customerPONumber = _customerPONumber;
		this._zones = _zones;
		this._orders = _orders;
		this._customerName = _customerName;
		this._orderTypeCode = _orderTypeCode;
		this._requestedDeliveryDate = _requestedDeliveryDate;
	}

	public EnOrderPickSwitchInfo() {
		super();
	}

	public int get_zoneID() {
		return _zoneID;
	}

	public void set_zoneID(int _zoneID) {
		this._zoneID = _zoneID;
	}

	public String get_zoneDescription() {
		return _zoneDescription;
	}

	public void set_zoneDescription(String _zoneDescription) {
		this._zoneDescription = _zoneDescription;
	}

	public int get_orderID() {
		return _orderID;
	}

	public void set_orderID(int _orderID) {
		this._orderID = _orderID;
	}

	public String get_orderNumber() {
		return _orderNumber;
	}

	public void set_orderNumber(String _orderNumber) {
		this._orderNumber = _orderNumber;
	}

	public int get_orderPercentComplete() {
		return _orderPercentComplete;
	}

	public void set_orderPercentComplete(int _orderPercentComplete) {
		this._orderPercentComplete = _orderPercentComplete;
	}

	public int get_orderContainerID() {
		return _orderContainerID;
	}

	public void set_orderContainerID(int _orderContainerID) {
		this._orderContainerID = _orderContainerID;
	}

	public int get_orderContainerPercentComplete() {
		return _orderContainerPercentComplete;
	}

	public void set_orderContainerPercentComplete(int _orderContainerPercentComplete) {
		this._orderContainerPercentComplete = _orderContainerPercentComplete;
	}

	public String get_customerPONumber() {
		return _customerPONumber;
	}

	public void set_customerPONumber(String _customerPONumber) {
		this._customerPONumber = _customerPONumber;
	}

	public ArrayList<EnZoneInfo> get_zones() {
		return _zones;
	}

	public void set_zones(ArrayList<EnZoneInfo> _zones) {
		this._zones = _zones;
	}

	public String get_orders() {
		return _orders;
	}

	public void set_orders(String _orders) {
		this._orders = _orders;
	}

	public String get_customerName() {
		return _customerName;
	}

	public void set_customerName(String _customerName) {
		this._customerName = _customerName;
	}

	public String get_orderTypeCode() {
		return _orderTypeCode;
	}

	public void set_orderTypeCode(String _orderTypeCode) {
		this._orderTypeCode = _orderTypeCode;
	}

	public String get_requestedDeliveryDate() {
		return _requestedDeliveryDate;
	}

	public void set_requestedDeliveryDate(String _requestedDeliveryDate) {
		this._requestedDeliveryDate = _requestedDeliveryDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
