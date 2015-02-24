/**
 * Name: $RCSfile: EnLPNumber.java,v $
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
public class EnLPNumber implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1902571850551505665L;
	private int _warehouseID;
	private int _hLevel;
	private int _binID;
	private String _binNumber;
	private String _binDescription;
	private String _runNumber;
	private int _binTypeID;
	private boolean _activeInd;
	private int _pickSequence;
	private int _parentBinID;
	private String _parentBinNumber;
	private int _parentBinTypeID;
	private int _firstBinID;
	private String _firstBinNumber;
	private int _firstBinTypeID;
	private int _topBinID;
	private String _topBinNumber;
	private int _topBinTypeID;
	private String _binPath;
	private String _binPathID;
	private int _masterShipperItemCount;
	private int _masterShipperLPCount;
	private boolean isEmpty;
	private boolean _isAtBinCapacity;
	
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
	 * @return the _hLevel
	 */
	public int get_hLevel() {
		return _hLevel;
	}
	/**
	 * @param _hLevel the _hLevel to set
	 */
	public void set_hLevel(int _hLevel) {
		this._hLevel = _hLevel;
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
	 * @return the _binDescription
	 */
	public String get_binDescription() {
		return _binDescription;
	}
	/**
	 * @param _binDescription the _binDescription to set
	 */
	public void set_binDescription(String _binDescription) {
		this._binDescription = _binDescription;
	}
	/**
	 * @return the _runNumber
	 */
	public String get_runNumber() {
		return _runNumber;
	}
	/**
	 * @param _runNumber the _runNumber to set
	 */
	public void set_runNumber(String _runNumber) {
		this._runNumber = _runNumber;
	}
	/**
	 * @return the _binTypeID
	 */
	public int get_binTypeID() {
		return _binTypeID;
	}
	/**
	 * @param _binTypeID the _binTypeID to set
	 */
	public void set_binTypeID(int _binTypeID) {
		this._binTypeID = _binTypeID;
	}
	/**
	 * @return the _activeInd
	 */
	public boolean is_activeInd() {
		return _activeInd;
	}
	/**
	 * @param _activeInd the _activeInd to set
	 */
	public void set_activeInd(boolean _activeInd) {
		this._activeInd = _activeInd;
	}
	/**
	 * @return the _pickSequence
	 */
	public int get_pickSequence() {
		return _pickSequence;
	}
	/**
	 * @param _pickSequence the _pickSequence to set
	 */
	public void set_pickSequence(int _pickSequence) {
		this._pickSequence = _pickSequence;
	}
	/**
	 * @return the _parentBinID
	 */
	public int get_parentBinID() {
		return _parentBinID;
	}
	/**
	 * @param _parentBinID the _parentBinID to set
	 */
	public void set_parentBinID(int _parentBinID) {
		this._parentBinID = _parentBinID;
	}
	/**
	 * @return the _parentBinNumber
	 */
	public String get_parentBinNumber() {
		return _parentBinNumber;
	}
	/**
	 * @param _parentBinNumber the _parentBinNumber to set
	 */
	public void set_parentBinNumber(String _parentBinNumber) {
		this._parentBinNumber = _parentBinNumber;
	}
	/**
	 * @return the _parentBinTypeID
	 */
	public int get_parentBinTypeID() {
		return _parentBinTypeID;
	}
	/**
	 * @param _parentBinTypeID the _parentBinTypeID to set
	 */
	public void set_parentBinTypeID(int _parentBinTypeID) {
		this._parentBinTypeID = _parentBinTypeID;
	}
	/**
	 * @return the _firstBinID
	 */
	public int get_firstBinID() {
		return _firstBinID;
	}
	/**
	 * @param _firstBinID the _firstBinID to set
	 */
	public void set_firstBinID(int _firstBinID) {
		this._firstBinID = _firstBinID;
	}
	/**
	 * @return the _firstBinNumber
	 */
	public String get_firstBinNumber() {
		return _firstBinNumber;
	}
	/**
	 * @param _firstBinNumber the _firstBinNumber to set
	 */
	public void set_firstBinNumber(String _firstBinNumber) {
		this._firstBinNumber = _firstBinNumber;
	}
	/**
	 * @return the _firstBinTypeID
	 */
	public int get_firstBinTypeID() {
		return _firstBinTypeID;
	}
	/**
	 * @param _firstBinTypeID the _firstBinTypeID to set
	 */
	public void set_firstBinTypeID(int _firstBinTypeID) {
		this._firstBinTypeID = _firstBinTypeID;
	}
	/**
	 * @return the _topBinID
	 */
	public int get_topBinID() {
		return _topBinID;
	}
	/**
	 * @param _topBinID the _topBinID to set
	 */
	public void set_topBinID(int _topBinID) {
		this._topBinID = _topBinID;
	}
	/**
	 * @return the _topBinNumber
	 */
	public String get_topBinNumber() {
		return _topBinNumber;
	}
	/**
	 * @param _topBinNumber the _topBinNumber to set
	 */
	public void set_topBinNumber(String _topBinNumber) {
		this._topBinNumber = _topBinNumber;
	}
	/**
	 * @return the _topBinTypeID
	 */
	public int get_topBinTypeID() {
		return _topBinTypeID;
	}
	/**
	 * @param _topBinTypeID the _topBinTypeID to set
	 */
	public void set_topBinTypeID(int _topBinTypeID) {
		this._topBinTypeID = _topBinTypeID;
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
	 * @return the _binPathID
	 */
	public String get_binPathID() {
		return _binPathID;
	}
	/**
	 * @param _binPathID the _binPathID to set
	 */
	public void set_binPathID(String _binPathID) {
		this._binPathID = _binPathID;
	}
	/**
	 * @return the _masterShipperItemCount
	 */
	public int get_masterShipperItemCount() {
		return _masterShipperItemCount;
	}
	/**
	 * @param _masterShipperItemCount the _masterShipperItemCount to set
	 */
	public void set_masterShipperItemCount(int _masterShipperItemCount) {
		this._masterShipperItemCount = _masterShipperItemCount;
	}
	/**
	 * @return the _masterShipperLPCount
	 */
	public int get_masterShipperLPCount() {
		return _masterShipperLPCount;
	}
	/**
	 * @param _masterShipperLPCount the _masterShipperLPCount to set
	 */
	public void set_masterShipperLPCount(int _masterShipperLPCount) {
		this._masterShipperLPCount = _masterShipperLPCount;
	}
	/**
	 * @return the isEmpty
	 */
	public boolean isEmpty() {
		return isEmpty;
	}
	/**
	 * @param isEmpty the isEmpty to set
	 */
	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}
	/**
	 * @return the _isAtBinCapacity
	 */
	public boolean is_isAtBinCapacity() {
		return _isAtBinCapacity;
	}
	/**
	 * @param _isAtBinCapacity the _isAtBinCapacity to set
	 */
	public void set_isAtBinCapacity(boolean _isAtBinCapacity) {
		this._isAtBinCapacity = _isAtBinCapacity;
	}
	
}
