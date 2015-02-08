package cmsc420.schema.mediator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.xml.XmlUtility;

public class CommandWriter {

	private Document output;
	private Element root;
	
	CommandWriter() {
		try {
			this.output = XmlUtility.getDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
		}
	}
	
	void fatalError() {
		this.output.appendChild(output.createElement("fatalError"));
	}
	
	void createRoot() {
		this.root = this.output.createElement("results");
		this.output.appendChild(root); // root tag of XML output
	}
	
	void print() {
		try {
			XmlUtility.print(this.output);
		} catch (TransformerException e) {
		}
	}
}
