package com.android.chat.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
	Socket clientSocket;
	OutputStream ostream;
	PrintWriter pwrite;
	InputStream istream;
	BufferedReader receiveRead;

	public ClientThread(Socket clientSocket) throws IOException {
		this.clientSocket = clientSocket;
		ostream = clientSocket.getOutputStream();
		pwrite = new PrintWriter(ostream, true);
		istream = clientSocket.getInputStream();
		receiveRead = new BufferedReader(new InputStreamReader(istream));
	}

	public void run() {
		String receiveMsg;
		String sendMsg;
	}

	// Initialize all the variables
	public void init() {

	}
}
