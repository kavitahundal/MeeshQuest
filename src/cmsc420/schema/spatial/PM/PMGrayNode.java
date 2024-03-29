package cmsc420.schema.spatial.PM;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.drawing.CanvasPlus;
import cmsc420.schema.City;

public class PMGrayNode implements PMNode {

	private final Point2D.Float origin;
	private final int width;
	private final int height;
	private PMNode[] quadrants;
	private CanvasPlus canvas;
	private Validator validator;

	public PMGrayNode(Point2D.Float origin, int width, int height, CanvasPlus canvas, Validator validator) {
		this.origin = origin;
		this.width = width;
		this.height = height;
		this.canvas = canvas;
		this.validator = validator;
		this.quadrants = new PMNode[4];
		int dx = this.width / 2;
		int dy = this.height / 2;

		/* setting boundaries for the quadrants */
		this.quadrants[0] = new PMWhiteNode(new Point2D.Float(this.origin.x, this.origin.y + dy), dx, dy, this.canvas,
				this.validator);
		this.quadrants[1] = new PMWhiteNode(new Point2D.Float(this.origin.x + dx, this.origin.y + dy), dx, dy,
				this.canvas, this.validator);
		this.quadrants[2] = new PMWhiteNode(this.origin, dx, dy, this.canvas, this.validator);
		this.quadrants[3] = new PMWhiteNode(new Point2D.Float(this.origin.x + dx, this.origin.y), dx, dy, this.canvas,
				this.validator);
	}

	@Override
	public PMNode addCity(City city) {
		if (city != null) {
			for (int i = 0; i < this.quadrants.length; i++) {
				if (this.cityInQuadrant(this.quadrants[i], city)) {
					this.quadrants[i] = this.quadrants[i].addCity(city);
				}
			}
		}
		return this;
	}

	@Override
	public PMNode addRoad(City city1, City city2) {
		// find all relevant quadrants
		// add line to said quadrants
		for (int i = 0; i < 4; i++) {
			if (roadInQuadrant(this.quadrants[i], city1, city2)) {
				this.quadrants[i] = this.quadrants[i].addRoad(city1, city2);
			}
		}
		return this;
	}

	private boolean cityInQuadrant(PMNode quadrant, City city) {
		float x = quadrant.origin().x;
		float y = quadrant.origin().y;
		int w = quadrant.width();
		int h = quadrant.height();
		return city.x >= x && city.x <= x + w && city.y >= y && city.y <= y + h;
	}

	public static boolean roadInQuadrant(PMNode quadrant, City city1, City city2) {
		float x = quadrant.origin().x;
		float y = quadrant.origin().y;
		int w = quadrant.width();
		int h = quadrant.height();
		
		float highX = Math.max(city1.x, city2.x);
		float lowX = Math.min(city1.x, city2.x);
		float highY = Math.max(city1.y, city2.y);
		float lowY = Math.min(city1.y, city2.y);
		// given a square and a line, determine if the line goes in the square

		// determine if either end point is in the square
		// if not, find a intersection between the and the 4 boundaries

		// check city1
		if (city1.x >= x && city1.x <= x + w && city1.y >= y && city1.y <= y + h) {
			return true;
		}

		// check city2
		if (city2.x >= x && city2.x <= x + w && city2.y >= y && city2.y <= y + h) {
			return true;
		}

		// vertical lines
		if (city1.x == city2.x) {
			return city1.x >= x && city1.x <= x + w && highY >= y && lowY <= y + h;
		}
		
		// horizontal lines
		if (city1.y == city2.y) {
			return city1.y >= y && city1.y <= y + h && highX >= x && lowX <= x + w;
		}
		
		double slope = (city2.y - city1.y) / (city2.x - city1.x);
		// y - y1 = m(x - x1)
		// y = m(x - x1) + y1
		// x = ((y - y1) / m) + x1

		// when y = lower line, x is in range [xLow, xHigh)
		double xTest = ((y - city1.y) / slope) + city1.x;
		if (xTest >= x && xTest <= x + w && xTest >= lowX && xTest <= highX) {
			return true;
		}

		// when y = upper line - 1, x is in range [xLow, xHigh)
		xTest = ((y + h - city1.y) / slope) + city1.x;
		if (xTest >= x && xTest <= x + w && xTest >= lowX && xTest <= highX) {
			return true;
		}

		// when x = left line, y is in range [yLow, yHigh)
		double yTest = slope * (x - city1.x) + city1.y;
		if (yTest >= y && yTest <= y + h && yTest >= lowY && yTest <= highY) {
			return true;
		}

		// when x = right line - 1, y is in range [yLow, yHigh)
		yTest = slope * (x + w - city1.x) + city1.y;
		if (yTest >= y && yTest <= y + h && yTest >= lowY && yTest <= highY) {
			return true;
		}

		return false; // hopefully this should be correct!
	}

	public Set<PMNode> getChildren() {
		Set<PMNode> quads = new HashSet<>();
		for (PMNode q : this.quadrants) {
			quads.add(q);
		}
		return quads;
	}

	@Override
	public Element elementize(Document doc) {
		Element ele = doc.createElement("gray");
		ele.setAttribute("x", Integer.toString((int) this.origin.x + this.width / 2));
		ele.setAttribute("y", Integer.toString((int) this.origin.y + this.height / 2));

		/* recursive call on the children nodes */
		for (int i = 0; i < this.quadrants.length; i++) {
			ele.appendChild(this.quadrants[i].elementize(doc));
		}
		return ele;
	}

	@Override
	public boolean contains(City city) {
		for (int i = 0; i < this.quadrants.length; i++) {
			if (this.cityInQuadrant(this.quadrants[i], city)) {
				return this.quadrants[i].contains(city);
			}
		}
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
		for (int i = 0; i < this.quadrants.length; i++) {
			this.quadrants[i].range(cities, x, y, radius);
		}
	}

	@Override
	public void getCities(Set<City> cities) {
		for (int i = 0; i < this.quadrants.length; i++) {
			this.quadrants[i].getCities(cities);
		}
	}
}
