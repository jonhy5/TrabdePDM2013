package com.grupo5.trabetapa1.main;

import winterwell.jtwitter.Twitter;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.grupo5.trabetapa1.activities.PreferencesActivity;
import com.grupo5.trabetapa1.interfaces.UserTimelineListener;
import com.grupo5.trabetapa1.services.TimelinePull;

@SuppressLint("HandlerLeak")
public class YambApplication extends Application implements OnSharedPreferenceChangeListener  {
	public static final String EXTRA_MESSENGER = "TimeLineMessenger";
	
	private static final String TAG = YambApplication.class.getSimpleName();
	private SharedPreferences prefs;
	private UserTimelineListener userTimelineListener;
	// Singleton Class twitter;
	private Twitter twitter;
	
	public static final String preferencesFileName = "ClientPrefs";
	private PendingIntent intentPull;
	
	private final Handler _timepullHandler = new Handler() {
		@Override
	    public void handleMessage(Message msg) {
			if(userTimelineListener != null) {
				userTimelineListener.completeReport();
			}
	    }
	};
	
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
		startRepeatTimelinePull();
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
			
	public void setUserTimelineListener(UserTimelineListener listener) {
		userTimelineListener = listener;
	}
	
	private void startRepeatTimelinePull() {
		AlarmManager mng = (AlarmManager)getSystemService(ALARM_SERVICE);
		Intent timepull = new Intent(this, TimelinePull.class);
		timepull.putExtra(PreferencesActivity.USERNAMEKEY, prefs.getString(PreferencesActivity.USERNAMEKEY, "student"));
		timepull.putExtra(PreferencesActivity.MAXMSGKEY, Integer.parseInt(prefs.getString(PreferencesActivity.MAXMSGKEY, "20")));
		timepull.putExtra(EXTRA_MESSENGER, new Messenger(_timepullHandler));
		intentPull = PendingIntent.getService(this, 1, timepull, PendingIntent.FLAG_CANCEL_CURRENT);
		// Start 30 seconds after application boot and repeat every 5 minutes
		mng.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + 30000, 300000, intentPull);
	}
	
	public void lunchTimelinePull() {
		Intent intent = new Intent(this, TimelinePull.class);
		intent.putExtra(PreferencesActivity.USERNAMEKEY, prefs.getString(PreferencesActivity.USERNAMEKEY, "student"));
		intent.putExtra(PreferencesActivity.MAXMSGKEY, Integer.parseInt(prefs.getString(PreferencesActivity.MAXMSGKEY, "20")));
		intent.putExtra(YambApplication.EXTRA_MESSENGER, new Messenger(_timepullHandler));
		startService(intent);
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