package imgsplit.func;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import imgsplit.Imgsplit;
import imgsplit.Utils;
import misc.IndexedList;
import misc.Properties;
import misc.StringUtils;

public class FunctionAdvancedSheet implements Function{

	private static class Dimension{
		private int width;
		private int height;
		public Dimension(int width, int height) {
			this.width = width;
			this.height = height;
		}
		public int getWidth() {
			return width;
		}
		public int getHeight() {
			return height;
		}
	}
	
	public String execute(Properties p) {
		String folder = p.getString("folder");
		if(StringUtils.isEmpty(folder))folder = Imgsplit.getPath();
		folder = Utils.fixPath(folder, true);
		
		IndexedList<BufferedImage> imgs = Imgsplit.getAllImages(Imgsplit.getInstance().getGui(), folder, true, "png", -1, -1);
		
		ArrayList<Dimension> dims = new ArrayList<Dimension>();
		int i=0;
		while(i<imgs.getSize()) {
			BufferedImage img = imgs.get(i);
			Dimension d = new Dimension(img.getWidth(), img.getHeight());
			boolean add = true;
			int j=0;
			while(j<dims.size()) {
				if(isEqual(d, dims.get(j))) {
					add = false;
					break;
				}
				j++;
			}
			if(add)dims.add(d);
			
			i++;
		}
		
		ArrayList<BufferedImage> sheets = new ArrayList<BufferedImage>();

		System.out.println("Creating "+dims.size()+" Sheets");
		i=0;
		while(i<dims.size()) {
			Dimension dim = dims.get(i);
			System.out.println(dim.getWidth() + " x "+dim.getHeight());
			
			IndexedList<BufferedImage> tempSheet = new IndexedList<BufferedImage>();
			int j=0;
			while(j<imgs.getSize()) {
				BufferedImage img = imgs.get(j);
				if(dim.getWidth() == img.getWidth() && dim.getHeight() == img.getHeight()) {
					tempSheet.add(imgs.getId(j), img);
				}
				j++;
			}
			
			sheets.add(Imgsplit.createSheet(Imgsplit.getInstance().getGui(), tempSheet));
			i++;
		}
		
		int w = 0;
		int h = 0;
		i=0;
		while(i<sheets.size()) {
			if(sheets.get(i).getWidth() > w)w = sheets.get(i).getWidth();
			i++;
		}
		i=0;
		while(i<sheets.size()) {
			h += sheets.get(i).getHeight();
			i++;
		}
		
		System.out.println("Drawing All Sheets");
		BufferedImage img = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
		i=0;
		int pos=0;
		Graphics g = img.getGraphics();
		while(i<sheets.size()) {
			g.drawImage(sheets.get(i), 0, pos, null);
			pos += sheets.get(i).getHeight();
			
			i++;
		}
		
		try {
			ImageIO.write(img, "png", new File("OUT.PNG"));
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return "Done";
	}
	
	private static boolean isEqual(Dimension dim1, Dimension dim2) {
		return dim1.getWidth() == dim2.getWidth() && dim1.getHeight() == dim2.getHeight();
	}

	public String getMethodName() {
		return "advancedSheet";
	}

}
