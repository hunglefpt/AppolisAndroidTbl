/**
 * Name: $RCSfile: EnBinTransfer.java,v $
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
public class EnBinTransfer implements Serializable{

	private static final long serialVersionUID = -4361680164745881099L;
	public String FromBinNumber;
    public String ItemNumber;
    public String LotNumber;
    public String UomDescription;
    public double Quantity;
    public String ToBinNumber;
    public boolean IsLicensePlate;
    public String TransactionType;
    
	/**
	 * @return the fromBinNumber
	 */
	public String getFromBinNumber() {
		return FromBinNumber;
	}
	/**
	 * @param fromBinNumber the fromBinNumber to set
	 */
	public void setFromBinNumber(String fromBinNumber) {
		FromBinNumber = fromBinNumber;
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
	 * @return the quantity
	 */
	public double getQuantity() {
		return Quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(double quantity) {
		Quantity = quantity;
	}
	/**
	 * @return the toBinNumber
	 */
	public String getToBinNumber() {
		return ToBinNumber;
	}
	/**
	 * @param toBinNumber the toBinNumber to set
	 */
	public void setToBinNumber(String toBinNumber) {
		ToBinNumber = toBinNumber;
	}
	/**
	 * @return the isLicensePlate
	 */
	public boolean isIsLicensePlate() {
		return IsLicensePlate;
	}
	/**
	 * @param isLicensePlate the isLicensePlate to set
	 */
	public void setIsLicensePlate(boolean isLicensePlate) {
		IsLicensePlate = isLicensePlate;
	}
	/**
	 * @return the transactionType
	 */
	public String getTransactionType() {
		return TransactionType;
	}
	/**
	 * @param transactionType the transactionType to set
	 */
	public void setTransactionType(String transactionType) {
		TransactionType = transactionType;
	}
    
}
