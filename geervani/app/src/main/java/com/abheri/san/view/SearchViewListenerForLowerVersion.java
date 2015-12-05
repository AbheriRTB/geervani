package com.abheri.san.view;

import java.util.List;

import com.abheri.san.data.DataHelper;
import com.abheri.san.data.Word;
import com.abheri.san.data.WordDataSource;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class SearchViewListenerForLowerVersion implements TextWatcher {
	
	private DictionaryArrayAdapter dadapter;
	List<Word> values;
	View rootView = null;
	ListView listView;
	static int searchviewid;
	
	public SearchViewListenerForLowerVersion(View rv, ListView lv, DictionaryArrayAdapter da)
	{
		rootView = rv;
		listView = lv;
		dadapter = da;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		Log.i("PRAS", "in onQueryTextChange");
		if(s != null && s.length() >0)
		{
			
		}
		else
		{
			updateWordList(rootView, listView, null);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
Log.i("PRAS", "in onQueryTextSubmit");
		
		if(s.toString() != null && s.toString().length() >0)
		{
			String whereclause = "WHERE " + DataHelper.COLUMN_ENGLISHWORD + " like '" + s.toString() + "%'";
			updateWordList(rootView, listView, whereclause);
		}
		
	}
	
	void updateWordList(View rootView, ListView wordlist, String whereclause) {
		
		
		Context c = SplashActivity.getAppContext();
		
		WordDataSource datasource = new WordDataSource(c);
		datasource.open();

		List<Word> values;
		if(whereclause == null)
		{
			values = datasource.getAllWords();
		}
		else
		{
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

}
