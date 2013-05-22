package com.grupo5.trabetapa1.services;

import com.grupo5.trabetapa1.R;
import com.grupo5.trabetapa1.main.YambApplication;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class StatusUpload extends IntentService {
	public static String EXTRA_KEY = "StatusMsg";
	private YambApplication _aplication;
	private Handler _uiHandler;
	
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
		super.onCreate();
		Log.v("Oncreate","Status Upload");
		_aplication = (YambApplication) getApplication();
		_uiHandler = new Handler();
	}

	@Override
	protected void onHandleIntent(final Intent intent) {
		Log.v("OnHandleINtent","Status Upload");

		final String name = intent.getStringExtra(EXTRA_KEY);
		_aplication.getTwitter().updateStatus(name);
		
		// Queue toast message to run in main thread
		_uiHandler.post(new Runnable() {
			@Override
			public void run() {
				String success = String.format(getResources().getString(R.string.status_success_msg), name);
				Toast.makeText(StatusUpload.this, success, Toast.LENGTH_LONG).show();
			}
		});
	}
}
