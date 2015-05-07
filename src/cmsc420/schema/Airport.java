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
	
	public Airport(String name, String airlineName, int localX, int localY, int remoteX, int remoteY) {
		super(localX, localY);
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
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Airport)) {
			return false;
		}
		Airport a = (Airport) other;
		return super.equals(other) && this.remoteX == a.remoteX && this.remoteY == a.remoteY;
	}
}
