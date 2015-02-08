package cmsc420.schema.spatial;

import cmsc420.schema.City;

public class PRQuadTree implements SpatialStructure {
	
	private boolean hasBounds;
	private float width;
	private float height;
	
	public PRQuadTree() {
		//
	}
	
	public PRQuadTree(float width, float height) {
		this();
		this.setBounds(width, height);
	}

	@Override
	public void add(City city) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean contains(City city) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void remove(City city) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBounds(float width, float height) {
		this.hasBounds = true;
		this.width = width;
		this.height = height;
	}

}
