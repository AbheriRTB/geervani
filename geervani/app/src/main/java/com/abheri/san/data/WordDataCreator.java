package com.abheri.san.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

import com.abheri.san.view.SplashActivity;
import com.abheri.san.view.Util;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class WordDataCreator {

	static void createWords(Context context, SQLiteDatabase db) {

		String[] word_files = new String[] { "LetterA", "LetterB", "LetterC",
				"LetterD", "LetterE", "LetterF", "LetterG", "LetterH",
				"LetterI", "LetterJ", "LetterK", "LetterK", "LetterL",
				"LetterM", "LetterN", "LetterO", "LetterP", "LetterQ",
				"LetterR", "LetterS", "LetterT", "LetterU", "LetterV",
				"LetterW", "LetterX", "LetterY", "LetterZ", };

		WordDataSource wds = new WordDataSource(context, db);

		for (int i = 0; i < word_files.length; ++i) {
			try {
				Log.i("PRAS", "Processing - " + word_files[i]);

				Context c = SplashActivity.getAppContext();
				AssetManager am = c.getAssets();
				InputStream is = am.open("datafiles/dictionary/"
						+ word_files[i] + ".txt");
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
							String spltstr[] = trmline.split(":", 2);
							if (spltstr.length == 2) {
								datamap.put(spltstr[0], spltstr[1]);
							}
						}
					}
				} while (line != null);

				int idx;
				int nrecords = datamap.size();
				for(idx=1; idx<nrecords+1; ++idx) {
					String enkey = "key" + idx + "_en";
					String snkey = "key" + idx + "_sn";

					String othenvalue = datamap.get(enkey);
					String othsnvalue = datamap.get(snkey);
					if (othenvalue != null && othsnvalue != null) {

						String envalue = othenvalue.replace("<br>", Util.lineSeparator());
						String snvalue = othsnvalue.replace("<br>",
								Util.lineSeparator());

						envalue = envalue.trim();
						snvalue = snvalue.trim();

						// Remove the doublequotes
						envalue = envalue.substring(1, envalue.length() - 2);
						snvalue = snvalue.substring(1, snvalue.length() - 2);

						//Log.i("PRAS", envalue + "<<<<>>>>>" + snvalue);
						wds.createWord(envalue, snvalue);
					}
				}

			} catch (IOException e) {
				Log.i("PRAS", "IOException occurred");
				Log.i("PRAS", e.toString());
			}
		}

	}

}
