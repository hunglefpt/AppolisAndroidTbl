/**
 * Name: ItemPutAwayBin.java
 * Date: Jan 28, 2015 4:41:16 PM
 * Copyright (c) 2014 FPT Software, Inc. All rights reserved.
 */
package com.appolis.putaway;

import android.content.Context;

import com.appolis.androidtablet.R;
import com.appolis.utilities.BaseLinearLayout;

/**
 * @author hoangnh11
 */
public class ItemPutAwayBin extends BaseLinearLayout{
	private int _position;

	/**
	 * @param context
	 */
	public ItemPutAwayBin(Context context) {
		super(context);
		initControl(R.layout.item_list_put_away_bin, context);
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
}
