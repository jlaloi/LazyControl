package main.java.lazycontrol.misc;

import java.awt.image.BufferedImage;

import main.java.lazycontrol.ressources.Factory;

public class ImageComparator {

	public static final String separatorValue = "#";
	public static final String separatorObject = ",";

	public static String getImageDiffTrame(BufferedImage newImage, BufferedImage originalImage, int startPos, int pad) {
		StringBuilder trame = new StringBuilder();
		int rgb, x, y, w, h;
		w = newImage.getWidth();
		h = newImage.getHeight();
		int lastX, lastY = 0, lastRgb = 0, count = 0;
		if (originalImage == null || (w == originalImage.getWidth() && h == originalImage.getHeight())) {
			for (y = startPos; y < h; y += pad) {
				lastX = -1;
				for (x = 0; x < w; x++) {
					rgb = newImage.getRGB(x, y);
					if (originalImage == null || rgb != originalImage.getRGB(x, y)) {
						if (lastX == -1 || rgb != lastRgb) {
							if (lastX != -1) {
								addRGBtoTrame(trame, lastX, lastY, lastRgb, count);
							}
							lastRgb = rgb;
							lastX = x;
							lastY = y;
							count = 1;
						} else {
							count++;
						}
					} else if (lastX != -1) {
						addRGBtoTrame(trame, lastX, lastY, lastRgb, count);
						lastX = -1;
					}
				}
				if (lastX != -1) {
					addRGBtoTrame(trame, lastX, lastY, lastRgb, count);
				}
			}
		}
		return trame.toString();
	}

	private static void addRGBtoTrame(StringBuilder trame, int x, int y, int rgb, int count) {
		if (count > 1) {
			trame.append(x + separatorValue + y + separatorValue + rgb + separatorValue + (count - 1) + separatorObject);
		} else {
			trame.append(x + separatorValue + y + separatorValue + rgb + separatorObject);
		}
	}

	public static BufferedImage insertImageChange(BufferedImage image, String trame) {
		String[] array;
		int x, y, rgb, count;
		for (String rgbs : trame.split(separatorObject)) {
			array = rgbs.split(separatorValue);
			x = Integer.valueOf(array[0]);
			y = Integer.valueOf(array[1]);
			rgb = Integer.valueOf(array[2]);
			try{

				if (array.length == 3) {
					image.setRGB(x, y, rgb);
				} else if (array.length == 4) {
					count = Integer.valueOf(array[3]);
					for (int i = 0; i < count + 1; i++) {
						image.setRGB(x + i, y, rgb);
					}
				}
			}catch (Exception e) {
				// TODO: Real fix...
				System.out.println("Desynchronization ...");
				Factory.getServerFrame().updateCaptureResolution();
			}
		}
		return image;
	}

}
