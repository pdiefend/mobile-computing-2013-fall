package com.android.chat.fragments;

/**
 * Class to hold the contacts by username and IP address
 * 
 * @author prd005
 */
public class Contact {
	/**
	 * The common name for the contact
	 */
	private String username;
	/**
	 * The network address of the contact
	 */
	private String ip;

	/**
	 * Constructs a Contact object using the parameter username and ip address
	 * 
	 * @param _username
	 *            the contact's name
	 * @param _ip
	 *            the network address for the contact
	 */
	public Contact(String _username, String _ip) {
		this.username = _username;
		this.ip = _ip;
	}

	/**
	 * Constructs a Contact with username uname and ip address localhost
	 */
	public Contact() {
		this.username = "uname";
		this.ip = "localhost";
	}

	/**
	 * Retrieves the Contact's username
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the Contact's username to the paramter value
	 * 
	 * @param username
	 *            the username to be set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Retrieves the Contact's ip address
	 * 
	 * @return the ip address
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * Sets the Contact's ip to the parameter value
	 * 
	 * @param ip
	 *            the ip to set the Contact's ip to
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
}
