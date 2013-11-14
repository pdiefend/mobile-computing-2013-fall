package com.gps_test;

import android.util.Log;

import com.sgp.SGP_API;
import com.sgp.SGP_Location;

public class GPS_Thread extends Thread {
	private double lat;
	private double lon;
	private double alt;

	@Override
	public void run() {

		SGP_API iss = new SGP_API(tle, new SGP_Location("Bucknell", lat, lon,
				(int) alt, -5));
		iss.calculatePosition();
		Log.i("GPS_Thread", "azimuth: " + iss.getAzimuth());
		Log.i("GPS_Thread", "elevation: " + iss.getElevation());
		Log.i("GPS_Thread", "visible: " + iss.isVisible());
	}

	public void recieveData(double lat, double lon, double alt) {
		this.lat = lat;
		this.lon = lon;
		this.alt = alt;
	}

}
