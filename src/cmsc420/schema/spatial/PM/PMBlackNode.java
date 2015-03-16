package cmsc420.schema.spatial.PM;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Iterator;

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

	int numRoads() {
		return this.roads.size(); // number of directed edges
	}

	public City getCity() {
		return this.city;
	}

	@Override
	public PMNode addCity(City city) {
		if (this.city == null && this.validator.valid(this, city)) {
			this.city = city;
			return this;
		} else {
			PMNode node = new PMGrayNode(this.origin, this.width, this.height, this.canvas, this.validator); // partition
			if (this.canvas != null) {
				this.canvas.addLine(this.origin.x, this.origin.y + this.height / 2, this.origin.x + this.width,
						this.origin.y + this.height / 2, Color.GRAY);
				this.canvas.addLine(this.origin.x + this.width / 2, this.origin.y, this.origin.x + this.width / 2,
						this.origin.y + this.height, Color.GRAY);
			}
			node.addCity(this.city); // add the old node that was removed
			node.addCity(city); // add the new node
			Iterator<City[]> roadsToAdd = this.roads.iterator();
			while (roadsToAdd.hasNext()) {
				City[] road = roadsToAdd.next();
				node.addRoad(road[0], road[1]);
			}
			return node;
		}
	}

	@Override
	public PMNode addRoad(City city1, City city2) {
		if (this.validator.valid(this, city1, city2)) {
			this.roads.addUndirectedEdge(city1, city2);
			return this;
		} else {
			throw new UnsupportedOperationException("I'll implement road functionality in part 3");
			// create gray node
			// add city to gray node if it isn't null
			// add all cities in this node's adj list
			// add the new city
			// return the gray node
			// return null;
		}
	}

}
