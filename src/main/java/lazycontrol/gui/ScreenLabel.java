package main.java.lazycontrol.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JLabel;

import main.java.lazycontrol.ressources.Factory;

public class ScreenLabel extends JLabel {

	private int originalWidth, originalHeight;
	private int xCo, yCo;
	private ScreenMouseListener screenMouseListener;
	private ScreenKeyListener screenKeyListener;

	public ScreenLabel() {
		super();
		setPreferredSize(new Dimension(1280, 720));
		setOpaque(false);
		setForeground(Color.WHITE);
		setHorizontalAlignment(JLabel.LEFT);
		setVerticalAlignment(JLabel.TOP);
		setFocusable(true);
		screenMouseListener = new ScreenMouseListener();
		screenKeyListener = new ScreenKeyListener();
	}

	public void setOriginalResolution(int originalWidth, int originalHeight) {
		this.originalWidth = originalWidth;
		this.originalHeight = originalHeight;
	}

	public void setScreenBounds(List<Rectangle> screenBounds) {
		xCo = 0;
		yCo = 0;
		for (Rectangle r : screenBounds) {
			if (r.x < 0) {
				xCo += r.x;
			}
			if (r.y < 0) {
				yCo += r.y;
			}
		}
	}

	public static int getMouseClick(int click) {
		int result = 0;
		switch (click) {
		case 1:
		case 4:
			result = InputEvent.BUTTON1_MASK;
			break;
		case 2:
			result = InputEvent.BUTTON2_MASK;
			break;
		case 3:
			result = InputEvent.BUTTON3_MASK;
			break;
		default:
			result = 0;
			break;
		}
		return result;
	}

	public void setControl(boolean control) {
		System.out.println("Control: " + control);
		if (control) {
			addKeyListener(screenKeyListener);
			addMouseListener(screenMouseListener);
		} else {
			removeKeyListener(screenKeyListener);
			removeMouseListener(screenMouseListener);
		}
	}

	class ScreenMouseListener extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			int x = (e.getX() * originalWidth / getIcon().getIconWidth()) + xCo;
			int y = (e.getY() * originalHeight / getIcon().getIconHeight()) + yCo;
			Factory.getClientSocketSender().mouseReleased(x, y, getMouseClick(e.getButton()));
		}

		public void mousePressed(MouseEvent e) {
			int x = (e.getX() * originalWidth / getIcon().getIconWidth()) + xCo;
			int y = (e.getY() * originalHeight / getIcon().getIconHeight()) + yCo;
			Factory.getClientSocketSender().mousePressed(x, y, getMouseClick(e.getButton()));
		}

		public void mouseEntered(MouseEvent e) {
			requestFocus();
			requestFocusInWindow();
		}
	}

	class ScreenKeyListener extends KeyAdapter {
		public void keyReleased(KeyEvent e) {
			Factory.getClientSocketSender().keyReleased(e.getKeyCode());
		}

		public void keyPressed(KeyEvent e) {
			Factory.getClientSocketSender().keyPressed(e.getKeyCode());
		}
	}
}
