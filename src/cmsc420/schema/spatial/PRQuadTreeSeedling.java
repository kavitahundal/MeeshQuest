package cmsc420.schema.spatial;

public class PRQuadTreeSeedling implements Seedling {

	@Override
	public SpatialStructure generate(float width, float height) {
		return new PRQuadTree(width, height);
	}

}
