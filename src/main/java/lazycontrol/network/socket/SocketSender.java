package main.java.lazycontrol.network.socket;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import main.java.lazycontrol.misc.ImageComparator;
import main.java.lazycontrol.misc.ScreenCapture;

public class SocketSender extends SocketThread {

	public int interlacedPass = 2;

	private PrintWriter printWritter;
	private BufferedImage screenCapture, old;
	private boolean init = false;
	private String trame;
	private boolean sendScreenCapture = false;

	private List<String> trames;

	private int width = 480;

	public SocketSender(Socket socket, int threadSleepMs) {
		super(socket, threadSleepMs);
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
					trame = header.resolution.toString() + screenCapture.getWidth() + separator + screenCapture.getHeight() + separator;
					trame += ScreenCapture.getScreenCaptureBound().width + separator + ScreenCapture.getScreenCaptureBound().height + separator;
					for (Rectangle bound : ScreenCapture.getScreenBounds()) {
						trame += bound.x + separator + bound.y + separator + bound.width + separator + bound.height + separator;
					}
					printWritter.println(trame);
					printWritter.flush();
					old = null;
					init = true;
				}

				// Send Interlaced
				for (int i = 0; i < interlacedPass; i++) {
					trame = ImageComparator.getImageDiffTrame(screenCapture, old, i, interlacedPass);
					if (trame.length() > 0) {
						printWritter.print(header.rgbs.toString());
						printWritter.println(trame);
						printWritter.flush();
					}
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

	public void mouseReleased(int x, int y, int button) {
		addTrame(header.mouseReleased.toString() + x + separator + y + separator + button);
	}

	public void mousePressed(int x, int y, int button) {
		addTrame(header.mousePressed.toString() + x + separator + y + separator + button);
	}

	public void keyReleased(int key) {
		addTrame(header.keyReleased.toString() + key);
	}

	public void keyPressed(int key) {
		addTrame(header.keyPressed.toString() + key);
	}

	public void setInterlacedPass(int interlacedPass) {
		this.interlacedPass = Math.max(interlacedPass, 2);
	}

}
