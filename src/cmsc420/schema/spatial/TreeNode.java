package cmsc420.schema.spatial;


/**
 * An interface that represents the functionality of a general node of a tree.
 * 
 * @author Andrew Liu
 *
 */
public interface TreeNode<T> {

	/**
	 * Gives the node structure after adding a element.
	 * 
	 * @param element
	 *            the element to add
	 * @return the subtree after the addition
	 */
	public TreeNode<T> add(T element);

	/**
	 * Checks if the subtree with the node as a root contains the given element.
	 * 
	 * @param element
	 *            the element to search for
	 * @return if the subtree contains the element
	 */
	public boolean contains(T element);

	/**
	 * Gives the node structure after removing a element.
	 * 
	 * @param element
	 *            the element to remove
	 * @return the subtree after the removal
	 */
	public TreeNode<T> remove(T element);

}
