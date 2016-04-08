package com.abheri.san.view;

import java.util.Timer;
import java.util.TimerTask;

import com.abheri.san.R;
import com.abheri.san.data.DataFileCopier;
import com.abheri.san.data.TopicDataSource;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Window;
 
public class SplashActivity extends Activity {
 
    // Set Duration of the Splash Screen
    long Delay = 800;

	public static Context c;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove the Title Bar
        System.out.println("Testing......");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
 
        c = this.getApplicationContext();
        // Get the view from splash_screen.xml
        setContentView(R.layout.splash_screen);
 
        // Create a Timer
        Timer RunSplash = new Timer();
 
        // Task to do when the timer ends
        TimerTask ShowSplash = new TimerTask() {
            @Override
            public void run() {
                //Create database by invoking get topics
            	TopicDataSource ts = new TopicDataSource(c);
            	ts.open();
            	ts.getAllTopics();
            	ts.close();
            	
            	// Close SplashScreenActivity.class
            	finish();
            	Intent myIntent;
            	Log.i("PRAS", "Android Version Is:" + Util.androidversion);

                // Start MainActivity.class
        		if (Util.androidversion >= Util.minversioncheck)
            	{
	                 myIntent = new Intent(SplashActivity.this,
	                        MainActivity.class);
               
        		}
        		else
        		{
	                 myIntent = new Intent(SplashActivity.this,
	                        MainActivityForLowerVersion.class);
               
        		}
        		 startActivity(myIntent);
            }
        };
 
        // Start the timer
        RunSplash.schedule(ShowSplash, Delay);
    }
    
	public static Context getAppContext()
	{
		return c;
	}
}