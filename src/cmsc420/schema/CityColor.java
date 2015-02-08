package cmsc420.schema;

public enum CityColor {

	red ("red"),
	green ("green"),
	blue ("blue"),
	yellow ("yellow"),
	purple ("purple"),
	orange ("orange"),
	black ("black");
	
	private final String color;
	
	CityColor(String color) {
		this.color = color;
	}
	
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
	
	public String toString() {
		return this.color;
	}
}
