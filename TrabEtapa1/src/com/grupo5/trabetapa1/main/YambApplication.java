/**
 * Main Yamb5 application
 */
package com.grupo5.trabetapa1.main;

import winterwell.jtwitter.Twitter;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;

import com.grupo5.trabetapa1.activities.PreferencesActivity;
import com.grupo5.trabetapa1.services.TimelinePull;
import com.grupo5.trabetapa1.utils.Connectivity;

@SuppressLint("HandlerLeak")
public class YambApplication extends Application implements OnSharedPreferenceChangeListener  {
	public static final String EXTRA_MESSENGER = "TimeLineMessenger";
	
	private static final String TAG = YambApplication.class.getSimpleName();
	private SharedPreferences prefs;
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
		if(prefs.getBoolean(PreferencesActivity.AUTOUP, true)) {
			startRepeatTimelinePull();
		}
	}
	
	/**
	 * Retorna o objecto Twitter com as informações das preferencias
	 * 
	 * @return Twitter object
	 */
	public synchronized Twitter getTwitter() {
		if(twitter == null) {
			String username = prefs.getString(PreferencesActivity.USERNAMEKEY, "student");
			String password = prefs.getString(PreferencesActivity.PASSWORDKEY, "password");
			String url = prefs.getString(PreferencesActivity.BASEURIKEY, "http://yamba.marakana.com/api");
			twitter = new Twitter(username, password);
			twitter.setAPIRootUrl(url);
		}
		return twitter;
	}
		
	/**
	 * Se existir conectividade Wifi arranca o serviço de actualização da timeline
	 */
	private void startRepeatTimelinePull() {
		if(Connectivity.isConnectedWifi(getApplicationContext())) {
			System.out.println("ARRANCOU.....");
			
			
			AlarmManager mng = (AlarmManager)getSystemService(ALARM_SERVICE);
			Intent timepull = new Intent(this, TimelinePull.class);
			timepull.putExtra(PreferencesActivity.USERNAMEKEY, prefs.getString(PreferencesActivity.USERNAMEKEY, "student"));
			timepull.putExtra(PreferencesActivity.MAXMSGKEY, Integer.parseInt(prefs.getString(PreferencesActivity.MAXMSGKEY, "20")));
			intentPull = PendingIntent.getService(this, 1, timepull, PendingIntent.FLAG_CANCEL_CURRENT);
			// Arranca ao fim de 30 segundos e repete a cada minuto
			mng.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + 30000, 60000, intentPull);
		} else {
			System.out.println("OOOOPS NAO ARRANCOU.....");
		}
	}
	
	/**
	 * Callback para mudança nas preferencias
	 */
	@Override
	public synchronized void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// If preferences changes set twitter to null to create a new instance with the new values
		if(	key.equals(PreferencesActivity.USERNAMEKEY) ||
			key.equals(PreferencesActivity.PASSWORDKEY) ||
			key.equals(PreferencesActivity.BASEURIKEY)) {
			twitter = null;
		}
		if(key.equals(PreferencesActivity.AUTOUP)) {
			AlarmManager mng = (AlarmManager)getSystemService(ALARM_SERVICE);
			// Arrancar/Parar o servico de actualizacao da timeline
			if(sharedPreferences.getBoolean(PreferencesActivity.AUTOUP, true)) {
				startRepeatTimelinePull();
			} else {
				mng.cancel(intentPull);
			}
		}
	}
}