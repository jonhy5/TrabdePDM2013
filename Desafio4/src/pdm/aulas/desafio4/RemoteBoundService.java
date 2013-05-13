package pdm.aulas.desafio4;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

public class RemoteBoundService extends Service {

	private Handler _uiHandler;
	
	@Override
	public void onCreate() 
	{
		super.onCreate();
		_uiHandler = new Handler();
		Toast.makeText(this, "onCreate()", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDestroy() 
	{
		Toast.makeText(this, "onDestroy()", Toast.LENGTH_LONG).show();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) 
	{
		return new IRemoteBoundService.Stub() {
			
			@Override
			public String echo(String msg) throws RemoteException 
			{
				_uiHandler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(RemoteBoundService.this, "echo()", Toast.LENGTH_LONG).show();
					}
				});
				return msg + " echo in thread id = " + Thread.currentThread().getId();
			}
		};
	}

	@Override
	public boolean onUnbind(Intent intent) 
	{
		Toast.makeText(this, "onUnbind()", Toast.LENGTH_LONG).show();
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) 
	{
		Toast.makeText(this, "onRebind()", Toast.LENGTH_LONG).show();
		super.onRebind(intent);
	}

}
