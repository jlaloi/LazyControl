package main.java.lazycontrol.misc;

import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import main.java.lazycontrol.ressources.Factory;

public class ScreenCapture {

	public static List<Rectangle> getScreenBounds() {
		List<Rectangle> result = new ArrayList<Rectangle>();
		for (GraphicsDevice device : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
			result.add(device.getDefaultConfiguration().getBounds());
		}
		return result;
	}

	public static Rectangle getScreenCaptureBound() {
		int width = 0;
		int height = 0;
		int x = 0;
		int y = 0;
		for (Rectangle b : getScreenBounds()) {
			width += b.width;
			height = Math.max(height, b.height);
			x = Math.min(x, b.x);
			y = Math.min(y, b.y);
		}
		return new Rectangle(x, y, width, height);
	}

	public static BufferedImage getScreenCapture() {
		return getScreenCapture(getScreenCaptureBound());
	}

	public static BufferedImage getScreenCapture(Rectangle bounds) {
		return Factory.getRobot().createScreenCapture(bounds);
	}

	public static BufferedImage resizeImage(BufferedImage image, int width, int height) {
		BufferedImage resutl = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = resutl.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.drawImage(image, 0, 0, width, height, null);
		graphics2D.dispose();
		return resutl;
	}

}
