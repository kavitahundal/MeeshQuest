package cmsc420.schema.spatial.PM;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.drawing.CanvasPlus;
import cmsc420.schema.Airport;
import cmsc420.schema.City;

public abstract class PMQuadTree {
	
	private final int width;
	private final int height;
	protected Validator validator;
	protected PMNode root;
	private final Point2D.Float origin;
	private int size;
	private CanvasPlus canvas;
	private String name;

	public PMQuadTree(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.origin = new Point2D.Float();
		this.size = 0;
		this.canvas = new CanvasPlus(name, (int) width, (int) height);
		this.canvas.addRectangle(0, 0, width, height, Color.BLACK, false);
		this.validator = this.getValidator();
		this.root = new PMWhiteNode(this.origin, this.width, this.height, this.canvas, this.validator);
	}

	protected abstract Validator getValidator();

	public void add(Point2D.Float element) {
		if (this.canvas != null) {
			String name;
			if (element instanceof City) {
				name = ((City) element).getName();
			} else {
				name = ((Airport) element).getName();
			}
			this.canvas.addPoint(name, element.x, element.y, Color.BLACK);
		}
		if (!this.contains(element)) {
			this.size++;
			this.root = this.root.addVertex(element);
		}
	}

	public boolean contains(Point2D.Float element) {
		return this.root.contains(element);
	}

	public void remove(Point2D.Float element) {
		throw new UnsupportedOperationException("remove to be implmented in part 3");
	}

	public int size() {
		return this.size;
	}

	public float getSpatialWidth() {
		return this.width;
	}

	public float getSpatialHeight() {
		return this.height;
	}

	public void saveMap(String name) {
		try {
			this.canvas.save(name);
		} catch (IOException e) {
		}
	}

	public void addCircle(int x, int y, int radius) {
		this.canvas.addCircle(x, y, radius, Color.BLUE, false);
	}

	public void removeCircle(int x, int y, int radius) {
		this.canvas.removeCircle(x, y, radius, Color.BLUE, false);
	}

	public void removeCanvas() {
		this.canvas.dispose();
		this.canvas = null;
		this.root = null;
	}

	public void addRoad(City city1, City city2) {
		if (this.canvas != null) {
			this.canvas.addLine(city1.x, city1.y, city2.x, city2.y, Color.BLACK);
		}
		this.add(city1);
		this.add(city2);
		this.root = this.root.addRoad(city1, city2);
	}

	public String getName() {
		return this.name;
	}

	public PMNode getRoot() {
		return this.root;
	}
	
	public Element elementize(Document doc) {
		Element xmlRoot = doc.createElement("quadtree");
		int order = this instanceof PM1QuadTree? 1 : 3;
		xmlRoot.setAttribute("order", "" + order);
		xmlRoot.appendChild(this.root.elementize(doc)); // recursive call
		return xmlRoot;
	}
	
	public abstract PMQuadTree reset();

}
