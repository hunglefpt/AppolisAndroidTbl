/**
 * Name: $RCSfile: ObjectUser.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.entities;

import java.io.Serializable;

public class ObjectUser implements Serializable{
	/**
	 * variable declaration
	 */
	private static final long serialVersionUID = 1L;
	private String _userID;
	private String _username;
	private String _userBin;
	private String _userBinNumber;
	private String _PIN;
	private String _cultureID;
	private String _userRoles[];
	private String _defaultBinId;
	private String _defaultBinNumber;
	private boolean _isForceOrderScan;
	private boolean _isForcePOScan;
	private boolean _isForceCycleCountScan;
	private boolean _ssrsPrintingEnabled;
	private String _iaNotesRequirement;
	private boolean _showPutAwayBins;
	private boolean _expirationDateAsLotNumber;
	private String _gs1KeyWord;
	private boolean _useGs1Barcode;
	private boolean _expirationDateAllowEditing;
	
	/**
	 * @return the _ssrsPrintingEnabled
	 */
	public boolean is_ssrsPrintingEnabled() {
		return _ssrsPrintingEnabled;
	}
	/**
	 * @param _ssrsPrintingEnabled the _ssrsPrintingEnabled to set
	 */
	public void set_ssrsPrintingEnabled(boolean _ssrsPrintingEnabled) {
		this._ssrsPrintingEnabled = _ssrsPrintingEnabled;
	}
	/**
	 * @return the _iaNotesRequirement
	 */
	public String get_iaNotesRequirement() {
		return _iaNotesRequirement;
	}
	/**
	 * @param _iaNotesRequirement the _iaNotesRequirement to set
	 */
	public void set_iaNotesRequirement(String _iaNotesRequirement) {
		this._iaNotesRequirement = _iaNotesRequirement;
	}
	/**
	 * @return the _showPutAwayBins
	 */
	public boolean is_showPutAwayBins() {
		return _showPutAwayBins;
	}
	/**
	 * @param _showPutAwayBins the _showPutAwayBins to set
	 */
	public void set_showPutAwayBins(boolean _showPutAwayBins) {
		this._showPutAwayBins = _showPutAwayBins;
	}
	/**
	 * @return the _expirationDateAsLotNumber
	 */
	public boolean is_expirationDateAsLotNumber() {
		return _expirationDateAsLotNumber;
	}
	/**
	 * @param _expirationDateAsLotNumber the _expirationDateAsLotNumber to set
	 */
	public void set_expirationDateAsLotNumber(boolean _expirationDateAsLotNumber) {
		this._expirationDateAsLotNumber = _expirationDateAsLotNumber;
	}
	/**
	 * @return the _gs1KeyWord
	 */
	public String get_gs1KeyWord() {
		return _gs1KeyWord;
	}
	/**
	 * @param _gs1KeyWord the _gs1KeyWord to set
	 */
	public void set_gs1KeyWord(String _gs1KeyWord) {
		this._gs1KeyWord = _gs1KeyWord;
	}
	/**
	 * @return the _useGs1Barcode
	 */
	public boolean is_useGs1Barcode() {
		return _useGs1Barcode;
	}
	/**
	 * @param _useGs1Barcode the _useGs1Barcode to set
	 */
	public void set_useGs1Barcode(boolean _useGs1Barcode) {
		this._useGs1Barcode = _useGs1Barcode;
	}
	/**
	 * @return the _expirationDateAllowEditing
	 */
	public boolean is_expirationDateAllowEditing() {
		return _expirationDateAllowEditing;
	}
	/**
	 * @param _expirationDateAllowEditing the _expirationDateAllowEditing to set
	 */
	public void set_expirationDateAllowEditing(boolean _expirationDateAllowEditing) {
		this._expirationDateAllowEditing = _expirationDateAllowEditing;
	}
	public String get_userID() {
		return _userID;
	}
	public void set_userID(String _userID) {
		this._userID = _userID;
	}
	public String get_username() {
		return _username;
	}
	public void set_username(String _username) {
		this._username = _username;
	}
	public String get_userBin() {
		return _userBin;
	}
	public void set_userBin(String _userBin) {
		this._userBin = _userBin;
	}
	public String get_userBinNumber() {
		return _userBinNumber;
	}
	public void set_userBinNumber(String _userBinNumber) {
		this._userBinNumber = _userBinNumber;
	}
	public String get_PIN() {
		return _PIN;
	}
	public void set_PIN(String _PIN) {
		this._PIN = _PIN;
	}
	public String get_cultureID() {
		return _cultureID;
	}
	public void set_cultureID(String _cultureID) {
		this._cultureID = _cultureID;
	}
	public String[] get_userRoles() {
		return _userRoles;
	}
	public void set_userRoles(String[] _userRoles) {
		this._userRoles = _userRoles;
	}
	public String get_defaultBinId() {
		return _defaultBinId;
	}
	public void set_defaultBinId(String _defaultBinId) {
		this._defaultBinId = _defaultBinId;
	}
	public String get_defaultBinNumber() {
		return _defaultBinNumber;
	}
	public void set_defaultBinNumber(String _defaultBinNumber) {
		this._defaultBinNumber = _defaultBinNumber;
	}
	public boolean get_isForceOrderScan() {
		return _isForceOrderScan;
	}
	public void set_isForceOrderScan(boolean _isForceOrderScan) {
		this._isForceOrderScan = _isForceOrderScan;
	}
	public boolean get_isForcePOScan() {
		return _isForcePOScan;
	}
	public void set_isForcePOScan(boolean _isForcePOScan) {
		this._isForcePOScan = _isForcePOScan;
	}
	public boolean get_isForceCycleCountScan() {
		return _isForceCycleCountScan;
	}
	public void set_isForceCycleCountScan(boolean _isForceCycleCountScan) {
		this._isForceCycleCountScan = _isForceCycleCountScan;
	}
}
