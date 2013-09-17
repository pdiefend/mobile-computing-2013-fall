/**
 * 
 */
package com.android.chat.fragments;

import java.util.ArrayList;

/**
 * @author prd005
 * 
 */
public class ChatData {
	private ArrayList<String> contacts;
	private ArrayList<String> messages;

	public ChatData(int n) {
		contacts = new ArrayList<String>(n);
		messages = new ArrayList<String>(n);
	}

	public void addContact(int position, String contact, String message) {
		contacts.add(position, contact);
		messages.add(position, message);
	}

	public void modifyMessage(int position, String message) {
		messages.set(position, message);
	}

	public String getMessage(int position) {
		return messages.get(position);
	}

	public String[] getContacts() {
		return (String[]) contacts.toArray();
	}

}
