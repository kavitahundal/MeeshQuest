package cmsc420.exceptions;

public class DuplicateAirportNameException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateAirportNameException() {
		super("duplicateAirportName");
	}
}
