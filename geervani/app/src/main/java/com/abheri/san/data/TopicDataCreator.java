package com.abheri.san.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class TopicDataCreator {

	public static final int ETIQUITTE_ID = 1;
	public static final int INTRODUCTION_ID = 2;
	public static final int MEETING_FRIENDS_ID = 3;
	public static final int JOURNEY_ID = 4;
	public static final int ON_ARRIVAL_ID = 5;
	public static final int TRAIN_ID = 6;
	public static final int SUTDENTS_ID = 7;
	public static final int EXAMINATION_ID = 8;
	public static final int FILMS_ID = 9;
	public static final int TEACHERS_ID = 10;
	public static final int TELEPHONE_ID = 11;
	public static final int DRESS_N_JEWELRY_ID = 12;
	public static final int COMMERCE_ID = 13;
	public static final int WEATHER_ID = 14;
	public static final int DOMESTIC_ID = 15;
	public static final int FOOD_ID = 16;
	public static final int WOMEN_ID = 17;
	public static final int TIME_ID = 18;
	public static final int GREETINGS_ID = 19;

	static void createTopics(Context context, SQLiteDatabase db) {

		int[] topic_ids = new int[] { ETIQUITTE_ID, INTRODUCTION_ID,
				MEETING_FRIENDS_ID, JOURNEY_ID, ON_ARRIVAL_ID, TRAIN_ID,
				SUTDENTS_ID, EXAMINATION_ID, FILMS_ID, TEACHERS_ID,
				TELEPHONE_ID, DRESS_N_JEWELRY_ID, COMMERCE_ID, WEATHER_ID,
				DOMESTIC_ID, FOOD_ID, WOMEN_ID, TIME_ID, GREETINGS_ID };

		String[] topics = new String[] { "Etiquttes", "Introduction",
				"Meeting Friends", "Journey", "On Arrival", "Train",
				"Students", "Examination", "Films", "Teachers", "Telephone",
				"Dress & Jewelry", "Commerce", "Weather", "Domestic", "Food",
				"Women", "Time", "Greetings", };
		
		String[] sanskrit_topics = new String[] { "शिश्ठाचारः", "परिचयः",
				"मित्र मेलनम्", "प्रयाणम्", "प्रवासतः प्रतिनिवर्तनम्", "रेल्यानम्",
				"छात्राः", "परीक्षा", "चलच्चित्रम्", "शिक्षकाः", "दूरभाषा",
				"वेषभूषणानि", "वाणिज्यम्", "वातावर्णम्", "गृहसंभाषणम्", "भोजनम्",
				"स्त्रियः", "समयः", "शुभाशयाः", };

		TopicDataSource tds = new TopicDataSource(context, db);

		for (int i = 0; i < topics.length; ++i)
			tds.createTopic(topic_ids[i], topics[i], sanskrit_topics[i]);
	}

}
