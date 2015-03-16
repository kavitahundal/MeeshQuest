package cmsc420.meeshquest.part2;

import cmsc420.schema.dictionary.AvlGTreeDictionary;
import cmsc420.schema.mediator.CommandParser;
import cmsc420.schema.spatial.PRQuadTreeSeedling;

/**
 * Driver class for MeeshQuest Part 2.
 * 
 * @author Andrew Liu
 *
 */
public class MeeshQuest {

	public static void main(String[] args) {
		CommandParser mediator = new CommandParser(new AvlGTreeDictionary(1), new PRQuadTreeSeedling(), null);
		// TODO change pr to pm
		mediator.process();
		mediator.print();
	}
}