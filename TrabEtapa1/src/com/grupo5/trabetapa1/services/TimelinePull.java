package com.grupo5.trabetapa1.services;

import java.util.List;

import winterwell.jtwitter.Twitter.Status;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.grupo5.trabetapa1.activities.PreferencesActivity;
import com.grupo5.trabetapa1.main.YambApplication;
import com.grupo5.trabetapa1.sql.StatusDataSource;

public class TimelinePull extends IntentService {
	public static final String TIMELINEPULL_ACTION = "timelinepull";
	public static final String NEWROWS_EXTRA = "newrows";
	private YambApplication aplication;
	private StatusDataSource datasource;
	
	public TimelinePull() {
		super("TimelinePull");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		aplication = (YambApplication) getApplication();
		datasource = new StatusDataSource(this);
	    datasource.open();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		int maxListItems, toDelete;
		
		String user = intent.getStringExtra(PreferencesActivity.USERNAMEKEY);
		maxListItems = intent.getIntExtra(PreferencesActivity.MAXMSGKEY, 0);

		List<Status> statusList = aplication.getTwitter().getUserTimeline(user);
		int total = datasource.rowCount();
		for(Status st: statusList) {
			if(datasource.createStatus(st.getId(), st.getText(), st.getUser().getName(), st.getCreatedAt().getTime()) != null) {
				total++;
			}
		}
		toDelete = total - maxListItems;
		if(toDelete > 0) {
			datasource.deleteLastRows(toDelete);
		}
		
		// Notificar actividades interessadas que TimelinePull terminou
		Intent broadIntent = new Intent(TIMELINEPULL_ACTION);
		broadIntent.putExtra(NEWROWS_EXTRA, toDelete);
		LocalBroadcastManager.getInstance(this).sendBroadcast(broadIntent);
		
		Log.v("OnHandleIntent", "Timeline: " + toDelete);
	}
	
	@Override
	public void onDestroy() {
		datasource.close();
		super.onDestroy();
	}
}
