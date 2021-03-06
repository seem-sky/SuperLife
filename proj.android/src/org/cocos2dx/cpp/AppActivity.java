/****************************************************************************
Copyright (c) 2008-2010 Ricardo Quesada
Copyright (c) 2010-2012 cocos2d-x.org
Copyright (c) 2011      Zynga Inc.
Copyright (c) 2013-2014 Chukong Technologies Inc.
 
http://www.cocos2d-x.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
****************************************************************************/
package org.cocos2dx.cpp;

import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxGLSurfaceView;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class AppActivity extends Cocos2dxActivity {
	private static String MY_FLURRY_APIKEY = "NXFGF9WJH6H6K42MZ54Q";
	private static String MY_AD_UNIT_ID = "ca-app-pub-3628527903442392/5175433864";
	private static AppActivity context; 
	private AdView adView;
	private WindowManager windowMgr;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        context = this; 
        windowMgr = (WindowManager) context.getSystemService("window");
        
        FlurryAgent.setLogEnabled(false);
		//FlurryAgent.setLogLevel(Log.VERBOSE);
		FlurryAgent.init(this, MY_FLURRY_APIKEY);
	}
	
	private class AdmobListener extends AdListener { 
	    @Override
	    public void onAdLoaded() {
	        Log.d("SuperLife", "onAdLoaded"); 
	        if (null != adView) {
				adView.setVisibility(View.VISIBLE);
			}
	    }
	    @Override
	    public void onAdFailedToLoad(int errorCode) {
	        String errorReason = "";
	        switch(errorCode) {
	            case AdRequest.ERROR_CODE_INTERNAL_ERROR:
	                errorReason = "Internal error";
	                break;
	            case AdRequest.ERROR_CODE_INVALID_REQUEST:
	                errorReason = "Invalid request";
	                break;
	            case AdRequest.ERROR_CODE_NETWORK_ERROR:
	                errorReason = "Network Error";
	                break;
	            case AdRequest.ERROR_CODE_NO_FILL:
	                errorReason = "No fill";
	                break;
	        }
	        Log.d("SuperLife","failed to receive ad (" + errorReason + ")"); 
	    }

	    @Override
	    public void onAdOpened() {
	    	Log.d("SuperLife", "onAdOpened"); 
	    }

	    @Override
	    public void onAdClosed() {
	    	Log.d("SuperLife", "onAdClosed"); 
	    }

	    @Override
	    public void onAdLeftApplication() {
	    	 Log.d("SuperLife", "onAdLeftApplication"); 
	    } 
    } 
	public void showAds(){
    	context.runOnUiThread(new Runnable() { 
    		@Override 
            public void run() { 
    			if (null != adView) {
					if (null != windowMgr) {
						windowMgr.removeView(adView);
					}
					adView.destroy();
					adView = null;
				}
    			// 创建adView。
    		    adView = new AdView(context);
    		    adView.setAdUnitId(MY_AD_UNIT_ID);
    		    adView.setAdSize(AdSize.BANNER);
    		    adView.setAdListener(new AdmobListener());
    		    
    			WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    	 		params.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
    	 		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
    	 		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
    	 		params.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    	 		params.gravity = Gravity.BOTTOM | Gravity.LEFT;
    	 		windowMgr.addView(adView, params);
    	 		adView.setVisibility(View.GONE);
    	 		
                //测试Admob时使用测试Device
                AdRequest adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest); 
    		}
    	});
    }
    public void hideAds(){
    	context.runOnUiThread(new Runnable() { 
    		@Override 
            public void run() { 
    			if (null != adView) {
					if (null != windowMgr) {
						windowMgr.removeView(adView);
					}
					adView.destroy();
					adView = null;
				}
    		}
    	});
    }
	public static void setAdVisible(boolean b) { 
		if(b)
			context.showAds();
		else
			context.hideAds();
	} 

	@Override
	protected void onStart()
	{
		super.onStart();
		FlurryAgent.onStartSession(this);
	}
	 
	@Override
	protected void onStop()
	{
		super.onStop();		
		FlurryAgent.onEndSession(this);
	}
	@Override
	public void onDestroy() {
		if (null != adView) {
			if (null != windowMgr) {
				windowMgr.removeViewImmediate(adView);
			}
			adView.destroy();
			adView = null;
		}
	    super.onDestroy();
	}
}
