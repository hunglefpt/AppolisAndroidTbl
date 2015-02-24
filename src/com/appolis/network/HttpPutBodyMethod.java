/*
 * Name: $RCSfile: HttpPutBodyMethod.java,v $
 * Version: $Revision: 1.4 $
 * Date: $Date: 2011/09/20 11:55:16 $
 *
 */
package com.appolis.network;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPut;

/**
 * @author HoangNH11
 * Make method for PUT protocol
 */
public class HttpPutBodyMethod extends HttpConnection
{
    /**
     * @param url
     */
    public HttpPutBodyMethod(String url)
    {
        super(url);
    }

    /**
     * @see com.doximity.doximitydroid.android.core.network.HttpConnection#doRequest()
     */
    @Override
    protected void doRequest() throws Exception
    {
        String url = this._urlString;
        HttpPut httpRequest = new HttpPut(url);
        
        if (_command != null)
        {
            try
            {
                httpRequest = (HttpPut) _command.buildHeader(httpRequest);
                httpRequest = _command.buildPutCommand(httpRequest);
                httpRequest = _command.buildPutBody(httpRequest);
            }
            catch (UnsupportedEncodingException ex)
            {
                throw new UnsupportedEncodingException();
            }
        }
        if (httpRequest != null)
        {
            getData(httpRequest);
        }
    }

    /**
     * @see com.doximity.doximitydroid.android.core.network.HttpConnection#doRequest(java.lang.String)
     */
    @Override
    protected void doRequest(String encodeType) throws Exception
    {
    }
}
