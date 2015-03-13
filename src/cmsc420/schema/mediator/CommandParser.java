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
import cmsc420.schema.adjacencylist.AdjacencyList;
import cmsc420.schema.dictionary.DictionaryStructure;
import cmsc420.schema.spatial.PRQuadTree;
import cmsc420.schema.spatial.Seedling;
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
	private DictionaryStructure dictionary;
	private Seedling seed;
	private AdjacencyList<City> adjacencyList;
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
	public CommandParser(DictionaryStructure dict, Seedling seed, AdjacencyList<City> adj) {
		this.processed = false;

		/* initialize data structures */
		this.dictionary = dict;
		this.seed = seed;
		this.adjacencyList = adj;

		/* initialize output XML writer */
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
		int spatialWidth = Integer.parseInt(attrs.getNamedItem("spatialWidth").getNodeValue());
		int spatialHeight = Integer.parseInt(attrs.getNamedItem("spatialHeight").getNodeValue());
		this.runner = new CommandRunner(this.dictionary, this.seed, this.adjacencyList, spatialWidth, spatialHeight);

		/* process each command */
		for (int i = 0; i < commands.getLength(); i++) {
			Node commandNode = commands.item(i);
			if (commandNode instanceof Element) {
				String command = commandNode.getNodeName();
				NamedNodeMap params = commandNode.getAttributes();
				if (command.equals("createCity")) {

					/* get parameters */
					String xString = params.getNamedItem("x").getNodeValue();
					String yString = params.getNamedItem("y").getNodeValue();
					String radiusString = params.getNamedItem("radius").getNodeValue();
					String colorString = params.getNamedItem("color").getNodeValue();
					String name = params.getNamedItem("name").getNodeValue();

					/* parse parameters */
					int x = Integer.parseInt(xString);
					int y = Integer.parseInt(yString);
					int radius = Integer.parseInt(radiusString);
					CityColor color = CityColor.getCityColor(colorString);
					String id = null;
					try {
						id = params.getNamedItem("id").getNodeValue();
					} catch (NullPointerException e) {
					}
					String[] paramNames = { "name", "x", "y", "radius", "color" };
					String[] parameters = { name, xString, yString, radiusString, colorString };
					try {
						this.runner.createCity(name, x, y, radius, color);
						// TODO add id to appendTag
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
						this.writer.appendTagUnmapped(command, parameters, paramNames, deleted, id);
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
						this.writer.appendTag(command, parameters, paramNames, cities, id);
					} catch (NoCitiesToListException e) {
						this.writer.appendTag(e.getMessage(), command, parameters, paramNames, id);
					}
				} else if (command.equals("mapCity")) {

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
						this.runner.mapCity(name);
						this.writer.appendTag(null, command, parameters, paramNames, id);
					} catch (NameNotInDictionaryException | CityAlreadyMappedException | CityOutOfBoundsException e) {
						this.writer.appendTag(e.getMessage(), command, parameters, paramNames, id);
					}
				} else if (command.equals("unmapCity")) {

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
						this.runner.unmapCity(name);
						this.writer.appendTag(null, command, parameters, paramNames, id);
					} catch (NameNotInDictionaryException | CityNotMappedException e) {
						this.writer.appendTag(e.getMessage(), command, parameters, paramNames, id);
					}
				} else if (command.equals("printPRQuadtree")) {
					String id = null;
					try {
						id = params.getNamedItem("id").getNodeValue();
					} catch (NullPointerException e) {
					}
					String[] paramNames = {};
					String parameters[] = {};
					try {
						PRQuadTree tree = this.runner.printPRQuadTree();
						this.writer.appendTag(command, parameters, paramNames, tree, id);
					} catch (MapIsEmptyException e) {
						this.writer.appendTag(e.getMessage(), command, parameters, paramNames, id);
					}
				} else if (command.equals("saveMap")) {

					/* get parameters */
					String name = params.getNamedItem("name").getNodeValue();
					String id = null;
					try {
						id = params.getNamedItem("id").getNodeValue();
					} catch (NullPointerException e) {
					}
					String[] paramNames = { "name" };
					String[] parameters = { name };
					this.runner.saveMap(name);
					this.writer.appendTag(null, command, parameters, paramNames, id);
				} else if (command.equals("rangeCities")) {

					/* get parameters */
					String xString = params.getNamedItem("x").getNodeValue();
					String yString = params.getNamedItem("y").getNodeValue();
					String radiusString = params.getNamedItem("radius").getNodeValue();

					/* get parameters */
					int x = Integer.parseInt(xString);
					int y = Integer.parseInt(yString);
					int radius = Integer.parseInt(radiusString);
					String saveMap = null;
					try {
						saveMap = params.getNamedItem("saveMap").getNodeValue();
					} catch (NullPointerException e) {
					}
					String id = null;
					try {
						id = params.getNamedItem("id").getNodeValue();
					} catch (NullPointerException e) {
					}
					String[] paramNames = { "x", "y", "radius", "saveMap" };
					String[] parameters = { xString, yString, radiusString, saveMap };
					try {
						List<City> cities = this.runner.rangeCities(x, y, radius, saveMap);
						this.writer.appendTag(command, parameters, paramNames, cities, id);
					} catch (NoCitiesExistInRangeException e) {
						this.writer.appendTag(e.getMessage(), command, parameters, paramNames, id);
					}
				} else if (command.equals("nearestCity")) {

					/* get parameters */
					String xString = params.getNamedItem("x").getNodeValue();
					String yString = params.getNamedItem("y").getNodeValue();
					int x = Integer.parseInt(xString);
					int y = Integer.parseInt(yString);
					String id = null;
					try {
						id = params.getNamedItem("id").getNodeValue();
					} catch (NullPointerException e) {
					}
					String[] paramNames = { "x", "y" };
					String[] parameters = { xString, yString };
					try {
						City city = this.runner.nearestCity(x, y);
						this.writer.appendTag(command, parameters, paramNames, city, id);
					} catch (MapIsEmptyException e) {
						this.writer.appendTag(e.getMessage(), command, parameters, paramNames, id);
					}
				} else if (command.equals("printAvlTree")) {
					//
				} else if (command.equals("mapRoad")) {
					//
				} else if (command.equals("printPMQuadTree")) {
					//
				} else if (command.equals("rangeRoads")) {
					//
				} else if (command.equals("nearestIsolatedCity")) {
					//
				} else if (command.equals("nearestRoad")) {
					//
				} else if (command.equals("nearestCityToRoad")) {
					//
				} else if (command.equals("shortestPath")) {
					//
				} else {
					this.writer.undefinedError();
				}
			}
		}
		this.writer.close();
		this.runner.close();
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
