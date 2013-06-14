package com.grupo5.trabetapa1.services;

import java.util.List;

import winterwell.jtwitter.Twitter.Status;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.grupo5.trabetapa1.R;
import com.grupo5.trabetapa1.activities.PreferencesActivity;
import com.grupo5.trabetapa1.activities.TimelineActivity;
import com.grupo5.trabetapa1.main.YambApplication;
import com.grupo5.trabetapa1.sql.StatusDataSource;

public class TimelinePull extends IntentService {
	public static int STATUSBAR_ID = 2;
	public static final String TIMELINEPULL_ACTION = "timelinepull";
	public static final String TIMELINEREAD_ACTION = "timelineread";
	public static final String NEWROWS_EXTRA = "newrows";
	private YambApplication aplication;
	private StatusDataSource datasource;
	private static int newMessages = 0;
	
	public TimelinePull() {
		super("TimelinePull");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		aplication = (YambApplication) getApplication();
		datasource = new StatusDataSource(this);
	    datasource.open();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if(intent.getAction().equals(TIMELINEPULL_ACTION)) {
			int maxListItems, toDelete;
			
			String user = intent.getStringExtra(PreferencesActivity.USERNAMEKEY);
			maxListItems = intent.getIntExtra(PreferencesActivity.MAXMSGKEY, 0);
	
			List<Status> statusList = aplication.getTwitter().getUserTimeline(user);
			int total = datasource.rowCount();
			for(Status st: statusList) {
				if(datasource.createStatus(st.getId(), st.getText(), st.getUser().getName(), st.getCreatedAt().getTime()) != null) {
					total++;
				}
			}
			toDelete = total - maxListItems;
			if(toDelete > 0) {
				datasource.deleteLastRows(toDelete);
			}
			// Criar uma notificao com as mensagens nao lidas
			sendNotification(toDelete);
			
			// Notificar actividades interessadas que TimelinePull terminou
			Intent broadIntent = new Intent(TIMELINEPULL_ACTION);
			broadIntent.putExtra(NEWROWS_EXTRA, toDelete);
			LocalBroadcastManager.getInstance(this).sendBroadcast(broadIntent);
		} else if(intent.getAction().equals(TIMELINEREAD_ACTION)) {
			newMessages = 0;
		}
		Log.v("OnHandleIntent", "TimelinePull: " + intent.getAction());
	}
	
	/**
	 * Criar/Actualizar mensagem para a status bar
	 * @param msgNot
	 */
	private void sendNotification(int msgnum) {
		if(msgnum == 0) {
			return;
		}
		newMessages += msgnum;
		String msgNot = String.format(getResources().getString(R.string.timelinenotificationtext), newMessages);
		
		PendingIntent pending = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(this, TimelineActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
		Notification notification = new Notification.Builder(this)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentTitle(getResources().getString(R.string.timelinenotificationtitle))
			.setContentText(msgNot)
			.setAutoCancel(true)
			.setNumber(newMessages)
			.setWhen(System.currentTimeMillis() + 5000).build();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.contentIntent = pending;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(STATUSBAR_ID, notification);
	}
	
	@Override
	public void onDestroy() {
		datasource.close();
		super.onDestroy();
	}
}
