/**
 * Name: $RCSfile: AppolisException.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.common;

/**
 * Handling exceptions of the application.
 * 
 * @author hoangnh11
 */
public class AppolisException extends RuntimeException
{
    private static final long serialVersionUID = 7232999076749501603L;

    private int errorCode;

    /**
     * Default Constructor.
     */
    public AppolisException()
    {
        super();
    }

    /**
     * @param detailMessage
     * @param throwable
     */
    public AppolisException(String detailMessage, Throwable throwable)
    {
        super(detailMessage, throwable);
    }

    /**
     * @param detailMessage
     */
    public AppolisException(String detailMessage)
    {
        super(detailMessage);
    }

    /**
     * @param throwable
     */
    public AppolisException(Throwable throwable)
    {
        super(throwable);
    }

    /**
     * @param errorCode
     * @param detailMessage
     */
    public AppolisException(int errorCode, String detailMessage)
    {
        super(detailMessage);
        this.errorCode = errorCode;        
    }
      
    /**
     * @param errorCode
     * @param detailMessage
     * @param throwable
     */
    public AppolisException(int errorCode, String detailMessage, Throwable throwable)
    {
        super(detailMessage, throwable);
        this.errorCode = errorCode;        
    }
  
    /**
     * @param errorCode
     * @param throwable
     */
    public AppolisException(int errorCode, Throwable throwable)
    {
        super(throwable);
        this.errorCode = errorCode;        
    }    
    /**
     * @return the errorCode
     */
    public int getErrorCode()
    {
        return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }
}
