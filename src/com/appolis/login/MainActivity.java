/**
 * Name: $RCSfile: MainActivity.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.login;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appolis.androidtablet.R;
import com.appolis.common.AppPreferences;
import com.appolis.common.LanguagePreferences;
import com.appolis.cyclecount.AcCycleCount;
import com.appolis.entities.ObjectIconMainScreen;
import com.appolis.move.AcMove;
import com.appolis.putaway.AcPutAway;
import com.appolis.receiverinventory.AcReceiverScanLP;
import com.appolis.receiving.AcRecevingList;
import com.appolis.scan.CaptureBarcodeCamera;
import com.appolis.scan.EzPairActivity;
import com.appolis.scan.SingleEntryApplication;
import com.appolis.userprofile.UserProfileActivity;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.Utilities;

/**
 * @author hoangnh11
 * Display Main screen
 */
public class MainActivity extends Activity implements OnClickListener, AnimationListener {

	private ViewPager viewPager;
	private static int NUM_AWESOME_VIEWS;
	private static int numberItem;
	private Context mContext;
	private AwesomePagerAdapter viewPagerAdapter;
	private ObjectIconMainScreen[] arryIcon;
	private Button btnNext;
	private Button btnPrevious;
	private TextView tvHeader;
	private ImageView imgViewScanBarcode;
	private String message;
    private Animation animation1;
    private Animation animation2;
    private ImageView imgHome;
    private Button btn;
    IntentFilter filter;
	private Context _context;
	boolean checkSocket = false;
	private LanguagePreferences languagePrefs;
	private ArrayList<String> itemName;
	private LinearLayout linTip, linTipTwo;
	private ImageView imgCancel, imgCancelTwo;
	private AppPreferences _appPrefs;
	private String scanFlag;
    long lastClickTime = 0;
    
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_appPrefs = new AppPreferences(getApplicationContext());
		languagePrefs = new LanguagePreferences(getApplicationContext());
		itemName = new ArrayList<>();
		
		itemName.add(0, getLanguage(GlobalParams.RECEIVE, GlobalParams.RECEIVE));
		itemName.add(1, getLanguage(GlobalParams.MAINLIST_MENPUTAWAY, GlobalParams.PUT_AWAY));
		itemName.add(2, getLanguage(GlobalParams.PICK, GlobalParams.PICK));
		itemName.add(3, getLanguage(GlobalParams.MAINLIST_MENBINTRANSFER, GlobalParams.MOVE));
		itemName.add(4, getLanguage(GlobalParams.INVENTORYADJUSTMENT_NAME, GlobalParams.INVENTORY_ADJUSTMENT));
		itemName.add(5, getLanguage(GlobalParams.MAINLIST_MENCYCLECOUNT, GlobalParams.CYCLE_COUNT));
		itemName.add(6, getLanguage(GlobalParams.MAINLIST_MENLPOPERATIONS, GlobalParams.LP_OPERATIONS));
		itemName.add(7, getLanguage(GlobalParams.SHIP, GlobalParams.SHIP));
		itemName.add(8, getLanguage(GlobalParams.MAINLIST_MENEXITLOGOUT, GlobalParams.LOGOUT));
		itemName.add(9, getLanguage(GlobalParams.REPLENISH, GlobalParams.REPLENISH));
		itemName.add(10, getLanguage(GlobalParams.MAINLIST_MENUSERPROFILE, GlobalParams.PROFILE));
		itemName.add(11, getLanguage(GlobalParams.RETURNPARTMAIN_LBL_HEADER, GlobalParams.RETURN_PARTS));
		itemName.add(12, getLanguage(GlobalParams.MAINLIST_MENINVENTORYHISTORY, GlobalParams.INVENTORY_HISTORY));
		itemName.add(13, getLanguage(GlobalParams.MAINLIST_MENJOBS, GlobalParams.JOBS));
		itemName.add(14, getLanguage(GlobalParams.MAINLIST_MENMANAGEPARTSANDSUPPLIES, GlobalParams.MANAGE_PARTS));
		itemName.add(15, getLanguage(GlobalParams.MAINLIST_MENRECEIVEINVENTORY, GlobalParams.RECEIVE_INVENSTORY));
		itemName.add(16, getLanguage(GlobalParams.CREATE_BINS, GlobalParams.CREATE_BINS));
		itemName.add(17, getLanguage(GlobalParams.CREATE_ITEMS, GlobalParams.CREATE_ITEMS));

        animation1 = AnimationUtils.loadAnimation(this, R.anim.to_middle);
        animation1.setAnimationListener(this);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.from_middle);
        animation2.setAnimationListener(this);
		arryIcon = new ObjectIconMainScreen[GlobalParams.NAME_ITEM.length];
		
		for (int i = 0; i < arryIcon.length; i++) {
			arryIcon[i] = new ObjectIconMainScreen();
		}
		
		arryIcon[0].setId(0);
		arryIcon[0].setNameItem(GlobalParams.NAME_ITEM[0]);
		arryIcon[0].setIconItem(R.drawable.btn_main_receiving);
		arryIcon[1].setId(1);
		arryIcon[1].setNameItem(GlobalParams.NAME_ITEM[1]);
		arryIcon[1].setIconItem(R.drawable.btn_main_put_away);
		arryIcon[2].setId(2);
		arryIcon[2].setNameItem(GlobalParams.NAME_ITEM[2]);
		arryIcon[2].setIconItem(R.drawable.btn_main_picking);
		arryIcon[3].setId(3);
		arryIcon[3].setNameItem(GlobalParams.NAME_ITEM[3]);
		arryIcon[3].setIconItem(R.drawable.btn_main_move);
		arryIcon[4].setId(4);
		arryIcon[4].setNameItem(GlobalParams.NAME_ITEM[4]);
		arryIcon[4].setIconItem(R.drawable.btn_main_inv_adjust);
		arryIcon[5].setId(5);
		arryIcon[5].setNameItem(GlobalParams.NAME_ITEM[5]);
		arryIcon[5].setIconItem(R.drawable.btn_main_cycle_count);
		arryIcon[6].setId(6);
		arryIcon[6].setNameItem(GlobalParams.NAME_ITEM[6]);
		arryIcon[6].setIconItem(R.drawable.img_lpops_iconb);
		arryIcon[7].setId(7);
		arryIcon[7].setNameItem(GlobalParams.NAME_ITEM[7]);
		arryIcon[7].setIconItem(R.drawable.img_shipping_iconb);
		arryIcon[8].setId(8);
		arryIcon[8].setNameItem(GlobalParams.NAME_ITEM[8]);
		arryIcon[8].setIconItem(R.drawable.btn_main_logout);
		arryIcon[9].setId(9);
		arryIcon[9].setNameItem(GlobalParams.NAME_ITEM[9]);
		arryIcon[9].setIconItem(R.drawable.img_replenish_iconb);
		arryIcon[10].setId(10);
		arryIcon[10].setNameItem(GlobalParams.NAME_ITEM[10]);
		arryIcon[10].setIconItem(R.drawable.btn_main_user_profile);
		arryIcon[11].setId(11);
		arryIcon[11].setNameItem(GlobalParams.NAME_ITEM[11]);
		arryIcon[11].setIconItem(R.drawable.btn_main_returns);
		arryIcon[12].setId(12);
		arryIcon[12].setNameItem(GlobalParams.NAME_ITEM[12]);
		arryIcon[12].setIconItem(R.drawable.btn_main_inv_history);
		arryIcon[13].setId(13);
		arryIcon[13].setNameItem(GlobalParams.NAME_ITEM[13]);
		arryIcon[13].setIconItem(R.drawable.btn_main_jobs);
		arryIcon[14].setId(14);
		arryIcon[14].setNameItem(GlobalParams.NAME_ITEM[14]);
		arryIcon[14].setIconItem(R.drawable.btn_main_manage_parts_and_supplies);
		arryIcon[15].setId(15);
		arryIcon[15].setNameItem(GlobalParams.NAME_ITEM[15]);
		arryIcon[15].setIconItem(R.drawable.btn_main_receive_inventory);
		arryIcon[16].setId(16);
		arryIcon[16].setNameItem(GlobalParams.NAME_ITEM[16]);
		arryIcon[16].setIconItem(R.drawable.btn_bins);
		arryIcon[17].setId(17);
		arryIcon[17].setNameItem(GlobalParams.NAME_ITEM[17]);
		arryIcon[17].setIconItem(R.drawable.btn_items);
		
		setContentView(R.layout.activity_main);
		mContext = this;
		btn = new Button(mContext);
		numberItem = LoginActivity.listItemMain.length;	
		NUM_AWESOME_VIEWS = Utilities.getPagerNumber(numberItem, 10);
	
		initLayout();
	}

	/**
	 * instance layout
	 */
	private void initLayout() {
		viewPagerAdapter = new AwesomePagerAdapter();
		viewPager = (ViewPager) findViewById(R.id.awesomepager);
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int page) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				if (numberItem > 10) {
					if (arg0 == 0) {
						setStatusButtonsNextPrevious(false);
					} else {
						setStatusButtonsNextPrevious(true);
					}
				}				
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		
		tvHeader = (TextView) findViewById(R.id.tvHeader);
		tvHeader.setText(getLanguage(GlobalParams.WITHOUTWIRE, GlobalParams.WITHOUTWIRE));
		tvHeader.setVisibility(View.VISIBLE);
		imgHome = (ImageView) findViewById(R.id.imgHome);
		imgHome.setImageResource(R.drawable.socket_setting);
		imgHome.setVisibility(View.VISIBLE);
		imgViewScanBarcode = (ImageView) findViewById(R.id.img_main_menu_scan_barcode);
		imgViewScanBarcode.setVisibility(View.VISIBLE);	
		btnNext = (Button) findViewById(R.id.button_next);
		btnPrevious = (Button) findViewById(R.id.button_previous);
		linTip = (LinearLayout) findViewById(R.id.linTip);
		imgCancel = (ImageView) findViewById(R.id.imgCancel);
		linTipTwo = (LinearLayout) findViewById(R.id.linTipTwo);
		imgCancelTwo = (ImageView) findViewById(R.id.imgCancelTwo);
		
		// Check tip visible
		if (_appPrefs.getTipMain().equalsIgnoreCase(GlobalParams.TRUE)) {
			linTip.setVisibility(View.GONE);
		} else {
			linTip.setVisibility(View.VISIBLE);
		}
		
		// Check socket pair
		if (_appPrefs.getSocketConnect().equalsIgnoreCase(GlobalParams.TRUE)) {
//			imgHome.setVisibility(View.GONE);
		} else {
			imgHome.setVisibility(View.VISIBLE);
		}
		
		imgHome.setOnClickListener(this);
		imgViewScanBarcode.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		btnPrevious.setOnClickListener(this);
		imgCancel.setOnClickListener(this);
		imgCancelTwo.setOnClickListener(this);
		
		if (numberItem < 11) {
			btnPrevious.setEnabled(false);
			btnPrevious.setBackgroundResource(R.drawable.btn_prev_disabled);
			btnNext.setEnabled(false);
			btnNext.setBackgroundResource(R.drawable.btn_next_disabled);
		}
		_context = getApplicationContext();
		onRegisterReceiver();
	}

	/**
	 * instance adapter for view pager
	 * @author hoangnh11
	 */
	private class AwesomePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return NUM_AWESOME_VIEWS;
		}
	
		@Override
		public Object instantiateItem(ViewGroup collection, int position) {
			LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout bigLayout = (LinearLayout) layoutInflater.inflate(R.layout.main_page, null);
			LinearLayout[] chirldLayout = new LinearLayout[5];
			
			for (int i = 0; i < 5; i++) {
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
						(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp.setMargins(5, 10, 5, 0);
				chirldLayout[i] = new LinearLayout(mContext);
				chirldLayout[i].setGravity(Gravity.LEFT);
				chirldLayout[i].setLayoutParams(lp);
			}
			
			int temp = 0;
			int count = 0;
			
			for (int i = position * 10; i < numberItem; i++) {
				if (temp < 2) {
					if (i % 2 == 0) {
						
						for (int k = 0; k < arryIcon.length; k++) {
							if (arryIcon[k].getNameItem().equals(LoginActivity.listItemMain[i])) {
								chirldLayout[temp].addView(initButton(arryIcon[k].getId(), arryIcon[k].getIconItem(),
										arryIcon[k].getNameItem()));
								count++;
							}
						}
						
					} else {
						for (int k = 0; k < arryIcon.length; k++) {
							if (arryIcon[k].getNameItem().equals(LoginActivity.listItemMain[i])) {
								chirldLayout[temp].addView(initButton(arryIcon[k].getId(), arryIcon[k].getIconItem(),
										arryIcon[k].getNameItem()));
								count++;
							}
						}
					}
					
					if (count == 5) {
						temp++;
						count = 0;
					}
				}
			}
			
			for (int i = 0; i < 5; i++) {		
				switch (chirldLayout[i].getChildCount()) {
				case 1:
					chirldLayout[i].addView(initButtonTransferBackground());
					chirldLayout[i].addView(initButtonTransferBackground());
					break;
				case 2:
					chirldLayout[i].addView(initButtonTransferBackground());
					break;
				default:
					break;
				}
				
				bigLayout.addView(chirldLayout[i]);
			}
			
			collection.addView(bigLayout, 0);
			return bigLayout;
		}

		/**
		 * instance button
		 * @param id
		 * @param background
		 * @param text
		 * @return
		 */
		private Button initButton(int id, int background, String text) {LinearLayout.LayoutParams lp = 
				new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			Button btn = new Button(mContext);
			lp.setMargins(5, 0, 5, 0);	
			btn.setLayoutParams(lp);
			btn.setId(id);
			btn.setOnClickListener(MainActivity.this);
			btn.setBackgroundResource(background);
			btn.setText(text);
			btn.setTextColor(getResources().getColor(R.color.white));
			btn.setTextSize(18);
			btn.setGravity(Gravity.CENTER_HORIZONTAL);
			btn.setPadding(5,getResources().getDimensionPixelOffset(R.dimen.main_icon_pading_top), 5, 0);
			return btn;			
		}

		/**
		 * instance button background
		 * @return
		 */
		private Button initButtonTransferBackground() {
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams (LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			lp.weight = 1.0f;
			Button btn = new Button(mContext);
			lp.setMargins(5, 0, 5, 0);
			btn.setLayoutParams(lp);
			btn.setBackgroundColor(Color.TRANSPARENT);
			return btn;
		}

		@Override
		public void destroyItem(ViewGroup collection, int position, Object view) {
			collection.removeView((LinearLayout) view);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return (view == object);
		}

		@Override
		public void finishUpdate(ViewGroup arg0) {
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(ViewGroup arg0) {
		}

	}

	/**
	 * Set event for button Back
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Utilities.closeApp(this, getLanguage(GlobalParams.MESSAGECONFIRMEXIT,
					GlobalParams.ARE_YOU_SURE_YOU_WANT_TO_EXIT), GlobalParams.EXIT_APP,
					getLanguage(GlobalParams.YES_TEXT, GlobalParams.YES_TEXT),
					getLanguage(GlobalParams.NO_TEXT, GlobalParams.NO_TEXT));
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Process event for views
	 */
	@Override
	public void onClick(View v) {		
		Intent intent;
		long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime < GlobalParams.DOUBLE_CLICK_TIME_DELTA){
            //onDoubleClick(v);
        } else {
        	switch (v.getId()) {
    		
    		case R.id.imgHome:
    			// retrieve the scanner name and start the EZ Pair Process
    			intent = new Intent(_context, EzPairActivity.class);
    			startActivityForResult(intent, GlobalParams.SCAN_SOCKET);
    			break;
    			
    		case R.id.img_main_menu_scan_barcode:
    			intent = new Intent(this, CaptureBarcodeCamera.class);
    			startActivityForResult(intent, GlobalParams.AC_MAIN);
    			break;
    			
    		case R.id.button_next:
    			 viewPager.setCurrentItem(1, true);
    			 setStatusButtonsNextPrevious(true);
    			break;
    			
    		case R.id.button_previous:
    			 viewPager.setCurrentItem(0, true);
    			 setStatusButtonsNextPrevious(false);
    			break;
    			
    		case R.id.imgCancel:
    			linTip.setVisibility(View.GONE);
    			_appPrefs.saveTipMain(GlobalParams.TRUE);
    			break;
    			
    		case R.id.imgCancelTwo:
    			linTipTwo.setVisibility(View.GONE);
    			_appPrefs.saveTipMainTwo(GlobalParams.TRUE);
    			break;
    			
    		case 0: // Receiving	
    			setButtonId(v.getId());
    			break;
    			
    		case 1: // Put away
    			setButtonId(v.getId());
    			break;
    			
    		case 2:
    			setButtonId(v.getId());
    			break;
    			
    		case 3: // Move
    			setButtonId(v.getId());
    			break;
    			
    		case 4:
    			setButtonId(v.getId());
    			break;
    			
    		case 5: // Cycle count
    			setButtonId(v.getId());
    			break;
    			
    		case 6:
    			setButtonId(v.getId());
    			break;
    			
    		case 7:
    			setButtonId(v.getId());
    			break;
    			
    		case 8: // Logout
    			setButtonId(v.getId());
    			break;
    			
    		case 9:
    			setButtonId(v.getId());
    			break;
    			
    		case 10: // User profile
    			setButtonId(v.getId());
    			break;
    			
    		case 11:
    			setButtonId(v.getId());
    			break;
    			
    		case 12:
    			setButtonId(v.getId());
    			break;
    			
    		case 13:
    			setButtonId(v.getId());
    			break;
    			
    		case 14:
    			setButtonId(v.getId());
    			break;	
    			
    		case 15: // Receiver inventory		
    			setButtonId(v.getId());
    			break;
    			
    		case 16: // Create bins		
    			setButtonId(v.getId());
    			break;
    			
    		case 17: // Create Items
    			setButtonId(v.getId());
    			break;
    			
    		default:
    			break;
    		}
        }
        lastClickTime = clickTime;
	}

	/**
	 * Process activity
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);	
		
		switch (requestCode) {		
		case GlobalParams.AC_MAIN:
			
			if(resultCode == RESULT_OK){
			   message = data.getStringExtra(GlobalParams.RESULTSCAN);
			   Utilities.dialogShow(message, this);
			}
			
			break;
			
		case GlobalParams.SCAN_SOCKET:			
			if(resultCode == RESULT_OK){
				
				if (!data.getStringExtra(GlobalParams.DEVICE_SOCKET).equals(GlobalParams.BLANK_CHARACTER)) {
					filter = new IntentFilter(SingleEntryApplication.NOTIFY_DECODED_DATA);
			        registerReceiver(this._newItemsReceiver, filter);
			        checkSocket = true;
				} else {
					checkSocket = false;
				}
				
			} else {
				checkSocket = false;
			}
			
		default:
			break;
		}
	}

	/**
	 * 
	 * @param enable
	 */
	private void setStatusButtonsNextPrevious(boolean enable) {
		/* set enable for next button */
		if (enable) {
			btnNext.setEnabled(false);
			btnNext.setBackgroundResource(R.drawable.btn_next_disabled);
			btnPrevious.setEnabled(true);
			btnPrevious.setBackgroundResource(R.drawable.btn_prev);	
			
			if (_appPrefs.getTipMainTwo().equalsIgnoreCase(GlobalParams.TRUE)) {				
				linTipTwo.setVisibility(View.GONE);
			} else {
				linTipTwo.setVisibility(View.VISIBLE);
			}
			
		} else {
			btnPrevious.setEnabled(false);
			btnPrevious.setBackgroundResource(R.drawable.btn_prev_disabled);
			btnNext.setEnabled(true);
			btnNext.setBackgroundResource(R.drawable.btn_next);
		}
	}

	/**
	 * instance animation for views
	 */
	@Override
	public void onAnimationEnd(Animation animation) {
		Intent intent;
		
        if (animation == animation1) {
        	btn.clearAnimation();
        	btn.setAnimation(animation2);
        	btn.startAnimation(animation2);
        } else {
        	switch (btn.getId()) {
        	
    		case 0: // Receiving	
    			intent = new Intent(MainActivity.this, AcRecevingList.class);
				startActivity(intent);
    			break;
    			
    		case 1: // Put away    			
    			intent = new Intent(MainActivity.this, AcPutAway.class);
				startActivity(intent);
    			break;
    		case 2:

    			break;
    			
    		case 3: // Move
    			intent = new Intent(MainActivity.this, AcMove.class);
    			startActivity(intent);
    			break;
    			
    		case 4:

    			break;
    			
    		case 5: // Cycle count
    			Intent cycleIntent =  new Intent(MainActivity.this, AcCycleCount.class);
    			startActivity(cycleIntent);
    			break;
    			
    		case 6:

    			break;
    			
    		case 7:

    			break;
    			
    		case 8: // Logout
    			Utilities.closeApp(this, getLanguage(GlobalParams.MESSAGECONFIRMEXIT,
    					GlobalParams.ARE_YOU_SURE_YOU_WANT_TO_EXIT), GlobalParams.LOGOUT_APP,
    					getLanguage(GlobalParams.YES_TEXT, GlobalParams.YES_TEXT), 
    					getLanguage(GlobalParams.NO_TEXT, GlobalParams.NO_TEXT));
    			break;
    			
    		case 9:

    			break;
    			
    		case 10: // User profile
    			intent = new Intent(this, UserProfileActivity.class);
    			startActivity(intent);
    			break;
    			
    		case 11:

    			break;
    			
    		case 12:

    			break;
    			
    		case 13:

    			break;
    			
    		case 14:

    			break;
    			
    		case 15: // Receiver inventory		
    			intent = new Intent(MainActivity.this, AcReceiverScanLP.class);
    			startActivity(intent);    		
    			break;
    			
    		case 16: // Create bins		
    			
    			break;
    			
    		case 17: // Create items	
    			
    			break;
    			
    		default:
    			break;
    		}           
        }
	}

	@Override
	public void onAnimationRepeat(Animation animation) {		
	}

	@Override
	public void onAnimationStart(Animation animation) {		
	}
	
	/**
	 * set id for Button
	 * @param id
	 */
	private void setButtonId(int id){
		btn = (Button) findViewById(id);
		btn.clearAnimation();
		btn.setAnimation(animation1);
		btn.startAnimation(animation1);
	}
	
	/**
	 * handler for receiving the notifications coming from 
	 * SingleEntryApplication.
	 * Update the UI accordingly when we receive a notification
	 */
	private final BroadcastReceiver _newItemsReceiver = new BroadcastReceiver() {   
	    
		@Override  
	    public void onReceive(Context context, Intent intent) {
			
			// ScanAPI is initialized
	        if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_SCANPI_INITIALIZED)) {
	        	
	        }
	        
	        // a Scanner has connected
	        else if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_SCANNER_ARRIVAL)) {
	        	imgHome.setVisibility(View.VISIBLE);
	        	imgViewScanBarcode.setVisibility(View.GONE);	      
	        	_appPrefs.saveSocketConnect(GlobalParams.TRUE);
	        	scanFlag = GlobalParams.FLAG_ACTIVE;	        	
	        }
	        
	        // a Scanner has disconnected
	        else if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_SCANNER_REMOVAL)) {
//	        	imgHome.setVisibility(View.GONE);
	        	imgViewScanBarcode.setVisibility(View.VISIBLE);	        	
	        }
	        
	        // decoded Data received from a scanner
	        else if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_DECODED_DATA)) {
				char[] data = intent.getCharArrayExtra(SingleEntryApplication.EXTRA_DECODEDDATA);
				if (scanFlag.equals(GlobalParams.FLAG_ACTIVE)) {
					dialogShow(new String(data), MainActivity.this);
					scanFlag = GlobalParams.FLAG_INACTIVE;
				}
	        }
	        
	        // an error has occurred
	        else if(intent.getAction().equalsIgnoreCase(SingleEntryApplication.NOTIFY_ERROR_MESSAGE)) {
	        	String text = intent.getStringExtra(SingleEntryApplication.EXTRA_ERROR_MESSAGE);
//	        	Utilities.dialogShow(text, MainActivity.this);
	        }
	    }
	};

	@Override
	protected void onResume() {
		super.onResume();
		onRegisterReceiver();
		if (checkSocket) {
			filter = new IntentFilter(SingleEntryApplication.NOTIFY_DECODED_DATA);
	        registerReceiver(this._newItemsReceiver, filter);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		onUnregisterReceiver();
	}
	
	/**
	 * Register Receiver
	 */
	public void onRegisterReceiver() { 
		// register to receive notifications from SingleEntryApplication
        // these notifications originate from ScanAPI 
        IntentFilter filter;   
        filter = new IntentFilter(SingleEntryApplication.NOTIFY_SCANPI_INITIALIZED);   
        registerReceiver(this._newItemsReceiver, filter); 

        filter = new IntentFilter(SingleEntryApplication.NOTIFY_SCANNER_ARRIVAL);
        registerReceiver(this._newItemsReceiver, filter);

        filter = new IntentFilter(SingleEntryApplication.NOTIFY_SCANNER_REMOVAL);   
        registerReceiver(this._newItemsReceiver, filter);

        filter = new IntentFilter(SingleEntryApplication.NOTIFY_ERROR_MESSAGE);   
        registerReceiver(this._newItemsReceiver, filter);
		
        filter = new IntentFilter(SingleEntryApplication.NOTIFY_CLOSE_ACTIVITY);   
        registerReceiver(this._newItemsReceiver, filter);        
      
    	// increasing the Application View count from 0 to 1 will
    	// cause the application to open and initialize ScanAPI
    	SingleEntryApplication.getApplicationInstance().increaseViewCount();
	}
	
	/**
	 * Unregister Receiver
	 */
	public void onUnregisterReceiver(){
		// unregister the notifications
		unregisterReceiver(_newItemsReceiver);
        // indicate this view has been destroyed
        // if the reference count becomes 0 ScanAPI can
        // be closed if this is not a screen rotation scenario
        SingleEntryApplication.getApplicationInstance().decreaseViewCount();
	}
	
	/**
	 * get language from language package
	 */
	public String getLanguage(String key, String value){
		return languagePrefs.getPreferencesString(key, value);
	}
	
	// Show dialog
    public void dialogShow(String message, Activity activity)
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
				scanFlag = GlobalParams.FLAG_ACTIVE;
			}
		});
		
		dialog.show();		
    }
}