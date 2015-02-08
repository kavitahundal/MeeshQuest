package cmsc420.meeshquest.part1;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cmsc420.xml.XmlUtility;

public class MeeshQuest {

	public static void main(String[] args) {

		/* XML documents for input and output */
		Document doc = null;
		Document results = null;

		/* initialize output XML document */
		try {
			results = XmlUtility.getDocumentBuilder().newDocument();

			/* validate input XML */
			try {
				doc = XmlUtility.validateNoNamespace(System.in);
				Node root = doc.getFirstChild(); // root tag from XML document
				NodeList commands = root.getChildNodes(); // command tags
				Element resultsRoot = results.createElement("results");
				results.appendChild(resultsRoot); // root tag of XML output

				/* retrieve spatial attributes */
				NamedNodeMap atts = root.getAttributes();
				Node width = atts.getNamedItem("spatialWidth");
				Node height = atts.getNamedItem("spatialHeight");
				int spatialWidth = Integer.parseInt(width.getNodeValue());
				int spatialHeight = Integer.parseInt(height.getNodeValue());

				/* process each command */
				for (int i = 0; i < commands.getLength(); i++) {
					Node commandNode = commands.item(i);
					if (commandNode instanceof Element) {
						String command = commandNode.getNodeName();
						NamedNodeMap params = commandNode.getAttributes();
						if (command.equals("createCity")) {
							// retrieve params
							// null check them all
							// undefined error if one is null?
						} else if (command.equals("deleteCity")) {
							//
						} else if (command.equals("clearAll")) {
							//
						} else if (command.equals("listCities")) {
							//
						} else if (command.equals("mapCity")) {
							//
						} else if (command.equals("unmapCity")) {
							//
						} else if (command.equals("printPRQuadtree")) {
							//
						} else if (command.equals("saveMap")) {
							//
						} else if (command.equals("rangeCities")) {
							//
						} else if (command.equals("nearestCity")) {
							//
						}
					}
				}
			} catch (SAXException | IOException | ParserConfigurationException e) {

				/* create fatal error tag */
				Element elt = results.createElement("fatalError");
				results.appendChild(elt);
			}

			/* print output XML document */
			try {
				XmlUtility.print(results);
			} catch (TransformerException e) {
				e.printStackTrace(); // should never occur
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace(); // should never occur
		}

	}
}
