package com.grupo5.trabetapa1.appwidget;

import com.grupo5.trabetapa1.contentprovider.StatusProvider;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.database.Cursor;

public class TimelineWidgetProvider extends AppWidgetProvider {
	private final String[] projection = {
			StatusProvider.KEY_MESSAGE,
			StatusProvider.KEY_USER,
			StatusProvider.KEY_CREATEDAT };
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		Cursor cursor = context.getContentResolver().query(StatusProvider.CONTENT_URI,
                projection, null, null, null);
		cursor.moveToFirst();
		String msg = cursor.getString(0);
        String user = cursor.getString(1);
		long created = cursor.getLong(2);
		
		
	}	
}
