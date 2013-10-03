/**
 * 
 */
package com.example.broadcasttest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * @author prd005
 * 
 */
public class LocalService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Log.i("Service", "Binded");
		return null;

	}

	@Override
	public void onCreate() {
		Log.i("Service", "Created");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.i("Service", "Started");
	}

	@Override
	public void onDestroy() {
		Log.i("Service", "Destroyed");
	}

	public class MyLocalBinder extends Binder {
		LocalService getService() {
			return LocalService.this;
		}
	}
}