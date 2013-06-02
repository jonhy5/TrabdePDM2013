package com.grupo5.trabetapa1.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteStatusHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "yamb5.db";
	private static final int DATABASE_VERSION = 1;
	public static final String TABLE_STATUS = "status";
	
	// Columns
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_MESSAGE = "message";
	public static final String COLUMN_AUTHOR = "author";
	public static final String COLUMN_DATE = "date";
	
	// Database creation SQLlite statement
	private static final String DATABASE_CREATE = 
		"CREATE TABLE " + TABLE_STATUS + "(" + 
			COLUMN_ID + " integer primary key, " +
			COLUMN_MESSAGE + " text not null, " +
			COLUMN_AUTHOR + " text not null, " +
			COLUMN_DATE + " integer not null);";
	  
	public SQLiteStatusHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SQLiteStatusHelper.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATUS);
	    onCreate(db);
	}
}
