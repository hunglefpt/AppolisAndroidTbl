/**
 * Name: $RCSfile: CaptureBarcodeCamera.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.scan;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.appolis.androidtablet.R;
import com.appolis.utilities.GlobalParams;
import com.appolis.utilities.Logger;
import com.appolis.utilities.SharedPreferenceManager;

/**
 * @author HoangNH11
 * Scan bar code by camera 
 */
public class CaptureBarcodeCamera extends Activity implements OnClickListener {
	
	private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;
    private String resultScan;
    private ImageScanner scanner;      
    private boolean previewing = true;
    private TextView btnCancel;
    private ImageView btnInfor;
    private FrameLayout cameraPreview1;
    SharedPreferenceManager sm;
    Bundle bundle; 
	
	static {
        System.loadLibrary("iconv");
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan_screen);
		sm = new SharedPreferenceManager(this);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		InitLayout();
	}

	/**
	 * instance layout
	 */
	private void InitLayout() {
		btnCancel = (TextView) findViewById(R.id.btnCancel);
		btnInfor = (ImageView) findViewById(R.id.btnInfor);
		
		btnCancel.setOnClickListener(this);
		btnInfor.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.btnCancel:
			Intent intent = new Intent();          
            setResult(RESULT_CANCELED, intent);
			finish();
			break;
			
		case R.id.btnInfor:
			
			break;
			
		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {		
		super.onResume();	
		onSetupCameraScan();
	}

	// set up camera scan
	public void onSetupCameraScan()
	{
		autoFocusHandler = new Handler();
        mCamera = getCameraInstance();
        
        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);
        
        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);        
        LayoutParams l = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);
        preview.addView(mPreview, l);
	}
	
	public void onPause() {
        super.onPause();
        releaseCamera();
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
        	Log.e("CaptureActivityGarden", "Exception: " + e);
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
            public void run() {
                if (previewing)
                    mCamera.autoFocus(autoFocusCB);
            }
    };

    PreviewCallback previewCb = new PreviewCallback() {
            public void onPreviewFrame(byte[] data, Camera camera) {            	
                Camera.Parameters parameters = camera.getParameters();
                Size size = parameters.getPreviewSize();               
                Image barcode = new Image(size.width, size.height, "Y800");
                barcode.setData(data);
                int result = scanner.scanImage(barcode);   
                
                if (result != 0) {
                    previewing = false;
                    mCamera.setPreviewCallback(null);
                    mCamera.stopPreview();               
                    SymbolSet syms = scanner.getResults();
                    for (Symbol sym : syms) {
                        resultScan = sym.getData();
                        Logger.error(resultScan);
                    }

                    Intent intent = new Intent();
                    intent.putExtra(GlobalParams.RESULTSCAN , resultScan);  
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
    };

    // Mimic continuous auto-focusing
    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
    		Intent intent = new Intent();    	
            setResult(RESULT_CANCELED, intent);
			finish();
            return true;
        }
    	
		return super.onKeyDown(keyCode, event);		
	}

    /**
     * Instance bundle
     * @return
     */
	public Bundle getBundle()
    {
        Bundle bundle = this.getIntent().getExtras();
        
        if (bundle == null)
        {
            bundle = new Bundle();
        }
        
        return bundle;
    }
}
