package com.android.chat.fragments;

import java.net.InetAddress;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MessageService extends Service {

	// Instance field
	public boolean online;
	BroadcastThread broadcastStatus = new BroadcastThread(this);
	// IBinder to connect the activity
	private final IBinder mBinder = new LocalBinder();

	// Constructor
	public MessageService() {
		// TODO Auto-generated constructor stub
	}

	public class LocalBinder extends Binder {
		MessageService getService() {
			return MessageService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	public void broadcastOnline() {
		broadcastStatus.start();
		Log.i("Activity", "Created");
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
