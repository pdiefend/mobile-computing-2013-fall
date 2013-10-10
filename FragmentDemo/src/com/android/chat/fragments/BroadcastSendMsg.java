package com.android.chat.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class BroadcastSendMsg extends Thread {

	private static final String TAG = "BroadcastSendMsg";
	public static final String BROADCAST_MSGSENT = "com.android.chat.fragment.mainactivity";
	private final Handler handler = new Handler();
	Intent intent = new Intent(BROADCAST_MSGSENT);
	Context mContext;

	private String message;
	private String ip;

	public BroadcastSendMsg(Context mContext, String message, String ip) {
		this.mContext = mContext;
		this.message = message;
		this.ip = ip;
		Log.i(TAG, "Sending intent: " + message + " " + ip);
	}

	public void run() {
		handler.removeCallbacks(sendMsg);
		handler.postDelayed(sendMsg, 1000);
	}

	public Runnable sendMsg = new Runnable() {
		public void run() {
			Log.i(TAG, "Entered SendMsg");
			broadcastMsgToSocket();
			handler.postDelayed(this, 1000);
		}
	};

	private void broadcastMsgToSocket() {
		Log.i(TAG, "Broadcasting the intent");
		intent.putExtra("msg", message);
		intent.putExtra("ip", ip);
		mContext.sendBroadcast(intent);
	}
}
