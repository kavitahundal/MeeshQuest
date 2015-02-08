package cmsc420.schema.mediator;

import java.util.List;

import cmsc420.schema.City;
import cmsc420.schema.CityColor;
import cmsc420.schema.SortType;
import cmsc420.schema.adjacencylist.AdjacencyListStructure;
import cmsc420.schema.dictionary.DictionaryStructure;
import cmsc420.schema.spatial.PRQuadTree;
import cmsc420.schema.spatial.SpatialStructure;

public class CommandRunner {

	private DictionaryStructure dictionary;
	private SpatialStructure spatial;
	private AdjacencyListStructure adjacencyList;
	
	CommandRunner(DictionaryStructure dict, SpatialStructure spatial, AdjacencyListStructure adj) {
		this.dictionary = dict;
		this.spatial = spatial;
		this.adjacencyList = adj;
	}
	
	void createCity(String name, int x, int y, int radius, CityColor color) {
		//
	}
	
	City deleteCity(String name) { // parameter->name + city (cityunmapped tag)
		// city unmapped tag if put in prquadtree
		// otherwise let's just return null
		// city unmapped tag
		return null;
	}
	
	void clearAll() {
		//
	}
	
	List<City> listCities(SortType sortBy) { // citylist = 1+ cities = sorted by sortBy
		// citylist -> city tags
		return null;
	}
	
	void mapCity(String name) {
		//
	}
	
	void unmapCity(String name) {
		//
	}
	
	PRQuadTree printPRQuadTree() { // this one is tricky
		// gray, black, white tags
		return null;
	}
	
	void saveMap(String name) {
		//	
	}
	
	List<City> rangeCities(int x, int y, int radius, String name) { // one citylist = multiple cities = asciibetical
		// citylist -> city tags
		return null;
	}
	
	City nearestCity(int x, int y) { // single city
		// city tag
		return null;
	}
}
