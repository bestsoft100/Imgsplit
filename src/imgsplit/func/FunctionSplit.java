package imgsplit.func;

import java.awt.image.BufferedImage;

import imgsplit.Imgsplit;
import imgsplit.InvalidInputException;
import imgsplit.Utils;
import misc.FileUtils;
import misc.IndexedList;
import misc.Properties;
import misc.StringUtils;

public class FunctionSplit implements Function{

	public String execute(Properties p) {
		String in = p.getString("in");
		if(StringUtils.getFilenameExtension(in) == null || !StringUtils.getFilenameExtension(in).equalsIgnoreCase("png"))in += ".png";
		in = Utils.fixPath(in, false);
		Imgsplit.validateFile(in);
		String namePath = p.getString("names");
		String[] names = FileUtils.readAllLines(Utils.fixPath(namePath, false));
		String out = Utils.fixPath(p.getString("out"), true);
		if(StringUtils.isEmpty(out)) {
			System.out.println("Output Path is empty, using default path: "+Imgsplit.path);
			out = Imgsplit.path;
		}
		boolean rmdup = p.getBoolean(Imgsplit.cmdRemoveDuplicates);
		
		BufferedImage img = FileUtils.loadImage(in,true);
		if(img == null)throw new InvalidInputException("Input Image", in, "The file is not an image");
		
		System.out.println("Using custom names: "+(names != null && names.length > 0));
		
		int w=-1;
		int h=-1;
		try {
			w = Integer.parseInt(p.getString("w"));
		}catch (Exception ex) {}
		try {
			h = Integer.parseInt(p.getString("h"));
		}catch (Exception ex) {}
		if(w < 1) {
			throw new InvalidInputException("Width", p.getString("w"), "Has to be an integer larger than 0");
		}
		if(h < 1) {
			throw new InvalidInputException("Height", p.getString("h"), "Has to be an integer larger than 0");
		}
		
		IndexedList<BufferedImage> split = Imgsplit.split(Imgsplit.getInstance().getGui(), img, names, w, h);
		
		if(rmdup) {
			split = Imgsplit.removeDuplicateImages(Imgsplit.getInstance().getGui(), split);
		}
		
		Imgsplit.saveAllImages(Imgsplit.getInstance().getGui(), split, out);
		
		return "Splitting complete without errors.\nSaved "+split.getSize()+" images!"; 
	}

	public String getMethodName() {
		return "split";
	}

}
