package main.java.lazycontrol.misc;

import java.awt.image.BufferedImage;

public class ImageComparator {

	public static final String separatorValue = "#";
	public static final String separatorObject = ",";

	public static String getImageDiffTrame(BufferedImage newImage, BufferedImage originalImage, int startPos, int pad) {
		StringBuilder trame = new StringBuilder();
		int rgb, x, y, w, h;
		w = newImage.getWidth();
		h = newImage.getHeight();
		if (originalImage == null || (w == originalImage.getWidth() && h == originalImage.getHeight())) {
			for (x = 0; x < w; x++) {
				for (y = startPos; y < h; y += pad) {
					rgb = newImage.getRGB(x, y);
					if (originalImage == null || rgb != originalImage.getRGB(x, y)) {
						trame.append(x + separatorValue + y + separatorValue + rgb + separatorObject);
					}
				}
			}
		}
		return trame.toString();
	}

	public static BufferedImage insertImageChange(BufferedImage image, String trame) {
		String[] array;
		for (String rgbs : trame.split(separatorObject)) {
			array = rgbs.split(separatorValue);
			if (array.length >= 3) {
				image.setRGB(Integer.valueOf(array[0]), Integer.valueOf(array[1]), Integer.valueOf(array[2]));
			}
		}
		return image;
	}

}
