package com.abheri.san.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.abheri.san.R;
import com.abheri.san.data.DataFileCopier;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.text.method.ScrollingMovementMethod;

public class WODFragment extends Fragment {

    Context context;
    List<String> subhashitas = new ArrayList<String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_wod,
                container, false);

		/* Add Manual Tabbar (only for lower versions) */
        if (Util.androidversion < Util.minversioncheck) {
            View tabbarview = inflater.inflate(R.layout.tabbar, container, false);
            RelativeLayout rl = (RelativeLayout) rootView;
            rl.addView(tabbarview);

            TabBarListenerForLV tl = new TabBarListenerForLV();
            tl.setTabbarView(tabbarview, TabBarListenerForLV.TOD);
        }
        /* End add manual tabbar */

        context = SplashActivity.getAppContext();
        TextView sv = (TextView) rootView.findViewById(R.id.subhashita);

        if (Util.androidversion < Util.minversioncheck) {
            Typeface font = Util.getLocalFont();
            sv.setTypeface(font);
        }

        String sub = getCurrentSubhashita();
        //Don't user Random. Use day of the month as index.
        sv.setText(sub);
        sv.setMovementMethod(new ScrollingMovementMethod());

        return rootView;
    }

    String getCurrentSubhashita() {

        InputStream is;
        InputStreamReader inputStreamReader;
        String retstr = getString(R.string.default_subhashita).toString();

        try {
            if (DataFileCopier.isSDCardAvailable()) {

                //Read the files from SD card
                System.out.println("****READING FROM SD CARD****");
                DataFileCopier dfc = new DataFileCopier(context);
                File infile = dfc.getFileOnSDCardForReading(Util.SUBHASHITANI + ".txt", dfc.TOPIC_DIRECTORY);
                is = new FileInputStream(infile);
                inputStreamReader = new InputStreamReader(is, "UTF-8");

            } else {
                //Read from the assets directory
                System.out.println("****READING FROM ASSETS****");
                AssetManager am = context.getAssets();
                is = am.open("datafiles/topics/" + Util.SUBHASHITANI + ".txt");
                inputStreamReader = new InputStreamReader(is);
            }
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

            int sSize = subhashitas.size();
            Log.i("PRAS", "Size is " + sSize);

            int srandom = (int) (Math.random() * sSize);
            Log.i("PRAS", subhashitas.get(srandom));


            Calendar cal = Calendar.getInstance();
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

            //Don't user Random. Use day of the month as index.
            retstr = subhashitas.get(dayOfMonth);
            Log.i("PRAS", "Selected Subhashita: " + retstr);

        } catch (IOException e) {
            Log.i("PRAS", "IOException occurred");
            Log.i("PRAS", e.toString());
        }

        return retstr;

    }
}