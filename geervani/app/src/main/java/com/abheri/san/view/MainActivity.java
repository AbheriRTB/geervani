package com.abheri.san.view;

import com.abheri.san.R;
import com.abheri.san.data.NetworkUtil;
import com.abheri.san.data.WebFileReader;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.Context;

@TargetApi(11)
public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener, HandleServiceResponse {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	public static Context c;
	public static MainActivity ma;
	ProgressBar progressBar;
    ImageView logoImage;
    public int pos = 0;
	// Tab titles
	private String[] tabs = { "ThoughtForTheDay", "Topics", "Dictionary", "Info" };
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		c = this.getApplicationContext();

		progressBar = (ProgressBar)findViewById(R.id.MainProgressBar);
        logoImage = (ImageView)findViewById(R.id.MainLogoImage);

		Util ut = new Util();
		if (NetworkUtil.isNetworkAvailable(this))  {
			progressBar.setVisibility(View.VISIBLE);
			WebFileReader rt = new WebFileReader(this, GeervaniViews.HOME, this);
			rt.execute("");
		}

	}
	
	public static Context getAppContext()
	{
		return c;
	}

	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

		System.out.print("*****In onTabSelectd ******" + ft.toString());
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());

	}

	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
		System.out.print("*****In onTabReselected ******" + ft.toString());

	}

	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
		System.out.print("*****In onTabUnselected ******" + ft.toString());
	}
	
	@Override
	public void onBackPressed()
	{
		
		/*Hack: If it is info tab don't do anything on back button press
		 * as doing so is crashing the app after going to sentences fragment
		 * Couldn't figure out what was the issue. Hence this hack
		 * Do this only when using ViewPager and ActionBar & if the tab is Info
		 */
		if(pos == 3 && Util.androidversion >= Util.minversioncheck)
		{
			Toast.makeText(getApplicationContext(), R.string.nobackpress, Toast.LENGTH_LONG).show();
		}
		else
			super.onBackPressed();
		
	}

    /* This method is called after the WebFileReader AsyncTask is
        successfully finished
     */
	@Override
	public void onSuccess(Object result) {

        progressBar.setVisibility(View.GONE);
        logoImage.setVisibility(View.GONE);

		tabs[0] = getString(R.string.todtab);
		tabs[1] = getString(R.string.topictab);
		tabs[2] = getString(R.string.dicttab);
		tabs[3] = getString(R.string.infotab);


		// Initialization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		// actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			/*
			 * ActionBar.Tab tab = actionBar.newTab(); tab.setText(tab_name);
			 * tab.setTabListener(this); actionBar.addTab(tab);
			 */

			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
				pos = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

	}

	@Override
	public void onError(Object result) {

	}
}