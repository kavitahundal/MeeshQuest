package cmsc420.exceptions;

/**
 * An exception class to catch cases where a city to be deleted from a
 * dictionary does not exist in the dictionary.
 * 
 * @author Andrew Liu
 *
 */
public class CityDoesNotExistException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor. Sets the error message to reflect the exception
	 * type.
	 */
	public CityDoesNotExistException() {
		super("cityDoesNotExist");
	}

}
