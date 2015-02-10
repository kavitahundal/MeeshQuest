package cmsc420.schema;

import java.util.Comparator;

public enum SortType {

	name,
	coordinate;
	
	public static SortType getSortType(String sortType) {
		if (sortType.equals("name")) {
			return name;
		} else if (sortType.equals("coordinate")) {
			return coordinate;
		} else {
			return null;
		}
	}
	
	public String toString() {
		if (this.equals(SortType.name)) {
			return "name";
		} else {
			return "coordinate";
		}
	}
	
	public Comparator<?> comparator() {
		if (this.equals(SortType.name)) {
			return new CityNameComparator();
		} else {
			return new CityCoordinateComparator();
		}
	}
}
