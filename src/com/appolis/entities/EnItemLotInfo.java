/**
 * Name: $RCSfile: EnItemLotInfo.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.entities;

import java.io.Serializable;

public class EnItemLotInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1876064758072044897L;
	private int _BinID;
	private String _BinNumber;
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
	private String _barCodeNumber;
	private int _orderContainerID;
	private String _classID;
	private String _originationDate;
	private boolean _receivingPlacementRestrictionInd;
	private int _uomTypeID;
	private String _uomDescription;
	private double _quantityOnHand;
	private int _significantDigits;
	private String _binPath;
	private String _parentBinNumber;
	private String _itemDescription;
	private int _validLotExpiration;
	private boolean _expirationDateIndicator;
	private String _expirationDate;
	
	public EnItemLotInfo() {
		super();
	}
	
	public EnItemLotInfo(int _BinID, String _BinNumber, String _LotNumber,
			boolean _LotTrackingInd, boolean _InventoryTrackingInd,
			boolean _licensePlateInd, int _OrderContainerDetailID, int _UserID,
			int _TempContainerID, int _itemID, String _itemNumber,
			int _inventoryStatusID, String _inventoryStatusDesc, int _toBinID,
			boolean _fullPalletInd, boolean _unPick,
			int _orderLicensePlateDetailID, String _barCodeNumber,
			int _orderContainerID, String _classID, String _originationDate,
			boolean _receivingPlacementRestrictionInd, int _uomTypeID,
			String _uomDescription, double _quantityOnHand,
			int _significantDigits, String _binPath, String _parentBinNumber,
			String _itemDescription, int _validLotExpiration,
			boolean _expirationDateIndicator, String _expirationDate) {
		super();
		this._BinID = _BinID;
		this._BinNumber = _BinNumber;
		this._LotNumber = _LotNumber;
		this._LotTrackingInd = _LotTrackingInd;
		this._InventoryTrackingInd = _InventoryTrackingInd;
		this._licensePlateInd = _licensePlateInd;
		this._OrderContainerDetailID = _OrderContainerDetailID;
		this._UserID = _UserID;
		this._TempContainerID = _TempContainerID;
		this._itemID = _itemID;
		this._itemNumber = _itemNumber;
		this._inventoryStatusID = _inventoryStatusID;
		this._inventoryStatusDesc = _inventoryStatusDesc;
		this._toBinID = _toBinID;
		this._fullPalletInd = _fullPalletInd;
		this._unPick = _unPick;
		this._orderLicensePlateDetailID = _orderLicensePlateDetailID;
		this._barCodeNumber = _barCodeNumber;
		this._orderContainerID = _orderContainerID;
		this._classID = _classID;
		this._originationDate = _originationDate;
		this._receivingPlacementRestrictionInd = _receivingPlacementRestrictionInd;
		this._uomTypeID = _uomTypeID;
		this._uomDescription = _uomDescription;
		this._quantityOnHand = _quantityOnHand;
		this._significantDigits = _significantDigits;
		this._binPath = _binPath;
		this._parentBinNumber = _parentBinNumber;
		this._itemDescription = _itemDescription;
		this._validLotExpiration = _validLotExpiration;
		this._expirationDateIndicator = _expirationDateIndicator;
		this._expirationDate = _expirationDate;
	}

	public int get_BinID() {
		return _BinID;
	}

	public void set_BinID(int _BinID) {
		this._BinID = _BinID;
	}

	public String get_BinNumber() {
		return _BinNumber;
	}

	public void set_BinNumber(String _BinNumber) {
		this._BinNumber = _BinNumber;
	}

	public String get_LotNumber() {
		return _LotNumber;
	}

	public void set_LotNumber(String _LotNumber) {
		this._LotNumber = _LotNumber;
	}

	public boolean is_LotTrackingInd() {
		return _LotTrackingInd;
	}

	public void set_LotTrackingInd(boolean _LotTrackingInd) {
		this._LotTrackingInd = _LotTrackingInd;
	}

	public boolean is_InventoryTrackingInd() {
		return _InventoryTrackingInd;
	}

	public void set_InventoryTrackingInd(boolean _InventoryTrackingInd) {
		this._InventoryTrackingInd = _InventoryTrackingInd;
	}

	public boolean is_licensePlateInd() {
		return _licensePlateInd;
	}

	public void set_licensePlateInd(boolean _licensePlateInd) {
		this._licensePlateInd = _licensePlateInd;
	}

	public int get_OrderContainerDetailID() {
		return _OrderContainerDetailID;
	}

	public void set_OrderContainerDetailID(int _OrderContainerDetailID) {
		this._OrderContainerDetailID = _OrderContainerDetailID;
	}

	public int get_UserID() {
		return _UserID;
	}

	public void set_UserID(int _UserID) {
		this._UserID = _UserID;
	}

	public int get_TempContainerID() {
		return _TempContainerID;
	}

	public void set_TempContainerID(int _TempContainerID) {
		this._TempContainerID = _TempContainerID;
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

	public int get_inventoryStatusID() {
		return _inventoryStatusID;
	}

	public void set_inventoryStatusID(int _inventoryStatusID) {
		this._inventoryStatusID = _inventoryStatusID;
	}

	public String get_inventoryStatusDesc() {
		return _inventoryStatusDesc;
	}

	public void set_inventoryStatusDesc(String _inventoryStatusDesc) {
		this._inventoryStatusDesc = _inventoryStatusDesc;
	}

	public int get_toBinID() {
		return _toBinID;
	}

	public void set_toBinID(int _toBinID) {
		this._toBinID = _toBinID;
	}

	public boolean is_fullPalletInd() {
		return _fullPalletInd;
	}

	public void set_fullPalletInd(boolean _fullPalletInd) {
		this._fullPalletInd = _fullPalletInd;
	}

	public boolean is_unPick() {
		return _unPick;
	}

	public void set_unPick(boolean _unPick) {
		this._unPick = _unPick;
	}

	public int get_orderLicensePlateDetailID() {
		return _orderLicensePlateDetailID;
	}

	public void set_orderLicensePlateDetailID(int _orderLicensePlateDetailID) {
		this._orderLicensePlateDetailID = _orderLicensePlateDetailID;
	}

	public String get_barCodeNumber() {
		return _barCodeNumber;
	}

	public void set_barCodeNumber(String _barCodeNumber) {
		this._barCodeNumber = _barCodeNumber;
	}

	public int get_orderContainerID() {
		return _orderContainerID;
	}

	public void set_orderContainerID(int _orderContainerID) {
		this._orderContainerID = _orderContainerID;
	}

	public String get_classID() {
		return _classID;
	}

	public void set_classID(String _classID) {
		this._classID = _classID;
	}

	public String get_originationDate() {
		return _originationDate;
	}

	public void set_originationDate(String _originationDate) {
		this._originationDate = _originationDate;
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

	public String get_uomDescription() {
		return _uomDescription;
	}

	public void set_uomDescription(String _uomDescription) {
		this._uomDescription = _uomDescription;
	}

	public double get_quantityOnHand() {
		return _quantityOnHand;
	}

	public void set_quantityOnHand(double _quantityOnHand) {
		this._quantityOnHand = _quantityOnHand;
	}

	public int get_significantDigits() {
		return _significantDigits;
	}

	public void set_significantDigits(int _significantDigits) {
		this._significantDigits = _significantDigits;
	}

	public String get_binPath() {
		return _binPath;
	}

	public void set_binPath(String _binPath) {
		this._binPath = _binPath;
	}

	public String get_parentBinNumber() {
		return _parentBinNumber;
	}

	public void set_parentBinNumber(String _parentBinNumber) {
		this._parentBinNumber = _parentBinNumber;
	}

	public String get_itemDescription() {
		return _itemDescription;
	}

	public void set_itemDescription(String _itemDescription) {
		this._itemDescription = _itemDescription;
	}

	public int get_validLotExpiration() {
		return _validLotExpiration;
	}

	public void set_validLotExpiration(int _validLotExpiration) {
		this._validLotExpiration = _validLotExpiration;
	}

	public boolean is_expirationDateIndicator() {
		return _expirationDateIndicator;
	}

	public void set_expirationDateIndicator(boolean _expirationDateIndicator) {
		this._expirationDateIndicator = _expirationDateIndicator;
	}

	public String get_expirationDate() {
		return _expirationDate;
	}

	public void set_expirationDate(String _expirationDate) {
		this._expirationDate = _expirationDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
