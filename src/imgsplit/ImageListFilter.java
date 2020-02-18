package imgsplit;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import misc.IndexedList;
import misc.StringUtils;

public class ImageListFilter {
	
	public static String[] filterOld(String args, IndexedList<BufferedImage> imgs) {
		System.out.println("-------------");
		System.out.println("args: "+args);
		IndexedList<BufferedImage> imgs2 = new IndexedList<BufferedImage>();

		ArrayList<String> positiveArgs = new ArrayList<String>();
		ArrayList<String> negativeArgs = new ArrayList<String>();
		
		System.out.println(imgs.getSize() + " imgs");
		
		String[] argsArr = StringUtils.split(args, ' ');
		int i=0;
		while(i<argsArr.length) {
			if(argsArr[i].length() > 0) {
				char first = argsArr[i].charAt(0);
				if(first == '-') {
					negativeArgs.add(argsArr[i]);
				}else{
					positiveArgs.add(argsArr[i]);
				}
			}
			i++;
		}

		System.out.println(positiveArgs.size() + " positive args");
		System.out.println(negativeArgs.size() + " negative args");
		
		i=0;
		while(i<imgs.getSize()) {
			String id = imgs.getId(i);
			int j=0;
			boolean add = true;
			
			while(j<positiveArgs.size()) {
				String arg = positiveArgs.get(j);
				if(!stringContains(id, arg)) {
					add = false;
				}
				j++;
			}
			
			if(add) {
				imgs2.add(imgs.getId(i), imgs.get(i));
			}
			i++;
		}

		System.out.println(imgs2.getSize() + " imgs");
		
		i=0;
		while(i<negativeArgs.size()) {
			imgs2 = filterNegative(imgs2, negativeArgs.get(i));
			System.out.println("remain: "+imgs2.getSize());
			i++;
		}

		System.out.println(imgs2.getSize() + " imgs");
		
		String[] ret = new String[imgs2.getSize()];
		i=0;
		while(i<ret.length) {
			ret[i] = imgs2.getId(i);
			i++;
		}
		return ret;
	}
	
	public static String[] filter(String allArgs, IndexedList<BufferedImage> imgs) {
		String[] argSplit = StringUtils.split(allArgs, ' ');
		ArrayList<Argument> args = new ArrayList<ImageListFilter.Argument>();
		
		int i=0;
		while(i<argSplit.length) {
			args.add(getArgument(argSplit[i]));
			i++;
		}
		
		IndexedList<BufferedImage> original = imgs;
		
		i=0;
		while(i<args.size()) {
			imgs = args.get(i).filter(imgs, original);
			i++;
		}
		
		return imgs.getIdArray();
	}

	public static Argument getArgument(String str) {
		if(str == null || str.length() == 0) {
			return null;
		}
		char prefix = str.charAt(0);
		if(prefix == '-') {
			return new NegativeArgument(str.substring(1));
		}else if(prefix == '+') {
			return new PositiveArgument(str.substring(1));
		}else {
			return new NormalArgument(str);
		}
	}
	
	private static IndexedList<BufferedImage> filterNegative(IndexedList<BufferedImage> l1, String string) {
		System.out.println("filter negative: "+string);
		IndexedList<BufferedImage> l2 = new IndexedList<BufferedImage>();
		
		int i=0;
		while(i<l1.getSize()) {
			if(!stringContains(l1.getId(i), string)) {
				l2.add(l1.getId(i), l1.get(i));
			}
			i++;
		}
		
		return l2;
	}

	private static boolean stringContains(String str1, String str2) {
		int i=0;
		while(i<str1.length() - str2.length() + 1) {
			if(str1.substring(i,i+str2.length()).equalsIgnoreCase(str2)) {
				return true;
			}
			i++;
		}
		return false;
	}
	
	static private abstract class Argument{
		public static int TYPE_NORMAL = 0;
		public static int TYPE_NEGATIVE = 1;
		public static int TYPE_POSITIVE = 2;
		public Argument(String val) {
			this.val = val;
		}
		public String val;
		public String getVal() {
			return val;
		}
		public abstract IndexedList<BufferedImage> filter(IndexedList<BufferedImage> current, IndexedList<BufferedImage> original);
	}
	
	static private class NormalArgument extends Argument{
		public NormalArgument(String val) {
			super(val);
		}
		public IndexedList<BufferedImage> filter(IndexedList<BufferedImage> current,
				IndexedList<BufferedImage> original) {
			if(current == null) {
				return null;
			}
			IndexedList<BufferedImage> newList = new IndexedList<BufferedImage>();
			int i=0;
			while(i<current.getSize()) {
				String id = current.getId(i);
				
				if(stringContains(id, val)) {
					if(!newList.containsId(id)) {
						newList.add(id, current.get(i));
					}
				}
				i++;
			}
			
			return newList;
		}
	}
	
	static private class PositiveArgument extends Argument{
		public PositiveArgument(String val) {
			super(val);
		}
		public IndexedList<BufferedImage> filter(IndexedList<BufferedImage> current,
				IndexedList<BufferedImage> original) {
			if(current == null) {
				return null;
			}
			IndexedList<BufferedImage> newList = new IndexedList<BufferedImage>();
			int i=0;
			while(i<original.getSize()) {
				String id = original.getId(i);
				if(stringContains(id, val)) {
					if(!newList.containsId(id)) {
						newList.add(id, original.get(i));
					}
				}
				
				i++;
			}
			return newList;
		}
	}
	
	static private class NegativeArgument extends Argument{
		public NegativeArgument(String val) {
			super(val);
		}
		public IndexedList<BufferedImage> filter(IndexedList<BufferedImage> current,
				IndexedList<BufferedImage> original) {
			if(current == null) {
				return null;
			}
			IndexedList<BufferedImage> newList = new IndexedList<BufferedImage>();
			int i=0;
			while(i<current.getSize()) {
				String id = current.getId(i);
				
				if(!stringContains(id, val)) {
					if(!newList.containsId(id)) {
						newList.add(id, current.get(i));
					}
				}
				i++;
			}
			
			return newList;
		}
	}
	
}
