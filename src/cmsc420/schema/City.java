package cmsc420.schema;

import java.awt.geom.Point2D.Float;

public class City extends Float {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private CityColor color;
	
	public City(String name, float x, float y, CityColor color) {
		super(x, y);
		this.name = name;
		this.color = color;
	}

	public String getName() {
		return this.name;
	}
	
	public CityColor getColor() {
		return this.color;
	}
}
