/**
 * Name: $RCSfile: EnBarcodeExistences.java,v $
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
public class EnBarcodeExistences implements Serializable{

	private static final long serialVersionUID = 3044778690816984208L;
	private int BinOnlyCount;
	private int ItemIdentificationCount;
	private int ItemOnlyCount;
	private int LPCount;
	private int LotOnlyCount;
	private int OrderCount;
	private int PoCount;
	private int UOMBarcodeCount;
	private int GtinCount;
	
	/**
	 * @return the binOnlyCount
	 */
	public int getBinOnlyCount() {
		return BinOnlyCount;
	}
	
	/**
	 * @param binOnlyCount the binOnlyCount to set
	 */
	public void setBinOnlyCount(int binOnlyCount) {
		BinOnlyCount = binOnlyCount;
	}
	/**
	 * @return the itemIdentificationCount
	 */
	public int getItemIdentificationCount() {
		return ItemIdentificationCount;
	}
	/**
	 * @param itemIdentificationCount the itemIdentificationCount to set
	 */
	public void setItemIdentificationCount(int itemIdentificationCount) {
		ItemIdentificationCount = itemIdentificationCount;
	}
	/**
	 * @return the itemOnlyCount
	 */
	public int getItemOnlyCount() {
		return ItemOnlyCount;
	}
	/**
	 * @param itemOnlyCount the itemOnlyCount to set
	 */
	public void setItemOnlyCount(int itemOnlyCount) {
		ItemOnlyCount = itemOnlyCount;
	}
	/**
	 * @return the lPCount
	 */
	public int getLPCount() {
		return LPCount;
	}
	/**
	 * @param lPCount the lPCount to set
	 */
	public void setLPCount(int lPCount) {
		LPCount = lPCount;
	}
	/**
	 * @return the lotOnlyCount
	 */
	public int getLotOnlyCount() {
		return LotOnlyCount;
	}
	/**
	 * @param lotOnlyCount the lotOnlyCount to set
	 */
	public void setLotOnlyCount(int lotOnlyCount) {
		LotOnlyCount = lotOnlyCount;
	}
	/**
	 * @return the orderCount
	 */
	public int getOrderCount() {
		return OrderCount;
	}
	/**
	 * @param orderCount the orderCount to set
	 */
	public void setOrderCount(int orderCount) {
		OrderCount = orderCount;
	}
	/**
	 * @return the poCount
	 */
	public int getPoCount() {
		return PoCount;
	}
	/**
	 * @param poCount the poCount to set
	 */
	public void setPoCount(int poCount) {
		PoCount = poCount;
	}
	/**
	 * @return the uOMBarcodeCount
	 */
	public int getUOMBarcodeCount() {
		return UOMBarcodeCount;
	}
	/**
	 * @param uOMBarcodeCount the uOMBarcodeCount to set
	 */
	public void setUOMBarcodeCount(int uOMBarcodeCount) {
		UOMBarcodeCount = uOMBarcodeCount;
	}
	/**
	 * @return the gtinCount
	 */
	public int getGtinCount() {
		return GtinCount;
	}
	/**
	 * @param gtinCount the gtinCount to set
	 */
	public void setGtinCount(int gtinCount) {
		GtinCount = gtinCount;
	}
	
}
