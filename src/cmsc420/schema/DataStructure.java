package cmsc420.schema;

public interface DataStructure<T> {

	public void add(T element);
	public boolean contains(T element);
	public void remove(T element);
}
