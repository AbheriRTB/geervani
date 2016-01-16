package com.abheri.san.view;

import java.util.List;

import com.abheri.san.R;
import com.abheri.san.data.*;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.util.Log;

public class TopicsFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_topics, container,
				false);

		/* Add programmatic tabbar to support lower versions */
		if (Util.androidversion < Util.minversioncheck) {
			View tabbarview = inflater.inflate(R.layout.tabbar, container,
					false);
			RelativeLayout rl = (RelativeLayout) rootView;
			rl.addView(tabbarview);
			TabBarListenerForLV tl = new TabBarListenerForLV();
			tl.setTabbarView(tabbarview, TabBarListenerForLV.TOPICS);
		}
		/* End add manual tabbar */

		Log.i("PRAS", "In TopicsFragment");
		Sentence.selectedPosition = -1; //Reset the position
		// --------------------------------

		// Get ListView object from xml
		ListView listView = (ListView) rootView.findViewById(R.id.topicsList);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setSelector(android.R.color.holo_blue_light);

		updateTopicList(rootView, listView);
		listView.setOnItemClickListener(new TopicDrillDown());

		// -----------------------------------------------------------

		return rootView;
	}

	void updateTopicList(View rootView, ListView topiclist) {
		TopicDataSource datasource = new TopicDataSource(rootView.getContext());
		datasource.open();

		List<Topic> values = datasource.getAllTopics();

		// use the SimpleCursorAdapter to show the
		// elements in a ListView
		ArrayAdapter<Topic> adapter = new ArrayAdapter<Topic>(
				rootView.getContext(), R.layout.topics_row_layout, values);

		topiclist.setAdapter(adapter);
		datasource.close();
	}

}