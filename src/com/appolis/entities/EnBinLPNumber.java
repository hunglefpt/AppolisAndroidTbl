/**
 * Name: EnBinLPNumber.java
 * Date: Jan 27, 2015 6:48:07 PM
 * Copyright (c) 2014 FPT Software, Inc. All rights reserved.
 */
package com.appolis.entities;

import java.io.Serializable;

/**
 * @author hoangnh11
 */
public class EnBinLPNumber implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6876293446771539851L;
	private int _warehouseID;
	private int _binID;
	private int _itemID;
	private int _quantity;
	private String _quantityDisplay;
	private int _quantityPicked;
	private String _quantityPickedDisplay;
	private int _binItemID;
	private String _itemNumber;
	private int _inventoryStatusID;
	private String _itemDescription;
	private String _lotNumber;
	private String _binNumber;
	private int _uomTypeID;
	private String _uomDescription;
	/**
	 * @return the _warehouseID
	 */
	public int get_warehouseID() {
		return _warehouseID;
	}
	/**
	 * @param _warehouseID the _warehouseID to set
	 */
	public void set_warehouseID(int _warehouseID) {
		this._warehouseID = _warehouseID;
	}
	/**
	 * @return the _binID
	 */
	public int get_binID() {
		return _binID;
	}
	/**
	 * @param _binID the _binID to set
	 */
	public void set_binID(int _binID) {
		this._binID = _binID;
	}
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
	 * @return the _quantity
	 */
	public int get_quantity() {
		return _quantity;
	}
	/**
	 * @param _quantity the _quantity to set
	 */
	public void set_quantity(int _quantity) {
		this._quantity = _quantity;
	}
	/**
	 * @return the _quantityDisplay
	 */
	public String get_quantityDisplay() {
		return _quantityDisplay;
	}
	/**
	 * @param _quantityDisplay the _quantityDisplay to set
	 */
	public void set_quantityDisplay(String _quantityDisplay) {
		this._quantityDisplay = _quantityDisplay;
	}
	/**
	 * @return the _quantityPicked
	 */
	public int get_quantityPicked() {
		return _quantityPicked;
	}
	/**
	 * @param _quantityPicked the _quantityPicked to set
	 */
	public void set_quantityPicked(int _quantityPicked) {
		this._quantityPicked = _quantityPicked;
	}
	/**
	 * @return the _quantityPickedDisplay
	 */
	public String get_quantityPickedDisplay() {
		return _quantityPickedDisplay;
	}
	/**
	 * @param _quantityPickedDisplay the _quantityPickedDisplay to set
	 */
	public void set_quantityPickedDisplay(String _quantityPickedDisplay) {
		this._quantityPickedDisplay = _quantityPickedDisplay;
	}
	/**
	 * @return the _binItemID
	 */
	public int get_binItemID() {
		return _binItemID;
	}
	/**
	 * @param _binItemID the _binItemID to set
	 */
	public void set_binItemID(int _binItemID) {
		this._binItemID = _binItemID;
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
	 * @return the _inventoryStatusID
	 */
	public int get_inventoryStatusID() {
		return _inventoryStatusID;
	}
	/**
	 * @param _inventoryStatusID the _inventoryStatusID to set
	 */
	public void set_inventoryStatusID(int _inventoryStatusID) {
		this._inventoryStatusID = _inventoryStatusID;
	}
	/**
	 * @return the _itemDescription
	 */
	public String get_itemDescription() {
		return _itemDescription;
	}
	/**
	 * @param _itemDescription the _itemDescription to set
	 */
	public void set_itemDescription(String _itemDescription) {
		this._itemDescription = _itemDescription;
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
	 * @return the _uomTypeID
	 */
	public int get_uomTypeID() {
		return _uomTypeID;
	}
	/**
	 * @param _uomTypeID the _uomTypeID to set
	 */
	public void set_uomTypeID(int _uomTypeID) {
		this._uomTypeID = _uomTypeID;
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
}
