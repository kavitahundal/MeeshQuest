package cmsc420.schema.mediator;

import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.schema.Airport;
import cmsc420.schema.City;
import cmsc420.schema.spatial.SpatialStructure;
import cmsc420.sortedmap.AvlGTree;
import cmsc420.xml.XmlUtility;

public class CommandWriter {

	private Document output;
	private Element root;

	/**
	 * Constructor. Creates the output XML document.
	 */
	CommandWriter() {
		try {
			this.output = XmlUtility.getDocumentBuilder().newDocument();
			this.root = this.output.createElement("results"); // root tag of XML
		} catch (ParserConfigurationException e) {
		}
	}

	void fatalError() {
		try {
			this.output = XmlUtility.getDocumentBuilder().newDocument();
			this.output.appendChild(output.createElement("fatalError"));
		} catch (ParserConfigurationException e) {
		}
	}

	/**
	 * Appends the XML output of a generic command.
	 * 
	 * @param errorType
	 *            the error type (null if success)
	 * @param command
	 *            the command processed
	 * @param parameters
	 *            the values of the parameters of the command
	 * @param paramNames
	 *            the names of the parameters of the command
	 */
	void appendTag(String errorType, String command, String[] parameters, String[] paramNames, String id) {
		Element tag = this.initiateTag(errorType);
		tag = this.createTag(tag, command, parameters, paramNames, id);
		Element outputTag = this.output.createElement("output");
		if (errorType == null) {
			tag.appendChild(outputTag); // error tags have no output child tag
		}
		this.root.appendChild(tag);
	}

	/**
	 * Appends the XML output of a printing of a PR Quadtree.
	 * 
	 * @param command
	 *            the command processed
	 * @param parameters
	 *            the values of the parameters of the command
	 * @param paramNames
	 *            the names of the parameters of the command
	 * @param tree
	 *            the PR Quadtree to print
	 */
	void appendTagQuadtree(String command, String[] parameters, String[] paramNames, SpatialStructure tree, String id) {
		Element tag = this.initiateTag(null);
		tag = this.createTag(tag, command, parameters, paramNames, id);
		Element outputTag = this.output.createElement("output");
		Element treeTag = tree.elementize(this.output); // XML version of tree
		outputTag.appendChild(treeTag);
		tag.appendChild(outputTag);
		this.root.appendChild(tag);
	}

	// do you like how I copied the code from the above method? :D
	void appendTagAVLTree(String command, String[] parameters, String[] paramNames, AvlGTree<String, City> tree, String id) {
		Element tag = this.initiateTag(null);
		tag = this.createTag(tag, command, parameters, paramNames, id);
		Element outputTag = this.output.createElement("output");
		Element treeTag = tree.elementize(this.output); // XML version of tree
		outputTag.appendChild(treeTag);
		tag.appendChild(outputTag);
		this.root.appendChild(tag);
	}

	/**
	 * Appends the XML output of a command that outputs a list of cities.
	 * 
	 * @param command
	 *            the command processed
	 * @param parameters
	 *            the values of the parameters of the command
	 * @param paramNames
	 *            the names of the parameters of the command
	 * @param cities
	 *            the list of cities to output
	 */
	void appendTagCityList(String command, String[] parameters, String[] paramNames, List<City> cities, String id) {
		Element tag = this.initiateTag(null);
		tag = this.createTag(tag, command, parameters, paramNames, id);
		Element outputTag = this.output.createElement("output");
		Element cityList = this.output.createElement("cityList");

		/* appending city XML nodes */
		for (City city : cities) {
			cityList.appendChild(this.mapCity(city));
		}
		outputTag.appendChild(cityList);
		tag.appendChild(outputTag);
		this.root.appendChild(tag);
	}

	/**
	 * Appends the XML output of a command that outputs a single city.
	 * 
	 * @param command
	 *            the command processed
	 * @param parameters
	 *            the values of the parameters of the command
	 * @param paramNames
	 *            the names of the parameters of the command
	 * @param city
	 *            the city to output
	 */
	void appendTagCity(String command, String[] parameters, String[] paramNames, City city, String id) {
		Element tag = this.initiateTag(null);
		tag = this.createTag(tag, command, parameters, paramNames, id);
		Element outputTag = this.output.createElement("output");
		outputTag.appendChild(this.mapCity(city)); // append city node
		tag.appendChild(outputTag);
		this.root.appendChild(tag);
	}
	
	void appendTagAirport(String command, String[] parameters, String[] paramNames, Airport airport, String id) {
		Element tag = this.initiateTag(null);
		tag = this.createTag(tag, command, parameters, paramNames, id);
		Element outputTag = this.output.createElement("output");
		outputTag.appendChild(this.mapAirport(airport)); // append city node
		tag.appendChild(outputTag);
		this.root.appendChild(tag);
	}

	/**
	 * Appends the XML output of a command where cities may be unmapped as a
	 * side effect of running the command.
	 * 
	 * @param command
	 *            the command processed
	 * @param parameters
	 *            the values of the parameters of the command
	 * @param paramNames
	 *            the names of the parameters of the command
	 * @param city
	 *            the city that was unmapped (null if no city was unmapped)
	 */
	void appendTagCityUnmapped(String command, String[] parameters, String[] paramNames, City city, List<City[]> roads, String id) {
		Element tag = this.initiateTag(null);
		tag = this.createTag(tag, command, parameters, paramNames, id);
		Element outputTag = this.output.createElement("output");

		/* append all city nodes */
		if (city != null) {
			outputTag.appendChild(this.unmapCity(city));
		}

		for (City[] road : roads) {
			Element roadTag = this.output.createElement("roadUnmapped");
			roadTag.setAttribute("start", road[0].getName());
			roadTag.setAttribute("end", road[1].getName());
			outputTag.appendChild(roadTag);
		}
		
		tag.appendChild(outputTag);
		this.root.appendChild(tag);
	}

	void appendTagRoadCreated(String command, String[] parameters, String[] paramNames, String start, String end, String id) {
		Element tag = this.initiateTag(null);
		tag = this.createTag(tag, command, parameters, paramNames, id);
		Element outputTag = this.output.createElement("output");
		outputTag.appendChild(this.mapRoadCreated(start, end));
		tag.appendChild(outputTag);
		this.root.appendChild(tag);
	}
	
	void appendTagRoadDeleted(String command, String[] parameters, String[] paramNames, String start, String end, String id) {
		Element tag = this.initiateTag(null);
		tag = this.createTag(tag, command, parameters, paramNames, id);
		Element outputTag = this.output.createElement("output");
		outputTag.appendChild(this.mapRoadDeleted(start, end));
		tag.appendChild(outputTag);
		this.root.appendChild(tag);
	}
	
	
	Element shortestPathTag(String command, String[] parameters, String[] paramNames, Element e, String id) {
		Element tag = this.initiateTag(null);
		tag = this.createTag(tag, command, parameters, paramNames, id);
		Element outputTag = this.output.createElement("output");
		outputTag.appendChild(e);
		tag.appendChild(outputTag);
//		this.root.appendChild(tag);
		return tag;
	}
	
	void appendShortestPathTag(Element tag) {
		this.root.appendChild(tag);
	}
	
	void appendTagCityRoadsUnmapped(String command, String[] parameters, String[] paramNames, City city, List<City[]> deleted, String id) {
		Element tag = this.initiateTag(null);
		tag = this.createTag(tag, command, parameters, paramNames, id);
		Element outputTag = this.output.createElement("output");
		// outputtag append child cityunmapped
		outputTag.appendChild(this.unmapCity(city));
		// for each road
		// outputtag append child roadunmapped
		for (City[] road : deleted) {
			outputTag.appendChild(this.mapRoadUnmapped(road[0].getName(), road[1].getName()));
		}
		tag.appendChild(outputTag);
		this.root.appendChild(tag);
	}

	private Element mapCity(City city) {
		Element tag = this.output.createElement("city");
		return this.mapLocation(city, tag); // append city XML
	}
	
	private Element mapAirport(Airport airport) {
		Element tag = this.output.createElement("airport");
		return this.mapAirportLocation(airport, tag); // append city XML
	}

	private Element mapRoadCreated(String start, String end) {
		Element tag = this.output.createElement("roadCreated");
		tag.setAttribute("start", start);
		tag.setAttribute("end", end);
		return tag;
	}
	
	private Element mapRoadDeleted(String start, String end) {
		Element tag = this.output.createElement("roadDeleted");
		tag.setAttribute("start", start);
		tag.setAttribute("end", end);
		return tag;
	}
	
	private Element mapRoadUnmapped(String start, String end) {
		Element tag = this.output.createElement("roadUnmapped");
		tag.setAttribute("start", start);
		tag.setAttribute("end", end);
		return tag;
	}

	private Element unmapCity(City city) {
		Element tag = this.output.createElement("cityUnmapped");
		return this.mapLocation(city, tag); // append city XML
	}

	private Element mapLocation(City city, Element tag) {
		tag.setAttribute("name", city.getName());
		tag.setAttribute("localX", Integer.toString((int) city.x));
		tag.setAttribute("localY", Integer.toString((int) city.y));
		tag.setAttribute("remoteX", Integer.toString((int) city.remoteX));
		tag.setAttribute("remoteY", Integer.toString((int) city.remoteY));
		tag.setAttribute("color", city.getColor().toString());
		tag.setAttribute("radius", Integer.toString(city.getRadius()));
		return tag; // XML element with the city
	}
	
	private Element mapAirportLocation(Airport airport, Element tag) {
		tag.setAttribute("name", airport.getName());
		tag.setAttribute("airlineName", airport.getAirlineName());
		tag.setAttribute("localX", Integer.toString((int) airport.x));
		tag.setAttribute("localY", Integer.toString((int) airport.y));
		tag.setAttribute("remoteX", Integer.toString(airport.remoteX));
		tag.setAttribute("remoteY", Integer.toString(airport.remoteY));
		return tag; // XML element with the city
	}

	private Element initiateTag(String errorType) {
		Element tag;
		if (errorType == null) {
			tag = this.output.createElement("success");
		} else {
			tag = this.output.createElement("error");
			tag.setAttribute("type", errorType);
		}
		return tag; // return tag with success or error
	}

	private Element createTag(Element tag, String command, String[] parameters, String[] paramNames, String id) {
		Element commandTag = this.output.createElement("command"); // command
																	// tag
		commandTag.setAttribute("name", command);
		if (id != null) {
			commandTag.setAttribute("id", id);
		}
		tag.appendChild(commandTag);

		Element paramTag = this.output.createElement("parameters"); // parameter
																	// tag
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i] == null) {
				continue; // for optional parameters
			}
			Element param = this.output.createElement(paramNames[i]);
			param.setAttribute("value", parameters[i]);
			paramTag.appendChild(param);
		}
		tag.appendChild(paramTag);
		return tag;
	}

	/**
	 * Prints an undefined error tag to the XML output.
	 */
	void undefinedError() {
		this.root.appendChild(this.output.createElement("undefinedError"));
	}

	/**
	 * Concludes the XML output by appending the root node to the XML document.
	 */
	void close() {
		this.output.appendChild(root);
	}

	/**
	 * Prints the XML document.
	 */
	void print() {
		try {
			XmlUtility.print(this.output);
		} catch (TransformerException e) {
		}
	}
	
	Document getDoc() {
		return this.output;
	}
}
