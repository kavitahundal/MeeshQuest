package cmsc420.schema.spatial.PM;

import java.awt.geom.Point2D;

import cmsc420.schema.City;

public class PM1QuadTree extends PMQuadTree {

	public PM1QuadTree(String name, int width, int height) {
		super(name, width, height);
	}

	static class PM1Validator implements Validator {

		@Override
		public boolean valid(PMBlackNode node, Point2D.Float landmark) {
			return node.numRoads() == 0;
		}

		@Override
		public boolean valid(PMBlackNode node, Point2D.Float landmark1, Point2D.Float landmark2) {
			// if node.city is null then false
			// if it's not null then it should be one of the endpoints
			Point2D.Float landmark = node.getLandmark();
			return landmark == null || !(landmark instanceof City) ? false : landmark.equals(landmark1) || landmark.equals(landmark2);
		}

	}

	@Override
	public PMQuadTree reset() {
		return new PM1QuadTree(this.getName(), (int) this.getSpatialWidth(), (int) this.getSpatialHeight());
	}

	@Override
	protected Validator getValidator() {
		return new PM1Validator();
	}

}
