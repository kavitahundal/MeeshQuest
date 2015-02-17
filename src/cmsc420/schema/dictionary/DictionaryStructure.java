package cmsc420.schema.dictionary;

import java.util.List;

import cmsc420.schema.City;
import cmsc420.schema.DataStructure;
import cmsc420.schema.SortType;

/**
 * An interface representing a dictionary data structure.
 * 
 * @author Andrew Liu
 *
 */
public interface DictionaryStructure extends DataStructure<City> {

	/**
	 * Checks if the dictionary contains a city with the given name.
	 * 
	 * @param name
	 *            the name to check
	 * @return whether the city with the name exists in the dictionary
	 */
	public boolean containsName(String name);

	/**
	 * Checks if the dictionary contains a city with the given coordinates.
	 * 
	 * @param city
	 *            the city with the coordinates to check
	 * @return whether the city with the coordinates exists in the dictionary
	 */
	public boolean containsCoordinates(City city);

	/**
	 * Removes the city with the given name from the dictionary.
	 * 
	 * @param name
	 *            the name of the city to remove
	 */
	public void remove(String name);

	/**
	 * Gets the number of cities in the dictionary.
	 * 
	 * @return the size of the dictionary
	 */
	public int size();

	/**
	 * Gets the city with the given name from the dictionary.
	 * 
	 * @param name
	 *            the name of the city to retrieve
	 * @return the city with the given name
	 */
	public City getCity(String name);

	/**
	 * Gets a list of all the cities in the dictionary, sorted by a selected
	 * method.
	 * 
	 * @param sortBy
	 *            the method of sorting the cities
	 * @return a sorted list of all the cities in the dictionary
	 */
	public List<City> listCities(SortType sortBy);
}
