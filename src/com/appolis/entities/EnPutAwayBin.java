/**
 * Name: EnPutAwayBin.java
 * Date: Jan 28, 2015 2:10:20 PM
 * Copyright (c) 2014 FPT Software, Inc. All rights reserved.
 */
package com.appolis.entities;

import java.io.Serializable;

/**
 * @author hoangnh11
 */
public class EnPutAwayBin implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5336998515629728818L;
	private int _binID;
	private String _binNumber;
	private String _binPath;
	private String _binSeq;
	private String _binQtyDisplay;
	private String _binStatus;
	private int _binTypeID;
	private double k__BackingField;
	
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
	 * @return the _binSeq
	 */
	public String get_binSeq() {
		return _binSeq;
	}
	/**
	 * @param _binSeq the _binSeq to set
	 */
	public void set_binSeq(String _binSeq) {
		this._binSeq = _binSeq;
	}
	/**
	 * @return the _binQtyDisplay
	 */
	public String get_binQtyDisplay() {
		return _binQtyDisplay;
	}
	/**
	 * @param _binQtyDisplay the _binQtyDisplay to set
	 */
	public void set_binQtyDisplay(String _binQtyDisplay) {
		this._binQtyDisplay = _binQtyDisplay;
	}
	/**
	 * @return the _binStatus
	 */
	public String get_binStatus() {
		return _binStatus;
	}
	/**
	 * @param _binStatus the _binStatus to set
	 */
	public void set_binStatus(String _binStatus) {
		this._binStatus = _binStatus;
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
	 * @return the k__BackingField
	 */
	public double getK__BackingField() {
		return k__BackingField;
	}
	/**
	 * @param k__BackingField the k__BackingField to set
	 */
	public void setK__BackingField(double k__BackingField) {
		this.k__BackingField = k__BackingField;
	}
}
