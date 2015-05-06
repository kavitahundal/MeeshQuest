package cmsc420.schema;

import java.awt.geom.Point2D;

import cmsc420.schema.adjacencylist.AdjacencyList;
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
	
	private PMQuadTree metropole;
	private DictionaryStructure dict;
	private AdjacencyList<City> adj;

	public Metropole(float x, float y, int pmOrder, int width, int height, int g) {
		super(x, y);
//		super("metropole", x, y, CityColor.blue, 0);
		if (pmOrder == 1) {
			this.metropole = new PM1QuadTree("metropole PM", width, height);
		} else if (pmOrder == 3) {
			this.metropole = new PM3QuadTree("metropole PM", width, height);
		} else {
			throw new UnsupportedOperationException("wrong pm order");
		}
		this.dict = new AvlGTreeDictionary(g);
	}
	
	public PMQuadTree getMetropole() {
		return this.metropole;
	}
	
	public DictionaryStructure getDict() {
		return this.dict;
	}
	
	public AdjacencyList<City> getAdj() {
		return this.adj;
	}

}
