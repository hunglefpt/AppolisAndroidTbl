/**
 * Name: ItemPutAway.java
 * Date: Jan 26, 2015 3:08:34 PM
 * Copyright (c) 2014 FPT Software, Inc. All rights reserved.
 */
package com.appolis.putaway;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.appolis.androidtablet.R;
import com.appolis.utilities.BaseLinearLayout;

/**
 * @author hoangnh11
 */
public class ItemPutAway extends BaseLinearLayout{
	private int _position;
	private LinearLayout linItem;
	private OnClickListener _onItemClick;

	/**
	 * @param context
	 */
	public ItemPutAway(Context context) {
		super(context);
		initControl(R.layout.item_list_put_away, context);
		linItem = (LinearLayout) findViewById(R.id.linItem);
//		linItem.setOnClickListener(onItemClick);
	}

	/**
	 * @return the _position
	 */
	public int get_position() {
		return _position;
	}

	/**
	 * @param _position
	 *            the _position to set
	 */
	public void set_position(int _position) {
		this._position = _position;
	}
	
	OnClickListener onItemClick = new OnClickListener() 
	{

        @Override
        public void onClick(View v)
        {
            if (_onItemClick != null)
                _onItemClick.onClick(ItemPutAway.this);
        }
    };
    
    public void setOnThisItemClickHandler(OnClickListener itemClick)
    {
        _onItemClick = itemClick;
    }
}
