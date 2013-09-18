package com.android.chat.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class FullScreenChatActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_screen_chat);
		ActionBar actionBar = getActionBar();
		actionBar.show();
		actionBar.setDisplayHomeAsUpEnabled(true);
		TextView textview = (TextView) findViewById(R.id.chatView);
		textview.setText(MainActivity.getData().getMessage(
				ContactsFragment.getSelectedIndex()));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.fs_chat_menu, menu);
		return true;
	}

	public void sendMessage(View view) {
		TextView textview = (TextView) findViewById(R.id.chatView);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		textview.setText(message);
		MainActivity.getData().modifyMessage(
				ContactsFragment.getSelectedIndex(), message);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		super.onBackPressed();
		return true;
	}

}
