package com.gps_test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.sgp.SGP_TLE_Download;

public class TLEPullService extends IntentService {
	public final static String EXTRAS = "com.gps_test.EXTRAS";
	private String TAG = "TLE Pull Service";
	SGP_TLE_Download downloader;
	private String result;

	public TLEPullService() {
		super("TLEPullService");
		this.downloader = new SGP_TLE_Download();
		Log.i(TAG, "Created");
	}

	@Override
	protected void onHandleIntent(Intent workIntent) {
		try {
			Log.i(TAG, "Received Intent");
			// Gets data from the incoming Intent
			// String dataString = workIntent.getDataString();
			String[] extras = workIntent
					.getStringArrayExtra(TLEPullService.EXTRAS);

			if (extras.length == 2 && this.isExternalStorageWritable()) {

				if (extras[0].contains("get")) {
					// check if we have the TLE in local storage
					File dir = getExternalFilesDir(null);
					String[] list = dir.list();

					if (Arrays.asList(list).indexOf("tles.txt") < 0) {
						Log.i(TAG, "TLE File not found. Creating it now.");
						// File not found, create it and download the TLE

						// Download the TLE
						result = downloader.downloadTLE(extras[1]);
						/*
						 * Creates a new Intent containing a Uri object
						 * BROADCAST_ACTION is a custom Intent action
						 */
						Intent localIntent = new Intent(
								Constants.BROADCAST_ACTION)
						// Puts the status into the Intent
								.putExtra(Constants.EXTENDED_DATA_STATUS,
										result);
						// Broadcasts the Intent to receivers in this app.
						LocalBroadcastManager.getInstance(this).sendBroadcast(
								localIntent);

						// save the TLE
						PrintWriter pw = new PrintWriter(new FileWriter(
								dir.getAbsolutePath() + "tles.txt", true));

						pw.println(result);
						pw.close();
					} else {
						// file found
						Log.i(TAG, "TLE File found.");

						// search for the TLE
						BufferedReader reader = new BufferedReader(
								new FileReader(dir.getAbsolutePath()
										+ "tles.txt"));
						String line = reader.readLine();
						while (line != null) {
							System.out.println(line);
							line = reader.readLine();
						}
						reader.close();
						// store in result
						result = "";

						// NOT DONE
						// YET!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

						Intent localIntent = new Intent(
								Constants.BROADCAST_ACTION)
						// Puts the status into the Intent
								.putExtra(Constants.EXTENDED_DATA_STATUS,
										result);
						// Broadcasts the Intent to receivers in this app.
						LocalBroadcastManager.getInstance(this).sendBroadcast(
								localIntent);
					}

				} else if (extras[0].contains("update")) {

					// store in location storage
					// check if we have the TLE in local storage
					File dir = getExternalFilesDir(null);
					String[] list = dir.list();

					if (Arrays.asList(list).indexOf("tles.txt") < 0) {
						Log.i(TAG, "TLE File not found. Creating it now.");
						// File not found, create it and download the TLE

						// Download the TLE
						result = downloader.downloadTLE(extras[1]);
						/*
						 * Creates a new Intent containing a Uri object
						 * BROADCAST_ACTION is a custom Intent action
						 */
						Intent localIntent = new Intent(
								Constants.BROADCAST_ACTION)
						// Puts the status into the Intent
								.putExtra(Constants.EXTENDED_DATA_STATUS,
										result);
						// Broadcasts the Intent to receivers in this app.
						LocalBroadcastManager.getInstance(this).sendBroadcast(
								localIntent);

						// save the TLE
						PrintWriter pw = new PrintWriter(new FileWriter(
								dir.getAbsolutePath() + "tles.txt", true));

						pw.println(result);
						pw.close();
					} else {
						// file found
						Log.i(TAG, "TLE File found.");

						// search for the TLE
						BufferedReader reader = new BufferedReader(
								new FileReader(dir.getAbsolutePath()
										+ "tles.txt"));
						String line = reader.readLine();
						while (line != null) {
							System.out.println(line);
							line = reader.readLine();
						}
						// store the lines in a String except the one being
						// updated
						reader.close();
						// store in result
						result = "";

						// NOT DONE
						// YET!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

						Intent localIntent = new Intent(
								Constants.BROADCAST_ACTION)
						// Puts the status into the Intent
								.putExtra(Constants.EXTENDED_DATA_STATUS,
										result);
						// Broadcasts the Intent to receivers in this app.
						LocalBroadcastManager.getInstance(this).sendBroadcast(
								localIntent);

						// store the TLEs
						PrintWriter pw = new PrintWriter(new FileWriter(
								dir.getAbsolutePath() + "tles.txt", true));

						pw.println(result);
						// print the other TLEs back
					}

				} else {
					Log.e(TAG, "Unknown Command");
				}

			} else if (extras.length != 2) {
				Log.e(TAG, "Not Enough Parameters");
			} else if (!this.isExternalStorageWritable()) {
				Log.e(TAG, "Not Enough Parameters");
			} else {
				Log.e(TAG, "Jim, he's dead");
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

}
