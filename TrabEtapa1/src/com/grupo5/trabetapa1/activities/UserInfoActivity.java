package com.grupo5.trabetapa1.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupo5.trabetapa1.R;
import com.grupo5.trabetapa1.services.IRemoteBoundService;
import com.grupo5.trabetapa1.services.MyParcelable;
import com.grupo5.trabetapa1.services.UserInfoReceiverCallback;

public class UserInfoActivity extends Activity {
	
	ImageView image;
	private IRemoteBoundService _remoteServive;
	private Handler _uiHandler;
	boolean _isBounded;

	private ServiceConnection _connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.w("UI", "onServiceConnected");
			_remoteServive = IRemoteBoundService.Stub.asInterface(service);
			
			UserInfoReceiverCallback callback = new UserInfoReceiverCallback.Stub() {
				public void UserInfoReceiver(final MyParcelable myInfo) throws RemoteException {
					_uiHandler.post(new Runnable() {
						@Override
						public void run() {
							((TextView) findViewById(R.id.edtNameUserInfo)).setText(myInfo.getName());
							((TextView) findViewById(R.id.edtStatusCountUserInfo)).setText(Integer.toString(myInfo.getStatusCount()));
							((TextView) findViewById(R.id.edtFriendsCountUserInfo)).setText(Integer.toString(myInfo.getFriendsCount()));
							((TextView) findViewById(R.id.edtFollowersCountUserInfo)).setText(Integer.toString(myInfo.getFollowersCount()));
						}
					});					
				}
			};

			try {
				_remoteServive.setCallback(callback);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.w("UI", "onServiceDisconnected");
			try {
				_remoteServive.unsetCallback();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			_remoteServive = null;
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		
		Log.w("UI", "onCreate");
		
		_uiHandler = new Handler();
		Intent i = new Intent("UserInfoServiceName");
	    _isBounded = bindService(i, _connection, BIND_AUTO_CREATE);
		
		((Button)findViewById(R.id.loaduserinfobutton)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.w("UI", "onClick");
				try {
					_remoteServive.getStatus();
					
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}			
		});
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.w("UI", "onStop");
		if(_isBounded) {
			unbindService(_connection);
			_isBounded = false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_info, menu);
		return true;
	}
}