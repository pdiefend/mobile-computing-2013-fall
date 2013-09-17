/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

import com.android.chat.fragments.ContactsFragment.OnContactSelectedListener;

public class MainActivity extends FragmentActivity implements
		OnContactSelectedListener {

	private static ChatData data = new ChatData(2);

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
		DialogFragment newFragment = new LogonDialogFragment();
		newFragment.show(getFragmentManager(), "logon");

		data.addContact(0, "Contact 1", "Contact 1 message");
		data.addContact(1, "Contact 2", "Contact 2 message");
	}

	public void onContactSelected(int position) {
		ChatFragment articleFrag = (ChatFragment) getSupportFragmentManager()
				.findFragmentById(R.id.chat_fragment);

		if (articleFrag != null) {
			// If contact frag is available, we're in two-pane layout...
			// Call a method in the ChatFragment to update its content
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