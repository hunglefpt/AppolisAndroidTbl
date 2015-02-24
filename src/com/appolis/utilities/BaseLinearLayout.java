/**
 * Name: $RCSfile: BaseLinearLayout.java,v $
 * Version: $Revision: 1.20 $
 * Date: $Date: 2013/03/19 02:48:48 $
 *
 * Copyright (C) 2013 FPT Software, Inc. All rights reserved.
 */
package com.appolis.utilities;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * 
 * @author HoangNH11
 * Instance Linear layout
 */
public class BaseLinearLayout extends LinearLayout{
	
	public BaseLinearLayout(Context context)
    {
        super(context);
    }

    public BaseLinearLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);

    }

    protected void initControl(int reID, Context context)
    {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(reID, this, true);
    }

    public Activity getActivity()
    {
        return (Activity)this.getContext();
    }
}
