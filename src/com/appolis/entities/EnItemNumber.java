/**
 * Name: $RCSfile: EnItemNumber.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.entities;

import java.io.Serializable;

/**
 * @author hoangnh11
 */
public class EnItemNumber implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3468323386162517798L;
	private int _BinID;
	private int _BinNumber;
	private String _LotNumber;
	private boolean _LotTrackingInd;
	private boolean _InventoryTrackingInd;
	private boolean _licensePlateInd;
	private int _OrderContainerDetailID;
	private int _UserID;
	private int _TempContainerID;
	private int _itemID;
	private String _itemNumber;
	private int _inventoryStatusID;
	private String _inventoryStatusDesc;
	private int _toBinID;
	private boolean _fullPalletInd;
	private boolean _unPick;
	private int _orderLicensePlateDetailID;
	private int _barCodeNumber;
	private int _orderContainerID;
	private int _classID;
	private String _originationDate;
	private boolean _receivingPlacementRestrictionInd;
	private int _uomTypeID;
	private String _uomDescription;
	private double _quantityOnHand;
	private int _significantDigits;
	private String _binPath;
	private int _parentBinNumber;
	private String _itemDescription;
	private int _validLotExpiration;
	private boolean _expirationDateIndicator;
	private String _expirationDate;
	/**
	 * @return the _BinID
	 */
	public int get_BinID() {
		return _BinID;
	}
	/**
	 * @param _BinID the _BinID to set
	 */
	public void set_BinID(int _BinID) {
		this._BinID = _BinID;
	}
	/**
	 * @return the _BinNumber
	 */
	public int get_BinNumber() {
		return _BinNumber;
	}
	/**
	 * @param _BinNumber the _BinNumber to set
	 */
	public void set_BinNumber(int _BinNumber) {
		this._BinNumber = _BinNumber;
	}
	/**
	 * @return the _LotNumber
	 */
	public String get_LotNumber() {
		return _LotNumber;
	}
	/**
	 * @param _LotNumber the _LotNumber to set
	 */
	public void set_LotNumber(String _LotNumber) {
		this._LotNumber = _LotNumber;
	}
	/**
	 * @return the _LotTrackingInd
	 */
	public boolean is_LotTrackingInd() {
		return _LotTrackingInd;
	}
	/**
	 * @param _LotTrackingInd the _LotTrackingInd to set
	 */
	public void set_LotTrackingInd(boolean _LotTrackingInd) {
		this._LotTrackingInd = _LotTrackingInd;
	}
	/**
	 * @return the _InventoryTrackingInd
	 */
	public boolean is_InventoryTrackingInd() {
		return _InventoryTrackingInd;
	}
	/**
	 * @param _InventoryTrackingInd the _InventoryTrackingInd to set
	 */
	public void set_InventoryTrackingInd(boolean _InventoryTrackingInd) {
		this._InventoryTrackingInd = _InventoryTrackingInd;
	}
	/**
	 * @return the _licensePlateInd
	 */
	public boolean is_licensePlateInd() {
		return _licensePlateInd;
	}
	/**
	 * @param _licensePlateInd the _licensePlateInd to set
	 */
	public void set_licensePlateInd(boolean _licensePlateInd) {
		this._licensePlateInd = _licensePlateInd;
	}
	/**
	 * @return the _OrderContainerDetailID
	 */
	public int get_OrderContainerDetailID() {
		return _OrderContainerDetailID;
	}
	/**
	 * @param _OrderContainerDetailID the _OrderContainerDetailID to set
	 */
	public void set_OrderContainerDetailID(int _OrderContainerDetailID) {
		this._OrderContainerDetailID = _OrderContainerDetailID;
	}
	/**
	 * @return the _UserID
	 */
	public int get_UserID() {
		return _UserID;
	}
	/**
	 * @param _UserID the _UserID to set
	 */
	public void set_UserID(int _UserID) {
		this._UserID = _UserID;
	}
	/**
	 * @return the _TempContainerID
	 */
	public int get_TempContainerID() {
		return _TempContainerID;
	}
	/**
	 * @param _TempContainerID the _TempContainerID to set
	 */
	public void set_TempContainerID(int _TempContainerID) {
		this._TempContainerID = _TempContainerID;
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
	 * @return the _inventoryStatusDesc
	 */
	public String get_inventoryStatusDesc() {
		return _inventoryStatusDesc;
	}
	/**
	 * @param _inventoryStatusDesc the _inventoryStatusDesc to set
	 */
	public void set_inventoryStatusDesc(String _inventoryStatusDesc) {
		this._inventoryStatusDesc = _inventoryStatusDesc;
	}
	/**
	 * @return the _toBinID
	 */
	public int get_toBinID() {
		return _toBinID;
	}
	/**
	 * @param _toBinID the _toBinID to set
	 */
	public void set_toBinID(int _toBinID) {
		this._toBinID = _toBinID;
	}
	/**
	 * @return the _fullPalletInd
	 */
	public boolean is_fullPalletInd() {
		return _fullPalletInd;
	}
	/**
	 * @param _fullPalletInd the _fullPalletInd to set
	 */
	public void set_fullPalletInd(boolean _fullPalletInd) {
		this._fullPalletInd = _fullPalletInd;
	}
	/**
	 * @return the _unPick
	 */
	public boolean is_unPick() {
		return _unPick;
	}
	/**
	 * @param _unPick the _unPick to set
	 */
	public void set_unPick(boolean _unPick) {
		this._unPick = _unPick;
	}
	/**
	 * @return the _orderLicensePlateDetailID
	 */
	public int get_orderLicensePlateDetailID() {
		return _orderLicensePlateDetailID;
	}
	/**
	 * @param _orderLicensePlateDetailID the _orderLicensePlateDetailID to set
	 */
	public void set_orderLicensePlateDetailID(int _orderLicensePlateDetailID) {
		this._orderLicensePlateDetailID = _orderLicensePlateDetailID;
	}
	/**
	 * @return the _barCodeNumber
	 */
	public int get_barCodeNumber() {
		return _barCodeNumber;
	}
	/**
	 * @param _barCodeNumber the _barCodeNumber to set
	 */
	public void set_barCodeNumber(int _barCodeNumber) {
		this._barCodeNumber = _barCodeNumber;
	}
	/**
	 * @return the _orderContainerID
	 */
	public int get_orderContainerID() {
		return _orderContainerID;
	}
	/**
	 * @param _orderContainerID the _orderContainerID to set
	 */
	public void set_orderContainerID(int _orderContainerID) {
		this._orderContainerID = _orderContainerID;
	}
	/**
	 * @return the _classID
	 */
	public int get_classID() {
		return _classID;
	}
	/**
	 * @param _classID the _classID to set
	 */
	public void set_classID(int _classID) {
		this._classID = _classID;
	}
	/**
	 * @return the _originationDate
	 */
	public String get_originationDate() {
		return _originationDate;
	}
	/**
	 * @param _originationDate the _originationDate to set
	 */
	public void set_originationDate(String _originationDate) {
		this._originationDate = _originationDate;
	}
	/**
	 * @return the _receivingPlacementRestrictionInd
	 */
	public boolean is_receivingPlacementRestrictionInd() {
		return _receivingPlacementRestrictionInd;
	}
	/**
	 * @param _receivingPlacementRestrictionInd the _receivingPlacementRestrictionInd to set
	 */
	public void set_receivingPlacementRestrictionInd(
			boolean _receivingPlacementRestrictionInd) {
		this._receivingPlacementRestrictionInd = _receivingPlacementRestrictionInd;
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
	/**
	 * @return the _quantityOnHand
	 */
	public double get_quantityOnHand() {
		return _quantityOnHand;
	}
	/**
	 * @param _quantityOnHand the _quantityOnHand to set
	 */
	public void set_quantityOnHand(double _quantityOnHand) {
		this._quantityOnHand = _quantityOnHand;
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
	/**
	 * @return the _binPath
	 */
	public String get_binPath() {
		return _binPath;
	}
	/**
	 * @param _binPath the _binPath to set
	 */
	public void set_binPath(String _binPath) {
		this._binPath = _binPath;
	}
	/**
	 * @return the _parentBinNumber
	 */
	public int get_parentBinNumber() {
		return _parentBinNumber;
	}
	/**
	 * @param _parentBinNumber the _parentBinNumber to set
	 */
	public void set_parentBinNumber(int _parentBinNumber) {
		this._parentBinNumber = _parentBinNumber;
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
	 * @return the _validLotExpiration
	 */
	public int get_validLotExpiration() {
		return _validLotExpiration;
	}
	/**
	 * @param _validLotExpiration the _validLotExpiration to set
	 */
	public void set_validLotExpiration(int _validLotExpiration) {
		this._validLotExpiration = _validLotExpiration;
	}
	/**
	 * @return the _expirationDateIndicator
	 */
	public boolean is_expirationDateIndicator() {
		return _expirationDateIndicator;
	}
	/**
	 * @param _expirationDateIndicator the _expirationDateIndicator to set
	 */
	public void set_expirationDateIndicator(boolean _expirationDateIndicator) {
		this._expirationDateIndicator = _expirationDateIndicator;
	}
	/**
	 * @return the _expirationDate
	 */
	public String get_expirationDate() {
		return _expirationDate;
	}
	/**
	 * @param _expirationDate the _expirationDate to set
	 */
	public void set_expirationDate(String _expirationDate) {
		this._expirationDate = _expirationDate;
	}
	
}
