package com.grupo5.trabetapa1.activities;

import com.grupo5.trabetapa1.R;
import com.grupo5.trabetapa1.R.layout;
import com.grupo5.trabetapa1.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class DetailedActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("","onCreatedetailed");
		setContentView(R.layout.activity_detailed);
		
		Bundle bundle = getIntent().getExtras();
		DetailData d =  (DetailData)bundle.getParcelable("detail");
		
		
		TextView txtid =(TextView) findViewById(R.id.textIdSeq);
		txtid.setText(d.getId());
		
		TextView txtautor =(TextView) findViewById(R.id.textAutor);
		txtautor.setText(d.getAutor());
		
		TextView txtmsg =(TextView) findViewById(R.id.txtMsg);
		txtmsg.setText(d.getMsg());
		
		TextView txtDt =(TextView) findViewById(R.id.textDt);
		txtDt.setText(d.getDate());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detailed, menu);
		return true;
	}

}
