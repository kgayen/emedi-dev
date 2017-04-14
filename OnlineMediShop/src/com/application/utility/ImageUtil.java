package com.application.utility;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ImageUtil {
	
	public static BufferedImage resizeImage(BufferedImage originalImage, int type){
		BufferedImage resizedImage = new BufferedImage(30, 30, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, 30, 30, null);
		g.dispose();
		return resizedImage;
	}
}
