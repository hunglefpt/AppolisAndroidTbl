/*
 * Name: $RCSfile: HttpGetMethod.java,v $
 * Version: $Revision: 1.18 $
 * Date: $Date: 2012/10/04 09:42:34 $
 *
 */
package com.appolis.network;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import android.util.Log;

/**
 * @author HoangNH11
 * Make method for GET protocol
 */
public class HttpGetMethod extends HttpConnection
{
    public HttpGetMethod(String url)
    {
        super(url);
    }

    @Override
    protected void doRequest() throws Exception
    {
        HttpRequestBase httpRequest = null;
        String url = this._urlString;
        Log.e("GET - doRequest", "url = " + url);
        if (_command != null)
        {
            url = _command.buildGetCommandUrl(url);
            try
            {
                URI uir = new URI(url);
                httpRequest = new HttpGet(uir);
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
    {}

}
