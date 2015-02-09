package cmsc420.meeshquest.part1;

import cmsc420.schema.City;
import cmsc420.schema.CityColor;
import cmsc420.schema.spatial.PRQuadTree;
import cmsc420.schema.spatial.PRQuadTreeSeedling;

public class MeeshQuest {

	public static void main(String[] args) {
//		CommandParser mediator = new CommandParser(new TreeMapDictionary(), new PRQuadTreeSeedling(), null);
//		mediator.process();
//		mediator.print();
		PRQuadTree p = (PRQuadTree) new PRQuadTreeSeedling().generate(1024, 1024);
		p.add(new City("Edinburgh", 100, 500, CityColor.red, 50));
		p.add(new City("Lisbon", 100, 100, CityColor.orange, 80));
		p.add(new City("London", 150, 250, CityColor.yellow, 60));
		p.add(new City("Prague", 150, 700, CityColor.green, 60));
		p.add(new City("Madrid", 200, 100, CityColor.blue, 90));
//		p.add(new City("Paris", 250, 300, CityColor.purple, 40)); // this breaks it
//		p.add(new City("Copenhagen", 250, 500, CityColor.black, 50));
//		p.add(new City("Marseilles", 350, 100, CityColor.red, 50));
//		p.add(new City("Vienna", 350, 700, CityColor.orange, 70));
//		p.add(new City("Amsterdam", 400, 300, CityColor.yellow, 70));
//		p.add(new City("Munich", 400, 200, CityColor.green, 30));
//		p.add(new City("Milan", 500, 50, CityColor.blue, 20));
//		p.add(new City("Rome", 500, 450, CityColor.purple, 80));
//		p.add(new City("Naples", 450, 650, CityColor.black, 50));
//		p.add(new City("Varna", 100, 800, CityColor.red, 90));
//		p.add(new City("Geneva", 300, 200, CityColor.orange, 50));
		System.out.println("done");
	}
}
