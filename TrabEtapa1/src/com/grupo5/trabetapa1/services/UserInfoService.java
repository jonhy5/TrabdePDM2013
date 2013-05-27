package com.grupo5.trabetapa1.services;


import winterwell.jtwitter.Twitter.User;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.grupo5.trabetapa1.activities.PreferencesActivity;
import com.grupo5.trabetapa1.main.YambApplication;

public class UserInfoService extends Service {
	private static final String TAG = "UserInfoService";
	private UserInfoReceiverCallback _userInfoReceiverCallback;
	
	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate()");
		super.onCreate();
		_userInfoReceiverCallback = null;
	}
	
	@Override
	public IBinder onBind(Intent intent) 
	{
		Log.v(TAG,"OnBind");
		return new IRemoteBoundService.Stub() {
			@Override
			public void getStatus() {
				Thread thread = new Thread() {
				    @Override
				    public void run() {
				    	MyParcelable info;
				    	
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
						image = user.getProfileBackgroundImageUrl().toString();
						
						info = new MyParcelable(name, statusCount,friendsCount, followersCount, image);
						
						if(_userInfoReceiverCallback != null) {
							try {
								_userInfoReceiverCallback.UserInfoReceiver(info);
							} catch (RemoteException e) {
								e.printStackTrace();
							}
						}
				    }
				};
				thread.start();
			}
			
			@Override
			public void setCallback(UserInfoReceiverCallback callback) {
				_userInfoReceiverCallback = callback;
			}
			
			@Override
			public void unsetCallback() {
				_userInfoReceiverCallback = null;
			}
		};
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy()");
	}
}
