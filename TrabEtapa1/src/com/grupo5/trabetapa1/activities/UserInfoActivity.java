package com.grupo5.trabetapa1.activities;

import com.grupo5.trabetapa1.R;
import com.grupo5.trabetapa1.services.IRemoteBoundService;
import com.grupo5.trabetapa1.services.MyParcelable;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class UserInfoActivity extends Activity {

	TextView screenName, statusCount, friendsCount, followersCount;
	ImageView image;
	
	private IRemoteBoundService _remoteBinder;
	private Intent _bindIntent;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		
		//image = (ImageView) findViewById(R.id.);
		
		screenName = (TextView) findViewById(R.id.edtNameUserInfo);
		statusCount = (TextView) findViewById(R.id.edtStatusCountUserInfo);
		friendsCount = (TextView) findViewById(R.id.edtFriendsCountUserInfo);
		followersCount = (TextView) findViewById(R.id.edtFollowersCountUserInfo);
		
		
		_bindIntent = new Intent(UserInfoActivity.this,IRemoteBoundService.class);
		
		final ServiceConnection connection = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) 
			{
				Log.v("","OnServiceConnected");
				_remoteBinder = IRemoteBoundService.Stub.asInterface(service);
				
				LoadData();
			}

			@Override
			public void onServiceDisconnected(ComponentName name) 
			{
				unbindService(this);
				_remoteBinder = null;
			}
		};
		
		bindService(_bindIntent, connection, Service.BIND_AUTO_CREATE);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_info, menu);
		return true;
	}
	
	public void LoadData()
	{
		MyParcelable info;
		try {
			info = _remoteBinder.getStatus();
			screenName.setText(info.getName());
			statusCount.setText(""+info.getStatusCount());
			friendsCount.setText(""+info.getFriendsCount());
			followersCount.setText(""+info.getFollowersCount());
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}	
	

}
