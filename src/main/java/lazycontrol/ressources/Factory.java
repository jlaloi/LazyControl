package main.java.lazycontrol.ressources;

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

	public static ClientFrame getServerFrame() {
		if (serverFrame == null) {
			serverFrame = new ClientFrame();
		}
		return serverFrame;
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

	public static SocketSender initClientSocketSender(Socket socket) {
		clientSocketSender = new SocketSender(socket);
		clientSocketSender.start();
		return clientSocketSender;
	}

	public static SocketSender initServerSocketSender(Socket socket) {
		serverSocketSender = new SocketSender(socket);
		serverSocketSender.start();
		return serverSocketSender;
	}

	public static SocketReceiver initClientSocketReceiver(Socket socket) {
		clientSocketReceiver = new SocketReceiver(socket);
		clientSocketReceiver.start();
		return clientSocketReceiver;
	}

	public static SocketReceiver initServerSocketReceiver(Socket socket) {
		serverSocketReceiver = new SocketReceiver(socket);
		serverSocketReceiver.start();
		return serverSocketReceiver;
	}
}
