package com.grupo5.trabetapa1.services;

import com.grupo5.trabetapa1.main.YambApplication;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class StatusUpload extends IntentService {
	private YambApplication aplication;
	
	public StatusUpload() {
		super("StatusUpload");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v("OnStartCommand","Status Upload");
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.v("Oncreate","Status Upload");
		aplication = (YambApplication) getApplication();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.v("OnHandleINtent","Status Upload");
		String name = intent.getStringExtra("StatusMsg");
		aplication.getTwitter().updateStatus(name);
	}
}
