package imgsplit.func;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import imgsplit.Imgsplit;
import imgsplit.InvalidInputException;
import imgsplit.Utils;
import misc.FileUtils;
import misc.IndexedList;
import misc.Properties;
import misc.StringUtils;

public class FunctionCreateSheet implements Function{

	@Override
	public String execute(Properties p) {
		boolean includeSubDirectories = p.getBoolean(Imgsplit.cmdIncludeSubdirectories);
		boolean saveNames = p.getBoolean(Imgsplit.cmdSaveNames);
		boolean rmdup = p.getBoolean(Imgsplit.cmdRemoveDuplicates);
		String out = p.getString("out");
		String in = p.getString("folder");
		
		if(StringUtils.isEmpty(in)) {
			System.out.println("Path is empty, using default path");
			in = Imgsplit.getPath();
		}

		System.out.println("Include Subdirectories: "+includeSubDirectories);
		System.out.println("Save Names: "+saveNames);
		System.out.println("Remove Duplicates: "+rmdup);
		System.out.println("Out: "+out);
		
		if(StringUtils.isEmpty(in)) {
			System.out.println("Path Emtpy");
			in = Imgsplit.path;
		}
		in = Utils.fixPath(in, true);
		Imgsplit.validateFolder(in);

		System.out.println("In: "+in);
		
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
		
		IndexedList<BufferedImage> allImages = Imgsplit.getAllImages(Imgsplit.getInstance().getGui(), in, includeSubDirectories, "png", w, h);
		
		if(rmdup) {
			allImages = Imgsplit.removeDuplicateImages(Imgsplit.getInstance().getGui(), allImages);
		}
		
		BufferedImage sheet = Imgsplit.createSheet(Imgsplit.getInstance().getGui(), allImages);

		
		String[] names = allImages.getIdArray();
		
		String ext = StringUtils.getFilenameExtension(out);
		if(ext != null)out = out.substring(0, out.length() - ext.length() - 1);
		
		String imgFileName = out + ".png";
		File outputFile = new File(imgFileName);
		
		try {
			ImageIO.write(sheet, "png", outputFile);
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error while saving image!");
		}
		if(saveNames) {
			FileUtils.writeAllLines(names, out + "-names.txt");
		}
		return null;
	}

	public String getMethodName() {
		return "createSheet";
	}

}
