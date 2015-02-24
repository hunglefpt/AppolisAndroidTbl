/*
 * Name: $RCSfile: HttpPostMethod.java,v $
 * Version: $Revision: 1.9 $
 * Date: $Date: 2012/09/24 08:38:09 $
 *
 */
package com.appolis.network;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

import android.util.Log;

/**
 * @author HoangNH11
 * Make method for POST protocol
 */
public class HttpPostMethod extends HttpConnection
{
    public HttpPostMethod(String url)
    {
        super(url);
    }

    @Override
    protected void doRequest() throws Exception
    {
        HttpRequestBase httpRequest = null;
        String url = this._urlString;
        Log.e("POST - doRequest", "url = " + url);
        httpRequest = new HttpPost(url);
        
        if (_command != null)
        {
            try
            {
                httpRequest = _command.buildHeader(httpRequest);
                httpRequest = _command.buildPostCommand((HttpPost) httpRequest);

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
        HttpRequestBase httpRequest = null;
        String url = this._urlString;
        httpRequest = new HttpPost(url);
        
        if (_command != null)
        {
            try
            {
                httpRequest = _command.buildHeader(httpRequest);
                httpRequest = _command.buildPostCommand((HttpPost) httpRequest,
                    encodeType);
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
}
