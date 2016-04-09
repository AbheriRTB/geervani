package com.abheri.san.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.abheri.san.view.Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by prasanna.ramaswamy on 23/02/16.
 */
public class DataFileCopier {

    public final String GEERVANI_BASE_DIRECTORY = "geervani";
    public final String TOPIC_DIRECTORY = "topics";
    public final String DICTIONARY_DIRECTORY = "dictionary";

    public FileCacheDataSource fcds;
    private Context context;
    private DataHelper dbHelper;
    private SQLiteDatabase database;

    public static boolean isSDCardAvailable(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }

        return false;
    }

    public DataFileCopier(Context ctx){
        context = ctx;
        fcds = new FileCacheDataSource(ctx);
    }

    public File getFileOnSDCardForReading(String filename, String subdir) {

        File file = null;

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //handle case of no SDCARD present
        } else {
            String dir = Environment.getExternalStorageDirectory() + File.separator +
                    GEERVANI_BASE_DIRECTORY + File.separator +
                    subdir + File.separator;
            //create file
            System.out.println("File:"+dir+filename);
            file = new File(dir, filename);
        }

        return file;
    }

    public File createNewDataFile(String filename, String subdir) {

        File file = null;

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //handle case of no SDCARD present
        } else {
            String dir = Environment.getExternalStorageDirectory() + File.separator +
                    GEERVANI_BASE_DIRECTORY + File.separator +
                    subdir + File.separator;
            //create folder
            File folder = new File(dir); //folder name
            folder.mkdirs();

            //create file
            System.out.println("File:"+dir+filename);
            file = new File(dir, filename);
        }

        return file;
    }



    public void copyTopicFiles() {

        Boolean hasAtleastOneFileChanged = false;
        //Do nothing if SD card is not available
        if(!isSDCardAvailable())
            return;

        String topic_files[] = Util.topic_files;
        String newline = System.getProperty("line.separator");

        for(int i=0; i<topic_files.length; ++i) {

            try {

                // Create a URL for the desired page
                String urlString = Util.getServiceUrl()+TOPIC_DIRECTORY + "/" + topic_files[i]+".txt";

                /* CHeck if the file has changed since last download
                If file has not changed don't do anything. If changed
                download the file to local and refresh the database
                 */

                URLConnection urlConnection = new URL(urlString).openConnection();
                String lastModified = urlConnection.getHeaderField("Last-Modified");
                System.out.println(">>>>>>>>>>>>>>>>>" + lastModified);
                Date modDate = getDateFromString(lastModified);
                System.out.println("^^^^^^^^^^"+ modDate);
                Boolean isOld = isCacheOld(modDate, topic_files[i]);
                if(!isOld){
                    continue; //File has not changed check the next file in the list
                }

                //Download the file from web and store it in local SD Card
                File topicFile = createNewDataFile(topic_files[i] + ".txt", TOPIC_DIRECTORY);
                OutputStream os = (OutputStream) new FileOutputStream(topicFile);
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                URL url = new URL(urlString);

                // Read all the text returned by the server
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                String str;
                while ((str = in.readLine()) != null) {
                    // str is one line of text; readLine() strips the newline character(s)
                    out.write(str + newline);
                }
                in.close();
                out.close();

                if(isOld){
                    insertFileCacheInfo(topic_files[i]);
                    hasAtleastOneFileChanged = true;
                }

            } catch (MalformedURLException e) {
                System.out.println("MalformedURLException");
                e.printStackTrace();


            } catch (FileNotFoundException e){
                System.out.println("FileNotFoundException");
                e.printStackTrace();

            } catch (IOException e) {
                System.out.println("IOException");
                e.printStackTrace();

            }
        }

        //Create Database Entries
        if(hasAtleastOneFileChanged) {
            createTopicAndSentenceData();
        }

    }

    void insertFileCacheInfo(String filename){
        //Mon, 30 Jun 2014 06:17:16 GMT
        Calendar cal = Calendar.getInstance();
        Date curDate = cal.getTime();

        DateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        String cacheDate = sdf.format(curDate).toString();

        List<FileCache> fcl = fcds.getFileCache(filename);
        if(fcl != null) {
           if(fcl.size() != 0) {
                fcds.updateFileCache(filename, cacheDate);
            }else{
                fcds.createFileCache(filename, cacheDate);
            }
        }
    }

    Boolean isCacheOld(Date modDate, String filename){

        String cacheDateStr;
        Boolean isOld = true;

        List<FileCache> fcl = fcds.getFileCache(filename);
        if(fcl != null && fcl.size() == 1) {
            cacheDateStr = fcl.get(0).getCachedate();
            Date cacheDate = getDateFromString(cacheDateStr);
            if (cacheDate.compareTo(modDate) > 0) {
                isOld = false;
                System.out.println("Cache is New:" + filename);
            }
        }

        return isOld;
    }


    Date getDateFromString(String dateStr){

        Calendar cal;
        String day, month, year, hour, min, sec, tz, tmptime;
        HashMap<String, Integer> monthMap = new HashMap<String, Integer>();
        monthMap.put("JAN", 1);
        monthMap.put("FEB", 2);
        monthMap.put("MAR", 3);
        monthMap.put("APR", 4);
        monthMap.put("MAY", 5);
        monthMap.put("JUN", 6);
        monthMap.put("JUL", 7);
        monthMap.put("AUG", 8);
        monthMap.put("SEP", 9);
        monthMap.put("OCT", 10);
        monthMap.put("NOV", 11);
        monthMap.put("DEC", 12);


        //Mon, 30 Jun 2014 06:17:16 GMT

        cal = Calendar.getInstance();
        String parts[] = dateStr.split("\\s");
        if(parts.length == 6) {
            day = parts[1];
            month = parts[2].toUpperCase();
            year = parts[3];

            tmptime = parts[4];
            tz = parts[5];

            String tparts[]= tmptime.split(":");
            if(tparts.length == 3){
                hour = tparts[0];
                min = tparts[1];
                sec = tparts[2];

                cal.set(Integer.parseInt(year),
                        monthMap.get(month).intValue(),
                        Integer.parseInt(day),
                        Integer.parseInt(hour),
                        Integer.parseInt(min),
                        Integer.parseInt(sec)
                );
                cal.setTimeZone(TimeZone.getTimeZone(tz));
            }

        }

        return cal.getTime();
    }

    public void createTopicAndSentenceData(){
        dbHelper = new DataHelper(context);
        database = dbHelper.getWritableDatabase();

        System.out.println("ReCreating Topic & Sentence Data");
        TopicDataCreator.createTopics(context, database);

        SentenceDataCreator sdc = new SentenceDataCreator(context, database);
        sdc.createSentences();
    }

}


