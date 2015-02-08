package cmsc420.exceptions;

public class CityAlreadyMappedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CityAlreadyMappedException() {
		this("cityAlreadyMapped");
	}

}
