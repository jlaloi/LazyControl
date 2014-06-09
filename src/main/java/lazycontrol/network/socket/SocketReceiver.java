package main.java.lazycontrol.network.socket;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import main.java.lazycontrol.misc.ImageComparator;
import main.java.lazycontrol.ressources.Factory;

public class SocketReceiver extends SocketThread {

	private BufferedReader bufferedReader;
	private BufferedImage image;
	private String trame;
	private boolean allowControl = false;

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
				Factory.getServerFrame().setOriginalResolution(Integer.valueOf(dimension[2]), Integer.valueOf(dimension[3]));
				List<Rectangle> screenBounds = new ArrayList<Rectangle>();
				for (int i = 4; i < dimension.length; i += 4) {
					screenBounds.add(new Rectangle(Integer.valueOf(dimension[i]), Integer.valueOf(dimension[i + 1]), Integer.valueOf(dimension[i + 2]), Integer.valueOf(dimension[i + 3])));
				}
				Factory.getServerFrame().setScreenBounds(screenBounds);
			} else if (trame.startsWith(header.rgbs.name())) {
				ImageComparator.insertImageChange(image, trame.substring(header.rgbs.name().length()));
				Factory.getServerFrame().setScreenCapture(new ImageIcon(image), trame.length());
			} else if (trame.startsWith(header.screenCaptureSize.name())) {
				Factory.getServerSocketSender().setWidth(Integer.valueOf(trame.substring(header.screenCaptureSize.name().length())));
			} else if (allowControl && trame.startsWith(header.mousePressed.name())) {
				System.out.println(trame);
				String[] param = trame.substring(header.mousePressed.name().length()).split(separator);
				Factory.getRobot().mouseMove(Integer.valueOf(param[0]), Integer.valueOf(param[1]));
				sleep(10);
				Factory.getRobot().mousePress(Integer.valueOf(param[2]));
			} else if (allowControl && trame.startsWith(header.mouseReleased.name())) {
				String[] param = trame.substring(header.mouseReleased.name().length()).split(separator);
				System.out.println(trame);
				Factory.getRobot().mouseMove(Integer.valueOf(param[0]), Integer.valueOf(param[1]));
				sleep(10);
				Factory.getRobot().mouseRelease(Integer.valueOf(param[2]));
			} else if (allowControl && trame.startsWith(header.keyPressed.name())) {
				int c = Integer.valueOf(trame.substring(header.keyPressed.name().length()));
				System.out.println("Key Pressed: " + ((char) c));
				Factory.getRobot().keyPress(c);
			} else if (allowControl && trame.startsWith(header.keyReleased.name())) {
				int c = Integer.valueOf(trame.substring(header.keyReleased.name().length()));
				System.out.println("Key Released: " + ((char) c));
				Factory.getRobot().keyRelease(c);
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

	public boolean isAllowControl() {
		return allowControl;
	}

	public void setAllowControl(boolean allowControl) {
		this.allowControl = allowControl;
	}

}
