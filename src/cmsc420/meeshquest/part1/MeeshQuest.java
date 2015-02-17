package cmsc420.meeshquest.part1;

import cmsc420.schema.dictionary.TreeMapDictionary;
import cmsc420.schema.mediator.CommandParser;
import cmsc420.schema.spatial.PRQuadTreeSeedling;

/**
 * Driver class for MeeshQuest Part 1.
 * 
 * @author Andrew Liu
 *
 */
public class MeeshQuest {

	public static void main(String[] args) {
		CommandParser mediator = new CommandParser(new TreeMapDictionary(), new PRQuadTreeSeedling(), null);
		mediator.process();
		mediator.print();
	}
}
