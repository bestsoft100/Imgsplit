package misc;

import java.awt.image.BufferedImage;

public abstract class ImageUtils {
	
	public static boolean isEqual(BufferedImage img1, BufferedImage img2) {
		if(img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight())return false;
		
		int i=0;
		while(i<img1.getWidth()) {
			int j=0;
			while(j<img1.getHeight()) {
				int c1 = img1.getRGB(i, j);
				int c2 = img2.getRGB(i, j);
				if(c1 != c2) {
					return false;
				}
				j++;
			}
			i++;
		}
		
		return true;
	}
	
	
	
}
