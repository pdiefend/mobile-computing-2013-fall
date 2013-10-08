package com.android.chat.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import android.content.Context;
import android.content.Intent;
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

	// Local Broadcast instances
	public static final String BROADCAST_MSGRCVED = "com.android.chat.fragments.msgrcved";
	private final Handler handler = new Handler();
	Intent intent;

	public ClientThread(Context context, Socket clientSocket)
			throws IOException {
		this.mContext = context;
		this.clientSocket = clientSocket;
		ostream = clientSocket.getOutputStream();
		pwrite = new PrintWriter(ostream, true);
		istream = clientSocket.getInputStream();
		receiveRead = new BufferedReader(new InputStreamReader(istream));
		intent = new Intent(BROADCAST_MSGRCVED);
	}

	public void run() {
		// register the broadcast
		handler.removeCallbacks(send);
		handler.postDelayed(send, 1000);
	}

	private Runnable send = new Runnable() {
		public void run() {
			Log.i(TAG, "Entered Runnable");
			intent.putExtra("msg", receiveMsg);
			intent.putExtra("ip", clientSocket.getInetAddress()
					.getHostAddress());
			mContext.sendBroadcast(intent);
		}
	};

	// Wait for either messages from client or input message
	public void sendRcv() throws IOException {
		if ((receiveMsg = receiveRead.readLine()) != null) {
			// Send out broadcast intent with the message and IP
		}
		//
	}
}
