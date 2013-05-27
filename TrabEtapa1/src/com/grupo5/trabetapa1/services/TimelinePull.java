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

public class TimelinePull extends IntentService {
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
		String user;
		Bundle extras;
		
		user = intent.getStringExtra(PreferencesActivity.USERNAMEKEY);
		extras = intent.getExtras();
		if (extras != null) {
			List<Status> statusList = aplication.getTwitter().getUserTimeline(user);
			
			Messenger messenger = (Messenger) extras.get(YambApplication.EXTRA_MESSENGER);
			Message msg = Message.obtain();
			msg.obj = statusList;
			try {
				messenger.send(msg);
			}
			catch (android.os.RemoteException e) {
		        Log.w(getClass().getName(), "Exception sending message", e);
			}
		}
	}
}
