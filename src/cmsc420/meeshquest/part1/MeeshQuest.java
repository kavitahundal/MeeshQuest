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

import cmsc420.schema.CityColor;
import cmsc420.schema.SortType;
import cmsc420.xml.XmlUtility;

public class MeeshQuest {

	public static void main(String[] args) {

		/* XML documents for input and output */
		Document doc = null;
		Document results = null;

		/* initialize output XML document */
		try {
			results = XmlUtility.getDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace(); // should never occur
		}
		/* validate input XML */
		try {
			doc = XmlUtility.validateNoNamespace(System.in);
			Node root = doc.getFirstChild(); // root tag from XML document
			NodeList commands = root.getChildNodes(); // command tags
			Element resultsRoot = results.createElement("results");
			results.appendChild(resultsRoot); // root tag of XML output

			/* retrieve spatial attributes */
			NamedNodeMap attrs = root.getAttributes();
			int spatialWidth = Integer.parseInt(attrs.getNamedItem("spatialWidth").getNodeValue());
			int spatialHeight = Integer.parseInt(attrs.getNamedItem("spatialHeight").getNodeValue());

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

	}
}
