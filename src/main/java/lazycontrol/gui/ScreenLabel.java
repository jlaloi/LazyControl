package main.java.lazycontrol.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import main.java.lazycontrol.ressources.Factory;

public class ScreenLabel extends JLabel {

	private int originalWidth, originalHeight;

	public ScreenLabel() {
		super();
		setPreferredSize(new Dimension(1280, 720));
		setOpaque(false);
		setForeground(Color.WHITE);
		setFocusable(true);
		setHorizontalAlignment(JLabel.CENTER);
		addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				int x = e.getX() * originalWidth / getIcon().getIconWidth();
				int y = e.getY() * originalHeight / getIcon().getIconHeight();
				Factory.getClientSocketSender().mouseReleased(x, y, getMouseClick(e.getButton()));
			}

			public void mousePressed(MouseEvent e) {
				int x = e.getX() * originalWidth / getIcon().getIconWidth();
				int y = e.getY() * originalHeight / getIcon().getIconHeight();
				Factory.getClientSocketSender().mousePressed(x, y, getMouseClick(e.getButton()));
			}
		});
		addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				Factory.getClientSocketSender().keyReleased(e.getKeyCode());
			}

			public void keyPressed(KeyEvent e) {
				Factory.getClientSocketSender().keyPressed(e.getKeyCode());
			}
		});
	}

	public void setOriginalResolution(int originalWidth, int originalHeight) {
		this.originalWidth = originalWidth;
		this.originalHeight = originalHeight;
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
}
