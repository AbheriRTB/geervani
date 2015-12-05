package com.abheri.san.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.abheri.san.R;

public class TabBarListenerForLV implements OnClickListener {

	View tabbarview;
	public Button tod, topic, dict, info;
	public static final int TOD=1;
	public static final int TOPICS=2;
	public static final int DICTIONARY=3;
	public static final int INFO=4;

	public void setTabbarView(View tabbar, int currentbutton) {

		tabbarview = tabbar;
		tod = (Button) tabbarview.findViewById(R.id.wodtab);
		topic = (Button) tabbarview.findViewById(R.id.topictab);
		dict = (Button) tabbarview.findViewById(R.id.dictionarytab);
		info = (Button) tabbarview.findViewById(R.id.infotab);

		tod.setOnClickListener(this);
		topic.setOnClickListener(this);
		dict.setOnClickListener(this);
		info.setOnClickListener(this);
		
		//Set the margin to the current button to highlight the selection
		setButtonMargin(tod, 0, 0, 0, 0);
		setButtonMargin(topic, 0, 0, 0, 0);
		setButtonMargin(dict, 0, 0, 0, 0);
		setButtonMargin(info, 0, 0, 0, 0);
		int nm = 5;
		if(currentbutton == TabBarListenerForLV.TOD)
			setButtonMargin(tod, 0,0,0,nm);
		if(currentbutton == TabBarListenerForLV.TOPICS)
			setButtonMargin(topic, 0,0,0,nm);
		if(currentbutton == TabBarListenerForLV.DICTIONARY)
			setButtonMargin(dict, 0,0,0,nm);
		if(currentbutton == TabBarListenerForLV.INFO)
			setButtonMargin(info, 0,0,0,nm);
			
	}	
	
	void setButtonMargin(Button btn, int l, int t, int r, int b)
	{
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)btn.getLayoutParams();
		params.setMargins(l,t,r,b); //left, top, right, bottom
		btn.setLayoutParams(params);
		btn.setPadding(2, 10, 2, 10);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		int id = v.getId();

		Fragment newfragment = new WODFragment();

		if (id == tod.getId())
			newfragment = new WODFragment();
		else if (id == topic.getId())
			newfragment = new TopicsFragment();
		else if (id == dict.getId())
			newfragment = new DictionaryFragment();
		else if (id == info.getId())
			newfragment = new InfoFragment();

		FragmentManager fm  = (FragmentManager) MainActivityForLowerVersion.getMyFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();

		// Replace whatever is in the fragment_container view with this
		// fragment,
		// and add the transaction to the back stack
		transaction.replace(R.id.container, newfragment);
		transaction.addToBackStack(null);

		// Commit the transaction
		transaction.commit();

	}

}
