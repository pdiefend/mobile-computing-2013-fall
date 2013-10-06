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

	// Instance field
	public boolean online;
	// IBinder to connect the activity
	private final IBinder mBinder = new LocalBinder();

	// Broadcast and broadcast receiver threads
	BroadcastThread broadcast = new BroadcastThread(this);
	BroadcastRcvThread broadcastRcv = new BroadcastRcvThread(this);

	/*
	 * Declares the socket for broadcast. Used by BroadcastThread class and
	 * BroadcastRcvThread class.
	 */
	public static DatagramSocket broadcastSocket;

	// Constructor
	public MessageService() throws SocketException {
		broadcastSocket = new DatagramSocket(MainActivity.PORT);
	}

	public class LocalBinder extends Binder {
		MessageService getService() {
			return MessageService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		try {
			broadcastSocket = new DatagramSocket(MainActivity.PORT);
			broadcastSocket.setBroadcast(true);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mBinder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	public void broadcastOnline() {
		broadcast.start();
		Log.i("broadcastOnline", "Created");
	}

	public void broadcastListener() {
		broadcastRcv.start();
		Log.i("broadcastListener", "Created");
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
