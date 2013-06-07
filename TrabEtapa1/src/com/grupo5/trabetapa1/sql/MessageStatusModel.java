/**
 * Message Status Model represents the database table yamb5.messagestatus object 
 */
package com.grupo5.trabetapa1.sql;

public class MessageStatusModel {
	private long id;
	private String message;
	
	public MessageStatusModel() {
		this(0, "");
	}
	
	public MessageStatusModel(long id, String message) {
		this.id = id;
		this.message = message;
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
}
