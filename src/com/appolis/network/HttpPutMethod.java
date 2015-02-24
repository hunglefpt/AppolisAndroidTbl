/*
 * Name: $RCSfile: HttpPutMethod.java,v $
 * Version: $Revision: 1.7 $
 * Date: $Date: 2013/03/18 06:35:32 $
 *
 */
package com.appolis.network;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import android.util.Log;


/**
 * @author HoangNH11
 * Make method for PUT protocol
 */
public class HttpPutMethod extends HttpConnection
{
    public HttpPutMethod(String url)
    {
        super(url);
    }

    @Override
    protected void doRequest() throws Exception
    {
        HttpRequestBase httpRequest = null;

        String url = this._urlString;
        Log.e("HttpPutMethod", "URL Request = " + url);
        
        if (_command != null)
        {
            url = _command.buildGetCommandUrl(url);
            try
            {
                URI uir = new URI(url);
                httpRequest = new HttpPut(uir);
                httpRequest = _command.buildHeader(httpRequest);
            }
            catch (URISyntaxException ex)
            {
                throw new URISyntaxException(ex.getInput(),ex.getReason());
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
