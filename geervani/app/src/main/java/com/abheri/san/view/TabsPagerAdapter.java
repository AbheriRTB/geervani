package com.abheri.san.view;


/*
import com.abheri.geervani.WODFragment;
import com.abheri.geervani.SentencesFragment;
import com.abheri.geervani.DictionaryFragment;
import com.abheri.geervani.InfoFragment; */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class TabsPagerAdapter extends FragmentPagerAdapter {
 
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            // Word Of the Day fragment activity
            return new WODFragment();
        case 1:
            // Topics fragmentholder activity
            return new TopicHolderFragment();
        case 2:
            // Dictionary fragment activity
            return new DictionaryFragment();
        case 3:
            // Info fragment activity
            return new InfoFragment();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }
 
}