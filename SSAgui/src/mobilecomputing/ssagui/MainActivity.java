package mobilecomputing.ssagui;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import com.sgp.SGP_API;
import com.sgp.SGP_Location;
import com.sgp.SGP_TLE;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class MainActivity extends Activity implements SensorEventListener {
	private final static String TAG = "MainActivity";

	// GPS
	private String locationProvider = LocationManager.NETWORK_PROVIDER;
	// private String locationProvider = LocationManager.GPS_PROVIDER;
	public double latitude;
	public double longitude;
	public double altitude;
	private final boolean GPS_DEBUG = false;

	// Download
	private String TLE = "";
	private String satList;

	// Compass
	SensorManager sensorManager;
	private Sensor sensorAccelerometer;
	private Sensor sensorMagneticField;
	private float[] valuesAccelerometer;
	private float[] valuesMagneticField;
	private float[] matrixR;
	private float[] matrixI;
	private float[] matrixValues;
	// private double az, pt, rl;
	private final boolean COMPASS_DEBUG = false;
	private final boolean COMPUTE_DEBUG = false;
	private int dir = 0;
	public double cam_az;
	public double cam_el;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	// private int selectedIndex;
	private static ArrayList<String> mSatelliteTitles;

	static ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();
		mSatelliteTitles = new ArrayList<String>(80);
		mSatelliteTitles.add("ISS 25544");
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		adapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item,
				mSatelliteTitles);
		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the proper interactions between
		// the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}

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

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorAccelerometer = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorMagneticField = sensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		valuesAccelerometer = new float[3];
		valuesMagneticField = new float[3];

		matrixR = new float[9];
		matrixI = new float[9];
		matrixValues = new float[3];

		String[] extras = { "list", "", "" };

		Intent mServiceIntent = new Intent(this, TLEPullService.class);
		mServiceIntent.putExtra(TLEPullService.EXTRAS, extras);
		this.startService(mServiceIntent);
		Log.i(TAG, "Requesting List Service Started");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public void onPause() {
		sensorManager.unregisterListener(this, sensorAccelerometer);
		sensorManager.unregisterListener(this, sensorMagneticField);
		super.onPause();
	}

	public void onResume() {
		sensorManager.registerListener(this, sensorAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, sensorMagneticField,
				SensorManager.SENSOR_DELAY_NORMAL);
		super.onResume();
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		menu.findItem(R.id.action_addsate).setVisible(!drawerOpen);
		menu.findItem(R.id.action_resetloc).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_websearch:
			// create intent to perform web search for this satellite
			Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
			intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
			// catch event that there's no activity to handle intent
			if (intent.resolveActivity(getPackageManager()) != null) {
				startActivity(intent);
			} else {
				Toast.makeText(this, R.string.app_not_available,
						Toast.LENGTH_LONG).show();
			}
			return true;

		case R.id.action_addsate:
			// make a dialog fragment to add new satellite
			DialogFragment addSateFragment = new AddSatelliteDialogue();
			addSateFragment.show(getFragmentManager(), "Add Satellite");
			return true;

		case R.id.action_resetloc:
			Log.i(TAG, "Requested Loc Update");
			locationManager.requestSingleUpdate(locationProvider,
					locationListener, Looper.getMainLooper());
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listener for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
			displayedAlready = false;
		};
	}

	/* Actions after the item is selected */
	private void selectItem(int position) {
		// update the main content by replacing fragments
		Fragment fragment = new TrackingFragment();
		Bundle args = new Bundle();
		args.putInt(TrackingFragment.ARG_SATELLITE_NUMBER, position);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mSatelliteTitles.get(position));
		mDrawerLayout.closeDrawer(mDrawerList);

		String satNum = mSatelliteTitles.get(position);
		String satName = satNum.substring(0, satNum.length() - 6);
		satNum = satNum.substring(satNum.length() - 5);

		String[] extras = { "get", satNum, satName };
		Intent mServiceIntent = new Intent(this, TLEPullService.class);
		mServiceIntent.putExtra(TLEPullService.EXTRAS, extras);
		this.startService(mServiceIntent);
		Log.i(TAG, "Download Service Started");
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * Fragment that appears in the "content_frame", shows camera preview
	 */
	public static class TrackingFragment extends Fragment {
		public static final String ARG_SATELLITE_NUMBER = "satellite_number";

		public TrackingFragment() {
			// Empty constructor required for fragment subclasses
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_tracking,
					container, false);

			// Camera operations
			mImageView = (ImageView) rootView.findViewById(R.id.imageView1);
			mVideoView = (VideoView) rootView.findViewById(R.id.videoView1);
			mImageBitmap = null;
			mVideoUri = null;

			// Initiate arrow image
			mArrowUp = (ImageView) rootView.findViewById(R.id.arrow_up);
			mArrowDown = (ImageView) rootView.findViewById(R.id.arrow_down);
			mArrowLeft = (ImageView) rootView.findViewById(R.id.arrow_left);
			mArrowRight = (ImageView) rootView.findViewById(R.id.arrow_right);
			mReady = (ImageView) rootView.findViewById(R.id.action_ready);

			Button picBtn = (Button) rootView.findViewById(R.id.btnIntend);
			setBtnListenerOrDisable(picBtn, mTakePicOnClickListener,
					MediaStore.ACTION_IMAGE_CAPTURE);

			Button vidBtn = (Button) rootView.findViewById(R.id.btnIntendV);
			setBtnListenerOrDisable(vidBtn, mTakeVidOnClickListener,
					MediaStore.ACTION_VIDEO_CAPTURE);

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
				mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
			} else {
				mAlbumStorageDirFactory = new BaseAlbumDirFactory();
			}

			// ===========================
			// Start tracking service

			return rootView;
		}

		// Direction Arrows
		private ImageView mArrowUp;
		private ImageView mArrowDown;
		private ImageView mArrowLeft;
		private ImageView mArrowRight;
		private ImageView mReady;

		private static final int ACTION_TAKE_PHOTO_B = 1;
		private static final int ACTION_TAKE_PHOTO_S = 2;
		private static final int ACTION_TAKE_VIDEO = 3;

		private static final String BITMAP_STORAGE_KEY = "viewbitmap";
		private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
		private ImageView mImageView;
		private Bitmap mImageBitmap;

		private static final String VIDEO_STORAGE_KEY = "viewvideo";
		private static final String VIDEOVIEW_VISIBILITY_STORAGE_KEY = "videoviewvisibility";
		private VideoView mVideoView;
		private Uri mVideoUri;

		private String mCurrentPhotoPath;

		private static final String JPEG_FILE_PREFIX = "IMG_";
		private static final String JPEG_FILE_SUFFIX = ".jpg";

		private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

		/* Photo album for this application */
		private String getAlbumName() {
			return getString(R.string.album_name);
		}

		private File getAlbumDir() {
			File storageDir = null;

			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())) {

				storageDir = mAlbumStorageDirFactory
						.getAlbumStorageDir(getAlbumName());

				if (storageDir != null) {
					if (!storageDir.mkdirs()) {
						if (!storageDir.exists()) {
							Log.d("CameraSample", "failed to create directory");
							return null;
						}
					}
				}

			} else {
				Log.v(getString(R.string.app_name),
						"External storage is not mounted READ/WRITE.");
			}

			return storageDir;
		}

		private File createImageFile() throws IOException {
			// Create an image file name
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
					.format(new Date());
			String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
			File albumF = getAlbumDir();
			File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX,
					albumF);

			String args = imageFileName + ","
					+ ((MainActivity) getActivity()).latitude + ","
					+ ((MainActivity) getActivity()).longitude + ","
					+ ((MainActivity) getActivity()).altitude + ","
					+ ((MainActivity) getActivity()).cam_az + ","
					+ ((MainActivity) getActivity()).cam_el + ",";
			Intent ir = new Intent(((MainActivity) getActivity()),
					StoreMetaDataService.class);
			ir.putExtra("data", args);
			((MainActivity) getActivity()).startService(ir);

			return imageF;
		}

		private File setUpPhotoFile() throws IOException {

			File f = createImageFile();
			mCurrentPhotoPath = f.getAbsolutePath();

			return f;
		}

		private void setPic() {

			/*
			 * There isn't enough memory to open up more than a couple camera
			 * photos
			 */
			/* So pre-scale the target bitmap into which the file is decoded */

			/* Get the size of the ImageView */
			int targetW = mImageView.getWidth();
			int targetH = mImageView.getHeight();

			/* Get the size of the image */
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			bmOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
			int photoW = bmOptions.outWidth;
			int photoH = bmOptions.outHeight;

			/* Figure out which way needs to be reduced less */
			int scaleFactor = 1;
			if ((targetW > 0) || (targetH > 0)) {
				scaleFactor = Math.min(photoW / targetW, photoH / targetH);
			}

			/* Set bitmap options to scale the image decode target */
			bmOptions.inJustDecodeBounds = false;
			bmOptions.inSampleSize = scaleFactor;
			bmOptions.inPurgeable = true;

			/* Decode the JPEG file into a Bitmap */
			Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath,
					bmOptions);

			/* Associate the Bitmap to the ImageView */
			mImageView.setImageBitmap(bitmap);
			mVideoUri = null;
			mImageView.setVisibility(View.VISIBLE);
			mVideoView.setVisibility(View.INVISIBLE);
		}

		private void dispatchTakePictureIntent(int actionCode) {

			Intent takePictureIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);

			switch (actionCode) {
			case ACTION_TAKE_PHOTO_B:
				File f = null;

				try {
					f = setUpPhotoFile();
					mCurrentPhotoPath = f.getAbsolutePath();
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(f));
				} catch (IOException e) {
					e.printStackTrace();
					f = null;
					mCurrentPhotoPath = null;
				}
				break;

			default:
				break;
			} // switch

			startActivityForResult(takePictureIntent, actionCode);
		}

		private void dispatchTakeVideoIntent() {
			Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			startActivityForResult(takeVideoIntent, ACTION_TAKE_VIDEO);
		}

		private void handleSmallCameraPhoto(Intent intent) {
			Bundle extras = intent.getExtras();
			mImageBitmap = (Bitmap) extras.get("data");
			mImageView.setImageBitmap(mImageBitmap);
			mVideoUri = null;
			mImageView.setVisibility(View.VISIBLE);
			mVideoView.setVisibility(View.INVISIBLE);
		}

		private void handleBigCameraPhoto() {

			if (mCurrentPhotoPath != null) {
				setPic();
				((MainActivity) getActivity()).galleryAddPic(mCurrentPhotoPath);
				mCurrentPhotoPath = null;
			}
		}

		private void handleCameraVideo(Intent intent) {
			mVideoUri = intent.getData();
			mVideoView.setVideoURI(mVideoUri);
			mImageBitmap = null;
			mVideoView.setVisibility(View.VISIBLE);
			mImageView.setVisibility(View.INVISIBLE);
		}

		Button.OnClickListener mTakePicOnClickListener = new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
			}
		};

		Button.OnClickListener mTakeVidOnClickListener = new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				dispatchTakeVideoIntent();
			}
		};

		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			switch (requestCode) {
			case ACTION_TAKE_PHOTO_B: {
				if (resultCode == RESULT_OK) {
					handleBigCameraPhoto();
				}
				break;
			} // ACTION_TAKE_PHOTO_B

			case ACTION_TAKE_PHOTO_S: {
				if (resultCode == RESULT_OK) {
					handleSmallCameraPhoto(data);
				}
				break;
			} // ACTION_TAKE_PHOTO_S

			case ACTION_TAKE_VIDEO: {
				if (resultCode == RESULT_OK) {
					handleCameraVideo(data);
				}
				break;
			} // ACTION_TAKE_VIDEO
			} // switch
		}

		/**
		 * Indicates whether the specified action can be used as an intent. This
		 * method queries the package manager for installed packages that can
		 * respond to an intent with the specified action. If no suitable
		 * package is found, this method returns false.
		 * http://android-developers
		 * .blogspot.com/2009/01/can-i-use-this-intent.html
		 * 
		 * @param context
		 *            The application's environment.
		 * @param action
		 *            The Intent action to check for availability.
		 * 
		 * @return True if an Intent with the specified action can be sent and
		 *         responded to, false otherwise.
		 */
		public static boolean isIntentAvailable(Context context, String action) {
			final PackageManager packageManager = context.getPackageManager();
			final Intent intent = new Intent(action);
			List<ResolveInfo> list = packageManager.queryIntentActivities(
					intent, PackageManager.MATCH_DEFAULT_ONLY);
			return list.size() > 0;
		}

		private void setBtnListenerOrDisable(Button btn,
				Button.OnClickListener onClickListener, String intentName) {
			if (isIntentAvailable((MainActivity) getActivity(), intentName)) {
				btn.setOnClickListener(onClickListener);
			} else {
				btn.setText(getText(R.string.cannot).toString() + " "
						+ btn.getText());
				btn.setClickable(false);
			}
		}

		// Set arrow in the images
		public void setArrows(int dir) {
			Log.i(TAG, Integer.toBinaryString(dir));
			if (isBitSet(dir, 0))
				mArrowUp.setVisibility(View.VISIBLE);
			else
				mArrowUp.setVisibility(View.INVISIBLE);
			if (isBitSet(dir, 1))
				mArrowRight.setVisibility(View.VISIBLE);
			else
				mArrowRight.setVisibility(View.INVISIBLE);
			if (isBitSet(dir, 2))
				mArrowDown.setVisibility(View.VISIBLE);
			else
				mArrowDown.setVisibility(View.INVISIBLE);
			if (isBitSet(dir, 3))
				mArrowLeft.setVisibility(View.VISIBLE);
			else
				mArrowLeft.setVisibility(View.INVISIBLE);
			if (dir == 0)
				mReady.setVisibility(View.VISIBLE);
			else
				mReady.setVisibility(View.INVISIBLE);
		}

		public boolean isBitSet(int data, int position) {
			int temp = (data >> position) & 1;
			return temp == 1;
		}

	}

	public void galleryAddPic(String photoPath) {
		Intent mediaScanIntent = new Intent(
				"android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(photoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		sendBroadcast(mediaScanIntent);
	}

	public void addItems(View v, String satelliteName, String satelliteNum,
			boolean update) {
		String satellite = satelliteName + " " + satelliteNum;
		mSatelliteTitles.add(satellite);
		adapter.clear();
		adapter.addAll(mSatelliteTitles);
		adapter.notifyDataSetChanged();

		// forces an update
		if (update) {
			String[] extras = { "update", satelliteNum, satelliteName };
			Intent mServiceIntent = new Intent(this, TLEPullService.class);
			mServiceIntent.putExtra(TLEPullService.EXTRAS, extras);
			this.startService(mServiceIntent);
			Log.i(TAG, "Download Service Started");
		}
	}

	// Acquire a reference to the system Location Manager
	private LocationManager locationManager;
	// Define a listener that responds to location updates
	LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {

			// Called when a new location is found by the network location
			// provider.
			if (GPS_DEBUG) {
				Log.i(TAG, "latitude: " + location.getLatitude());
				Log.i(TAG, "longitude: " + location.getLongitude());
				Log.i(TAG, "altitude: " + location.getAltitude());
			}

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

	private boolean displayedAlready;

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
			String temp = extras.getString(Constants.EXTENDED_DATA_STATUS);
			if (!temp.contains(","))
				TLE = temp;
			else {
				// satList = temp;
				Log.i(TAG, temp);
				mSatelliteTitles = new ArrayList<String>(80);
				Scanner scan = new Scanner(temp);
				scan.useDelimiter(",");
				String line = scan.next();
				try {
					while (line != null) {
						String name = line.substring(0, line.length() - 6);
						String num = line.substring(line.length() - 5);
						addItems(getCurrentFocus(), name, num, false);

						line = scan.next();
					}
				} catch (NoSuchElementException e) {

				}

			}

			// Log.i(TAG, temp);
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			for (int i = 0; i < 3; i++) {
				valuesAccelerometer[i] = event.values[i];
			}
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			for (int i = 0; i < 3; i++) {
				valuesMagneticField[i] = event.values[i];
			}
			break;
		}

		boolean success = SensorManager.getRotationMatrix(matrixR, matrixI,
				valuesAccelerometer, valuesMagneticField);

		if (success) {
			SensorManager.getOrientation(matrixR, matrixValues);

			double az = Math.toDegrees(matrixValues[0]);
			double pt = Math.toDegrees(matrixValues[1]);
			double rl = Math.toDegrees(matrixValues[2]);
			if (COMPASS_DEBUG) {
				Log.i(TAG, "azimuth: " + az);
				Log.i(TAG, "pitch: " + pt);
				Log.i(TAG, "roll: " + rl);
			}

			cam_az = (az >= 0) ? az : (360 + az);
			cam_el = rl + 90;

			// myCompass.update(matrixValues[0]);
			if (TLE.length() > 100) {
				SGP_TLE tle = new SGP_TLE(TLE);
				SGP_API RSO = new SGP_API(tle, new SGP_Location(
						"Current Location", latitude, longitude,
						(int) altitude, -5));
				RSO.calculatePosition();
				if (COMPUTE_DEBUG)

				{
					Log.i("RSO", "azimuth: " + RSO.getAzimuth());
					Log.i("RSO", "elevation: " + RSO.getElevation());
					Log.i("RSO", "visible: " + RSO.isVisible());
				}

				if (!RSO.isVisible() && !displayedAlready) {
					// display error if not visible
					Toast.makeText(this, "RSO Not Visible", Toast.LENGTH_SHORT)
							.show();
					displayedAlready = true;
				}

				// compute differences
				int temp_dir = 0;
				// Elevation
				if (RSO.getElevation() > (cam_el + 5)) {
					temp_dir += 4;
				} else if (RSO.getElevation() < (cam_el - 5)) {
					temp_dir += 1;
				}

				double rAz = RSO.getAzimuth();
				// Azimuth
				if (Math.abs(rAz - cam_az) < 180) {
					if (rAz > (cam_az + 5)) {
						temp_dir += 2;
					} else if (rAz < (cam_az - 5)) {
						temp_dir += 8;
					}
				} else {
					if (rAz > cam_az + 5) {
						temp_dir += 8;
					} else if (rAz < cam_az - 5) {
						temp_dir += 2;
					}
				}
				dir = temp_dir;
				TrackingFragment fragment = (TrackingFragment) getFragmentManager()
						.findFragmentById(R.id.content_frame);
				if (fragment != null)
					fragment.setArrows(dir);
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

}