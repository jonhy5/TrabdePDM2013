package com.grupo5.trabetapa1.appwidget;

import java.util.Date;

import com.grupo5.trabetapa1.R;
import com.grupo5.trabetapa1.activities.TimelineActivity;
import com.grupo5.trabetapa1.contentprovider.StatusProvider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

public class TimelineWidgetProvider extends AppWidgetProvider {
	private final String[] projection = {
			StatusProvider.KEY_MESSAGE,
			StatusProvider.KEY_USER,
			StatusProvider.KEY_CREATEDAT };
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		
		Cursor cursor = context.getContentResolver().query(StatusProvider.CONTENT_URI, projection, null, null, StatusProvider.KEY_CREATEDAT + " desc LIMIT 1");
		if(cursor.moveToFirst()) {
			String msg = cursor.getString(0);
	        String user = cursor.getString(1);
			String created = new Date(cursor.getLong(2)).toString();

			remoteView.setTextViewText(R.id.userTextView, user);
			remoteView.setTextViewText(R.id.dateTextView, created);
			remoteView.setTextViewText(R.id.messageTextView, msg);
		}
		cursor.close();
		
		Intent launchAppIntent = new Intent(context, TimelineActivity.class);
		PendingIntent launchAppPendingIntent = PendingIntent.getActivity(context, 0, launchAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteView.setOnClickPendingIntent(R.id.full_widget, launchAppPendingIntent);
		
		ComponentName widget = new ComponentName(context, TimelineWidgetProvider.class);
		appWidgetManager.updateAppWidget(widget, remoteView);
	}	
}
