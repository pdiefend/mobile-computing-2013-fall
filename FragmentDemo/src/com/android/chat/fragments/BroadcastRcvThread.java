package com.android.chat.fragments;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

import android.util.Log;

public class BroadcastRcvThread extends Thread {

	public void run() {
		Log.i("BroadcastRsv:", "Started");
		try {
			listenForResponses(MessageService.broadcastSocket);
		} catch (IOException e) {
			Log.e("BroadcastRcv:", "ListenForResponses failed");
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
				String s = new String(packet.getData(), 0, packet.getLength());
				Log.d("BroadcastRcv:", "Received response " + s);
			}
		} catch (SocketTimeoutException e) {
			Log.d("BroadcastRcv:", "Receive timed out");
		}
	}

}
