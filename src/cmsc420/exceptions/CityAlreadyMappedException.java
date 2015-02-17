package cmsc420.exceptions;

/**
 * An exception class to catch cases where a city has already been mapped in a
 * dictionary.
 * 
 * @author Andrew Liu
 *
 */
public class CityAlreadyMappedException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor. Sets the error message to reflect the exception
	 * type.
	 */
	public CityAlreadyMappedException() {
		super("cityAlreadyMapped");
	}

}
