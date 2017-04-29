package com.abheri.san.view;

import java.util.List;

import com.abheri.san.R;
import com.abheri.san.data.*;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.util.Log;
import android.widget.TextView;

public class TopicsFragment extends Fragment {

    View rootView;
    ListView listView;
	TextView loadingText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_topics, container,
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

		loadingText = (TextView) rootView.findViewById(R.id.topicsLoadingText);
		loadingText.setText("Loading Data. Please Wait...");
		// Get ListView object from xml
        listView = (ListView) rootView.findViewById(R.id.topicsList);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setSelector(android.R.color.holo_blue_light);

		listView.setOnItemClickListener(new TopicDrillDown());

		return rootView;
	}

	@Override
	public void onResume(){
        super.onResume();
        updateTopicList(rootView, listView);
    }


	void updateTopicList(View rootView, ListView topiclist) {

		TopicDataSource datasource = new TopicDataSource(rootView.getContext());
		datasource.open();

		List<Topic> values = datasource.getAllTopics();

		if(values.size() > 0){
			loadingText.setVisibility(View.GONE);
		}

		// use the SimpleCursorAdapter to show the
		// elements in a ListView
		ArrayAdapter<Topic> adapter = new ArrayAdapter<Topic>(
				rootView.getContext(), R.layout.topics_row_layout, values);

		topiclist.setAdapter(adapter);
		datasource.close();
	}

}