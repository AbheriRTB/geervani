package com.abheri.san.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.abheri.san.R;
import com.abheri.san.data.DataHelper;
import com.abheri.san.data.NetworkUtil;
import com.abheri.san.data.SentenceDataCreator;
import com.abheri.san.data.WebFileReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
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

public class WODFragment extends Fragment implements HandleServiceResponse {

	Context context;
    ProgressBar progressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		List<String> subhashitas = new ArrayList<String>();

		final View rootView = inflater.inflate(R.layout.fragment_wod,
				container, false);

        progressBar = (ProgressBar)rootView.findViewById(R.id.WODProgressBar);

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

		getData(this, true);

		return rootView;
	}

	public void getData(WODFragment fragmentThis, boolean doRefresh) {

		Util ut = new Util();
		if (NetworkUtil.isNetworkAvailable(context))  {
            progressBar.setVisibility(View.VISIBLE);
			WebFileReader rt = new WebFileReader(fragmentThis, GeervaniViews.HOME);
			rt.execute("");
		}
	}

	@Override
	public void onSuccess(Object result) {
		DataHelper dh = new DataHelper(context);
		SQLiteDatabase database = dh.getDatabase();

		dh.TruncateSenteceTable();
		SentenceDataCreator sdc = new SentenceDataCreator(context, database);
		sdc.createSentences();

        progressBar.setVisibility(View.GONE);
	}

	@Override
	public void onError(Object result) {
        progressBar.setVisibility(View.GONE);


    }
}