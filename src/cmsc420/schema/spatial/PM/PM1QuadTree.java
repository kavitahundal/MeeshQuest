package cmsc420.schema.spatial.PM;

import cmsc420.schema.City;
import cmsc420.schema.DataStructure;

public class PM1QuadTree extends PMQuadTree {

	public PM1QuadTree(String name, int width, int height) {
		super(name, width, height);
		super.validator = new PM3Validator();
	}

	static class PM3Validator implements Validator {

		@Override
		public boolean valid(PMBlackNode node, City city) {
			return node.numRoads() == 0;
		}

		@Override
		public boolean valid(PMBlackNode node, City city1, City city2) {
			// if node.city is null then false
			// if it's not null then it should be one of the endpoints
			//
			City city = node.getCity();
			return city == null ? false : city.equals(city1) || city.equals(city2);
		}

	}

	@Override
	public DataStructure<City> reset() {
		return new PM1QuadTree(this.getName(), (int) this.getSpatialWidth(), (int) this.getSpatialHeight());
	}

}
