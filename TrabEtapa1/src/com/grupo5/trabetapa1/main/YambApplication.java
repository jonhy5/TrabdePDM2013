package com.grupo5.trabetapa1.main;

import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.grupo5.trabetapa1.interfaces.SubmitStatusListener;

public class YambApplication extends Application {
	private static final String TAG = YambApplication.class.getSimpleName();
	private SubmitStatusListener statusListener;
	private AsyncTask<String, Void, Boolean> statusTask;
	// Singleton Class twitter;
	private Twitter twitter;
	
	public static final String preferencesFileName = "ClientPrefs";
	
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate");
	}
	
	public synchronized Twitter getTwitter() {
		if(twitter == null) {
			/*
			String username = this.prefs.getString("username", null);
			String password = this.prefs.getString("password", null);
			String url = this.prefs.getString("url", "http://yamba.marakana.com/api");
			if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(url)) {
				this.twitter = new Twitter(username, password);
				this.twitter.setAPIRootUrl(url);
			}
			*/
			twitter = new Twitter("student", "password");
			twitter.setAPIRootUrl("http://yamba.marakana.com/api");
		}
		return this.twitter;
	}
	
	public void setSubmitStatusListener(SubmitStatusListener listener) {
		statusListener = listener;
	}
	
	public void submitStatus(String status) {
		if(statusTask != null) {
			throw new IllegalStateException();
		}
		
		statusTask = new AsyncTask<String, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(String... status) {
				try {
					getTwitter().updateStatus(status[0]);
			        return true;
				} catch (RuntimeException e) {
			        Log.e(TAG, "Failed to connect to twitter service", e);
			        return false;
			    }
			}
			
			@Override 
			protected void onPostExecute(Boolean result) {
				if(statusListener != null)
					statusListener.completeReport(result);
				statusTask = null;
			}
		}.execute(status);
	}
}
