package com.abheri.san.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TopicDataSource {

  // Database fields
  private SQLiteDatabase database;
  private DataHelper dbHelper;
  private String[] allColumns = { DataHelper.COLUMN_TID,
		  DataHelper.COLUMN_TOPIC, DataHelper.COLUMN_TOPICSANSKRIT };

  public TopicDataSource(Context context) {
    dbHelper = new DataHelper(context);
  }
  
  public TopicDataSource(Context context, SQLiteDatabase db) {
	  dbHelper = new DataHelper(context);  
	  database = db;
	  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public Topic createTopic(int tid, String topic, String topicsanskrit) {
    ContentValues values = new ContentValues();
    values.put(DataHelper.COLUMN_TID, tid);
    values.put(DataHelper.COLUMN_TOPIC, topic);
    values.put(DataHelper.COLUMN_TOPICSANSKRIT, topicsanskrit);
    long insertId = database.insert(DataHelper.TABLE_TOPIC, null,
        values);
    Cursor cursor = database.query(DataHelper.TABLE_TOPIC,
        allColumns, DataHelper.COLUMN_TID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    Topic newTopic = cursorToTopic(cursor);
    cursor.close();
    return newTopic;
  }

  public void deleteTopic(Topic topic) {
    long id = topic.getId();
    System.out.println("Topic deleted with id: " + id);
    database.delete(DataHelper.TABLE_TOPIC, DataHelper.COLUMN_TID
            + " = " + id, null);
  }

  public void deleteAllTopics() {
    int nrows = database.delete(DataHelper.TABLE_TOPIC, "1" , null);
      System.out.println(nrows + " Topics deleted");
  }

  public List<Topic> getAllTopics() {
    List<Topic> topics = new ArrayList<Topic>();

    Cursor cursor = database.query(DataHelper.TABLE_TOPIC,
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Topic topic = cursorToTopic(cursor);
      topics.add(topic);
      cursor.moveToNext();
    }
    // make sure to close the cursor
    cursor.close();
    return topics;
  }

  private Topic cursorToTopic(Cursor cursor) {
    Topic topic = new Topic();
    topic.setId(cursor.getLong(0));
    topic.setTopic(cursor.getString(1));
    topic.setTopicSanskrit(cursor.getString(2));
    return topic;
  }
} 