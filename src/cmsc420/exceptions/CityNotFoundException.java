package cmsc420.exceptions;

public class CityNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CityNotFoundException() {
		super("cityNotFound");
	}

}
