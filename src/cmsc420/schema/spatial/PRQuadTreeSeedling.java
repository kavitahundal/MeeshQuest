package cmsc420.schema.spatial;


public class PRQuadTreeSeedling implements Seedling {

	@Override
	public SpatialStructure generate(String name, float width, float height) {
		return new PRQuadTree(name, width, height);
	}

}
