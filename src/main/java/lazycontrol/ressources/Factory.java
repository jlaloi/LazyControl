package main.java.lazycontrol.ressources;

import java.awt.Robot;
import java.net.Socket;

import main.java.lazycontrol.gui.ClientFrame;
import main.java.lazycontrol.network.socket.SocketReceiver;
import main.java.lazycontrol.network.socket.SocketSender;

public class Factory {

	public static final String appName = "Lazy Control";
	public static final int defaultPort = 45878;
	public static final String defaultAddress = "localhost";

	private static ClientFrame serverFrame;
	private static SocketReceiver serverSocketReceiver, clientSocketReceiver;
	private static SocketSender serverSocketSender, clientSocketSender;

	private static Robot robot;

	public static ClientFrame getServerFrame() {
		if (serverFrame == null) {
			serverFrame = new ClientFrame();
		}
		return serverFrame;
	}

	public static Robot getRobot() {
		if (robot == null) {
			try {
				robot = new Robot();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return robot;
	}

	public static SocketReceiver getServerSocketReceiver() {
		return serverSocketReceiver;
	}

	public static SocketReceiver getClientSocketReceiver() {
		return clientSocketReceiver;
	}

	public static SocketSender getServerSocketSender() {
		return serverSocketSender;
	}

	public static SocketSender getClientSocketSender() {
		return clientSocketSender;
	}

	public static SocketSender initClientSocketSender(Socket socket, int threadSleepMs) {
		clientSocketSender = new SocketSender(socket, threadSleepMs);
		clientSocketSender.start();
		return clientSocketSender;
	}

	public static SocketSender initServerSocketSender(Socket socket, int threadSleepMs) {
		serverSocketSender = new SocketSender(socket, threadSleepMs);
		serverSocketSender.start();
		return serverSocketSender;
	}

	public static SocketReceiver initClientSocketReceiver(Socket socket, int threadSleepMs) {
		clientSocketReceiver = new SocketReceiver(socket, threadSleepMs);
		clientSocketReceiver.start();
		return clientSocketReceiver;
	}

	public static SocketReceiver initServerSocketReceiver(Socket socket, int threadSleepMs) {
		serverSocketReceiver = new SocketReceiver(socket, threadSleepMs);
		serverSocketReceiver.start();
		return serverSocketReceiver;
	}
}
