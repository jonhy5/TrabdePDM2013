package pdm.aulas.desafio2;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivityV2 extends Activity {

	private final String APP_TAG = "Challenge2";
	
	private Challenge2Application _application;
	
	private static final int CANCELED = -1, COMPLETE = 100;
	private String _currentStateKey;
	private int _currentProgress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		Log.v(APP_TAG, "onCreate() thread id = " + Thread.currentThread().getId());
		setContentView(R.layout.activity_main);
		
		_currentStateKey = getComponentName().flattenToString();
		_application = (Challenge2Application) getApplication();
		
		((Button) findViewById(R.id.start_button)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				findViewById(R.id.start_button).setEnabled(false);
				findViewById(R.id.cancel_button).setEnabled(true);
				_application.startWork();
			}
		});
		
		((Button) findViewById(R.id.cancel_button)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				_application.cancelWork();
			}
		});
		
		_application.setProgressListener(new Challenge2Application.ProgressListener() {
			@Override
			public void progressReport(int progress) 
			{
				TextView label = (TextView) findViewById(R.id.progress); 
				label.setText(Integer.toString(_currentProgress = progress) + '%');
				Log.v(APP_TAG, "progressReport() thread id = " + Thread.currentThread().getId() 
						+ "; label hash =" + label.hashCode());
			}

			@Override
			public void completeReport(boolean canceled) 
			{
				findViewById(R.id.start_button).setEnabled(true);
				findViewById(R.id.cancel_button).setEnabled(false);
				if(canceled)
				{
					((TextView) findViewById(R.id.progress)).setText(R.string.canceled_progress);
					_currentProgress = CANCELED;
				}
			}
		});
		
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) 
	{
		super.onRestoreInstanceState(savedInstanceState);
		Log.v(APP_TAG, "onRestoreInstanceState() : activity.hash = " + this.hashCode());
		_currentProgress = savedInstanceState.getInt(_currentStateKey);
		String labelText = _currentProgress == CANCELED 
				? getResources().getString(R.string.canceled_progress)
				: Integer.toString(_currentProgress) + '%';
		((TextView) findViewById(R.id.progress)).setText(labelText);
		boolean startEnabled = _currentProgress == COMPLETE || _currentProgress == CANCELED;
		findViewById(R.id.start_button).setEnabled(startEnabled);
		findViewById(R.id.cancel_button).setEnabled(!startEnabled);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{
		Log.v(APP_TAG, "onSaveInstanceState() : activity.hash = " + this.hashCode());
		outState.putInt(_currentStateKey, _currentProgress);
		super.onSaveInstanceState(outState);
	}
}
