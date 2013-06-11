package com.grupo5.trabetapa1.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.grupo5.trabetapa1.R;
import com.grupo5.trabetapa1.main.YambApplication;
import com.grupo5.trabetapa1.services.StatusUpload;

public class StatusActivity extends BaseActivity {
	private static final String TAG = YambApplication.class.getSimpleName();
	private int maxChars;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		Log.v(TAG, "onCreate() thread id = " + Thread.currentThread().getId());
		
		this.setTitle(R.string.title_activity_status);
		setContentView(R.layout.activity_status);
		
		SharedPreferences pref = getSharedPreferences(YambApplication.preferencesFileName, MODE_PRIVATE);
		maxChars = Integer.parseInt(pref.getString(PreferencesActivity.MAXCHARKEY, "140"));
		
		TextView textCounter = (TextView) findViewById(R.id.statusTextCounterTextView);
		EditText statusText = (EditText) findViewById(R.id.statusEditText);
		Button submitButton = (Button) findViewById(R.id.submitStatusButton);
		
		textCounter.setText(Integer.toString(maxChars)); 
		
		statusText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxChars)});
		statusText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				int count = maxChars - ((EditText) findViewById(R.id.statusEditText)).length();
				((TextView) findViewById(R.id.statusTextCounterTextView)).setText(Integer.toString(count));				
				((Button) findViewById(R.id.submitStatusButton)).setEnabled(((EditText) findViewById(R.id.statusEditText)).length() > 0);
			}
		});
	    
		submitButton.setEnabled(false);
		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StatusActivity.this, StatusUpload.class);
				intent.setAction(StatusUpload.SUBMIT_ACTION);
				intent.putExtra(StatusUpload.EXTRA_KEY, ((EditText) findViewById(R.id.statusEditText)).getText().toString());
				startService(intent);
			}
		});
	}
}
