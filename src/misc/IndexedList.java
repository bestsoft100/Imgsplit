package misc;

import java.util.ArrayList;

public class IndexedList<Type> {
	
	private ArrayList<IndexedObject<Type>> list = new ArrayList<IndexedObject<Type>>();
	
	public IndexedList() {
		
	}
	
	public void add(String id, Type object) {
		list.add(new IndexedObject<Type>(id, object));
	}
	
	public Type get(String id) {
		for(IndexedObject<Type> obj : list) {
			if(obj.getId().equals(id)) {
				return obj.getObject();
			}
		}
		return null;
	}
	
	public int getIndexOfId(String id) {
		int i=0;
		while(i<list.size()) {
			if(list.get(i).getId().equals(id))return i;
			i++;
		}
		return -1;
	}
	
	public int getIndexOfObject(Type object) {
		int i=0;
		while(i<list.size()) {
			if(list.get(i).getObject() == object)return i;
			i++;
		}
		return -1;
	}
	
	public String getIdOfObject(Type object) {
		for(IndexedObject<Type> obj : list) {
			if(obj.getObject() == object)return obj.getId();
		}
		return null;
	}
	
	public int getSize() {
		return list.size();
	}
	
	public void removeAll() {
		list.clear();
	}
	
	public void remove(String id) {
		for(IndexedObject<Type> obj : list) {
			if(obj.getId().equals(id)) {
				list.remove(obj);
			}
		}
	}
	
	public void printIds() {
		int i=0;
		while(i<list.size()) {
			System.out.println("\""+list.get(i).getId()+"\"");
			i++;
		}
	}
	
	public Type get(int id) {
		return list.get(id).getObject();
	}
	
	public Type getWithoutException(int id) {
		try{
			return list.get(id).getObject();
		}catch (Exception e) {
			return null;
		}
	}

	public String getId(int i) {
		return list.get(i).getId();
	}
	
	public IndexedObject<Type> getIndexedObject(int i){
		return list.get(i);
	}
	
	public IndexedObject<Type> getIndexedObjectWithId(String id){
		for(IndexedObject<Type> obj : list) {
			if(obj.getId().equals(id))return obj;
		}
		return null;
	}
	
	public IndexedObject<Type> getIndexedObject(Type object){
		for(IndexedObject<Type> obj : list) {
			if(obj.getObject().equals(object))return obj;
		}
		return null;
	}
	
	public void addIndexedObject(IndexedObject<Type> indexedObject) {
		list.add(indexedObject);
	}
	
	public ArrayList<String> getAllIds(){
		ArrayList<String> ids = new ArrayList<String>();
		
		for(IndexedObject<Type> obj : list) {
			ids.add(obj.getId());
		}
		
		return ids;
	}

	public void addAllExisting(IndexedList<Type> img2, ArrayList<String> ids) {
		for(String s : ids) {
			this.add(s, img2.get(s));
		}
	}
	
	public String[] getIdArray() {
		String[] arr=  new String[list.size()];
		
		int i=0;
		while(i<arr.length) {
			arr[i] = list.get(i).getId();
			i++;
		}
		
		return arr;
	}
	
	public boolean containsId(String id) {
		int i=0;
		while(i<list.size()) {
			if(list.get(i).getId().equals(id)) {
				return true;
			}
			i++;
		}
		return false;
	}
	
	public void clear() {
		list.clear();
	}
	
	
	
	
}
