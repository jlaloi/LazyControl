package main.java.lazycontrol.gui;

import java.awt.Dimension;

import javax.swing.JPasswordField;

class PasswordField extends JPasswordField {
	public PasswordField() {
		super();
		setPreferredSize(new Dimension(100, getPreferredSize().height));
	}
}