package cmsc420.schema;

import java.awt.geom.Point2D.Float;

public class City extends Float {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private CityColor color;
	private int radius;
	
	public City(String name, float x, float y, CityColor color, int radius) {
		super(x, y);
		this.name = name;
		this.color = color;
		this.radius = radius;
	}

	public String getName() {
		return this.name;
	}
	
	public CityColor getColor() {
		return this.color;
	}
	
	public int getRadius() {
		return this.radius;
	}
	
	public float getDistance(int x, int y) {
		float dx = this.x - x;
		float dy = this.y - y;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}
	
	@Override
	public String toString() {
		return this.name + " " + this.x + " " + this.y + " " + this.radius + " " + this.color.toString();
	}
}
