package cmsc420.schema.mediator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cmsc420.schema.CityColor;
import cmsc420.schema.SortType;
import cmsc420.schema.adjacencylist.AdjacencyListStructure;
import cmsc420.schema.dictionary.DictionaryStructure;
import cmsc420.schema.spatial.Seedling;
import cmsc420.schema.spatial.SpatialStructure;
import cmsc420.xml.XmlUtility;

/**
 * 
 * 
 * @author Andrew Liu
 *
 */
public class CommandParser {

	private Document input;
	private boolean processed;

	private DictionaryStructure dictionary;
	private Seedling seed;
	private SpatialStructure spatial;
	private AdjacencyListStructure adjacencyList;

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
	public CommandParser(DictionaryStructure dict, Seedling seed, AdjacencyListStructure adj) {
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
		if (this.processed) {
			return;
		}
		this.processed = true;
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
		if (this.processed) {
			return;
		}
		this.processed = true;
		try {
			this.input = XmlUtility.validateNoNamespace(xmlFile);
			this.parse();
		} catch (SAXException | IOException | ParserConfigurationException e) {
			this.writer.fatalError();
		}
	}

	/**
	 * process input
	 */
	private void parse() {
		this.writer.createRoot(); // initialize writer for adding tags
		Node root = this.input.getFirstChild(); // root tag from XML document
		NodeList commands = root.getChildNodes(); // command tags

		/* retrieve spatial attributes and generate spatial structure */
		NamedNodeMap attrs = root.getAttributes();
		int spatialWidth = Integer.parseInt(attrs.getNamedItem("spatialWidth").getNodeValue());
		int spatialHeight = Integer.parseInt(attrs.getNamedItem("spatialHeight").getNodeValue());
		this.spatial = this.seed.generate(spatialWidth, spatialHeight);
		this.runner = new CommandRunner(this.dictionary, this.spatial, this.adjacencyList);

		/* process each command */
		for (int i = 0; i < commands.getLength(); i++) {
			Node commandNode = commands.item(i);
			if (commandNode instanceof Element) {
				String command = commandNode.getNodeName();
				NamedNodeMap params = commandNode.getAttributes();
				if (command.equals("createCity")) {
					String xString = params.getNamedItem("x").getNodeValue();
					String yString = params.getNamedItem("y").getNodeValue();
					String radiusString = params.getNamedItem("radius").getNodeValue();
					String colorString = params.getNamedItem("color").getNodeValue();
					String name = params.getNamedItem("name").getNodeValue();
					int x = Integer.parseInt(xString);
					int y = Integer.parseInt(yString);
					int radius = Integer.parseInt(radiusString);
					CityColor color = CityColor.getCityColor(colorString);
					String[] parameters = { name, xString, yString, radiusString, colorString };
					this.runner.createCity(name, x, y, radius, color);
				} else if (command.equals("deleteCity")) {
					String name = params.getNamedItem("name").getNodeValue();
					String[] parameters = { name };
					this.runner.deleteCity(name);
				} else if (command.equals("clearAll")) {
					String[] parameters = {};
					this.runner.clearAll();
				} else if (command.equals("listCities")) {
					String sortByString = params.getNamedItem("sortBy").getNodeValue();
					SortType sortBy = SortType.getSortType(sortByString);
					String[] parameters = { sortByString };
					this.runner.listCities(sortBy);
				} else if (command.equals("mapCity")) {
					String name = params.getNamedItem("name").getNodeValue();
					String[] parameters = { name };
					this.runner.mapCity(name);
				} else if (command.equals("unmapCity")) {
					String name = params.getNamedItem("name").getNodeValue();
					String[] parameters = { name };
					this.runner.unmapCity(name);
				} else if (command.equals("printPRQuadtree")) {
					String parameters[] = {};
					this.runner.printPRQuadTree();
				} else if (command.equals("saveMap")) {
					String name = params.getNamedItem("name").getNodeValue();
					String[] parameters = {};
					this.runner.saveMap(name);
				} else if (command.equals("rangeCities")) {
					String xString = params.getNamedItem("x").getNodeValue();
					String yString = params.getNamedItem("y").getNodeValue();
					String radiusString = params.getNamedItem("radius").getNodeValue();
					int x = Integer.parseInt(xString);
					int y = Integer.parseInt(yString);
					int radius = Integer.parseInt(radiusString);
					String name = params.getNamedItem("name").getNodeValue();
					String[] parameters = { xString, yString, radiusString, name };
					this.runner.rangeCities(x, y, radius, name);
				} else if (command.equals("nearestCity")) {
					String xString = params.getNamedItem("x").getNodeValue();
					String yString = params.getNamedItem("y").getNodeValue();
					int x = Integer.parseInt(xString);
					int y = Integer.parseInt(yString);
					String[] parameters = { xString, yString };
					this.runner.nearestCity(x, y);
				}
			}
		}
	}

	/**
	 * Prints the XML output contents after processing the input commands. This
	 * method does nothing if process has net yet been called before.
	 */
	public void print() {
		if (this.processed) {
			return;
		}
		this.writer.print();
	}
}
