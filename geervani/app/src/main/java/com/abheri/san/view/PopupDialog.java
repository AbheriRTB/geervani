package com.abheri.san.view;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.abheri.san.R;
 
public class PopupDialog extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.popupdialogfragment, container,
                false);
        getDialog().setTitle("Abbrevations");        
        // Do something else
        return rootView;
    }
    
    @Override
    public void onStart()
    {
    	super.onStart();
    	if(getDialog() == null)
    	{
    		return;
    	}
    	Window w = getDialog().getWindow();
    	int org_wd = this.getResources().getDisplayMetrics().widthPixels;
    	int org_ht = this.getResources().getDisplayMetrics().heightPixels;
 
    	double wd = org_wd * 0.75;
    	double ht = org_ht * 0.75;
    	
    	System.out.println("Width & Height  " + 
    					org_wd + ":" + org_ht + "----" +
    					wd + ":" + ht + "----" +
    					(int)wd + ":" + (int)ht);
  
    	w.setLayout((int)wd, (int)ht);
    }
}