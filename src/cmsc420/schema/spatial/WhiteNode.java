package cmsc420.schema.spatial;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.List;

import cmsc420.schema.Metropole;

/**
 * A TreeNode that acts as a leaf that does not contain any cities.
 * 
 * @author Andrew Liu
 *
 */
public class WhiteNode implements TreeNode {

	private final Point2D.Float origin;
	private final float width;
	private final float height;

	/**
	 * Constructor.
	 * 
	 * @param origin
	 *            the lowest value corner of the node's region
	 * @param width
	 *            width of this node
	 * @param height
	 *            height of this node
	 * @param canvas
	 *            the drawing canvas of the structure
	 */
	public WhiteNode(Point2D.Float origin, float width, float height) {
		this.origin = origin;
		this.width = width;
		this.height = height;
	}

	@Override
	public TreeNode add(Metropole metropole) {
		return new BlackNode(metropole, this.origin, this.width, this.height);
	}

	@Override
	public boolean contains(Point2D.Float loc) {
		return false;
	}

	@Override
	public TreeNode remove(Metropole metropole) {
		throw new UnsupportedOperationException(); // no city to remove
	}

	@Override
	public void range(List<Metropole> metropoles, int x, int y, int radius) {
		return;
	}

	@Override
	public Metropole getMetropole(Float loc) {
		throw new UnsupportedOperationException();
	}

}
