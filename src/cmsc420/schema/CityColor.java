package cmsc420.schema;

/**
 * An enumerated type representing the possible colors of a city.
 * 
 * @author Andrew Liu
 *
 */
public enum CityColor {

	red("red"), green("green"), blue("blue"), yellow("yellow"), purple("purple"), orange("orange"), black("black");

	private final String color;

	CityColor(String color) {
		this.color = color;
	}

	/**
	 * A getter method for retrieve a CityColor object based of the string
	 * representation of the color.
	 * 
	 * @param color
	 *            the color as a string
	 * @return the color as a CityColor enum
	 */
	public static CityColor getCityColor(String color) {
		if (color.equals(red.color)) {
			return red;
		} else if (color.equals(green.color)) {
			return green;
		} else if (color.equals(blue.color)) {
			return blue;
		} else if (color.equals(yellow.color)) {
			return yellow;
		} else if (color.equals(purple.color)) {
			return purple;
		} else if (color.equals(orange.color)) {
			return orange;
		} else if (color.equals(black.color)) {
			return black;
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return this.color;
	}
}
