/**
 * Name: $RCSfile: ItemReceiveInventoryDetails.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.receiverinventory;

import android.content.Context;

import com.appolis.androidtablet.R;
import com.appolis.utilities.BaseLinearLayout;

/**
 * @author hoangnh11
 */
public class ItemReceiveInventoryDetails extends BaseLinearLayout{
	private int _position;

	/**
	 * @param context
	 */
	public ItemReceiveInventoryDetails(Context context) {
		super(context);
		initControl(R.layout.item_listview_po_details, context);
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
