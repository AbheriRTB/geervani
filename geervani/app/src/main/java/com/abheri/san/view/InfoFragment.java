package com.abheri.san.view;

import com.abheri.san.R;

import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InfoFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_info, container,
				false);

		LinearLayout lLayout = (LinearLayout)rootView.findViewById(R.id.infolayout);
		
		/* Add tabbar programatically to support lower versions */
		if (Util.androidversion < Util.minversioncheck)
		{
			View tabbarview = inflater.inflate(R.layout.tabbar, container, false);
			RelativeLayout rl = (RelativeLayout)rootView;
			rl.addView(tabbarview);
			TabBarListenerForLV tl = new TabBarListenerForLV();
			tl.setTabbarView(tabbarview, TabBarListenerForLV.INFO);
			
			Typeface font = Util.getLocalFont();
			//TextView tv = (TextView)rootView.findViewById(R.id.welcometxt);
			//tv.setTypeface(font);
		}
		
		
		//WebView wv = (WebView) rootView.findViewById(R.id.infoview);
		//wv.loadData(getString(R.string.info), "text/html", "utf-8");
		
		//wv.setScrollContainer(true);

		TextView tv = (TextView)rootView.findViewById(R.id.infoview);
		tv.setText(Html.fromHtml(getString(R.string.info)).toString());

		int v = 0;
		String vn = "";
		try {
			String pkgname = rootView.getContext().getPackageName();
			PackageManager pm = rootView.getContext().getPackageManager();
			v = pm.getPackageInfo(pkgname, 0).versionCode;
			vn = pm.getPackageInfo(pkgname, 0).versionName;
		} catch (Exception e) {
			// Huh? Really?
		}
		
		TextView iv = (TextView) rootView.findViewById(R.id.versioninfo);
		if (Util.androidversion < 19) {
            lLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
			iv.setText(getResources().getString(R.string.welcome) + "(Version " + vn + ")");
		}else{
			iv.setText( "Version " + vn);
		}

		return rootView;
	}

}