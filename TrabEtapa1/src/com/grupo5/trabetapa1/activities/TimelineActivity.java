package com.grupo5.trabetapa1.activities;

import java.util.Date;
import java.util.List;

import winterwell.jtwitter.Twitter.Status;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class TimelineActivity extends BaseActivity implements UserTimelineListener {
	private static final String TIMELINESTATUSKEY = "TimeLineActivity_status";
	private YambApplication application;
	private int _maxListItems;
	private boolean _statusDownload;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(ACTIVITY_SERVICE, "Oncreate Timeline");

		final SharedPreferences pref = getSharedPreferences(YambApplication.preferencesFileName, MODE_PRIVATE);
		_maxListItems = Integer.parseInt(pref.getString(PreferencesActivity.MAXMSGKEY, "20"));
		
		application = (YambApplication) getApplication();
		application.setUserTimelineListener(this);
				
		// Activity without android:label to have the application name, so we need to set the title here 
		setTitle(R.string.title_activity_timeline);
		setContentView(R.layout.activity_timeline);
		
		if(application.getStatusList() == null) {
			_statusDownload = true;
			((Button)findViewById(R.id.Btn_refresh)).setEnabled(!_statusDownload);

			application.lunchTimelinePull();
		} else {
			completeReport(application.getStatusList());
		}

		((GridView) findViewById(R.id.timelineGridView)).setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> adapterView, View arg1,int position, long id) {
	        	Log.v(ACTIVITY_SERVICE, "onItemClick");

	        	Status status = (Status) adapterView.getAdapter().getItem(position);
	        	
	    		Intent intent = new Intent(TimelineActivity.this, DetailedActivity.class);
	    		long nr = status.getId();
	    		String user = status.getUser().getName();
	    		String msg = status.getText();
	    		Date dt = (Date) status.getCreatedAt();
	    		
	    		DetailData d = new DetailData(nr, user, msg, dt.getTime() );
	    		intent.putExtra("detail", d);

	    		startActivity(intent);
	        }
	    });

		((Button)findViewById(R.id.Btn_refresh)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_statusDownload = true;
				((Button)findViewById(R.id.Btn_refresh)).setEnabled(!_statusDownload);

				application.lunchTimelinePull();
			}
		});
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
		outState.putString(TIMELINESTATUSKEY, Boolean.toString(_statusDownload));
		
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		_statusDownload = Boolean.valueOf(savedInstanceState.getString(TIMELINESTATUSKEY));
		findViewById(R.id.submitStatusButton).setEnabled(!_statusDownload);
	}

	@Override
	public void completeReport(List<Status> list) {
		GridView gridView = (GridView) findViewById(R.id.timelineGridView);				
		gridView.setAdapter(new TimelineAdapter(TimelineActivity.this, list.subList(0, list.size() < _maxListItems ? list.size(): _maxListItems)));
		_statusDownload = false;
		((Button)findViewById(R.id.Btn_refresh)).setEnabled(!_statusDownload);
	}
	
	@Override
	protected void onDestroy() {
		application.setUserTimelineListener(null);
		super.onDestroy();
	}
}
