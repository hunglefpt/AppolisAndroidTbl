/**
 * Name: $RCSfile: Utilities.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appolis.androidtablet.R;
import com.appolis.common.LanguagePreferences;
import com.appolis.interfaceapp.KeyboardVisibilityListener;

/**
 * @author hoangnh11
 * Create common utilities for whole project classes
 */
public class Utilities {	
	
	/* start activity and transfer a array param to next activity */
	public static void startActivityWithParam(Context mContext,
			Class<?> newClass, String[] param, String[] values) {
		Intent intentNew = new Intent(mContext, newClass);
		for (int i = 0; i < param.length; i++) {
			intentNew.putExtra(param[i], values[i]);
		}
		mContext.startActivity(intentNew);
	}

	public static void startActivityForResultWithParam(Activity mContext,
			Class<?> newClass, int requestCode, String[] param, String[] values) {
		Intent intentNew = new Intent(mContext, newClass);
		for (int i = 0; i < param.length; i++) {
			intentNew.putExtra(param[i], values[i]);
		}
		mContext.startActivityForResult(intentNew, requestCode);
	}

	public static boolean isConnected(Context mContext) {
		boolean connected = false;

		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (cm != null) {
			NetworkInfo[] netInfo = cm.getAllNetworkInfo();

			for (NetworkInfo ni : netInfo) {
				if ((ni.getTypeName().equalsIgnoreCase("WIFI") || ni
						.getTypeName().equalsIgnoreCase("MOBILE"))
						&& ni.isConnected() && ni.isAvailable()) {
					connected = true;
				}

			}
		}

		return connected;
	}

	@SuppressWarnings("deprecation")
	public static void closeApp(final Activity mActivity, final String strMessages, final int tyle,	String yes, String no) {
		final AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
		alertDialog.setTitle(GlobalParams.LOG_OUT);
		alertDialog.setMessage(strMessages);		
		alertDialog.setButton(no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						alertDialog.dismiss();

					}
				});		
		alertDialog.setButton2(yes,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch (tyle) {
						case GlobalParams.EXIT_APP:
							mActivity.setResult(mActivity.RESULT_FIRST_USER, new Intent());
							mActivity.finish();
							break;
						case GlobalParams.LOGOUT_APP:
							mActivity.setResult(mActivity.RESULT_CANCELED, new Intent());
							mActivity.finish();
							break;
						default:
							break;
						}
					}
				});
		alertDialog.show();
	}

	public static void showPopUp(final Context mContext,
			final Class<?> newClass, final String strMessages) {
		String message;
		if (strMessages.equals(GlobalParams.BLANK)) {
			message = GlobalParams.WRONG_USER;
		} else {
			message = strMessages;
		}
		
		final Dialog dialog = new Dialog(mContext, R.style.Dialog_NoTitle);
		dialog.setContentView(R.layout.dialogwarning);
		// set the custom dialog components - text, image and button		
		TextView text2 = (TextView) dialog.findViewById(R.id.tvScantitle2);		
		text2.setText(message);
		
		LanguagePreferences langPref = new LanguagePreferences(mContext);
		Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
		dialogButtonOk.setText(langPref.getPreferencesString(GlobalParams.OK, GlobalParams.OK));
		
		dialogButtonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	public static void showPopUpWithAction(final Context mContext,
			final Class<?> newClass, final String strMessages, final EditText focus) {
		String message;
		if (strMessages.equals(GlobalParams.BLANK)) {
			message = GlobalParams.WRONG_USER;
		} else {
			message = strMessages;
		}
		
		final Dialog dialog = new Dialog(mContext, R.style.Dialog_NoTitle);
		dialog.setContentView(R.layout.dialogwarning);		
		TextView text2 = (TextView) dialog.findViewById(R.id.tvScantitle2);		
		text2.setText(message);

		Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
		dialogButtonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				focus.requestFocus();
				showKeyboard(mContext);
			}
		});
		dialog.show();
	}

	public static void showKeyboard(final Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}
	
	public static void hideKeyboard(final Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(((Activity) mContext).getWindow().getCurrentFocus().getWindowToken(), 0);
	}
	
	public static boolean showKeyboard(Context mContext, EditText edt) {	
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		return imm.showSoftInput(edt, InputMethodManager.SHOW_IMPLICIT);
	}
	
	public static boolean hideKeyboard(Context mContext, EditText edt) {	
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		return imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
	}
	
	public static void scanCode(final Activity mContext) {
		Intent intentCamera = new Intent("com.google.zxing.client.android.SCAN");
		intentCamera.putExtra("SCAN_MODE","QR_CODE_MODE,PRODUCT_MODE,CODE_39,CODE_93,CODE_128,DATA_MATRIX");
		mContext.startActivityForResult(intentCamera, GlobalParams.SCAN);
	}

	public static boolean isSoftKeyboardShowing(Activity mContext) {
	    InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    return inputMethodManager.isActive();
	}
	
	public static void setKeyboardVisibilityListener(Activity activity, final KeyboardVisibilityListener keyboardVisibilityListener) {
	    final View contentView = activity.findViewById(android.R.id.content);
	    contentView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	        private int mPreviousHeight;

	        @Override
	        public void onGlobalLayout() {
	            int newHeight = contentView.getMeasuredHeight();
	            if (mPreviousHeight != 0) {
	                if (mPreviousHeight > newHeight) {
	                    // Height decreased: keyboard was shown
	                    keyboardVisibilityListener.onKeyboardVisibilityChanged(true);
	                    Logger.error("333333: " + newHeight + " : " + mPreviousHeight);
	                } else if (mPreviousHeight < newHeight) {
	                    // Height increased: keyboard was hidden
	                    keyboardVisibilityListener.onKeyboardVisibilityChanged(false);
	                    Logger.error("4444444: " + newHeight + " : " + mPreviousHeight);
	                } else {
	                    // No change
	                	Logger.error("555555555: " + newHeight + " : " + mPreviousHeight);
	                }
	            }
	            mPreviousHeight = newHeight;
	        }
	    });
	}
	
	public static int getPagerNumber(int total, int div) {

		if (total % div == 0) {
			return total / div;
		} else {
			return total / div + 1;
		}
	}
	
	// Show dialog
    public static void dialogShow(String message, Activity activity)
    {
    	final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogwarning);
        dialog.setCanceledOnTouchOutside(false);
        TextView tvScantitle2 = (TextView) dialog.findViewById(R.id.tvScantitle2);
        tvScantitle2.setText(message);
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();          
			}
		});
		dialog.show();
    }
    
    /**
     * trim " character in response string
     * @param s example abc or "abc"
     * @return abc
     */
    public static String getDataBarcode(String s) {
		if(s.indexOf("\"") > -1) {
			s = s.substring(1, s.length() - 1);
		}
		
		return s;
	}   
}
