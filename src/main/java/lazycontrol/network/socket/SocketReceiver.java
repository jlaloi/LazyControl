package main.java.lazycontrol.network.socket;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.ImageIcon;

import main.java.lazycontrol.misc.ImageComparator;
import main.java.lazycontrol.ressources.Factory;

public class SocketReceiver extends SocketThread {

	private BufferedReader bufferedReader;
	private BufferedImage image;
	private String trame;

	public SocketReceiver(Socket socket) {
		super(socket);
	}

	protected void execute() {
		try {
			trame = bufferedReader.readLine();
			if (trame == null) {
				System.out.println("Null trame received, stopping thead");
				stopThread();
			} else if (trame.startsWith(header.resolution.name())) {
				String[] dimension = trame.substring(header.resolution.name().length()).split(separator);
				image = new BufferedImage(Integer.valueOf(dimension[0]), Integer.valueOf(dimension[1]), BufferedImage.TYPE_INT_RGB);
			} else if (trame.startsWith(header.rgbs.name())) {
				ImageComparator.insertImageChange(image, trame.substring(header.rgbs.name().length()));
				Factory.getServerFrame().setScreenCapture(new ImageIcon(image), trame.length());
			} else if (trame.startsWith(header.screenCaptureSize.name())) {
				Factory.getServerSocketSender().setWidth(Integer.valueOf(trame.substring(header.screenCaptureSize.name().length())));
			}
		} catch (Exception e) {
			e.printStackTrace();
			stopThread();
		}
	}

	protected void init() {
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void finalize() {
		try {
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
