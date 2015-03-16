package cmsc420.schema.spatial.PM;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.List;

import cmsc420.drawing.CanvasPlus;
import cmsc420.schema.City;
import cmsc420.schema.spatial.SpatialStructure;

public abstract class PMQuadTree implements SpatialStructure {
	
	private final int width;
	private final int height;
	protected Validator validator;
	private PMNode root;
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
		this.root = new PMWhiteNode(this.origin, this.width, this.height, this.canvas, this.validator);
	}

	@Override
	public void add(City element) {
		if (this.canvas != null) {
			this.canvas.addPoint(element.getName(), element.x, element.y, Color.BLACK);
		}
		this.root = this.root.addCity(element);
	}

	@Override
	public boolean contains(City element) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void remove(City element) {
		// TODO implement in part 3
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
		// TODO Auto-generated method stub
		return null;
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

}
