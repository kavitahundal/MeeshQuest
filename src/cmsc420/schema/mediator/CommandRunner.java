package cmsc420.schema.mediator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import cmsc420.exceptions.CityAlreadyMappedException;
import cmsc420.exceptions.CityDoesNotExistException;
import cmsc420.exceptions.CityNotMappedException;
import cmsc420.exceptions.CityOutOfBoundsException;
import cmsc420.exceptions.DuplicateCityCoordinatesException;
import cmsc420.exceptions.DuplicateCityNameException;
import cmsc420.exceptions.MapIsEmptyException;
import cmsc420.exceptions.NameNotInDictionaryException;
import cmsc420.exceptions.NoCitiesExistInRangeException;
import cmsc420.exceptions.NoCitiesToListException;
import cmsc420.schema.City;
import cmsc420.schema.CityColor;
import cmsc420.schema.CityDistanceComparator;
import cmsc420.schema.CityNameComparator;
import cmsc420.schema.SortType;
import cmsc420.schema.adjacencylist.AdjacencyListStructure;
import cmsc420.schema.dictionary.DictionaryStructure;
import cmsc420.schema.spatial.BlackNode;
import cmsc420.schema.spatial.GrayNode;
import cmsc420.schema.spatial.PRQuadTree;
import cmsc420.schema.spatial.Seedling;
import cmsc420.schema.spatial.SpatialStructure;
import cmsc420.schema.spatial.TreeNode;

/**
 * This class represents the collection of data structures used to run the
 * commands specified by the XML schema.
 * 
 * @author Andrew Liu
 *
 */
public class CommandRunner {

	private DictionaryStructure dictionary;
	private SpatialStructure spatial;
	private AdjacencyListStructure adjacencyList;

	/**
	 * Constructor.
	 * 
	 * @param dict
	 *            the dictionary structure
	 * @param seed
	 *            the seedling for the spatial structure
	 * @param adj
	 *            the adjacency list
	 * @param width
	 *            the width of the spatial structure
	 * @param height
	 *            the height of the spatial structure
	 */
	CommandRunner(DictionaryStructure dict, Seedling seed, AdjacencyListStructure adj, int width, int height) {
		this.dictionary = dict;
		this.spatial = seed.generate("MeeshQuest", width, height);
		this.adjacencyList = adj;
	}

	/**
	 * Creates a city (a vertex) with the specified name, coordinates, radius,
	 * and color (the last two attributes will be used in later project parts).
	 * A city can be successfully created if its name is unique (i.e., there
	 * isn't already another city with the same name) and its coordinates are
	 * also unique (i.e., there isn't already another city with the same (x, y)
	 * coordinate). Names are case-sensitive.
	 * 
	 * @param name
	 *            the name of the city
	 * @param x
	 *            the x coordinate of the city
	 * @param y
	 *            the y coordinate of the city
	 * @param radius
	 *            the radius of the city
	 * @param color
	 *            the color of the city
	 * @throws DuplicateCityNameException
	 *             an exception if the dictionary already contains the city name
	 * @throws DuplicateCityCoordinatesException
	 *             an exception if the dictionary already contains the city
	 *             coordinates
	 */
	void createCity(String name, int x, int y, int radius, CityColor color) throws DuplicateCityNameException,
			DuplicateCityCoordinatesException {
		City city = new City(name, x, y, color, radius); // create city

		/* check for exceptions */
		if (this.dictionary.containsName(name)) {
			throw new DuplicateCityNameException();
		} else if (this.dictionary.containsCoordinates(city)) {
			throw new DuplicateCityCoordinatesException();
		} else {
			this.dictionary.add(city);
		}
	}

	/**
	 * Removes a city with the specified name from data dictionary and the
	 * adjacency list. The criteria for success here is simply that the city
	 * exists. Note that if the city has been mapped, then it must be removed
	 * from the PR quadtree first, and then deleted.
	 * 
	 * @param name
	 *            the name of the city to delete
	 * @return the deleted city
	 * @throws CityDoesNotExistException
	 *             an exception if the city to delete is not in the dictionary
	 */
	City deleteCity(String name) throws CityDoesNotExistException {
		City ret = null;

		/* check for exceptions */
		if (!this.dictionary.containsName(name)) {
			throw new CityDoesNotExistException();
		}

		/* get city to remove */
		City city = this.dictionary.getCity(name);
		if (this.spatial.contains(city)) {
			try {
				this.unmapCity(name); // unmap city if in spatial
			} catch (NameNotInDictionaryException | CityNotMappedException e) {
			}
			ret = city;
		}
		this.dictionary.remove(city);
		return ret;
	}

	/**
	 * Resets all of the structures including the PR Quadtree, clearing them.
	 * This has the effect of removing every city. This command cannot fail, so
	 * it should unilaterally produce a <success> element in the output XML.
	 */
	void clearAll() {
		this.dictionary = (DictionaryStructure) this.dictionary.reset();
		this.spatial = (SpatialStructure) this.spatial.reset();
		if (this.adjacencyList != null) {
			this.adjacencyList = (AdjacencyListStructure) this.adjacencyList.reset();
		}
	}

	/**
	 * Prints all cities currently present in the dictionary. The order in which
	 * the attributes for the <city> tags are listed is unimportant. However,
	 * the city tags themselves must be listed in ascending order either by name
	 * or by coordinate, as per the sortBy attribute in the listCities command,
	 * whose two legal values are name and coordinate. The ordering by name is
	 * asciibetical according to the java.lang.String.compareTo() method, and
	 * the ordering by coordinate is discussed in the spec. To reiterate,
	 * coordinate ordering is done by comparing x values first; for cities with
	 * the same x value, one city is less than another city if its y value is
	 * less than the other. This command is only successful if there is at least
	 * 1 (1 or more) cities in the dictionary.
	 * 
	 * @param sortBy
	 *            the sorting method
	 * @return the list of cities in the dictionary
	 * @throws NoCitiesToListException
	 *             an exception if there are no cities in the dictionary
	 */
	List<City> listCities(SortType sortBy) throws NoCitiesToListException {
		if (this.dictionary.size() == 0) {
			throw new NoCitiesToListException();
		}
		return this.dictionary.listCities(sortBy);
	}

	/**
	 * Inserts the named city into the spatial map.
	 * 
	 * @param name
	 *            the name of the city to map
	 * @throws NameNotInDictionaryException
	 *             an exception if the name is not in the dictionary
	 * @throws CityAlreadyMappedException
	 *             an exception if the city has already been mapped
	 * @throws CityOutOfBoundsException
	 *             an exception if the city's coordinates are invalid
	 */
	void mapCity(String name) throws NameNotInDictionaryException, CityAlreadyMappedException, CityOutOfBoundsException {
		/* check for exceptions */
		if (!this.dictionary.containsName(name)) {
			throw new NameNotInDictionaryException();
		}

		/* get city to map */
		City city = this.dictionary.getCity(name);

		/* check for exceptions */
		if (this.spatial.contains(city)) {
			throw new CityAlreadyMappedException();
		}
		if (city.x >= this.spatial.getSpatialWidth() || city.y >= this.spatial.getSpatialHeight() || city.x < 0
				|| city.y < 0) {
			throw new CityOutOfBoundsException();
		}
		this.spatial.add(city);
	}

	/**
	 * Removes the named city from the spatial map.
	 * 
	 * @param name
	 *            the city to unmap
	 * @throws NameNotInDictionaryException
	 *             an exception if the city does not exist in the dictionary
	 * @throws CityNotMappedException
	 *             an exception if the city has not been mapped
	 */
	void unmapCity(String name) throws NameNotInDictionaryException, CityNotMappedException {
		/* check for exceptions */
		if (!this.dictionary.containsName(name)) {
			throw new NameNotInDictionaryException();
		}

		/* get city to unmap */
		City city = this.dictionary.getCity(name);

		/* check for exceptions */
		if (!this.spatial.contains(city)) {
			throw new CityNotMappedException();
		} else {
			this.spatial.remove(city);
		}
	}

	/**
	 * Prints the PR quadtree. Since PR quadtrees are deterministic, your XML
	 * should match exactly the primary input/output.
	 * 
	 * @return the PR Quadtree
	 * @throws MapIsEmptyException
	 *             an exception if no cities are mapped
	 */
	PRQuadTree printPRQuadTree() throws MapIsEmptyException {
		/* check for exceptions */
		if (this.spatial.size() == 0) {
			throw new MapIsEmptyException();
		} else {
			return (PRQuadTree) this.spatial;
		}
	}

	/**
	 * Saves the current map to a file. The image file should be saved with the
	 * correct name. It should match our image file: same dimensions, same
	 * cities, same colors, same partitions, etc. How to keep track of your
	 * graphic map is discussed in the previous section. Printing it out is
	 * discussed there too.
	 * 
	 * @param name
	 *            the name of the file to save to
	 */
	void saveMap(String name) {
		this.spatial.saveMap(name);
	}

	/**
	 * Lists all the cities present in the map within a radius of a point x,y in
	 * the map. Cities on the boundary of the circle are included, and x,y are
	 * integer coordinates. That is, only cities that are in the spatial
	 * structure, in this case, the PR quadtree, are relevant to this commmand.
	 * <success> will result from the existence of at least one <city> that
	 * satisfy the range check condition. If none do, then an <error> tag will
	 * be the result. If the radius is 0, no cities will ever exist in the
	 * range, even if there is a city at the range point. It should be noted
	 * that the radius attribute for a city does not factor into this
	 * calculation; all cities are considered points. If the saveMap attribute
	 * is present, the current map will be saved to an image file (see saveMap).
	 * The image file should be saved with the correct name. It should match our
	 * image file: same dimensions, same cities, etc. How to keep track of your
	 * graphic map is discussed in saveMap. Printing it out is discussed there
	 * too. The main difference with saveMap is that the image file should have
	 * a blue unfilled circle centered at the (x,y) values passed in with the
	 * radius passed in. Because CanvasPlus does not behave well when shapes
	 * exceed the bounds of the spatial map, the saveMap attribute will only be
	 * present when an entire range circle lies inclusively within the bounds of
	 * the spatial map.
	 * 
	 * @param x
	 *            the x coordinate of the center of the search area
	 * @param y
	 *            the y coordinate of the center of the search area
	 * @param radius
	 *            the radius of the search area
	 * @param saveMap
	 *            the name of the file to save to (null if there is no saving)
	 * @return the list of cities with in the search area
	 * @throws NoCitiesExistInRangeException
	 *             an exception if there are no cities within the search area
	 */
	List<City> rangeCities(int x, int y, int radius, String saveMap) throws NoCitiesExistInRangeException {
		/* check for exceptions */
		if (radius == 0) {
			throw new NoCitiesExistInRangeException();
		}

		/* get cities within range */
		List<String> names = this.spatial.range(x, y, radius);
		Collections.sort(names, new CityNameComparator());

		/* check for exceptions */
		if (names.size() == 0) {
			throw new NoCitiesExistInRangeException();
		} else {
			if (saveMap != null) { // saving map
				this.spatial.addCircle(x, y, radius);
				this.saveMap(saveMap);
				this.spatial.removeCircle(x, y, radius);
			}

			/* converting list of names to list of cities */
			List<City> cities = new LinkedList<>();
			for (String name : names) {
				cities.add(this.dictionary.getCity(name));
			}
			return cities;
		}
	}

	/**
	 * Will return the name and location of the closest city to the specified
	 * point in space. To do this correctly, you may want to use an algorithm
	 * using a PriorityQueue, such as
	 * www.cs.umd.edu/users/meesh/cmsc420/Notes/neighbornotes/incnear.pdf or
	 * www.cs.umd.edu/users/meesh/cmsc420/Notes/neighbornotes/incnear2.pdf -
	 * otherwise, you might not be fast enough.
	 * 
	 * @param x
	 *            the x coordinate of the search point
	 * @param y
	 *            the y coordinate of the search point
	 * @return the nearest city (or the lexicographical first of the nearest
	 *         cities)
	 * @throws MapIsEmptyException
	 *             an exception if there are no cities mapped
	 */
	City nearestCity(int x, int y) throws MapIsEmptyException {
		/* check for exceptions */
		if (this.spatial.size() == 0) {
			throw new MapIsEmptyException();
		}

		/* fill priority queue with cities based off proximity */
		PriorityQueue<City> queue = new PriorityQueue<>(new CityDistanceComparator(x, y));
		this.fillQueue(queue, ((PRQuadTree) this.spatial).getRoot());
		City nearest = queue.poll();

		/* getting lexicographically first city in case of ties */
		while (queue.peek() != null && queue.peek().distance(x, y) <= nearest.distance(x, y)) {
			City temp = queue.poll();
			if (temp.getName().compareTo(nearest.getName()) < 0) {
				nearest = temp;
			}
		}
		return nearest;
	}

	private void fillQueue(PriorityQueue<City> queue, TreeNode node) {
		if (node instanceof BlackNode) {
			queue.add(((BlackNode) node).getCity()); // add city
		} else if (node instanceof GrayNode) {
			for (TreeNode child : ((GrayNode) node).getChildren()) {
				this.fillQueue(queue, child); // add children nodes
			}
		}
	}

	/**
	 * Clears the canvas of the spatial structure.F
	 */
	void close() {
		this.spatial.removeCanvas();
	}

}
