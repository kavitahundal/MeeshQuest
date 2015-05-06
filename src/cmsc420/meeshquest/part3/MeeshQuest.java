package cmsc420.meeshquest.part3;

import cmsc420.schema.mediator.CommandParser;

public class MeeshQuest {

	public static void main(String[] args) {
		CommandParser mediator = new CommandParser();
		mediator.process();
		mediator.print();
	}

}
