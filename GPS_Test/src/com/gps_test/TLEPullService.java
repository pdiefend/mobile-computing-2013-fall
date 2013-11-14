package com.gps_test;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.sgp.SGP_TLE_Download;

public class TLEPullService extends IntentService {
	private String TAG = "TLE Pull Service";
	SGP_TLE_Download downloader;
	private String result;

	public TLEPullService() {
		super("TLEPullService");
		this.downloader = new SGP_TLE_Download();
		// TODO Auto-generated constructor stub
		Log.i(TAG, "Created");
	}

	@Override
	protected void onHandleIntent(Intent workIntent) {
		Log.i(TAG, "Received Intent");
		// Gets data from the incoming Intent
		String dataString = workIntent.getDataString();

		// Do work here, based on the contents of dataString
		result = downloader.downloadTLE(dataString);

		/*
		 * Creates a new Intent containing a Uri object BROADCAST_ACTION is a
		 * custom Intent action
		 */
		Intent localIntent = new Intent(Constants.BROADCAST_ACTION)
		// Puts the status into the Intent
				.putExtra(Constants.EXTENDED_DATA_STATUS, result);
		// Broadcasts the Intent to receivers in this app.
		LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
	}

}
