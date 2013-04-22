package com.grupo5.trabetapa1.activities;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;

import com.grupo5.trabetapa1.R;
import com.grupo5.trabetapa1.R.layout;
import com.grupo5.trabetapa1.R.menu;
import com.grupo5.trabetapa1.main.YambApplication;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class TimelineActivity extends BaseActivity {

	private YambApplication application;
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		Log.v(ACTIVITY_SERVICE, "Oncreate Timeline");

		application = (YambApplication) getApplication();
		
		Twitter tw = application.getTwitter();
		SharedPreferences shp = getSharedPreferences(YambApplication.preferencesFileName, 0);
		
		String maxItens = shp.getString("MaxMsg", null);
		
		/*
		List<Status> list = tw.getUserTimeline();
		
		
		for (Status status : list) {
           Log.v("Timeline", status.getUser().getName() + ":" + status.getText() );
		}
		*/
		
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
