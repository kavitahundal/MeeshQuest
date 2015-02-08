package cmsc420.schema.dictionary;

import java.util.TreeMap;

import cmsc420.schema.City;
import cmsc420.schema.CityCoordinateComparator;
import cmsc420.schema.CityNameComparator;

public class TreeMapDictionary {

	private TreeMap<String, City> nameToCity;
	private TreeMap<City, String> cityToName;
	
	public TreeMapDictionary() {
		this.nameToCity = new TreeMap<>(new CityNameComparator());
		this.cityToName = new TreeMap<>(new CityCoordinateComparator());
	}
	
	public void insert(City city) {
		// TODO what if coordinates are the same?
		this.nameToCity.put(city.getName(), city);
		this.cityToName.put(city, city.getName());
	}
	
	public boolean containsName(String name) {
		return this.nameToCity.containsKey(name);
	}
	
	public boolean containsCoordinates(City city) {
		return this.cityToName.containsKey(city); // equals for city must only check coords!!!
		// in other words, don't override equals :)
	}
	
	public void delete(City city) {
		// precondition: contains returns true
		this.nameToCity.remove(city.getName());
		this.cityToName.remove(city);
	}
}
