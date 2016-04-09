package com.abheri.san.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.sql.SQLClientInfoException;

public class DataHelper extends SQLiteOpenHelper {

    public static final String TABLE_TOPIC = "topic";
    public static final String COLUMN_TID = "_id";
    public static final String COLUMN_TOPIC = "topic";
    public static final String COLUMN_TOPICSANSKRIT = "topicsanskrit";

    public static final String TABLE_SENTENCE = "sentence";
    public static final String COLUMN_SID = "_sid";
    public static final String TOPIC_ID = "_tid";
    public static final String COLUMN_ENGLISH = "english";
    public static final String COLUMN_SANSKRIT = "sanskrit";
    public static final String COLUMN_TRANSLIT = "translit";


    public static final String TABLE_WORD = "word_table";
    public static final String COLUMN_WID = "_wid";
    public static final String COLUMN_ENGLISHWORD = "englishword";
    public static final String COLUMN_SANSKRITWORD = "sanskritword";

    public static final String TABLE_CACHE = "cache_table";
    public static final String COLUMN_CID = "_cid";
    public static final String COLUMN_FILENAME = "filename";
    public static final String COLUMN_DATECACHED = "datecached";

    private static final String DATABASE_NAME = "geervani.db";
    private static final int DATABASE_VERSION = 2;

    private Context dbContext;

    //Database creation sql statement

    private static final String topic_table = "create table "
            + TABLE_TOPIC + "(" + COLUMN_TID
            + " integer primary key, "
            + COLUMN_TOPIC
            + " text not null,"
            + COLUMN_TOPICSANSKRIT
            + " text not null);";

    private static final String sentence_table = "create table "
            + TABLE_SENTENCE + "(" + COLUMN_SID
            + " integer primary key autoincrement, "
            + COLUMN_ENGLISH
            + " text not null, "
            + COLUMN_SANSKRIT
            + " text not null, "
            + COLUMN_TRANSLIT
            + " text not null, "
            + TOPIC_ID
            + " integer, "
            + " foreign key( "
            + TOPIC_ID + ") references "
            + TABLE_TOPIC
            + "(" + COLUMN_TID + "));";

    private static final String word_table = "create table "
            + TABLE_WORD + "(" + COLUMN_WID
            + " integer primary key autoincrement, "
            + COLUMN_ENGLISHWORD
            + " text not null, "
            + COLUMN_SANSKRITWORD
            + " text not null );";

    private static final String cache_table = "create table "
            + TABLE_CACHE + "(" + COLUMN_CID
            + " integer primary key autoincrement, "
            + COLUMN_FILENAME
            + " text not null, "
            + COLUMN_DATECACHED
            + " text not null );";

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        dbContext = context;
    }

    public SQLiteDatabase getDatabase() {
        return getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        //database.execSQL(DATABASE_CREATE);
        database.execSQL(topic_table);
        database.execSQL(sentence_table);
        database.execSQL(word_table);
        database.execSQL(cache_table);

        // database.execSQL("INSERT INTO " + TABLE_TOPIC +" ( " + COLUMN_ID + "," + COLUMN_TOPIC + ") "
        // 		   + " Values (" + null + ", '" + "Etiqutte" + "');");


        /*
        TopicDataCreator.createTopics(dbContext, database);

        SentenceDataCreator sdc = new SentenceDataCreator(dbContext, database);
        sdc.createSentences();
        */

        WordDataCreator.createWords(dbContext, database);
        //WordDataCreatorPSV.createWords(dbContext, database);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DataHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        DeleteTopicTable();
        DeleteSentenceTable();
        DeleteWordTable();
        DeleteCacheTable();

        onCreate(db);
    }

    public void DeleteTopicTable() {
        getDatabase().execSQL("DROP TABLE IF EXISTS " + TABLE_TOPIC + ";");
    }

    public void DeleteSentenceTable() {
        getDatabase().execSQL("DROP TABLE IF EXISTS " + TABLE_SENTENCE + ";");
    }

    public void DeleteWordTable() {
        getDatabase().execSQL("DROP TABLE IF EXISTS " + TABLE_WORD + ";");
    }
    public void DeleteCacheTable() {
        getDatabase().execSQL("DROP TABLE IF EXISTS " + TABLE_CACHE + ";");
    }

    public void TruncateSenteceTable() {
        getDatabase().execSQL("DELETE FROM " + TABLE_SENTENCE + ";");
        getDatabase().execSQL("VACUUM;");

    }


} 