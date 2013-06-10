package com.grupo5.trabetapa1.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.grupo5.trabetapa1.R;
import com.grupo5.trabetapa1.main.YambApplication;
import com.grupo5.trabetapa1.parcelable.UserInfo;
import com.grupo5.trabetapa1.services.IRemoteBoundService;
import com.grupo5.trabetapa1.services.UserInfoService;

public class UserInfoActivity extends BaseActivity {
	public static final String USERINFO_ACTION = "userinfopull";
	
	ImageView image;
	private IRemoteBoundService _remoteService;
	boolean _isBounded;

	private ServiceConnection _connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.w("UI", "onServiceConnected");
			_remoteService = IRemoteBoundService.Stub.asInterface(service);
		}
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.w("UI", "onServiceDisconnected");
			_remoteService = null;
		}
	};
	
	private BroadcastReceiver _receiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	    	Log.d("UI", "onReceive()");

	    	UserInfo myInfo = intent.getParcelableExtra(UserInfoService.USERINFO_EXTRA);
	    	((TextView) findViewById(R.id.edtNameUserInfo)).setText(myInfo.getName());
			((TextView) findViewById(R.id.edtStatusCountUserInfo)).setText(Integer.toString(myInfo.getStatusCount()));
			((TextView) findViewById(R.id.edtFriendsCountUserInfo)).setText(Integer.toString(myInfo.getFriendsCount()));
			((TextView) findViewById(R.id.edtFollowersCountUserInfo)).setText(Integer.toString(myInfo.getFollowersCount()));
			((ImageView) findViewById(R.id.imageView)).setImageBitmap(myInfo.getImageBitmap());
	    }
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		
		Log.w("UI", "onCreate");
		
	    _isBounded = bindService(new Intent("UserInfoServiceName"), _connection, BIND_AUTO_CREATE);		
		((Button)findViewById(R.id.loaduserinfobutton)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.w("UI", "onClick");
				try {
					if(((YambApplication)getApplication()).isNetworkAvailable()) {
						_remoteService.getStatus();
					} else {
						Toast.makeText(UserInfoActivity.this, getResources().getString(R.string.nonetworkmessage), Toast.LENGTH_SHORT).show();
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}			
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.w("UI", "onResume");
		registerReceiver(_receiver, new IntentFilter(USERINFO_ACTION));
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.w("UI", "onPause");
		if (_receiver != null) {
            unregisterReceiver(_receiver);
        }
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
}