package cmsc420.schema;

/**
 * An interface to represent basic add, find, and removal functionality of data
 * structures.
 * 
 * @author Andrew Liu
 *
 * @param <T>
 *            the type of object that the data structure holds
 */
public interface DataStructure<T> {

	/**
	 * Adds the element into the data structure.
	 * 
	 * @param element
	 *            the element to add
	 */
	public void add(T element);

	/**
	 * Checks of the element exists in the data structure.
	 * 
	 * @param element
	 *            the element to find
	 * @return whether the element exists in the structure
	 */
	public boolean contains(T element);

	/**
	 * Removes the element from the data structure.
	 * 
	 * @param element
	 *            the element to remove
	 */
	public void remove(T element);

	/**
	 * Resets all the data within the data structure.
	 * 
	 * @return The new data structure without any internal data.
	 */
	public DataStructure<T> reset();
}
