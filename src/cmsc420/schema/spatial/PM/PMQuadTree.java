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
		// TODO initialize root
		this.origin = new Point2D.Float();
		this.size = 0;
		this.canvas = new CanvasPlus(name, (int) width, (int) height);
		this.canvas.addRectangle(0, 0, width, height, Color.BLACK, false);
	}

	@Override
	public void add(City element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean contains(City element) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void remove(City element) {
		// TODO Auto-generated method stub
		
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
		// add city1
		// add city 2
		// add the line
	}
	
	@Override
	public String getName() {
		return this.name;
	}

}
