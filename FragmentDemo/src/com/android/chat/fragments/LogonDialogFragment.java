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

						Log.w("Change User:", user);
					}
				}).setTitle(R.string.logon_prompt);

		// Create the AlertDialog object and return it
		return builder.create();
	}
}
