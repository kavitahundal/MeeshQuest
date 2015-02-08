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

	private static final String ERROR_TAG = "fatalError";
	private static final String WIDTH_TAG = "spatialWidth";
	private static final String HEIGHT_TAG = "spatialHeight";

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
				
				/* retrieve spatial attributes */
				NamedNodeMap atts = root.getAttributes();
				String width = atts.getNamedItem(WIDTH_TAG).getNodeValue();
				String height = atts.getNamedItem(HEIGHT_TAG).getNodeValue();
				int spatialWidth = Integer.parseInt(width);
				int spatialHeight = Integer.parseInt(height);

				/* process each command */
				for (int i = 0; i < commands.getLength(); i++) {
					Node command = commands.item(i);
					if (command instanceof Element) {
						// read the data stuff
					}
				}
			} catch (SAXException | IOException | ParserConfigurationException
					| NumberFormatException e) {

				/* create fatal error tag */
				Element elt = results.createElement(ERROR_TAG);
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
