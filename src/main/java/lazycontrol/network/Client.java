package main.java.lazycontrol.network;

import java.io.PrintWriter;
import java.net.Socket;

import main.java.lazycontrol.ressources.Factory;

public class Client extends Thread {

	private String address, password;
	private int port;

	private Socket socket;

	private PrintWriter printWriter;

	public Client(String address, int port, String password) {
		super();
		this.address = address;
		this.port = port;
		this.password = password;
	}

	public void run() {
		try {
			socket = new Socket(address, port);
			printWriter = new PrintWriter(socket.getOutputStream());
			printWriter.println(password);
			printWriter.flush();
			Factory.initClientSocketReceiver(socket);
			Factory.initClientSocketSender(socket);
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
