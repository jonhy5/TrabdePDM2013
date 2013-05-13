package pdm.aulas.desafio4;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class LocalBoundService extends Service {

	@Override
	public void onCreate() 
	{
		super.onCreate();
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
		class EchoBinder extends Binder implements ILocalBoundService
		{
			@Override
			public String echo(String msg) 
			{
				Toast.makeText(LocalBoundService.this, "echo()", Toast.LENGTH_LONG).show();
				return msg + " echo in thread id = " + Thread.currentThread().getId();
			}
		}
		
		Toast.makeText(this, "onBind()", Toast.LENGTH_LONG).show();
		return new EchoBinder();
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
