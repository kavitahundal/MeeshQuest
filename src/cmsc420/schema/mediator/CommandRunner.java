package cmsc420.schema.mediator;

import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc132.Graph;
import cmsc420.exceptions.AirportDoesNotExistException;
import cmsc420.exceptions.AirportNotFoundException;
import cmsc420.exceptions.AirportOutOfBoundsException;
import cmsc420.exceptions.AirportViolatesPMRulesException;
import cmsc420.exceptions.CityDoesNotExistException;
import cmsc420.exceptions.CityNotFoundException;
import cmsc420.exceptions.DuplicateAirportCoordinatesException;
import cmsc420.exceptions.DuplicateAirportNameException;
import cmsc420.exceptions.DuplicateCityCoordinatesException;
import cmsc420.exceptions.DuplicateCityNameException;
import cmsc420.exceptions.EmptyTreeException;
import cmsc420.exceptions.EndPointDoesNotExistException;
import cmsc420.exceptions.MapIsEmptyException;
import cmsc420.exceptions.MetropoleIsEmptyException;
import cmsc420.exceptions.MetropoleOutOfBoundsException;
import cmsc420.exceptions.NoCitiesExistInRangeException;
import cmsc420.exceptions.NoCitiesToListException;
import cmsc420.exceptions.NoPathExistsException;
import cmsc420.exceptions.NonExistentEndException;
import cmsc420.exceptions.NonExistentStartException;
import cmsc420.exceptions.RoadAlreadyMappedException;
import cmsc420.exceptions.RoadIntersectsAnotherRoadException;
import cmsc420.exceptions.RoadNotInOneMetropoleException;
import cmsc420.exceptions.RoadNotMappedException;
import cmsc420.exceptions.RoadOutOfBoundsException;
import cmsc420.exceptions.RoadViolatesPMRulesException;
import cmsc420.exceptions.StartEqualsEndException;
import cmsc420.exceptions.StartOrEndIsIsolatedException;
import cmsc420.exceptions.StartPointDoesNotExistException;
import cmsc420.schema.Airport;
import cmsc420.schema.City;
import cmsc420.schema.CityColor;
import cmsc420.schema.CityCoordinateComparator;
import cmsc420.schema.CityDistanceComparator;
import cmsc420.schema.Metropole;
import cmsc420.schema.SortType;
import cmsc420.schema.adjacencylist.AdjacencyList;
import cmsc420.schema.dictionary.AirportDictionary;
import cmsc420.schema.dictionary.AvlGTreeDictionary;
import cmsc420.schema.spatial.PRQuadTree;
import cmsc420.schema.spatial.PM.PMBlackNode;
import cmsc420.schema.spatial.PM.PMGrayNode;
import cmsc420.schema.spatial.PM.PMNode;
import cmsc420.schema.spatial.PM.PMQuadTree;
import cmsc420.schema.spatial.PM.PMWhiteNode;
import cmsc420.sortedmap.AvlGTree;

/**
 * This class represents the collection of data structures used to run the
 * commands specified by the XML schema.
 * 
 * @author Andrew Liu
 *
 */
public class CommandRunner {

	private PRQuadTree metropoles;
	AvlGTreeDictionary cityDictionary;
	private AdjacencyList<City> cityAdjList;
	private AirportDictionary airportDictionary;

	private int localWidth;
	private int localHeight;
	private int globalWidth;
	private int globalHeight;
	private int g;
	private int pmOrder;

	CommandRunner(int localWidth, int localHeight, int globalWidth, int globalHeight, int g, int pmOrder) {
		this.localWidth = localWidth;
		this.localHeight = localHeight;
		this.globalWidth = globalWidth;
		this.globalHeight = globalHeight;
		this.g = g;
		this.pmOrder = pmOrder;
		this.clearAll(); // initialize the dictionaries
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
	void createCity(String name, int localX, int localY, int remoteX, int remoteY, int radius, CityColor color)
			throws DuplicateCityNameException, DuplicateCityCoordinatesException {
		City city = new City(name, localX, localY, remoteX, remoteY, color, radius);
		Airport airport = new Airport(name, "", localX, localY, remoteX, remoteY);
		/* check for exceptions */
		if (this.cityDictionary.containsName(name) || this.airportDictionary.containsName(name)) {
			throw new DuplicateCityNameException();
		} else if (this.cityDictionary.contains(city) || this.airportDictionary.containsCoordinates(airport)) {
			throw new DuplicateCityCoordinatesException();
		} else {
			this.cityDictionary.add(city);
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
	List<City[]> deleteCity(String name) throws CityDoesNotExistException {
		/* check for exceptions */
		if (!this.cityDictionary.containsName(name)) {
			throw new CityDoesNotExistException();
		}

		/* get city to remove */
		City city = this.cityDictionary.getCity(name);
		Point2D.Float loc = new Point2D.Float((float) city.remoteX, (float) city.remoteY);
		List<City[]> roadList = new LinkedList<>();
		if (this.metropoles.contains(loc)) {
			// get metropole and pm quadtree
			Metropole metropole= this.metropoles.getMetropole(loc);
			PMQuadTree pm = metropole.getRoads();
			// check adjacencylist for all roads
			// delete each road
			Set<City> neighbors = this.cityAdjList.neighbors(city);
			for (City neighbor : neighbors) {
				// add road to list
				// remove road from pm quadtree
			}

		}
		this.cityDictionary.remove(city);
		return roadList;
	}

	/**
	 * Resets all of the structures including the PM Quadtree, clearing them.
	 * This has the effect of removing every city and every road. This command
	 * cannot fail, so it should unilaterally produce a <success> element in the
	 * output XML.
	 */
	void clearAll() {
		this.metropoles = new PRQuadTree(this.globalWidth, this.globalHeight);
		this.cityDictionary = new AvlGTreeDictionary(g);
		this.cityAdjList = new AdjacencyList<City>(new CityCoordinateComparator());
		this.airportDictionary = new AirportDictionary();
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
		if (this.cityDictionary.size() == 0) {
			throw new NoCitiesToListException();
		}
		return this.cityDictionary.listCities(sortBy);
	}

	AvlGTree<String, City> printAvlTree() throws EmptyTreeException {
		AvlGTree<String, City> tree = this.cityDictionary.getPrintingTree(); 
		if (tree.size() == 0) {
			throw new EmptyTreeException();
		}
		return tree;
	}

	void mapRoad(String start, String end) throws StartPointDoesNotExistException, EndPointDoesNotExistException,
			StartEqualsEndException, StartOrEndIsIsolatedException, RoadAlreadyMappedException,
			RoadOutOfBoundsException, RoadNotInOneMetropoleException, RoadIntersectsAnotherRoadException, RoadViolatesPMRulesException {
		if (!this.cityDictionary.containsName(start)) {
			throw new StartPointDoesNotExistException();
		}
		if (!this.cityDictionary.containsName(end)) {
			throw new EndPointDoesNotExistException();
		}
		City city1 = this.cityDictionary.getCity(start);
		City city2 = this.cityDictionary.getCity(end);
		if (start.equals(end) || city1.equals(city2)) {
			throw new StartEqualsEndException();
		}
		if (city1.remoteX != city2.remoteX || city1.remoteY != city2.remoteY) {
			throw new RoadNotInOneMetropoleException();
		}
		if (!PMGrayNode.roadInQuadrant(new PMWhiteNode(new Point2D.Float(), (int) this.localWidth,
				(int) this.localHeight, null, null), city1, city2)) {
			throw new RoadOutOfBoundsException();
		}
		if (this.cityAdjList.containsUndirectedEdge(city1, city2)) {
			throw new RoadAlreadyMappedException();
		}
		// TODO loop through all neighboring locations to see if there's a city
		// ...or some more appropriate version of validation
		// add to adjacency list
		this.cityAdjList.addUndirectedEdge(city1, city2);
		if (!this.metropoles.contains(new Point2D.Float(city1.remoteX, city1.remoteY))) {
			Metropole metropole = new Metropole(city1.remoteX, city2.remoteY, this.pmOrder, this.localWidth,
					this.localHeight, this.g);
			this.metropoles.add(metropole);
		}
		Metropole metropole = this.metropoles.getMetropole(new Point2D.Float(city1.remoteX, city1.remoteY));
		PMQuadTree quadtree = metropole.getRoads();
		quadtree.addRoad(city1, city2);
	}

	void mapAirport(String name, String airlineName, int localX, int localY, int remoteX, int remoteY)
			throws DuplicateAirportNameException, DuplicateAirportCoordinatesException, AirportOutOfBoundsException,
			AirportViolatesPMRulesException {
		// duplicateAirportName
		// duplicateAirportCoordinates
		// airportOutOfBounds
		// airportViolatesPMRules
		// TODO
		// put airport in a global dictionary (mapping of airline to airport)
		// put airport in a local dictionary
		// BUT we need to validate? (look's like just the coordinate should be not occupied?)
		// TODO PMQUADTREE HOLD AIRPORTS
		City city = new City(name, localX, localY, remoteX, remoteY, null, 0);
		Airport airport = new Airport(name, airlineName, localX, localY, remoteX, remoteY);
		/* check for exceptions */
		if (this.cityDictionary.containsName(name) || this.airportDictionary.containsName(name)) {
			throw new DuplicateAirportNameException();
		} else if (this.cityDictionary.contains(city) || this.airportDictionary.containsCoordinates(airport)) {
			throw new DuplicateAirportCoordinatesException();
		}
		// check if violation or out of bounds here
		this.airportDictionary.add(airport);
		// map it in a local dictionary here
	}

	void unmapRoad(String start, String end) throws StartPointDoesNotExistException, EndPointDoesNotExistException,
			StartEqualsEndException, RoadNotMappedException {
		if (!this.cityDictionary.containsName(start)) {
			throw new StartPointDoesNotExistException();
		}
		if (!this.cityDictionary.containsName(end)) {
			throw new EndPointDoesNotExistException();
		}
		City city1 = this.cityDictionary.getCity(start);
		City city2 = this.cityDictionary.getCity(end);
		if (city1.equals(city2)) {
			throw new StartEqualsEndException();
		}
		Metropole metropole = this.metropoles.getMetropole(new Point2D.Float(city1.remoteX, city1.remoteY));
		AdjacencyList<City> alsoRoadsLOL = metropole.getAdj();
		if (!alsoRoadsLOL.containsUndirectedEdge(city1, city2)) {
			throw new RoadNotMappedException();
		}
		PMQuadTree roads = metropole.getRoads();
		// TODO actually remove that road....
	}

	void unmapAirport(String name) throws AirportDoesNotExistException {
		if (this.airportDictionary.containsName(name)) {
			throw new AirportDoesNotExistException();
		}
		Airport airport = this.airportDictionary.get(name);
		this.airportDictionary.remove(airport);
		Metropole metropole = this.metropoles.getMetorpole(airport.remoteX, airport.remoteY);
		metropole.getAirports().remove(airport);
	}

	PMQuadTree printPMQuadtree(int remoteX, int remoteY) throws MetropoleOutOfBoundsException,
			MetropoleIsEmptyException {
		/* check for exceptions */
		if (remoteX >= this.globalWidth || remoteY >= this.globalHeight) {
			throw new MetropoleOutOfBoundsException();
		}
		Metropole metropole = this.metropoles.getMetropole(new Point2D.Float(remoteX, remoteY));
		if (metropole == null) {
			throw new MetropoleIsEmptyException();
		}
		PMQuadTree roads = metropole.getRoads();
		if (roads.size() == 0) {
			throw new MetropoleIsEmptyException();
		} else {
			return roads;
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
	void saveMap(int remoteX, int remoteY, String name) throws MetropoleOutOfBoundsException, MetropoleIsEmptyException {
		if (remoteX >= this.globalWidth || remoteY >= this.globalHeight) {
			throw new MetropoleOutOfBoundsException();
		}
		Metropole metropole = this.metropoles.getMetropole(new Point2D.Float(remoteX, remoteY));
		if (metropole == null) {
			throw new MetropoleIsEmptyException();
		}
		PMQuadTree roads = metropole.getRoads();
		if (roads.size() == 0) {
			throw new MetropoleIsEmptyException();
		}
		roads.saveMap(name);
	}

	List<City> globalRangeCities(int remoteX, int remoteY, int radius) throws NoCitiesExistInRangeException {
		try {
			List<City> cities = this.listCities(SortType.name);
			List<City> filter = new LinkedList<City>();
			Point2D.Float center = new Point2D.Float(remoteX, remoteY);
			for (City city : cities) {
				if (center.distance(city.remoteX, city.remoteY) <= radius) {
					filter.add(city);
				}
			}
			return filter;
		} catch (NoCitiesToListException e) {
			throw new NoCitiesExistInRangeException();
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
	City nearestCity(int localX, int localY, int remoteX, int remoteY) throws CityNotFoundException {
		if (remoteX >= this.globalWidth || remoteY >= this.globalHeight) {
			throw new CityNotFoundException();
		}
		Metropole metropole = this.metropoles.getMetropole(new Point2D.Float(remoteX, remoteY));
		if (metropole == null) {
			throw new CityNotFoundException();
		}
		PMQuadTree roads = metropole.getRoads();
		if (roads.size() == 0) {
			throw new CityNotFoundException();
		}

		/* fill priority queue with cities based off proximity */
		PriorityQueue<City> queue = new PriorityQueue<>(new CityDistanceComparator(localX, localY));
		this.fillQueue(queue, roads.getRoot(), false);
		if (queue.size() == 0) {
			throw new CityNotFoundException();
		}
		City nearest = queue.poll();

		/* getting lexicographically first city in case of ties */
		while (queue.peek() != null && queue.peek().distance(localX, localY) <= nearest.distance(localX, localY)) {
			City temp = queue.poll();
			if (temp.getName().compareTo(nearest.getName()) < 0) {
				nearest = temp;
			}
		}
		return nearest;
	}

	Airport nearestAirport(int localX, int localY, int remoteX, int remoteY) throws AirportNotFoundException {
		if (remoteX >= this.globalWidth || remoteY >= this.globalHeight) {
			throw new AirportNotFoundException();
		}
		Metropole metropole = this.metropoles.getMetropole(new Point2D.Float(remoteX, remoteY));
		if (metropole == null) {
			throw new AirportNotFoundException();
		}
		AirportDictionary airports = metropole.getAirports();
		if (airports.size() == 0) {
			throw new AirportNotFoundException();
		}

		/* fill priority queue with cities based off proximity */
		PriorityQueue<Airport> queue = new PriorityQueue<>(new CityDistanceComparator(localX, localY));
		// this.fillQueue(queue, roads.getRoot(), false);
		for (Airport airport : airports) {
			queue.add(airport);
		}
		if (queue.size() == 0) {
			throw new AirportNotFoundException();
		}
		Airport nearest = queue.poll();

		/* getting lexicographically first city in case of ties */
		while (queue.peek() != null && queue.peek().distance(localX, localY) <= nearest.distance(localX, localY)) {
			Airport temp = queue.poll();
			if (temp.getName().compareTo(nearest.getName()) < 0) {
				nearest = temp;
			}
		}
		return nearest;
	}

	Element shortestPath(String start, String end, String saveMap, String saveHTML, Document doc, CommandWriter writer,
			String id) throws NonExistentStartException, NonExistentEndException, NoPathExistsException {
		City startCity = this.cityDictionary.getCity(start);
		// TODO
		// if (startCity == null || !this.spatial.contains(startCity)) {
		// throw new NonExistentStartException();
		// }
		City endCity = this.cityDictionary.getCity(end);
		// TODO spatial stuff changes
		// if (endCity == null || !this.spatial.contains(endCity)) {
		// throw new NonExistentEndException();
		// }
		Graph<City> dijkstra = new Graph<>();
		try {
			dijkstra.addVertex(start, startCity);
		} catch (IllegalArgumentException e) {
		}
		try {
			dijkstra.addVertex(end, endCity);
		} catch (IllegalArgumentException e) {
		}
		for (Object[] road : this.cityAdjList) {
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
					arc.setArcByTangent(this.cityDictionary.getCity(endRoad), this.cityDictionary.getCity(midPoint),
							this.cityDictionary.getCity(startRoad), 1);
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
		// if (saveMap != null || saveHTML != null) {
		// CanvasPlus canvas = this.shortestPathCanvas(path);
		// if (saveMap != null) {
		// try {
		// canvas.save(saveMap);
		// } catch (IOException e) {
		// }
		// }
		// if (saveHTML != null) {
		// try {
		// org.w3c.dom.Document shortestPathDoc =
		// XmlUtility.getDocumentBuilder().newDocument();
		// org.w3c.dom.Node spNode = shortestPathDoc.importNode(success, true);
		// shortestPathDoc.appendChild(spNode);
		// XmlUtility.transform(shortestPathDoc, new File("shortestPath.xsl"),
		// new File(saveHTML + ".html"));
		// canvas.save(saveHTML); // add picture (?)
		// } catch (ParserConfigurationException | TransformerException |
		// IOException e) {
		// }
		// }
		// canvas.dispose();
		// }
		return success;
	}

	private void fillQueue(PriorityQueue<City> queue, PMNode node, boolean isolated) {
		if (node instanceof PMBlackNode) {
			City city = ((PMBlackNode) node).getCity();
			// if city isn't null and is isolated, add
			// if (city != null && this.adjacencyList.isIsolated(city) ==
			// isolated) {
			if (city != null) {
				queue.add(city);
			}

		} else if (node instanceof PMGrayNode) {
			for (PMNode child : ((PMGrayNode) node).getChildren()) {
				this.fillQueue(queue, child, isolated); // add children nodes
			}
		}
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

}
