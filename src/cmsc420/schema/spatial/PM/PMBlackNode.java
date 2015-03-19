package cmsc420.schema.spatial.PM;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
			if (this.width == 1 || this.height == 1) {
				throw new UnsupportedOperationException("Can't subdivide further!");
			}
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
				Object[] road = roadsToAdd.next();
				node.addRoad((City) road[0], (City) road[1]);
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

	@Override
	public Element elementize(Document doc) {
		Element ele = doc.createElement("black");
		List<City[]> cities = this.getSortedUniqueRoads();
		int cardinality = cities.size();
		if (this.city != null) {
			cardinality++;
			// check if isolated
			String tagName = this.roads.isIsolated(this.city) ? "isolatedCity" : "city";
			Element cityTag = doc.createElement(tagName);
			cityTag.setAttribute("color", this.city.getColor().toString());
			cityTag.setAttribute("name", this.city.getName());
			cityTag.setAttribute("radius", "" + this.city.getRadius());
			cityTag.setAttribute("x", "" + (int) this.city.x);
			cityTag.setAttribute("y", "" + (int) this.city.y);
			ele.appendChild(cityTag);
		}
		ele.setAttribute("cardinality", "" + cardinality);
		for (City[] road : cities) {
			// add each road
			Element roadTag = doc.createElement("road");
			roadTag.setAttribute("start", road[0].getName());
			roadTag.setAttribute("end", road[1].getName());
			ele.appendChild(roadTag);
		}
		return ele;
	}

	private List<City[]> getSortedUniqueRoads() {
		List<City[]> ret = new LinkedList<City[]>();
		Iterator<City[]> iter = this.roads.iterator();
		while (iter.hasNext()) {
			Object[] next = iter.next();
			if (((City) next[0]).getName().compareTo(((City) next[1]).getName()) < 0) {
				City[] toAdd = new City[2];
				toAdd[0] = (City) next[0];
				toAdd[1] = (City) next[1];
				ret.add(toAdd);
			}
		}
		Collections.sort(ret, new RoadComparator());
		return ret;
	}

	public static class RoadComparator implements Comparator<City[]> {

		@Override
		public int compare(City[] o1, City[] o2) {
			int startCmp = o1[0].getName().compareTo(o2[0].getName());
			return startCmp == 0 ? o1[1].getName().compareTo(o2[1].getName()) : startCmp;
		}

	}

	@Override
	public boolean contains(City city) {
		if (this.city == null) {
			return false;
		} else {
			return this.city.equals(city) && this.city.getName().equals(city.getName());
		}
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
		if (this.city != null && this.city.distance(x, y) <= radius) {
			cities.add(this.city.getName());
		}
	}

	@Override
	public void getCities(Set<City> cities) {
		if (this.city != null) {
			cities.add(this.city);
		}
	}

}
