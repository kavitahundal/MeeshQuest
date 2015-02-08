package cmsc420.schema.mediator;

import cmsc420.schema.CityColor;
import cmsc420.schema.SortType;
import cmsc420.schema.adjacencylist.AdjacencyListStructure;
import cmsc420.schema.dictionary.DictionaryStructure;
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
	
	void deleteCity(String name) {
		//
	}
	
	void clearAll() {
		//
	}
	
	void listCities(SortType sortBy) {
		//
	}
	
	void mapCity(String name) {
		//
	}
	
	void unmapCity(String name) {
		//
	}
	
	void printPRQuadTree() {
		//
	}
	
	void saveMap(String name) {
		//
	}
	
	void rangeCities(int x, int y, int radius, String name) {
		//
	}
	
	void nearestCity(int x, int y) {
		//
	}
}
