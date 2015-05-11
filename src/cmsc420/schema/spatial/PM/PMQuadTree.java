package cmsc420.schema.spatial.PM;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.drawing.CanvasPlus;
import cmsc420.exceptions.PartitionException;
import cmsc420.geom.Inclusive2DIntersectionVerifier;
import cmsc420.schema.Airport;
import cmsc420.schema.City;
import cmsc420.schema.CityCoordinateComparator;
import cmsc420.schema.adjacencylist.AdjacencyList;

public abstract class PMQuadTree {

	private final int width;
	private final int height;
	protected Validator validator;
	protected PMNode root;
	private final Point2D.Float origin;
	private int size;
	private CanvasPlus canvas;
	private String name;
	private AdjacencyList<City> roads;

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
		this.roads = new AdjacencyList<City>(new CityCoordinateComparator());
	}

	protected abstract Validator getValidator();

	public void add(Point2D.Float element) throws PartitionException {
		if (!this.contains(element)) {
			this.root = this.root.addVertex(element);
			this.size++;
			if (this.canvas != null) {
				String name;
				if (element instanceof City) {
					name = ((City) element).getName();
				} else {
					name = ((Airport) element).getName();
				}
				this.canvas.addPoint(name, element.x, element.y, Color.BLACK);
			}
		}
	}

	public boolean contains(Point2D.Float element) {
		return this.root.contains(element);
	}

	public void remove(Point2D.Float element) {
		this.root = this.root.remove(element);
		// remove corresponding roads too!
		if (element instanceof City) {
			City source = (City) element;
			for (City sink : this.roads.neighbors(source)) {
				this.removeRoad(source, sink);
			}
		}
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

	public void addRoad(City city1, City city2) throws PartitionException {
		this.add(city1);
		this.add(city2);
		this.root = this.root.addRoad(city1, city2);
		this.roads.addUndirectedEdge(city1, city2);
		if (this.canvas != null) {
			this.canvas.addLine(city1.x, city1.y, city2.x, city2.y, Color.BLACK);
		}
	}

	public void removeRoad(City city1, City city2) {
		this.root = this.root.removeRoad(city1, city2);
		this.roads.removeUndirectedEdge(city1, city2);
	}

	public String getName() {
		return this.name;
	}

	public PMNode getRoot() {
		return this.root;
	}

	public Element elementize(Document doc) {
		Element xmlRoot = doc.createElement("quadtree");
		int order = this instanceof PM1QuadTree ? 1 : 3;
		xmlRoot.setAttribute("order", "" + order);
		xmlRoot.appendChild(this.root.elementize(doc)); // recursive call
		return xmlRoot;
	}

	public abstract PMQuadTree reset();

	public boolean validAddVertex(Point2D.Float vertex) {
		// check if this point and neighboring points are empty
		int x = (int) vertex.x;
		int y = (int) vertex.y;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (this.contains(new Point2D.Float(x + i, y + j))) {
					return false;
				}
			}
		}

		// make sure this point intersects with no other line
		for (Object[] road : this.roads) {
			Line2D edge = new Line2D.Float((Point2D.Float) road[0], (Point2D.Float) road[1]);
			if (Inclusive2DIntersectionVerifier.intersects(vertex, edge)) {
				return false;
			}
		}
		return true;
	}

	public boolean validAddEdge(City city1, City city2) {
		Line2D edge = new Line2D.Float(city1, city2);
		// make sure this point doesn't intersect any other lines (except at
		// endpoint)
		for (Object [] road : this.roads) {
			Line2D otherEdge = new Line2D.Float((Point2D.Float) road[0], (Point2D.Float) road[1]);
			if (Inclusive2DIntersectionVerifier.intersects(edge, otherEdge)
					&& !Inclusive2DIntersectionVerifier.intersects(city1, edge)
					&& !Inclusive2DIntersectionVerifier.intersects(city2, edge)) {
				return false;
			}
		}
		// what if PM1 and point is really really close to edge?
		return true;
	}

}
