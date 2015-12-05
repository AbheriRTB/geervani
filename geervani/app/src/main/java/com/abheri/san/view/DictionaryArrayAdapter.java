package com.abheri.san.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.abheri.san.R;
import com.abheri.san.data.Word;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.util.Log;

public class DictionaryArrayAdapter extends ArrayAdapter<Word> implements
		SectionIndexer  {

	private Context context;
	private int layoutResourceId;
	List<Word> words;
	List<Word> originalValues;
	List<Word> arrayList;
	ArrayList<Word> myElements;
	
	HashMap<String, Integer> azIndexer;
	String[] sections;

	public DictionaryArrayAdapter(Context context, int layoutResourceId,
			List<Word> words) {
		super(context, layoutResourceId, words);

		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.words = words;
		this.originalValues = words;

		myElements = (ArrayList<Word>) words;
		azIndexer = new HashMap<String, Integer>(); // stores the positions for
													// the start of each letter

		int size = myElements.size();
		for (int i = size - 1; i >= 0; i--) {
			Word element = (Word) myElements.get(i);
			String thisword = element.getEnglishWord();
			// We store the first letter of the word, and its index.
			//Log.i("DA", i + "=" + thisword);
			if (thisword != null && thisword.length() >= 1)
				azIndexer.put(thisword.substring(0, 1), i);
			else
				azIndexer.put(" ", i);
		}

		Set<String> keys = azIndexer.keySet(); // set of letters

		Iterator<String> it = keys.iterator();
		ArrayList<String> keyList = new ArrayList<String>();

		while (it.hasNext()) {
			String key = it.next();
			keyList.add(key);
		}
		Collections.sort(keyList);// sort the keylist
		sections = new String[keyList.size()]; // simple conversion to array
		keyList.toArray(sections);

	}

	@Override
	public int getCount() {
		return words.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		ViewHolder holder = null;

		if (row == null) {

			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new ViewHolder();
			holder.engText = (TextView) row.findViewById(R.id.englishWordView);
			holder.sanText = (TextView) row.findViewById(R.id.sanskritWordView);
			holder.exampleText = (TextView) row
					.findViewById(R.id.exampleWordView);
			
			if(Util.androidversion < Util.minversioncheck)
			{
				Typeface font= Util.getLocalFont();
				holder.engText.setTypeface(font);
				holder.sanText.setTypeface(font);
				holder.exampleText.setTypeface(font);
			}
			
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}
		Word currentword = words.get(position);

		holder.engText.setText(currentword.getEnglishWord());
		holder.sanText.setText(currentword.getSanskritWord());
		String example = currentword.getExampleSentence();
		if (example != null && example.length() > 0) {
			holder.exampleText.setText("Example: " + Util.lineSeparator()
					+ example);
		} else
			holder.exampleText.setText("");

		/*Log.i("DA",
				"*** " + position + ":" + currentword.getEnglishWord() + "="
						+ currentword.getSanskritWord() + "="
						+ currentword.getExampleSentence());*/

		return row;
	}

	private static class ViewHolder {
		TextView engText, sanText, exampleText;
	}

	public int getPositionForSection(int section) {
		String letter = sections[section];
		return azIndexer.get(letter);
	}

	public int getSectionForPosition(int position) {
		Log.v("getSectionForPosition", "called");
		for (int i = sections.length - 1; i >= 0; i--) {
			if (position >= azIndexer.get(sections[i])) {
				return i;
			}
		}
		return 0;
	}

	public Object[] getSections() {
		return sections; // to string will be called to display the letter
	}
}