package cmsc420.schema.mediator;

import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.schema.City;
import cmsc420.schema.spatial.PRQuadTree;
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
	void appendTag(String errorType, String command, String[] parameters, String[] paramNames) {
		Element tag = this.initiateTag(errorType);
		tag = this.createTag(tag, command, parameters, paramNames);
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
	void appendTag(String command, String[] parameters, String[] paramNames, PRQuadTree tree) {
		Element tag = this.initiateTag(null);
		tag = this.createTag(tag, command, parameters, paramNames);
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
	void appendTag(String command, String[] parameters, String[] paramNames, List<City> cities) {
		Element tag = this.initiateTag(null);
		tag = this.createTag(tag, command, parameters, paramNames);
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
	void appendTag(String command, String[] parameters, String[] paramNames, City city) {
		Element tag = this.initiateTag(null);
		tag = this.createTag(tag, command, parameters, paramNames);
		Element outputTag = this.output.createElement("output");
		outputTag.appendChild(this.mapCity(city)); // append city node
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
	void appendTagUnmapped(String command, String[] parameters, String[] paramNames, City city) {
		Element tag = this.initiateTag(null);
		tag = this.createTag(tag, command, parameters, paramNames);
		Element outputTag = this.output.createElement("output");

		/* append all city nodes */
		if (city != null) {
			outputTag.appendChild(this.unmapCity(city));
		}
		tag.appendChild(outputTag);
		this.root.appendChild(tag);
	}

	private Element mapCity(City city) {
		Element tag = this.output.createElement("city");
		return this.mapLocation(city, tag); // append city XML
	}

	private Element unmapCity(City city) {
		Element tag = this.output.createElement("cityUnmapped");
		return this.mapLocation(city, tag); // append city XML
	}

	private Element mapLocation(City city, Element tag) {
		tag.setAttribute("name", city.getName());
		tag.setAttribute("x", Integer.toString((int) city.x));
		tag.setAttribute("y", Integer.toString((int) city.y));
		tag.setAttribute("color", city.getColor().toString());
		tag.setAttribute("radius", Integer.toString(city.getRadius()));
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

	private Element createTag(Element tag, String command, String[] parameters, String[] paramNames) {
		Element commandTag = this.output.createElement("command"); // command tag
		commandTag.setAttribute("name", command);
		tag.appendChild(commandTag);
		Element paramTag = this.output.createElement("parameters"); // parameter tag
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
}
