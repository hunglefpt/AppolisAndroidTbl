/**
 * Name: $RCSfile: CommontDialog.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appolis.androidtablet.R;

/**
 * common class for creating & using dialogs
 * @author HoangNH11
 */
public class CommontDialog
{
	/**
	 * create dialog with layout content designed in XML. can used such as a common method.
	 * This dialog will full screen
	 * 
	 * @param context
	 * @param idLayout
	 * @return
	 */
	public static Dialog createDialogFullScreen(Context context, int idLayout)
	{
		Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(idLayout);
		return dialog;
	}

	/**
	 * create dialog with layout content designed XML
	 * this dialog will show in center screen
	 * @param context
	 * @param title
	 * @param idLayout
	 * @return
	 */
	public static Dialog createDialogCenter(Context context, String title, int idLayout)
	{
		final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.common_layout_dialog_center);
		View outOfDialog = dialog.findViewById(R.id.commonDialogBackground);
		outOfDialog.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
			}
		});
		TextView titleDialog = (TextView) dialog.findViewById(R.id.commonDialogTitle);
		titleDialog.setText(title);
		View contentDialog = dialog.getLayoutInflater().inflate(idLayout, null);
		LinearLayout bodyDialog = (LinearLayout) dialog.findViewById(R.id.commonDialogBody);
		bodyDialog.addView(contentDialog);
		return dialog;
	}
	
	/**
	 * create dialog with layout content designed XML
	 * this dialog will show in center screen
	 * @param context
	 * @param title
	 * @param idLayout
	 * @return
	 */
	public static Dialog createDialogNoTitleCenter(Context context, int idLayout)
	{
		final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.common_layout_dialog_no_title_center);
		View outOfDialog = dialog.findViewById(R.id.commonDialogBackground);
		outOfDialog.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
			}
		});
		View contentDialog = dialog.getLayoutInflater().inflate(idLayout, null);
		LinearLayout bodyDialog = (LinearLayout) dialog.findViewById(R.id.commonDialogBody);
		bodyDialog.addView(contentDialog);
		return dialog;
	}
	
	/**
	 * create dialog with layout content designed XML
	 * this dialog will show in center screen
	 * @param context
	 * @param title
	 * @param idLayout
	 * @return
	 */
	public static Dialog createDialogCenterStyle1(Context context, String title, View contentDialog)
	{
		final Dialog dialog = new Dialog(context, android.R.style.Theme_Dialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.common_layout_dialog_center);
		View outOfDialog = dialog.findViewById(R.id.commonDialogBackground);
		outOfDialog.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
			}
		});
		TextView titleDialog = (TextView) dialog.findViewById(R.id.commonDialogTitle);
		titleDialog.setText(title);
		LinearLayout bodyDialog = (LinearLayout) dialog.findViewById(R.id.commonDialogBody);
		bodyDialog.addView(contentDialog);
		return dialog;
	}
	
	/**
	 * create dialog with layout content designed XML
	 * this dialog will show in center screen
	 * @param context
	 * @param title
	 * @param idLayout
	 * @return
	 */
	public static Dialog createDialogCenter(Context context, String title, View contentDialog)
	{
		final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.common_layout_dialog_center);
		View outOfDialog = dialog.findViewById(R.id.commonDialogBackground);
		outOfDialog.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
			}
		});
		TextView titleDialog = (TextView) dialog.findViewById(R.id.commonDialogTitle);
		titleDialog.setText(title);
		LinearLayout bodyDialog = (LinearLayout) dialog.findViewById(R.id.commonDialogBody);
		bodyDialog.addView(contentDialog);
		return dialog;
	}
	
	/**
	 * showErrorDialog
	 */
	@SuppressWarnings("deprecation")
	public static void showErrorDialog(Context context, String message, String title){
		if(StringUtils.isBlank(title)){
			title = context.getString(R.string.COMMON_ERROR);
		}
		
		TextView mTextView = new TextView(context);
		mTextView.setLayoutParams(new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT));
		mTextView.setGravity(Gravity.CENTER);
		mTextView.setTextSize(16);
		mTextView.setPadding(10, 20, 10, 30);
		mTextView.setTextColor(context.getResources().getColor(R.color.white));
		mTextView.setText(message);
		
		Dialog mDialog = createDialogCenterStyle1(context, title, mTextView);
		mDialog.show();
	}
	
	/**
	 * show dialog with 2 button (OK and Cancel)
	 * @param title dialog title. Can be resource ID or String
	 * @param content dialog content. Can be resource ID or String
	 * @param okClickListener callback when click button OK
	 * @param cancelClickListener callback when click button Cancel
	 */
	public static void showConfirmAlerDialog(Context context, Object title, Object content, Object btnOkText, Object btnCancelText,
			android.content.DialogInterface.OnClickListener okClickListener,
			android.content.DialogInterface.OnClickListener cancelClickListener) {		
		String messageTitle;
		String mesageContent;
        String txtOkContent;
        String txtCancelContent;
		if (title instanceof Integer) {
			messageTitle = context.getString((Integer) title);
		} else {
			messageTitle = (String) title;
		}

		if (content instanceof Integer) {
			mesageContent = context.getString((Integer) content);
		} else {
			mesageContent = (String) content;
		}

        if (btnOkText instanceof Integer) {
            txtOkContent = context.getString((Integer) btnOkText);
        } else {
            txtOkContent = (String) btnOkText;
        }

        if (btnCancelText instanceof Integer) {
            txtCancelContent = context.getString((Integer) btnCancelText);
        } else {
            txtCancelContent = (String) btnCancelText;
        }

		Dialog commontDialog = createConfirmAlerDialog(context, messageTitle, mesageContent, txtOkContent, txtCancelContent, okClickListener,
				cancelClickListener);
		commontDialog.setCanceledOnTouchOutside(false);
	}

	/**
	 * show dialog with 2 button (OK and Cancel)
	 * @param title dialog title. Can be resource ID or String
	 * @param content dialog content. Can be resource ID or String
	 * @param okClickListener callback when click button OK
	 * @param cancelClickListener callback when click button Cancel
	 * @param onDissmissListener callback when dialog dismiss
	 * @param textContentIsBold true if bold text content
	 */
	public static void showConfirmAlerDialog(Context context, Object title, Object content,
			android.content.DialogInterface.OnClickListener okClickListener,
			android.content.DialogInterface.OnClickListener cancelClickListener,
			final DialogInterface.OnDismissListener onDissmissListener, boolean textContentIsBold) {
		
		String messageTitle;
		String mesageContent;
		if (title instanceof Integer) {
			messageTitle = context.getString((Integer) title);
		} else {
			messageTitle = (String) title;
		}

		if (content instanceof Integer) {
			mesageContent = context.getString((Integer) content);
		} else {
			mesageContent = (String) content;
		}

		Dialog commontDialog = createConfirmAlerDialog(context, messageTitle, mesageContent, okClickListener,
				cancelClickListener, textContentIsBold);
		commontDialog.setCanceledOnTouchOutside(false);
	}
	
	/**
	 * create confirm dialog message with OK and Cancel buttons
	 * 
	 * @param context
	 * @param title
	 * @param meg
	 * @param okListener
	 * @param cancelListener
	 * @return
	 */
	public static Dialog createConfirmAlerDialog(Context context, String title,
			String meg, DialogInterface.OnClickListener onOkClickListener,
			DialogInterface.OnClickListener onCancelClickListener) {
		return createConfirmAlerDialog(context, title, meg, context
				.getResources().getString(R.string.COMMON_OK), context
				.getResources().getString(R.string.COMMON_CANCEL),
				onOkClickListener, onCancelClickListener);
	}

	/**
	 * create confirm dialog message with OK and Cancel buttons
	 * 
	 * @param context
	 * @param title
	 * @param meg
	 * @param okListener
	 * @param cancelListener
	 * @return
	 */
	public static Dialog createConfirmAlerDialog(Context context, String title,
			String meg, String okText, String cancelText,
			DialogInterface.OnClickListener onOkClickListener,
			DialogInterface.OnClickListener onCancelClickListener) {
		AlertDialog.Builder alBuild = getCommonAlertDialogBuilder(context,title, meg);
		alBuild.setPositiveButton(okText, onOkClickListener);
		alBuild.setNegativeButton(cancelText, onCancelClickListener);
		AlertDialog alertDialog = alBuild.create();
		alertDialog.show();
		
		TextView messageView = (TextView)alertDialog.findViewById(android.R.id.message);
		messageView.setPadding(15, 50, 15, 50);
		messageView.setGravity(Gravity.CENTER);
		
		return alertDialog;
	}

	/**
	 * create confirm dialog message with OK and Cancel buttons
	 * 
	 * @param context
	 * @param title
	 * @param meg
	 * @param okListener
	 * @param cancelListener
	 * @return
	 */
	public static Dialog createConfirmAlerDialog(Context context, String title,
			String meg, DialogInterface.OnClickListener onOkClickListener,
			DialogInterface.OnClickListener onCancelClickListener,
			boolean textContentIsBold) {
		
		return createConfirmAlerDialog(context, title, meg, onOkClickListener,
				onCancelClickListener);
	}
	
	public static AlertDialog.Builder getCommonAlertDialogBuilder(
			Context context, String title, String meg) {
		AlertDialog.Builder alBuild = new AlertDialog.Builder(context,R.style.appolisHoloDialog);
		if (title != null) {
			alBuild.setTitle(title);
		}

		if (meg != null) {
			alBuild.setMessage(meg);
		}

		return alBuild;
	}
	
}
