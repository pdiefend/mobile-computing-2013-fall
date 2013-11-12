package com.example.gps_test;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	public final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		Log.i(TAG, "Created");

		GPSThread thread = new GPSThread();

		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		thread.receiveLocManager(locationManager);
		thread.run();
		return true;
	}

}
