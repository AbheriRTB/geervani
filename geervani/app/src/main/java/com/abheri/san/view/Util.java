package com.abheri.san.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

@SuppressLint("NewApi")
public class Util {
	
	public static String lineSeparator()
	{
	    if(android.os.Build.VERSION.SDK_INT >= 19)
	    	return System.lineSeparator();
	    else
	    	return "\n";
	}
	
	public static Typeface getLocalFont()
	{
		Context c =  SplashActivity.getAppContext();
		AssetManager am = c.getAssets();
		//Typeface font= Typeface.createFromAsset(am, "DroidSans.ttf");
		Typeface font= Typeface.createFromAsset(am, "AA_NAGARI_SHREE_L1.ttf");
		
		return font;
	}
	
	public static int minversioncheck = 11;
	
	//Uncomment the following line for production purposes
	public static int androidversion = android.os.Build.VERSION.SDK_INT;
	
	//Uncomment the following line if want to fake the android version for testing
	//public static int androidversion = 8;
	


}
