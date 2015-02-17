package cmsc420.exceptions;

/**
 * An exception class to catch cases where a city is out of the bounds of a
 * spatial structure that it is being added to.
 * 
 * @author Andrew Liu
 *
 */
public class CityOutOfBoundsException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor. Sets the error message to reflect the exception
	 * type.
	 */
	public CityOutOfBoundsException() {
		super("cityOutOfBounds");
	}

}
