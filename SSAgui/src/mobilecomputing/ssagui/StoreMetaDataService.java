package mobilecomputing.ssagui;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

public class StoreMetaDataService extends IntentService {
	private final static String TAG = "StoreMetaData";
	private final boolean STORE_DEBUG = true;

	public StoreMetaDataService() {
		super(TAG);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent workIntent) {
		try {
			Log.i(TAG, "Received Intent");
			String dataString = (String) workIntent.getExtras().get("data");
			if (this.isExternalStorageWritable()) {
				File dir = getExternalFilesDir(null);
				String[] list = dir.list();

				PrintWriter pw = new PrintWriter(new FileWriter(dir.getPath()
						+ "/data.txt", true));

				if (Arrays.asList(list).indexOf("data.txt") < 0) {
					if (STORE_DEBUG)
						Log.i(TAG, "MetaData File not found. Creating it now.");
					pw.println("image,latitude,longitude,altitude,azimuth,elevation");

				} else {
					if (STORE_DEBUG)
						Log.i(TAG, "TLE File found.");
				}
				// save the TLE
				pw.println(dataString);
				pw.close();

			} else {
				if (STORE_DEBUG)
					Log.e(TAG, "Storage Not Writeable");
			}
		} catch (Exception e) {
			if (STORE_DEBUG)
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