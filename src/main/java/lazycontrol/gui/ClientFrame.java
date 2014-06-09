package main.java.lazycontrol.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import main.java.lazycontrol.network.Client;
import main.java.lazycontrol.ressources.Factory;

public class ClientFrame extends JFrame {

	private ScreenLabel screenResolution;
	private int intLastSecond;
	private int fpsCount, lastSecond, lastWidth;
	private int originalWidth, originalHeight;
	private long lastResize;
	private JProgressBar jProgressBar;

	private TextField address, port;
	private PasswordField password;
	private JButton connect, disconnect;

	private Client client;
	private boolean init = false;

	public ClientFrame() {
		super(Factory.appName);
		getRootPane().setLayout(new BorderLayout());
		getRootPane().setBackground(Color.BLACK);

		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());
		getRootPane().add(controlPanel, BorderLayout.NORTH);
		address = new TextField(Factory.defaultAddress);
		port = new TextField(Factory.defaultPort + "");
		password = new PasswordField();
		connect = new JButton("Connect");
		disconnect = new JButton("Disconnect");
		disconnect.setEnabled(false);
		controlPanel.add(connect);
		controlPanel.add(new JLabel("Address: "));
		controlPanel.add(address);
		controlPanel.add(new JLabel("Port: "));
		controlPanel.add(port);
		controlPanel.add(new JLabel("Password: "));
		controlPanel.add(password);
		controlPanel.add(connect);
		controlPanel.add(disconnect);

		screenResolution = new ScreenLabel();
		getRootPane().add(screenResolution, BorderLayout.CENTER);

		jProgressBar = new JProgressBar();
		jProgressBar.setStringPainted(true);
		getRootPane().add(jProgressBar, BorderLayout.SOUTH);

		addListener();

		pack();
		setLocationRelativeTo(getParent());
		setVisible(true);
	}

	private void addListener() {
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				if (Factory.getClientSocketSender() != null) {
					if (lastWidth != screenResolution.getWidth()) {
						lastResize = Calendar.getInstance().getTimeInMillis();
						SwingUtilities.invokeLater(new ResizeResolutionChange(lastResize));
					}
				}
			}
		});
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (client != null) {
					client.stopClient();
				}
				dispose();
			}
		});
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					client = new Client(address.getText(), Integer.valueOf(port.getText()), new String(password.getPassword()));
					client.start();
					init = false;
					connect.setEnabled(false);
					disconnect.setEnabled(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		disconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (client != null) {
					client.stopClient();
					screenResolution.setIcon(null);
				}
				connect.setEnabled(true);
				disconnect.setEnabled(false);
			}
		});
	}

	public void setScreenCapture(ImageIcon image, int length) {
		lastSecond = Calendar.getInstance().get(Calendar.SECOND);
		fpsCount++;
		if (lastSecond != intLastSecond) {
			setTitle(Factory.appName + " - " + fpsCount + " fps" + " - " + image.getIconWidth() + "x" + image.getIconHeight() + " - (" + originalWidth + "x" + originalHeight + ")");
			fpsCount = 0;
			intLastSecond = lastSecond;
		}

		if (!init) {
			updateCaptureResolution();
			init = true;
		}

		screenResolution.setIcon(image);

		if (jProgressBar.getMaximum() < length) {
			jProgressBar.setMaximum(length);
		}
		jProgressBar.setValue(length);
		jProgressBar.setString(((int) (length * 100f / jProgressBar.getMaximum())) + "%");
	}

	public void setOriginalResolution(int originalWidth, int originalHeight) {
		this.originalWidth = originalWidth;
		this.originalHeight = originalHeight;
		screenResolution.setOriginalResolution(originalWidth, originalHeight);
	}

	private void updateCaptureResolution() {
		Factory.getClientSocketSender().changeScreenCaptureSize(screenResolution.getWidth());
		lastWidth = screenResolution.getWidth();
	}

	class ResizeResolutionChange implements Runnable {
		private long time;

		private ResizeResolutionChange(long time) {
			super();
			this.time = time;
		}

		public void run() {
			if (lastResize == time && lastWidth != screenResolution.getWidth()) {
				updateCaptureResolution();
			}
		}

	}
}
