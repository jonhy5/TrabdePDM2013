package com.grupo5.trabetapa1.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import winterwell.jtwitter.Twitter.User;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.StrictMode;
import android.util.Log;

import com.grupo5.trabetapa1.activities.PreferencesActivity;
import com.grupo5.trabetapa1.main.YambApplication;
import com.grupo5.trabetapa1.parcelable.UserInfo;

public class UserInfoService extends Service {
	private static final String TAG = "UserInfoService";
	private UserInfoReceiver _userInfoReceiver;
	
	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate()");
		super.onCreate();
		_userInfoReceiver = null;
	}
	
	@Override
	public IBinder onBind(Intent intent) 
	{
		return new IRemoteBoundService.Stub() {
			@Override
			public void getStatus() {
				// Para corrigir bug do emulador
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
				StrictMode.setThreadPolicy(policy);

				UserInfo info;
				    	
		    	YambApplication app = (YambApplication)getApplication();
				SharedPreferences pref = getSharedPreferences(YambApplication.preferencesFileName, MODE_PRIVATE);
				String name = pref.getString(PreferencesActivity.USERNAMEKEY, "student");
				
				User user = app.getTwitter().getUser(name);
				
				final int statusCount;
				final int friendsCount;
				final int followersCount;
				final String image;
				
				statusCount = user.getStatusesCount();
				friendsCount = user.getFriendsCount();
				followersCount = user.getFollowersCount();
				image = user.getProfileImageUrl().toString();
				Bitmap bitmap = null;
				try {
					bitmap = BitmapFactory.decodeStream(new URL(image).openConnection().getInputStream());
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}						
				info = new UserInfo(name, statusCount, friendsCount, followersCount, image, bitmap);

				if(_userInfoReceiver != null) {
					try {
						_userInfoReceiver.UserInfoReceiver(info);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
			
			@Override
			public void setCallback(UserInfoReceiver callback) {
				_userInfoReceiver = callback;
			}
			
			@Override
			public void unsetCallback() {
				_userInfoReceiver = null;
			}
		};
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy()");
	}
}
