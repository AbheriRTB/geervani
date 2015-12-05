package com.abheri.san.data;

public class Sentence {
	private long id;
	private String english;
	private String sanskrit;
	private String translit;

	public static int selectedPosition=-1;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEnglish() {
		return english;
	}

	public void setEnglish(String english) {
		this.english = english;
	}

	public String getSanskrit() {
		return sanskrit;
		//return "संस्क्रृतम्";
	}

	public void setSanskrit(String sanskrit) {
		this.sanskrit = sanskrit;
	}

	public String getTranslit() {
		return translit;
	}

	public void setTranslit(String translit) {
		this.translit = translit;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return english;
	}
}