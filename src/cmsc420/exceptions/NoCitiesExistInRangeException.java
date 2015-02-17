package cmsc420.exceptions;

/**
 * An exception class to catch cases where no cities exist in the range of a
 * search space within a spatial structure.
 * 
 * @author Andrew Liu
 *
 */
public class NoCitiesExistInRangeException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor. Sets the error message to reflect the exception
	 * type.
	 */
	public NoCitiesExistInRangeException() {
		super("noCitiesExistInRange");
	}

}
