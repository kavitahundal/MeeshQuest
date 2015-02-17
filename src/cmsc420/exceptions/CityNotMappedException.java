package cmsc420.exceptions;

/**
 * An exception class to catch cases where a city to be unmapped from a spatial
 * structure is not mapped to begin with.
 * 
 * @author Andrew Liu
 *
 */
public class CityNotMappedException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor. Sets the error message to reflect the exception
	 * type.
	 */
	public CityNotMappedException() {
		super("cityNotMapped");
	}

}
