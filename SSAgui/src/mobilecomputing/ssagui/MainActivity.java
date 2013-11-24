package mobilecomputing.ssagui;

import java.util.ArrayList;

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
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
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
import android.widget.ListView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class MainActivity extends Activity {
	private final static String TAG = "MainActivity";
	private String locationProvider = LocationManager.NETWORK_PROVIDER;
	// private String locationProvider = LocationManager.GPS_PROVIDER;

	private double latitude;
	private double longitude;
	private double altitude;
	private String TLE;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private static ArrayList<String> mSatelliteTitles;

	static ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();
		mSatelliteTitles = new ArrayList<String>(80);
		mSatelliteTitles.add("ISS #25544");
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

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
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

			// ===========================
			// Start tracking service

			return rootView;
		}

	}

	public void addItems(View v, String satelliteName, String satelliteNum) {
		String satellite = satelliteName + " #" + satelliteNum;
		mSatelliteTitles.add(satellite);
		adapter.notifyDataSetChanged();

		String[] extras = { "update", satelliteNum, satelliteName };

		// extras[0] = "list"; extras[1] = ""; // to list available TLEs
		// extras[0] = "get"; extras[1] = "xxxxx"; // to get TLE by sat num
		// extras[0] = "update"; extras[1] = "xxxxx"; // to force download of
		// TLE by sat num

		Intent mServiceIntent = new Intent(this, TLEPullService.class);
		mServiceIntent.putExtra(TLEPullService.EXTRAS, extras);
		this.startService(mServiceIntent);
		Log.i(TAG, "Download Service Started");
	}

	// Acquire a reference to the system Location Manager
	private LocationManager locationManager;
	// Define a listener that responds to location updates
	LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {

			// Called when a new location is found by the network location
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

}
