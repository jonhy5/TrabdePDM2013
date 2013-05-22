package com.grupo5.trabetapa1.services;

import java.util.List;

import winterwell.jtwitter.Twitter.Status;

import com.grupo5.trabetapa1.activities.PreferencesActivity;
import com.grupo5.trabetapa1.activities.TimelineActivity;
import com.grupo5.trabetapa1.main.YambApplication;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class TimelinePull extends IntentService{
	private YambApplication aplication;
	
	public TimelinePull() {
		super("TimelinePull");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		aplication = (YambApplication) getApplication();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String user = intent.getStringExtra(PreferencesActivity.USERNAMEKEY);
		
		Bundle extras = intent.getExtras();
		if (extras != null) {
			List<Status> statusList = aplication.getTwitter().getUserTimeline(user);
			
			Messenger messenger = (Messenger) extras.get(TimelineActivity.EXTRA_MESSENGER);			
			Message msg = Message.obtain();
			msg.obj = statusList;
			try {
				messenger.send(msg);
			}
			catch (android.os.RemoteException e) {
		        Log.w(getClass().getName(), "Exception sending message", e);
			}
		}
		
		
		//aplication.updateStatusList(name, updateView);
	}
}
