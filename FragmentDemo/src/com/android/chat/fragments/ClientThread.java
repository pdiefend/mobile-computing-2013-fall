package com.android.chat.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;

public class ClientThread extends Thread {
	private static final String TAG = "ClientThread: ";
	Context mContext;

	// Socket instances
	Socket clientSocket;
	OutputStream ostream;
	PrintWriter pwrite;
	InputStream istream;
	BufferedReader receiveRead;
	String receiveMsg;
	String sendMsg;

	// Local Broadcast instances for update message
	public static final String BROADCAST_MSGRCVED = "com.android.chat.fragments.msgrcved";
	private final Handler handler = new Handler();
	Intent intentRcv;

	// Local Broadcast instance for sending message
	Intent intentSend;

	public ClientThread(Context context, Socket clientSocket)
			throws IOException {
		this.mContext = context;
		this.clientSocket = clientSocket;
		ostream = clientSocket.getOutputStream();
		pwrite = new PrintWriter(ostream, true);
		istream = clientSocket.getInputStream();
		receiveRead = new BufferedReader(new InputStreamReader(istream));
		intentRcv = new Intent(BROADCAST_MSGRCVED);

		// Initialize the broadcast listener for intent from BroadcastSendMsg
		intentSend = new Intent(mContext, BroadcastSendMsg.class);

	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context mContext, Intent intent) {
			Log.i(TAG, "Sending Message");
			String message = intentSend.getStringExtra("msg");
			String ip = intentSend.getStringExtra("ip");
			sendMessage(message);
		}
	};

	private void sendMessage(String message) {
		pwrite.print(sendMsg);
	}

	public void run() {
		// register the broadcast
		mContext.registerReceiver(broadcastReceiver, new IntentFilter(
				BroadcastSendMsg.BROADCAST_MSGSENT));

		while (true) {
			try {
				sendRcv();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private Runnable send = new Runnable() {
		public void run() {
			Log.i(TAG, "Entered Runnable");
			intentRcv.putExtra("msg", receiveMsg);
			intentRcv.putExtra("ip", clientSocket.getInetAddress()
					.getHostAddress());
			mContext.sendBroadcast(intentRcv);
		}
	};

	// If receives any message from the socket broadcast it out.
	public void sendRcv() throws IOException {
		if ((receiveMsg = receiveRead.readLine()) != null) {
			// Send out broadcast intent with the message and IP
			handler.removeCallbacks(send);
			handler.postDelayed(send, 1000);
		}
		//
	}
}
