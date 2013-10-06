package com.android.chat.fragments;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class BroadcastRcvThread extends Thread {
	Context mContext;
	private static final String TAG = "BroadcastRcv: ";
	public static final String BROADCAST_ADD_CONTACT = "com.android.chat.fragments.broadcast.addcontact";
	private final Handler handler = new Handler();
	Intent intent = new Intent(BROADCAST_ADD_CONTACT);

	// instances for storing received contact information.
	private String[] rcvInfo;
	private String contact;
	private String contactIP;

	public BroadcastRcvThread(Context mContext) {
		this.mContext = mContext;
	}

	public void run() {
		Log.i("BroadcastRsv:", "Started");
		try {
			listenForResponses(MessageService.broadcastSocket);
		} catch (IOException e) {
			Log.e(TAG, "ListenForResponses failed");
			e.printStackTrace();
		}
	}

	/**
	 * Listen on socket for responses, timing out after TIMEOUT_MS
	 * 
	 * @param socket
	 *            socket on which the announcement request was sent
	 * @throws IOException
	 */
	private void listenForResponses(DatagramSocket socket) throws IOException {
		byte[] buf = new byte[1024];
		try {
			while (true) {
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				rcvInfo = parseInfo(new String(packet.getData(), 0,
						packet.getLength()));
				contact = rcvInfo[1];
				contactIP = rcvInfo[0];
				if (!MainActivity.getData().contains(contact, contactIP)) {
					Log.i(TAG, "Adding new contact " + contactIP + "/"
							+ contact);
					MainActivity.getData().addContact(contact, contactIP);
					handler.removeCallbacks(sendUpdatesToUI);
					handler.postDelayed(sendUpdatesToUI, 1000);
					Log.i(TAG, "New contact added " + contactIP + "/" + contact);
				} else {
					Log.i(TAG, contact + " received already exists.");
				}
			}
		} catch (SocketTimeoutException e) {
			Log.d(TAG, "Receive timed out");
		}
	}

	private Runnable sendUpdatesToUI = new Runnable() {
		public void run() {
			Log.i(TAG, "entered sendUpdatesToUI");
			intent.putExtra("contact", contact);
			intent.putExtra("contactIP", contactIP);
			mContext.sendBroadcast(intent);
		}
	};

	// Assume the broadcasted string is in the format: "IP/username"
	private String[] parseInfo(String rcvedUserInfo) {
		String[] tokens = rcvedUserInfo.split("/");
		return tokens;
	}

}
