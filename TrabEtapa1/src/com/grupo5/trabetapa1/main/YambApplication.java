package com.grupo5.trabetapa1.main;

import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

public class YambApplication extends Application {
	public static Twitter twitter;
	public static final String preferencesFileName = "ClientPrefs";
	public static SharedPreferences pref;
	public void onCreate() {
		super.onCreate();
		Log.v(ACTIVITY_SERVICE, "Aplication");
		if(pref == null){pref = getSharedPreferences(preferencesFileName, 0);}
		twitter = new Twitter("student", "password");
		twitter.setAPIRootUrl("http://yamba.marakana.com/api");
	}
	
}
