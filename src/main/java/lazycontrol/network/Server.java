package main.java.lazycontrol.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import main.java.lazycontrol.ressources.Factory;

public class Server extends Thread {

	private int port;
	private String password;
	private ServerSocket serverSocket;
	private boolean stop = false;
	private BufferedReader bufferedReader;

	public Server(int port, String password) {
		super();
		this.port = port;
		this.password = password;
	}

	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Listening on " + port);
			while (!stop) {
				Socket socket = serverSocket.accept();
				System.out.println("New socket from " + socket.getInetAddress());
				bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String received = bufferedReader.readLine();
				if (received.equals(password)) {
					System.out.println("Socket accepted");
					Factory.initServerSocketReceiver(socket);
					Factory.initServerSocketSender(socket).setSendScreenCapture(true);
				} else {
					System.out.println("Socket closed");
					socket.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		stop = true;
		try {
			serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
