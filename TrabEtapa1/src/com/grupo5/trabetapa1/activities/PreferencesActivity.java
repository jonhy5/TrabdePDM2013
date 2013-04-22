package com.grupo5.trabetapa1.activities;

import winterwell.jtwitter.Twitter.User;

import com.grupo5.trabetapa1.R;
import com.grupo5.trabetapa1.main.YambApplication;

import android.widget.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class PreferencesActivity extends Activity {
	private EditText usr;
	private EditText pass;
	private EditText uri;
	private EditText maxmsg;
	private EditText maxchar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferences);
		
		((Button)findViewById(R.id.but1_PrefApply)).setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				usr = (EditText)findViewById(R.id.User);
				pass = (EditText)findViewById(R.id.Pass);
				uri = (EditText)findViewById(R.id.baseURI);
				maxmsg = (EditText) findViewById(R.id.MaxNumMsg);
				maxchar = (EditText) findViewById(R.id.NumChar);
				YambApplication.pref = getSharedPreferences(YambApplication.preferencesFileName, 0);
				YambApplication.pref.edit().putString("UserName",usr.getText().toString());
				YambApplication.pref.edit().putString("PassWord",pass.getText().toString());
				YambApplication.pref.edit().putString("BaseUri",uri.getText().toString());
				YambApplication.pref.edit().putString("MaxMsg",maxmsg.getText().toString());
				YambApplication.pref.edit().putString("MaxChar",maxchar.getText().toString());
				YambApplication.pref.edit().commit();
				User u = YambApplication.twitter.getUser(YambApplication.pref.getString("UserName", "bad"));
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
