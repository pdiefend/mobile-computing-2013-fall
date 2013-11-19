package com.gps_test;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
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
	private String locationProvider = LocationManager.NETWORK_PROVIDER;
	// private String locationProvider = LocationManager.GPS_PROVIDER;

	private double latitude;
	private double longitude;
	private double altitude;
	private String TLE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.i(TAG, "Created");

		// hardcoded test functionality
		String[] extras = { "list", "" };

		// extras[0] = "list"; extras[1] = ""; // to list available TLEs
		// extras[0] = "get"; extras[1] = "xxxxx"; // to get TLE by sat num
		// extras[0] = "update"; extras[1] = "xxxxx"; // to force download of
		// TLE by sat num

		Intent mServiceIntent = new Intent(this, TLEPullService.class);
		mServiceIntent.putExtra(TLEPullService.EXTRAS, extras);
		this.startService(mServiceIntent);
		Log.i(TAG, "Download Service Started");
		// Response will come in the broadcast receiver below

		// Register the listener with the Location Manager to receive location
		// updates
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		locationManager.requestSingleUpdate(locationProvider, locationListener,
				Looper.getMainLooper());

		IntentFilter mStatusIntentFilter = new IntentFilter(
				Constants.BROADCAST_ACTION);

		// Instantiates a new DownloadStateReceiver
		TLEResponseReceiver mDownloadStateReceiver = new TLEResponseReceiver();
		// Registers the DownloadStateReceiver and its intent filters
		LocalBroadcastManager.getInstance(this).registerReceiver(
				mDownloadStateReceiver, mStatusIntentFilter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Computes the pointing vector from the user's current location. Takes
	 * virtually no time to execute
	 * 
	 * @param v
	 *            current view. Not used except for button
	 */
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

	// Broadcast receiver for receiving status updates from the
	// IntentService
	private class TLEResponseReceiver extends BroadcastReceiver {

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

	// Acquire a reference to the system Location Manager
	private LocationManager locationManager;
	// Define a listener that responds to location updates
	LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {

			// Called when a new location is found by the network
			// location
			// provider.
			Log.i(TAG, "latitude: " + location.getLatitude());
			Log.i(TAG, "longitude: " + location.getLongitude());
			Log.i(TAG, "altitude: " + location.getAltitude());

			latitude = location.getLatitude();
			longitude = location.getLongitude();
			altitude = location.getAltitude();

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onProviderDisabled(String provider) {
		}
	};
}
