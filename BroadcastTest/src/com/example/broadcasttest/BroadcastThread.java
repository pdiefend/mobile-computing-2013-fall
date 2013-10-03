/**
 * 
 */
package com.example.broadcasttest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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
public class BroadcastThread implements Runnable {
	Context mContext;

	public BroadcastThread(Context c) {
		mContext = c;
	}

	public void run() {
		Log.i("Thread", "Started");

		try {
			while (true) {
				Log.i("Thread", "Ping");
				Thread.sleep(1000);
				this.sendPing();
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
		Log.i("Thread IP addr", Formatter.formatIpAddress(ip));

		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		return InetAddress.getByAddress(quads);
	}

	public void sendPing() {
		DatagramSocket socket;
		try {
			socket = new DatagramSocket(3141);
			// Log.i("Thread", "here");
			socket.setBroadcast(true);
			DatagramPacket packet = new DatagramPacket("PING".getBytes(),
					"PING".length(), getBroadcastAddress(), 3141);
			socket.send(packet);
			socket.close();

		} catch (IOException e) {
			Log.e("Thread", "Exception Occured", e);
		}
	}

}
