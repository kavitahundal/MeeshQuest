package cmsc420.schema.spatial;

import java.awt.geom.Point2D;
import java.util.List;

import cmsc420.schema.Metropole;

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
	public TreeNode add(Metropole metropole);

	/**
	 * Checks if the subtree with the node as a root contains the given city.
	 * 
	 * @param city
	 *            the city to search for
	 * @return if the subtree contains the city
	 */
	public boolean contains(Metropole metropole);

	/**
	 * Gives the node structure after removing a city.
	 * 
	 * @param city
	 *            the city to remove
	 * @return the subtree after the removal
	 */
	public TreeNode remove(Metropole metropole);

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
	public void range(List<Metropole> metropoles, int x, int y, int radius);
	
	public Metropole getMetropole(Point2D.Float loc);
}
