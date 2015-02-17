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

/**
 * A PR Quadtree data structure.
 * 
 * @author Andrew Liu
 *
 */
public class PRQuadTree implements SpatialStructure {

	private TreeNode root;
	private final Point2D.Float origin;
	private final float width;
	private final float height;
	private int size;
	private CanvasPlus canvas;
	private String name;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            the name of the canvas
	 * @param width
	 *            the width of the tree
	 * @param height
	 *            the height of the tree
	 */
	public PRQuadTree(String name, float width, float height) {
		this.name = name;
		this.origin = new Point2D.Float();
		this.width = width;
		this.height = height;
		this.size = 0;
		this.canvas = new CanvasPlus(name, (int) width, (int) height);
		this.canvas.addRectangle(0, 0, width, height, Color.BLACK, false);
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
		if (this.root instanceof BlackNode) { // empty the tree
			this.root = new WhiteNode(this.origin, this.width, this.height, this.canvas);
		} else {
			this.root = this.root.remove(city);
		}
	}

	@Override
	public DataStructure<City> reset() {
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

	/**
	 * Returns the tree as an XML element.
	 * 
	 * @param doc
	 *            the XML document to generate elements
	 * @return the as an XML element
	 */
	public Element elementize(Document doc) {
		Element xmlRoot = doc.createElement("quadtree");
		xmlRoot.appendChild(this.root.elementize(doc)); // recursive call
		return xmlRoot;
	}

	@Override
	public List<String> range(int x, int y, int radius) {
		List<String> cities = new LinkedList<>();
		this.root.range(cities, x, y, radius); // recursive call
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
		this.canvas.dispose();
		this.canvas = null;
		this.root = null;
	}

	/**
	 * Gets the root of this tree.
	 * 
	 * @return the root node
	 */
	public TreeNode getRoot() {
		return this.root;
	}

}
