package com.appolis.network;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpRequestBase;

import android.util.Log;

/**
 * @author HoangNH11
 */
public class HttpDeleteBodyMethod extends HttpConnection{

	public HttpDeleteBodyMethod(String url)
    {
        super(url);
    }
	
	@Override
	protected void doRequest() throws Exception {
		 String url = this._urlString;
		 Log.e("HttpDeleteBodyMethod - doRequest", "url = " + url);
		 HttpDeleteWithBody httpRequest = new HttpDeleteWithBody(url);
	        if (_command != null)
	        {
	            try
	            {
	                httpRequest = (HttpDeleteWithBody) _command.buildHeader((HttpRequestBase)httpRequest);
	                httpRequest = _command.buildDeleteCommand((HttpDeleteWithBody) httpRequest);
	                httpRequest = _command.buildDeleteBody(httpRequest);
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

	@Override
	protected void doRequest(String encodeType) throws Exception {
		// TODO Auto-generated method stub
		
	}

	
    
}
