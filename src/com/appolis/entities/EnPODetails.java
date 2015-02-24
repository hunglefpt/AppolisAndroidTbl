/**
 * Name: $RCSfile: EnPODetails.java,v $
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
public class EnPODetails implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4716641594983663412L;
	private EnItemPODetails Item;
	private double Qty;
	/**
	 * @return the item
	 */
	public EnItemPODetails getItem() {
		return Item;
	}
	/**
	 * @param item the item to set
	 */
	public void setItem(EnItemPODetails item) {
		Item = item;
	}
	/**
	 * @return the qty
	 */
	public double getQty() {
		return Qty;
	}
	/**
	 * @param qty the qty to set
	 */
	public void setQty(double qty) {
		Qty = qty;
	}
	
}
