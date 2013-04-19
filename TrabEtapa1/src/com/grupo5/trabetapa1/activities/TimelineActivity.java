package com.grupo5.trabetapa1.activities;

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

	ListView lv_Timeline;
	StatusActivity status;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		Log.v(ACTIVITY_SERVICE, "Oncreate Timeline");

		
		// Find views by id    
		lv_Timeline = (ListView) findViewById(R.id.Timeline);
		
		
		String lista[] = new String[]{"Item - 1", "Item - 2", "Item - 3", "Item - 4"};
		lv_Timeline.setAdapter(new TimelineAdapter(this, lista));
		
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
