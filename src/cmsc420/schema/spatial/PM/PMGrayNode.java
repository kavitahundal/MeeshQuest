package cmsc420.schema.spatial.PM;

import java.awt.geom.Point2D;
import java.util.HashSet;
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

	private Point2D.Float center() {
		return new Point2D.Float(this.origin.x + this.width / 2, this.origin.y + this.height / 2);
	}

	private int getQuadrantIndex(City city) {
		if (city == null) {
			return -1;
		}
		Point2D.Float center = this.center();
		if (city.x >= center.x) {
			if (city.y >= center.y) {
				return 1;
			} else {
				return 3;
			}
		} else {
			if (city.y >= center.y) {
				return 0;
			} else {
				return 2;
			}
		}
	}

	@Override
	public PMNode addCity(City city) {
		if (city != null) {
			int quadrant = this.getQuadrantIndex(city);
			this.quadrants[quadrant] = this.quadrants[quadrant].addCity(city);
		}
		return this;
	}

	@Override
	public PMNode addRoad(City city1, City city2) {
		// find all relevant quadrants
		// add line to said quadrants
		for (int i = 0; i < 4; i++) {
			if (this.roadInQuadrant(this.quadrants[i], city1, city2)) {
				this.quadrants[i] = this.quadrants[i].addRoad(city1, city2);
			}
		}
		return this;
	}

	private boolean roadInQuadrant(PMNode node, City city1, City city2) {
		// given a square and a line, determine if the line goes in the square

		// determine if either end point is in the square
		// if not, find a intersection between the and the 4 boundaries

		// check city1
		if (city1.x >= this.origin.x && city1.x < this.origin.x + this.width && city1.y >= this.origin.y
				&& city1.y < this.origin.y + this.height) {
			return true;
		}

		// check city2
		if (city2.x >= this.origin.x && city2.x < this.origin.x + this.width && city2.y >= this.origin.y
				&& city2.y < this.origin.y + this.height) {
			return true;
		}

		double slope = (city2.y - city1.y) / (city2.x - city1.x);
		// y - y1 = m(x - x1)
		// y = m(x - x1) + y1
		// x = ((y - y1) / m) + x1

		// when y = lower line, x is in range [xLow, xHigh)
		double xTest = ((this.origin.y - city1.y) / slope) + city1.x;
		if (xTest >= this.origin.x && xTest < this.origin.x + this.width) {
			return true;
		}

		// when y = upper line - 1, x is in range [xLow, xHigh)
		xTest = ((this.origin.y = this.height - city1.y) / slope) + city1.x;
		if (xTest >= this.origin.x && xTest < this.origin.x + this.width) {
			return true;
		}

		// when x = left line, y is in range [yLow, yHigh)
		double yTest = slope * (this.origin.x - city1.x) + city1.y;
		if (yTest >= this.origin.y && yTest < this.origin.y + this.height) {
			return true;
		}

		// when x = right line - 1, y is in range [yLow, yHigh)
		yTest = slope * (this.origin.x + this.width - city1.x) + city1.y;
		if (yTest >= this.origin.y && yTest < this.origin.y + this.height) {
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
}
