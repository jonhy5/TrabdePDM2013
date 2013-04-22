package com.grupo5.trabetapa1.activities;

import com.grupo5.trabetapa1.R;
import com.grupo5.trabetapa1.main.YambApplication;

import android.widget.*;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class PreferencesActivity extends Activity {
	public static final String USERNAMEKEY = "UserName";
	public static final String PASSWORDKEY = "PassWord";
	public static final String BASEURIKEY = "BaseUri";
	public static final String MAXMSGKEY = "MaxMsg";
	public static final String MAXCHARKEY = "MaxChar";
	private SharedPreferences pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferences);
		Log.v(ACTIVITY_SERVICE, "Oncreate Preferences");
		
		pref = getSharedPreferences(YambApplication.preferencesFileName, MODE_PRIVATE);
		((EditText) findViewById(R.id.statusEditText)).setText(pref.getString(USERNAMEKEY, "student"));
		((EditText) findViewById(R.id.Pass)).setText(pref.getString(PASSWORDKEY, "password"));
		((EditText) findViewById(R.id.baseURI)).setText(pref.getString(BASEURIKEY, "http://yamba.marakana.com/api"));
		((EditText) findViewById(R.id.MaxNumMsg)).setText(pref.getString(MAXMSGKEY, "20"));
		((EditText) findViewById(R.id.NumChar)).setText(pref.getString(MAXCHARKEY, "140"));
		
		((Button)findViewById(R.id.but1_PrefApply)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// Save preferences
				pref = getSharedPreferences(YambApplication.preferencesFileName, MODE_PRIVATE);
				Editor edit = pref.edit();
				edit.clear();
				edit.putString(USERNAMEKEY,((EditText) findViewById(R.id.statusEditText)).getText().toString());
				edit.putString(PASSWORDKEY,((EditText) findViewById(R.id.Pass)).getText().toString());
				edit.putString(BASEURIKEY,((EditText) findViewById(R.id.baseURI)).getText().toString());
				edit.putString(MAXMSGKEY,((EditText) findViewById(R.id.MaxNumMsg)).getText().toString());
				edit.putString(MAXCHARKEY,((EditText) findViewById(R.id.NumChar)).getText().toString());
				edit.commit();
				
				// Redirect to Timeline activity
				Intent inten = new Intent(PreferencesActivity.this,TimelineActivity.class);
				startActivity(inten);
			}			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preferences, menu);
		return true;
	}
}