package main.java.lazycontrol.network;

import java.io.PrintWriter;
import java.net.Socket;

import main.java.lazycontrol.ressources.Factory;

public class Client extends Thread {

	private String address, password;
	private int port;
	private int threadSleepMs;

	private Socket socket;

	private PrintWriter printWriter;

	public Client(String address, int port, String password, int threadSleepMs) {
		super();
		this.address = address;
		this.port = port;
		this.password = password;
		this.threadSleepMs = threadSleepMs;
	}

	public void run() {
		try {
			socket = new Socket(address, port);
			printWriter = new PrintWriter(socket.getOutputStream());
			printWriter.println(password);
			printWriter.flush();
			Factory.initClientSocketReceiver(socket, threadSleepMs);
			Factory.initClientSocketSender(socket, threadSleepMs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stopClient() {
		try {
			Factory.getClientSocketReceiver().stopThread();
			Factory.getClientSocketSender().stopThread();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
