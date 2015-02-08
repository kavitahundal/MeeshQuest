package cmsc420.schema.mediator;

import java.io.IOException;

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

public class CommandParser {

	private Document input;
	private Document output;
	private float spatialWidth;
	private float spatialHeight;
	private DictionaryStructure dictionary;
	private Seedling seed;
	private SpatialStructure spatial;
	private AdjacencyListStructure adjacencyList;
	private boolean processed;
	private Element results;
	
	// TODO documentation, print success/error, classes for each command???

	public CommandParser(DictionaryStructure dict, Seedling seed, AdjacencyListStructure adj) {
		this.processed = false;
		
		/* initialize data structures */
		this.dictionary = dict;
		this.seed = seed;
		this.adjacencyList = adj;
		
		/* initialize output XML document */
		try {
			this.output = XmlUtility.getDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
		}
	}
	
	public void process() {
		if (!this.processed) {
			this.processed = true;
			try {
				input = XmlUtility.validateNoNamespace(System.in);
				results = output.createElement("results");
				output.appendChild(results); // root tag of XML output
				this.parse();
			} catch (SAXException | IOException | ParserConfigurationException e) {
				output.appendChild(output.createElement("fatalError"));
			}
		}
	}
	
	private void parse() {
		Node root = input.getFirstChild(); // root tag from XML document
		NodeList commands = root.getChildNodes(); // command tags
		

		/* retrieve spatial attributes and generate spatial structure */
		NamedNodeMap attrs = root.getAttributes();
		this.spatialWidth = Integer.parseInt(attrs.getNamedItem("spatialWidth").getNodeValue());
		this.spatialHeight = Integer.parseInt(attrs.getNamedItem("spatialHeight").getNodeValue());
		this.spatial = this.seed.generate(spatialWidth, spatialHeight);

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
	
	public void print() {
		if (this.processed) {
			try {
				XmlUtility.print(this.output);
			} catch (TransformerException e) {
			}	
		}
	}
}
