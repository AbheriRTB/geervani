package com.abheri.san.view;

import java.util.List;

import com.abheri.san.data.DataHelper;
import com.abheri.san.data.Word;
import com.abheri.san.data.WordDataSource;

import android.annotation.TargetApi;
import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.util.Log;

@TargetApi(19)
public class SearchViewListener  implements
		SearchView.OnQueryTextListener, SearchView.OnCloseListener {

	private DictionaryArrayAdapter dadapter;
	List<Word> values;
	View rootView = null;
	ListView listView;
	static int searchviewid;
	
	public SearchViewListener(View rv, ListView lv, DictionaryArrayAdapter da)
	{
		rootView = rv;
		listView = lv;
		dadapter = da;
	}

	void updateWordList(View rootView, ListView wordlist, String whereclause) {
		Context c = MainActivity.c;
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
		dadapter.addAll(values);
		dadapter.notifyDataSetChanged();
		datasource.close();
	}

	@Override
	public boolean onClose() {
		// TODO Auto-generated method stub
		Log.i("PRAS", "in onClose");
		return false;
	}

	@Override
	public boolean onQueryTextChange(String searchstring) {
		// TODO Auto-generated method stub
		
		Log.i("PRAS", "in onQueryTextChange");
		if(searchstring != null && searchstring.length() >0)
		{
			//return false;
			
			//Try the following for performance. If it is bad, then remove
			String whereclause = "WHERE " + DataHelper.COLUMN_ENGLISHWORD + " like '" + searchstring + "%'";
			updateWordList(rootView, listView, whereclause);
			return true;
		}
		else
		{
			updateWordList(rootView, listView, null);
			return true;
		}
	}

	@Override
	public boolean onQueryTextSubmit(String searchstring) {
		// TODO Auto-generated method stub
		Log.i("PRAS", "in onQueryTextSubmit");
		
		if(searchstring != null && searchstring.length() >0)
		{
			String whereclause = "WHERE " + DataHelper.COLUMN_ENGLISHWORD + " like '" + searchstring + "%'";
			updateWordList(rootView, listView, whereclause);
			return true;
		}
		else
			return false;
	}

}