package com.appolis.entities;

import java.io.Serializable;

public class EnZoneInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3961192101662472995L;
	private int zoneID;
	private String _zoneDescription;
	
	public EnZoneInfo() {
		super();
	}
	public EnZoneInfo(int zoneID, String _zoneDescription) {
		super();
		this.zoneID = zoneID;
		this._zoneDescription = _zoneDescription;
	}
	public int getZoneID() {
		return zoneID;
	}
	public void setZoneID(int zoneID) {
		this.zoneID = zoneID;
	}
	public String get_zoneDescription() {
		return _zoneDescription;
	}
	public void set_zoneDescription(String _zoneDescription) {
		this._zoneDescription = _zoneDescription;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
