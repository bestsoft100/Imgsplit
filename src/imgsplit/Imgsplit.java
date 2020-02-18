package imgsplit;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import imgsplit.func.FunctionCombine;
import imgsplit.func.FunctionCreateSheet;
import imgsplit.func.FunctionEdit;
import imgsplit.func.FunctionSplit;
import imgsplit.gui.Gui;
import misc.FileUtils;
import misc.ImageUtils;
import misc.IndexedList;
import misc.Properties;
import misc.StringUtils;

public class Imgsplit implements Runnable{
	
	private static final String VERSION = "3.0";

	public static String cmdRemoveDuplicates = "removeDuplicates";
	public static String cmdIncludeSubdirectories = "includeSubdir";
	public static String cmdSaveNames = "saveNames";
	private static Imgsplit instance;
	
	public static Imgsplit getInstance() {
		return instance;
	}
	
	public static String path;

	public static Thread thread1;
	public static Thread thread2;
	
	private Properties properties;
	private Gui gui;
	private boolean nogui = false;
	
	public static void main(String[] args) {
		new Imgsplit(args);
	}
	
	public Imgsplit(String[] args) {
		instance = this;
		System.out.println("Start!");

		argsToProperties(args);
		
		properties.print();
		
		nogui = properties.getBoolean("nogui");

		path = new File("").getAbsolutePath();
		System.out.println("Path: "+path);
		
		if(!nogui) {
			thread2 = WorkThread.createThread();
			thread2.start();
			
			thread1 = new Thread(this);
			thread1.start();
		}else {
			String method = properties.getString("method");
			try {
				if(method.equalsIgnoreCase("split")) {
					new FunctionSplit().execute(properties);
				}else
				if(method.equalsIgnoreCase("createSheet")) {
					new FunctionCreateSheet().execute(properties);
				}else
				if(method.equalsIgnoreCase("combine")) {
					new FunctionCombine().execute(properties);
				}else
				if(method.equalsIgnoreCase("edit")) {
					new FunctionEdit().execute(properties);
				}
			}catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

	public void run() {
		
		gui = new Gui();
		gui.setVisible(true);
	}
	
	private void argsToProperties(String[] args) {
		properties = new Properties();
		
		
		properties.set("out", "Imgsplit Output");
		properties.set("nogui", "false");
		properties.set(cmdIncludeSubdirectories, "false");
		properties.set(cmdRemoveDuplicates, "false");
		properties.set(cmdSaveNames, "false");
		
		
		for(String ln : args) {
			if(ln != null) {
				String[] s = StringUtils.splitAtFirstOccurence(ln.replace('?', ' '), ':');
				properties.set(s[0], s[1]);
			}
		}

	}
	
	public static BufferedImage createSheet(Gui gui, IndexedList<BufferedImage> imgs) {
		System.out.println("Creating Sheet");
		System.out.println("Number of images: "+imgs.getSize());

		
		if(imgs.getSize() == 0) {
			throw new RuntimeException("No Images!");
		}
		
		int tw = imgs.get(0).getWidth();
		int th = imgs.get(0).getHeight();
		
		double dcw = Math.sqrt(imgs.getSize());
		int cw = (int) dcw + (dcw%1.0 > 0 ? 1 : 0);
		double dd = imgs.getSize() * 1.0 / cw;
		int ch = (int) dd + (dd%1.0 > 0 ? 1 : 0);
		
		System.out.println("Output Image Size: "+cw+" x "+ch);
		
		BufferedImage out = new BufferedImage(cw * tw, ch * th, BufferedImage.TYPE_INT_ARGB);
		Graphics g = out.getGraphics();
		int i=0;
		while(i<imgs.getSize()) {
			g.drawImage(imgs.get(i), (i%cw)*tw, (i/cw)*th, tw, th, null);
			i++;
		}
		return out;
	}
	
	public static IndexedList<BufferedImage> getAllImages(Gui gui, String folder, boolean includeSubdirectories, String ext, int w, int h){
		printPos();
		System.out.println("Include Subdirectories: "+includeSubdirectories);
		IndexedList<BufferedImage> imgs = new IndexedList<BufferedImage>();
		System.out.println("Folder: "+folder);
		
		File f = new File(folder);
		File[] allFiles = f.listFiles();
		if(includeSubdirectories) {
			allFiles = FileUtils.getAllFiles(folder);
		}else{
			allFiles = f.listFiles();
		}
		
		System.out.println("Found "+allFiles.length+" files");

		System.out.println("Collecting Images in: \""+f.getAbsolutePath()+"\"");
		if(gui != null) {
			gui.getStatusBar().setText("Collecting Images in: \""+f.getAbsolutePath()+"\"");
		}
		
		int folderPathLength = f.getAbsolutePath().length() + 1;
		int i=0;
		for(File temp : allFiles) {
			String ext2 = StringUtils.getFilenameExtension(temp.getName());
			
			if(ext2 != null && ext2.equals(ext)) {
				BufferedImage img = FileUtils.loadImage(temp.getAbsolutePath());
				if(img == null) {
					throw new RuntimeException("Error while loading image: \""+temp.getAbsolutePath()+"\"");
				}
				if((w == -1 || h == -1)||(img.getWidth() == w && img.getHeight() == h)) {
					String str = temp.getAbsolutePath().substring(folderPathLength);
					str = str.substring(0, str.length() - ext.length() - 1);
					imgs.add(str, img);
				}
			}

			if(i%1000==0) {
				if(gui != null) {
					gui.getProgressBar().setValue(getPercentValue(i, allFiles.length));
				}else {
					System.out.println(getPercentValue(i, allFiles.length) + "%");
				}
			}
			i++;
		}
		System.out.println("Found "+imgs.getSize()+" images");
		
		if(gui != null) {
			gui.getProgressBar().setValue(0);
			gui.getStatusBar().setText("Idle");
		}
		
		return imgs;
	}
	
	public static IndexedList<BufferedImage> split(Gui gui, BufferedImage image, String[] names, int width, int height){
		printPos();
		if(gui != null) {
			gui.getStatusBar().setText("Splitting Image...");
		}
		System.out.println("Splitting Image");
		IndexedList<BufferedImage> out = new IndexedList<BufferedImage>();
		
		int cw = image.getWidth() / width;
		int ch = image.getHeight() / height;
		int c = cw*ch;
		
		System.out.println("Image Count: "+c+" ("+cw+" x "+ch+")");
		
		int i=0;
		while(i<c) {
			BufferedImage split = image.getSubimage((i%cw)*width, (i/cw)*height, width, height);
			String name = null;
			try {
				name = names[i];
			}catch (Exception e) {}
			out.add(name, split);
			
			if(i%600 == 0) {
				if(gui != null) {
					gui.getProgressBar().setValue(getPercentValue(i, c));
				}else {
					System.out.println(getPercentValue(i, c) + "%");
				}
			}
			i++;
		}
		
		System.out.println("Splitting Done!");
		if(gui != null) {
			gui.getProgressBar().setValue(0);
			gui.getStatusBar().setText("Idle");
		}
		return out;
	}
	
	public static void saveAllImages(Gui gui, IndexedList<BufferedImage> imgs, String path) {
		printPos();
		if(gui != null) {
			gui.getStatusBar().setText("Saving Images...");
		}
		System.out.println("Saving Images...");
		path = Utils.fixPath(path, true);
		System.out.println("Output Folder: "+path);
		File folder = new File(path);
		folder.mkdirs();
		
		int i=0;
		while(i<imgs.getSize()) {
			BufferedImage img = imgs.get(i);
			String name = imgs.getId(i);
			if(name == null || name.equals("")) {
				name = ""+i;
			}
			name += ".png";
			
			File f = new File(path + name);
			try {
				File f2 = f.getParentFile();
				if(!f2.exists()) {
					f2.mkdirs();
				}
				System.out.println("Creating New File: "+f.getAbsolutePath());
				f.createNewFile();
			}catch (Exception e) {
				throw new RuntimeException("File creation failed for: "+f.getAbsolutePath());
			}
			try {
				ImageIO.write(img, StringUtils.getFilenameExtension(name), f);
			}catch (Exception e) {
				throw new RuntimeException("Image saving failed for: "+f.getAbsolutePath());
			}
			
			if(i%200==0) {
				if(gui != null) {
					gui.getProgressBar().setValue((int)((i*1.0f / imgs.getSize())*100));
				}else {
					System.out.println(getPercentValue(i, imgs.getSize()) + "%");
				}
			}
			i++;
		}

		if(gui != null) {
			gui.getProgressBar().setValue(0);
			gui.getStatusBar().setText("Idle");
		}
		System.out.println("Done");
	}
	
	public static void validateFolder(String path) {
		File f = new File(path);
		if(!f.exists()) {
			f.mkdirs();
		}
		if(f.exists() && f.isFile()) {
			throw new InvalidInputException("Folder", path, "A file with the same name already exists!");
		}
	}
	
	public static void validateFile(String path) {
		if(path == null)throw new InvalidInputException("File", path, "No path given!");
		File f = new File(path);
		if(f.exists()) {
			if(!f.isFile()) {
				throw new InvalidInputException("File", path, "There is a folder with the same name");
			}
		}
	}
	
	public static IndexedList<BufferedImage> removeDuplicateImages(Gui gui, IndexedList<BufferedImage> imgs) {
		printPos();
		IndexedList<BufferedImage> imgs2 = new IndexedList<BufferedImage>();

		if(gui != null) {
			gui.getStatusBar().setText("Removing Duplicates...");
		}else {
			System.out.println("Removing Duplicates...");
		}
		
		
		int i=0;
		while(i<imgs.getSize()) {
			int j=0;
			boolean add = true;
			while(j<imgs2.getSize()) {
				
				if(ImageUtils.isEqual(imgs.get(i), imgs2.get(j)))add = false;
				
				j++;
			}
			if(add)imgs2.add(imgs.getId(i), imgs.get(i));
			if(i%500==0) {
				if(gui != null) {
					gui.getProgressBar().setValue(getPercentValue(i, imgs.getSize()));
				}else {
					System.out.println(getPercentValue(i, imgs.getSize()) + "%");
				}
			}
			i++;
		}
		
		if(gui != null) {
			gui.getProgressBar().setValue(0);
			gui.getStatusBar().setText("Idle");
		}
		
		return imgs2;
	}
	
	public static int getPercentValue(int a, int b) {
		return (int)((a*1.0f / b)*100);
	}

	public Gui getGui() {
		return gui;
	}
	
	public static void printPos() {
		System.err.println(new Exception().getStackTrace()[1]);
	}
	
	public static String getPath() {
		return path;
	}
	
	public static String getVersion() {
		return VERSION;
	}
	
}
