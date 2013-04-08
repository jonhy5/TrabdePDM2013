package pdm.aulas.desafio2;

import android.os.AsyncTask;

/**
 * Non thread-safe. All methods must be called from the main thread 
 */
public class Challenge2Application extends android.app.Application {

	public static interface ProgressListener
	{
		void progressReport(int progress);
		void completeReport(boolean canceled);
	}
	
	private AsyncTask<Void, Integer, Void> _currentTask;
	private ProgressListener _listener;
	
	public void setProgressListener(ProgressListener listener)
	{
		_listener = listener;
	}

	public void startWork()
	{
		if(_currentTask != null)
			throw new IllegalStateException();
		
		_currentTask = new AsyncTask<Void, Integer, Void>() {
			
			@Override protected Void doInBackground(Void... params) 
			{
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
				if(_listener != null)
					_listener.progressReport(values[0].intValue());
			}

			@Override protected void onPostExecute(Void result) 
			{
				if(_listener != null)
					_listener.completeReport(false);
				_currentTask = null;
			}

			@Override protected void onCancelled() 
			{
				if(_listener != null)
					_listener.completeReport(true);
				_currentTask = null;
			}
		}.execute();
	}

	public void cancelWork()
	{
		if(_currentTask != null)
			_currentTask.cancel(true);
	}
}
