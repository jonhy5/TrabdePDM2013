package com.grupo5.trabetapa1.services;

import java.util.List;

import winterwell.jtwitter.Twitter.Status;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.grupo5.trabetapa1.activities.PreferencesActivity;
import com.grupo5.trabetapa1.main.YambApplication;
import com.grupo5.trabetapa1.sql.StatusDataSource;

public class TimelinePull extends IntentService {
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
		int maxListItems, inserted;
		String user;
		Bundle extras;
		
		user = intent.getStringExtra(PreferencesActivity.USERNAMEKEY);
		maxListItems = intent.getIntExtra(PreferencesActivity.MAXMSGKEY, 0);
		inserted = 0;
		
		List<Status> statusList = aplication.getTwitter().getUserTimeline(user);
		for(Status st: statusList) {
			if(datasource.createStatus(st.getId(), st.getText(), st.getUser().getName(), st.getCreatedAt().getTime()) != null) {
				inserted++;
			}
		}

		
		
		extras = intent.getExtras();
		if (extras != null) {
			// TODO
			Messenger messenger = (Messenger) extras.get(YambApplication.EXTRA_MESSENGER);
			Message msg = Message.obtain();
			try {
				messenger.send(msg);
			}
			catch (android.os.RemoteException e) {
		        Log.w(getClass().getName(), "Exception sending message", e);
			}
		}
	}
	
	@Override
	public void onDestroy() {
		datasource.close();
		super.onDestroy();
	}
}
