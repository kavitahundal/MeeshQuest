package cmsc420.schema;

import java.util.Comparator;

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
