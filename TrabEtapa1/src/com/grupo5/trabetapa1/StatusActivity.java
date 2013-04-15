package com.grupo5.trabetapa1;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class StatusActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		getMenuInflater().inflate(R.menu.actionbar_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.)
		
		if(item.getItemId() == R.id.Timeline){
			Intent inten = new Intent(StatusActivity.this,TimelineActivity.class);
			startActivity(inten);
			return true;
		}
		else
		return false;
	}
}
