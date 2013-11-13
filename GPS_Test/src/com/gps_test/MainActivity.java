package com.gps_test;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.example.gps_test.R;

public class MainActivity extends Activity {
	public final String TAG = "MainActivity";
	private boolean duckTape = false;

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

		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {

				if (!duckTape) {
					// Called when a new location is found by the network
					// location
					// provider.
					// makeUseOfNewLocation(location);
					Log.i(TAG, "latitude: " + location.getLatitude());
					Log.i(TAG, "longitude: " + location.getLongitude());
					Log.i(TAG, "altitude: " + location.getAltitude());

					GPS_Thread t = new GPS_Thread();
					t.recieveData(location.getLatitude(),
							location.getLongitude(), location.getAltitude());
					t.start();
					duckTape = true;
				}
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};

		// Register the listener with the Location Manager to receive location
		// updates
		String locationProvider = LocationManager.NETWORK_PROVIDER;
		// Or, use GPS location data:
		// String locationProvider = LocationManager.GPS_PROVIDER;

		locationManager.requestLocationUpdates(locationProvider, 0, 0,
				locationListener);

		// GPSThread thread = new GPSThread();

		// LocationManager locationManager = (LocationManager)
		// getSystemService(Context.LOCATION_SERVICE);
		// thread.receiveLocManager(locationManager);
		// thread.run();
		return true;
	}
}
