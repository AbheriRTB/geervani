package com.abheri.san.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.Toast;

import com.abheri.san.BuildConfig;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

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


	//--------------- Data Releated

	public static final String SUBHASHITANI = "subhashitani";

	public static final String[] topic_files = new String[] { "Etiquettes", "Introduction",
			"MeetingFriends", "Journey", "OnArrival", "Train", "Students",
			"Examination", "Films", "Teachers", "Telephone",
			"DressJewelry", "Commerce", "Weather", "Domestic", "Food",
			"Women", "Time", "Greetings", Util.SUBHASHITANI};

	public static final String[] word_files = new String[] { "LetterA", "LetterB", "LetterC",
			"LetterD", "LetterE", "LetterF", "LetterG", "LetterH",
			"LetterI", "LetterJ", "LetterK", "LetterK", "LetterL",
			"LetterM", "LetterN", "LetterO", "LetterP", "LetterQ",
			"LetterR", "LetterS", "LetterT", "LetterU", "LetterV",
			"LetterW", "LetterX", "LetterY", "LetterZ", };


	public static String getServiceUrl(){

		String url = "";

		//url="http://10.0.3.2/datafiles/"; //For Genymotion
		//url = "http://10.0.2.2/datafiles/"; //For AVD
		url="http://abheri.pythonanywhere.com/static/geervani/datafiles/";

		return url;
	}


	public void myToastMessage(android.content.Context context){

		Toast.makeText(
				context,
				"Timer Cancelled...",
				Toast.LENGTH_SHORT).show();
	}

	public static void logToGA(String what) {
		Tracker mTracker;

		//Log to Google Analytics only when the build type = Release
		if (!BuildConfig.DEBUG) {
			// Obtain the shared Tracker instance.
			AnalyticsApplication application = (AnalyticsApplication) new AnalyticsApplication();
			mTracker = application.getDefaultTracker();
			Log.i("Geervani", "Setting screen name: " + what);
			mTracker.setScreenName("Image~" + what);
			mTracker.send(new HitBuilders.ScreenViewBuilder().build());
			mTracker.send(new HitBuilders.EventBuilder()
					.setCategory("Action")
					.setAction("Share")
					.build());
		}
	}
	


}
