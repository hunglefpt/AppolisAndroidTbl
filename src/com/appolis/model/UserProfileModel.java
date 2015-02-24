/**
 * Name: $RCSfile: UserProfileModel.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.model;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author hoangnh11
 * Entity of User profile
 */
public class UserProfileModel {
	private TextView tvScreenHeader;
	private TextView tvScreenTitle;
	private TextView tvScreenTitleExplane;
	private TextView tvVersion;
	private TextView tvProfileCurrentPass;
	private TextView tvProfileNewPass;
	private TextView tvProfileReNewPass;
	private EditText edtProfileCurrentPass;
	private EditText edtProfileNewPass;
	private EditText edtProfileReNewPass;
	private Button btnCofirm;
	private Button btnCancel;
	
	public TextView getTvScreenHeader() {
		return tvScreenHeader;
	}
	
	public void setTvScreenHeader(TextView tvScreenHeader) {
		this.tvScreenHeader = tvScreenHeader;
	}
	
	public TextView getTvScreenTitle() {
		return tvScreenTitle;
	}
	
	public void setTvScreenTitle(TextView tvScreenTitle) {
		this.tvScreenTitle = tvScreenTitle;
	}
	
	public TextView getTvScreenTitleExplane() {
		return tvScreenTitleExplane;
	}
	
	public void setTvScreenTitleExplane(TextView tvScreenTitleExplane) {
		this.tvScreenTitleExplane = tvScreenTitleExplane;
	}
	public TextView getTvVersion() {
		return tvVersion;
	}
	
	public void setTvVersion(TextView tvVersion) {
		this.tvVersion = tvVersion;
	}
	
	public TextView getTvProfileCurrentPass() {
		return tvProfileCurrentPass;
	}
	
	public void setTvProfileCurrentPass(TextView tvProfileCurrentPass) {
		this.tvProfileCurrentPass = tvProfileCurrentPass;
	}
	public TextView getTvProfileNewPass() {
		return tvProfileNewPass;
	}
	
	public void setTvProfileNewPass(TextView tvProfileNewPass) {
		this.tvProfileNewPass = tvProfileNewPass;
	}
	
	public TextView getTvProfileReNewPass() {
		return tvProfileReNewPass;
	}
	
	public void setTvProfileReNewPass(TextView tvProfileReNewPass) {
		this.tvProfileReNewPass = tvProfileReNewPass;
	}
	
	public EditText getEdtProfileCurrentPass() {
		return edtProfileCurrentPass;
	}
	
	public void setEdtProfileCurrentPass(EditText edtProfileCurrentPass) {
		this.edtProfileCurrentPass = edtProfileCurrentPass;
	}
	
	public EditText getEdtProfileNewPass() {
		return edtProfileNewPass;
	}
	
	public void setEdtProfileNewPass(EditText edtProfileNewPass) {
		this.edtProfileNewPass = edtProfileNewPass;
	}
	
	public EditText getEdtProfileReNewPass() {
		return edtProfileReNewPass;
	}
	
	public void setEdtProfileReNewPass(EditText edtProfileReNewPass) {
		this.edtProfileReNewPass = edtProfileReNewPass;
	}
	
	public Button getBtnCofirm() {
		return btnCofirm;
	}
	
	public void setBtnCofirm(Button btnCofirm) {
		this.btnCofirm = btnCofirm;
	}
	
	public Button getBtnCancel() {
		return btnCancel;
	}
	
	public void setBtnCancel(Button btnCancel) {
		this.btnCancel = btnCancel;
	}
}
