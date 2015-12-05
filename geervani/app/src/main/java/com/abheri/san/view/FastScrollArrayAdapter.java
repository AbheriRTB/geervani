package com.abheri.san.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.abheri.san.data.Sentence;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;

class FastScrollArrayAdapter extends ArrayAdapter<Sentence> implements
		SectionIndexer {
	ArrayList<Sentence> myElements;
	HashMap<String, Integer> azIndexer;
	String[] sections;

	public FastScrollArrayAdapter(Context context, int textViewResourceId,
			List<Sentence> objects) {
		super(context, textViewResourceId, objects);
		myElements = (ArrayList<Sentence>) objects;
		azIndexer = new HashMap<String, Integer>(); // stores the positions for
													// the start of each letter

		int size = myElements.size();
		for (int i = size - 1; i >= 0; i--) {
			Sentence element = (Sentence)myElements.get(i);
			// We store the first letter of the word, and its index.
			azIndexer.put(element.getEnglish().substring(0, 1), i);
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
												// keyList.toArray(sections);
	}

	public int getPositionForSection(int section) {
		String letter = sections[section];
		return azIndexer.get(letter);
	}

	public int getSectionForPosition(int position) {
		Log.v("getSectionForPosition", "called");
		return 0;
	}

	public Object[] getSections() {
		return sections; // to string will be called to display the letter
	}
}