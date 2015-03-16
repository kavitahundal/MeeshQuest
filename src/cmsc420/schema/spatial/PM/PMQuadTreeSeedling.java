package cmsc420.schema.spatial.PM;

import cmsc420.schema.spatial.Seedling;
import cmsc420.schema.spatial.SpatialStructure;

public class PMQuadTreeSeedling implements Seedling {
	
	private int order = 3;

	@Override
	public SpatialStructure generate(String name, float width, float height) {
		if (this.order == 3) {
			return new PM3QuadTree(name, (int) width, (int) height);
		} else {
			return new PM1QuadTree(name, (int) width, (int) height);
		}
	}
	
	public void setOrder(int order) {
		this.order = order;
	}

}
