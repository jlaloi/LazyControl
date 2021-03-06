package main.java.lazycontrol.network.socket;

import java.net.Socket;

public abstract class SocketThread extends Thread {

	protected Socket socket;
	protected boolean stop = false;
	protected int threadSleepMs;

	public enum header {
		resolution, rgbs, screenCaptureSize, mousePressed, mouseReleased, keyPressed, keyReleased;
	};

	public static final String separator = ",";

	public SocketThread(Socket socket, int threadSleepMs) {
		super();
		this.socket = socket;
		this.threadSleepMs = threadSleepMs;
	}

	public void run() {
		try {
			init();
			while (!stop) {
				execute();
				sleep(threadSleepMs);
			}
			finalize();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				System.out.println("Closing socket " + socket.getInetAddress());
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void stopThread() {
		stop = true;
	}

	public boolean isStop() {
		return stop;
	}

	protected abstract void execute() throws Exception;

	protected abstract void init();

	protected abstract void finalize();
}
