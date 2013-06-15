package com.grupo5.trabetapa1.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StatusHelper extends SQLiteOpenHelper{
	public static final String DB_NAME = "timeline.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "timeline";

    public StatusHelper(Context context) {
    	super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	String sql = String.format("create table %s ( %s INT PRIMARY KEY,"
    			+ "%s INT, %s TEXT, %s TEXT);", TABLE,
    			StatusContract.Columns._ID, StatusContract.Columns.CREATED_AT,
    			StatusContract.Columns.USER, StatusContract.Columns.MESSAGE);
    	db.execSQL(sql);
    	
    	Log.w(StatusHelper.class.getName(), "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	Log.w(StatusHelper.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE);
	    onCreate(db);
    }
}
