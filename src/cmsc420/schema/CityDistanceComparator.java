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
		float firstDist = arg1.getDistance(x, y);
		float secondDist = arg0.getDistance(x, y);
		if (firstDist > secondDist) {
			return -1;
		} else if (secondDist > firstDist) {
			return 1;
		} else {
			return 0;
		}
	}

}
