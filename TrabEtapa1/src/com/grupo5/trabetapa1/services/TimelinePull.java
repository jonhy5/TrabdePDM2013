package com.grupo5.trabetapa1.services;



import com.grupo5.trabetapa1.activities.PreferencesActivity;
import com.grupo5.trabetapa1.main.YambApplication;

import android.app.IntentService;
import android.content.Intent;



public class TimelinePull extends IntentService{

	private YambApplication aplication;
	
	public TimelinePull() {
		super("IntentService");
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		aplication = (YambApplication) getApplication();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String name = intent.getStringExtra(PreferencesActivity.USERNAMEKEY);
		aplication.updateStatusList(name);
	}
}