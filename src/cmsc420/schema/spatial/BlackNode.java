package cmsc420.schema.spatial;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.drawing.CanvasPlus;
import cmsc420.schema.City;

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
	private City city;
	private CanvasPlus canvas;

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
	public BlackNode(City city, Point2D.Float origin, float width, float height, CanvasPlus canvas) {
		this.origin = origin;
		this.width = width;
		this.height = height;
		this.city = city;
		this.canvas = canvas;
	}

	@Override
	public TreeNode add(City city) {
		GrayNode node = new GrayNode(this.origin, this.width, this.height, this.canvas); // partition
		if (this.canvas != null) {
			this.canvas.addLine(this.origin.x, this.origin.y + this.height / 2, this.origin.x + this.width,
					this.origin.y + this.height / 2, Color.BLACK);
			this.canvas.addLine(this.origin.x + this.width / 2, this.origin.y, this.origin.x + this.width / 2,
					this.origin.y + this.height, Color.BLACK);
			this.canvas.removePoint(this.city.getName(), this.city.x, this.city.y, Color.BLACK);
		}
		node.add(this.city); // add the old node that was removed
		node.add(city); // add the new node
		return node;
	}

	@Override
	public boolean contains(City city) {
		return city.equals(this.city);
	}

	@Override
	public TreeNode remove(City city) {
		if (this.canvas != null) {
			this.canvas.removePoint(city.getName(), city.x, city.y, Color.BLACK);
		}
		return new WhiteNode(this.origin, this.width, this.height, this.canvas);
	}

	/**
	 * Gets the city that this node contains.
	 * 
	 * @return the node's city
	 */
	public City getCity() {
		return this.city;
	}

	@Override
	public Element elementize(Document doc) {
		Element ele = doc.createElement("black");
		ele.setAttribute("name", this.city.getName());
		ele.setAttribute("x", Integer.toString((int) this.city.x));
		ele.setAttribute("y", Integer.toString((int) this.city.y));
		return ele;
	}

	@Override
	public void range(List<String> cities, int x, int y, int radius) {
		if (this.city.distance(x, y) <= radius) {
			cities.add(this.city.getName());
		}
	}

}
