/**
 * Name: $RCSfile: EnUom.java,v $
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
public class EnUom implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6448810846548428086L;
	private int UomId;
	private String UomDescription;
	private int SignificantDigits;
	/**
	 * @return the uomId
	 */
	public int getUomId() {
		return UomId;
	}
	/**
	 * @param uomId the uomId to set
	 */
	public void setUomId(int uomId) {
		UomId = uomId;
	}
	/**
	 * @return the uomDescription
	 */
	public String getUomDescription() {
		return UomDescription;
	}
	/**
	 * @param uomDescription the uomDescription to set
	 */
	public void setUomDescription(String uomDescription) {
		UomDescription = uomDescription;
	}
	/**
	 * @return the significantDigits
	 */
	public int getSignificantDigits() {
		return SignificantDigits;
	}
	/**
	 * @param significantDigits the significantDigits to set
	 */
	public void setSignificantDigits(int significantDigits) {
		SignificantDigits = significantDigits;
	}
	
}
