package cmsc420.schema;

import java.util.Comparator;

/**
 * An enum for the two methods of sorting cities. Cities can be sorted by name
 * or by coordinate.
 * 
 * @author Andrew Liu
 *
 */
public enum SortType {

	name, coordinate;

	/**
	 * A getter method for the SortType object based off the string
	 * representation of the sorting method.
	 * 
	 * @param sortType
	 *            the string representation of the sorting method
	 * @return the SortType object representing the sorting method
	 */
	public static SortType getSortType(String sortType) {
		if (sortType.equals("name")) {
			return name;
		} else if (sortType.equals("coordinate")) {
			return coordinate;
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		if (this.equals(SortType.name)) {
			return "name";
		} else {
			return "coordinate";
		}
	}

	/**
	 * Gets the comparator associated with the SortType.
	 * 
	 * @return the comparator that corresponds with the sorting type
	 */
	public Comparator<?> comparator() {
		if (this.equals(SortType.name)) {
			return new CityNameComparator();
		} else {
			return new CityCoordinateComparator();
		}
	}
}
