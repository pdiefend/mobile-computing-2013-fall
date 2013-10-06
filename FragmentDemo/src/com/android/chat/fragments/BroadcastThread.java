/**
 * 
 */
package com.android.chat.fragments;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

/**
 * @author prd005
 * 
 */
@SuppressWarnings("deprecation")
public class BroadcastThread extends Thread {
	Context mContext;
	private String onlineStatus = "Not available";

	public BroadcastThread(Context c) {
		mContext = c;
	}

	public void run() {
		Log.i("Thread", "Started");

		try {
			while (true) {
				Log.i("Thread", "Ping");
				this.sendOnlineStatus();
				Thread.sleep(10000);
			}
		} catch (InterruptedException ex) {
			Log.e("Thread", "Exception caught");
		}

	}

	InetAddress getBroadcastAddress() throws IOException {
		// WifiManager wifi = mContext.getSystemService(Context.WIFI_SERVICE);
		WifiManager wifi = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
		// handle null somehow

		WifiInfo wifiInfo = wifi.getConnectionInfo();
		int ip = wifiInfo.getIpAddress();
		/*
		 * InetAddress ipAd = InetAddress.getLocalHost(); String ipAdd =
		 * ipAd.getHostAddress(); Log.i("Thread InetAddress", ipAdd);
		 */
		Log.i("Thread IP addr", Formatter.formatIpAddress(ip));

		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		return InetAddress.getByAddress(quads);
	}

	public String getIpAddress() throws IOException {
		WifiManager wifi = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);

		WifiInfo wifiInfo = wifi.getConnectionInfo();
		int ip = wifiInfo.getIpAddress();
		return Formatter.formatIpAddress(ip);
	}

	public void sendOnlineStatus() {
		try {
			// Log.i("Thread", "here");
			setOnlineStatus(getIpAddress() + "/" + MainActivity.username);
			DatagramPacket packet = new DatagramPacket(onlineStatus.getBytes(),
					onlineStatus.length(), getBroadcastAddress(),
					MainActivity.PORT);
			MessageService.broadcastSocket.send(packet);
		} catch (IOException e) {
			Log.e("Thread", "Exception Occured", e);
		}
	}

	public String getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(String onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

}
