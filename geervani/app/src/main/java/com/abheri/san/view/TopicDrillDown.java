package com.abheri.san.view;

import com.abheri.san.R;
import com.abheri.san.data.*;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class TopicDrillDown implements OnItemClickListener {

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		// ListView Clicked item value
		Topic itemValue = (Topic) parent.getItemAtPosition(position);

		/*--------------------------------
		 * Switch fragment. Pass the topic 
		 * selection through intent
		 *------------------------------- */
		Log.i("PRAS", "In TopicDrillDown");

		Bundle arguments = new Bundle();
		arguments.putLong("topic_id", itemValue.getId());
		arguments.putString("topic", itemValue.getTopic());
		arguments.putString("topicsanskrit", itemValue.getTopicSanskrit());

		Fragment newFragment = new SentencesFragment();
		newFragment.setArguments(arguments);
		FragmentActivity act = (FragmentActivity) view.getContext();
		FragmentTransaction transaction = act.getSupportFragmentManager()
				.beginTransaction();

		// Replace whatever is in the fragment_container view with this
		// fragment,
		// and add the transaction to the back stack
		if(Util.androidversion >= Util.minversioncheck)
		{
			transaction.replace(R.id.topicFragment, newFragment);
		}
		else
		{
			transaction.replace(R.id.container, newFragment);
			
		}

		transaction.addToBackStack(null);

		// Commit the transaction
		transaction.commit();
	}

}
