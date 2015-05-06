package cmsc420.schema.mediator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
import cmsc420.schema.SortType;
import cmsc420.schema.spatial.PM.PMQuadTree;
import cmsc420.sortedmap.AvlGTree;
import cmsc420.xml.XmlUtility;

/**
 * The main mediator object that reads and processes the XML directives.
 * 
 * @author Andrew Liu
 *
 */
public class CommandParser {

	private Document input;
	private boolean processed;
	private CommandRunner runner;
	private CommandWriter writer;

	/**
	 * Constructor. Initializes functional data structures and prepares the XML
	 * output document.
	 * 
	 * @param dict
	 *            dictionary data structure
	 * @param seed
	 *            seedling for spatial data structure
	 * @param adj
	 *            adjacency list data structure
	 */
	public CommandParser() {
		this.processed = false;
		writer = new CommandWriter();
	}

	/**
	 * Checks if the parser has processed any input yet.
	 * 
	 * @return whether this instance has run process yet
	 */
	public boolean isProcessed() {
		return this.processed;
	}

	/**
	 * Reads and validates XML input from stdin and proceeds to parse the input.
	 * This method does nothing if process has already been called before.
	 */
	public void process() {
		this.process(System.in);
	}

	/**
	 * Reads and validates XML input from an input stream and proceeds to parse
	 * the input. This method does nothing if process has already been called
	 * before.
	 * 
	 * @param xmlStream
	 *            the XML input stream
	 */
	public void process(InputStream xmlStream) {
		/* prevent multiple parsing */
		if (this.processed) {
			return;
		}
		this.processed = true;

		/* validate XML and then parse */
		try {
			this.input = XmlUtility.validateNoNamespace(xmlStream);
			this.parse();
		} catch (SAXException | IOException | ParserConfigurationException e) {
			this.writer.fatalError();
		}
	}

	/**
	 * Reads and validates XML input from a file and proceeds to parse the
	 * input. This method does nothing if process has already been called
	 * before.
	 * 
	 * @param xmlFile
	 *            the XML input file
	 */
	public void process(File xmlFile) {
		/* prevent multiple parsing */
		if (this.processed) {
			return;
		}
		this.processed = true;

		/* validate XML and then parse */
		try {
			this.input = XmlUtility.validateNoNamespace(xmlFile);
			this.parse();
		} catch (SAXException | IOException | ParserConfigurationException e) {
			this.writer.fatalError();
		}
	}

	/**
	 * Parses the XML input, runs the commands, and writes the output to an XML
	 * document.
	 */
	private void parse() {
		Node root = this.input.getFirstChild(); // root tag from XML document
		NodeList commands = root.getChildNodes(); // command tags

		/* retrieve spatial attributes and generate spatial structure */
		NamedNodeMap attrs = root.getAttributes();
//		int spatialWidth = Integer.parseInt(attrs.getNamedItem("spatialWidth").getNodeValue());
//		int spatialHeight = Integer.parseInt(attrs.getNamedItem("spatialHeight").getNodeValue());
		int localSpatialWidth = Integer.parseInt(attrs.getNamedItem("localSpatialWidth").getNodeValue());
		int localSpatialHeight = Integer.parseInt(attrs.getNamedItem("localSpatialHeight").getNodeValue());
		int remoteSpatialWidth = Integer.parseInt(attrs.getNamedItem("remoteSpatialWidth").getNodeValue());
		int remoteSpatialHeight = Integer.parseInt(attrs.getNamedItem("remoteSpatialHeight").getNodeValue());
		int g = 1;
		try {
			g = Integer.parseInt(attrs.getNamedItem("g").getNodeValue());
		} catch (NullPointerException e) {
		}
		int pmOrder = 3;
		try {
			pmOrder = Integer.parseInt(attrs.getNamedItem("pmOrder").getNodeValue());
		} catch (NullPointerException e) {
		}
		this.runner = new CommandRunner(localSpatialWidth, localSpatialHeight, remoteSpatialWidth, remoteSpatialHeight, g, pmOrder);

		/* process each command */
		for (int i = 0; i < commands.getLength(); i++) {
			Node commandNode = commands.item(i);
			if (commandNode instanceof Element) {
				String command = commandNode.getNodeName();
				NamedNodeMap params = commandNode.getAttributes();
				if (command.equals("createCity")) {

					/* get parameters */
					String localXString = params.getNamedItem("localX").getNodeValue();
					String localYString = params.getNamedItem("localY").getNodeValue();
					String remoteXString = params.getNamedItem("remoteX").getNodeValue();
					String remoteYString = params.getNamedItem("remoteY").getNodeValue();
					String radiusString = params.getNamedItem("radius").getNodeValue();
					String colorString = params.getNamedItem("color").getNodeValue();
					String name = params.getNamedItem("name").getNodeValue();

					/* parse parameters */
					int localX = Integer.parseInt(localXString);
					int localY = Integer.parseInt(localYString);
					int remoteX = Integer.parseInt(remoteXString);
					int remoteY = Integer.parseInt(remoteYString);
					int radius = Integer.parseInt(radiusString);
					CityColor color = CityColor.getCityColor(colorString);
					String id = null;
					try {
						id = params.getNamedItem("id").getNodeValue();
					} catch (NullPointerException e) {
					}
					String[] paramNames = { "name", "localX", "localY", "remoteX", "remoteY", "radius", "color" };
					String[] parameters = { name, localXString, localYString, remoteXString, remoteYString,
							radiusString, colorString };
					try {
						this.runner.createCity(name, localX, localY, remoteX, remoteY, radius, color);
						this.writer.appendTag(null, command, parameters, paramNames, id);
					} catch (DuplicateCityNameException | DuplicateCityCoordinatesException e) {
						this.writer.appendTag(e.getMessage(), command, parameters, paramNames, id);
					}
				} else if (command.equals("deleteCity")) {

					/* get parameters */
					String name = params.getNamedItem("name").getNodeValue();
					String id = null;
					try {
						id = params.getNamedItem("id").getNodeValue();
					} catch (NullPointerException e) {
					}
					String[] paramNames = { "name" };
					String[] parameters = { name };
					try {
						City deleted = this.runner.deleteCity(name);
//						this.writer.appendTagCityUnmapped(command, parameters, paramNames, deleted, id);
						// TODO return type is city and roadlist
						// need to fix writer method
					} catch (CityDoesNotExistException e) {
						this.writer.appendTag(e.getMessage(), command, parameters, paramNames, id);
					}
				} else if (command.equals("clearAll")) {
					String id = null;
					try {
						id = params.getNamedItem("id").getNodeValue();
					} catch (NullPointerException e) {
					}
					String[] paramNames = {};
					String[] parameters = {};
					this.runner.clearAll();
					this.writer.appendTag(null, command, parameters, paramNames, id);
				} else if (command.equals("listCities")) {

					/* get parameters */
					String sortByString = params.getNamedItem("sortBy").getNodeValue();
					SortType sortBy = SortType.getSortType(sortByString);
					String id = null;
					try {
						id = params.getNamedItem("id").getNodeValue();
					} catch (NullPointerException e) {
					}
					String[] paramNames = { "sortBy" };
					String[] parameters = { sortByString };
					try {
						List<City> cities = this.runner.listCities(sortBy);
						this.writer.appendTagCityList(command, parameters, paramNames, cities, id);
					} catch (NoCitiesToListException e) {
						this.writer.appendTag(e.getMessage(), command, parameters, paramNames, id);
					}
				} else if (command.equals("printAvlTree")) {
					String id = null;
					try {
						id = params.getNamedItem("id").getNodeValue();
					} catch (NullPointerException e) {
					}
					String[] paramNames = {};
					String parameters[] = {};
					try {
						AvlGTree<String, City> tree = this.runner.printAvlTree();
						this.writer.appendTagAVLTree(command, parameters, paramNames, tree, id);
					} catch (EmptyTreeException e) {
						this.writer.appendTag(e.getMessage(), command, parameters, paramNames, id);
					}

				} else if (command.equals("mapRoad")) {

					/* get parameters */
					String start = params.getNamedItem("start").getNodeValue();
					String end = params.getNamedItem("end").getNodeValue();
					String id = null;
					try {
						id = params.getNamedItem("id").getNodeValue();
					} catch (NullPointerException e) {
					}
					String[] paramNames = { "start", "end" };
					String[] parameters = { start, end };
					try {
						this.runner.mapRoad(start, end);
						this.writer.appendTagRoadCreated(command, parameters, paramNames, start, end, id);
					} catch (StartPointDoesNotExistException | EndPointDoesNotExistException | StartEqualsEndException
							| StartOrEndIsIsolatedException | RoadAlreadyMappedException | RoadOutOfBoundsException
							|RoadNotInOneMetropoleException | RoadIntersectsAnotherRoadException | RoadViolatesPMRulesException e) {
						this.writer.appendTag(e.getMessage(), command, parameters, paramNames, id);
					}
				} else if (command.equals("mapAirport")) {

					/* get parameters */
					String name = params.getNamedItem("name").getNodeValue();
					String airlineName = params.getNamedItem("airlineName").getNodeValue();
					String localXString = params.getNamedItem("localX").getNodeValue();
					String localYString = params.getNamedItem("localY").getNodeValue();
					String remoteXString = params.getNamedItem("remoteX").getNodeValue();
					String remoteYString = params.getNamedItem("remoteY").getNodeValue();
					String id = null;
					
					/* parse params */
					int localX = Integer.parseInt(localXString);
					int localY = Integer.parseInt(localYString);
					int remoteX = Integer.parseInt(remoteXString);
					int remoteY = Integer.parseInt(remoteYString);
					
					try {
						id = params.getNamedItem("id").getNodeValue();
					} catch (NullPointerException e) {
					}
					String[] paramNames = { "name", "airlineName", "localX", "localY", "remoteX", "remoteY" };
					String[] parameters = { name, airlineName, localXString, localYString, remoteXString, remoteYString };
					try {
						this.runner.mapAirport(name, airlineName, localX, localY, remoteX, remoteY);
						this.writer.appendTag(null, command, parameters, paramNames, id);
					} catch (DuplicateAirportNameException | DuplicateAirportCoordinatesException
							| AirportOutOfBoundsException | AirportViolatesPMRulesException e) {
						this.writer.appendTag(e.getMessage(), command, parameters, paramNames, id);
					}
				} else if (command.equals("unmapRoad")) {
					/* get parameters */
					String start = params.getNamedItem("start").getNodeValue();
					String end = params.getNamedItem("end").getNodeValue();
					String[] paramNames = { "start", "end" };
					String[] parameters = { start, end };
					try {
						this.runner.unmapRoad(start, end);
						this.writer.appendTagRoadDeleted(command, parameters, paramNames, start, end, null);
					} catch (StartPointDoesNotExistException | EndPointDoesNotExistException | StartEqualsEndException
							| RoadNotMappedException e) {
						this.writer.appendTag(e.getMessage(), command, parameters, paramNames, null);
					}
				} else if (command.equals("unmapAirport")) {
					String name = params.getNamedItem("name").getNodeValue();
					String[] paramNames = { "name" };
					String[] parameters = { name };
					try {
						this.runner.unmapAirport(name);
						this.writer.appendTag(null, command, parameters, paramNames, null);
					} catch (AirportDoesNotExistException e) {
						this.writer.appendTag(e.getMessage(), command, parameters, paramNames, null); 
					}
				} else if (command.equals("printPMQuadtree")) {
					String remoteXString = params.getNamedItem("remoteX").getNodeValue();
					String remoteYString = params.getNamedItem("remoteY").getNodeValue();
					int remoteX = Integer.parseInt(remoteXString);
					int remoteY = Integer.parseInt(remoteYString);
					String id = null;
					try {
						id = params.getNamedItem("id").getNodeValue();
					} catch (NullPointerException e) {
					}
					String[] paramNames = { "remoteX", "remoteY" };
					String parameters[] = { remoteXString, remoteYString };
					try {
						PMQuadTree tree = this.runner.printPMQuadtree(remoteX, remoteY);
						this.writer.appendTagQuadtree(command, parameters, paramNames, tree, id);
					} catch (MetropoleOutOfBoundsException | MetropoleIsEmptyException e) {
						this.writer.appendTag(e.getMessage(), command, parameters, paramNames, id);
					}
				} else if (command.equals("saveMap")) {

					/* get parameters */
					String remoteXString = params.getNamedItem("remoteX").getNodeValue();
					String remoteYString = params.getNamedItem("remoteY").getNodeValue();
					String name = params.getNamedItem("name").getNodeValue();
					String id = null;
					try {
						id = params.getNamedItem("id").getNodeValue();
					} catch (NullPointerException e) {
					}
					int remoteX = Integer.parseInt(remoteXString);
					int remoteY = Integer.parseInt(remoteYString);
					String[] paramNames = { "remoteX", "remoteY", "name" };
					String[] parameters = { remoteXString, remoteYString, name };
					try {
						this.runner.saveMap(remoteX, remoteY, name);
						this.writer.appendTag(null, command, parameters, paramNames, id);
					} catch (MetropoleOutOfBoundsException | MetropoleIsEmptyException e) {
						this.writer.appendTag(e.getMessage(), command, parameters, paramNames, null);
					}
				} else if (command.equals("globalRangeCities")) {
					
					String remoteXString = params.getNamedItem("remoteX").getNodeValue();
					String remoteYString = params.getNamedItem("remoteY").getNodeValue();
					String radiusString = params.getNamedItem("radius").getNodeValue();
					int remoteX = Integer.parseInt(remoteXString);
					int remoteY = Integer.parseInt(remoteYString);
					int radius = Integer.parseInt(radiusString);
					String[] paramNames = { "remoteX", "remoteY", "radius" };
					String[] parameters = { remoteXString, remoteYString, radiusString };
					try {
						List<City> cities = this.runner.globalRangeCities(remoteX, remoteY, radius);
						this.writer.appendTagCityList(command, parameters, paramNames, cities, null);
					} catch (NoCitiesExistInRangeException e) {
						this.writer.appendTag(e.getMessage(), command, parameters, paramNames, null);
					}
				} else if (command.equals("nearestCity")) {

					/* get parameters */
					String localXString = params.getNamedItem("localX").getNodeValue();
					String localYString = params.getNamedItem("localY").getNodeValue();
					String remoteXString = params.getNamedItem("remoteX").getNodeValue();
					String remoteYString = params.getNamedItem("remoteY").getNodeValue();
					int localX = Integer.parseInt(localXString);
					int localY = Integer.parseInt(localYString);
					int remoteX = Integer.parseInt(remoteXString);
					int remoteY = Integer.parseInt(remoteYString);
					String id = null;
					try {
						id = params.getNamedItem("id").getNodeValue();
					} catch (NullPointerException e) {
					}
					String[] paramNames = { "localX", "localY", "remoteX", "remoteY" };
					String[] parameters = { localXString, localYString, remoteXString, remoteYString };
					try {
						City city = this.runner.nearestCity(localX, localY, remoteX, remoteY);
						this.writer.appendTagCity(command, parameters, paramNames, city, id);
					} catch (CityNotFoundException e) {
						this.writer.appendTag(e.getMessage(), command, parameters, paramNames, id);
					}
				} else if (command.equals("nearestAirport")) {
					String localXString = params.getNamedItem("localX").getNodeValue();
					String localYString = params.getNamedItem("localY").getNodeValue();
					String remoteXString = params.getNamedItem("remoteX").getNodeValue();
					String remoteYString = params.getNamedItem("remoteY").getNodeValue();
					int localX = Integer.parseInt(localXString);
					int localY = Integer.parseInt(localYString);
					int remoteX = Integer.parseInt(remoteXString);
					int remoteY = Integer.parseInt(remoteYString);
					String[] paramNames = { "localX", "localY", "remoteX", "remoteY" };
					String[] parameters = { localXString, localYString, remoteXString, remoteYString };
					try {
						Airport airport = this.runner.nearestAirport(localX, localY, remoteX, remoteY);
						this.writer.appendTagAirport(command, parameters, paramNames, airport, null);
					} catch (AirportNotFoundException e) {
						this.writer.appendTag(e.getMessage(), command, parameters, paramNames, null);
					}
				} else if (command.equals("shortestPath")) {

					/* get parameters */
					String start = params.getNamedItem("start").getNodeValue();
					String end = params.getNamedItem("end").getNodeValue();
					String saveMap = null;
					try {
						saveMap = params.getNamedItem("saveMap").getNodeValue();
					} catch (NullPointerException e) {
					}
					String saveHTML = null;
					try {
						saveHTML = params.getNamedItem("saveHTML").getNodeValue();
					} catch (NullPointerException e) {
					}
					String id = null;
					try {
						id = params.getNamedItem("id").getNodeValue();
					} catch (NullPointerException e) {
					}
					String[] paramNames = { "start", "end", "saveMap", "saveHTML" };
					String[] parameters = { start, end, saveMap, saveHTML };
					try {
						Element e = this.runner.shortestPath(start, end, saveMap, saveHTML, this.writer.getDoc(),
								this.writer, id);
						this.writer.appendShortestPathTag(e);
					} catch (NonExistentStartException | NonExistentEndException | NoPathExistsException e) {
						this.writer.appendTag(e.getMessage(), command, parameters, paramNames, id);
					}
				} else {
					this.writer.undefinedError();
				}
			}
		}
		this.writer.close();
//		this.runner.close();
	}

	/**
	 * Prints the XML output contents after processing the input commands. This
	 * method does nothing if process has net yet been called before.
	 */
	public void print() {
		if (!this.processed) {
			return;
		}
		this.writer.print();
	}
}
