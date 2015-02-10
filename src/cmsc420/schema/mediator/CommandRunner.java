package cmsc420.schema.mediator;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cmsc420.drawing.CanvasPlus;
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
import cmsc420.schema.SortType;
import cmsc420.schema.adjacencylist.AdjacencyListStructure;
import cmsc420.schema.dictionary.DictionaryStructure;
import cmsc420.schema.spatial.PRQuadTree;
import cmsc420.schema.spatial.Seedling;
import cmsc420.schema.spatial.SpatialStructure;

/**
 * @author Andrew
 *
 */
public class CommandRunner {

	private DictionaryStructure dictionary;
	private SpatialStructure spatial;
	private AdjacencyListStructure adjacencyList;
	private CanvasPlus canvas;

	CommandRunner(DictionaryStructure dict, Seedling seed, AdjacencyListStructure adj, int width, int height) {
		this.canvas = new CanvasPlus("MeeshQuest", (int) this.spatial.getSpatialWidth(),
				(int) this.spatial.getSpatialHeight());
		this.dictionary = dict;
		this.spatial = seed.generate(width, height, this.canvas);
		this.adjacencyList = adj;
		this.canvas.addRectangle(0, 0, (int) this.spatial.getSpatialWidth(), (int) this.spatial.getSpatialHeight(),
				Color.BLACK, false);
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
	 * @param x
	 * @param y
	 * @param radius
	 * @param color
	 * @throws DuplicateCityNameException
	 * @throws DuplicateCityCoordinatesException
	 */
	void createCity(String name, int x, int y, int radius, CityColor color) throws DuplicateCityNameException,
			DuplicateCityCoordinatesException {
//		System.out.println("create city: " + name + " " + x + " " + y + " " + radius + " " + color.toString());
		City city = new City(name, x, y, color, radius);
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
	 * @return
	 * @throws CityDoesNotExistException
	 */
	City deleteCity(String name) throws CityDoesNotExistException {
//		System.out.println("deleteCity: " + name);
		if (!this.dictionary.containsName(name)) {
			throw new CityDoesNotExistException();
		}
		City city = this.dictionary.getCity(name);
		this.dictionary.remove(city);
		if (this.spatial.contains(city)) {
			try {
				this.unmapCity(name);
			} catch (NameNotInDictionaryException | CityNotMappedException e) {
			}
			return city;
		} else {
			return null;
		}
	}

	/**
	 * Resets all of the structures including the PR Quadtree, clearing them.
	 * This has the effect of removing every city. This command cannot fail, so
	 * it should unilaterally produce a <success> element in the output XML.
	 */
	void clearAll() {
//		System.out.println("clearAll");
		this.dictionary = (DictionaryStructure) this.dictionary.reset();
		this.spatial = (SpatialStructure) this.spatial.reset();
		if (this.adjacencyList != null) {
			this.adjacencyList = (AdjacencyListStructure) this.adjacencyList.reset();
		}
		this.canvas = new CanvasPlus("MeeshQuest", (int) this.spatial.getSpatialWidth(),
				(int) this.spatial.getSpatialHeight());
		this.canvas.addRectangle(0, 0, (int) this.spatial.getSpatialWidth(), (int) this.spatial.getSpatialHeight(),
				Color.BLACK, false);
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
	 * @return
	 * @throws NoCitiesToListException
	 */
	List<City> listCities(SortType sortBy) throws NoCitiesToListException {
//		System.out.println("listCities: " + sortBy.toString());
		if (this.dictionary.size() == 0) {
			throw new NoCitiesToListException();
		}
		return this.dictionary.listCities(sortBy);
	}

	/**
	 * Inserts the named city into the spatial map.
	 * 
	 * @param name
	 * @throws NameNotInDictionaryException
	 * @throws CityAlreadyMappedException
	 * @throws CityOutOfBoundsException
	 */
	void mapCity(String name) throws NameNotInDictionaryException, CityAlreadyMappedException, CityOutOfBoundsException {
//		System.out.println("mapCity: " + name);
		if (!this.dictionary.containsName(name)) {
			throw new NameNotInDictionaryException();
		}
		City city = this.dictionary.getCity(name);
		if (this.spatial.contains(city)) {
			throw new CityAlreadyMappedException();
		}
		if (city.x > this.spatial.getSpatialWidth() || city.y > this.spatial.getSpatialHeight()) {
			throw new CityOutOfBoundsException();
		}
		this.spatial.add(city);
		this.canvas.addPoint(city.getName(), city.x, city.y, Color.BLACK);
		// TODO need to draw graynode quadrants
	}

	/**
	 * Removes the named city from the spatial map.
	 * 
	 * @param name
	 * @throws NameNotInDictionaryException
	 * @throws CityNotMappedException
	 */
	void unmapCity(String name) throws NameNotInDictionaryException, CityNotMappedException {
//		System.out.println("unmapCity: " + name);
		if (!this.dictionary.containsName(name)) {
			throw new NameNotInDictionaryException();
		}
		City city = this.dictionary.getCity(name);
		if (!this.spatial.contains(city)) {
			throw new CityNotMappedException();
		} else {
			this.spatial.remove(city);
			this.canvas.removePoint(city.getName(), city.x, city.y, Color.BLACK);
			// TODO remove quadrant lines
		}
	}

	/**
	 * Prints the PR quadtree. Since PR quadtrees are deterministic, your XML
	 * should match exactly the primary input/output.
	 * 
	 * @return
	 * @throws MapIsEmptyException
	 */
	PRQuadTree printPRQuadTree() throws MapIsEmptyException {
//		System.out.println("printPRQuadTree");
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
	 */
	void saveMap(String name) {
//		System.out.println("saveMap");
		try {
			this.canvas.save(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	 * @param y
	 * @param radius
	 * @param saveMap
	 * @return
	 * @throws NoCitiesExistInRangeException
	 */
	List<City> rangeCities(int x, int y, int radius, String saveMap) throws NoCitiesExistInRangeException {
		boolean t = true;
		if (t) {
			throw new NoCitiesExistInRangeException();
		}
//		System.out.println("rangeCities: " + x + " " + y + " " + radius + " " + saveMap);
		if (radius == 0) {
			throw new NoCitiesExistInRangeException();
		}
		List<City> cities = new ArrayList<>();
		// TODO implement
		if (cities.size() == 0) {
			throw new NoCitiesExistInRangeException();
		} else {
			if (saveMap != null) {
				// assuming we only add the circle for this step (and then remove)
				this.canvas.addCircle(x, y, radius, Color.BLUE, false);
				this.saveMap(saveMap);
				this.canvas.removeCircle(x, y, radius, Color.BLUE, false);
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
	 * @param y
	 * @return
	 * @throws MapIsEmptyException
	 */
	City nearestCity(int x, int y) throws MapIsEmptyException {
		boolean t = true;
		if (t) {
			throw new MapIsEmptyException();
		}
//		System.out.println("nearestCity: " + x + " " + y);
		// ASSUMING CITY MUST BE IN THE SPATIAL
		if (this.spatial.size() == 0) {
			throw new MapIsEmptyException();
		}
		// TODO get city
		return null;
	}

	void close() {
		this.canvas.dispose();
	}

}
