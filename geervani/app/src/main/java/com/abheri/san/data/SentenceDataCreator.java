package com.abheri.san.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.*;
import java.util.Hashtable;

import com.abheri.san.view.SplashActivity;
import com.abheri.san.view.Util;


public class SentenceDataCreator {

    Context context;
    SQLiteDatabase db;
    SentenceDataSource sds;

    int[] topic_ids = new int[]{TopicDataCreator.ETIQUITTE_ID,
            TopicDataCreator.INTRODUCTION_ID,
            TopicDataCreator.MEETING_FRIENDS_ID,
            TopicDataCreator.JOURNEY_ID, TopicDataCreator.ON_ARRIVAL_ID,
            TopicDataCreator.TRAIN_ID, TopicDataCreator.SUTDENTS_ID,
            TopicDataCreator.EXAMINATION_ID, TopicDataCreator.FILMS_ID,
            TopicDataCreator.TEACHERS_ID, TopicDataCreator.TELEPHONE_ID,
            TopicDataCreator.DRESS_N_JEWELRY_ID,
            TopicDataCreator.COMMERCE_ID, TopicDataCreator.WEATHER_ID,
            TopicDataCreator.DOMESTIC_ID, TopicDataCreator.FOOD_ID,
            TopicDataCreator.WOMEN_ID, TopicDataCreator.TIME_ID,
            TopicDataCreator.GREETINGS_ID};

    public SentenceDataCreator(Context context, SQLiteDatabase sdb) {
        this.context = context;
        this.db = sdb;
    }


    public void createSentences() {

        sds = new SentenceDataSource(context, db);
        sds.deleteAllSentences();

        String[] topic_files = Util.topic_files;

        for (int i = 0; i < topic_files.length; ++i) {
            try {
                Log.i("PRAS", "Processing....");
                Log.i("PRAS", topic_files[i]);

                Context c = SplashActivity.getAppContext();

                InputStream is;
                InputStreamReader inputStreamReader;
                if (DataFileCopier.isSDCardAvailable()) {

                    //Read the files from SD card
                    System.out.println("****READING FROM SD CARD****");
                    DataFileCopier dfc = new DataFileCopier(context);
                    File infile = dfc.getFileOnSDCardForReading(topic_files[i] + ".txt", dfc.TOPIC_DIRECTORY);
                    is = new FileInputStream(infile);
                    inputStreamReader = new InputStreamReader(is, "UTF-8");

                } else {

                    //Read from the assets directory
                    System.out.println("****READING FROM ASSETS****");
                    AssetManager am = c.getAssets();
                    is = am.open("datafiles/topics/" + topic_files[i] + ".txt");
                    inputStreamReader = new InputStreamReader(is);
                }

                ProcessFileData(inputStreamReader, i);


            } catch (IOException e) {
                Log.i("PRAS", "IOException occurred");
                Log.i("PRAS", e.toString());
            }
        }
    }

    void ProcessFileData(InputStreamReader inputStreamReader, int topicIndex) throws IOException {
        BufferedReader br = new BufferedReader(inputStreamReader);

        String line;
        Hashtable<String, String> datamap = new Hashtable<String, String>();
        do {
            line = br.readLine();
            if (line != null) {
                // Log.i("PRAS", line);
                String trmline = line.trim();
                if (!trmline.startsWith("/*") && trmline.length() != 0) {
                    String spltstr[] = trmline.split(":", 2);
                    if (spltstr.length == 2) {
                        datamap.put(spltstr[0], spltstr[1]);
                    }
                }
            }
        } while (line != null);

        int idx = 1;
        while (true) {
            String enkey = "key" + idx + "_en";
            String snkey = "key" + idx + "_sn";

            String envalue = datamap.get(enkey);
            String othvalue = datamap.get(snkey);
            if (envalue != null && othvalue != null) {

                String spltval[] = othvalue.split("<br>");
                if (spltval.length == 2) {
                    envalue = envalue.trim();
                    String snvalue = spltval[0].trim();
                    String translitvalue = spltval[1].trim();

                    // Remove the doublequotes
                    envalue = envalue
                            .substring(1, envalue.length() - 2); // trim
                    // double
                    // quote
                    // and
                    // a
                    // comma
                    // in
                    // the
                    // end
                    snvalue = snvalue.substring(1);
                    translitvalue = translitvalue.substring(0,
                            translitvalue.length() - 2);// trim double
                    // quote and a
                    // comma in the
                    // end

                    //Log.i("PRAS", envalue + "<<<<>>>>>" + snvalue
                    //		+ "<<<<>>>>>" + translitvalue);
                    sds.createSentence(envalue, snvalue, translitvalue,
                            topic_ids[topicIndex]);
                }

                idx++;
            } else
                break;
        }

    }
}