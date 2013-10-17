package com.android.chat.fragments;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.chat.fragments.MessageService.LocalBinder;
import com.android.chat.fragments.R.id;

public class MainActivity extends FragmentActivity implements
		ContactsFragment.OnHeadlineSelectedListener {

	// =======================================

	private static final String TAG = "MainActivity";

	// Broadcast sending msg
	public static final String BROADCAST_MSGSENT = "com.android.chat.fragment.mainactivity";
	private final Handler handler = new Handler();
	Intent intentMsg = new Intent(BROADCAST_MSGSENT);
	Context mContext;
	private String message;
	private String ip;

	public Runnable sendMsg = new Runnable() {
		public void run() {
			Log.i(TAG, "Entered SendMsg");
			broadcastMsgToSocket();
			handler.postDelayed(this, 1000);
		}
	};

	private void broadcastMsgToSocket() {
		Log.i(TAG, "Broadcasting the intent");
		intentMsg.putExtra("msg", message);
		intentMsg.putExtra("ip", ip);
		sendBroadcast(intentMsg);
	}

	// End of broadcasting send msg

	boolean mBound = false;
	MessageService mMsgService;
	public static String username = "New User";
	public static int MAXUSERS = 40;
	public static final int BROADCASTPORT = 3141;
	public static final int MSGPORT = 2014;

	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.i("ServiceBounding:", "ServiceDisconnect");
			// mMsgService.online = false; Not working
			MessageService.broadcastSocket.close();
			mBound = false;
			mMsgService = null;

			unregisterReceiver(broadcastReceiver);
			unregisterReceiver(msgReceiver);
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i("ServiceBounding:", "ServiceConnect");
			// mMsgService.online = true; Not working
			LocalBinder mLocalBinder = (LocalBinder) service;
			mMsgService = mLocalBinder.getService();
			mBound = true;

			if (mBound) {
				// Initiate broadcasting
				mMsgService.broadcastOnline();

				// Initiate broadcast receiver listening to other users
				mMsgService.broadcastListener();

				// Start Server to wait for message
				mMsgService.waitForMsg();

				// register local broadcast receiver.
				registerReceiver(broadcastReceiver, new IntentFilter(
						BroadcastRcvThread.BROADCAST_ADD_CONTACT));
				registerReceiver(msgReceiver, new IntentFilter(
						ClientThread.BROADCAST_MSGRCVED));
			} else
				Log.i("checkBound", "Failed");
		}
	};

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String opcode = intent.getStringExtra("opcode");
			String contact = intent.getStringExtra("contact");
			String contactIP = intent.getStringExtra("contactIP");
			String message = intent.getStringExtra("message");
			int position = intent.getIntExtra("position", -1);
			if (opcode.compareTo("Change Contact") == 0) {
				/*
				 * Log.i("opcode", opcode); Log.i("contact", contact);
				 * Log.i("contactIP", contactIP); Log.i("message", message);
				 * Log.i("position", "" + position);
				 */
				ContactsFragment.modifyName(findViewById(id.contacts_list),
						contact, position);
			} else {
				ContactsFragment.addItems(findViewById(id.contacts_list),
						contact, contactIP, message);
			}
		}
	};

	private BroadcastReceiver msgReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String contactIP = intent.getStringExtra("ip");
			String message = intent.getStringExtra("msg");
			Log.i(TAG, "updating UI msg: " + message + " " + contactIP
					+ " ====================");
			receiveMessage(message, contactIP);
		}
	};

	// =======================================
	private static ChatData data;

	public static ChatData getData() {
		return data;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.w("On create:", "called");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_view);

		// ===================================
		// Starts the message service
		/*
		 * Intent msgServiceInit = new Intent(this, MessageService.class);
		 * msgServiceInit.putExtra("PORT", PORT); startService(msgServiceInit);
		 */
		// Bind the message service

		// Starts the broadcasting service

		// ===================================
		ActionBar actionBar = getActionBar();
		actionBar.show();
		actionBar.setDisplayHomeAsUpEnabled(false);

		if (findViewById(R.id.fragment_container) != null) {
			if (savedInstanceState != null) {
				return;
			}
			ContactsFragment firstFragment = new ContactsFragment();
			firstFragment.setArguments(getIntent().getExtras());
			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, firstFragment).commit();
		}
		if (savedInstanceState == null) {
			// ==================================================
			data = new ChatData(MAXUSERS);

			// Don't do this:
			// data.addContact(0, "Contact 1", null, "Contact 1 message");
			// data.addContact(1, "Contact 2", null, "Contact 2 message");
			// ==================================================
			DialogFragment newFragment = new LogonDialogFragment();
			newFragment.show(getFragmentManager(), "logon");
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.w("On Stop:", "called");
		// ========================================
		// unbind the message service onStop
		if (mBound) {
			Log.i("checkBound", "mBound is True at onStop");
			unbindService(mConnection);
			mBound = false;
		} else
			Log.i("checkBound", "mBound is False at onStop");
		// ========================================
	}

	// ==================================================
	// Initiate the message service onStart
	// Bind the service with the main Activity
	@Override
	protected void onStart() {
		super.onStart();
		Log.w("On Start:", "called");
		// Bind the service with the activity
		Intent mIntent = new Intent(this, MessageService.class);
		bindService(mIntent, mConnection, Context.BIND_AUTO_CREATE);

	}

	// ====================================================

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.w("On Destroy:", "called");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.w("On Pause:", "called");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.w("On Resume:", "called");
	}

	public void onContactSelected(int position) {
		// The user selected the headline of an article from the
		// HeadlinesFragment

		// Capture the article fragment from the activity layout
		ChatFragment articleFrag = (ChatFragment) getSupportFragmentManager()
				.findFragmentById(R.id.article_fragment);

		if (articleFrag != null) {
			// If contact frag is available, we're in two-pane layout...

			// Call a method in the ArticleFragment to update its content
			articleFrag.updateArticleView(position);

		} else {
			// If the frag is not available, we're in the one-pane layout and
			// must swap frags...

			// Create fragment and give it an argument for the selected article
			ChatFragment newFragment = new ChatFragment();
			Bundle args = new Bundle();
			args.putInt(ChatFragment.ARG_POSITION, position);
			newFragment.setArguments(args);
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();

			// Replace whatever is in the fragment_container view with this
			// fragment,
			// and add the transaction to the back stack so the user can
			// navigate back
			transaction.replace(R.id.fragment_container, newFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
		}
	}

	public void sendMessage(View view) {
		if (!ContactsFragment.contactsList.isEmpty()) {
			TextView textview = (TextView) findViewById(R.id.chatView);
			EditText editText = (EditText) findViewById(R.id.edit_message);
			String message = editText.getText().toString();
			textview.setText(message);
			data.modifyMessage(ContactsFragment.getSelectedIndex(), message);
			editText.setText("");

			// =====================================================================================================
			// Sends out the message
			Log.i("sendMessage", "Sending " + message);
			Log.i("data",
					" "
							+ data.getContactIP(ContactsFragment
									.getSelectedIndex()));
			this.ip = data.getContactIP(ContactsFragment.getSelectedIndex());
			this.message = message;
			broadcastMsgToSocket();
			// handler.removeCallbacks(sendMsg);
			// handler.postDelayed(sendMsg, 1000);

			// =====================================================================================================

		}
	}

	public void receiveMessage(String msg, String ip) {
		int contactIndex = data.indexOfContactIP(ip);

		TextView chat = (TextView) findViewById(R.id.chatView);
		data.modifyMessage(contactIndex, msg);

		// update the current message if looking at that conversation
		if (ContactsFragment.getSelectedIndex() == contactIndex) {
			chat.setText(msg);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// use an inflater to populate the ActionBar with items
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.chat_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// same as using a normal menu
		switch (item.getItemId()) {
		case R.id.item_full_screen:
			Intent intent = new Intent(this, FullScreenChatActivity.class);
			startActivity(intent);
			break;
		case R.id.item_change_username:
			DialogFragment logonFragment = new LogonDialogFragment();
			logonFragment.show(getFragmentManager(), "logon");
			break;
		case R.id.item_add_contact:
			DialogFragment addFragment = new AddUserDialogFragment();
			addFragment.show(getFragmentManager(), "addUser");
			break;
		case R.id.item_remove_contact:
			DialogFragment removeFragment = new RemoveUserDialogFragment();
			removeFragment.show(getFragmentManager(), "removeUser");
			break;
		// add other button options here
		}
		return true;
	}
}