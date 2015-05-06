package cmsc420.schema.spatial;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.List;

import cmsc420.schema.Metropole;

/**
 * A TreeNode that acts as a leaf that holds one city.
 * 
 * @author Andrew Liu
 *
 */
public class BlackNode implements TreeNode {

	private final Point2D.Float origin;
	private final float width;
	private final float height;
	private Metropole metropole;

	/**
	 * Constructor.
	 * 
	 * @param city
	 *            the city stored within this node
	 * @param origin
	 *            the lowest value corner of the node's region
	 * @param width
	 *            width of this node
	 * @param height
	 *            height of this node
	 * @param canvas
	 *            the drawing canvas of the structure
	 */
	public BlackNode(Metropole metropole, Point2D.Float origin, float width, float height) {
		this.origin = origin;
		this.width = width;
		this.height = height;
		this.metropole = metropole;
	}

	@Override
	public TreeNode add(Metropole metropole) {
		GrayNode node = new GrayNode(this.origin, this.width, this.height); // partition
		node.add(this.metropole); // add the old node that was removed
		node.add(metropole); // add the new node
		return node;
	}

	@Override
	public boolean contains(Point2D.Float loc) {
		return true;
	}

	@Override
	public TreeNode remove(Metropole metropole) {
		return new WhiteNode(this.origin, this.width, this.height);
	}


	@Override
	public Metropole getMetropole(Float loc) {
		return this.metropole;
	}

	@Override
	public void range(List<Metropole> metropoles, int x, int y, int radius) {
		if (this.metropole.distance(x, y) <= radius) {
			metropoles.add(this.metropole);
		}
	}

}
