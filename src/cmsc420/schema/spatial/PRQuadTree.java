package cmsc420.schema.spatial;

import cmsc420.schema.City;

public class PRQuadTree implements SpatialStructure {
	
	private TreeNode root;
	private float width;
	private float height;
	
	PRQuadTree(float width, float height) {
		this.root = new WhiteNode();
		this.width = width;
		this.height = height;
	}

	@Override
	public void add(City city) {
		//
	}

	@Override
	public boolean contains(City city) {
		return false;
	}

	@Override
	public void remove(City city) {
		//
	}

}
