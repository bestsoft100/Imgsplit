package misc;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public abstract class FileUtils {
	
	public static ArrayList<String> readFile(String path){
		File f = new File(path);
		if(!f.isFile())throw new NullPointerException("File not Found: \""+path+"\"");
		
		FileReader fr = null;
		try {
			fr = new FileReader(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		BufferedReader br = new BufferedReader(fr);
		
		ArrayList<String> l = new ArrayList<String>();
		
		while(true) {
			String line = null;
			try {
				line = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(line == null) {
				break;
			}else {
				l.add(line);
			}
		}
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return l;
	}
	
	public static void writeFile(String path, String content) {
		File f = new File(path);
		try{
			File f2 = new File(f.getParent());
			f2.mkdirs();
		}catch (NullPointerException e) {}
		
		if(!f.exists())
			try {
				f.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		FileWriter fw = null;
		try {
			fw = new FileWriter(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeAllLines(String[] str, String path) {
		createFile(path);
		try {
			FileWriter fw = new FileWriter(new File(path));
			
			String str2 = StringUtils.toString(str,'\n');
			
			fw.write(str2);
			
			fw.close();
		}catch (IOException e) {
			e.printStackTrace();
			
			throw new RuntimeException();
		}catch (Exception e) {
			e.printStackTrace();
			
			throw new RuntimeException();
		}
	}
	
	public static File createFile(String path) {
		File f = new File(path);
		f = f.getAbsoluteFile();
		File fP = f.getParentFile();
		fP.mkdirs();
		try {
			f.createNewFile();
		} catch (IOException e) {
			throw new RuntimeException("Could not create file: "+path);
		}
		return f;
	}
	
	public static BufferedImage loadImage(String string) {
		return loadImage(string, false);
	}

	public static BufferedImage loadImage(String string, boolean silent) {
		try {
			return ImageIO.read(new File(string));
		} catch (IOException e) {
			if(!silent)e.printStackTrace();
			return null;
		}
	}
	
	public static boolean isExistingFile(String path) {
		File f = new File(path);
		return f.exists() && f.isFile();
	}
	
	public static boolean isExistingFolder(String path) {
		File f = new File(path);
		return f.exists() && f.isDirectory();
	}

	public static final char[] INVALID_FILENAME_CHAR = new char[] {'/','\\',':','*','?','"','<','>','|'};
	public static final char[] INVALID_PATH_CHAR = new char[] {':','*','?','"','<','>','|'};
	
	public static boolean isValidFilename(String filename) {
		char[] arr = filename.toCharArray();
		int i=0;
		while(i<arr.length) {
			int j=0;
			while(j<INVALID_FILENAME_CHAR.length) {
				if(arr[i] == INVALID_FILENAME_CHAR[j])return false;
				j++;
			}
			i++;
		}
		return true;
	}
	
	public static boolean isValidPath(String filename) {
		char[] arr = filename.toCharArray();
		int i=0;
		while(i<arr.length) {
			int j=0;
			while(j<INVALID_PATH_CHAR.length) {
				if(arr[i] == INVALID_PATH_CHAR[j])return false;
				j++;
			}
			i++;
		}
		return true;
	}
	
	public static String[] readAllLines(String path) {
		if(path == null)return null;
		File f = new File(path);
		String[] data;
		if(!f.exists()) {
			return null;
		}
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			
			ArrayList<String> list = new ArrayList<String>();
			
			while(true) {
				String ln = br.readLine();
				if(ln == null)break;
				
				list.add(ln);
				
			}
			
			br.close();

			data = new String[list.size()];
			int i=0;
			for (String ln : list) {
				data[i] = ln;
				i++;
			}
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
		return data;
	}
	
	public static File[] getAllFiles(String path) {
		System.out.println("Get All Files in: \""+path+"\"");
		File f = new File(path);
		ArrayList<File> files = new ArrayList<File>();
		getAllFiles(files, f);
		
		File[] arr = new File[files.size()];
		int i=0;
		while(i<files.size()) {
			arr[i] = files.get(i);
			i++;
		}
		
		return arr;
	}
	
	
	public static void getAllFiles(ArrayList<File> list, File f){
		File[] subFiles = f.listFiles();
		
		for(File f2 : subFiles) {
			if(f2.isDirectory()) {
				getAllFiles(list, f2);
			}else if(f2.isFile()) {
				list.add(f2);
			}
		}
	}
	
}
