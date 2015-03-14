package cmsc420.schema.spatial.PM;

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
	
	public PMBlackNode(City city, Point2D.Float origin, int width, int height, CanvasPlus canvas) {
		this.city = city;
		this.origin = origin;
		this.width = width;
		this.height = height;
		this.canvas = canvas;
		this.roads = new AdjacencyList<City>(new CityCoordinateComparator());
	}
	
	@Override
	public PMNode addCity(City city) {
		// TODO Auto-generated method stub
		// create graynode
		// draw lines
		// add this.city
		// add city
		// TODO how to add roads?
		return null;
	}
	@Override
	public PMNode addRoad(City city1, City city2) {
		// TODO Auto-generated method stub
		// TODO need to find a way to import ajd list
		// just add road to adj list here
		return null;
	}

}
