package com.grupo5.trabetapa1.main;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.AsyncTask;
import android.util.Log;

import com.grupo5.trabetapa1.activities.PreferencesActivity;
import com.grupo5.trabetapa1.interfaces.SubmitStatusListener;
import com.grupo5.trabetapa1.interfaces.UserTimelineListener;

public class YambApplication extends Application implements OnSharedPreferenceChangeListener  {
	private static final String TAG = YambApplication.class.getSimpleName();
	private SharedPreferences prefs;
	private SubmitStatusListener statusListener;
	private AsyncTask<String, Void, Boolean> statusTask;
	private UserTimelineListener userTimelineListener;
	private AsyncTask<String, Void, List<Status>> timelineTask;
	// Singleton Class twitter;
	private Twitter twitter;
	
	public static final String preferencesFileName = "ClientPrefs";
	
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate");
		
		prefs = getSharedPreferences(YambApplication.preferencesFileName, MODE_PRIVATE);
		prefs.registerOnSharedPreferenceChangeListener(this);
	}
	
	public synchronized Twitter getTwitter() {
		if(twitter == null) {
			String username = prefs.getString(PreferencesActivity.USERNAMEKEY, "student");
			String password = prefs.getString(PreferencesActivity.PASSWORDKEY, "password");
			String url = prefs.getString(PreferencesActivity.BASEURIKEY, "http://yamba.marakana.com/api");
			twitter = new Twitter(username, password);
			twitter.setAPIRootUrl(url);
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
	
	public void setUserTimelineListener(UserTimelineListener listener) {
		userTimelineListener = listener;
	}
	
	public void getUserTimeline(String user) {
		if(timelineTask != null) {
			throw new IllegalStateException();
		}
		
		timelineTask = new AsyncTask<String, Void, List<Status>>() {
			@Override
			protected List<winterwell.jtwitter.Twitter.Status> doInBackground(String... user) {
				try {
					List<winterwell.jtwitter.Twitter.Status> list = getTwitter().getUserTimeline(user[0]);
			        return list;
				} catch (RuntimeException e) {
			        Log.e(TAG, "Failed to connect to twitter service", e);
			        return null;
			    }
			}
			
			@Override 
			protected void onPostExecute(List<winterwell.jtwitter.Twitter.Status> result) {
				if(userTimelineListener != null)
					userTimelineListener.completeReport(result);
				timelineTask = null;
			}
		}.execute(user);
	}

	@Override
	public synchronized void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// If preferences changes set twitter to null to create a new instance with the new values
		if(	key.equals(PreferencesActivity.USERNAMEKEY) ||
			key.equals(PreferencesActivity.PASSWORDKEY) ||
			key.equals(PreferencesActivity.BASEURIKEY)) {
			twitter = null;
		}
	}
}
