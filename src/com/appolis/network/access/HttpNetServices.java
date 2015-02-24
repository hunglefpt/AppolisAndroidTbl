/*
 * Name: $RCSfile: HttpNetServices.java,v $
 * Version: $Revision: 1.273 $
 * Date: $Date: 2013/04/01 02:09:58 $
 *
 */

package com.appolis.network.access;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.appolis.common.AppolisException;
import com.appolis.common.AppolisNetworkException;
import com.appolis.network.HttpCommand;
import com.appolis.network.HttpConnection;
import com.appolis.network.HttpDeleteBodyMethod;
import com.appolis.network.HttpDeleteMethod;
import com.appolis.network.HttpGetMethod;
import com.appolis.network.HttpPostBodyMethod;
import com.appolis.network.HttpPostMethod;
import com.appolis.network.HttpPutBodyMethod;
import com.appolis.network.HttpPutMethod;
import com.appolis.network.HttpsMethod;
import com.appolis.network.NetParameter;

/**
 * @author HoangNH11
 */
public final class HttpNetServices implements INetServices {

	public final static HttpNetServices Instance = new HttpNetServices();

	private HttpCommand getHttpCommand(HttpFunctionInfo func) {
		HttpCommand cmd = new HttpCommand();
		cmd.setParam(func.getParams());
		cmd.setHeader(func.getHeader());
		cmd.setBody(func.getBody());
		return cmd;
	}

	private String executer(HttpFunctionInfo funcInfo) throws Exception {
		HttpConnection httpConnection = null;
		switch (funcInfo.getMethodType()) {
		case HttpConnection.HTTP_METHOD_POST:
			httpConnection = new HttpPostMethod(funcInfo.getUrl());
			break;
		case HttpConnection.HTTP_METHOD_GET:
			httpConnection = new HttpGetMethod(funcInfo.getUrl());
			break;
		case HttpConnection.HTTPS_METHOD:
			httpConnection = new HttpsMethod(funcInfo.getUrl());
			break;
		case HttpConnection.HTTP_METHOD_PUT:
			httpConnection = new HttpPutMethod(funcInfo.getUrl());
			break;
		case HttpConnection.HTTP_METHOD_DELETE:
			httpConnection = new HttpDeleteMethod(funcInfo.getUrl());
			break;
		case HttpConnection.HTTP_METHOD_DELETE_BODY:
			httpConnection = new HttpDeleteBodyMethod(funcInfo.getUrl());
			break;
		case HttpConnection.HTTP_METHOD_POST_BODY:
			httpConnection = new HttpPostBodyMethod(funcInfo.getUrl());
			break;
		case HttpConnection.HTTP_METHOD_PUT_BODY:
			httpConnection = new HttpPutBodyMethod(funcInfo.getUrl());
			break;
		}
		HttpCommand cmd = getHttpCommand(funcInfo);
		return httpConnection.makeRequest(cmd);
	}
		
	public String firstLogin(NetParameter[] netParameters) throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.firstLogin(netParameters);
		return executer(functionInfo);
	}
	
	public String login(NetParameter[] netParameters) throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.login(netParameters);
		return executer(functionInfo);
	}
	
	public String getUser() throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.getUser();
		return executer(functionInfo);
	}
	
	public String getCultureInfo(NetParameter[] netParameters) throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.getCultureInfo(netParameters);
		return executer(functionInfo);
	}
	
	@Override
	public String getReceiveList() throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.getReceiveList();
		return executer(functionInfo);
	}
	
	@Override
	public String getReceiveDetail(String poID) throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.getReceiveDetail(poID);
		return executer(functionInfo);
	}
	
	@Override
	public String commitReceiveItemDetail(NetParameter[] netParameters) throws Exception{
		HttpFunctionInfo functionInfo = HttpFunctionFactory.commitReceiveItemDetail(netParameters);
		return executer(functionInfo);
	}
	
	@Override
	public String undoReceiveItemDetail(String netParameters) throws Exception{
		HttpFunctionInfo functionInfo = HttpFunctionFactory.undoReceiveItemDetail(netParameters);
		return executer(functionInfo);
	}
	
	@Override
	public String getReceiveItemDetailWithBarcode(String barcode) throws Exception{
		HttpFunctionInfo functionInfo = HttpFunctionFactory.getReceiveItemDetailWithBarcode(barcode);
		return executer(functionInfo);
	}
	
	public static int AuthenticateURL(String url) throws AppolisException {		
		int code = 0;
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		try {
			HttpResponse response = httpclient.execute(httpget);
			code = response.getStatusLine().getStatusCode();			
		} catch (Exception ex) {
			code = 0;
			throw new AppolisNetworkException(ex);
		}
		return code;
	}

	@Override
	public String getProfileVersion() throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.getProfileVersion();
		return executer(functionInfo);
	}
	
	@Override
	public String updateProfilePin(NetParameter[] netParameters) throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.updateProfilePin(netParameters);
		return executer(functionInfo);
	}
	
	@Override
	public String getCycleAdjustment(NetParameter[] netParameters) throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.getCycleAdjustment(netParameters);
		return executer(functionInfo);
	}
	
	@Override
	public String getCycleCount() throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.getCycleCountList();
		return executer(functionInfo);
	}

	@Override
	public String getCycleCountCurrentList(NetParameter[] parameters) throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.getCycleCountList(parameters);
		return executer(functionInfo);	
	}
	
	public String getBarcode(NetParameter[] netParameters) throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.getBarcode(netParameters);
		return executer(functionInfo);
	}

	@Override
	public String getCycleQuanity(NetParameter[] netParameters)	throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.getCycleQuanity(netParameters);
		return executer(functionInfo);
	}
	

	@Override
	public String updateCycleCountItem(NetParameter[] netParameters) throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.updateCycleCountItem(netParameters);
		return executer(functionInfo);
	}
	

	@Override
	public String getItemBarcode(NetParameter[] netParameters) throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.getItemBarcode(netParameters);
		return executer(functionInfo);
	}
	
	@Override
	public String getUOMItemNumber(NetParameter[] netParameters) throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.getUOMItemNumber(netParameters);
		return executer(functionInfo);
	}
	
	@Override
	public String getBins(NetParameter[] netParameters) throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.getBins(netParameters);
		return executer(functionInfo);
	}
	
	@Override
	public String postItem(NetParameter[] netParameters) throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.postItem(netParameters);
		return executer(functionInfo);
	}
	
	@Override
	public String postItem(String netParameters) throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.postItem(netParameters);
		return executer(functionInfo);
	}
	
	@Override
	public String getLPNumber(NetParameter[] netParameters) throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.getLPNumber(netParameters);
		return executer(functionInfo);
	}
	
	@Override
	public String setCompletedBins(NetParameter[] netParameters) throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.setCompletedBins(netParameters);
		return executer(functionInfo);
	}

	@Override
	public String commitReceiveDetail(int purChaseId) throws Exception{
		HttpFunctionInfo functionInfo = HttpFunctionFactory.commitReceiveDetail(purChaseId);
		return executer(functionInfo);
	}
	
	@Override
	public String commitReceiveOptionDamage(NetParameter[] netParameters) throws Exception{
		HttpFunctionInfo functionInfo = HttpFunctionFactory.commitReceiveOptionDamage(netParameters);
		return executer(functionInfo);
	}
	
	@Override
	public String submitReceiveInventory(NetParameter[] netParameters) throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.submitReceiveInventory(netParameters);
		return executer(functionInfo);
	}
	@Override
	public String getLpByBarcode(NetParameter[] netParameters) throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.getLpByBarcode(netParameters);
		return executer(functionInfo);
	}

	@Override
	public String getItemByBarcode(NetParameter[] netParameters) throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.getItemByBarcode(netParameters);
		return executer(functionInfo);
	}

	@Override
	public String getDataBinOrLpOption(NetParameter[] netParameters) throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.getDataBinOrLpOption(netParameters);
		return executer(functionInfo);
	}

	@Override
	public String getPutAway() throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.getPutAway();
		return executer(functionInfo);
	}
	
	@Override
	public String getPutAway(NetParameter[] netParameters) throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.getPutAway(netParameters);
		return executer(functionInfo);
	}	
	
	@Override
	public String creareLpOption(NetParameter[] netParameters) throws Exception {
		HttpFunctionInfo functionInfo = HttpFunctionFactory.creareLpOption(netParameters);
		return executer(functionInfo);
	}

	@Override
	public String createNewLisecePlate(NetParameter[] netParameters) throws Exception{
		HttpFunctionInfo functionInfo = HttpFunctionFactory.createNewLisecePlate(netParameters);
		return executer(functionInfo);
	}

	@Override
	public String putAcquireBarcode(NetParameter[] netParameters) throws Exception{
		HttpFunctionInfo functionInfo = HttpFunctionFactory.putAcquireBarcode(netParameters);
		return executer(functionInfo);
	}
}
