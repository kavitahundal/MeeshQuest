package cmsc420.schema;

import java.awt.geom.Point2D;

import cmsc420.schema.adjacencylist.AdjacencyList;
import cmsc420.schema.dictionary.AirportDictionary;
import cmsc420.schema.dictionary.AvlGTreeDictionary;
import cmsc420.schema.dictionary.DictionaryStructure;
import cmsc420.schema.spatial.PM.PM1QuadTree;
import cmsc420.schema.spatial.PM.PM3QuadTree;
import cmsc420.schema.spatial.PM.PMQuadTree;

public class Metropole extends Point2D.Float {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PMQuadTree roads;
	private DictionaryStructure dict;
	private AdjacencyList<City> adj;
	private AirportDictionary airports;

	public Metropole(float x, float y, int pmOrder, int width, int height, int g) {
		super(x, y);
//		if (pmOrder == 1) {
//			this.roads = new PM1QuadTree("metropole PM", width, height);
//		} else if (pmOrder == 3) {
//			this.roads = new PM3QuadTree("metropole PM", width, height);
//		} else {
//			throw new UnsupportedOperationException("wrong pm order");
//		}
		this.roads = new PM3QuadTree("metropole PM", width, height);
		// TODO
		this.dict = new AvlGTreeDictionary(g);
		this.adj = new AdjacencyList<City>(new CityCoordinateComparator());
		this.airports = new AirportDictionary();
	}
	
	public PMQuadTree getRoads() {
		return this.roads;
	}
	
	public DictionaryStructure getDict() {
		return this.dict;
	}
	
	public AdjacencyList<City> getAdj() {
		return this.adj;
	}
	
	public AirportDictionary getAirports() {
		return this.airports;
	}
	
	// add/remove airport/road?

}
