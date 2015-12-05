package com.abheri.san.data;

public class Word {
	private long id;
	private String englishword;
	private String sanskritword;
	private String examplesentence;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEnglishWord() {
		return englishword;
	}

	public void setEnglishWord(String word) {
		this.englishword = word;
	}

	public String getSanskritWord() {
		return sanskritword;
	}

	public void setSanskritWord(String word) {
		this.sanskritword = word;
	}

	public String getExampleSentence() {
		return examplesentence;
	}

	public void setExampleSentence(String sentence) {
		this.examplesentence = sentence;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return englishword + ":" + sanskritword;
	}
}