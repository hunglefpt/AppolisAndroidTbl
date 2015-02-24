/**
 * Name: $RCSfile: ObjectBinList.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.entities;

import java.io.Serializable;
import java.util.List;

public class ObjectBinList implements Serializable {
	List<ObjectCountCycleCurrent> binList;

	public List<ObjectCountCycleCurrent> getBinList() {
		return binList;
	}

	public void setBinList(List<ObjectCountCycleCurrent> binList) {
		this.binList = binList;
	}
	
}
