package cmsc420.schema.spatial.PM;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.drawing.CanvasPlus;
import cmsc420.schema.Airport;
import cmsc420.schema.City;
import cmsc420.schema.CityCoordinateComparator;
import cmsc420.schema.adjacencylist.AdjacencyList;

public class PMBlackNode implements PMNode {

	private final Point2D.Float origin;
	private final int width;
	private final int height;
	private AdjacencyList<City> roads;
	private Point2D.Float landmark;
	private CanvasPlus canvas;
	private Validator validator;

	public PMBlackNode(Point2D.Float landmark, Point2D.Float origin, int width, int height, CanvasPlus canvas, Validator validator) {
		this.landmark = landmark;
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

	public Point2D.Float getLandmark() {
		return this.landmark;
	}

	@Override
	public PMNode addVertex(Point2D.Float landmark) {
		if (this.landmark == null && this.validator.valid(this, landmark)) {
			this.landmark = landmark;
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
			node.addVertex(this.landmark); // add the old node that was removed
			node.addVertex(landmark); // add the new node
			Iterator<City[]> roadsToAdd = this.roads.iterator();
			while (roadsToAdd.hasNext()) {
				Object[] road = roadsToAdd.next();
				node.addRoad((City) road[0], (City) road[1]);
			}
			return node;
		}
	}

	@Override
	public PMNode addRoad(Point2D.Float landmark1, Point2D.Float landmark2) {
		if (this.validator.valid(this, landmark1, landmark2)) {
			this.roads.addUndirectedEdge((City) landmark1, (City) landmark2);
			return this;
		} else {
			// TODO
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
		if (this.landmark != null) {
			cardinality++;
			Element landmarkTag;
			if (this.landmark instanceof City) {
				String tagName = "city";
				City city = (City) this.landmark;
				landmarkTag = doc.createElement(tagName);
				landmarkTag.setAttribute("color", city.getColor().toString());
				landmarkTag.setAttribute("name", city.getName());
				landmarkTag.setAttribute("radius", "" + city.getRadius());
				landmarkTag.setAttribute("localX", "" + (int) city.x);
				landmarkTag.setAttribute("localY", "" + (int) city.y);
				landmarkTag.setAttribute("remoteX", "" + city.remoteX);
				landmarkTag.setAttribute("remoteY", "" + city.remoteY);
			} else {
				String tagName = "airport";
				Airport airport = (Airport) this.landmark;
				landmarkTag = doc.createElement(tagName);
				landmarkTag.setAttribute("name", airport.getName());
				landmarkTag.setAttribute("airlineName", airport.getAirlineName());
				landmarkTag.setAttribute("localX", "" + (int) airport.x);
				landmarkTag.setAttribute("localY", "" + (int) airport.y);
				landmarkTag.setAttribute("remoteX", "" + airport.remoteX);
				landmarkTag.setAttribute("remoteY", "" + airport.remoteY);
			}
			ele.appendChild(landmarkTag);
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
	public boolean contains(Point2D.Float landmark) {
		if (this.landmark == null) {
			return false;
		} else {
			String thisName = this.landmark instanceof City ? ((City) this.landmark).getName() : ((Airport) this.landmark).getName();
			String otherName = landmark instanceof City ? ((City) landmark).getName() : ((Airport) landmark).getName();
			return this.landmark.equals(landmark) && thisName.equals(otherName);
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

}
