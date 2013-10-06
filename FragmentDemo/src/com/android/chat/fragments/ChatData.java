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
	private ArrayList<String> contact;
	private ArrayList<String> contactIP;

	public ChatData(int n) {
		contact = new ArrayList<String>(n);
		contactIP = new ArrayList<String>(n);
	}

	public ChatData(ChatData tmp) {
		this.contact = new ArrayList<String>(contact);
		this.contactIP = new ArrayList<String>(contactIP);
	}

	public boolean contains(String contact, String contactIP) {
		return this.contact.contains(contact)
				&& this.contactIP.contains(contactIP);
	}

	public void addContact(int position, String contact, String contactIP) {
		this.contact.add(position, contact);
		this.contactIP.add(position, contactIP);
	}

	public void addContact(String contact, String contactIP) {
		this.contact.add(contact);
		this.contactIP.add(contactIP);
	}

	public void modifyMessage(int position, String contactIP) {
		this.contactIP.set(position, contactIP);
	}

	public String getMessage(int position) {
		return this.contactIP.get(position);
	}

	public String[] getContacts() {
		return (String[]) this.contact.toArray();
	}

	public ArrayList<String> getContacts(boolean i) {
		return this.contact;
	}

	public void removeContact(String contact) {
		int index = this.contact.indexOf(contact);
		this.contact.remove(index);
		this.contactIP.remove(index);
	}

	public void removeContact(int index) {
		this.contact.remove(index);
		this.contactIP.remove(index);
	}

}