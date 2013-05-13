package com.grupo5.trabetapa1.main;

import java.sql.Time;
import java.util.Calendar;
import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.AsyncTask;

import android.util.Log;
import com.grupo5.trabetapa1.activities.PreferencesActivity;
import com.grupo5.trabetapa1.interfaces.SubmitStatusListener;
import com.grupo5.trabetapa1.interfaces.UserTimelineListener;
import com.grupo5.trabetapa1.services.TimelinePull;

public class YambApplication extends Application implements OnSharedPreferenceChangeListener  {
	private static final String TAG = YambApplication.class.getSimpleName();
	private SharedPreferences prefs;
	private SubmitStatusListener statusListener;
	private AsyncTask<String, Void, Boolean> statusTask;
	private UserTimelineListener userTimelineListener;
	private AsyncTask<String, Void, List<Status>> timelineTask;
	private List<Status> statusList;
	// Singleton Class twitter;
	private Twitter twitter;
	
	public static final String preferencesFileName = "ClientPrefs";
	private PendingIntent intentPull;
	
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate");
		prefs = getSharedPreferences(YambApplication.preferencesFileName, MODE_PRIVATE);
		prefs.registerOnSharedPreferenceChangeListener(this);
		
		if(!prefs.contains(PreferencesActivity.USERNAMEKEY)) {
			Intent newActivity = new Intent(this, PreferencesActivity.class);
			newActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(newActivity);
		}
		AlarmManager mng = (AlarmManager) getSystemService(ALARM_SERVICE);
		Intent timepull = new Intent(this,TimelinePull.class);
		intentPull = PendingIntent.getService(this, 1, timepull, PendingIntent.FLAG_CANCEL_CURRENT);
		mng.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(),AlarmManager.INTERVAL_FIFTEEN_MINUTES,intentPull);
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
		Log.v("submit sattus", "submit sattus");
		
		/*statusTask = new AsyncTask<String, Void, Boolean>() {
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
		}.execute(status);*/
		
	}
	
	public void setUserTimelineListener(UserTimelineListener listener) {
		userTimelineListener = listener;
	}
	
	public void updateStatusList(String user, boolean updateview){
		statusList = getTwitter().getUserTimeline(user);
		if(updateview)
			getUserTimeline(prefs.getString(PreferencesActivity.USERNAMEKEY, "student"));
	}
	
	public void getUserTimeline(String user) {
		if(timelineTask != null) {
			throw new IllegalStateException();
		}
		
		timelineTask = new AsyncTask<String, Void, List<Status>>() {
			@Override
			protected List<winterwell.jtwitter.Twitter.Status> doInBackground(String... user) {
				
				try {
					if(statusList == null){updateStatusList(user[0], false);}
					List<winterwell.jtwitter.Twitter.Status> list = statusList;//getTwitter().getUserTimeline(user[0]);
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
