package cmsc420.schema.spatial.PM;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.drawing.CanvasPlus;
import cmsc420.schema.City;
import cmsc420.schema.spatial.SpatialStructure;

public abstract class PMQuadTree implements SpatialStructure {

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

	@Override
	public void add(City element) {
		if (this.canvas != null) {
			this.canvas.addPoint(element.getName(), element.x, element.y, Color.BLACK);
		}
		if (!this.contains(element)) {
			this.size++;
			this.root = this.root.addCity(element);
		}
	}

	@Override
	public boolean contains(City element) {
		return this.root.contains(element);
	}

	@Override
	public void remove(City element) {
		throw new UnsupportedOperationException("remove to be implmented in part 3");
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

	@Override
	public void addRoad(City city1, City city2) {
		if (this.canvas != null) {
			this.canvas.addLine(city1.x, city1.y, city2.x, city2.y, Color.BLACK);
		}
		this.add(city1);
		this.add(city2);
		this.root = this.root.addRoad(city1, city2);
	}

	@Override
	public String getName() {
		return this.name;
	}

	public PMNode getRoot() {
		return this.root;
	}
	
	@Override
	public Element elementize(Document doc) {
		Element xmlRoot = doc.createElement("quadtree");
		int order = this instanceof PM1QuadTree? 1 : 3;
		xmlRoot.setAttribute("order", "" + order);
		xmlRoot.appendChild(this.root.elementize(doc)); // recursive call
		return xmlRoot;
	}
	
	@Override
	public Set<City> getCities() {
		Set<City> cities = new HashSet<>();
		this.root.getCities(cities);
		return cities;
	}

}
