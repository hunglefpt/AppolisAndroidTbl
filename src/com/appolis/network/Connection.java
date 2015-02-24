/*
 * Name: $RCSfile: Connection.java,v $
 * Version: $Revision: 1.1 $
 * Date: $Date: 2010/11/29 07:51:35 $
 *
 */

package com.appolis.network;

/**
 * @author hoangnh11
 */
public abstract class Connection
{
    protected String _urlString;

    public Connection()
    {

    }

    public Connection(String url)
    {
        _urlString = url;
    }
}
