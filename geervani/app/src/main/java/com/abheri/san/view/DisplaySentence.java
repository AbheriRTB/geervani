package com.abheri.san.view;

import com.abheri.san.R;
import com.abheri.san.data.*;
import com.abheri.san.view.MyListAdapter;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.graphics.Typeface;
import android.util.Log;

public class DisplaySentence implements OnItemClickListener {

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		// ListView Clicked item value
		// Call notifyDataSetChanged as the background color was set in
		// MyListAdapter
		MyListAdapter adapter = (MyListAdapter) parent.getAdapter();
		adapter.notifyDataSetChanged();

		Sentence itemValue = (Sentence) parent.getItemAtPosition(position);

		String sanskrit = itemValue.getSanskrit();
		String translit = itemValue.getTranslit();

		Sentence.selectedPosition = position;

		Log.i("PRAS", "**************Sanskrit is " + sanskrit);

		TextView tv = (TextView) view.getRootView().findViewById(
				R.id.sentenceSanskritView);

		if (Util.androidversion < Util.minversioncheck) {
			Typeface font = Util.getLocalFont();
			tv.setTypeface(font);
		}

		tv.setText(sanskrit);

		TextView ev = (TextView) view.getRootView().findViewById(
				R.id.sentenceEnglishView);
		ev.setText(translit);

	}

}
