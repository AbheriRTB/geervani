package com.abheri.san.view;

import java.util.List;

import com.abheri.san.R;
import com.abheri.san.data.Word;
import com.abheri.san.data.WordDataSource;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;


public class DictionaryFragment extends Fragment implements OnClickListener {

	private WordDataSource datasource;
	private DictionaryArrayAdapter dadapter;
	List<Word> values;
	View rootView;
	ListView listView;
	static int searchviewid;
	FragmentManager fm;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		fm = getFragmentManager();
		
		rootView = inflater.inflate(R.layout.fragment_dictionary, container,
				false);
		RelativeLayout rl = (RelativeLayout)rootView;
		
		Button popButton = (Button) rootView.findViewById(R.id.openpopup);
		popButton.setOnClickListener(this);
		
		
		/* Add tabbar programatically to support lower versions */
		if (Util.androidversion < Util.minversioncheck)
		{
			View tabbarview = inflater.inflate(R.layout.tabbar, container, false);
			rl.addView(tabbarview);
			TabBarListenerForLV tl = new TabBarListenerForLV();
			tl.setTabbarView(tabbarview, TabBarListenerForLV.DICTIONARY);
		}

		// Get ListView object from xml
		listView = (ListView) rootView.findViewById(R.id.wordsList);
		listView.setFastScrollEnabled(true);

		// Set the adapter

		datasource = new WordDataSource(rootView.getContext());
		datasource.open();
		values = datasource.getAllWords();
		dadapter = new DictionaryArrayAdapter(rootView.getContext(),
				R.layout.word_row_layout, values);
		listView.setAdapter(dadapter);
		datasource.close();

		// listView.setOnItemClickListener(new DisplaySentence());

		// prepare the SearchView
		// searchView = (SearchView) rootView.findViewById(R.id.search_view);
		if (Util.androidversion >= Util.minversioncheck)
			addSearchView(rootView, rl);
		else
			addSearchViewForLowerVersion(rootView, rl);
		return rootView;
	}

	@TargetApi(11)
	private void addSearchView(View rootView, RelativeLayout rl) {

		SearchView sv = new SearchView(rootView.getContext());
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		//lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		lp.addRule(RelativeLayout.BELOW, R.id.tabbar);

		//searchviewid = View.generateViewId();
		searchviewid = 0xf8f8;
		sv.setId(searchviewid);
		sv.setQueryHint("Enter a word to search");

		RelativeLayout.LayoutParams llp = (RelativeLayout.LayoutParams) listView
				.getLayoutParams();
		llp.addRule(RelativeLayout.BELOW, searchviewid);
		listView.setLayoutParams(llp);

		// Sets the default or resting state of the search field. If true, a
		// single search icon is shown by default and
		// expands to show the text field and other buttons when pressed. Also,
		// if the default state is iconified, then it
		// collapses to that state when the close button is pressed. Changes to
		// this property will take effect immediately.
		// The default value is true.
		sv.setIconifiedByDefault(false);

		SearchViewListener svl = new SearchViewListener(rootView, listView,
				dadapter);
		sv.setOnQueryTextListener(svl);
		sv.setOnCloseListener(svl);

		rl.addView(sv, lp);

	}
	
	private void addSearchViewForLowerVersion(View rootView, RelativeLayout rl) {

		EditText sv = new EditText(rootView.getContext());
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		//lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		lp.addRule(RelativeLayout.BELOW, R.id.tabbar);

		//searchviewid = View.generateViewId();
		searchviewid = 0xf8f8;
		sv.setId(searchviewid);
		sv.setHint("Enter a word to search");

		RelativeLayout.LayoutParams llp = (RelativeLayout.LayoutParams) listView
				.getLayoutParams();
		llp.addRule(RelativeLayout.BELOW, searchviewid);
		listView.setLayoutParams(llp);

		sv.addTextChangedListener(new SearchViewListenerForLowerVersion(rootView, listView, dadapter));

		rl.addView(sv, lp);

	}

	void updateWordList(View rootView, ListView wordlist, String whereclause) {
		WordDataSource datasource = new WordDataSource(rootView.getContext());
		datasource.open();

		List<Word> values;
		if (whereclause == null) {
			values = datasource.getAllWords();
		} else {
			values = datasource.getAllWords(whereclause, true);
		}

		dadapter.clear();
		if(Util.androidversion >= Util.minversioncheck)
		{
			dadapter.addAll(values);
		}
		else
		{
			for(int i=0; i<values.size(); ++i)
				dadapter.add(values.get(i));
		}
		dadapter.notifyDataSetChanged();
		datasource.close();
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.openpopup) {
			//popupMessage.showAsDropDown(popupButton, 0, 0);
			PopupDialog dFragment = new PopupDialog();
            // Show DialogFragment
            dFragment.show(fm, getString(R.string.legend));
        }
		else {
			//popupMessage.dismiss();

		}

	}

}