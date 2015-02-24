/**
 * Name: $RCSfile: AppolisNetworkException.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.common;

import java.net.SocketException;

/**
 * Handling exception occurs by network.
 * 
 * @author hoangnh11
 */
public class AppolisNetworkException extends AppolisException
{
    private static final long serialVersionUID = 8746521335524991375L;

    /**
     * Default Constructor.
     */
    public AppolisNetworkException()
    {
        super();
    }

    /**
     * @param detailMessage
     * @param throwable
     */
    public AppolisNetworkException(String detailMessage, Throwable throwable)
    {
        super(detailMessage, throwable);
    }

    /**
     * @param detailMessage
     */
    public AppolisNetworkException(String detailMessage)
    {
        super(detailMessage);
    }

    /**
     * @param throwable
     */
    public AppolisNetworkException(Throwable throwable)
    {
        super(throwable);
    }
    
    /**
     * @param throwable
     */
    public AppolisNetworkException(SocketException ex)
    {
        super(ex.getMessage());
    }
}
