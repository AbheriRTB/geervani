package com.abheri.san.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.abheri.san.view.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class WordDataSource {

	// Database fields
	private SQLiteDatabase database;
	private DataHelper dbHelper;
	private Context context;
	private static List<Sentence> ls = null;
	private String[] allColumns = { DataHelper.COLUMN_WID,
			DataHelper.COLUMN_ENGLISHWORD, DataHelper.COLUMN_SANSKRITWORD };

	public WordDataSource(Context context) {
		dbHelper = new DataHelper(context);
		this.context = context;
	}

	public WordDataSource(Context context, SQLiteDatabase db) {
		dbHelper = new DataHelper(context);
		database = db;
		this.context = context;
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Word createWord(String english, String sanskrit) {
		ContentValues values = new ContentValues();
		values.put(DataHelper.COLUMN_ENGLISHWORD, english);
		values.put(DataHelper.COLUMN_SANSKRITWORD, sanskrit);

		long insertId = database.insert(DataHelper.TABLE_WORD, null, values);
		Cursor cursor = database.query(DataHelper.TABLE_WORD, allColumns,
				DataHelper.COLUMN_WID + " = " + insertId, null, null, null,
				null);
		cursor.moveToFirst();
		Word newWord = cursorToWord(cursor);
		cursor.close();
		return newWord;
	}

	public void deleteWord(Word word) {
		long id = word.getId();
		System.out.println("Word deleted with id: " + id);
		database.delete(DataHelper.TABLE_WORD, DataHelper.COLUMN_WID + " = "
				+ id, null);
	}

	public List<Word> getAllWords() {
		List<Word> words = new ArrayList<Word>();

		Cursor cursor = database.query(DataHelper.TABLE_WORD, allColumns, null,
				null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Word word = cursorToWord(cursor);
			words.add(word);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return words;
	}

	public List<Word> getAllWords(String whereclause, boolean getexample) {
		List<Word> words = new ArrayList<Word>();

		String query = "SELECT * FROM " + DataHelper.TABLE_WORD + " "
				+ whereclause + " order by " + DataHelper.COLUMN_ENGLISHWORD
				+ " asc ;";
		Log.i("PRAS", "Query=" + query);
		Cursor cursor = database.rawQuery(query, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Word word = cursorToWord(cursor);

			if (getexample) {
				String thisword = word.getEnglishWord();
				String example = searchForExample(thisword);
				word.setExampleSentence(example);
				//Log.i("kkk", thisword + "=" + example);
			}
			words.add(word);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return words;
	}

	private Word cursorToWord(Cursor cursor) {
		Word word = new Word();
		word.setId(cursor.getLong(0));
		word.setEnglishWord(cursor.getString(1));
		word.setSanskritWord(cursor.getString(2));
		return word;
	}

	public String searchForExample(String word) {
		String example;

		String query = "SELECT * FROM " + DataHelper.TABLE_SENTENCE + " WHERE "
				+ DataHelper.COLUMN_ENGLISH + " like '%" + word
				+ "%' order by " + DataHelper.COLUMN_ENGLISH + " ;";

		Cursor cursor = database.rawQuery(query, null);
		int nrecords = cursor.getCount();
		if (nrecords <= 0)
		{
			cursor.close();
			return "";
		}


		// Get Randon index between 0 and nrecords
		// Get a random example everytime
		Random rand = new Random();
		int rindex = rand.nextInt((nrecords - 0));

		//cursor.moveToFirst();
		cursor.moveToPosition(rindex);
		// Get the sanskrit string
		// example = cursor.getString(2);
		example = cursor.getString(1) + Util.lineSeparator();
		example += cursor.getString(2) + Util.lineSeparator();
		example += cursor.getString(3);

		cursor.close();

		return example;

	}

	public String searchForExampleUsingSentences(String word) {
		String example;

		SentenceDataSource sds = new SentenceDataSource(context, database);
		if (ls == null) {
			ls = sds.getAllSentences();
		}
		List<Integer> match = new ArrayList<Integer>();
		String english;
		String regex = "(.*)" + word + "(.*)";

		for (int i = 0; i < ls.size(); ++i) {
			english = ls.get(i).getEnglish();

			if (english != null && english.matches(regex)) {
				match.add(new Integer(i));
			}
		}

		int nrecords = match.size();
		if (nrecords <= 0)
			return "";

		// Get Randon index between 0 and nrecords
		// Get a random example everytime
		Random rand = new Random();
		int rindex = rand.nextInt((nrecords - 0));

		// Get the sanskrit string
		int idx = (int) match.get(rindex);
		Sentence ms = ls.get(idx);

		example = ms.getEnglish() + Util.lineSeparator();
		example += ms.getSanskrit() + Util.lineSeparator();
		example += ms.getTranslit();

		return example;

	}

}