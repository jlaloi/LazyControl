package main.java.lazycontrol.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import main.java.lazycontrol.network.socket.SocketSender;
import main.java.lazycontrol.ressources.Factory;

public class Server extends Thread {

	private int port;
	private String password;
	private ServerSocket serverSocket;
	private boolean stop = false;
	private boolean allowControl;
	private BufferedReader bufferedReader;
	private int interlacedPass, threadSleepMs;

	public Server(int port, String password, boolean allowControl, int interlacedPass, int threadSleepMs) {
		super();
		this.port = port;
		this.password = password;
		this.allowControl = allowControl;
		this.interlacedPass = interlacedPass;
		this.threadSleepMs = threadSleepMs;
	}

	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Listening on " + port + ", allowControl: " + allowControl + ", interlacedPass: " + interlacedPass + ", threadSleepMs: " + threadSleepMs);
			while (!stop) {
				Socket socket = serverSocket.accept();
				System.out.println("New socket from " + socket.getInetAddress());
				bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String received = bufferedReader.readLine();
				if (received.equals(password)) {
					System.out.println("Socket accepted");
					Factory.initServerSocketReceiver(socket, threadSleepMs).setAllowControl(allowControl);
					SocketSender socketSender = Factory.initServerSocketSender(socket, threadSleepMs);
					socketSender.setSendScreenCapture(true);
					socketSender.setInterlacedPass(interlacedPass);
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
