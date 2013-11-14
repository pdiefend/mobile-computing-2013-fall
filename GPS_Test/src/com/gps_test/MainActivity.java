package com.gps_test;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.example.gps_test.R;
import com.sgp.SGP_API;
import com.sgp.SGP_Location;
import com.sgp.SGP_TLE;

public class MainActivity extends Activity {
	public final String TAG = "MainActivity";
	// private String locationProvider = LocationManager.NETWORK_PROVIDER; //
	// Or,
	// use
	// GPS
	// location
	// data:
	// LocationManager.GPS_PROVIDER;
	private double latitude;
	private double longitude;
	private double altitude;
	private String TLE;

	// Acquire a reference to the system Location Manager
	// private LocationManager locationManager;

	// Define a listener that responds to location updates
	// LocationListener locationListener = new LocationListener() {
	// public void onLocationChanged(Location location) {
	//
	// // Called when a new location is found by the network
	// // location
	// // provider.
	// // makeUseOfNewLocation(location);
	// Log.i(TAG, "latitude: " + location.getLatitude());
	// Log.i(TAG, "longitude: " + location.getLongitude());
	// Log.i(TAG, "altitude: " + location.getAltitude());
	//
	// latitude = location.getLatitude();
	// longitude = location.getLongitude();
	// altitude = location.getAltitude();
	// // GPS_Thread t = new GPS_Thread();
	// // t.recieveData(location.getLatitude(), location.getLongitude(),
	// // location.getAltitude());
	// // t.start();
	//
	// }
	//
	// public void onStatusChanged(String provider, int status, Bundle extras) {
	// }
	//
	// public void onProviderEnabled(String provider) {
	// }
	//
	// public void onProviderDisabled(String provider) {
	// }
	// };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.i(TAG, "Created");

		// Register the listener with the Location Manager to receive location
		// updates

		Intent mServiceIntent = new Intent(this, TLEPullService.class);
		mServiceIntent.setData(Uri.parse("25544")); // hardcoded get ISS
		this.startService(mServiceIntent);

		Log.i(TAG, "Download Service Started");

		mServiceIntent = new Intent(this, LocationPullService.class);
		this.startService(mServiceIntent);

		Log.i(TAG, "Location Service Started");

		// locationManager = (LocationManager) this
		// .getSystemService(Context.LOCATION_SERVICE);
		//
		// locationManager.requestSingleUpdate(locationProvider,
		// locationListener,
		// Looper.getMainLooper());

		IntentFilter mStatusIntentFilter = new IntentFilter(
				Constants.BROADCAST_ACTION);

		// Instantiates a new DownloadStateReceiver
		TLEResponseReceiver mDownloadStateReceiver = new TLEResponseReceiver();
		// Registers the DownloadStateReceiver and its intent filters
		LocalBroadcastManager.getInstance(this).registerReceiver(
				mDownloadStateReceiver, mStatusIntentFilter);
		// GPSThread thread = new GPSThread();

		// LocationManager locationManager = (LocationManager)
		// getSystemService(Context.LOCATION_SERVICE);
		// thread.receiveLocManager(locationManager);
		// thread.run();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void compute(View v) {
		Log.i(TAG, "Compute pressed");

		SGP_TLE tle = new SGP_TLE(TLE);
		SGP_API iss = new SGP_API(tle, new SGP_Location("Current Location",
				latitude, longitude, (int) altitude, -5));
		iss.calculatePosition();
		Log.i("GPS_Thread", "azimuth: " + iss.getAzimuth());
		Log.i("GPS_Thread", "elevation: " + iss.getElevation());
		Log.i("GPS_Thread", "visible: " + iss.isVisible());
	}

	private class TLEResponseReceiver extends BroadcastReceiver {

		// Broadcast receiver for receiving status updates from the
		// IntentService
		// Prevents instantiation
		private TLEResponseReceiver() {
		}

		// Called when the BroadcastReceiver gets an Intent it's registered to
		// receive
		public void onReceive(Context context, Intent intent) {

			Bundle extras = intent.getExtras();
			TLE = extras.getString(Constants.EXTENDED_DATA_STATUS);
			Log.i(TAG, TLE);

		}
	}
}
