package com.abheri.san.data;

import com.abheri.san.view.Util;


public class Topic {
  private long id;
  private String topic;
  private String topicsanskrit;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }
  
  public String getTopicSanskrit() {
	    return topicsanskrit;
	  }

	  public void setTopicSanskrit(String topicsanskrit) {
	    this.topicsanskrit = topicsanskrit;
	  }

  // Will be used by the ArrayAdapter in the ListView
  @Override
  public String toString() {
	  
	  if(Util.androidversion < Util.minversioncheck)
	  {
		    return topic;
	  }
	  else
	  {
		    return topic + "  (" + topicsanskrit + ")";
	  }

  }
}