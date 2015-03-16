package cmsc420.schema.spatial.PM;

import cmsc420.schema.City;
import cmsc420.schema.DataStructure;

public class PM3QuadTree extends PMQuadTree{
	
	public PM3QuadTree(String name, int width, int height) {
		super(name, width, height);
	}

	static class PM3Validator implements Validator {

		@Override
		public boolean valid(PMBlackNode node, City city) {
			return true;
		}

		@Override
		public boolean valid(PMBlackNode node, City city1, City city2) {
			return true;
		}

	}

	@Override
	public DataStructure<City> reset() {
		return new PM3QuadTree(this.getName(), (int) this.getSpatialWidth(), (int) this.getSpatialHeight());
	}

	@Override
	protected Validator getValidator() {
		return new PM3Validator();
	}

}
