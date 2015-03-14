package cmsc420.schema.spatial.PM;

import java.awt.geom.Point2D;
import java.util.List;

import cmsc420.drawing.CanvasPlus;
import cmsc420.schema.City;
import cmsc420.schema.DataStructure;
import cmsc420.schema.spatial.SpatialStructure;
import cmsc420.schema.spatial.TreeNode;

public abstract class PMQuadTree implements SpatialStructure {
	
	private final int width;
	private final int height;
	private Validator validator;
	private PMNode root;
	private final Point2D.Float origin;
	private int size;
	private CanvasPlus canvas;
	private String name;

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
	public DataStructure<City> reset() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getSpatialWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getSpatialHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> range(int x, int y, int radius) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveMap(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCircle(int x, int y, int radius) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeCircle(int x, int y, int radius) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeCanvas() {
		// TODO Auto-generated method stub
		
	}

}
