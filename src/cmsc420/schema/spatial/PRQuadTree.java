package cmsc420.schema.spatial;

import cmsc420.schema.City;
import cmsc420.schema.DataStructure;

public class PRQuadTree implements SpatialStructure {
	
	private TreeNode root;
	private int width;
	private int height;
	private int size;
	
	PRQuadTree(int width, int height) {
		this.root = new WhiteNode();
		this.width = width;
		this.height = height;
		this.size = 0;
	}

	@Override
	public void add(City city) {
		//
		this.root = this.root.add(city);
		this.size++;
	}

	@Override
	public boolean contains(City city) {
		return this.root.contains(city);
	}

	@Override
	public void remove(City city) {
		//
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
	public int getSpatialWidth() {
		return this.width;
	}

	@Override
	public int getSpatialHeight() {
		return this.height;
	}

}
