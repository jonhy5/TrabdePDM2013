package com.grupo5.trabetapa1.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

public class StatusProvider extends ContentProvider {
	private StatusHelper dbHelper;

	public static final String T_TIMELINE = "timeline";
	public static final String AUTHORITY = "com.grupo5.trabetapa1.contentprovider";

	// The content:// style URL for this table
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/status");

	// The MIME type providing a single status
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.yamba5.status";

	// The MIME type providing a set of statuses
	public static final String CONTENT_DIR_TYPE = "vnd.android.cursor.dir/vnd.yamba5.status";

	// Constants to help differentiate between the URI requests
	private static final int STATUS_ITEM = 1;
	private static final int STATUS_DIR = 2;

	private static final UriMatcher uriMatcher;

	// Static initializer, allocating a UriMatcher object. A URI ending in "/status" is a
	// request for all statuses, and a URI ending in "/status/<id>" refers to a single status.
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "status", STATUS_DIR);
		uriMatcher.addURI(AUTHORITY, "status/#", STATUS_ITEM);
	}

	public static final String KEY_ID = BaseColumns._ID;
	public static final String KEY_USER = "user";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_CREATEDAT = "createdAt";

	// Define default sort order for queries
	private static final String DEFAULT_SORT_ORDER = KEY_CREATEDAT + " desc";
	
	// Identify the MIME types we provide for a given URI
	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
			case STATUS_DIR:
				return CONTENT_DIR_TYPE;
			case STATUS_ITEM:
				return CONTENT_ITEM_TYPE;
			default:
	            throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}
	
	@Override
	public boolean onCreate() {
		Context context = getContext();

		dbHelper = new StatusHelper(context);
		return (dbHelper == null) ? false : true;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sort) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		qb.setTables(T_TIMELINE);

		switch (uriMatcher.match(uri)) {
			case STATUS_DIR:
			break;

			case STATUS_ITEM:
				qb.appendWhere(KEY_ID + "=" + uri.getPathSegments().get(1));
			break;
			
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		String orderBy = TextUtils.isEmpty(sort) ? DEFAULT_SORT_ORDER : sort;

		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), uri);

		return c;
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
        if (uriMatcher.match(uri) != STATUS_DIR) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowID = db.insertOrThrow(T_TIMELINE, null, initialValues);
        
        Uri newUri = ContentUris.withAppendedId(CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(newUri, null);

        return newUri;
	}
	
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count;

		switch (uriMatcher.match(uri)) {
			case STATUS_DIR:
				count = db.delete(T_TIMELINE, where, whereArgs);
				break;
			case STATUS_ITEM:
				String segment = uri.getPathSegments().get(1);
				String whereClause = KEY_ID + "=" + segment 
								   + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : "");
				count = db.delete(T_TIMELINE, whereClause, whereArgs);
				break;
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
    	getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count;

		switch (uriMatcher.match(uri)) {
			case STATUS_DIR:
				count = db.update(T_TIMELINE, values, where, whereArgs);
				break;
			case STATUS_ITEM:
				String segment = uri.getPathSegments().get(1);
				String whereClause = KEY_ID + "=" + segment 
								   + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : "");
				count = db.update(T_TIMELINE, values, whereClause, whereArgs);
				break;
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
    	getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
}
