package misc;

public class IndexedObject<Type> {
	
	private String id;
	private Type object;
	
	public IndexedObject(String id, Type object) {
		this.id = id;
		this.object = object;
	}
	
	public String getId() {
		return id;
	}
	
	public Type getObject() {
		return object;
	}
	
}
