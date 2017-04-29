package com.abheri.san.data;

public class FileCache {
	private long id;
	private String filename;
	private String cachedate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String fname) {
		this.filename = fname;
	}

	public String getCachedate() {
		return cachedate;
	}

	public void setCachedate(String cdate) {
		this.cachedate = cdate;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return filename + ":" + cachedate;
	}
}