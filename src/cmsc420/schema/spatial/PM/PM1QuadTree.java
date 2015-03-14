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
		public boolean valid() {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

	@Override
	public DataStructure<City> reset() {
		return new PM1QuadTree(this.getName(), (int) this.getSpatialWidth(), (int) this.getSpatialHeight());
	}

	// TODO in part 3
}
