package com.grupo5.trabetapa1.activities;

import java.util.List;

import winterwell.jtwitter.Twitter.Status;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import com.grupo5.trabetapa1.R;
import com.grupo5.trabetapa1.interfaces.UserTimelineListener;
import com.grupo5.trabetapa1.main.YambApplication;

public class TimelineActivity extends BaseActivity {
	private int maxListItems;
	private YambApplication application;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		Log.v(ACTIVITY_SERVICE, "Oncreate Timeline");

		SharedPreferences pref = getSharedPreferences(YambApplication.preferencesFileName, MODE_PRIVATE);
		maxListItems = Integer.parseInt(pref.getString(PreferencesActivity.MAXMSGKEY, "20"));
		
		application = (YambApplication) getApplication();
		application.setUserTimelineListener(new UserTimelineListener() {
			@Override
			public void completeReport(List<Status> list) {
				GridView gridView = (GridView) findViewById(R.id.timelineGridView);
				gridView.setAdapter(new TimelineAdapter(TimelineActivity.this, list));
				
				gridView.setOnItemClickListener(new OnItemClickListener() {

			        @Override
			        public void onItemClick(AdapterView<?> arg0, View arg1,int position, long id) {

			        	Log.v(ACTIVITY_SERVICE, "onItemClick");

			        	Status status = (Status) arg0.getAdapter().getItem(position);
			        	
						
			        	
			    		Intent intent = new Intent(TimelineActivity.this, DetailedActivity.class);
			    		intent.putExtra("id", status.getId());
			    		intent.putExtra("autor", status.getUser().toString());
			    		intent.putExtra("msg", status.getText());
			    		intent.putExtra("dt", status.getCreatedAt());
			    		

			    		startActivity(intent);
			        }
			    });

				
			}
		});
		
		application.getUserTimeline(pref.getString(PreferencesActivity.USERNAMEKEY, ""));
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
}
