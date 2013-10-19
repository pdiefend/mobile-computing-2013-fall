package com.android.chat.fragments;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MessageService extends Service {

	public static final String TAG = "MessageService";

	// Instance field
	public boolean online;
	// IBinder to connect the activity
	private final IBinder mBinder = new LocalBinder();

	// Broadcast and broadcast receiver threads
	BroadcastThread broadcast;
	BroadcastRcvThread broadcastRcv;

	// Message server
	ServerThread server;

	// Signal all Thread exit the loop
	public static boolean isServiceAlive;

	/*
	 * Declares the socket for broadcast. Used by BroadcastThread class and
	 * BroadcastRcvThread class.
	 */
	public static DatagramSocket broadcastSocket;

	// Constructor
	public MessageService() throws SocketException {
		broadcastSocket = new DatagramSocket(MainActivity.BROADCASTPORT);
	}

	public class LocalBinder extends Binder {
		MessageService getService() {
			return MessageService.this;
		}
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return true;
	}

	@Override
	public void onDestroy() {
		isServiceAlive = false;
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "Received start id " + startId + ": " + intent);
		isServiceAlive = true;
		try {
			broadcastSocket = new DatagramSocket(MainActivity.BROADCASTPORT);
			broadcastSocket.setBroadcast(true);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	public void broadcastOnline() {
		broadcast = new BroadcastThread(this);
		broadcast.start();
		Log.i("broadcastOnline", "Created");
	}

	public void broadcastListener() {
		broadcastRcv = new BroadcastRcvThread(this);
		broadcastRcv.start();
		Log.i("broadcastListener", "Created");
	}

	public void waitForMsg() {
		server = new ServerThread(MessageService.this, MainActivity.MSGPORT);
		server.start();
		Log.i("waitForMsg", "Started");
	}

	public void sendMessage(InetAddress sdAdd, String message) {
		(new SendMessage(sdAdd, message)).start();
	}

	public class SendMessage extends Thread {
		public InetAddress sdAdd;
		public String message;

		public SendMessage(InetAddress sdAdd, String message) {
			this.sdAdd = sdAdd;
			this.message = message;
		}

		public void run() {

		}
	}

}
