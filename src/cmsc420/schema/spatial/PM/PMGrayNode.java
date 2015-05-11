package cmsc420.schema.spatial.PM;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.drawing.CanvasPlus;
import cmsc420.schema.City;
import cmsc420.schema.spatial.WhiteNode;

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
	public PMNode addVertex(Point2D.Float landmark) {
		if (landmark != null) {
			for (int i = 0; i < this.quadrants.length; i++) {
				if (this.cityInQuadrant(this.quadrants[i], landmark)) {
					this.quadrants[i] = this.quadrants[i].addVertex(landmark);
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

	private boolean cityInQuadrant(PMNode quadrant, Point2D.Float landmark) {
		float x = quadrant.origin().x;
		float y = quadrant.origin().y;
		int w = quadrant.width();
		int h = quadrant.height();
		return landmark.x >= x && landmark.x <= x + w && landmark.y >= y && landmark.y <= y + h;
	}

	public static boolean roadInQuadrant(PMNode quadrant, Point2D.Float landmark1, Point2D.Float landmark2) {
		float x = quadrant.origin().x;
		float y = quadrant.origin().y;
		int w = quadrant.width();
		int h = quadrant.height();

		float highX = Math.max(landmark1.x, landmark2.x);
		float lowX = Math.min(landmark1.x, landmark2.x);
		float highY = Math.max(landmark1.y, landmark2.y);
		float lowY = Math.min(landmark1.y, landmark2.y);
		// given a square and a line, determine if the line goes in the square

		// determine if either end point is in the square
		// if not, find a intersection between the and the 4 boundaries

		// check city1
		if (landmark1.x >= x && landmark1.x <= x + w && landmark1.y >= y && landmark1.y <= y + h) {
			return true;
		}

		// check city2
		if (landmark2.x >= x && landmark2.x <= x + w && landmark2.y >= y && landmark2.y <= y + h) {
			return true;
		}

		// vertical lines
		if (landmark1.x == landmark2.x) {
			return landmark1.x >= x && landmark1.x <= x + w && highY >= y && lowY <= y + h;
		}

		// horizontal lines
		if (landmark1.y == landmark2.y) {
			return landmark1.y >= y && landmark1.y <= y + h && highX >= x && lowX <= x + w;
		}

		double slope = (landmark2.y - landmark1.y) / (landmark2.x - landmark1.x);
		// y - y1 = m(x - x1)
		// y = m(x - x1) + y1
		// x = ((y - y1) / m) + x1

		// when y = lower line, x is in range [xLow, xHigh)
		double xTest = ((y - landmark1.y) / slope) + landmark1.x;
		if (xTest >= x && xTest <= x + w && xTest >= lowX && xTest <= highX) {
			return true;
		}

		// when y = upper line - 1, x is in range [xLow, xHigh)
		xTest = ((y + h - landmark1.y) / slope) + landmark1.x;
		if (xTest >= x && xTest <= x + w && xTest >= lowX && xTest <= highX) {
			return true;
		}

		// when x = left line, y is in range [yLow, yHigh)
		double yTest = slope * (x - landmark1.x) + landmark1.y;
		if (yTest >= y && yTest <= y + h && yTest >= lowY && yTest <= highY) {
			return true;
		}

		// when x = right line - 1, y is in range [yLow, yHigh)
		yTest = slope * (x + w - landmark1.x) + landmark1.y;
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
	public boolean contains(Point2D.Float landmark) {
		for (int i = 0; i < this.quadrants.length; i++) {
			if (this.cityInQuadrant(this.quadrants[i], landmark)) {
				return this.quadrants[i].contains(landmark);
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

	public String meeshEasterEgg() {
		return "fuck you";
	}

	@Override
	public PMNode remove(Float element) {
		if (element != null) {
			for (int i = 0; i < this.quadrants.length; i++) {
				if (this.cityInQuadrant(this.quadrants[i], element)) {
					this.quadrants[i] = this.quadrants[i].remove(element);
				}
			}
		}
		return this.unPartition();
	}

	private PMNode unPartition() {
		// check if we need to un-partition
		PMNode quad = null;
		int occupiedQuadrants = 0;

		/* find number of non-white quadrants */
		for (PMNode q : this.quadrants) {
			if (!(q instanceof WhiteNode)) {
				occupiedQuadrants++;
				quad = q; // get instance of occupied quadrant
			}
		}

		/* check if this (gray) node needs to change */
		if (occupiedQuadrants < 2 && !(quad instanceof PMGrayNode)) {
			/* check if zero or one city is left */
			if (occupiedQuadrants == 0) {
				return new PMWhiteNode(this.origin, this.width, this.height, this.canvas, this.validator);
			} else {
				return new PMBlackNode(((PMBlackNode) quad).getLandmark(), this.origin, this.width, this.height,
						this.canvas, this.validator);
			}
		} else {
			return this;
		}
	}

	@Override
	public PMNode removeRoad(City city1, City city2) {
		for (int i = 0; i < this.quadrants.length; i++) {
			if (roadInQuadrant(this.quadrants[i], city1, city2)) {
				this.quadrants[i] = this.quadrants[i].removeRoad(city1, city2);
			}
		}
		return this.unPartition();
	}

}
