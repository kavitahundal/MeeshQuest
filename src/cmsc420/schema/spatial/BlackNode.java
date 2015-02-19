package cmsc420.schema.spatial;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.drawing.CanvasPlus;
import cmsc420.schema.element;

/**
 * A TreeNode that acts as a leaf that holds one element.
 * 
 * @author Andrew Liu
 *
 */
public class BlackNode<T> implements TreeNode<T> {

	private T element;

	/**
	 * Constructor.
	 * 
	 * @param element
	 *            the element stored within this node
	 */
	public BlackNode(T element) {
		this.element = element;
	}
	
	public T getElement() {
		return this.element;
	}

	@Override
	public TreeNode add(T element) {
		GrayNode node = new GrayNode(); // partition
		node.add(this.element); // add the old node that was removed
		node.add(element); // add the new node
		return node;
	}

	@Override
	public boolean contains(T element) {
		return element.equals(this.element);
	}

	@Override
	public TreeNode<T> remove(T element) {
		return WhiteNode.SINGLETON;
	}

}
