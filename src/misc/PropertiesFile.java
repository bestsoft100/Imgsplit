package misc;

import java.util.ArrayList;

public class PropertiesFile extends Properties{
	
	private String path;
	private String extension = "properties";
	
	public PropertiesFile(String path) {
		super();
		String ext = StringUtils.getFilenameExtension(path);
		if(ext != null) {
			path = path.substring(0, path.length() - (ext.length()+1));
		}
		this.path = path;
	}
	
	public String getPath() {
		return path+(extension!=null?'.'+extension:"");
	}
	
	public PropertiesFile load() {
		ArrayList<String> l = null;
		try {
			l = FileUtils.readFile(getPath());
		}catch (Exception e) {
			e.printStackTrace();
			return this;
		}
		
		for(String ln : l) {
			try{
				String[] line = StringUtils.splitAtFirstOccurence(ln, ':');
				set(line[0], line[1]);
			}catch (RuntimeException e) {
				//Invalid Line
				System.out.println("Config-File ["+getPath()+"]: Invalid line: \""+ln+"\"");
			}
		}
		return this;
	}
	
	public PropertiesFile save() {
		ArrayList<String> l = new ArrayList<String>();
		int i=0;
		while(i<getSize()) {
			String id = getId(i);
			if(id == null || id.equals(""))continue;
			String v = getString(id);
			if(v == null || v.equalsIgnoreCase("null"))v = "";
			
			l.add(id+':'+v);
			i++;
		}
		FileUtils.writeFile(path + "." + extension, StringUtils.listToString(l, true));
		System.out.println("Saved Properties: "+getPath());
		return this;
	}
	
	public PropertiesFile setExtension(String ext) {
		this.extension = ext;
		return this;
	}
	
	public String getExtension() {
		return extension;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}