package cmsc420.schema.spatial;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.drawing.CanvasPlus;
import cmsc420.schema.element;

/**
 * A TreeNode that acts as a leaf that does not contain any cities.
 * 
 * @author Andrew Liu
 *
 */
public class WhiteNode<T> implements TreeNode<T> {

	public static final WhiteNode SINGLETON = new WhiteNode();
	
	private WhiteNode() {
	}

	@Override
	public TreeNode<T> add(T element) {
		return new BlackNode<T>(element);
	}

	@Override
	public boolean contains(T element) {
		return false;
	}

	@Override
	public TreeNode<T> remove(T element) {
		throw new UnsupportedOperationException(); // no element to remove
	}

}
