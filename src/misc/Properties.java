package misc;

import java.util.ArrayList;

public class Properties {
	
	private ArrayList<Property> content;
	private ArrayList<IndexedObject<Object>> objects;
	private char seperator;
	
	public Properties() {
		this(':');
	}
	
	public Properties(char seperator) {
		content = new ArrayList<Property>();
		this.seperator = seperator;
	}
	
	public void set(String ln) {
		//TODO test
		String[] line = StringUtils.splitAtFirstOccurence(ln, seperator);
		set(line[0], line[1]);
	}
	
	public void print() {
		for(Property p : content) {
			System.out.println("\""+p.id+"\""+" = "+"\""+p.value+"\"");
		}
	}
	
	public Properties set(String property, String value) {
		
		for(Property p : content) {
			if(p.id.equals(property)) {
				p.value = value;
				return this;
			}
		}
		content.add(new Property(property, value));
		
		return this;
	}
	
	public Properties set(String property, int value) {
		return set(property, ""+value);
	}
	
	private class Property{
		public String id;
		public String value;
		public Property(String id, String value) {
			this.id = id;
			this.value = value;
		}
	}
	
	public String getString(String propertyId) {
		for(Property p : content) {
			if(p.id.equals(propertyId)) {
				return p.value;
			}
		}
		return null;
	}
	
	public int getInteger(String propertyId) {
		try{
			return Integer.parseInt(getString(propertyId));
		}catch (Exception e) {
			return 0;
		}
	}
	
	public boolean getBoolean(String propertyId) {
		String str = getString(propertyId);
		if(str != null && !str.equals("")) {
			if(str.equals("true") || str.equals("1")) {
				return true;
			}else if(str.equals("false") || str.equals("0")) {
				return false;
			}else {
				throw new RuntimeException("Not a boolean value: "+propertyId);
			}
		}
		return false;
	}
	
	public ArrayList<Property> getProperties(){
		return content;
	}
	
	public int getSize() {
		return content.size();
	}

	public String getId(int i) {
		return content.get(i).id;
	}
	
	public boolean isSet(String id) {
		for(Property p : content) {
			if(p.id.equals(id))return true;
		}
		return false;
	}
	
	public void addObject(String id, Object obj) {
		objects.add(new IndexedObject<Object>(id, obj));
	}
	
	public Object getObject(String id) {
		for(IndexedObject<Object> obj : objects) {
			if(obj.getId().equals(id)){
				return obj.getObject();
			}
		}
		return null;
	}
	
	public String getString(int i) {
		return content.get(i).value;
	}
	
	public boolean isInteger(String id) {
		try {
			Integer.parseInt(getString(id));
			return true;
		}catch (Exception e) {
			return false;
		}
	}
	
	public String[] getIds() {
		String[] str = new String[getSize()];
		int i=0;
		while(i<str.length) {
			str[i] = getId(i);
			i++;
		}
		return str;
	}
	
	public String[] getValues() {
		String[] str = new String[getSize()];
		int i=0;
		while(i<str.length) {
			str[i] = getString(i);
			i++;
		}
		return str;
	}
}
