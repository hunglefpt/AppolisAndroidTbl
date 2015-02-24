/*
 * Name: $RCSfile: HttpConnection.java,v $
 * Version: $Revision: 1.54 $
 * Date: $Date: 2012/11/28 03:08:22 $
 *
 */

package com.appolis.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.util.Log;
import android.view.WindowManager.BadTokenException;

import com.appolis.utilities.Logger;

/**
 * @author HoangNH11
 */
public abstract class HttpConnection extends Connection  
{
    /**
     * The downloading image status
     */
    private static final int DOWNLOADING_STATUS = 0;
    
    /**
     * The complete downloading image status
     */
    private static final int COMPLETE_DOWNLOADING_IMAGE_STATUS = 1;
    
    public static final int HTTP_CONNECTION_TIME_OUT = 20000;
    public static final int HTTP_OK = 0;
    public static final int HTTP_CANOT_CONNECTED = 1;
    public static final int HTTP_PAGE_NOTFOUND = 3;

    public static final int HTTP_METHOD_GET = 0;
    public static final int HTTP_METHOD_POST = 1;
    public static final int HTTPS_METHOD = 2;
    public static final int HTTP_METHOD_PUT = 3;
    public static final int HTTP_METHOD_DELETE = 4;
    public static final int HTTP_METHOD_DELETE_BODY = 7;
    public static final int HTTP_METHOD_POST_BODY = 5;
    public static final int HTTP_METHOD_PUT_BODY = 6;

    // ================================================================
    // private members
    protected int _methodTypeID;
    protected String _response, _statusCode;
    protected int statusCode;
    protected boolean _isAsyn;
    protected HttpCommand _command;

    // ================================================================
    // constructor function

    public HttpConnection(String url)
    {
        super(url);
    }

    /**
     * @param command
     * @return String
     * @throws Exception
     */
    public String makeRequest(HttpCommand command) throws Exception
    {
        this._command = command;
        doRequest();
        return _response;
    }
    
    /**
     * Execute http command using for Share and un_share my private profile
     * 
     * @param command
     * @return Status code
     * @throws Exception
     */
    public String makeRequestStatusCode(HttpCommand command) throws Exception
    {
        this._command = command;
        doRequest();
        return _statusCode;
    }
    
    /**
     * Execute http command with all parameters which are formatted 
     * by encode type, ex: UTF-8, ASCII
     * 
     * @param command The HttpCommand object
     * @param encodeType The encode type of url
     * 
     * @return The status code
     * 
     * @throws Exception
     */
    public String makeRequestStatusCode(HttpCommand command, String encodeType) throws Exception
    {
        this._command = command;
        doRequest(encodeType);
        return _statusCode;
    }
   
    /**
     * Execute http command with all parameters which are formatted not encode type
     * 
     * @param command The HttpCommand object
     * @return The status code
     * @throws Exception
     */
    public String makeRequestStatusCodes(HttpCommand command) throws Exception
    {
        this._command = command;
        doRequest();
        return _statusCode;
    }
 
    protected abstract void doRequest() throws Exception;
    
    /**
     * Implement two processes which they are send request to web server
     * and receive data that is returned from web service
     * 
     * @param encodeType The encode type of url
     * 
     * @throws Exception
     */
    protected abstract void doRequest(String encodeType) throws Exception;
    
    protected void getData(HttpRequestBase httpRequest) throws Exception
    {
        HttpResponse httpResponse = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
            HTTP_CONNECTION_TIME_OUT);
        StringBuilder strBuffer = new StringBuilder("");
        InputStream inputStream = null;
        
        try
        {
            httpResponse = httpClient.execute(httpRequest);
            
            if (httpResponse != null && httpResponse.getEntity() != null)
            {
                inputStream = httpResponse.getEntity().getContent();
                int len = (int) httpResponse.getEntity().getContentLength();
                byte[] data = new byte[1024];
                len = 0;
                while (-1 != (len = inputStream.read(data)))
                {
                    strBuffer.append(new String(data, 0, len));
                }
                
                // Get status code from response header
                statusCode = httpResponse.getStatusLine().getStatusCode();
                _statusCode = Integer.toString(httpResponse.getStatusLine().getStatusCode());
                Log.e("HttpConnection", "status Code = " + statusCode + ", _statusCode = " + _statusCode);
            }

            _response = strBuffer.toString();
        }
        catch (ClientProtocolException ex)
        {
            Log.e("HttpConnection", "Internet connection issue - ClientProtocolException");
            throw new ClientProtocolException(ex);
        }
        catch (UnknownHostException ex)
        {
            Log.e("HttpConnection", "Internet connection issue - UnknownHostException");
            throw new UnknownHostException();
        }
        catch (SocketTimeoutException ex)
        {
            Log.e("HttpConnection", "Internet connection issue - SocketTimeoutException");
            throw new SocketTimeoutException();
        }
        catch (SocketException ex)
        {
            Log.e("HttpConnection", "Internet connection issue - SocketException");
            throw new SocketException();
        }
        catch (ConnectTimeoutException ex)
        {
            Log.e("HttpConnection", "Internet connection issue - ConnectTimeoutException");
            throw new ConnectTimeoutException();
        }
        catch (MalformedURLException mue)
        {
            Log.e("HttpConnection", "Internet connection issue - MalformedURLException");
            throw new MalformedURLException();
        }
        catch (SSLException se)
        {
            Log.e("HttpConnection", "Internet connection issue - SSLException");
            throw new SSLException(se);
        }
        catch (BadTokenException be)
        {
            Log.e("HttpConnection", "Internet connection issue - BadTokenException");
            throw new BadTokenException();
        }
        catch (IOException ex)
        {
            Log.e("HttpConnection", "Internet connection issue - IOException");
            throw new IOException();
        }
        catch (OutOfMemoryError ex) 
        {
            Log.e("HttpConnection", "Internet connection issue - OutOfMemoryError");
            throw new OutOfMemoryError();
        }
        finally
        {
            try
            {
                if (inputStream != null)
                {
                    inputStream.close();
                }
            }
            catch (IOException ex)
            {
                Logger.warn("Unable to close the input stream...");
                Logger.warn("HttpNetServices.getData():" + ex);
            }
        }
    }
     
}
