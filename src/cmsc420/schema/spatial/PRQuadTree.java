package cmsc420.schema.spatial;

import java.awt.geom.Point2D;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.schema.City;
import cmsc420.schema.DataStructure;
import cmsc420.xml.XmlUtility;

public class PRQuadTree implements SpatialStructure {

	private TreeNode root;
	private final Point2D.Float origin;
	private final float width;
	private final float height;
	private int size;

	PRQuadTree(float width, float height) {
		this.origin = new Point2D.Float();
		this.width = width;
		this.height = height;
		this.size = 0;
		this.root = new WhiteNode(this.origin, this.width, this.height);
	}

	@Override
	public void add(City city) {
		// System.out.println(this.origin);
		// System.out.println(this.width);
		// System.out.println(this.height);
		// System.out.println(this.size);
		// System.out.println("yoyoyoyoyoyoyoyoyoyoy");
		this.print();
		this.root = this.root.add(city);
		// System.out.println("hihihihihihihihihhi");
		this.size++;
	}

	@Override
	public boolean contains(City city) {
		return this.root.contains(city);
	}

	@Override
	public void remove(City city) {
		// precondition: contains city
		if (this.root instanceof BlackNode) {
			this.root = new WhiteNode(this.origin, this.width, this.height);
		} else {
			this.root.remove(city);
		}
	}

	@Override
	public DataStructure<City> reset() {
		return new PRQuadTree(this.width, this.height);
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public float getSpatialWidth() {
		return this.width;
	}

	@Override
	public float getSpatialHeight() {
		return this.height;
	}

	public Element elementize(Document doc) {
		Element xmlRoot = doc.createElement("quadtree");
		xmlRoot.appendChild(this.root.elementize(doc));
		return xmlRoot;
	}

	public void print() {
		Document results = null;
        
        try {
            results = XmlUtility.getDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace(); // should never have an error
        }
        
        // add nodes
        results.appendChild(this.elementize(results));
        
        try {
            XmlUtility.print(results);
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}

	// TODO need method to check if last added node was gray

}
