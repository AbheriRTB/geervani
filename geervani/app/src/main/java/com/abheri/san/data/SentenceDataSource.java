package com.abheri.san.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SentenceDataSource {

  // Database fields
  private SQLiteDatabase database;
  private DataHelper dbHelper;
  private String[] allColumns = { DataHelper.COLUMN_SID,
		  DataHelper.COLUMN_ENGLISH,DataHelper.COLUMN_SANSKRIT, DataHelper.COLUMN_TRANSLIT, DataHelper.TOPIC_ID };

  public SentenceDataSource(Context context) {
    dbHelper = new DataHelper(context);
  }
  
  public SentenceDataSource(Context context, SQLiteDatabase db) {
	  dbHelper = new DataHelper(context);  
	  database = db;
	  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    //dbHelper.close();
  }

  public Sentence createSentence(String english, String sanskrit, String translit, int tid) {
    ContentValues values = new ContentValues();
    values.put(DataHelper.COLUMN_ENGLISH, english);
    values.put(DataHelper.COLUMN_SANSKRIT, sanskrit);
    values.put(DataHelper.COLUMN_TRANSLIT, translit);
    values.put(DataHelper.TOPIC_ID, tid);
    long insertId = database.insert(DataHelper.TABLE_SENTENCE, null,
        values);
    Cursor cursor = database.query(DataHelper.TABLE_SENTENCE,
        allColumns, DataHelper.COLUMN_SID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    Sentence newSentence = cursorToSentence(cursor);
    cursor.close();
    return newSentence;
  }

  public void deleteSentence(Sentence sentence) {
    long id = sentence.getId();
    System.out.println("Sentence deleted with id: " + id);
    database.delete(DataHelper.TABLE_SENTENCE, DataHelper.COLUMN_SID
            + " = " + id, null);
  }

  public void deleteAllSentences() {

    int nrows = database.delete(DataHelper.TABLE_SENTENCE, "1", null);
    System.out.println(nrows + " Sentences deleted");
  }

  public List<Sentence> getAllSentences() {
    List<Sentence> sentences = new ArrayList<Sentence>();


    Cursor cursor = database.query(DataHelper.TABLE_SENTENCE,
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Sentence sentence = cursorToSentence(cursor);
      sentences.add(sentence);
      cursor.moveToNext();
    }
    // make sure to close the cursor
    cursor.close();
    return sentences;
  }
  
  public List<Sentence> getAllSentences(long topic_id) {
	    List<Sentence> sentences = new ArrayList<Sentence>();

	    String query = "SELECT * FROM " + DataHelper.TABLE_SENTENCE + " WHERE " + DataHelper.TOPIC_ID + "=" + topic_id + ";";
	    
	    Cursor cursor = database.rawQuery(query, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Sentence sentence = cursorToSentence(cursor);
	      sentences.add(sentence);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return sentences;
	  }

  private Sentence cursorToSentence(Cursor cursor) {
    Sentence sentence = new Sentence();
    sentence.setId(cursor.getLong(0));
    sentence.setEnglish(cursor.getString(1));
    sentence.setSanskrit(cursor.getString(2));
    sentence.setTranslit(cursor.getString(3));
    return sentence;
  }
} 