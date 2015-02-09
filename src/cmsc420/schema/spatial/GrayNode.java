package cmsc420.schema.spatial;

import java.awt.geom.Point2D;

import cmsc420.schema.City;

public class GrayNode implements TreeNode {

	private final Point2D.Float origin;
	private final float width;
	private final float height;
	private TreeNode[] quadrants;
	private int occupiedQuadrants;

	public GrayNode(Point2D.Float origin, float width, float height) {
		this.origin = origin;
		this.width = width;
		this.height = height;
		float dx = this.width / 2;
		float dy = this.height / 2;
		this.occupiedQuadrants = 0;
		this.quadrants = new TreeNode[4];
		this.quadrants[0] = new WhiteNode(this.origin, dx, dy);
		this.quadrants[1] = new WhiteNode(new Point2D.Float(this.origin.x + dx, this.origin.y), dx, dy);
		this.quadrants[2] = new WhiteNode(new Point2D.Float(this.origin.x, this.origin.y + dy), dx, dy);
		this.quadrants[3] = new WhiteNode(new Point2D.Float(this.origin.x + dx, this.origin.y + dy), dx, dy);
	}

	@Override
	public TreeNode add(City city) {
		int quadrant = this.getQuadrantIndex(city);
		if (this.quadrants[quadrant] instanceof WhiteNode) {
			this.occupiedQuadrants++;
		}
		this.quadrants[quadrant] = this.quadrants[quadrant].add(city);
		return this;
	}

	@Override
	public boolean contains(City city) {
		int quadrant = this.getQuadrantIndex(city);
		return this.quadrants[quadrant].contains(city);
	}
	
	private int getQuadrantIndex(City city) {
		float dx = city.x - this.origin.x;
		float dy = city.y - this.origin.y;
		if (dx < this.width) {
			if (dy < this.height) {
				return 0;
			} else {
				return 1;
			}
		} else {
			if (dy < this.height) {
				return 2;
			} else {
				return 3;
			}
		}
	}

	@Override
	public TreeNode remove(City city) {
		if (this.occupiedQuadrants == 1) {
			return new WhiteNode(this.origin, this.width, this.height);
		} else {
			int quadrant = this.getQuadrantIndex(city);
			this.quadrants[quadrant] = this.quadrants[quadrant].remove(city);
			this.occupiedQuadrants--;
			return this;
		}
	}
}
