package pdm.aulas.desafio4;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;

public class MainActivity extends Activity {

	private static boolean REMOTE = true;
	
	private Button _bindButton, _unbindButton, _callButton;
	private ILocalBoundService _localBinder;
	private IRemoteBoundService _remoteBinder;
	private Intent _bindIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		_bindIntent = new Intent(MainActivity.this, REMOTE ? RemoteBoundService.class : LocalBoundService.class);
		
		final ServiceConnection connection = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) 
			{
				Toast.makeText(MainActivity.this, "onServiceConnected()", Toast.LENGTH_LONG).show();
				if(REMOTE)
				{
					_remoteBinder = IRemoteBoundService.Stub.asInterface(service);
					_localBinder = null;
				}
				else {
					_remoteBinder = null;
					_localBinder = (ILocalBoundService) service;
				}
			}

			@Override
			public void onServiceDisconnected(ComponentName name) 
			{
				Toast.makeText(MainActivity.this, "onServiceDisconnected()", Toast.LENGTH_LONG).show();
				_remoteBinder = null;
				_localBinder = null;
			}
			
		};
		
		(_bindButton = (Button) findViewById(R.id.button_bind)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				bindService(_bindIntent, connection, Service.BIND_AUTO_CREATE);
				_bindButton.setEnabled(false);
				_unbindButton.setEnabled(true);
				_callButton.setEnabled(true);
			}
		});
		
		(_unbindButton = (Button) findViewById(R.id.button_unbind)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				unbindService(connection);
				_bindButton.setEnabled(true);
				_unbindButton.setEnabled(false);
				_callButton.setEnabled(false);
			}
		});
		
		(_callButton = (Button) findViewById(R.id.button_call)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				if(REMOTE)
					try {
						Toast.makeText(MainActivity.this, _remoteBinder.echo("SLB! "), Toast.LENGTH_LONG).show();
					}
					catch(RemoteException ignored) { }
				else
					Toast.makeText(MainActivity.this, _localBinder.echo("SLB! "), Toast.LENGTH_LONG).show();
				
			}
		});
	}
}
