package cmsc420.schema.spatial.PM;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.drawing.CanvasPlus;
import cmsc420.schema.City;

public class PMWhiteNode implements PMNode {

	private final Point2D.Float origin;
	private final int width;
	private final int height;
	private CanvasPlus canvas;
	private Validator validator;

	public PMWhiteNode(Point2D.Float origin, int width, int height, CanvasPlus canvas, Validator validator) {
		this.origin = origin;
		this.width = width;
		this.height = height;
		this.canvas = canvas;
		this.validator = validator;
	}

	@Override
	public PMNode addCity(City city) {
		return new PMBlackNode(city, this.origin, this.width, this.height, this.canvas, this.validator);
	}

	@Override
	public PMNode addRoad(City city1, City city2) {
		return new PMBlackNode(null, this.origin, this.width, this.height, this.canvas, this.validator).addRoad(city1,
				city2);
	}

	@Override
	public Element elementize(Document doc) {
		return doc.createElement("white");
	}

	@Override
	public boolean contains(City city) {
		return false;
	}

	@Override
	public Point2D.Float origin() {
		return this.origin;
	}

	@Override
	public int width() {
		return this.width;
	}

	@Override
	public int height() {
		return this.height;
	}

	@Override
	public void range(List<String> cities, int x, int y, int radius) {
		return;
	}

	@Override
	public void getCities(Set<City> cities) {
		return;
	}

}
