package com.android.chat.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

public class RemoveUserDialogFragment extends DialogFragment {

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();

		builder.setView(inflater.inflate(R.layout.activity_remove_user, null));

		builder.setTitle(R.string.remove_user_prompt);
		// this is the breaking point
		int len = ContactsFragment.contacts.size();
		CharSequence[] tmp = new CharSequence[len];
		for (int i = 0; i < len; i++) {
			// have to append to a string to ensure that new memory is used to
			// get around static accesses
			tmp[i] = "" + ContactsFragment.contacts.get(i);
		}
		builder.setItems(tmp, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				ContactsFragment.removeItems(getView(), which);
			}
		});
		return builder.create();
	}
}
