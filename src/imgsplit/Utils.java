package imgsplit;

import java.util.ArrayList;

import misc.StringUtils;

public abstract class Utils {
	
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
	
	public static String[] split(String str, char c){
		return split(str, new char[] {c});
	}
	
	public static String fixPath(String path, boolean isFolder) {
		if(path == null)return "";
		int i=0;
		char[] arr = path.toCharArray();
		String str = "";
		while(i<arr.length) {
			if(arr[i] == '\\') {
				arr[i] = '/';
			}
			i++;
		}
		i=0;
		while(i<arr.length) {
			if(arr[i] != '"') {
				str += arr[i];
			}
			i++;
		}
		
		if(arr.length > 0) {
			if(isFolder) {
				if(arr[arr.length-1] != '/')str += '/';
			}
		}
		
		return str;
	}
	
	public static String fancyErrorString(Exception ex, int length) {
		String str = "Unexpected Error:\n      "+ex.getClass().getName()+"\n";
		str += "Message:\n      "+ex.getMessage()+"\n";
		str += " at: \n";
		int i=0;
		while(i<Math.min(length, ex.getStackTrace().length)) {
			str += "      "+ex.getStackTrace()[i]+"\n";
			i++;
		}
		int r = ex.getStackTrace().length - length;
		if(r > 0) {
			str += " "+r+" more...";
		}
		return str;
	}
	
	
}
