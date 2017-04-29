package com.abheri.san.view;

import com.abheri.san.R;
import com.abheri.san.data.NetworkUtil;
import com.abheri.san.data.WebFileReader;
import com.crashlytics.android.Crashlytics;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.Context;

import io.fabric.sdk.android.Fabric;

@TargetApi(11)
public class MainActivity extends FragmentActivity implements
        ActionBar.TabListener, HandleServiceResponse {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    public static Context context;
    public static MainActivity ma;
    ProgressBar progressBar;
    ImageView logoImage;
    public int pos = 0;
    // Tab titles
    private String[] tabs = {"ThoughtForTheDay", "Topics", "Dictionary", "Info"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        //requestWindowFeature(Window.FEATURE_PROGRESS);

        setContentView(R.layout.activity_main);
        context = this.getApplicationContext();

        setProgressBarIndeterminate(true);


        progressBar = (ProgressBar) findViewById(R.id.MainProgressBar);
        logoImage = (ImageView) findViewById(R.id.MainLogoImage);

        setActiveFlag(true);
        Util ut = new Util();
        if (NetworkUtil.isNetworkAvailable(this)) {
            progressBar.setVisibility(View.VISIBLE);
            WebFileReader rt = new WebFileReader(this, GeervaniViews.HOME, this);
            setProgressBarIndeterminateVisibility(true);
            rt.execute("");
        }
        this.onSuccess("RemoveProgressBarFalse");

    }

    public static Context getAppContext() {
        return context;
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
    public void onBackPressed() {

		/*Hack: If it is info tab don't do anything on back button press
		 * as doing so is crashing the app after going to sentences fragment
		 * Couldn't figure out what was the issue. Hence this hack
		 * Do this only when using ViewPager and ActionBar & if the tab is Info
		 */
        if (pos == 3 && Util.androidversion >= Util.minversioncheck) {
            Toast.makeText(getApplicationContext(), R.string.nobackpress, Toast.LENGTH_LONG).show();
        } else
            super.onBackPressed();

    }

    /* This method is called after the WebFileReader AsyncTask is
        successfully finished
     */
    @Override
    public void onSuccess(Object result) {

        progressBar.setVisibility(View.GONE);
        logoImage.setVisibility(View.GONE);

        if(result != null && (result.getClass() == String.class) && result.equals("RemoveProgressBarFalse")) {
            setProgressBarIndeterminateVisibility(true);//Keep the progress bar running
        }else{
            setProgressBarIndeterminateVisibility(false);//Network operation done. Stop progress bar
        }

        tabs[0] = getString(R.string.todtab);
        tabs[1] = getString(R.string.topictab);
        tabs[2] = getString(R.string.dicttab);
        tabs[3] = getString(R.string.infotab);

        //Don't do anything if app is no longer active when
        //callback is called after service interaction
        if(!isActivityActive()){
            return;
        }

        if (viewPager != null) {
            updateFragment();
            return;
        }

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

                updateFragment();
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

    public void updateFragment() {

        /* Find which fragment is active when refresh button is pressed
                 * Call corresponding 'getData()' method with force refresh
                 * (second argument as true).
                 *
                 * ProgramDetailsFragment does not have getData method. Hence user
                 * has to go back one screen to ProgramFragment to refresh the data
                 */
        if (viewPager.getCurrentItem() == 0) {
            WODFragment wf = (WODFragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
        } else if (viewPager.getCurrentItem() == 1) {
            TopicsFragment tf = (TopicsFragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
            tf.updateTopicList(tf.rootView, tf.listView);
        } else if (viewPager.getCurrentItem() == 2) {
            DictionaryFragment df = (DictionaryFragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
            df.updateWordList(df.rootView, null);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        FragmentManager fragmentManager;

        int v = 0;
        String vn = "";
        try {
            String pkgname = context.getPackageName();
            PackageManager pm = context.getPackageManager();
            v = pm.getPackageInfo(pkgname, 0).versionCode;
            vn = pm.getPackageInfo(pkgname, 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (id) {

            // action with ID action_settings was selected
            case R.id.action_about:
                Toast.makeText(this, "Geervani: (v" + vn + ") - By Team Abheri", Toast.LENGTH_LONG)
                        .show();
                break;
            case R.id.action_feedback:
                /*Toast.makeText(this, "Settings", Toast.LENGTH_SHORT)
                        .show();*/

                String subject = "Feedback on Geervani v" + vn;
                String body = "Hi Team Abheri, \n\nHere is my feedback on Geervani!";
                String to = "prasmax@gmail.com";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?subject=" + subject + "&body=" + body + "&to=" + to);
                intent.setData(data);
                startActivity(intent);
                break;

            default:
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        setActiveFlag(true);
    }

    @Override
    protected void onStop() {
        super.onStop();

        setActiveFlag(false);
    }

    public void setActiveFlag(boolean what){
        // Store our shared preference
        SharedPreferences sp = getSharedPreferences("OURINFO", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("active", what);
        ed.commit();
    }

    public boolean isActivityActive(){
        SharedPreferences sp = getSharedPreferences("OURINFO", MODE_PRIVATE);
        boolean isActive = sp.getBoolean("active", false);

        return isActive;
    }
}