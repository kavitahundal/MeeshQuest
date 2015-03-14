package cmsc420.schema.spatial.PM;

import java.awt.Color;
import java.awt.geom.Point2D;

import cmsc420.drawing.CanvasPlus;
import cmsc420.schema.City;

public class PMWhiteNode implements PMNode {
	
	private final Point2D.Float origin;
	private final int width;
	private final int height;
	private CanvasPlus canvas;
	
	public PMWhiteNode(Point2D.Float origin, int width, int height, CanvasPlus canvas) {
		this.origin = origin;
		this.width = width;
		this.height = height;
		this.canvas = canvas;
	}

	@Override
	public PMNode addCity(City city) {
		if (this.canvas != null) {
			this.canvas.addPoint(city.getName(), city.x, city.y, Color.BLACK);
		}
		return new PMBlackNode(city, this.origin, this.width, this.height, this.canvas);
	}

	@Override
	public PMNode addRoad(City city1, City city2) {
		// draw point on canvas
		// create black node
		// add point to black node
		return null;
	}

}
