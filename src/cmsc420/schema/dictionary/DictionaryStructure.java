package cmsc420.schema.dictionary;

import java.util.List;

import cmsc420.schema.City;
import cmsc420.schema.DataStructure;
import cmsc420.schema.SortType;

/**
 * An interface representing a dictionary data structure.
 * 
 * @author Andrew
 *
 */
public interface DictionaryStructure extends DataStructure<City> {
	
	public boolean containsName(String name);
	public boolean containsCoordinates(City city);
	public void remove(String name);
	public int size();
	public City getCity(String name);
	public List<City> listCities(SortType sortBy);
}
