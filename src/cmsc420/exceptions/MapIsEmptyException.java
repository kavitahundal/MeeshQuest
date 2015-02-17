package cmsc420.exceptions;

/**
 * An exception class to catch cases where an empty spatial structure is called
 * to be printed.
 * 
 * @author Andrew Liu
 *
 */
public class MapIsEmptyException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor. Sets the error message to reflect the exception
	 * type.
	 */
	public MapIsEmptyException() {
		super("mapIsEmpty");
	}

}
