package imgsplit.func;

import java.awt.image.BufferedImage;
import java.io.File;

import imgsplit.Imgsplit;
import imgsplit.Utils;
import misc.IndexedList;
import misc.Properties;

public class FunctionCombine implements Function{

	public String execute(Properties p) {
		String bg = p.getString("bg");
		String fg = p.getString("fg");
		String out = p.getString("out");
		
		bg = Utils.fixPath(bg, true);
		fg = Utils.fixPath(fg, true);
		out = Utils.fixPath(out, true);
		
		Imgsplit.validateFolder(bg);
		Imgsplit.validateFolder(fg);
		Imgsplit.validateFolder(out);

		String bgName = new File(bg).getName();
		String fgName = new File(fg).getName();
		
		int w = p.getInteger("w");
		int h = p.getInteger("h");

		IndexedList<BufferedImage> imgs1 = Imgsplit.getAllImages(Imgsplit.getInstance().getGui(), bg, false, "png", w, h);
		IndexedList<BufferedImage> imgs2 = Imgsplit.getAllImages(Imgsplit.getInstance().getGui(), fg, false, "png", w, h);
		IndexedList<BufferedImage> outputImages = new IndexedList<BufferedImage>();
		
		int i=0;
		while(i<imgs1.getSize()) {
			int j=0;
			while(j<imgs2.getSize()) {
				BufferedImage newImg = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
				newImg.getGraphics().drawImage(imgs1.get(i), 0, 0, null);
				newImg.getGraphics().drawImage(imgs2.get(j), 0, 0, null);
				outputImages.add(bgName+i+"-"+fgName+j, newImg);
				j++;
			}
			i++;
		}
		
		Imgsplit.saveAllImages(Imgsplit.getInstance().getGui(), outputImages, out);
		return null;
	}

	public String getMethodName() {
		return "combine";
	}

}
