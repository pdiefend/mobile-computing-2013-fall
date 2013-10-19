package com.android.chat.fragments;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

public class ServerThread extends Thread {
	private static final String TAG = "ServerThread: ";

	Context mContext;

	// Schedule handler for broadcastReceiver
	private Handler handler;
	HandlerThread handlerThread;

	// List of ip that has created client socket
	public ArrayList<String> mClientIP;

	// The port the server is listen to.
	int port;

	public ServerThread(Context context, int port) {
		Log.i(TAG + ".ID" + this.getId(), "Created Server Thread");
		this.mContext = context;
		this.port = port;
		mClientIP = new ArrayList<String>(MainActivity.MAXUSERS);

		handlerThread = new HandlerThread("MyNewClientThread");

	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			initiateClientSocket(intent);
		}
	};

	private void initiateClientSocket(Intent intentSend) {
		Log.i(TAG + ".ID" + this.getId(), "Intent Received");
		String message = intentSend.getStringExtra("msg");
		String ip = intentSend.getStringExtra("ip");
		if (!mClientIP.contains(ip)) {
			Log.i(TAG + ".ID" + this.getId(),
					"Establishing new client connection with " + ip);
			Socket sock = null;
			try {
				sock = new Socket(ip, port);
				Log.i(TAG + ".ID" + this.getId(), "Created new TCP socket");
			} catch (UnknownHostException e) {
				Log.e(TAG + ".ID" + this.getId(),
						"Unable to connect to the host: " + ip + " at port "
								+ port);
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mClientIP.add(ip);
			try {
				(new ClientThread(mContext, sock, ip, message)).start();
			} catch (IOException e) {
				Log.e(TAG + ".ID" + this.getId(), "Failed to make new client");
				e.printStackTrace();
			}
		}
	}

	public void run() {
		Log.i(TAG + ".ID" + this.getId(), "started");

		// Set up broadcast Receiver for receiving request from the main
		// activity
		Looper.prepare();
		handlerThread.start();
		Looper looper = handlerThread.getLooper();
		handler = new Handler(looper);
		mContext.registerReceiver(broadcastReceiver, new IntentFilter(
				BroadcastSendMsg.BROADCAST_MSGSENT), null, handler);

		// Waiting for client on messaging port.
		waitingForClient();
	}

	public void waitingForClient() {
		ServerSocket serverSocket = null;
		Socket clientSocket = null;
		String clientIP = null;

		try {
			serverSocket = new ServerSocket(port);
			Log.i(TAG + ".ID" + this.getId(), "Created Server Socket");
		} catch (IOException e) {
			System.out.println("Could not listen on port: " + port);
			System.exit(-1);
		}

		try {
			while (MessageService.isServiceAlive) {
				Log.i(TAG + ".ID" + this.getId(), "Entered accepting");
				clientSocket = serverSocket.accept();
				Log.i(TAG + ".ID" + this.getId(), "accepted");
				clientIP = clientSocket.getInetAddress().getHostAddress();
				mClientIP.add(clientIP);
				Log.i(TAG + ".ID" + this.getId(), "Creating client thread..."
						+ clientIP);
				(new ClientThread(mContext, clientSocket, clientIP, null))
						.start();
			}
		} catch (IOException e) {
			mContext.unregisterReceiver(broadcastReceiver);
			System.out.println("Accept failed: " + port);
			System.exit(-1);
		}
		Log.i(TAG + ".ID" + this.getId(), "I died gracefully");
	}
}
