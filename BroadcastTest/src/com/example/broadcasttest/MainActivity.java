package com.example.broadcasttest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		(new Thread(new BroadcastThread(this))).start();
		Log.i("Activity", "Created");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.i("Activity", "Paused");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.i("Activity", "Stopped");
	}

}
