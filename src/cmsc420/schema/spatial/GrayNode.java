package cmsc420.schema.spatial;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.drawing.CanvasPlus;

/**
 * A TreeNode that represents an inner node that has four children that
 * represents the four quadrants within the region of this node.
 *
 * @author Andrew Liu
 *
 */
public class GrayNode<T extends Point2D.Float> implements TreeNode<T> {

	private TreeNode[] quadrants;

	/**
	 * Constructor.
	 */
	public GrayNode(Point2D.Float origin, float width, float height, CanvasPlus canvas) {
		this.quadrants = new TreeNode[4];
	}

	@Override
	public TreeNode<T> add(T element) {
		int quadrant = this.getQuadrantIndex(element);
		this.quadrants[quadrant] = this.quadrants[quadrant].add(element);
		return this;
	}

	@Override
	public boolean contains(T element) {
		int quadrant = this.getQuadrantIndex(element);
		return this.quadrants[quadrant].contains(element);
	}

	private int getQuadrantIndex(T element) {
		Point2D.Float center = this.location();
		if (element.x >= center.x) {
			if (element.y >= center.y) {
				return 1;
			} else {
				return 3;
			}
		} else {
			if (element.y >= center.y) {
				return 0;
			} else {
				return 2;
			}
		}
	}

	@Override
	public TreeNode<T> remove(T element) {
		int quadrant = this.getQuadrantIndex(element);
		this.quadrants[quadrant] = this.quadrants[quadrant].remove(element);
		TreeNode<T> quad = null;
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

			/* check if zero or one element is left */
			if (occupiedQuadrants == 0) {
				return WhiteNode.SINGLETON;
			} else {
				return new BlackNode<T>(((BlackNode<T>) quad).getElement());
			}
		} else {
			return this;
		}
	}

}
