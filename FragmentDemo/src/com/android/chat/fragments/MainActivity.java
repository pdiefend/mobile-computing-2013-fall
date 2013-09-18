package com.android.chat.fragments;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements
		ContactsFragment.OnHeadlineSelectedListener {

	private static ChatData data;

	public static ChatData getData() {
		return data;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_view);

		// gets the activity's default ActionBar
		ActionBar actionBar = getActionBar();
		actionBar.show();
		// set the app icon as an action to go home
		// we are home so we don't need it
		actionBar.setDisplayHomeAsUpEnabled(true);

		if (findViewById(R.id.fragment_container) != null) {

			// However, if we're being restored from a previous state,
			// then we don't need to do anything and should return or else
			// we could end up with overlapping fragments.
			if (savedInstanceState != null) {
				return;
			}

			// Create an instance of ExampleFragment
			ContactsFragment firstFragment = new ContactsFragment();

			// In case this activity was started with special instructions from
			// an Intent,
			// pass the Intent's extras to the fragment as arguments
			firstFragment.setArguments(getIntent().getExtras());

			// Add the fragment to the 'fragment_container' FrameLayout
			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, firstFragment).commit();

		}
		data = new ChatData(2);
		data.addContact(0, "Contact 1", "Contact 1 message");
		data.addContact(1, "Contact 2", "Contact 2 message");

		DialogFragment newFragment = new LogonDialogFragment();
		newFragment.show(getFragmentManager(), "logon");
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
		TextView textview = (TextView) findViewById(R.id.chatView);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		textview.setText(message);
		data.modifyMessage(ContactsFragment.getSelectedIndex(), message);
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
		case R.id.item_settings:
			DialogFragment newFragment = new LogonDialogFragment();
			newFragment.show(getFragmentManager(), "logon");
			break;
		case R.id.item_full_screen:
			Intent intent = new Intent(this, FullScreenChatActivity.class);
			startActivity(intent);
			break;
		// add other button options here
		}
		return true;
	}

}