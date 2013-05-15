package com.grupo5.trabetapa1.activities;

import com.grupo5.trabetapa1.R;
import com.grupo5.trabetapa1.R.layout;
import com.grupo5.trabetapa1.R.menu;
import com.grupo5.trabetapa1.services.IRemoteBoundService;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
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
		
		_bindIntent = new Intent(UserInfoActivity.this,IRemoteBoundService.class);
		
		final ServiceConnection connection = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) 
			{
				_remoteBinder = IRemoteBoundService.Stub.asInterface(service);
			}

			@Override
			public void onServiceDisconnected(ComponentName name) 
			{
				unbindService(this);
				_remoteBinder = null;
			}
		};

		
			//image = (ImageView) findViewById(R.id.);
			
			screenName = (TextView) findViewById(R.id.edtNameUserInfo);
			statusCount = (TextView) findViewById(R.id.edtStatusCountUserInfo);
			friendsCount = (TextView) findViewById(R.id.edtFriendsCountUserInfo);
			followersCount = (TextView) findViewById(R.id.edtFollowersCountUserInfo);
			
			//screenName.setText();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_info, menu);
		return true;
	}
	

}
