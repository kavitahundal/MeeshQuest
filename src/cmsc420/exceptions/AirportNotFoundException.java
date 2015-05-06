package cmsc420.exceptions;

public class AirportNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AirportNotFoundException() {
		super("airportNotFound");
	}

}
