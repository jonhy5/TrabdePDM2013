package com.grupo5.trabetapa1.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

public class UserInfoPull extends Service {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	static class IncomingHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			Bundle payload = msg.getData();
			
		}
		
	}
	
	Messenger msg = new Messenger(new IncomingHandler());
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return msg.getBinder();
	}

}
