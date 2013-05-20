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
import android.widget.GridView;
import android.widget.Button;

import com.grupo5.trabetapa1.R;
import com.grupo5.trabetapa1.interfaces.UserTimelineListener;
import com.grupo5.trabetapa1.main.YambApplication;
import com.grupo5.trabetapa1.services.TimelinePull;

public class TimelineActivity extends BaseActivity {
	private int maxListItems;
	private YambApplication application;
	private static final String TIMELINEKEY = "TimeLineActivity_status";
	private StatusActivity.Status status;
	public static String UPDATEVIEW = "UpdateView";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_timeline);
		Log.v(ACTIVITY_SERVICE, "Oncreate Timeline");

		
		final SharedPreferences pref = getSharedPreferences(YambApplication.preferencesFileName, MODE_PRIVATE);
		maxListItems = Integer.parseInt(pref.getString(PreferencesActivity.MAXMSGKEY, "20"));
		
		application = (YambApplication) getApplication();
		application.setUserTimelineListener(new UserTimelineListener() {
			@Override
			public void completeReport(List<Status> list) {
				GridView gridView = (GridView) findViewById(R.id.timelineGridView);				
				gridView.setAdapter(new TimelineAdapter(TimelineActivity.this, list.subList(0, list.size()<maxListItems?list.size():maxListItems)));
				status = StatusActivity.Status.COMPLETED;
				((Button)findViewById(R.id.Btn_refresh)).setEnabled(true);
			}
		});
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
				/*((Button)findViewById(R.id.Btn_refresh)).setEnabled(false);
				application.getUserTimeline(pref.getString(PreferencesActivity.USERNAMEKEY, ""));*/
				Intent intent = new Intent(TimelineActivity.this, TimelinePull.class);
				intent.putExtra(PreferencesActivity.USERNAMEKEY, pref.getString(PreferencesActivity.USERNAMEKEY, "student"));
				intent.putExtra(UPDATEVIEW, true);
				startService(intent);
						
			}
		});
		//application.getUserTimeline(pref.getString(PreferencesActivity.USERNAMEKEY, ""));
		
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
		outState.putString(TIMELINEKEY, status.toString());
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		status = StatusActivity.Status.valueOf(savedInstanceState.getString(TIMELINEKEY));
		findViewById(R.id.submitStatusButton).setEnabled(status.compareTo(StatusActivity.Status.COMPLETED) == 0);
	}
}
