package cmsc420.schema.spatial;

import java.awt.geom.Point2D;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.schema.City;

public class BlackNode implements TreeNode {

	private final Point2D.Float origin;
	private final float width;
	private final float height;
	private City city;
	
	public BlackNode(City city, Point2D.Float origin, float width, float height) {
		this.origin = origin;
		this.width = width;
		this.height = height;
		this.city = city;
	}

	@Override
	public TreeNode add(City city) {
		GrayNode node = new GrayNode(this.origin, this.width, this.height);
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
		return new WhiteNode(this.origin, this.width, this.height);
	}
	
	public City getCity() {
		return this.city;
	}

	@Override
	public Element elementize(Document doc) {
		Element ele = doc.createElement("black");
		ele.setAttribute("name", this.city.getName());
		ele.setAttribute("x", Float.toString(this.city.x));
		ele.setAttribute("y", Float.toString(this.city.y));
		return ele;
	}
	
}
