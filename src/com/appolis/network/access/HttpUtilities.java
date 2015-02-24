/*
 * Name: $RCSfile: HttpUtilities.java,v $
 * Version: $Revision: 1.24 $
 * Date: $Date: 2013/03/26 11:58:54 $
 *
 */
package com.appolis.network.access;

import com.appolis.login.LoginActivity;
import com.appolis.network.NetParameter;

/**
 * @author HoangNH11
 */
public final class HttpUtilities
{
    public static NetParameter getHeaderContentJSon()
    {
        return new NetParameter("Content-Type",
            "application/json; charset=utf-8");
    }
    
    public static NetParameter getHeaderAuthorization()
    {
        return new NetParameter("Authorization", LoginActivity.valueAuthorization);
    }
}
