package cmsc420.schema.spatial;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.drawing.CanvasPlus;
import cmsc420.schema.City;
import cmsc420.schema.DataStructure;

public class PRQuadTree implements SpatialStructure {

	private TreeNode root;
	private final Point2D.Float origin;
	private final float width;
	private final float height;
	private int size;
	private CanvasPlus canvas;
	private String name;

	public PRQuadTree(String name, float width, float height) {
		this.name = name;
		this.origin = new Point2D.Float();
		this.width = width;
		this.height = height;
		this.size = 0;
		this.canvas = new CanvasPlus("MeeshQuest", (int) width, (int) height);
		this.canvas.addRectangle(0, 0, width, height, Color.BLACK, false);
//		this.canvas = canvas;
		this.root = new WhiteNode(this.origin, this.width, this.height, this.canvas);
	}

	@Override
	public void add(City city) {
		this.root = this.root.add(city);
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
			this.root = new WhiteNode(this.origin, this.width, this.height, this.canvas);
		} else {
			this.root.remove(city);
		}
		// TODO canvas remove (in if block)
	}

	@Override
	public DataStructure<City> reset() {
		// TODO make sure getName() return title (aka title == name)
		return new PRQuadTree(this.name, this.width, this.height);
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

	@Override
	public List<String> range(int x, int y, int radius) {
		List<String> cities = new LinkedList<>();
		this.root.range(cities, x, y, radius);
		return cities;
	}

	@Override
	public void saveMap(String name) {
		try {
			this.canvas.save(name);
		} catch (IOException e) {
		}
	}

	@Override
	public void addCircle(int x, int y, int radius) {
		this.canvas.addCircle(x, y, radius, Color.BLUE, false);
	}

	@Override
	public void removeCircle(int x, int y, int radius) {
		this.canvas.removeCircle(x, y, radius, Color.BLUE, false);
	}

	@Override
	public void removeCanvas() {
		// TODO Auto-generated method stub
		this.canvas.dispose();
		this.canvas = null;
		this.root = null;
	}

	// public void print() {
	// Document results = null;
	//
	// try {
	// results = XmlUtility.getDocumentBuilder().newDocument();
	// } catch (ParserConfigurationException e1) {
	// e1.printStackTrace(); // should never have an error
	// }
	//
	// // add nodes
	// results.appendChild(this.elementize(results));
	//
	// try {
	// XmlUtility.print(results);
	// } catch (TransformerException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	// TODO need method to check if last added node was gray

}
