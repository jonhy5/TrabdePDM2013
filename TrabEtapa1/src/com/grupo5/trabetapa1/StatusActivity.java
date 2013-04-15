package com.grupo5.trabetapa1;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class StatusActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);
		((Button)findViewById(R.id.btn1)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent inten = new Intent(StatusActivity.this,TimelineActivity.class);
				startActivity(inten);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.status, menu);
		return true;
	}

}
