package cmsc420.schema;

import java.util.Comparator;

/**
 * A comparator that compares the coordinates of cities. A lower x coordinate
 * grants precedence. If the x coordinates are equal, then the lower y
 * coordinate grants precedence.
 * 
 * @author Andrew Liu
 *
 */
public class CityCoordinateComparator implements Comparator<City> {

	@Override
	public int compare(City o1, City o2) {
		if (o1.x == o2.x) {
			if (o1.y == o2.y) {
				return 0;
			} else if (o1.y < o2.y) {
				return -1;
			} else { // o1.y > o2.y
				return 1;
			}
		} else if (o1.x < o2.x) {
			return -1;
		} else { // o1.x > o2.x
			return 1;
		}
	}

}
