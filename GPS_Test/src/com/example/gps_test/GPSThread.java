package com.example.gps_test;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GPSThread extends Thread {
	public final String TAG = "GPS Thread";
	private LocationManager lm;
	private String lat = "-", lon = "-";

	public void run() {
		LocationListener locationListener = new MyLocationListener();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10,
				locationListener);
		Log.i(TAG, "Got Location Listener");
		Location loc = null;
		while (true) {
			Log.i(TAG, lat);
			Log.i(TAG, lon);
			try {
				if (loc != null) {
					Log.i(TAG, "" + loc.getLatitude());
					Log.i(TAG, "" + loc.getLatitude());
				}
				Thread.sleep(5000);
			} catch (InterruptedException ex) {
			}

		}
	}

	public void receiveLocManager(LocationManager lm) {
		this.lm = lm;
		Log.i(TAG, "Got Location Manager");
	}

	/*----------Listener class to get coordinates ------------- */
	private class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location loc) {

			lon = "Longitude: " + loc.getLongitude();
			Log.i(TAG, lon);
			lat = "Latitude: " + loc.getLatitude();
			Log.i(TAG, lat);

			/*----------to get City-Name from coordinates ------------- */
			/*
			 * String cityName = null; Geocoder gcd = new
			 * Geocoder(getBaseContext(), Locale.getDefault()); List<Address>
			 * addresses; try { addresses =
			 * gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1); if
			 * (addresses.size() > 0)
			 * System.out.println(addresses.get(0).getLocality()); cityName =
			 * addresses.get(0).getLocality(); } catch (IOException e) {
			 * e.printStackTrace(); }
			 */
			// String s = longitude+"\n"+latitude +
			// "\n\nMy Currrent City is: "+cityName;

		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}
}
