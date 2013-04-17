package com.grupo5.trabetapa1.main;

import winterwell.jtwitter.Twitter;
import android.app.Application;

public class YambApplication extends Application {
	Twitter twitter;
	public static final String preferencesFileName = "ClientPrefs";
	public void onCreate() {
		super.onCreate();
		twitter = new Twitter("student", "password");
		twitter.setAPIRootUrl("http://yamba.marakana.com/api");
	}
}
