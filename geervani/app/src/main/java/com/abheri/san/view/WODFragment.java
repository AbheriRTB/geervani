package com.abheri.san.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.abheri.san.R;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.text.method.ScrollingMovementMethod;

public class WODFragment extends Fragment{

	Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		List<String> subhashitas = new ArrayList<String>();

		final View rootView = inflater.inflate(R.layout.fragment_wod,
				container, false);

		/* Add Manual Tabbar (only for lower versions) */
		if (Util.androidversion < Util.minversioncheck)
		{
			View tabbarview = inflater.inflate(R.layout.tabbar, container, false);
			RelativeLayout rl = (RelativeLayout) rootView;
			rl.addView(tabbarview);
	
			TabBarListenerForLV tl = new TabBarListenerForLV();
			tl.setTabbarView(tabbarview, TabBarListenerForLV.TOD);
		}
		/* End add manual tabbar */

		context =  SplashActivity.getAppContext();
		AssetManager am = context.getAssets();
		InputStream is;
		try {
			is = am.open("datafiles/topics/subhashitani.txt");
			InputStreamReader inputStreamReader = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(inputStreamReader);

			String sCurrentLine;
			String sub = "";
			do {
				sCurrentLine = br.readLine();
				if (sCurrentLine == null)
					break;
				if (sCurrentLine.trim().startsWith("<break>")) {
					subhashitas.add(sub);
					sub = "";
				} else {
					// Log.i("MAHA",sCurrentLine);
					sub += sCurrentLine + Util.lineSeparator();
				}
			} while (sCurrentLine != null);

		} catch (IOException e) {
			Log.i("PRAS", "IOException occurred");
			Log.i("PRAS", e.toString());
		}

		TextView sv = (TextView) rootView.findViewById(R.id.subhashita);
		int sSize = subhashitas.size();
		Log.i("MAHA", "Size is " + sSize);

		int srandom = (int) (Math.random() * sSize);
		Log.i("MAHA", subhashitas.get(srandom));
		
		if(Util.androidversion < Util.minversioncheck)
		{
			Typeface font= Util.getLocalFont();
			sv.setTypeface(font); 
		}
		
		Calendar cal = Calendar.getInstance();
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		
		//Don't user Random. Use day of the month as index.
		sv.setText(subhashitas.get(dayOfMonth));
		sv.setMovementMethod(new ScrollingMovementMethod());

		return rootView;
	}
}