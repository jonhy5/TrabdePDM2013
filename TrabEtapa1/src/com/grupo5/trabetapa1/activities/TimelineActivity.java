package com.grupo5.trabetapa1.activities;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.grupo5.trabetapa1.R;
import com.grupo5.trabetapa1.interfaces.UserTimelineListener;
import com.grupo5.trabetapa1.main.YambApplication;
import com.grupo5.trabetapa1.parcelable.DetailData;
import com.grupo5.trabetapa1.sql.StatusDataSource;
import com.grupo5.trabetapa1.sql.StatusModel;

public class TimelineActivity extends BaseActivity implements UserTimelineListener {
	private static final String TIMELINESTATUSKEY = "TimeLineActivity_status";
	private YambApplication application;
	private StatusDataSource datasource;
	
	
	private boolean _statusDownloading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(ACTIVITY_SERVICE, "Oncreate Timeline");

		application = (YambApplication) getApplication();
		application.setUserTimelineListener(this);
		
		datasource = new StatusDataSource(this);
	    datasource.open();

		// Activity without android:label to have the application name, so we need to set the title here 
		setTitle(R.string.title_activity_timeline);
		setContentView(R.layout.activity_timeline);

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
				_statusDownloading = true;
				((Button)findViewById(R.id.Btn_refresh)).setEnabled(!_statusDownloading);

				application.lunchTimelinePull();
			}
		});
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		completeReport();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.actionbar_menu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		Log.d(ACTIVITY_SERVICE, "onItemSelected");
		return super.onOptionsItemSelected(item);
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
	public void completeReport() {
		List<StatusModel> list = datasource.getAllStatus();
		
		GridView gridView = (GridView) findViewById(R.id.timelineGridView);
		gridView.setAdapter(new TimelineAdapter(TimelineActivity.this, list));
		
		_statusDownloading = false;
		((Button)findViewById(R.id.Btn_refresh)).setEnabled(!_statusDownloading);
	}

	@Override
	protected void onDestroy() {
		application.setUserTimelineListener(null);
		datasource.close();
		super.onDestroy();
	}
}
