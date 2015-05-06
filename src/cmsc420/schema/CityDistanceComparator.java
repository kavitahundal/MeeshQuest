package cmsc420.schema;

import java.awt.geom.Point2D;
import java.util.Comparator;

/**
 * A comparator to compare the relative distances of two cities from a given
 * coordinate.
 * 
 * @author Andrew Liu
 *
 */
public class CityDistanceComparator implements Comparator<Point2D.Float> {

	private int x;
	private int y;

	/**
	 * A constructor that gives the coordinate to act as the point to calculate
	 * distance for each city.
	 * 
	 * @param width
	 * @param height
	 */
	public CityDistanceComparator(int width, int height) {
		this.x = width;
		this.y = height;
	}

	@Override
	public int compare(Point2D.Float arg0, Point2D.Float arg1) {
		float firstDist = (float) arg1.distance(x, y);
		float secondDist = (float) arg0.distance(x, y);
		if (firstDist > secondDist) {
			return -1;
		} else if (secondDist > firstDist) {
			return 1;
		} else {
			return 0;
		}
	}

}
