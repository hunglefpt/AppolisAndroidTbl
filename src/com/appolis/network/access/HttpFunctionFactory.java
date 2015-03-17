/**
 * Name: $RCSfile: HttpFunctionFactory.java,v $
 * Version: $Revision: 1.333 $
 * Date: $Date: 2013/04/01 11:24:48 $
 * Version: $Revision: 1.333 $
 * Date: $Date: 2013/04/01 11:24:48 $
 *
 */

package com.appolis.network.access;

import java.net.URLEncoder;

import android.util.Log;

import com.appolis.common.AppPreferences;
import com.appolis.network.HttpConnection;
import com.appolis.network.NetParameter;
import com.appolis.utilities.BuManagement;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.Logger;

/**
 * This class handle API and Environment in project
 * 
 * @author HoangNH11
 */
public final class HttpFunctionFactory {
	public static String appolisHostURLshort = "http://demo.appolis.com/ioswebapi/";
	private static String userAgent = BuManagement.getUserAgent();
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static HttpFunctionInfo createPutBodyMethod(String name) {
		HttpFunctionInfo func = new HttpFunctionInfo();
		func.setFunctionName(name);
		func.setMethodType(HttpConnection.HTTP_METHOD_PUT_BODY);
		return func;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public static HttpFunctionInfo createDeleteMethod(String name) {
		HttpFunctionInfo func = new HttpFunctionInfo();
		func.setFunctionName(name);
		func.setMethodType(HttpConnection.HTTP_METHOD_DELETE);
		return func;
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static HttpFunctionInfo createDeleteBodyMethod(String name) {
		HttpFunctionInfo func = new HttpFunctionInfo();
		func.setFunctionName(name);
		func.setMethodType(HttpConnection.HTTP_METHOD_DELETE_BODY);
		return func;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public static HttpFunctionInfo createGetMethod(String name) {
		HttpFunctionInfo func = new HttpFunctionInfo();
		func.setFunctionName(name);
		func.setMethodType(HttpConnection.HTTP_METHOD_GET);
		return func;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public static HttpFunctionInfo createPostMethod(String name) {
		HttpFunctionInfo func = new HttpFunctionInfo();
		func.setFunctionName(name);
		func.setMethodType(HttpConnection.HTTP_METHOD_POST);
		return func;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public static HttpFunctionInfo createPostBodyMethod(String name) {
		HttpFunctionInfo func = new HttpFunctionInfo();
		func.setFunctionName(name);
		func.setMethodType(HttpConnection.HTTP_METHOD_POST_BODY);
		return func;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public static HttpFunctionInfo createPutMethod(String name) {
		HttpFunctionInfo func = new HttpFunctionInfo();
		func.setFunctionName(name);
		func.setMethodType(HttpConnection.HTTP_METHOD_PUT);
		return func;
	}

	public static String getParam(NetParameter[] netParameters) {
    	String params = "";
		for (int i = 0; i < netParameters.length; i++) {
			if (i == netParameters.length - 1) {
				params = params + netParameters[i].getName() + "="
						+ netParameters[i].getValue().trim();
			} else {
				params = params + netParameters[i].getName() + "="
						+ netParameters[i].getValue().trim() + "&";
			}
		}	
		
		return params;
	}
	
	/**
	 * getParamWithEncode
	 * @param netParameters
	 * @return
	 */
	public static String getParamWithEncode(NetParameter[] netParameters) throws Exception{
		String params = "";
		for (int i = 0; i < netParameters.length; i++) {
			if (i == netParameters.length - 1) {
				params = params + netParameters[i].getName() + "="
						+ URLEncoder.encode(netParameters[i].getValue().trim(), GlobalParams.UTF_8);
			} else {
				params = params + netParameters[i].getName() + "="
						+ URLEncoder.encode(netParameters[i].getValue().trim(), GlobalParams.UTF_8) + "&";
			}
		}	
		
		return params;
	}
	
	public static HttpFunctionInfo firstLogin(NetParameter[] netParameters){
		HttpFunctionInfo functionInfo = createGetMethod("firstLogin");	
		String params = getParam(netParameters);
		Log.e("firstLogin", "firstLogin: " + params);	
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/warehouse?" + params);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}
	
	public static HttpFunctionInfo login(NetParameter[] netParameters){
		HttpFunctionInfo functionInfo = createGetMethod("login");	
		String params = getParam(netParameters);
		Log.e("login", "login: " + params);
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/warehouse?" + params);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}
	
	public static HttpFunctionInfo getUser() {
		HttpFunctionInfo functionInfo = createGetMethod("getUser");	
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/user?");
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		Logger.error(HttpUtilities.getHeaderAuthorization().getValue());
		return functionInfo;
	}
	
	public static HttpFunctionInfo getCycleCountList() {
		HttpFunctionInfo functionInfo = createGetMethod("getCycleCountList");	
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/cyclecount");
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}
	
	public static HttpFunctionInfo getCycleCountList(NetParameter[] netParameters) {
		HttpFunctionInfo functionInfo = createGetMethod("getCycleCountCurrentList");	
		String params = getParam(netParameters);
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/cyclecount?" + params);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}
	
	public static HttpFunctionInfo getProfileVersion() {
		HttpFunctionInfo functionInfo = createGetMethod("profileVersion");
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/version");
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}
	
	public static HttpFunctionInfo updateProfilePin(NetParameter[] netParameters) {
		HttpFunctionInfo functionInfo = createPostMethod("profileUpdatePin");
		String params = getParam(netParameters);
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/user/?" + params);
//		functionInfo.setUrl("http://10.16.36.95/webapi_v7" + "/api/user/?" + params);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}
	
	public static HttpFunctionInfo getCycleAdjustment(NetParameter[] netParameters) {
		HttpFunctionInfo functionInfo = createGetMethod("getCycleAdjustment");
		String params = getParam(netParameters);
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/uom/?" + params);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}
	
	public static HttpFunctionInfo getCycleQuanity(NetParameter[] netParameters) {
		HttpFunctionInfo functionInfo = createGetMethod("getCycleQuanity");
		String params = getParam(netParameters);
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/item?" + params);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}
	
	public static HttpFunctionInfo updateCycleCountItem(NetParameter[] netParameters) {
		HttpFunctionInfo functionInfo = createPostMethod("updateCycleCountItem");
		String params = getParam(netParameters);
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/cyclecount/?" + params);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}
	
	public static HttpFunctionInfo getCultureInfo(NetParameter[] netParameters){
		HttpFunctionInfo functionInfo = createGetMethod("getCultureInfo");	
		String params = getParam(netParameters);	
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/warehouse?" + params);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}
	
	public static HttpFunctionInfo getReceiveList() throws Exception{
		HttpFunctionInfo functionInfo = createGetMethod("getReceiveList");
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/po");
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}
	
	public static HttpFunctionInfo getReceiveDetail(String poID)throws Exception {
		HttpFunctionInfo functionInfo = createGetMethod("getReceiveDetail");
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/po/?poNumber=" + poID);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}
	
	public static HttpFunctionInfo commitReceiveDetail(int purChaseId) throws Exception{
		HttpFunctionInfo functionInfo = createPostMethod("commitReceiveDetail");
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/po/?purchaseOrderId=" + purChaseId);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}
	
	public static HttpFunctionInfo getReceiveItemDetailWithBarcode(String barcode) throws Exception {
		HttpFunctionInfo functionInfo = createGetMethod("getReceiveItemDetailWithBarcode");
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/Item?barcode=" + barcode);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}
	
	public static HttpFunctionInfo commitReceiveItemDetail(NetParameter[] netParameters) throws Exception{
		HttpFunctionInfo functionInfo = createPostMethod("commitReceiveItemDetail");	
		String params = getParamWithEncode(netParameters);
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/po/?" + params);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}
	
	public static HttpFunctionInfo undoReceiveItemDetail(String netParameters) throws Exception{
		HttpFunctionInfo functionInfo = createDeleteBodyMethod("undoReceiveItemDetail");	
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/po?");
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization(),
				HttpUtilities.getHeaderContentJSon()});
		functionInfo.setBody(netParameters);
		return functionInfo;
	}
	
	public static HttpFunctionInfo commitReceiveOptionDamage( NetParameter[] netParameters) throws Exception{
		HttpFunctionInfo functionInfo = createPostMethod("commitReceiveOptionDamage");		
		String params = getParam(netParameters);
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/po/?" + params);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}
	
	public static HttpFunctionInfo getPickOrderList(String orderType) throws Exception{
		HttpFunctionInfo functionInfo = createGetMethod("getPickOrderList");
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/order?orderType=" + orderType);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}
	
	public static HttpFunctionInfo putAcquireBarcode(NetParameter[] netParameters) throws Exception{
		HttpFunctionInfo functionInfo = createPutMethod("putAcquireBarcode");
		String params = getParamWithEncode(netParameters);
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/uom?" + params);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}
	public static HttpFunctionInfo createNewLisecePlate( NetParameter[] netParameters) throws Exception{
		HttpFunctionInfo functionInfo = createPutMethod("createNewLisecePlate");		
		String params = getParam(netParameters);
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/bins/?" + params);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}
	
	public static HttpFunctionInfo getBarcode(NetParameter[] netParameters) throws Exception{
		HttpFunctionInfo functionInfo = createGetMethod("getBarcode");	
		String params = getParam(netParameters);
		Log.e("getBarcode", "getBarcode: " + params);
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/barcode?" + params);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}
	
	public static HttpFunctionInfo getItemBarcode(NetParameter[] netParameters) throws Exception{
		HttpFunctionInfo functionInfo = createGetMethod("getItemBarcode");	
		String params = getParam(netParameters);
		Log.e("getItemBarcode", "getItemBarcode: " + params);
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/Item?" + params);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}
	
	public static HttpFunctionInfo getUOMItemNumber(NetParameter[] netParameters) throws Exception{
		HttpFunctionInfo functionInfo = createGetMethod("getUOMItemNumber");	
		String params = getParam(netParameters);
		Log.e("getUOMItemNumber", "getUOMItemNumber: " + params);
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/uom?" + params);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}
	
	public static HttpFunctionInfo getBins(NetParameter[] netParameters) throws Exception{
		HttpFunctionInfo functionInfo = createGetMethod("getBins");	
		String params = getParam(netParameters);
		Log.e("getBins", "getBins: " + params);
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/bins?" + params);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}
	
	public static HttpFunctionInfo postItem(NetParameter[] netParameters) throws Exception{
		HttpFunctionInfo functionInfo = createPostBodyMethod("postItem");		
		String params = getParam(netParameters);
		Log.e("postItem", "postItem: " + params);
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/Item");
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization(), HttpUtilities.getHeaderContentJSon()});
		functionInfo.setParams(netParameters);
		return functionInfo;
	}
	
	public static HttpFunctionInfo postItem(String netParameters) throws Exception{
		HttpFunctionInfo functionInfo = createPostBodyMethod("postItem");
		Log.e("postItem", "postItem: " + netParameters);
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/Item");
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization(), HttpUtilities.getHeaderContentJSon()});
		functionInfo.setBody(netParameters);
		return functionInfo;
	}
	
	public static HttpFunctionInfo getLPNumber(NetParameter[] netParameters) throws Exception{
		HttpFunctionInfo functionInfo = createGetMethod("getLPNumber");
		String params = getParam(netParameters);
		Log.e("getLPNumber", "getLPNumber: " + params);
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/po?" + params);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}
	
	public static HttpFunctionInfo submitReceiveInventory(NetParameter[] netParameters) throws Exception{
		HttpFunctionInfo functionInfo = createPostBodyMethod("submitReceiveInventory");
		String params = getParam(netParameters);
		Log.e("submitReceiveInventory", "submitReceiveInventory: " + params);
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/po");
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		functionInfo.setParams(netParameters);
		return functionInfo;
	}

	public static HttpFunctionInfo setCompletedBins(NetParameter[] netParameters) throws Exception{
		HttpFunctionInfo functionInfo = createPostMethod("setCompletedBins");	
		String params = getParam(netParameters);
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/cyclecount/?" + params);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}

	public static HttpFunctionInfo getItemByBarcode(NetParameter[] netParameters) throws Exception{
		HttpFunctionInfo functionInfo = createGetMethod("getItemByBarcode");	
		String params = getParam(netParameters);
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/item/?" + params);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}

	public static HttpFunctionInfo getLpByBarcode(NetParameter[] netParameters) throws Exception{
		HttpFunctionInfo functionInfo = createGetMethod("getLpByBarcode");	
		String params = getParam(netParameters);
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/bins/?" + params);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}

	public static HttpFunctionInfo getDataBinOrLpOption(NetParameter[] netParameters) throws Exception{
		HttpFunctionInfo functionInfo = createGetMethod("getDataBinOrLpOption");	
		String params = getParam(netParameters);
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/cyclecount/?" + params);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}

	public static HttpFunctionInfo getPutAway() {
		HttpFunctionInfo functionInfo = createGetMethod("getPutAway");
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/putaway");
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}
	
	public static HttpFunctionInfo getPutAway(NetParameter[] netParameters) {
		HttpFunctionInfo functionInfo = createGetMethod("getPutAway");	
		String params = getParam(netParameters);
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/putaway?" + params);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}

	public static HttpFunctionInfo creareLpOption(NetParameter[] netParameters) {
		HttpFunctionInfo functionInfo = createPutMethod("creareLpOption");	
		String params = getParam(netParameters);
		functionInfo.setUrl(AppPreferences.getURLFirstLogin() + "/api/bins/?" + params);
		functionInfo.setHeader(new NetParameter[] {HttpUtilities.getHeaderAuthorization()});
		return functionInfo;
	}

}
