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
	
	CommandWriter() {
		try {
			this.output = XmlUtility.getDocumentBuilder().newDocument();
			this.root = this.output.createElement("results");
			this.output.appendChild(root); // root tag of XML output
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
	 * append generic success or error
	 * 
	 * @param errorType
	 * @param command
	 * @param parameters
	 */
	void appendTag(String errorType, String command, String[] parameters) {
		Element tag = this.initiateTag(errorType);
		tag = this.createTag(tag, command, parameters);
		Element outputTag = this.output.createElement("output");
		tag.appendChild(outputTag);
		this.root.appendChild(tag);
	}
	
	/**
	 * append print success
	 * 
	 * @param command
	 * @param parameters
	 * @param tree
	 */
	void appendTag(String command, String[] parameters, PRQuadTree tree) {
		Element tag = this.initiateTag(null);
		tag = this.createTag(tag, command, parameters);
		Element outputTag = this.output.createElement("output");
		Element treeTag = tree.elementize(this.output);
		outputTag.appendChild(treeTag);
		tag.appendChild(outputTag);
		this.root.appendChild(tag);
	}
	
	/**
	 * append citylist success
	 * 
	 * @param command
	 * @param parameters
	 * @param cities
	 */
	void appendTag(String command, String[] parameters, List<City> cities) {
		Element tag = this.initiateTag(null);
		tag = this.createTag(tag, command, parameters);
		Element outputTag = this.output.createElement("output");
		Element cityList = this.output.createElement("cityList");
		for (City city : cities) {
			cityList.appendChild(this.mapCity(city));
		}
		outputTag.appendChild(cityList);
		tag.appendChild(outputTag);
		this.root.appendChild(tag);
	}
	
	/**
	 * append city success
	 * 
	 * @param command
	 * @param parameters
	 * @param city
	 */
	void appendTag(String command, String[] parameters, City city) { // precond: city not null
		Element tag = this.initiateTag(null);
		tag = this.createTag(tag, command, parameters);
		Element outputTag = this.output.createElement("output");
		outputTag.appendChild(this.mapCity(city));
		tag.appendChild(outputTag);
		this.root.appendChild(tag);
	}
	
	/**
	 * append delete unmap city success
	 * 
	 * @param command
	 * @param parameters
	 * @param city
	 */
	void appendTagUnmapped(String command, String[] parameters, City city) {
		Element tag = this.initiateTag(null);
		tag = this.createTag(tag, command, parameters);
		Element outputTag = this.output.createElement("output");
		if (city != null) {
			outputTag.appendChild(this.unmapCity(city));
		}
		tag.appendChild(outputTag);
		this.root.appendChild(tag);
	}
	
	/**
	 * create city element
	 * 
	 * @param city
	 * @return
	 */
	private Element mapCity(City city) {
		Element tag = this.output.createElement("city");
		return this.mapLocation(city, tag);
	}
	
	/**
	 * create city element with different tag name
	 * 
	 * @param city
	 * @return
	 */
	private Element unmapCity(City city) {
		Element tag = this.output.createElement("cityUnmapped");
		return this.mapLocation(city, tag);
	}
	
	/**
	 * create city element with any tag name
	 * 
	 * @param city
	 * @param tag
	 * @return
	 */
	private Element mapLocation(City city, Element tag) {
		tag.setAttribute("name", city.getName());
		tag.setAttribute("x", Float.toString(city.x));
		tag.setAttribute("y", Float.toString(city.y));
		tag.setAttribute("color", city.getColor().toString());
		tag.setAttribute("radius", Integer.toString(city.getRadius()));
		return tag;
	}
	
	private Element initiateTag(String errorType) {
		Element tag;
		if (errorType == null) {
			tag = this.output.createElement("success");
		} else {
			tag = this.output.createElement("error");
			tag.setAttribute("type", errorType);
		}
		return tag;
	}
	
	private Element createTag(Element tag, String command, String[] parameters) {
		Element commandTag = this.output.createElement("command");
		commandTag.setAttribute("name", command);
		tag.appendChild(commandTag);
		Element paramTag = this.output.createElement("parameters");
		for (String parameter : parameters) {
			Element param = this.output.createElement("name");
			param.setAttribute("value", parameter);
			paramTag.appendChild(param);
		}
		tag.appendChild(paramTag);
		return tag; // remember: no output tag!
	}
	
	void print() {
		try {
			XmlUtility.print(this.output);
		} catch (TransformerException e) {
		}
	}
}
