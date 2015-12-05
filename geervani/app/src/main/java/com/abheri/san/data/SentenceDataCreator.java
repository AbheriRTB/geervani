package com.abheri.san.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.*;
import java.util.Hashtable;

import com.abheri.san.view.SplashActivity;


public class SentenceDataCreator {

	static void createSentences(Context context, SQLiteDatabase db) {

		SentenceDataSource sds = new SentenceDataSource(context, db);

		int[] topic_ids = new int[] { TopicDataCreator.ETIQUITTE_ID,
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
				TopicDataCreator.GREETINGS_ID };

		String[] topic_files = new String[] { "Etiquettes", "Introduction",
				"MeetingFriends", "Journey", "OnArrival", "Train", "Students",
				"Examination", "Films", "Teachers", "Telephone",
				"DressJewelry", "Commerce", "Weather", "Domestic", "Food",
				"Women", "Time", "Greetings", };

		for (int i = 0; i < topic_files.length; ++i) {
			try {
				Log.i("PRAS","Processing....");
				Log.i("PRAS",topic_files[i]);

				Context c = SplashActivity.getAppContext();
				
				AssetManager am = c.getAssets();
				InputStream is = am.open("datafiles/topics/" + topic_files[i]
						+ ".txt");
				InputStreamReader inputStreamReader = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(inputStreamReader);

				String line;
				Hashtable<String, String> datamap = new Hashtable<String, String>();
				do {
					line = br.readLine();
					if (line != null) {
						// Log.i("PRAS", line);
						String trmline = line.trim();
						if (!trmline.startsWith("/*") && trmline.length() != 0) {
							String spltstr[] = trmline.split(":",2);
							if (spltstr.length == 2) {
								datamap.put(spltstr[0], spltstr[1]);
							}
						}
					}
				} while (line != null);

				// Enumeration<String> enr = datamap.keys();
				// iterate through the collection
				/*
				 * Iterator<String> itr = collection.iterator();
				 * while(itr.hasNext()) Log.i("PRAS", (String)itr.next());
				 */

				/*
				 * for (Enumeration<String> enr = datamap.keys();
				 * enr.hasMoreElements();) { String key = enr.nextElement();
				 * String value = datamap.get(key); Log.i("PRAS", key +
				 * "<<<<>>>>>" + value); }
				 */

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
									topic_ids[i]);
						}

						idx++;
					} else
						break;
				}

			} catch (IOException e) {
				Log.i("PRAS", "IOException occurred");
				Log.i("PRAS", e.toString());
			}
		}
	}

}
