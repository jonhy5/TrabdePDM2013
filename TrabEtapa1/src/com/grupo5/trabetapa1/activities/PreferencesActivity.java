package com.grupo5.trabetapa1.activities;

import com.grupo5.trabetapa1.R;
import com.grupo5.trabetapa1.main.YambApplication;

import android.widget.*;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class PreferencesActivity extends Activity {
	private SharedPreferences pref;
	private EditText usr;
	private EditText pass;
	private EditText uri;
	private EditText maxmsg;
	private EditText maxchar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferences);
		Log.v(ACTIVITY_SERVICE, "Oncreate Preferences");
		((Button)findViewById(R.id.but1_PrefApply)).setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				usr = (EditText)findViewById(R.id.User);
				pass = (EditText)findViewById(R.id.Pass);
				uri = (EditText)findViewById(R.id.baseURI);
				maxmsg = (EditText) findViewById(R.id.MaxNumMsg);
				maxchar = (EditText) findViewById(R.id.NumChar);
				pref = getSharedPreferences(YambApplication.preferencesFileName, 0);
				pref.edit().putString("UserName",usr.getText().toString());
				pref.edit().putString("PassWord",pass.getText().toString());
				pref.edit().putString("BaseUri",uri.getText().toString());
				pref.edit().putString("MaxMsg",maxmsg.getText().toString());
				pref.edit().putString("MaxChar",maxchar.getText().toString());
				pref.edit().commit();
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
