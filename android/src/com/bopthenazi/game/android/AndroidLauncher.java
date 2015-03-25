package com.bopthenazi.game.android;

import java.util.HashMap;

import android.graphics.Color;
import android.os.Bundle;
import android.text.style.EasyEditSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.bopthenazi.game.BTNGame;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kilobytegames.zombiebop.android.R;

public class AndroidLauncher extends AndroidApplication {
	
	  private static final String AD_UNIT_ID = "ca-app-pub-8229376725623531/7395624009";

	  protected AdView adView;

	  
	  public enum TrackerName {
		  
		  APP_TRACKER, // Tracker used only in this app.
		  GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
		  ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
		}

	  private HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
	  
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
		  
		  	super.onCreate(savedInstanceState);

		    AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		    cfg.useAccelerometer = false;
		    cfg.useCompass = false;
//		    cfg.useImmersiveMode = true;
	
		    // Do the stuff that initialize() would do for you
		    requestWindowFeature(Window.FEATURE_NO_TITLE);
		    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	
		    FrameLayout layout = new FrameLayout(this);
		    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		    layout.setLayoutParams(params);
	
		    View gameView = createGameView(cfg);
		    layout.addView(gameView);
		    
		    AdView admobView = createAdView();
		    layout.addView(admobView);
		    
		    setContentView(layout);
		    startAdvertising(admobView);
		    
		    // Enable analytics on the advertising.
		    // Get tracker.
		    Tracker t = getTracker(TrackerName.APP_TRACKER);

		    // Enable Advertising Features.
		    t.enableAdvertisingIdCollection(true);
		    
		    // Set screen name.
		    t.setScreenName("Main Screen");

		    // Send a screen view.
		    t.send(new HitBuilders.ScreenViewBuilder().build());
		    
		    Gdx.app.log("Zombie Bop!", "Sending screen hit now: " + t.toString());
	  }

	  private synchronized Tracker getTracker(TrackerName trackerId) {
		  
		  if (!mTrackers.containsKey(trackerId)) {

		    GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
		   
		    Tracker t = analytics.newTracker(R.xml.tracker);
		    
		    mTrackers.put(trackerId, t);
		  }
		  
		  return mTrackers.get(trackerId);
		}
	  
	  private AdView createAdView() {
	    
			adView = new AdView(this);
			adView.setAdSize(AdSize.SMART_BANNER);
		    adView.setAdUnitId(AD_UNIT_ID);
		    adView.setId(12345); // this is an arbitrary id, allows for relative positioning in createGameView()
		   
		    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		    params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		    params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		    
		    adView.setLayoutParams(params);
		    adView.setBackgroundColor(Color.BLACK);
		    
		    return adView;
	  }

	  private View createGameView(AndroidApplicationConfiguration cfg) {
	    
		  	View output = initializeForView(new BTNGame(), cfg);
		  
		    return output;
	  }

	  public void startAdvertising(AdView adView) {
	   
			AdRequest adRequest = new AdRequest.Builder().build();
//		  	AdRequest request = new AdRequest.Builder().addTestDevice("B791A64DEE8D9C6CECF0940D5A78F774").build();
		  
		  	adView.loadAd(adRequest);
	  }
	  
	  @Override
	  public void onResume() {
	    
		super.onResume();
	    
	    GoogleAnalytics.getInstance(this).reportActivityStart(this);
		
		if (adView != null) {
	    	
	    	adView.resume();
	    }
	  }

	  @Override
	  public void onPause() {
	    
		if (adView != null){ 
			
			adView.pause();
	  	}
	    	super.onPause();
	  }

	  @Override
	  public void onDestroy() {
	    
		  GoogleAnalytics.getInstance(this).reportActivityStop(this);
		    
		  if (adView != null){
			  
			  adView.destroy();
		  }
			  
		  super.onDestroy();
	  }
}
