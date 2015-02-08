package cmsc420.schema.mediator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

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
//	private Document output;
//	private Element results;

	private boolean processed;
//	private float spatialWidth;
//	private float spatialHeight;

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

		/* initialize output XML document */
//		try {
//			this.output = XmlUtility.getDocumentBuilder().newDocument();
//		} catch (ParserConfigurationException e) {
//		}
		
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
//			output.appendChild(output.createElement("fatalError"));
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
//			output.appendChild(output.createElement("fatalError"));
			this.writer.fatalError();
		}
	}

	/**
	 * process input
	 */
	private void parse() {
//		this.results = output.createElement("results");
//		this.output.appendChild(results); // root tag of XML output
		this.writer.createRoot();

		Node root = this.input.getFirstChild(); // root tag from XML document
		NodeList commands = root.getChildNodes(); // command tags

		/* retrieve spatial attributes and generate spatial structure */
		NamedNodeMap attrs = root.getAttributes();
		float spatialWidth = Integer.parseInt(attrs.getNamedItem("spatialWidth").getNodeValue());
		float spatialHeight = Integer.parseInt(attrs.getNamedItem("spatialHeight").getNodeValue());
		this.spatial = this.seed.generate(spatialWidth, spatialHeight);
		this.runner = new CommandRunner(this.dictionary, this.spatial, this.adjacencyList);

		/* process each command */
		for (int i = 0; i < commands.getLength(); i++) {
			Node commandNode = commands.item(i);
			if (commandNode instanceof Element) {
				String command = commandNode.getNodeName();
				NamedNodeMap params = commandNode.getAttributes();
				if (command.equals("createCity")) {
					String name = params.getNamedItem("name").getNodeValue();
					int x = Integer.parseInt(params.getNamedItem("x").getNodeValue());
					int y = Integer.parseInt(params.getNamedItem("y").getNodeValue());
					int radius = Integer.parseInt(params.getNamedItem("radius").getNodeValue());
					CityColor color = CityColor.getCityColor(params.getNamedItem("color").getNodeValue());
				} else if (command.equals("deleteCity")) {
					String name = params.getNamedItem("name").getNodeValue();
				} else if (command.equals("clearAll")) {
					// no params
				} else if (command.equals("listCities")) {
					SortType sortBy = SortType.getSortType(params.getNamedItem("sortBy").getNodeValue());
				} else if (command.equals("mapCity")) {
					String name = params.getNamedItem("name").getNodeValue();
				} else if (command.equals("unmapCity")) {
					String name = params.getNamedItem("name").getNodeValue();
				} else if (command.equals("printPRQuadtree")) {
					// no params
				} else if (command.equals("saveMap")) {
					String name = params.getNamedItem("name").getNodeValue();
				} else if (command.equals("rangeCities")) {
					int x = Integer.parseInt(params.getNamedItem("x").getNodeValue());
					int y = Integer.parseInt(params.getNamedItem("y").getNodeValue());
					int radius = Integer.parseInt(params.getNamedItem("radius").getNodeValue());
					String name = params.getNamedItem("name").getNodeValue();
				} else if (command.equals("nearestCity")) {
					int x = Integer.parseInt(params.getNamedItem("x").getNodeValue());
					int y = Integer.parseInt(params.getNamedItem("y").getNodeValue());
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
//		try {
//			XmlUtility.print(this.output);
//		} catch (TransformerException e) {
//		}
		this.writer.print();
	}
}
