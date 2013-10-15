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
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

public class ClientThread extends Thread {
	private static final String TAG = "ClientThread: ";
	Context mContext;

	// Socket instances
	Socket clientSocket;
	String ip;
	OutputStream ostream;
	PrintWriter pwrite;
	InputStream istream;
	BufferedReader receiveRead;
	String receiveMsg;
	String sendMsg;

	// Local Broadcast instances for update message
	public static final String BROADCAST_MSGRCVED = "com.android.chat.fragments.msgrcved";
	Intent intentRcv;

	// Local Broadcast instance for sending message
	Intent intentSend;

	// Schedule handler for broadcastReceiver
	private Handler handler;
	HandlerThread handlerThread;

	public ClientThread(Context context, Socket clientSocket, String ip,
			String message) throws IOException {
		this.mContext = context;
		this.clientSocket = clientSocket;
		ostream = clientSocket.getOutputStream();
		pwrite = new PrintWriter(ostream, true);
		istream = clientSocket.getInputStream();
		this.ip = ip;
		receiveRead = new BufferedReader(new InputStreamReader(istream));
		intentRcv = new Intent(BROADCAST_MSGRCVED);

		// Initialize the broadcast listener for intent from BroadcastSendMsg
		intentSend = new Intent(mContext, BroadcastSendMsg.class);
		sendMsg = message;
		handlerThread = new HandlerThread("MyNewClientThread");
		Log.i(TAG, "Created clientThread with" + ip);

	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context mContext, Intent intent) {
			sendMessage(intent);
		}
	};

	private void sendMessage(Intent intentSend) {
		Log.i(TAG, "Intent Received");
		String message = intentSend.getStringExtra("msg");
		String ip = intentSend.getStringExtra("ip");
		if (ip.compareTo(this.ip) == 0) {
			Log.i(TAG, "Sending message: " + message + " to " + ip);
			pwrite.print(message);
		}
	}

	public void run() {
		// Set up broadcast Receiver for receiving request from the main
		// activity
		Looper.prepare();
		handlerThread.start();
		Looper looper = handlerThread.getLooper();
		handler = new Handler(looper);
		mContext.registerReceiver(broadcastReceiver, new IntentFilter(
				BroadcastSendMsg.BROADCAST_MSGSENT), null, handler);

		if (sendMsg != null) {
			pwrite.print(sendMsg);
		}

		while (true) {
			try {
				sendRcv();
			} catch (IOException e) {
				mContext.unregisterReceiver(broadcastReceiver);
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
