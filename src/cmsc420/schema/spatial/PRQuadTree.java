package cmsc420.schema.spatial;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import cmsc420.schema.Metropole;

/**
 * A PR Quadtree data structure.
 * 
 * @author Andrew Liu
 *
 */
public class PRQuadTree {

	private TreeNode root;
	private final Point2D.Float origin;
	private final float width;
	private final float height;
	private int size;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            the name of the canvas
	 * @param width
	 *            the width of the tree
	 * @param height
	 *            the height of the tree
	 */
	public PRQuadTree(float width, float height) {
		this.origin = new Point2D.Float();
		this.width = width;
		this.height = height;
		this.size = 0;
		this.root = new WhiteNode(this.origin, this.width, this.height);
	}

	
	public void add(Metropole metropole) {
		this.root = this.root.add(metropole);
		this.size++;
	}

	
	public boolean contains(Point2D.Float loc) {
		return this.root.contains(loc);
	}

	public void remove(Metropole metropole) { // assuming contains is true
		this.size--;
		if (this.root instanceof BlackNode) { // empty the tree
			this.root = new WhiteNode(this.origin, this.width, this.height);
		} else {
			this.root = this.root.remove(metropole);
		}
	}

	
	public PRQuadTree reset() {
		return new PRQuadTree(this.width, this.height);
	}
	
	public int size() {
		return this.size;
	}

	
	public float getSpatialWidth() {
		return this.width;
	}

	
	public float getSpatialHeight() {
		return this.height;
	}
	
	public List<Metropole> range(int x, int y, int radius) {
		List<Metropole> metropoles = new LinkedList<>();
		this.root.range(metropoles, x, y, radius); // recursive call
		return metropoles;
	}

	/**
	 * Gets the root of this tree.
	 * 
	 * @return the root node
	 */
	public TreeNode getRoot() {
		return this.root;
	}
	
	public Metropole getMetropole(Point2D.Float loc) {
		return this.root.getMetropole(loc);
	}
	
	public Metropole getMetorpole(int remoteX, int remoteY) {
		return this.getMetropole(new Point2D.Float(remoteX, remoteY));
	}

}
