package main.java.lazycontrol.gui;

import java.awt.Dimension;

import javax.swing.JTextField;

class TextField extends JTextField {
	public TextField(String value) {
		super(value);
		setPreferredSize(new Dimension(100, getPreferredSize().height));
	}
}