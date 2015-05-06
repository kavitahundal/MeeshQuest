package cmsc420.schema.spatial;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cmsc420.schema.Metropole;

/**
 * A TreeNode that represents an inner node that has four children that
 * represents the four quadrants within the region of this node.
 *
 * @author Andrew Liu
 *
 */
public class GrayNode implements TreeNode {

	private final Point2D.Float origin;
	private final float width;
	private final float height;
	private TreeNode[] quadrants;

	/**
	 * Constructor.
	 * 
	 * @param origin
	 *            the lowest value corner of the node's region
	 * @param width
	 *            the width of this node's region
	 * @param height
	 *            the height of this node's region
	 * @param canvas
	 *            the drawing canvas of the structure
	 */
	public GrayNode(Point2D.Float origin, float width, float height) {
		this.origin = origin;
		this.width = width;
		this.height = height;
		float dx = this.width / 2;
		float dy = this.height / 2;
		this.quadrants = new TreeNode[4];

		/* setting boundaries for the quadrants */
		this.quadrants[0] = new WhiteNode(new Point2D.Float(this.origin.x, this.origin.y + dy), dx, dy);
		this.quadrants[1] = new WhiteNode(new Point2D.Float(this.origin.x + dx, this.origin.y + dy), dx, dy);
		this.quadrants[2] = new WhiteNode(this.origin, dx, dy);
		this.quadrants[3] = new WhiteNode(new Point2D.Float(this.origin.x + dx, this.origin.y), dx, dy);
	}

	@Override
	public TreeNode add(Metropole metropole) {
		int quadrant = this.getQuadrantIndex(metropole);
		this.quadrants[quadrant] = this.quadrants[quadrant].add(metropole);
		return this;
	}

	@Override
	public boolean contains(Metropole metropole) {
		int quadrant = this.getQuadrantIndex(metropole);
		return this.quadrants[quadrant].contains(metropole);
	}

	private int getQuadrantIndex(Point2D.Float metropole) {
		Point2D.Float center = this.location();
		if (metropole.x >= center.x) {
			if (metropole.y >= center.y) {
				return 1;
			} else {
				return 3;
			}
		} else {
			if (metropole.y >= center.y) {
				return 0;
			} else {
				return 2;
			}
		}
	}

	@Override
	public TreeNode remove(Metropole metropole) {
		int quadrant = this.getQuadrantIndex(metropole);
		this.quadrants[quadrant] = this.quadrants[quadrant].remove(metropole);
		TreeNode quad = null;
		int occupiedQuadrants = 0;

		/* find number of non-white quadrants */
		for (TreeNode q : this.quadrants) {
			if (!(q instanceof WhiteNode)) {
				occupiedQuadrants++;
				quad = q; // get instance of occupied quadrant
			}
		}

		/* check if this (gray) node needs to change */
		if (occupiedQuadrants < 2 && !(quad instanceof GrayNode)) {
			/* check if zero or one city is left */
			if (occupiedQuadrants == 0) {
				return new WhiteNode(this.origin, this.width, this.height);
			} else {
				return new BlackNode(quad.getMetropole(null), this.origin, this.width, this.height);
			}
		} else {
			return this;
		}
	}

	/**
	 * Gets the spatial center of the node's region.
	 * 
	 * @return the center of the region
	 */
	public Point2D.Float location() {
		return new Point2D.Float(this.origin.x + this.width / 2, this.origin.y + this.height / 2);
	}

	@Override
	public void range(List<Metropole> metropoles, int x, int y, int radius) {
		for (int i = 0; i < this.quadrants.length; i++) {
			this.quadrants[i].range(metropoles, x, y, radius);
		}
	}

	/**
	 * Gets the set of children nodes of this node.
	 * 
	 * @return the children nodes
	 */
	public Set<TreeNode> getChildren() {
		Set<TreeNode> quads = new HashSet<>();
		for (TreeNode q : this.quadrants) {
			quads.add(q);
		}
		return quads;
	}

	@Override
	public Metropole getMetropole(Float loc) {
		int quadrant = this.getQuadrantIndex(loc);
		return this.quadrants[quadrant].getMetropole(loc);
	}
	
	/*
	 * metropole extends city
	 * we will only add metropoles
	 */
}
