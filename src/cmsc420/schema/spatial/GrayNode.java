package cmsc420.schema.spatial;

import java.awt.geom.Point2D;

import cmsc420.schema.City;

public class GrayNode implements TreeNode {

	private final Point2D.Float origin;
	private final float width;
	private final float height;
	private TreeNode quadrant1;
	private TreeNode quadrant2;
	private TreeNode quadrant3;
	private TreeNode quadrant4;
	
	public GrayNode(Point2D.Float origin, float width, float height) {
		this.origin = origin;
		this.width = width;
		this.height = height;
		float dx = this.width / 2;
		float dy = this.height / 2;
		Point2D.Float q2Origin = new Point2D.Float(this.origin.x + dx, this.origin.y);
		Point2D.Float q3Origin = new Point2D.Float(this.origin.x, this.origin.y + dy);
		Point2D.Float q4Origin = new Point2D.Float(this.origin.x + dx, this.origin.y + dy);
		this.quadrant1 = new WhiteNode(this.origin, dx, dy);
		this.quadrant2 = new WhiteNode(q2Origin, dx, dy);
		this.quadrant3 = new WhiteNode(q3Origin, dx, dy);
		this.quadrant4 = new WhiteNode(q4Origin, dx, dy);
	}

	@Override
	public TreeNode add(City city) {
		// r = findRegion()
		// replace r with r.add(city)
		return this;
	}

	@Override
	public boolean contains(City city) {
		// r = getRegion()
		// return r.contains(city)
		return false;
	}
}
