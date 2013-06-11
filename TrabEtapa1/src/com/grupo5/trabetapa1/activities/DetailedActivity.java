package com.grupo5.trabetapa1.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.grupo5.trabetapa1.R;
import com.grupo5.trabetapa1.parcelable.DetailData;

public class DetailedActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("","onCreatedetailed");
		setContentView(R.layout.activity_detailed);
		
		Bundle bundle = getIntent().getExtras();
		final DetailData detailData =  (DetailData)bundle.getParcelable("detail");
		
		((TextView) findViewById(R.id.textIdSeq)).setText(detailData.getId());
		((TextView) findViewById(R.id.textAutor)).setText(detailData.getAutor());
		((TextView) findViewById(R.id.txtMsg)).setText(detailData.getMsg());
		((TextView) findViewById(R.id.textDt)).setText(detailData.getDate());
		((Button) findViewById(R.id.buttonFinish)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		((Button) findViewById(R.id.buttonEmail)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.shareemailsubject));
				i.putExtra(Intent.EXTRA_TEXT   , detailData.getMsg());
				try {
				    startActivity(Intent.createChooser(i, getResources().getString(R.string.bntEmail) + "..."));
				} catch (android.content.ActivityNotFoundException ex) {
				    Toast.makeText(DetailedActivity.this, getResources().getString(R.string.noemailclient), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
		
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detailed, menu);
		return true;
	}
}
