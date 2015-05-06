package cmsc420.schema;

import java.awt.geom.Point2D;

public class Airport extends Point2D.Float {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String airlineName;
	public int remoteX;
	public int remoteY;
	
	public Airport(String name, String airlineName, int remoteX, int remoteY) {
		this.name = name;
		this.airlineName = airlineName;
		this.remoteX = remoteX;
		this.remoteY = remoteY;
	}

	public String getName() {
		return this.name;
	}
	
	public String getAirlineName() {
		return this.airlineName;
	}
}
