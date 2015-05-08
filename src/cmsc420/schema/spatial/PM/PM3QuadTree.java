package cmsc420.schema.spatial.PM;

import java.awt.geom.Point2D;

public class PM3QuadTree extends PMQuadTree{
	
	public PM3QuadTree(String name, int width, int height) {
		super(name, width, height);
	}

	static class PM3Validator implements Validator {

		@Override
		public boolean valid(PMBlackNode node, Point2D.Float landmark) {
			return true;
		}

		@Override
		public boolean valid(PMBlackNode node, Point2D.Float landmark1, Point2D.Float landmark2) {
			return true;
		}

	}

	public PMQuadTree reset() {
		return new PM3QuadTree(this.getName(), (int) this.getSpatialWidth(), (int) this.getSpatialHeight());
	}

	@Override
	protected Validator getValidator() {
		return new PM3Validator();
	}

}
