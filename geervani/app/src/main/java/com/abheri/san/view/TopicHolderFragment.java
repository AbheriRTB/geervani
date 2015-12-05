package com.abheri.san.view;

import com.abheri.san.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TopicHolderFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_topicholder,
				container, false);

		/*
		 * Create Topic fragment and replace the frame layout with it
		 */
		Log.i("PRAS", "In TestFragment");
		Fragment newFragment = new TopicsFragment();
		FragmentActivity act = (FragmentActivity) rootView.getContext();
		FragmentTransaction transaction = act.getSupportFragmentManager()
				.beginTransaction();

		// Replace whatever is in the fragment_container view with this
		// fragment,
		// and add the transaction to the back stack
		//transaction.replace(R.id.topicHolder, newFragment);
		transaction.replace(R.id.topicHolder, newFragment);

		//Don't put this transaction to backstack. 


		// Commit the transaction
		transaction.commit();

		// ------------------------

		return rootView;
	}
}