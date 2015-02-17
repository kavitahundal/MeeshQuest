package cmsc420.exceptions;

/**
 * An exception class to catch cases where the dictionary already contains the
 * name of a new city to be added.
 * 
 * @author Andrew Liu
 *
 */
public class DuplicateCityNameException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor. Sets the error message to reflect the exception
	 * type.
	 */
	public DuplicateCityNameException() {
		super("duplicateCityName");
	}

}
