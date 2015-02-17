package cmsc420.schema.spatial;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.schema.City;

/**
 * An interface that represents the functionality of a general node of a tree.
 * 
 * @author Andrew Liu
 *
 */
public interface TreeNode {

	/**
	 * Gives the node structure after adding a city.
	 * 
	 * @param city
	 *            the city to add
	 * @return the subtree after the addition
	 */
	public TreeNode add(City city);

	/**
	 * Checks if the subtree with the node as a root contains the given city.
	 * 
	 * @param city
	 *            the city to search for
	 * @return if the subtree contains the city
	 */
	public boolean contains(City city);

	/**
	 * Gives the node structure after removing a city.
	 * 
	 * @param city
	 *            the city to remove
	 * @return the subtree after the removal
	 */
	public TreeNode remove(City city);

	/**
	 * Gives the subtree rooted at the node as an XML element
	 * 
	 * @param doc
	 *            the document to generate XML elements
	 * @return the XML representation of the subtree
	 */
	public Element elementize(Document doc);

	/**
	 * Finds a list of cities within the given coordinate's radius that are part
	 * of the subtree rooted at this node.
	 * 
	 * @param cities
	 *            the list of cities within the search area
	 * @param x
	 *            the x coordinate of the circle's center
	 * @param y
	 *            the y coordinate of the circle's center
	 * @param radius
	 *            the radius of the circle
	 */
	public void range(List<String> cities, int x, int y, int radius);
}
