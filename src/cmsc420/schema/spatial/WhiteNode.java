package cmsc420.schema.spatial;

import java.awt.geom.Point2D;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.drawing.CanvasPlus;
import cmsc420.schema.City;

public class WhiteNode implements TreeNode {
	
	private final Point2D.Float origin;
	private final float width;
	private final float height;
	private CanvasPlus canvas;
	
	public WhiteNode(Point2D.Float origin, float width, float height) {
		this.origin = origin;
		this.width = width;
		this.height = height;
	}

	@Override
	public TreeNode add(City city) {
		return new BlackNode(city, this.origin, this.width, this.height);
	}

	@Override
	public boolean contains(City city) {
		return false;
	}

	@Override
	public TreeNode remove(City city) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Element elementize(Document doc) {
		return doc.createElement("white");
	}
	
	
}
