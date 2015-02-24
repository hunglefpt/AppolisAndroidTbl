/*
 * Name: $RCSfile: HttpCommand.java,v $
 * Version: $Revision: 1.25 $
 * Date: $Date: 2011/08/24 03:21:35 $
 *
 */
package com.appolis.network;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.util.Log;

import com.appolis.utilities.Logger;
import com.appolis.utilities.StringUtils;

/**
 * @author HoangNH11
 */
public class HttpCommand extends Command
{
    protected NetParameter[] _arrParams;
    protected NetParameter[] _header;
    public StringEntity _body;

    public HttpCommand()
    {

    }
    
    public void setBody(String body)
    {	
        if(StringUtils.isNotBlank(body))
        {
            try
            {
                _body = new StringEntity(body, HTTP.UTF_8);
            }
            catch (UnsupportedEncodingException e)
            {
                Logger.error("Error when set body for http methods see detail below (func:setBody class: HttpCommand)");
                Logger.error(e);
            }
        }
    }
   

    public void setHeader(NetParameter[] arrHeader)
    {
        _header = arrHeader;
    }

    public void setParam(NetParameter[] arrParam)
    {
        _arrParams = arrParam;
    }

    public NetParameter[] getParams()
    {
        return _arrParams;
    }

    public HttpRequestBase buildHeader(HttpRequestBase httpRequest)
    {
        if (_header == null || _header.length == 0)
            return httpRequest;
        int len = _header.length;
        for (int i = 0; i < len; i++)
        {
            httpRequest.setHeader(_header[i].getName(), _header[i].getValue());
        }
        return httpRequest;
    }

    private String buildStringParam()
    {
        if (_arrParams == null)
            return "";
        
        StringBuilder strBuffer = new StringBuilder();
        int len = this._arrParams.length;
        for (int i = 0; i < len; i++)
        {
            if (StringUtils.isBlank(_arrParams[i].getValue()) == false)
            {
                strBuffer.append("&");
                strBuffer.append(_arrParams[i].getName());
                strBuffer.append("=");
                try
                {
                    strBuffer.append(URLEncoder.encode(
                        _arrParams[i].getValue(), HTTP.UTF_8));
                }
                catch (UnsupportedEncodingException ex)
                {
                    Logger.error("Error when build String param for http get methods see detail below (func:buildStringParam class: HttpCommand)");
                    Logger.error(ex);
                }
            }
        }

        return strBuffer.toString();
    }

    public String buildGetCommandUrl(String url)
    {
        String path = url;
        path = path + buildStringParam();

        return path;
    }

    private List<NameValuePair> buildEntityParam()
    {
        if (_arrParams == null || _arrParams.length == 0)
            return null;
        int len = _arrParams.length;
        List<NameValuePair> listParam = new ArrayList<NameValuePair>(len);
        for (int i = 0; i < len; i++)
        {
            listParam.add(new BasicNameValuePair(_arrParams[i].getName(),
                _arrParams[i].getValue()));
        }
        return listParam;
    }

    public HttpPost buildBody(HttpPost httpPost)
    {
        if(_body != null && _body.getContentLength()>0)
            httpPost.setEntity(_body);
        return httpPost;
    }
    
    public HttpDeleteWithBody buildDeleteBody(HttpDeleteWithBody httpDeleteWithBody)
    {
    	if(_body != null && _body.getContentLength()>0)
    		httpDeleteWithBody.setEntity(_body);
    	return httpDeleteWithBody;
    }
    
    public HttpPost buildPostCommand(HttpPost httpPost)
            throws UnsupportedEncodingException
    {
        List<NameValuePair> pairs = this.buildEntityParam();
        if (pairs != null)
        {
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
        }
        return httpPost;
    }
    
    public HttpDeleteWithBody buildDeleteCommand(HttpDeleteWithBody httpDelete)
    		throws UnsupportedEncodingException 
    {
		List<NameValuePair> pairs = this.buildEntityParam();
		if (pairs != null) {
			httpDelete.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
		}
		return httpDelete;
	}
    
    /**
     * Build all parameters which are send to on URL
     * 
     * @param httpPost HttpPost object
     * @param encodeType The encode type of url
     * 
     * @return HttpPost object
     * 
     * @throws UnsupportedEncodingException
     */
    public HttpPost buildPostCommand(HttpPost httpPost, String encodeType)
            throws UnsupportedEncodingException
    {
        List<NameValuePair> pairs = this.buildEntityParam();
        if (pairs != null)
        {
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, encodeType));
        }
        return httpPost;
    }
        
    public HttpPut buildPutBody(HttpPut httpPut)
    {
        if(_body != null && _body.getContentLength() > 0)
        {
            httpPut.setEntity(_body);
        }
        return httpPut;
    }
    
    public HttpPut buildPutCommand(HttpPut httpPut)
            throws UnsupportedEncodingException
    {
        List<NameValuePair> pairs = this.buildEntityParam();
        if (pairs != null)
        {
            httpPut.setEntity(new UrlEncodedFormEntity(pairs));
        }
        return httpPut;
    }
}
