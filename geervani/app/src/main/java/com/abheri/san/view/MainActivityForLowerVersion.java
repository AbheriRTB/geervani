package com.abheri.san.view;

import com.abheri.san.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;

public class MainActivityForLowerVersion extends FragmentActivity {

	public static Context c;
	public static FragmentManager fm;
	public static MainActivityForLowerVersion ma;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_lv);
		c = this.getApplicationContext();
		fm = this.getSupportFragmentManager();

		if (savedInstanceState == null) {
			/* getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new WODFragment()).commit();*/
			
			
			Fragment newFragment = new WODFragment();
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

			// Replace whatever is in the fragment_container view with this
			// fragment,
			// and add the transaction to the back stack
			transaction.replace(R.id.container, newFragment);

			transaction.addToBackStack(null);


			// Commit the transaction
			transaction.commit();
		}

	}

	public static Context getAppContext() {
		return c;
	}
	
	public static FragmentManager getMyFragmentManager() {
		return fm;
	}

}