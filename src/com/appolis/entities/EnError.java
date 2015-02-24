/**
 * Name: EnError.java
 * Date: Feb 9, 2015 3:25:30 PM
 * Copyright (c) 2014 FPT Software, Inc. All rights reserved.
 */
package com.appolis.entities;

import java.io.Serializable;

/**
 * @author hoangnh11
 */
public class EnError implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7571280701318827201L;
	private String Message;
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return Message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		Message = message;
	}	
}
