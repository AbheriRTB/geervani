package com.abheri.san.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FileCacheDataSource {

	// Database fields
	private SQLiteDatabase database;
	private DataHelper dbHelper;
	private Context context;
	private static List<Sentence> ls = null;
	private String[] allColumns = { DataHelper.COLUMN_CID,
			DataHelper.COLUMN_FILENAME, DataHelper.COLUMN_DATECACHED };

	public FileCacheDataSource(Context context) {
		dbHelper = new DataHelper(context);
        database = dbHelper.getWritableDatabase();
        this.context = context;
	}

	public FileCacheDataSource(Context context, SQLiteDatabase db) {
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

	public FileCache createFileCache(String filename, String cachedate) {
		ContentValues values = new ContentValues();
		values.put(DataHelper.COLUMN_FILENAME, filename);
		values.put(DataHelper.COLUMN_DATECACHED, cachedate);

		long insertId = database.insert(DataHelper.TABLE_CACHE, null, values);
		Cursor cursor = database.query(DataHelper.TABLE_CACHE, allColumns,
				DataHelper.COLUMN_CID + " = " + insertId, null, null, null,
				null);
		cursor.moveToFirst();
		FileCache newfilecache = cursorToFileCache(cursor);
		cursor.close();
		return newfilecache;
	}

    public FileCache updateFileCache(String filename, String cachedate) {
        ContentValues values = new ContentValues();
        values.put(DataHelper.COLUMN_FILENAME, filename);
        values.put(DataHelper.COLUMN_DATECACHED, cachedate);

        long insertId = database.update(DataHelper.TABLE_CACHE, values,
                            DataHelper.COLUMN_FILENAME + "='" + filename +"'", null);
        Cursor cursor = database.query(DataHelper.TABLE_CACHE, allColumns,
                DataHelper.COLUMN_CID + " = " + insertId, null, null, null,
                null);
        cursor.moveToFirst();
        FileCache newfilecache = cursorToFileCache(cursor);
        cursor.close();
        return newfilecache;
    }

	public void deleteFileCache(FileCache filecache) {
		long id = filecache.getId();
		System.out.println("FileCache deleted with id: " + id);
		database.delete(DataHelper.TABLE_CACHE, DataHelper.COLUMN_CID + " = "
				+ id, null);
	}

	public List<FileCache> getAllFileCaches() {
		List<FileCache> filecaches = new ArrayList<FileCache>();

		Cursor cursor = database.query(DataHelper.TABLE_CACHE, allColumns, null,
				null, null, null, DataHelper.COLUMN_FILENAME + " ASC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			FileCache filecache = cursorToFileCache(cursor);
			filecaches.add(filecache);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return filecaches;
	}

	public List<FileCache> getFileCache(String filename) {
		List<FileCache> filecaches = new ArrayList<FileCache>();

		String query = "SELECT * FROM " + DataHelper.TABLE_CACHE + " "
				+ "where " + DataHelper.COLUMN_FILENAME + "='" + filename + "';";
		//Log.i("PRAS", "Query=" + query);
		Cursor cursor = database.rawQuery(query, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			FileCache filecache = cursorToFileCache(cursor);
			filecaches.add(filecache);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return filecaches;
	}

	public List<FileCache> getAllFileCaches(String whereclause) {
		List<FileCache> filecaches = new ArrayList<FileCache>();

		String query = "SELECT * FROM " + DataHelper.TABLE_CACHE + " "
				+ whereclause + " order by " + DataHelper.COLUMN_FILENAME
				+ " asc ;";
		Log.i("PRAS", "Query=" + query);
		Cursor cursor = database.rawQuery(query, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			FileCache filecache = cursorToFileCache(cursor);
			filecaches.add(filecache);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return filecaches;
	}

	private FileCache cursorToFileCache(Cursor cursor) {
		FileCache filecache = new FileCache();
		filecache.setId(cursor.getLong(0));
		filecache.setFilename(cursor.getString(1));
		filecache.setCachedate(cursor.getString(2));
		return filecache;
	}


}