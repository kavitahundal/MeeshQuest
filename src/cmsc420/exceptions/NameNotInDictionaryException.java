package cmsc420.exceptions;

/**
 * An exception class to catch cases where a city to be mapped in a spatial
 * structure is not in its dictionary.
 * 
 * @author Andrew Liu
 *
 */
public class NameNotInDictionaryException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor. Sets the error message to reflect the exception
	 * type.
	 */
	public NameNotInDictionaryException() {
		super("nameNotInDictionary");
	}

}
