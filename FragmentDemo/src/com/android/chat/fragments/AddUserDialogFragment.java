package com.android.chat.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;

public class AddUserDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();
		// inflater.inflate(R.layout.activity_logon, );

		builder.setView(inflater.inflate(R.layout.activity_add_user, null));

		builder.setPositiveButton(R.string.cont,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Dialog dialog2 = (Dialog) dialog;
						EditText edit = (EditText) dialog2
								.findViewById(R.id.addUsername);
						String user = edit.getText().toString();

						if (!user.equals("")) {
							Log.w("Add User:", user);
							ContactsFragment.addItems(getView(), user, null);
						} else {
							Log.w("Add User:", "aborted");
						}
					}
				}).setTitle(R.string.add_user_prompt);

		return builder.create();
	}
}
