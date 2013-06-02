/**
 * Status Model represents the database table yamb5.status object 
 */
package com.grupo5.trabetapa1.sql;

public class StatusModel {
	private long id;
	private String message;
	private String author;
	private long date;
	
	public StatusModel() {
		this(0, "", "", 0);
	}
	
	public StatusModel(long id, String message, String author, long date) {
		this.id = id;
		this.message = message;
		this.author = author;
		this.date = date;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public long getDate() {
		return date;
	}
	
	public void setDate(long date) {
		this.date = date;
	}
}
