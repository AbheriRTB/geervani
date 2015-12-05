package com.abheri.san.view;

import java.util.ArrayList;
import java.util.List;

import com.abheri.san.data.Sentence;
import com.abheri.san.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyListAdapter extends ArrayAdapter<Sentence> {

	List<Sentence> sentences;
	private View oldSelection = null;

	public MyListAdapter(Context context, int resource,
			List<Sentence> sentencelist) {
		super(context, resource, sentencelist);
		this.sentences = sentencelist;
		// TODO Auto-generated constructor stub
	}

	ArrayList<Sentence> sentencelist;
	Activity currentActivity;
	final MyListAdapter self = this;

	public void update() {
		currentActivity.runOnUiThread(new Runnable() {
			public void run() {
				self.notifyDataSetChanged();
			}
		});
	}

	public void clearSelection() {
		if (oldSelection != null) {
			oldSelection.setBackgroundColor(android.R.color.holo_blue_light);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder = null;

		if (v == null) {

			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.sentence_row_layout, null);

			holder = new ViewHolder();
			holder.textView = (TextView) v.findViewById(R.id.englishSentence);
			
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		Sentence currentsentence = this.sentences.get(position);
		String curEnglish = currentsentence.getEnglish();

		if (position == Sentence.selectedPosition)
		{
			v.setBackgroundColor(v.getResources().getColor(R.color.teal));
		}
		else
			v.setBackgroundColor(android.R.color.white);
		
		holder.textView.setText(curEnglish);

		return v;
	}

	public void onClick(int position, View view) {
		// this log always reports the correct position when i select a list
		// item
		// Log.i(new Integer(position).toString(), books.get(position).title);

		/*
		 * if(selected != null) {
		 * selected.setBackgroundResource(R.color.holo_green_light); } selected
		 * = view; selected.setBackgroundResource(R.color.transparent);
		 */}

	private class OnItemClickListener implements OnClickListener {
		private int mPosition;

		OnItemClickListener(int position) {
			mPosition = position;
		}

		@Override
		public void onClick(View view) {
			MyListAdapter.this.onClick(mPosition, view);
		}
	}

	private static class ViewHolder {
		TextView textView;
	}
}
