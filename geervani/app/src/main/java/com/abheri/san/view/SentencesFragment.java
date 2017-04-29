package com.abheri.san.view;

import java.util.List;

import com.abheri.san.R;
import com.abheri.san.data.*;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SentencesFragment extends Fragment{

	View rootView;
	ListView listView;
	long topic_id;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		rootView = inflater.inflate(R.layout.fragment_sentences,
				container, false);
		
		/* Add tabbar programatically to support lower versions */
		if (Util.androidversion < Util.minversioncheck)
		{
			View tabbarview = inflater.inflate(R.layout.tabbar, container, false);
			RelativeLayout rl = (RelativeLayout)rootView;
			rl.addView(tabbarview);
			TabBarListenerForLV tl = new TabBarListenerForLV();
			tl.setTabbarView(tabbarview, TabBarListenerForLV.TOPICS);
		}
		
		Log.i("PRAS", "In SentencesFragment");

		/*--------------------------------
		 * Read the intent and populate the
		 * Topic in the text view
		 */
		Bundle arguments = getArguments();
		topic_id = arguments.getLong("topic_id");
		String topic = arguments.getString("topic");
		String sanskrit = arguments.getString("topicsanskrit");
		String english = topic;

		TextView hv = (TextView) rootView.findViewById(R.id.heading);
		if(Util.androidversion < Util.minversioncheck)
		{
			hv.setText(english);
		}
		else
		{
			hv.setText(english + "(" +sanskrit+ ")");
		}


		// Get ListView object from xml
		listView = (ListView) rootView.findViewById(R.id.sentenceList);
		listView.setOnItemClickListener(new DisplaySentence());

		return rootView;
	}

	@Override
	public void onResume(){
        super.onResume();
		updateSentenceList(rootView, listView, topic_id);
	}
	
	void updateSentenceList(View rootView, ListView topiclist, long topic_id)
	{
		   SentenceDataSource datasource = new SentenceDataSource(rootView.getContext());
		    datasource.open();

		    List<Sentence> values = datasource.getAllSentences(topic_id);

		    // use the SimpleCursorAdapter to show the
		    // elements in a ListView
		    MyListAdapter adapter = new MyListAdapter(rootView.getContext(),
		    		R.layout.sentence_row_layout, values);
		    topiclist.setAdapter(adapter);
		    
		    datasource.close();
	}

}