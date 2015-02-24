/*
 * Name: $RCSfile: INetServices.java,v $
 * Version: $Revision: 1.189 $
 * Date: $Date: 2013/04/01 02:09:59 $
 *
 */

package com.appolis.network.access;

import com.appolis.network.NetParameter;

/**
 * @author HoangNH11
 */
public interface INetServices {	
	
	String firstLogin(NetParameter[] netParameters) throws Exception;	
	String login(NetParameter[] netParameters) throws Exception;
	String getUser() throws Exception;
	String getCultureInfo(NetParameter[] netParameters) throws Exception;
	String getCycleCount() throws Exception;
	String getCycleCountCurrentList(NetParameter[] parameters) throws Exception;
	String getReceiveList() throws Exception;
	String getReceiveDetail(String poID) throws Exception;
	String getBarcode(NetParameter[] netParameters) throws Exception;
	String getProfileVersion() throws Exception;
	String updateProfilePin(NetParameter[] netParameters) throws Exception;
	String getItemBarcode(NetParameter []netParameters) throws Exception;
	String getUOMItemNumber(NetParameter []netParameters) throws Exception;
	String getBins(NetParameter[] netParameters) throws Exception;
	String postItem(NetParameter[] netParameters) throws Exception;
	String postItem(String netParameters) throws Exception;
	String getLPNumber(NetParameter[] netParameters) throws Exception;
	String submitReceiveInventory(NetParameter[] netParameters) throws Exception;
	String getCycleAdjustment(NetParameter []netParameters) throws Exception;
	String getCycleQuanity(NetParameter []netParameters) throws Exception;	
	String commitReceiveItemDetail(NetParameter[] netParameters) throws Exception;
	String getReceiveItemDetailWithBarcode(String barcode) throws Exception;
	String commitReceiveDetail(int purChaseId) throws Exception;
	String updateCycleCountItem(NetParameter []netParameters) throws Exception;
	String commitReceiveOptionDamage(NetParameter[] netParameters) throws Exception;
	String setCompletedBins(NetParameter []netParameters) throws Exception;
	String getLpByBarcode(NetParameter []netParameters) throws Exception;
	String getItemByBarcode(NetParameter []netParameters) throws Exception;
	String getPutAway() throws Exception;
	String getPutAway(NetParameter []netParameters) throws Exception;	
	String undoReceiveItemDetail(String netParameters) throws Exception;
	String getDataBinOrLpOption(NetParameter []netParameters) throws Exception;	
	String creareLpOption(NetParameter []netParameter) throws Exception;
	String createNewLisecePlate(NetParameter[] netParameters) throws Exception;
}
