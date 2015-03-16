package cmsc420.meeshquest.part2;

import cmsc420.schema.dictionary.AvlGTreeDictionary;
import cmsc420.schema.mediator.CommandParser;
import cmsc420.schema.spatial.PM.PMQuadTreeSeedling;

/**
 * Driver class for MeeshQuest Part 2.
 * 
 * @author Andrew Liu
 *
 */
public class MeeshQuest {

	public static void main(String[] args) {
		CommandParser mediator = new CommandParser(new AvlGTreeDictionary(-1342), new PMQuadTreeSeedling(), null);
		mediator.process();
		mediator.print();
	}
}