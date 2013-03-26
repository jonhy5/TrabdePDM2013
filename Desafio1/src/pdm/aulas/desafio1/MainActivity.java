package pdm.aulas.desafio1;

import pdm.aulas.desafio1.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	private final String APP_TAG = "Challenge1";
	
	private TextView _label;
	private int _currentState;
	private String _currentStateKey;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		Log.v(APP_TAG, "onCreate()");
		
		setContentView(R.layout.activity_main);
		_currentStateKey = getComponentName().flattenToString();
		_label = (TextView) findViewById(R.id.textView);
		final String off = getResources().getString(R.string.off);
		_currentState = _label.getText().equals(off) ? R.string.off : R.string.on;

		((Button) findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
			final EditText _editText = (EditText) findViewById(R.id.editText);
			@Override
			public void onClick(View v) 
			{
				// Let's toggle!
				int nextState = R.string.on;
				if(_currentState == R.string.on)
					nextState = R.string.off;
				
				String nextStateText = getResources().getString(_currentState = nextState);
				_label.setText(nextStateText);
				_editText.setText(nextStateText);
				Log.v(APP_TAG, "onClick() : label.hash = " + _label.hashCode() + "; edit.hash = " + _editText.hashCode());
			}
		});
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) 
	{
		super.onRestoreInstanceState(savedInstanceState);
		Log.v(APP_TAG, "onRestoreInstanceState() : activity.hash = " + this.hashCode());
		_label.setText(_currentState = savedInstanceState.getInt(_currentStateKey));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{
		Log.v(APP_TAG, "onSaveInstanceState() : activity.hash = " + this.hashCode());
		outState.putInt(_currentStateKey, _currentState);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() 
	{
		Log.v(APP_TAG, "onDestroy()");
		super.onDestroy();
	}
}
