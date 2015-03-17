package cmsc420.schema.spatial;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.schema.City;
import cmsc420.schema.DataStructure;

/**
 * An interface to represent the structure and behavior of spatial structures.
 * 
 * @author Andrew Liu
 *
 */
public interface SpatialStructure extends DataStructure<City> {

	/**
	 * Gets the number of cities stored within the spatial structure.
	 * 
	 * @return the size of the structure
	 */
	public int size();

	/**
	 * Gets the width of the spatial structure.
	 * 
	 * @return the width of the structure
	 */
	public float getSpatialWidth();

	/**
	 * Gets the height of the spatial structure.
	 * 
	 * @return the height of the structure
	 */
	public float getSpatialHeight();

	/**
	 * Gets a list of the names of the cities (sorted in asciibetical order)
	 * that are within the radius of the given coordinates.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param radius
	 *            the search radius
	 * @return a list of the names of cities that were within the radius
	 */
	public List<String> range(int x, int y, int radius);

	/**
	 * Saves a picture of the canvas representation of the spatial structure
	 * using the name provided.
	 * 
	 * @param name
	 *            the name of the picture to save
	 */
	public void saveMap(String name);

	/**
	 * Draws a circle on the canvas of the structure with the given properties.
	 * 
	 * @param x
	 *            the x coordinate of the center
	 * @param y
	 *            the y coordinate of the center
	 * @param radius
	 *            the radius of the circle
	 */
	public void addCircle(int x, int y, int radius);

	/**
	 * Removes a circle from the canvas of the structure with the given
	 * properties.
	 * 
	 * @param x
	 *            the x coordinate of the center
	 * @param y
	 *            the y coordinate of the center
	 * @param radius
	 *            the radius of the circle
	 */
	public void removeCircle(int x, int y, int radius);

	/**
	 * Destroys the canvas within the spatial structure.
	 */
	public void removeCanvas();

	public void addRoad(City city1, City city2);

	/**
	 * Gets the name of this spatial structure.
	 * 
	 * @return the spatial structure name
	 */
	public String getName();
	
	/**
	 * Returns the tree as an XML element.
	 * 
	 * @param doc
	 *            the XML document to generate elements
	 * @return the as an XML element
	 */
	public Element elementize(Document doc);
}
