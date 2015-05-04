package cmsc420.meeshquest.part3;

import cmsc420.schema.City;
import cmsc420.schema.CityCoordinateComparator;
import cmsc420.schema.adjacencylist.AdjacencyList;
import cmsc420.schema.dictionary.AvlGTreeDictionary;
import cmsc420.schema.mediator.CommandParser;
import cmsc420.schema.spatial.PM.PMQuadTreeSeedling;

public class MeeshQuest {

	public static void main(String[] args) {
		CommandParser mediator = new CommandParser(new AvlGTreeDictionary(-1342), new PMQuadTreeSeedling(),
				new AdjacencyList<City>(new CityCoordinateComparator()));
		mediator.process();
		mediator.print();
	}

}
