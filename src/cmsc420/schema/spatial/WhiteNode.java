package cmsc420.schema.spatial;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.drawing.CanvasPlus;
import cmsc420.schema.City;

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
	private CanvasPlus canvas;

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
	public WhiteNode(Point2D.Float origin, float width, float height, CanvasPlus canvas) {
		this.origin = origin;
		this.width = width;
		this.height = height;
		this.canvas = canvas;
	}

	@Override
	public TreeNode add(City city) {
		if (this.canvas != null) {
			this.canvas.addPoint(city.getName(), city.x, city.y, Color.BLACK);
		}
		return new BlackNode(city, this.origin, this.width, this.height, this.canvas);
	}

	@Override
	public boolean contains(City city) {
		return false;
	}

	@Override
	public TreeNode remove(City city) {
		throw new UnsupportedOperationException(); // no city to remove
	}

	@Override
	public Element elementize(Document doc) {
		return doc.createElement("white");
	}

	@Override
	public void range(List<String> cities, int x, int y, int radius) {
		return;
	}

}
