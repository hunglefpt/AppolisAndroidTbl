/**
 * Name: $RCSfile: DataParser.java,v $
 * Version: $Revision: 1.1 $
 * Date: $Date: May 31, 2013 2:40:18 PM $
 *
 * Copyright (c) 2013 FPT Software, Inc. All rights reserved.
 */
package com.appolis.utilities;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.appolis.entities.EnBarcodeExistences;
import com.appolis.entities.EnItemLotInfo;
import com.appolis.entities.EnItemNumber;
import com.appolis.entities.EnItemPODetails;
import com.appolis.entities.EnLPNumber;
import com.appolis.entities.EnOrderPickSwitchInfo;
import com.appolis.entities.EnPO;
import com.appolis.entities.EnPassPutAway;
import com.appolis.entities.EnPurchaseOrderInfo;
import com.appolis.entities.EnPurchaseOrderReceiptInfo;
import com.appolis.entities.EnPutAway;
import com.appolis.entities.EnPutAwayBin;
import com.appolis.entities.EnReceivingInfo;
import com.appolis.entities.EnUom;
import com.appolis.entities.ObjectCountCycleCurrent;
import com.appolis.entities.ObjectCycleCount;
import com.appolis.entities.ObjectCycleItem;
import com.appolis.entities.ObjectCycleItemDetail;
import com.appolis.entities.ObjectCycleLp;
import com.appolis.entities.ObjectCycleUom;
import com.appolis.entities.ObjectSettingLanguage;
import com.appolis.entities.ObjectUser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * @author HoangNH11
 * Data Parser for Json
 */
public final class DataParser {

	private static Gson mGson;

	/**
	 * Gson member initialization
	 */
	private static void initGson() {
		if (null == mGson) {
			mGson = new Gson();
		}
	}
	
	public static ObjectUser getUser(String jsonData) throws JsonSyntaxException {
		if (StringUtils.isBlank(jsonData)) {
			return null;
		}
		Gson gson = new Gson();
		Type collectionType = new TypeToken<ObjectUser>() {
		}.getType();
		ObjectUser details = gson.fromJson(jsonData, collectionType);
		return details;
	}

	public static String convertObjectToString(Object jsonData)
			throws JsonSyntaxException {
		if (jsonData == null) {
			return null;
		}

		String json;		
		Gson gson = new Gson();
		json = gson.toJson(jsonData);
		return json;
	}

	public static List<ObjectSettingLanguage> getCultureInfo(String jsonData) throws JsonSyntaxException {
		if (StringUtils.isBlank(jsonData)) {
			return null;
		}
		Gson gson = new Gson();
		Type collectionType = new TypeToken<List<ObjectSettingLanguage>>() {
		}.getType();
		List<ObjectSettingLanguage> details = gson.fromJson(jsonData, collectionType);
		return details;
	}
	public static ArrayList<EnOrderPickSwitchInfo> getListOrderPickSwitchInfos(String jsonData) throws JsonSyntaxException {
		if (StringUtils.isBlank(jsonData)) {
			return null;
		}
		
		Gson gson = new Gson();
		Type collectionType = new TypeToken<ArrayList<EnOrderPickSwitchInfo>>(){}.getType();
		ArrayList<EnOrderPickSwitchInfo> details = gson.fromJson(jsonData, collectionType);
		return details;
	}
	
	public static ArrayList<EnReceivingInfo> getListReceiveInfo(String jsonData) throws JsonSyntaxException {
		if (StringUtils.isBlank(jsonData)) {
			return null;
		}
		
		Gson gson = new Gson();
		Type collectionType = new TypeToken<ArrayList<EnReceivingInfo>>() {}.getType();
		ArrayList<EnReceivingInfo> details = gson.fromJson(jsonData, collectionType);
		return details;
	}
	
	public static ArrayList<EnPurchaseOrderInfo> getListEnPurchaseOrderInfo(String jsonData) throws JsonSyntaxException {
		if (StringUtils.isBlank(jsonData)) {
			return null;
		}
		
		Gson gson = new Gson();
		Type collectionType = new TypeToken<ArrayList<EnPurchaseOrderInfo>>() {}.getType();
		ArrayList<EnPurchaseOrderInfo> details = gson.fromJson(jsonData, collectionType);
		return details;
	}
	
	public static ArrayList<EnPurchaseOrderReceiptInfo> getListEnPurchaseOrderReceiptInfo(String jsonData) throws JsonSyntaxException {
		if (StringUtils.isBlank(jsonData)) {
			return null;
		}
		
		Gson gson = new Gson();
		Type collectionType = new TypeToken<ArrayList<EnPurchaseOrderReceiptInfo>>() {}.getType();
		ArrayList<EnPurchaseOrderReceiptInfo> details = gson.fromJson(jsonData, collectionType);
		return details;
	}
	
	public static EnPurchaseOrderInfo getEnPurchaseOrderInfo(String jsonData) throws JsonSyntaxException {
		if (StringUtils.isBlank(jsonData)) {
			return null;
		}
		
		Gson gson = new Gson();
		Type collectionType = new TypeToken<EnPurchaseOrderInfo>() {}.getType();
		EnPurchaseOrderInfo details = gson.fromJson(jsonData, collectionType);
		return details;
	}
	
	public static EnItemLotInfo getEnItemLotInfo(String jsonData) throws JsonSyntaxException {
		if (StringUtils.isBlank(jsonData)) {
			return null;
		}
		
		Gson gson = new Gson();
		Type collectionType = new TypeToken<EnItemLotInfo>() {}.getType();
		EnItemLotInfo details = gson.fromJson(jsonData, collectionType);
		return details;
	}
	
	public static ObjectCycleCount getCycleCount(String jsonData) throws JsonSyntaxException {
		if (StringUtils.isBlank(jsonData)) {
			return null;
		}
		Gson gson = new Gson();
		ObjectCycleCount details = gson.fromJson(jsonData, ObjectCycleCount.class);
		return details;
	}
	
	public static ArrayList<ObjectCountCycleCurrent> getCycleCountCurrentList(String jsonData) throws JsonSyntaxException {
		if (StringUtils.isBlank(jsonData)) {
			return null;
		}
		
		Gson gson = new Gson();
		Type collectionType = new TypeToken<ArrayList<ObjectCountCycleCurrent>>() {}.getType();
		ArrayList<ObjectCountCycleCurrent> details = gson.fromJson(jsonData, collectionType);
		return details;
	}
	
	public static ArrayList<ObjectCycleUom> getCycleUomList(String jsonData) throws JsonSyntaxException {
		if (StringUtils.isBlank(jsonData)) {
			return null;
		}
		
		Gson gson = new Gson();
		Type collectionType = new TypeToken<ArrayList<ObjectCycleUom>>() {}.getType();
		ArrayList<ObjectCycleUom> details = gson.fromJson(jsonData, collectionType);
		return details;
	}
	
	public static EnBarcodeExistences getBarcode(String jsonData) throws JsonSyntaxException {
		if (StringUtils.isBlank(jsonData)) {
			return null;
		}
		Gson gson = new Gson();
		Type collectionType = new TypeToken<EnBarcodeExistences>() {
		}.getType();
		EnBarcodeExistences details = gson.fromJson(jsonData, collectionType);
		return details;
	}
	
	public static ArrayList<EnPutAway> getPutAway(String jsonData) throws JsonSyntaxException {
		
		if (StringUtils.isBlank(jsonData)) {
			return null;
		}
		
		Gson gson = new Gson();
		Type collectionType = new TypeToken<ArrayList<EnPutAway>>() {}.getType();
		
		ArrayList<EnPutAway> details = gson.fromJson(jsonData, collectionType);
		return details;
	}
	
	public static EnPassPutAway getEnPutAway(String jsonData) throws JsonSyntaxException {
		
		if (StringUtils.isBlank(jsonData)) {
			return null;
		}
		
		Gson gson = new Gson();
		Type collectionType = new TypeToken<EnPassPutAway>() {}.getType();
		
		EnPassPutAway details = gson.fromJson(jsonData, collectionType);
		return details;
	}
	
	public static EnItemNumber getItemNumber(String jsonData) throws JsonSyntaxException {
		if (StringUtils.isBlank(jsonData)) {
			return null;
		}
		Gson gson = new Gson();
		Type collectionType = new TypeToken<EnItemNumber>() {
		}.getType();
		EnItemNumber details = gson.fromJson(jsonData, collectionType);
		return details;
	}
	
	public static ArrayList<EnUom> getUom(String jsonData) throws JsonSyntaxException {
		if (StringUtils.isBlank(jsonData)) {
			return null;
		}
		Gson gson = new Gson();
		Type collectionType = new TypeToken<ArrayList<EnUom>>() {
		}.getType();
		ArrayList<EnUom> details = gson.fromJson(jsonData, collectionType);
		return details;
	}
	
	public static ObjectCycleLp getCycleLP(String jsonData) throws JsonSyntaxException {
		if (StringUtils.isBlank(jsonData)) {
			return null;
		}
		Gson gson = new Gson();
		Type collectionType = new TypeToken<ObjectCycleLp>() {
		}.getType();
		ObjectCycleLp details = gson.fromJson(jsonData, collectionType);
		return details;
	}
	
	public static ObjectCycleItem getCycleItem(String jsonData) throws JsonSyntaxException {
		if (StringUtils.isBlank(jsonData)) {
			return null;
		}
		Gson gson = new Gson();
		Type collectionType = new TypeToken<ObjectCycleItem>() {
		}.getType();
		ObjectCycleItem details = gson.fromJson(jsonData, collectionType);
		return details;
	}
	

	public static ObjectCycleItemDetail getCycleItemDetail(String jsonData) throws JsonSyntaxException {
		if (StringUtils.isBlank(jsonData)) {
			return null;
		}
		Gson gson = new Gson();
		Type collectionType = new TypeToken<ObjectCycleItemDetail>() {
		}.getType();
		ObjectCycleItemDetail details = gson.fromJson(jsonData, collectionType);
		return details;
	}

	public static EnLPNumber getBinLPNumber(String jsonData) throws JsonSyntaxException {
		if (StringUtils.isBlank(jsonData)) {
			return null;
		}
		Gson gson = new Gson();
		Type collectionType = new TypeToken<EnLPNumber>() {
		}.getType();
		EnLPNumber details = gson.fromJson(jsonData, collectionType);
		return details;
	}
	
	public static ArrayList<EnPutAwayBin> getPutAwayBinTrue(String jsonData) throws JsonSyntaxException {
		if (StringUtils.isBlank(jsonData)) {
			return null;
		}
		Gson gson = new Gson();
		Type collectionType = new TypeToken<ArrayList<EnPutAwayBin>>() {
		}.getType();
		ArrayList<EnPutAwayBin> details = gson.fromJson(jsonData, collectionType);
		return details;
	}
	
	public static EnItemPODetails getPutAwayDetails(String jsonData) throws JsonSyntaxException {
		if (StringUtils.isBlank(jsonData)) {
			return null;
		}
		Gson gson = new Gson();
		Type collectionType = new TypeToken<EnItemPODetails>() {
		}.getType();
		EnItemPODetails details = gson.fromJson(jsonData, collectionType);
		return details;
	}
	
	public static EnPO getLPNumber(String jsonData) throws JsonSyntaxException {
		if (StringUtils.isBlank(jsonData)) {
			return null;
		}
		Gson gson = new Gson();
		Type collectionType = new TypeToken<EnPO>() {
		}.getType();
		EnPO details = gson.fromJson(jsonData, collectionType);
		return details;
	}
	
}
