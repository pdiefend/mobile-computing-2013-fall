package com.android.chat.fragments;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;

public class ServerThread extends Thread {
	private static final String TAG = "ServerThread: ";

	// The port the server is listen to.
	int port;

	public ServerThread(int port) {
		this.port = port;
	}

	public void run() {
		Log.i(TAG, "started");

		waitingForClient();
	}

	public void waitingForClient() {
		ServerSocket serverSocket = null;
		Socket clientSocket = null;
		String clientIP = null;

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Could not listen on port: " + port);
			System.exit(-1);
		}

		try {
			while (true) {
				clientSocket = serverSocket.accept();
				clientIP = clientSocket.getInetAddress().getHostAddress();
				Log.i(TAG, "Creating client thread..." + clientIP);
				(new ClientThread(clientSocket)).start();
			}
		} catch (IOException e) {
			System.out.println("Accept failed: " + port);
			System.exit(-1);
		}
	}
}