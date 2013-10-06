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
	private ArrayList<String> message;

	public ChatData(int n) {
		contact = new ArrayList<String>(n);
		contactIP = new ArrayList<String>(n);
		message = new ArrayList<String>(n);
	}

	public ChatData(ChatData tmp) {
		this.contact = new ArrayList<String>(contact);
		this.contactIP = new ArrayList<String>(contactIP);
		this.message = new ArrayList<String>(message);
	}

	// ================================================

	public boolean contains(String contact, String contactIP) {
		return this.contact.contains(contact)
				&& this.contactIP.contains(contactIP);
	}

	public boolean containsContact(String contact) {
		return this.contact.contains(contact);
	}

	public boolean containsIP(String contactIP) {
		return this.contactIP.contains(contactIP);
	}

	public void modifyContact(String contact, int which) {
		this.contact.set(which, contact);
	}

	public int indexOfContact(String contact) {
		return this.contact.indexOf(contact);
	}

	public int indexOfContactIP(String contactIP) {
		return this.contactIP.indexOf(contactIP);
	}

	public String getContactName(String contactIP) {
		return this.contact.get(this.contactIP.indexOf(contactIP));
	}

	// =================================================

	public void addContact(int position, String contact, String contactIP,
			String message) {
		this.contact.add(position, contact);
		this.contactIP.add(position, contactIP);
		this.message.add(position, message);
	}

	public void addContact(String contact, String contactIP, String message) {
		this.contact.add(contact);
		this.contactIP.add(contactIP);
		this.message.add(message);
	}

	public void modifyMessage(int position, String message) {
		this.message.set(position, message);
	}

	public String getMessage(int position) {
		return this.message.get(position);
	}

	public String[] getContact() {
		return (String[]) this.contact.toArray();
	}

	public ArrayList<String> getContact(boolean i) {
		return this.contact;
	}

	public void removeContact(String contact) {
		int index = this.contact.indexOf(contact);
		this.contact.remove(index);
		this.contactIP.remove(index);
		this.message.remove(index);
	}

	public void removeContact(int index) {
		this.contact.remove(index);
		this.contactIP.remove(index);
	}

}