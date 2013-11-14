package com.gps_test;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocationPullService extends IntentService {
	private static String TAG = "Location Pull Service";
	private boolean ductTape = false;

	// private String locationProvider = LocationManager.NETWORK_PROVIDER;
	private String locationProvider = LocationManager.GPS_PROVIDER;

	private LocationManager locationManager;

	protected double latitude;
	protected double altitude;
	protected double longitude;;

	public LocationPullService() {
		super(TAG);
		Log.i(TAG, "Created");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i(TAG, "Received Intent");
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		locationManager.requestLocationUpdates(locationProvider, 0, 0,
				locationListener);
		while (!ductTape) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				Log.e(TAG, e.toString());
			}
		}

		// is not reaching this point, but does when while loop removed Not
		// calling listener
		Log.i(TAG, "Location Pull done");
	}

	// Define a listener that responds to location updates
	LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			Log.i(TAG, "latitude: " + location.getLatitude());
			Log.i(TAG, "longitude: " + location.getLongitude());
			Log.i(TAG, "altitude: " + location.getAltitude());

			latitude = location.getLatitude();
			longitude = location.getLongitude();
			altitude = location.getAltitude();
			locationManager.removeUpdates(this);
			ductTape = true;
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onProviderDisabled(String provider) {
		}
	};

}
