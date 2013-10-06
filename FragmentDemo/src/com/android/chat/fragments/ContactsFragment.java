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

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ContactsFragment extends ListFragment {
	OnHeadlineSelectedListener mCallback;
	private static int selectedIndex = 0;
	static ArrayList<String> contactsList = new ArrayList<String>();
	static ArrayAdapter<String> adapter;

	// The container Activity must implement this interface so the frag can
	// deliver messages
	public interface OnHeadlineSelectedListener {
		/** Called by HeadlinesFragment when a list item is selected */
		public void onContactSelected(int position);
	}

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		int layout = android.R.layout.simple_list_item_activated_1;
		adapter = new ArrayAdapter<String>(getActivity(), layout, contactsList);
		setListAdapter(adapter);
	}

	// Method for Dynamic insertion into contacts
	public static void addItems(View v, String contact, String contactIP) {
		contactsList.add(contact);
		adapter.notifyDataSetChanged();
		MainActivity.getData().addContact(contact, "");
	}

	// Method for Dynamic insertion into contacts
	public static void removeItems(View v, int which) {
		contactsList.remove(which);
		adapter.notifyDataSetChanged();
		MainActivity.getData().removeContact(which);
	}

	@Override
	public void onStart() {
		super.onStart();

		// When in two-pane layout, set the listview to highlight the selected
		// list item
		// (We do this during onStart because at the point the listview is
		// available.)
		if (getFragmentManager().findFragmentById(R.id.article_fragment) != null) {
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception.
		try {
			mCallback = (OnHeadlineSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Notify the parent activity of selected item
		mCallback.onContactSelected(position);
		// Set the item as checked to be highlighted when in two-pane layout
		getListView().setItemChecked(position, true);
		selectedIndex = position;

	}

	public static int getSelectedIndex() {
		return selectedIndex;
	}

	public static void setSelectedIndex(int selectedIndex) {
		ContactsFragment.selectedIndex = selectedIndex;
	}
}