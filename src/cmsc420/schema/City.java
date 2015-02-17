package cmsc420.schema;

import java.awt.geom.Point2D.Float;

/**
 * A class representing a city. Cities have names, locations, colors, and a
 * radius.
 * 
 * @author Andrew Liu
 *
 */
public class City extends Float {

	private static final long serialVersionUID = 1L;
	private String name;
	private CityColor color;
	private int radius;

	/**
	 * The public constructor for a city.
	 * 
	 * @param name
	 *            the name of the city
	 * @param x
	 *            the x coordinate of the city
	 * @param y
	 *            the y coordinate of the city
	 * @param color
	 *            the color of the city
	 * @param radius
	 *            the radius of the city
	 */
	public City(String name, float x, float y, CityColor color, int radius) {
		super(x, y);
		this.name = name;
		this.color = color;
		this.radius = radius;
	}

	/**
	 * A getter method for the city's name.
	 * 
	 * @return the name of the city
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * A getter method for the city's color.
	 * 
	 * @return the color of the city
	 */
	public CityColor getColor() {
		return this.color;
	}

	/**
	 * A getter method for the city's radius.
	 * 
	 * @return the radius of the city
	 */
	public int getRadius() {
		return this.radius;
	}

	@Override
	public String toString() {
		return this.name + " " + this.x + " " + this.y + " " + this.radius + " " + this.color.toString();
	}
}
