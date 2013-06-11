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
import com.grupo5.trabetapa1.services.StatusUpload;
import com.grupo5.trabetapa1.services.TimelinePull;
import com.grupo5.trabetapa1.utils.Connectivity;

@SuppressLint("HandlerLeak")
public class YambApplication extends Application implements OnSharedPreferenceChangeListener  {
	public static final String EXTRA_MESSENGER = "TimeLineMessenger";	
	private static final String TAG = YambApplication.class.getSimpleName();

	private boolean isMobileNetworkAvailable, isWifiNetworkAvailable;
	private boolean isTimelinePullRunning;
	
	private SharedPreferences prefs;
	// Singleton Class twitter;
	private Twitter twitter;
	
	public static final String preferencesFileName = "ClientPrefs";
	private PendingIntent intentPull;
	
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate");
		
		isWifiNetworkAvailable = Connectivity.isConnectedWifi(getApplicationContext());
		isMobileNetworkAvailable = Connectivity.isConnectedMobile(getApplicationContext());
		isTimelinePullRunning = false;
		
		prefs = getSharedPreferences(YambApplication.preferencesFileName, MODE_PRIVATE);
		prefs.registerOnSharedPreferenceChangeListener(this);
		
		if(!prefs.contains(PreferencesActivity.USERNAMEKEY)) {
			Intent newActivity = new Intent(this, PreferencesActivity.class);
			newActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(newActivity);
		}
		if(prefs.getBoolean(PreferencesActivity.AUTOUP, true)) {
			startRepeatTimelinePull();
			emptyStatusqueue();
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
	 * Se existir conectividade rede arranca o serviço de actualização periodica da timeline
	 */
	private void startRepeatTimelinePull() {
		if(isWifiNetworkAvailable && !isTimelinePullRunning) {
			AlarmManager mng = (AlarmManager)getSystemService(ALARM_SERVICE);
			Intent timepull = new Intent(this, TimelinePull.class);
			timepull.putExtra(PreferencesActivity.USERNAMEKEY, prefs.getString(PreferencesActivity.USERNAMEKEY, "student"));
			timepull.putExtra(PreferencesActivity.MAXMSGKEY, Integer.parseInt(prefs.getString(PreferencesActivity.MAXMSGKEY, "20")));
			intentPull = PendingIntent.getService(this, 1, timepull, PendingIntent.FLAG_CANCEL_CURRENT);
			// Arranca ao fim de 30 segundos e repete a cada minuto
			mng.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + 30000, 60000, intentPull);
			isTimelinePullRunning = true;
		}
	}
	
	/**
	 * Para o serviço de actualização periodica da timeline se estiver a correr
	 */
	private void stopRepeatTimelinePull() {
		if(isTimelinePullRunning) {
			AlarmManager mng = (AlarmManager)getSystemService(ALARM_SERVICE);
			isTimelinePullRunning = false;
			mng.cancel(intentPull);
		}
	}
	
	/**
	 * 
	 */
	private void emptyStatusqueue() {
		Intent intent = new Intent(this, StatusUpload.class);
		intent.setAction(StatusUpload.EMPTYQUEUE_ACTION);
		startService(intent);
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
			// Arrancar/Parar o servico de actualizacao da timeline
			if(sharedPreferences.getBoolean(PreferencesActivity.AUTOUP, true)) {
				startRepeatTimelinePull();
			} else {
				stopRepeatTimelinePull();
			}
		}
	}

	/**
	 * Retorna se existe alguma rede disponivel
	 * 
	 * @return boolean 
	 */
	public synchronized boolean isNetworkAvailable() {
		return isMobileNetworkAvailable || isWifiNetworkAvailable;
	}
	
	/**
	 * Retorna se existe rede movel disponivel
	 * 
	 * @return boolean 
	 */
	public synchronized boolean isMobileNetworkAvailable() {
		return isMobileNetworkAvailable;
	}
	
	/**
	 * Retorna se existe rede Wifi disponivel
	 * 
	 * @return boolean 
	 */
	public synchronized boolean isWifiNetworkAvailable() {
		return isWifiNetworkAvailable;
	}

	/**
	 * Atribui se existe rede movel e wifi disponivel
	 * 
	 * @param is_network_available
	 */
	public synchronized void setNetworkAvailability(boolean isMobileNetworkAvailable, boolean isWifiNetworkAvailable) {
		// Se estamos offline e passamos a ter rede enviamos os status em queue
		if((!this.isMobileNetworkAvailable && !this.isWifiNetworkAvailable) && (isMobileNetworkAvailable || isWifiNetworkAvailable)) {
			emptyStatusqueue();
		}
		this.isMobileNetworkAvailable = isMobileNetworkAvailable;
		this.isWifiNetworkAvailable = isWifiNetworkAvailable;
		// Arranca o servico de Timepull se Wifi estiver disponivel ou para-o caso contrario
		if(isWifiNetworkAvailable) {
			startRepeatTimelinePull();
		} else {
			stopRepeatTimelinePull();
		}
	}
}