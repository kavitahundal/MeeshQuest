package cmsc420.schema.spatial;

public class PRQuadTreeSeedling implements Seedling {

	@Override
	public SpatialStructure generate(int width, int height) {
		return new PRQuadTree(width, height);
	}

}
