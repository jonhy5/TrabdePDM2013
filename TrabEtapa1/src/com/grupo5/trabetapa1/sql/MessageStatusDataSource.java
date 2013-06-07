package com.grupo5.trabetapa1.sql;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

public class MessageStatusDataSource {
	// Database fields
	private SQLiteDatabase database;
	private SQLiteMessageStatusHelper dbHelper;
	private String[] allColumns = { 
			SQLiteMessageStatusHelper.COLUMN_ID,
			SQLiteMessageStatusHelper.COLUMN_MESSAGE};
	
	public MessageStatusDataSource(Context context) {
	    dbHelper = new SQLiteMessageStatusHelper(context);
	}
	
	public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public MessageStatusModel createMessageStatus(String message) {
		ContentValues values = new ContentValues();
		values.put(SQLiteMessageStatusHelper.COLUMN_MESSAGE, message);
		
		long insertId;
		try {
			insertId = database.insertOrThrow(SQLiteMessageStatusHelper.TABLE_MESSAGESTATUS, null, values);
		} catch (SQLiteConstraintException e) {
			return null;
		}
		
		Cursor cursor = database.query(SQLiteMessageStatusHelper.TABLE_MESSAGESTATUS, allColumns, SQLiteMessageStatusHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		MessageStatusModel newStatus = cursorToStatus(cursor);
	    cursor.close();
	    return newStatus;
	}
	
	public void deleteMessageStatus(MessageStatusModel status) {
	    long id = status.getId();
	    database.delete(SQLiteMessageStatusHelper.TABLE_MESSAGESTATUS, SQLiteMessageStatusHelper.COLUMN_ID + " = " + id, null);
	}
	
	public void delete(String where) {
	    database.delete(SQLiteMessageStatusHelper.TABLE_MESSAGESTATUS, where, null);
	}
	
	public void delete(long id) {
	    database.delete(SQLiteMessageStatusHelper.TABLE_MESSAGESTATUS, SQLiteMessageStatusHelper.COLUMN_ID + "=" + id, null);
	}
	
	public void deleteAll() {
	    delete(null);
	}
	
	public List<MessageStatusModel> getAllStatus() {
	    List<MessageStatusModel> status = new ArrayList<MessageStatusModel>();

	    Cursor cursor = database.query(SQLiteMessageStatusHelper.TABLE_MESSAGESTATUS, allColumns, null, null, null, null, SQLiteMessageStatusHelper.COLUMN_ID + " ASC");
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	MessageStatusModel st = cursorToStatus(cursor);
	    	status.add(st);
	    	cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	    return status;
	}
	
	public int rowCount() {
		Cursor mCount = database.rawQuery("select count(*) from " + SQLiteMessageStatusHelper.TABLE_MESSAGESTATUS, null);
		mCount.moveToFirst();
		int count= mCount.getInt(0);
		mCount.close();
		
		return count;
	}
	
	private MessageStatusModel cursorToStatus(Cursor cursor) {
		MessageStatusModel status = new MessageStatusModel();
		status.setId(cursor.getLong(0));
	    status.setMessage(cursor.getString(1));
	    return status;
	}
}
