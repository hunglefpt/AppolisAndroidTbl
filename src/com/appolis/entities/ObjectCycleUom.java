/**
 * Name: $RCSfile: ObjectCycleUom.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.entities;

import java.io.Serializable;

/**
 * @author CongLT
 *
 */
public class ObjectCycleUom implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int getUomId() {
		return UomId;
	}
	public void setUomId(int uomId) {
		UomId = uomId;
	}
	public String getUomDescription() {
		return UomDescription;
	}
	public void setUomDescription(String uomDescription) {
		UomDescription = uomDescription;
	}
	public int getSignificantDigits() {
		return SignificantDigits;
	}
	public void setSignificantDigits(int significantDigits) {
		SignificantDigits = significantDigits;
	}
	private int UomId;
	private String UomDescription;
	private int SignificantDigits;

	@Override
	public String toString() {
		return UomDescription;
	}
	
}
