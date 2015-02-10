package cmsc420.schema.spatial;

import java.awt.Color;
import java.awt.geom.Point2D;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.drawing.CanvasPlus;
import cmsc420.schema.City;

public class BlackNode implements TreeNode {

	private final Point2D.Float origin;
	private final float width;
	private final float height;
	private City city;
	private CanvasPlus canvas;

	public BlackNode(City city, Point2D.Float origin, float width, float height, CanvasPlus canvas) {
		this.origin = origin;
		this.width = width;
		this.height = height;
		this.city = city;
		this.canvas = canvas;
	}

	@Override
	public TreeNode add(City city) {
		// TODO warning: does adding the same point twice cause problems?
		GrayNode node = new GrayNode(this.origin, this.width, this.height, this.canvas);
		if (this.canvas != null) {
			this.canvas.addLine(this.origin.x, this.origin.y + this.height / 2, this.origin.x + this.width,
					this.origin.y + this.height / 2, Color.BLACK);
			this.canvas.addLine(this.origin.x + this.width / 2, this.origin.y, this.origin.x + this.width / 2,
					this.origin.y + this.height, Color.BLACK);
			this.canvas.removePoint(this.city.getName(), this.city.x, this.city.y, Color.BLACK);
			// remove this.city because the next command will add it again
		}
		node.add(this.city);
		node.add(city);
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

}
