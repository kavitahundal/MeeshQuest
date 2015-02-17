package cmsc420.exceptions;

/**
 * An exception class to catch cases where a request to list cities in a
 * dictionary cannot occur because there are no cities in the dictionary.
 * 
 * @author Andrew Liu
 *
 */
public class NoCitiesToListException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor. Sets the error message to reflect the exception
	 * type.
	 */
	public NoCitiesToListException() {
		super("noCitiesToList");
	}

}
