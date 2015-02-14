package cmsc420.schema;

import java.util.Comparator;

public class CityDistanceComparator implements Comparator<City> {
	
	private int x;
	private int y;
	
	public CityDistanceComparator(int width, int height) {
		this.x = width;
		this.y = height;
	}

	@Override
	public int compare(City arg0, City arg1) {
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
