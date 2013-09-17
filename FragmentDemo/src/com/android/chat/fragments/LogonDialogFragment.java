package com.android.chat.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;

public class LogonDialogFragment extends DialogFragment {
	/*
	 * @Override protected void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState);
	 * setContentView(R.layout.activity_logon); }
	 * 
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.logon, menu); return true; }
	 */

	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// // Inflate the layout to use as dialog or embedded fragment
	// return inflater.inflate(R.layout.activity_logon, container, false);
	// }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();
		// inflater.inflate(R.layout.activity_logon, );

		builder.setView(inflater.inflate(R.layout.activity_logon, null));

		builder.setPositiveButton(R.string.cont,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Dialog dialog2 = (Dialog) dialog;
						EditText edit = (EditText) dialog2
								.findViewById(R.id.username);
						String user = edit.getText().toString();

						Log.w("<============", user);
					}
				}).setTitle(R.string.logon_prompt);

		// Create the AlertDialog object and return it
		return builder.create();
	}
}
