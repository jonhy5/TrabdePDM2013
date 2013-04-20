package com.grupo5.trabetapa1.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.grupo5.trabetapa1.R;

public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.base, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent inten;
		
		switch(item.getItemId()){
			case R.id.Timeline:
				inten = new Intent(this,TimelineActivity.class);
				startActivity(inten);
				return true;
				
			case R.id.Status:
				inten = new Intent(this,StatusActivity.class);
				startActivity(inten);
				return true;
		 
			case R.id.preferences:
				inten = new Intent(this,PreferencesActivity.class);
				startActivity(inten);
				return true;
			
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
