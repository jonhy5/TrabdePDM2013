package com.grupo5.trabetapa1.services;

import java.util.List;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.grupo5.trabetapa1.R;
import com.grupo5.trabetapa1.main.YambApplication;
import com.grupo5.trabetapa1.sql.MessageStatusDataSource;
import com.grupo5.trabetapa1.sql.MessageStatusModel;

public class StatusUpload extends IntentService {
	public static int STATUSBAR_ID = 1;
	public static String SUBMIT_ACTION = "sumbitstatus";
	public static String EMPTYQUEUE_ACTION = "emptyqueue";
	public static String EXTRA_KEY = "StatusMsg";
	private YambApplication _application;
	private MessageStatusDataSource _datasource;
	private Handler _uiHandler;
	
	public StatusUpload() {
		super("StatusUpload");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v("OnStartCommand","Status Upload");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.v("Oncreate","Status Upload");
		_application = (YambApplication) getApplication();
		_datasource = new MessageStatusDataSource(this);
		_datasource.open();
		_uiHandler = new Handler();
	}

	@Override
	protected void onHandleIntent(final Intent intent) {
		String action = intent.getAction();
		Log.v("OnHandleIntent","Status Upload: " + action);
		
		if(action.equals(SUBMIT_ACTION)) {
			final String msg = intent.getStringExtra(EXTRA_KEY);
			if(_application.isNetworkAvailable()) {
				_application.getTwitter().updateStatus(msg);
				
				// Queue toast final message to run in main thread
				_uiHandler.post(new Runnable() {
					@Override
					public void run() {
						String success = String.format(getResources().getString(R.string.status_success_msg), msg);
						Toast.makeText(StatusUpload.this, success, Toast.LENGTH_LONG).show();
					}
				});
			} else {
				// Adicionar a mensagem a queue
				_datasource.createMessageStatus(msg);
				int rows = _datasource.rowCount();
				
				String msgNot = String.format(getResources().getString(R.string.statusnotificationqueuetext), rows);
				sendNotification(msgNot);
			}
		} else if(action.equals(EMPTYQUEUE_ACTION)) {
			List<MessageStatusModel> status = _datasource.getAllStatus();
			int rows = 0;
			for(MessageStatusModel msgStatus: status) {
				_application.getTwitter().updateStatus(msgStatus.getMessage());
				rows++;
			}
			if(rows > 0) {
				_datasource.deleteAll();
				String msgNot = String.format(getResources().getString(R.string.statusnotificationsenttext), rows);
				sendNotification(msgNot);
			}
		}		
	}

	/**
	 * Criar/Actualizar mensagem para a status bar
	 * @param msgNot
	 */
	private void sendNotification(String msgNot) {
		PendingIntent pending = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0);
		
		Notification notification = new Notification.Builder(this)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentTitle(getResources().getString(R.string.statusnotificationtitle))
			.setContentText(msgNot)
			.setAutoCancel(true)
			.setWhen(System.currentTimeMillis()).build();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.contentIntent = pending;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(STATUSBAR_ID, notification);
	}
	
	@Override
	public void onDestroy() {
		_datasource.close();
		super.onDestroy();
	}
}
