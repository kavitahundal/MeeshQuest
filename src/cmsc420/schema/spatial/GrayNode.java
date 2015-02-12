package cmsc420.schema.spatial;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.drawing.CanvasPlus;
import cmsc420.schema.City;

public class GrayNode implements TreeNode {

	private final Point2D.Float origin;
	private final float width;
	private final float height;
	private TreeNode[] quadrants;
	// private int occupiedQuadrants;
	private CanvasPlus canvas;

	public GrayNode(Point2D.Float origin, float width, float height, CanvasPlus canvas) {
		this.origin = origin;
		this.width = width;
		this.height = height;
		float dx = this.width / 2;
		float dy = this.height / 2;
		// this.occupiedQuadrants = 0;
		this.canvas = canvas;
		this.quadrants = new TreeNode[4];
		this.quadrants[0] = new WhiteNode(new Point2D.Float(this.origin.x, this.origin.y + dy), dx, dy, this.canvas);
		this.quadrants[1] = new WhiteNode(new Point2D.Float(this.origin.x + dx, this.origin.y + dy), dx, dy,
				this.canvas);
		this.quadrants[2] = new WhiteNode(this.origin, dx, dy, this.canvas);
		this.quadrants[3] = new WhiteNode(new Point2D.Float(this.origin.x + dx, this.origin.y), dx, dy, this.canvas);
	}

	@Override
	public TreeNode add(City city) {
		int quadrant = this.getQuadrantIndex(city);
		// if (this.quadrants[quadrant] instanceof WhiteNode) {
		// this.occupiedQuadrants++;
		// }
		this.quadrants[quadrant] = this.quadrants[quadrant].add(city);
		return this;
	}

	@Override
	public boolean contains(City city) {
		int quadrant = this.getQuadrantIndex(city);
		return this.quadrants[quadrant].contains(city);
	}

	private int getQuadrantIndex(City city) {
		Point2D.Float center = this.location();
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
	public TreeNode remove(City city) {
		int quadrant = this.getQuadrantIndex(city);
		this.quadrants[quadrant] = this.quadrants[quadrant].remove(city);
		TreeNode quad = null;
		int occupiedQuadrants = 0;
		for (TreeNode q : this.quadrants) {
			if (!(q instanceof WhiteNode)) {
				occupiedQuadrants++;
				quad = q;
			}
		}
		if (occupiedQuadrants < 2 && !(quad instanceof GrayNode)) {
			if (this.canvas != null) {
				this.canvas.removeLine(this.origin.x, this.origin.y + this.height / 2, this.origin.x + this.width,
						this.origin.y + this.height / 2, Color.BLACK);
				this.canvas.removeLine(this.origin.x + this.width / 2, this.origin.y, this.origin.x + this.width / 2,
						this.origin.y + this.height, Color.BLACK);
			}
			if (occupiedQuadrants == 0) {
				return new WhiteNode(this.origin, this.width, this.height, this.canvas);
			} else {
				return new BlackNode(((BlackNode) quad).getCity(), this.origin, this.width, this.height, this.canvas);
			}
		} else {
			return this;
		}
		// this.occupiedQuadrants--;
		// if (this.occupiedQuadrants < 2) {
		// if (this.canvas != null) {
		// this.canvas.removeLine(this.origin.x, this.origin.y + this.height /
		// 2, this.origin.x + this.width,
		// this.origin.y + this.height / 2, Color.BLACK);
		// this.canvas.removeLine(this.origin.x + this.width / 2, this.origin.y,
		// this.origin.x + this.width / 2,
		// this.origin.y + this.height, Color.BLACK);
		// }
		// if (this.occupiedQuadrants == 0) {
		// return new WhiteNode(this.origin, this.width, this.height,
		// this.canvas);
		// } else { // this.occupiedQuadrants == 1
		// for (TreeNode q : this.quadrants) {
		// // return the only occupied quadrant as the black node
		// if (q instanceof BlackNode) {
		// return new BlackNode(((BlackNode) q).getCity(), this.origin,
		// this.width, this.height,
		// this.canvas);
		// } else if (q instanceof GrayNode) {
		// if (this.canvas != null) {
		// this.canvas.addLine(this.origin.x, this.origin.y + this.height / 2,
		// this.origin.x
		// + this.width, this.origin.y + this.height / 2, Color.BLACK);
		// this.canvas.addLine(this.origin.x + this.width / 2, this.origin.y,
		// this.origin.x
		// + this.width / 2, this.origin.y + this.height, Color.BLACK);
		// }
		// return this;
		// }
		// }
		// throw new UnsupportedOperationException(); // will never happen
		// }
		// } else {
		// return this;
		// }
	}

	@Override
	public Element elementize(Document doc) {
		Element ele = doc.createElement("gray");
		Point2D.Float location = this.location();
		ele.setAttribute("x", Integer.toString((int) location.x));
		ele.setAttribute("y", Integer.toString((int) location.y));
		for (int i = 0; i < this.quadrants.length; i++) {
			ele.appendChild(this.quadrants[i].elementize(doc));
		}
		return ele;
	}

	public Point2D.Float location() {
		return new Point2D.Float(this.origin.x + this.width / 2, this.origin.y + this.height / 2);
	}

	@Override
	public void range(List<String> cities, int x, int y, int radius) {
		for (int i = 0; i < this.quadrants.length; i++) {
			this.quadrants[i].range(cities, x, y, radius);
		}
	}

	public Set<TreeNode> getChildren() {
		Set<TreeNode> quads = new HashSet<>();
		for (TreeNode q : this.quadrants) {
			quads.add(q);
		}
		return quads;
	}
}
