package com.abheri.san.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.abheri.san.view.SplashActivity;
import com.abheri.san.view.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

public class WordDataCreatorPSV {

	static void createWords(Context context, SQLiteDatabase db) {

		/*String[] word_files = new String[] { "LetterA", "LetterB", "LetterC",
				"LetterD", "LetterE", "LetterF", "LetterG", "LetterH",
				"LetterI", "LetterJ", "LetterK", "LetterK", "LetterL",
				"LetterM", "LetterN", "LetterO", "LetterP", "LetterQ",
				"LetterR", "LetterS", "LetterT", "LetterU", "LetterV",
				"LetterW", "LetterX", "LetterY", "LetterZ", }; */

		String[] word_files = new String[] { "LetterA"};

		WordDataSource wds = new WordDataSource(context, db);

		for (int i = 0; i < word_files.length; ++i) {
			try {
				Log.i("PRAS", "Processing - " + word_files[i]);

				Context c = SplashActivity.getAppContext();
				AssetManager am = c.getAssets();
				InputStream is = am.open("datafiles/dictionary/"
						+ word_files[i] + ".psv");
				InputStreamReader inputStreamReader = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(inputStreamReader);

				String line;

				do {
					line = br.readLine();
					if (line != null) {
						// Log.i("PRAS", line);
						String trmline = line.trim();
						if (!trmline.startsWith("//") && trmline.length() != 0) {
							String spltstr[] = trmline.split("\\|", 2);
							if (spltstr.length == 2) {

								String envalue = spltstr[0].replace("<br>", Util.lineSeparator());
								String snvalue = spltstr[1].replace("<br>", Util.lineSeparator());

								envalue = envalue.trim();
								snvalue = snvalue.trim();

								// Remove the doublequotes
								//envalue = envalue.substring(1, envalue.length() - 2);
								//snvalue = snvalue.substring(1, snvalue.length() - 2);

								//Log.i("PRAS", envalue + "<<<<>>>>>" + snvalue);
								wds.createWord(envalue, snvalue);
							}
						}
					}
				} while (line != null);

			} catch (IOException e) {
				Log.i("PRAS", "IOException occurred");
				Log.i("PRAS", e.toString());
			}
		}

	}

}
