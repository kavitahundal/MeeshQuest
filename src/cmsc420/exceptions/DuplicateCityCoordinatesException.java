package cmsc420.exceptions;

/**
 * An exception class to catch cases where a dictionary already contains the
 * same coordinates of a city to be added to the dictionary.
 * 
 * @author Andrew Liu
 *
 */
public class DuplicateCityCoordinatesException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor. Sets the error message to reflect the exception
	 * type.
	 */
	public DuplicateCityCoordinatesException() {
		super("duplicateCityCoordinates");
	}

}
