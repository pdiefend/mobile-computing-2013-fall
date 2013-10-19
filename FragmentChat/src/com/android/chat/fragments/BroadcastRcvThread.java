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
	private String contact;
	private String pcontact; // previous contact
	private int position;
	private String contactIP;
	private String message;
	private String opcode; // 0 -> add contact; 1 -> change username

	public BroadcastRcvThread(Context mContext) {
		Log.i(TAG + ".ID" + this.getId(), "created");
		this.mContext = mContext;
	}

	public void run() {
		Log.i(TAG + ".ID" + this.getId(), "Started");
		try {
			listenForResponses(MessageService.broadcastSocket);
		} catch (IOException e) {
			Log.e(TAG + ".ID" + this.getId(), "ListenForResponses failed");
			e.printStackTrace();
		}
		Log.i(TAG + ".ID" + this.getId(), "I died gracefully");
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
			while (MessageService.isServiceAlive) {
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				contact = new String(packet.getData(), 0, packet.getLength());
				contactIP = packet.getAddress().getHostAddress();
				if (!MainActivity.getData().contains(contact, contactIP)) {
					if (!MainActivity.getData().containsIP(contactIP)) {
						Log.i(TAG + ".ID" + this.getId(), "Adding new contact "
								+ contactIP + "/" + contact);
						// will be added on ContactsFragment.
						opcode = "New Contact";
						message = "Chat with " + contact;
						pcontact = "";
						position = -1;
						Log.i(TAG + ".ID" + this.getId(), "New contact added "
								+ contactIP + "/" + contact);
					} else {
						Log.i(TAG + ".ID" + this.getId(),
								"Changing username of " + contactIP + " to "
										+ contact);
						opcode = "Change Contact";
						pcontact = MainActivity.getData().getContactName(
								contactIP);
						position = ContactsFragment.contactsList
								.indexOf(pcontact);
						Log.i(TAG + ".ID" + this.getId(), contactIP
								+ " changed username to " + contact);
						MainActivity.getData().modifyContact(
								contact,
								MainActivity.getData().indexOfContactIP(
										contactIP));
					}
					handler.removeCallbacks(sendUpdatesToUI);
					handler.postDelayed(sendUpdatesToUI, 1000);
				} else {
					Log.i(TAG + ".ID" + this.getId(), contact
							+ " received already exists.");
				}
			}
		} catch (SocketTimeoutException e) {
			Log.d(TAG + ".ID" + this.getId(), "Receive timed out");
		}
	}

	private Runnable sendUpdatesToUI = new Runnable() {
		public void run() {
			Log.i(TAG + ".ID" + "sendUpdateToUI", "entered sendUpdatesToUI");
			intent.putExtra("opcode", opcode);
			intent.putExtra("contact", contact);
			intent.putExtra("contactIP", contactIP);
			intent.putExtra("message", message);
			intent.putExtra("position", position);
			mContext.sendBroadcast(intent);
		}
	};

}
