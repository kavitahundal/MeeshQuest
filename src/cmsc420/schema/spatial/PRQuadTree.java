package cmsc420.schema.spatial;

import cmsc420.schema.City;

public class PRQuadTree implements SpatialStructure {
	
	private TreeNode root;
	private boolean hasBounds;
	private float width;
	private float height;
	
	public PRQuadTree() {
		this.root = new WhiteNode();
	}
	
	public PRQuadTree(float width, float height) {
		this();
		this.setBounds(width, height);
	}

	@Override
	public void add(City city) {
		if (!hasBounds) {
			return;
		}
		//
	}

	@Override
	public boolean contains(City city) {
		if (!hasBounds) {
			return false;
		}
		return false;
	}

	@Override
	public void remove(City city) {
		if (!hasBounds) {
			return;
		}
		//
	}

	@Override
	public void setBounds(float width, float height) {
		this.hasBounds = true;
		this.width = width;
		this.height = height;
	}

}
