package com.grupo5.trabetapa1.services;


import winterwell.jtwitter.Twitter.User;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.grupo5.trabetapa1.activities.PreferencesActivity;
import com.grupo5.trabetapa1.main.YambApplication;

public class UserInfoPull extends Service {

	private Handler _uiHandler;
	private  MyParcelable info;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
	}


	@Override
	public boolean onUnbind(Intent intent) 
	{
		Toast.makeText(this, "onUnbind()", Toast.LENGTH_LONG).show();
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) 
	{
		Toast.makeText(this, "onRebind()", Toast.LENGTH_LONG).show();
		super.onRebind(intent);
	}
	

	@Override
	public IBinder onBind(Intent intent) 
	{
		Log.v("","OnBind");
		return new IRemoteBoundService.Stub() {
			
			
			@Override
			public MyParcelable getStatus() throws RemoteException {
				
	   			_uiHandler.post(new Runnable() {
					@Override
					public void run() {
						
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

					}
				});
				
				return info;
			}
		};
	}

}
