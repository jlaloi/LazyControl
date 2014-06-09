package main.java.lazycontrol.network.socket;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import main.java.lazycontrol.misc.ImageComparator;
import main.java.lazycontrol.misc.ScreenCapture;

public class SocketSender extends SocketThread {

	private PrintWriter printWritter;
	private BufferedImage screenCapture, old;
	private boolean init = false;
	private String trame;
	private boolean sendScreenCapture = false;

	private List<String> trames;

	private int width = 480;

	public SocketSender(Socket socket) {
		super(socket);
		trames = new ArrayList<String>();
	}

	protected void execute() throws Exception {
		try {
			if (sendScreenCapture) {
				// Capture
				screenCapture = ScreenCapture.getScreenCapture();
				if (width < screenCapture.getWidth()) {
					screenCapture = ScreenCapture.resizeImage(screenCapture, width, screenCapture.getHeight() * width / screenCapture.getWidth());
				}

				// Resolution change
				if (!init) {
					trame = header.resolution.toString() + screenCapture.getWidth() + separator + screenCapture.getHeight();
					printWritter.println(trame);
					printWritter.flush();
					old = null;
					init = true;
				}

				// Send Interlaced
				trame = ImageComparator.getImageDiffTrame(screenCapture, old, 0, 2);
				if (trame.length() > 0) {
					printWritter.print(header.rgbs.toString());
					printWritter.println(trame);
					printWritter.flush();
				}
				trame = ImageComparator.getImageDiffTrame(screenCapture, old, 1, 2);
				if (trame.length() > 0) {
					printWritter.print(header.rgbs.toString());
					printWritter.println(trame);
					printWritter.flush();
				}

				// Flush...
				old = screenCapture;
				trame = "";
				screenCapture.flush();
				screenCapture = null;

			}

			// Sending trames
			synchronized (trames) {
				if (trames.size() > 0) {
					for (String trame : trames) {
						printWritter.println(trame);
						printWritter.flush();
					}
					trames.clear();
				}
			}

			// Check error
			if (printWritter.checkError()) {
				System.out.println("Out error, stopping thead");
				stopThread();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void init() {
		try {
			printWritter = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void finalize() {
		try {
			printWritter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setWidth(int width) {
		this.width = width;
		init = false;
	}

	public boolean isSendScreenCapture() {
		return sendScreenCapture;
	}

	public void setSendScreenCapture(boolean sendScreenCapture) {
		this.sendScreenCapture = sendScreenCapture;
	}

	public void addTrame(String trame) {
		synchronized (trames) {
			trames.add(trame);
		}
	}

	public void changeScreenCaptureSize(int width) {
		addTrame(header.screenCaptureSize.toString() + width);
	}
}
