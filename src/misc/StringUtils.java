package misc;

import java.util.ArrayList;

public abstract class StringUtils {
	
	public static String replaceAll(String str, char toReplace, String replaceWith) {
		char[] arr = str.toCharArray();
		String out = "";
		int i=0;
		while(i<arr.length) {
			if(arr[i] == toReplace)out += replaceWith;
			else out += arr[i];
			
			i++;
		}
		return out;
	}
	
	public static String replaceAll(String str, char toReplace, char replaceWith) {
		return replaceAll(str, toReplace, ""+replaceWith);
	}
	
//	public static ArrayList<String> split(String str, char c){
//		ArrayList<String> list = new ArrayList<String>();
//		
//		char[] arr = str.toCharArray();
//		int i=0;
//		String ln = "";
//		while(i<arr.length) {
//			if(arr[i] == c) {
//				if(!ln.equals(""))list.add(ln);
//				ln = "";
//			}else {
//				ln += arr[i];
//			}
//			i++;
//		}
//		if(arr[arr.length-1] != c && !ln.equals("")) {
//			list.add(ln);
//		}
//		
//		return list;
//	}
	
	public static String[] split(String str, char c) {
		return split(str, new char[] {c});
	}
	
	public static String[] split(String str, char[] chars) {
		ArrayList<String> list = new ArrayList<String>();
		char[] arr = str.toCharArray();
		
		String ln = "";
		int i=0;
		while(i<arr.length) {
			boolean b = false;
			int j=0;
			while(j<chars.length) {
				if(arr[i] == chars[j]) {
					list.add(ln);
					ln = "";
					b = true;
				}
				if(!b) {
					ln += arr[i];
				}
				j++;
			}
			i++;
		}
		list.add(ln);
		
		String[] arr2 = new String[list.size()];
		i=0;
		while(i<list.size()) {
			arr2[i] = list.get(i);
			i++;
		}
		return arr2;
	}
	
//	public static ArrayList<String> splitAtFirstOccurence(String str, char c){
//		ArrayList<String> list = new ArrayList<String>();
//		
//		char[] arr = str.toCharArray();
//		int i=0;
//		while(i<arr.length) {
//			if(arr[i] == c) {
//				break;
//			}
//			i++;
//		}
//		if(i >= arr.length) {
//			throw new RuntimeException("No Separator");
//		}
//
//		list.add(str.substring(0,i));
//		list.add(str.substring(i+1));
//		
//		return list;
//	}
	
	public static String[] splitAtFirstOccurence(String str, char c) {
		char[] arr = str.toCharArray();
		String[] out = new String[] {"",""};
		boolean sep = false;
		
		int i=0;
		while(i<arr.length) {
			if(arr[i] == c && !sep) {
				sep = true;
			}else {
				if(!sep) {
					out[0] += arr[i];
				}else {
					out[1] += arr[i];
				}
			}
			i++;
		}
		
		return out;
	}
	
	public static void print(ArrayList<String> list) {
		for(String ln : list) {
			System.out.println(ln);
		}
	}
	
	public static String getFilenameExtension(String filename) {
		if(filename == null)return null;
		int i=0;
		int index = -1;
		char[] arr = filename.toCharArray();
		while(i<filename.length()) {
			if(arr[i] == '.') {
				index = i;
			}
			if(arr[i] == '/' || arr[i] == '\\')index = -1;
			i++;
		}
		if(index == -1)return null;
		return filename.substring(index+1);
	}
	
	public static String removeFilenameExtension(String filename) {
		String ext = getFilenameExtension(filename);
		if(ext == null)return filename;
		return filename.substring(0, filename.length() - ext.length() - 1);
	}
	
	public static String listToString(ArrayList<String> list, boolean insertLineBreaks) {
		String str = "";
		for(String ln : list) {
			str += ln + (insertLineBreaks?"\n":"");
		}
		return str;
	}
	
	public static String toString(String[] arr, char lineBreak) {
		String str = "";
		int i=0;
		while(i<arr.length) {
			String ln = arr[i] + (""+lineBreak);
			str += ln;
			i++;
		}
		return str;
	}

	public static String[] toArray(ArrayList<String> names) {
		String[] arr = new String[names.size()];
		int i=0;
		while(i<names.size()) {
			arr[i] = names.get(i);
			i++;
		}
		
		return arr;
	}
	
	public static ArrayList<String> filter(ArrayList<String> list, String filter){
		ArrayList<String> newList = new ArrayList<String>();
		int maxLength = 0;
		for(String str : list) {
			if(str.length() > maxLength)maxLength = str.length();
		}
		int i=0;
		while(i<maxLength) {
			int j=0;
			while(j<list.size()) {
				if(isInString(list.get(j), filter, i)) {
					if(!newList.contains(list.get(j)))newList.add(list.get(j));
				}
				j++;
			}
			i++;
		}
		return newList;
	}
	
	public static boolean isInString(String str1, String str2, int i) {
		try{
			str1 = str1.substring(i, i+str2.length());
			
			boolean b = str1.equals(str2);
			return b;
		}catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isLastChar(String str, char c) {
		return str.lastIndexOf(c) == str.length()-1;
	}
	
	public static boolean isEmpty(String str) {
		if(str == null)return true;
		char[] arr = str.toCharArray();
		if(str.length() == 0)return true;
		int i=0;
		while(i<arr.length) {
			if(arr[i] != ' ') {
				return false;
			}
			i++;
		}
		return true;
	}
	
}
