package cmsc420.schema.mediator;

import java.awt.Color;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc132.Graph;
import cmsc420.drawing.CanvasPlus;
import cmsc420.exceptions.CityAlreadyMappedException;
import cmsc420.exceptions.CityDoesNotExistException;
import cmsc420.exceptions.CityNotFoundException;
import cmsc420.exceptions.CityNotMappedException;
import cmsc420.exceptions.CityOutOfBoundsException;
import cmsc420.exceptions.DuplicateCityCoordinatesException;
import cmsc420.exceptions.DuplicateCityNameException;
import cmsc420.exceptions.EmptyTreeException;
import cmsc420.exceptions.EndPointDoesNotExistException;
import cmsc420.exceptions.MapIsEmptyException;
import cmsc420.exceptions.NameNotInDictionaryException;
import cmsc420.exceptions.NoCitiesExistInRangeException;
import cmsc420.exceptions.NoCitiesToListException;
import cmsc420.exceptions.NoOtherCitiesMappedException;
import cmsc420.exceptions.NoPathExistsException;
import cmsc420.exceptions.NoRoadsExistInRangeException;
import cmsc420.exceptions.NonExistentEndException;
import cmsc420.exceptions.NonExistentStartException;
import cmsc420.exceptions.RoadAlreadyMappedException;
import cmsc420.exceptions.RoadIsNotMappedException;
import cmsc420.exceptions.RoadNotFoundException;
import cmsc420.exceptions.RoadOutOfBoundsException;
import cmsc420.exceptions.StartEqualsEndException;
import cmsc420.exceptions.StartOrEndIsIsolatedException;
import cmsc420.exceptions.StartPointDoesNotExistException;
import cmsc420.schema.City;
import cmsc420.schema.CityColor;
import cmsc420.schema.CityDistanceComparator;
import cmsc420.schema.CityNameComparator;
import cmsc420.schema.SortType;
import cmsc420.schema.adjacencylist.AdjacencyList;
import cmsc420.schema.dictionary.AvlGTreeDictionary;
import cmsc420.schema.dictionary.DictionaryStructure;
import cmsc420.schema.spatial.BlackNode;
import cmsc420.schema.spatial.GrayNode;
import cmsc420.schema.spatial.PRQuadTree;
import cmsc420.schema.spatial.Seedling;
import cmsc420.schema.spatial.SpatialStructure;
import cmsc420.schema.spatial.TreeNode;
import cmsc420.schema.spatial.PM.PMBlackNode;
import cmsc420.schema.spatial.PM.PMBlackNode.RoadComparator;
import cmsc420.schema.spatial.PM.PMGrayNode;
import cmsc420.schema.spatial.PM.PMNode;
import cmsc420.schema.spatial.PM.PMQuadTree;
import cmsc420.schema.spatial.PM.PMWhiteNode;
import cmsc420.sortedmap.AvlGTree;
import cmsc420.xml.XmlUtility;

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
	private AdjacencyList<City> adjacencyList;

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
	CommandRunner(DictionaryStructure dict, Seedling seed, AdjacencyList<City> adj, int width, int height) {
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
	 * coordinate), and the coordinates are non-negative integers. Names are
	 * case-sensitive.
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
	 * Resets all of the structures including the PM Quadtree, clearing them.
	 * This has the effect of removing every city and every road. This command
	 * cannot fail, so it should unilaterally produce a <success> element in the
	 * output XML.
	 */
	void clearAll() {
		this.dictionary = (DictionaryStructure) this.dictionary.reset();
		this.spatial = (SpatialStructure) this.spatial.reset();
		if (this.adjacencyList != null) {
			this.adjacencyList.clear();
		}
	}

	/**
	 * Prints all cities currently present in the dictionary. The order in which
	 * the attributes for the <city> tags are listed is unimportant. However,
	 * the city tags themselves must be listed in ascending order either by name
	 * or by coordinate, as per the sortBy attribute in the listCities command,
	 * whose two legal values are name and coordinate. The ordering by name is
	 * asciibetical according to the java.lang.String.compareTo() method, and
	 * the ordering by coordinate is discussed in the Part 1 spec. To reiterate,
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
		if (this.spatial instanceof PRQuadTree
				&& (city.x == this.spatial.getSpatialWidth() || city.y == this.spatial.getSpatialHeight())) {
			throw new CityOutOfBoundsException();
		}
		if (city.x > this.spatial.getSpatialWidth() || city.y > this.spatial.getSpatialHeight() || city.x < 0
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
		names = new ArrayList<>(new LinkedHashSet<>(names));
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
	 * @throws CityNotFoundException
	 */
	City nearestCity(int x, int y) throws MapIsEmptyException, CityNotFoundException {
		/* check for exceptions */
		if (this.spatial.size() == 0) {
			if (this.spatial instanceof PRQuadTree) {
				throw new MapIsEmptyException();
			} else {
				throw new CityNotFoundException();
			}
		}

		/* fill priority queue with cities based off proximity */
		PriorityQueue<City> queue = new PriorityQueue<>(new CityDistanceComparator(x, y));
		if (this.spatial instanceof PRQuadTree) {
			this.fillQueue(queue, ((PRQuadTree) this.spatial).getRoot());
		} else {
			this.fillQueue(queue, ((PMQuadTree) this.spatial).getRoot(), false);
		}
		if (queue.size() == 0) {
			throw new CityNotFoundException();
		}
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

	private void fillQueue(PriorityQueue<City> queue, PMNode node, boolean isolated) {
		if (node instanceof PMBlackNode) {
			City city = ((PMBlackNode) node).getCity();
			// if city isn't null and is isolated, add
			if (city != null && this.adjacencyList.isIsolated(city) == isolated) {
				queue.add(city);
			}

		} else if (node instanceof PMGrayNode) {
			for (PMNode child : ((PMGrayNode) node).getChildren()) {
				this.fillQueue(queue, child, isolated); // add children nodes
			}
		}
	}

	/**
	 * Clears the canvas of the spatial structure.F
	 */
	void close() {
		this.spatial.removeCanvas();
	}

	AvlGTree<String, City> printAvlTree() throws EmptyTreeException {
		AvlGTree<String, City> tree = ((AvlGTreeDictionary) this.dictionary).getPrintingTree();
		if (tree.size() == 0) {
			throw new EmptyTreeException();
		}
		return tree;
	}

	void mapRoad(String start, String end) throws StartPointDoesNotExistException, EndPointDoesNotExistException,
			StartEqualsEndException, StartOrEndIsIsolatedException, RoadAlreadyMappedException,
			RoadOutOfBoundsException {
		if (!this.dictionary.containsName(start)) {
			throw new StartPointDoesNotExistException();
		}
		if (!this.dictionary.containsName(end)) {
			throw new EndPointDoesNotExistException();
		}
		City city1 = this.dictionary.getCity(start);
		City city2 = this.dictionary.getCity(end);
		if (start.equals(end) || city1.equals(city2)) {
			throw new StartEqualsEndException();
		}
		if ((this.spatial.contains(city1) && this.adjacencyList.isIsolated(city1))
				|| (this.spatial.contains(city2) && this.adjacencyList.isIsolated(city2))) {
			throw new StartOrEndIsIsolatedException();
		}
		if (this.adjacencyList.containsUndirectedEdge(city1, city2)) {
			throw new RoadAlreadyMappedException();
		}
		if (!PMGrayNode.roadInQuadrant(new PMWhiteNode(new Point2D.Float(), (int) this.spatial.getSpatialWidth(), (int) this.spatial.getSpatialHeight(), null, null), city1, city2)) {
			throw new RoadOutOfBoundsException();
		}
		// add to adjacency list
		this.adjacencyList.addUndirectedEdge(city1, city2);
		this.spatial.addRoad(city1, city2);
	}

	PMQuadTree printPMQuadtree() throws MapIsEmptyException {
		/* check for exceptions */
		if (this.spatial.size() == 0) {
			throw new MapIsEmptyException();
		} else {
			return (PMQuadTree) this.spatial;
		}
	}

	List<City[]> rangeRoads(int x, int y, int radius, String saveMap) throws NoRoadsExistInRangeException {
		if (radius == 0) {
			throw new NoRoadsExistInRangeException();
		}
		List<City[]> roads = new LinkedList<City[]>();
		for (Object[] road : this.adjacencyList) {
			City start = (City) road[0];
			City end = (City) road[1];
			if (start.getName().compareTo(end.getName()) < 0
					&& Math.sqrt(this.minSqDistanceBetweenCityAndRoad(x, y, start, end)) <= radius) {
				City[] ele = new City[2];
				ele[0] = start;
				ele[1] = end;
				roads.add(ele);
			}
		}
		roads = new ArrayList<>(new LinkedHashSet<>(roads)); // remove dups
		if (roads.size() == 0) {
			throw new NoRoadsExistInRangeException();
		}
		Collections.sort(roads, new RoadComparator());
		if (saveMap != null) {
			this.spatial.addCircle(x, y, radius);
			this.saveMap(saveMap);
			this.spatial.removeCircle(x, y, radius);
		}
		return roads;
	}

	private double minSqDistanceBetweenCityAndRoad(int x, int y, City start, City end) {
		double t = dot(x - start.x, y - start.y, end.x - start.x, end.y - start.y)
				/ sqDist(start.x, start.y, end.x, end.y);
		if (t <= 0) {
			return sqDist(x, y, start.x, start.y);
		}
		if (t >= 1) {
			return sqDist(x, y, end.x, end.y);
		}
		return sqDist(x, y, start.x + t * (end.x - start.x), start.y + t * (end.y - start.y));
	}

	private static double dot(double x1, double y1, double x2, double y2) {
		return x1 * x2 + y1 * y2; // dot product
	}

	private static double sqDist(double x1, double y1, double x2, double y2) {
		return (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
	}

	City nearestIsolatedCity(int x, int y) throws CityNotFoundException {
		/* check for exceptions */
		if (this.spatial.size() == 0) {
			throw new CityNotFoundException();
		}

		/* fill priority queue with cities based off proximity */
		PriorityQueue<City> queue = new PriorityQueue<>(new CityDistanceComparator(x, y));
		this.fillQueue(queue, ((PMQuadTree) this.spatial).getRoot(), true);
		if (queue.size() == 0) {
			throw new CityNotFoundException();
		}
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

	City[] nearestRoad(int x, int y) throws RoadNotFoundException {
		if (this.adjacencyList.size() == 0) {
			throw new RoadNotFoundException();
		}
		double smallestDistance = -1;
		City[] nearestRoad = null;
		for (Object[] road : this.adjacencyList) {
			City start = (City) road[0];
			City end = (City) road[1];
			if (start.getName().compareTo(end.getName()) < 0) {
				// squared distance for all distances should not break anything
				double dist = this.minSqDistanceBetweenCityAndRoad(x, y, start, end);
				City[] ele = new City[2];
				ele[0] = start;
				ele[1] = end;
				if (nearestRoad == null || dist < smallestDistance
						|| (dist <= smallestDistance && new RoadComparator().compare(ele, nearestRoad) < 0)) {
					nearestRoad = ele;
					smallestDistance = dist;
				}
			}
		}
		return nearestRoad;
	}

	City nearestCityToRoad(String start, String end) throws RoadIsNotMappedException, NoOtherCitiesMappedException {
		if (!this.adjacencyList.containsUndirectedEdge(this.dictionary.getCity(start), this.dictionary.getCity(end))) {
			throw new RoadIsNotMappedException();
		}

		/* get the set of all the cities */
		Set<City> cities = this.spatial.getCities();

		double minDist = -1;
		City nearest = null;
		City startCity = this.dictionary.getCity(start);
		City endCity = this.dictionary.getCity(end);

		for (City city : cities) {
			if (city.equals(startCity) || city.equals(endCity)) {
				continue;
			}
			// squared distance for all distances should not break anything
			double dist = this.minSqDistanceBetweenCityAndRoad((int) city.x, (int) city.y, startCity, endCity);
			if (nearest == null || dist < minDist
					|| (dist <= minDist && city.getName().compareTo(nearest.getName()) < 0)) {
				nearest = city;
				minDist = dist;
			}
		}
		if (nearest == null) {
			throw new NoOtherCitiesMappedException();
		}
		return nearest;
	}

	Element shortestPath(String start, String end, String saveMap, String saveHTML, Document doc, CommandWriter writer,
			String id) throws NonExistentStartException, NonExistentEndException, NoPathExistsException {
		City startCity = this.dictionary.getCity(start);
		if (startCity == null || !this.spatial.contains(startCity)) {
			throw new NonExistentStartException();
		}
		City endCity = this.dictionary.getCity(end);
		if (endCity == null || !this.spatial.contains(endCity)) {
			throw new NonExistentEndException();
		}
		Graph<City> dijkstra = new Graph<>();
		try {
			dijkstra.addVertex(start, startCity);
		} catch (IllegalArgumentException e) {
		}
		try {
			dijkstra.addVertex(end, endCity);
		} catch (IllegalArgumentException e) {
		}
		for (Object[] road : this.adjacencyList) {
			City city1 = (City) road[0];
			City city2 = (City) road[1];
			try {
				dijkstra.addVertex(city1.getName(), city1);
			} catch (IllegalArgumentException e) {
			}
			try {
				dijkstra.addVertex(city2.getName(), city2);
			} catch (IllegalArgumentException e) {
			}
			dijkstra.addDirectedEdge(city1.getName(), city2.getName(),
					Math.sqrt(sqDist(city1.x, city1.y, city2.x, city2.y)));
		}
		ArrayList<String> path = new ArrayList<String>();
		double length = dijkstra.doDijkstras(start, end, path);
		if (length < 0) {
			throw new NoPathExistsException();
		}
		int hops = path.size() - 1;
		Element ret = doc.createElement("path");
		ret.setAttribute("hops", "" + hops);
		DecimalFormat df = new DecimalFormat("0.000");
		String formattedLen = df.format(length);
		ret.setAttribute("length", formattedLen);
		String startRoad = null;
		String midPoint = null;
		String endRoad = null;
		Iterator<String> iter = path.iterator();
		do {
			// if start + mid aren't null add the road
			if (startRoad != null && midPoint != null) {
				Element road = doc.createElement("road");
				road.setAttribute("start", startRoad);
				road.setAttribute("end", midPoint);
				ret.appendChild(road);
				// if non are null add the direction
				if (endRoad != null) {
					Arc2D.Double arc = new Arc2D.Double();
					arc.setArcByTangent(this.dictionary.getCity(endRoad), this.dictionary.getCity(midPoint),
							this.dictionary.getCity(startRoad), 1);
					double angle = arc.getAngleExtent();

					if (angle > 45) {
						Element dir = doc.createElement("left");
						ret.appendChild(dir);
					} else if (angle <= -45) {
						Element dir = doc.createElement("right");
						ret.appendChild(dir);
					} else {
						Element dir = doc.createElement("straight");
						ret.appendChild(dir);
					}
				}
			}
			// shift
			startRoad = midPoint;
			midPoint = endRoad;
			endRoad = iter.hasNext() ? iter.next() : null;
		} while (startRoad != null || midPoint != null || endRoad != null);
		String[] paramNames = { "start", "end", "saveMap", "saveHTML" };
		String[] parameters = { start, end, saveMap, saveHTML };
		Element success = writer.shortestPathTag("shortestPath", parameters, paramNames, ret, id);
		if (saveMap != null || saveHTML != null) {
			CanvasPlus canvas = this.shortestPathCanvas(path);
			if (saveMap != null) {
				try {
					canvas.save(saveMap);
				} catch (IOException e) {
				}
			}
			if (saveHTML != null) {
				try {
					org.w3c.dom.Document shortestPathDoc = XmlUtility.getDocumentBuilder().newDocument();
					org.w3c.dom.Node spNode = shortestPathDoc.importNode(success, true);
					shortestPathDoc.appendChild(spNode);
					XmlUtility.transform(shortestPathDoc, new File("shortestPath.xsl"), new File(saveHTML + ".html"));
					canvas.save(saveHTML); // add picture (?)
				} catch (ParserConfigurationException | TransformerException | IOException e) {
				}
			}
			canvas.dispose();
		}
		return success;
	}

	private CanvasPlus shortestPathCanvas(ArrayList<String> cities) {
		CanvasPlus canvas = new CanvasPlus("MeeshQuest", (int) this.spatial.getSpatialWidth(),
				(int) this.spatial.getSpatialHeight());
		canvas.addRectangle(0, 0, this.spatial.getSpatialWidth(), this.spatial.getSpatialHeight(), Color.BLACK, false);
		String prev = null;
		String curr = null;
		Iterator<String> iter = cities.iterator();
		do {
			if (curr != null) {
				City currCity = this.dictionary.getCity(curr);
				// draw city
				Color color = Color.BLUE;
				if (curr.equals(cities.get(0))) {
					color = Color.GREEN;
				} else if (curr.equals(cities.get(cities.size() - 1))) {
					color = Color.RED;
				}
				canvas.addPoint(curr, currCity.x, currCity.y, color);
				if (prev != null) {
					// draw line
					City prevCity = this.dictionary.getCity(prev);
					canvas.addLine(prevCity.x, prevCity.y, currCity.x, currCity.y, Color.BLUE);
				}
			}
			prev = curr;
			curr = iter.hasNext() ? iter.next() : null;
		} while (prev != null || curr != null);
		return canvas;
	}

}
