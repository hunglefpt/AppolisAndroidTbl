/**
 * Name: $RCSfile: EnItemPODetails.java,v $
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
public class EnItemPODetails implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2315856715955169817L;
	private int ItemID;
	private String ItemNumber;
	private boolean IsLotTracked;
	private String LotNumber;
	private int UomTypeID;
	private String UomDescription;
	private double QuantityOnHand;
	private int SignificantDigits;
	private String BinNumber;
	private String BinPath;
	private boolean IsLp;
	private int Weight;
	private int BaseConversionFactor;
	private String ItemDescription;
	private boolean IsNewBin;
	private boolean IsFullyPickable;
	private boolean UpdateDefaultLp;
	private String MajorProductClass;
	private boolean IsExpirationTracked;
	/**
	 * @return the itemID
	 */
	public int getItemID() {
		return ItemID;
	}
	/**
	 * @param itemID the itemID to set
	 */
	public void setItemID(int itemID) {
		ItemID = itemID;
	}
	/**
	 * @return the itemNumber
	 */
	public String getItemNumber() {
		return ItemNumber;
	}
	/**
	 * @param itemNumber the itemNumber to set
	 */
	public void setItemNumber(String itemNumber) {
		ItemNumber = itemNumber;
	}
	/**
	 * @return the isLotTracked
	 */
	public boolean isIsLotTracked() {
		return IsLotTracked;
	}
	/**
	 * @param isLotTracked the isLotTracked to set
	 */
	public void setIsLotTracked(boolean isLotTracked) {
		IsLotTracked = isLotTracked;
	}
	/**
	 * @return the lotNumber
	 */
	public String getLotNumber() {
		return LotNumber;
	}
	/**
	 * @param lotNumber the lotNumber to set
	 */
	public void setLotNumber(String lotNumber) {
		LotNumber = lotNumber;
	}
	/**
	 * @return the uomTypeID
	 */
	public int getUomTypeID() {
		return UomTypeID;
	}
	/**
	 * @param uomTypeID the uomTypeID to set
	 */
	public void setUomTypeID(int uomTypeID) {
		UomTypeID = uomTypeID;
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
	 * @return the quantityOnHand
	 */
	public double getQuantityOnHand() {
		return QuantityOnHand;
	}
	/**
	 * @param quantityOnHand the quantityOnHand to set
	 */
	public void setQuantityOnHand(double quantityOnHand) {
		QuantityOnHand = quantityOnHand;
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
	/**
	 * @return the binNumber
	 */
	public String getBinNumber() {
		return BinNumber;
	}
	/**
	 * @param binNumber the binNumber to set
	 */
	public void setBinNumber(String binNumber) {
		BinNumber = binNumber;
	}
	/**
	 * @return the binPath
	 */
	public String getBinPath() {
		return BinPath;
	}
	/**
	 * @param binPath the binPath to set
	 */
	public void setBinPath(String binPath) {
		BinPath = binPath;
	}
	/**
	 * @return the isLp
	 */
	public boolean isIsLp() {
		return IsLp;
	}
	/**
	 * @param isLp the isLp to set
	 */
	public void setIsLp(boolean isLp) {
		IsLp = isLp;
	}
	/**
	 * @return the weight
	 */
	public int getWeight() {
		return Weight;
	}
	/**
	 * @param weight the weight to set
	 */
	public void setWeight(int weight) {
		Weight = weight;
	}
	/**
	 * @return the baseConversionFactor
	 */
	public int getBaseConversionFactor() {
		return BaseConversionFactor;
	}
	/**
	 * @param baseConversionFactor the baseConversionFactor to set
	 */
	public void setBaseConversionFactor(int baseConversionFactor) {
		BaseConversionFactor = baseConversionFactor;
	}
	/**
	 * @return the itemDescription
	 */
	public String getItemDescription() {
		return ItemDescription;
	}
	/**
	 * @param itemDescription the itemDescription to set
	 */
	public void setItemDescription(String itemDescription) {
		ItemDescription = itemDescription;
	}
	/**
	 * @return the isNewBin
	 */
	public boolean isIsNewBin() {
		return IsNewBin;
	}
	/**
	 * @param isNewBin the isNewBin to set
	 */
	public void setIsNewBin(boolean isNewBin) {
		IsNewBin = isNewBin;
	}
	/**
	 * @return the isFullyPickable
	 */
	public boolean isIsFullyPickable() {
		return IsFullyPickable;
	}
	/**
	 * @param isFullyPickable the isFullyPickable to set
	 */
	public void setIsFullyPickable(boolean isFullyPickable) {
		IsFullyPickable = isFullyPickable;
	}
	/**
	 * @return the updateDefaultLp
	 */
	public boolean isUpdateDefaultLp() {
		return UpdateDefaultLp;
	}
	/**
	 * @param updateDefaultLp the updateDefaultLp to set
	 */
	public void setUpdateDefaultLp(boolean updateDefaultLp) {
		UpdateDefaultLp = updateDefaultLp;
	}
	/**
	 * @return the majorProductClass
	 */
	public String getMajorProductClass() {
		return MajorProductClass;
	}
	/**
	 * @param majorProductClass the majorProductClass to set
	 */
	public void setMajorProductClass(String majorProductClass) {
		MajorProductClass = majorProductClass;
	}
	/**
	 * @return the isExpirationTracked
	 */
	public boolean isIsExpirationTracked() {
		return IsExpirationTracked;
	}
	/**
	 * @param isExpirationTracked the isExpirationTracked to set
	 */
	public void setIsExpirationTracked(boolean isExpirationTracked) {
		IsExpirationTracked = isExpirationTracked;
	}
	
}
