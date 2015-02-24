/*
 * Name: $RCSfile: HttpFunctionInfo.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2013/04/01 02:09:59 $
 *
 */
package com.appolis.network.access;

import com.appolis.login.LoginActivity;
import com.appolis.network.NetParameter;


/**
 * @author HoangNH11
 */
public class HttpFunctionInfo
{
    private String url;
    private int methodType;
    private NetParameter[] header;
    private NetParameter[] params;
    private String functionName;
    private String body;

    /**
     * @return the url
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url)
    {
        this.url = url;
    }

    /**
     * @return the methodType
     */
    public int getMethodType()
    {
        return methodType;
    }

    /**
     * @param methodType the methodType to set
     */
    public void setMethodType(int methodType)
    {
        this.methodType = methodType;
    }

    /**
     * @return the header
     */
    public NetParameter[] getHeader()
    {
        return header;
    }

    /**
     * @param header the header to set
     */
    public void setHeader(NetParameter[] header)
    {
        // For User-Agent header
        NetParameter userAgentHeader = new NetParameter("Authorization", LoginActivity.valueAuthorization);
        NetParameter[] newHeader = new NetParameter[header.length +1];
        
        for ( int i=0; i< header.length; i++)
        {
            newHeader[i] = header[i];
        }
        // Add the user-agent String
        newHeader[header.length] = userAgentHeader;
        this.header = newHeader;
    	this.header = header;
    }

    /**
     * @return the params
     */
    public NetParameter[] getParams()
    {
        return params;
    }

    /**
     * @param params the params to set
     */
    public void setParams(NetParameter[] params)
    {
        this.params = params;
    }

    /**
     * @return the functionName
     */
    public String getFunctionName()
    {
        return functionName;
    }

    /**
     * @param functionName the functionName to set
     */
    public void setFunctionName(String functionName)
    {
        this.functionName = functionName;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body)
    {
        this.body = body;
    }

    /**
     * @return the body
     */
    public String getBody()
    {
        return body;
    }
}
