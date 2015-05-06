package cmsc420.exceptions;

public class AirportDoesNotExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AirportDoesNotExistException() {
		super("airportDoesNotExist");
	}
}
