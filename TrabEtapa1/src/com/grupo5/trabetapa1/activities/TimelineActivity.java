package com.grupo5.trabetapa1.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.grupo5.trabetapa1.R;
import com.grupo5.trabetapa1.main.YambApplication;
import com.grupo5.trabetapa1.parcelable.DetailData;
import com.grupo5.trabetapa1.services.TimelinePull;
import com.grupo5.trabetapa1.sql.StatusDataSource;
import com.grupo5.trabetapa1.sql.StatusModel;

public class TimelineActivity extends BaseActivity {
	private static final String TIMELINESTATUSKEY = "TimeLineActivity_status";
	private StatusDataSource datasource;
	private boolean _statusDownloading;
	private BroadcastReceiver _onComplete = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	    	int rows = intent.getIntExtra(TimelinePull.NEWROWS_EXTRA, 0);
	    	updateGridView(rows);
	    }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(ACTIVITY_SERVICE, "Oncreate Timeline");
		
		datasource = new StatusDataSource(this);
	    datasource.open();

		// Activity without android:label to have the application name, so we need to set the title here 
		setTitle(R.string.title_activity_timeline);
		setContentView(R.layout.activity_timeline);
		
		((GridView) findViewById(R.id.timelineGridView)).setAdapter(new TimelineAdapter(TimelineActivity.this, R.id.timelineGridView, new ArrayList<StatusModel>()));
		((GridView) findViewById(R.id.timelineGridView)).setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long id) {
	        	Log.v(ACTIVITY_SERVICE, "onItemClick");

	        	StatusModel status = (StatusModel) adapterView.getAdapter().getItem(position);
	        	
	    		Intent intent = new Intent(TimelineActivity.this, DetailedActivity.class);
	    		long nr = status.getId();
	    		String user = status.getAuthor();
	    		String msg = status.getMessage();
	    		long dt = status.getDate();
	    		
	    		DetailData d = new DetailData(nr, user, msg, dt);
	    		intent.putExtra("detail", d);

	    		startActivity(intent);
	        }
	    });

		((Button)findViewById(R.id.Btn_refresh)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(((YambApplication)getApplication()).isNetworkAvailable()) {
					_statusDownloading = true;
					((Button)findViewById(R.id.Btn_refresh)).setEnabled(!_statusDownloading);
	
					Intent intent = new Intent(TimelineActivity.this, TimelinePull.class);
					SharedPreferences prefs = getSharedPreferences(YambApplication.preferencesFileName, MODE_PRIVATE);
					intent.putExtra(PreferencesActivity.USERNAMEKEY, prefs.getString(PreferencesActivity.USERNAMEKEY, "student"));
					intent.putExtra(PreferencesActivity.MAXMSGKEY, Integer.parseInt(prefs.getString(PreferencesActivity.MAXMSGKEY, "20")));
					intent.setAction(TimelinePull.TIMELINEPULL_ACTION);
					startService(intent);
				} else {
					Toast.makeText(TimelineActivity.this, getResources().getString(R.string.nonetworkmessage), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		updateGridView(1);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		IntentFilter iff = new IntentFilter(TimelinePull.TIMELINEPULL_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(_onComplete, iff);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(_onComplete);
	}

	// Update gridView
	private void updateGridView(int rows) {
    	List<StatusModel> list = datasource.getAllStatus();
		
		GridView gridView = (GridView) findViewById(R.id.timelineGridView);
		TimelineAdapter adapter = (TimelineAdapter)gridView.getAdapter();
		adapter.clear();
		adapter.addAll(list);
		adapter.notifyDataSetChanged();

		_statusDownloading = false;
		((Button)findViewById(R.id.Btn_refresh)).setEnabled(!_statusDownloading);
		
		// Cancelar notificacao se existir e voltar a 0 o contador de mensagens nao lidas
    	((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(TimelinePull.STATUSBAR_ID);
    	Intent intentTime = new Intent(TimelineActivity.this, TimelinePull.class);
    	intentTime.setAction(TimelinePull.TIMELINEREAD_ACTION);
		startService(intentTime);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(TIMELINESTATUSKEY, Boolean.toString(_statusDownloading));
		
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		_statusDownloading = Boolean.valueOf(savedInstanceState.getString(TIMELINESTATUSKEY));
		findViewById(R.id.submitStatusButton).setEnabled(!_statusDownloading);
	}

	@Override
	protected void onDestroy() {
		datasource.close();
		super.onDestroy();
	}
}
