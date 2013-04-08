package pdm.aulas.desafio2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private final String APP_TAG = "Challenge2";
	private AsyncTask<Void, Integer, Void> _currentTask = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		Log.v(APP_TAG, "onCreate() thread id = " + Thread.currentThread().getId());
		setContentView(R.layout.activity_main);
		
		((Button) findViewById(R.id.start_button)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				_currentTask = new AsyncTask<Void, Integer, Void>() {
					
					private void cleanUp(boolean canceled)
					{
						findViewById(R.id.start_button).setEnabled(true);
						findViewById(R.id.cancel_button).setEnabled(false);
						if(canceled)
							((TextView) findViewById(R.id.progress)).setText(R.string.canceled_progress);
						_currentTask = null;
					}
					
					@Override protected void onPreExecute() 
					{
						Log.v(APP_TAG, "onPreExecute() thread id = " + Thread.currentThread().getId());
						findViewById(R.id.start_button).setEnabled(false);
						findViewById(R.id.cancel_button).setEnabled(true);
					}
					
					@Override protected Void doInBackground(Void... params) 
					{
						Log.v(APP_TAG, "doInBackground() thread id = " + Thread.currentThread().getId());
						int currentProgress = 0;
						try {
							for(int progress = currentProgress; progress <= 100; progress += 5)
							{
								publishProgress(Integer.valueOf(progress));
								Thread.sleep(1000);
							}
						}
						catch(InterruptedException ie) { }
						return null;
					}

					@Override protected void onProgressUpdate(Integer... values) 
					{
						TextView label = (TextView) findViewById(R.id.progress); 
						label.setText(Integer.toString(values[0].intValue()) + '%');
						Log.v(APP_TAG, "progressReport() thread id = " + Thread.currentThread().getId() 
								+ "; label hash =" + label.hashCode());
					}

					@Override protected void onPostExecute(Void result) 
					{
						Log.v(APP_TAG, "onPostExecute() thread id = " + Thread.currentThread().getId());
						cleanUp(false);
					}

					@Override protected void onCancelled() 
					{
						Log.v(APP_TAG, "onCancelled() thread id = " + Thread.currentThread().getId());
						cleanUp(true);
					}
				}.execute();
			}
		});
		
		((Button) findViewById(R.id.cancel_button)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				if(_currentTask != null)
					_currentTask.cancel(true);
			}
		});
	}

	@Override
	protected void onDestroy() 
	{
		Log.v(APP_TAG, "onDestroy() thread id = " + Thread.currentThread().getId());
		/*
		if(_currentTask != null)
			_currentTask.cancel(true);
		*/
		super.onDestroy();
	}
}
