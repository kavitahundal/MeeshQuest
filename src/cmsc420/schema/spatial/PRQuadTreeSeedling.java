package cmsc420.schema.spatial;

import cmsc420.drawing.CanvasPlus;

public class PRQuadTreeSeedling implements Seedling {

	@Override
	public SpatialStructure generate(float width, float height, CanvasPlus canvas) {
		return new PRQuadTree(width, height, canvas);
	}

}
