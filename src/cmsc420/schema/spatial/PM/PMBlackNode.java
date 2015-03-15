package cmsc420.schema.spatial.PM;

import java.awt.Color;
import java.awt.geom.Point2D;

import cmsc420.drawing.CanvasPlus;
import cmsc420.schema.City;
import cmsc420.schema.CityCoordinateComparator;
import cmsc420.schema.adjacencylist.AdjacencyList;

public class PMBlackNode implements PMNode {

	private final Point2D.Float origin;
	private final int width;
	private final int height;
	private AdjacencyList<City> roads;
	private City city;
	private CanvasPlus canvas;
	private Validator validator;

	public PMBlackNode(City city, Point2D.Float origin, int width, int height, CanvasPlus canvas, Validator validator) {
		this.city = city;
		this.origin = origin;
		this.width = width;
		this.height = height;
		this.canvas = canvas;
		this.roads = new AdjacencyList<City>(new CityCoordinateComparator());
		this.validator = validator;
	}

	@Override
	public PMNode addCity(City city) {
		// TODO Auto-generated method stub
		// create graynode
		// draw lines
		// add this.city
		// add city
		// TODO how to add roads?
		if (this.city == null) {
			this.city = city; //TODO pm1...
			return this;
		} else {
			PMNode node = new PMGrayNode(this.origin, this.width, this.height, this.canvas, this.validator); // partition
			if (this.canvas != null) {
				this.canvas.addLine(this.origin.x, this.origin.y + this.height / 2, this.origin.x + this.width,
						this.origin.y + this.height / 2, Color.GRAY);
				this.canvas.addLine(this.origin.x + this.width / 2, this.origin.y, this.origin.x + this.width / 2,
						this.origin.y + this.height, Color.GRAY);
				this.canvas.removePoint(this.city.getName(), this.city.x, this.city.y, Color.BLACK);
			}
			node.addCity(this.city); // add the old node that was removed
			node.addCity(city); // add the new node
			//TODO add roads
			return node;
		}
	}

	@Override
	public PMNode addRoad(City city1, City city2) {
		this.roads.addUndirectedEdge(city1, city2);
		// TODO Auto-generated method stub
		// TODO need to find a way to import ajd list
		// just add road to adj list here
		// but need to check if need to partition first (validate)
		return this;
		// TODO integrate validator
	}

}
