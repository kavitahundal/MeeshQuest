package cmsc420.schema.spatial;

import java.awt.geom.Point2D;

import cmsc420.schema.City;

public class BlackNode implements TreeNode {

	private final Point2D.Float origin;
	private final float width;
	private final float height;
	private City city;
	
	public BlackNode(City city, Point2D.Float origin, float width, float height) {
		this.origin = origin;
		this.width = width;
		this.height = height;
		this.city = city;
	}

	@Override
	public TreeNode add(City city) {
		GrayNode node = new GrayNode(this.origin, this.width, this.height);
		node.add(this.city);
		node.add(city);
		return node;
	}

	@Override
	public boolean contains(City city) {
		return city.equals(this.city);
	}
	
}
