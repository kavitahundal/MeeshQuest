package cmsc420.schema.dictionary;

import cmsc420.schema.City;
import cmsc420.schema.DataStructure;

public interface DictionaryStructure extends DataStructure<City> {
	
	public boolean containsName(String name);
	public boolean containsCoordinates(City city);
	public void remove(String name);
	public int size();
	public City getCity(String name);
}
