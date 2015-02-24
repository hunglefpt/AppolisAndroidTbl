/*
 * Name: $RCSfile: HttpsMethod.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2011/09/20 11:55:16 $
 *
 */
package com.appolis.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import com.appolis.utilities.GlobalParams;

/**
 * @author HoangNH11
 */
public class HttpsMethod extends HttpConnection
{
    public HttpsMethod(String url)
    {
        super(url);
    }

    HttpsURLConnection setParam(HttpsURLConnection connection)
    {
        NetParameter[] arr = _command.getParams();
        // HoanLD, 11-Apr: Use Enhanced For Loop Syntax instead of for array. 
        for (NetParameter item : arr)
        {
            connection.setRequestProperty(item.getName(), item.getValue());
        }
        
        return connection;
    }

    protected void doRequest() throws Exception
    {
        TrustManager[] trustAllCerts;
        SSLContext sc = null;
        URL url;
        HttpsURLConnection connection;
        BufferedReader reader = null;
        StringBuilder str;
        String line;
        trustAllCerts = new TrustManager[] { new X509TrustManagerImpl() };

        try
        {
            sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            url = new URL(_urlString);

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            connection = (HttpsURLConnection) url.openConnection();
            connection.setHostnameVerifier(new HostnameVerifierImpl());

            // Make this URL connection available for input and output
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection = setParam(connection);

            reader = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
            str = new StringBuilder();

            while ((line = reader.readLine()) != null)
            {
                str.append(line + GlobalParams.NEW_LINE);
            }

            _response = str.toString();
        }
        catch (NoSuchAlgorithmException ex)
        {
            throw new NoSuchAlgorithmException(ex);
        }
        catch (KeyManagementException ex)
        {
            throw new KeyManagementException(ex);
        }
        catch (IOException ex)
        {
            throw new IOException();
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException ex)
                {
                    throw new IOException();
                }
            }
        }
    }

    static class X509TrustManagerImpl implements X509TrustManager
    {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException
        {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException
        {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers()
        {
            return null;
        }

    }

    static class HostnameVerifierImpl implements HostnameVerifier
    {
        @Override
        public boolean verify(String arg0, SSLSession arg1)
        {
            return true;
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
