package com.grupo5.trabetapa1.sql;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

public class StatusDataSource {
	// Database fields
	private SQLiteDatabase database;
	private SQLiteStatusHelper dbHelper;
	private String[] allColumns = { 
			SQLiteStatusHelper.COLUMN_ID,
			SQLiteStatusHelper.COLUMN_MESSAGE,
			SQLiteStatusHelper.COLUMN_AUTHOR,
			SQLiteStatusHelper.COLUMN_DATE};
	
	public StatusDataSource(Context context) {
	    dbHelper = new SQLiteStatusHelper(context);
	}
	
	public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public StatusModel createStatus(long id, String message, String author, long date) {
		ContentValues values = new ContentValues();
		values.put(SQLiteStatusHelper.COLUMN_ID, id);
		values.put(SQLiteStatusHelper.COLUMN_MESSAGE, message);
		values.put(SQLiteStatusHelper.COLUMN_AUTHOR, author);
		values.put(SQLiteStatusHelper.COLUMN_DATE, date);
		
		long insertId;
		try {
			insertId = database.insertOrThrow(SQLiteStatusHelper.TABLE_STATUS, null, values);
		} catch (SQLiteConstraintException e) {
			return null;
		}
		
		Cursor cursor = database.query(SQLiteStatusHelper.TABLE_STATUS, allColumns, SQLiteStatusHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		StatusModel newStatus = cursorToStatus(cursor);
	    cursor.close();
	    return newStatus;
	}
	
	public void deleteStatus(StatusModel status) {
	    long id = status.getId();
	    database.delete(SQLiteStatusHelper.TABLE_STATUS, SQLiteStatusHelper.COLUMN_ID + " = " + id, null);
	}
	
	public List<StatusModel> getAllStatus() {
	    List<StatusModel> status = new ArrayList<StatusModel>();

	    Cursor cursor = database.query(SQLiteStatusHelper.TABLE_STATUS, allColumns, null, null, null, null, SQLiteStatusHelper.COLUMN_DATE + " DESC");
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	StatusModel st = cursorToStatus(cursor);
	    	status.add(st);
	    	cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	    return status;
	}
	
	private StatusModel cursorToStatus(Cursor cursor) {
		StatusModel status = new StatusModel();
		status.setId(cursor.getLong(0));
	    status.setMessage(cursor.getString(1));
	    status.setAuthor(cursor.getString(2));
	    status.setDate(cursor.getLong(3));
	    return status;
	}
}
