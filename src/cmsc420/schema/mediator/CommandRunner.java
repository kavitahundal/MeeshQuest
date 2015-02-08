package cmsc420.schema.mediator;

import cmsc420.schema.adjacencylist.AdjacencyListStructure;
import cmsc420.schema.dictionary.DictionaryStructure;
import cmsc420.schema.spatial.Seedling;
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
}
