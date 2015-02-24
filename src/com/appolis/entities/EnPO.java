/**
 * Name: $RCSfile: EnPO.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author hoangnh11
 */
public class EnPO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7297218020584287873L;
	private int POID;
	private String PONumber;
	private ArrayList<EnPODetails> PODetails;
	private int TotalLpReceived;
	private int TotalLpShipped;
	private String LpNumber;
	/**
	 * @return the pOID
	 */
	public int getPOID() {
		return POID;
	}
	/**
	 * @param pOID the pOID to set
	 */
	public void setPOID(int pOID) {
		POID = pOID;
	}
	/**
	 * @return the pONumber
	 */
	public String getPONumber() {
		return PONumber;
	}
	/**
	 * @param pONumber the pONumber to set
	 */
	public void setPONumber(String pONumber) {
		PONumber = pONumber;
	}
	/**
	 * @return the pODetails
	 */
	public ArrayList<EnPODetails> getPODetails() {
		return PODetails;
	}
	/**
	 * @param pODetails the pODetails to set
	 */
	public void setPODetails(ArrayList<EnPODetails> pODetails) {
		PODetails = pODetails;
	}
	/**
	 * @return the totalLpReceived
	 */
	public int getTotalLpReceived() {
		return TotalLpReceived;
	}
	/**
	 * @param totalLpReceived the totalLpReceived to set
	 */
	public void setTotalLpReceived(int totalLpReceived) {
		TotalLpReceived = totalLpReceived;
	}
	/**
	 * @return the totalLpShipped
	 */
	public int getTotalLpShipped() {
		return TotalLpShipped;
	}
	/**
	 * @param totalLpShipped the totalLpShipped to set
	 */
	public void setTotalLpShipped(int totalLpShipped) {
		TotalLpShipped = totalLpShipped;
	}
	/**
	 * @return the lpNumber
	 */
	public String getLpNumber() {
		return LpNumber;
	}
	/**
	 * @param lpNumber the lpNumber to set
	 */
	public void setLpNumber(String lpNumber) {
		LpNumber = lpNumber;
	}
	
}
