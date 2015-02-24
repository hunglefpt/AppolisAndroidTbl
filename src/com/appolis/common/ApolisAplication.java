/**
 * Name: $RCSfile: ApolisAplication.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.common;

import android.app.Application;
import android.content.Context;

/**
 * 
 * @author hoangnh11
 */
public class ApolisAplication extends Application {
    private static Context context;
    private static ApolisAplication instance;
    
    public ApolisAplication() {
        instance = this;
    }
    
    public static ApolisAplication getInstance() {
    	if(null == instance){
    		new ApolisAplication();
    	}
        return instance;
    }
    
    @Override
	public void onCreate() {
    	super.onCreate();
        ApolisAplication.context = getApplicationContext();
    }
    
    public static Context getAppContext() {
        return ApolisAplication.context;
    }
}
