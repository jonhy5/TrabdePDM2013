package com.grupo5.trabetapa1.activities;

import com.grupo5.trabetapa1.R;
import com.grupo5.trabetapa1.R.id;
import com.grupo5.trabetapa1.R.layout;
import com.grupo5.trabetapa1.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

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
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		 case R.id.Timeline:{Intent inten = new Intent(this,TimelineActivity.class);
			startActivity(inten);
			return true;}
		 case R.id.Status:{Intent inten = new Intent(this,StatusActivity.class);
			startActivity(inten);
			return true;}
		 
		 case R.id.preferences:{
			 //Intent inten = new Intent(this,PreferencesActivity.class);
			//startActivity(inten);
			//return true;}
			 Intent inten = new Intent(this,TimelineActivity.class);
				startActivity(inten);
				return true;}
		 	default : return super.onOptionsItemSelected(item);
		 }
	}

}
