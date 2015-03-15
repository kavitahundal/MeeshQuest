package cmsc420.schema.spatial.PM;

import java.awt.geom.Point2D;

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
		// set individual quads
	}

	@Override
	public PMNode addCity(City city) {
		// TODO Auto-generated method stub
		// figure out quadrant
		// add to quadrant
		return null;
	}

	@Override
	public PMNode addRoad(City city1, City city2) {
		// TODO Auto-generated method stub
		// find all relevant quadrants
		// add line to said quadrants
		// or...
		// add line to all quadrants
		// and do a check per quadrant
		return null;
	}
}
