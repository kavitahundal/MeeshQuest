package cmsc420.schema.dictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import cmsc420.schema.City;
import cmsc420.schema.CityCoordinateComparator;
import cmsc420.schema.CityNameComparator;
import cmsc420.schema.DataStructure;
import cmsc420.schema.SortType;

/**
 * A dictionary that stores cities using TreeMaps as an internal mapping
 * structure.
 * 
 * @author Andrew Liu
 *
 */
public class TreeMapDictionary implements DictionaryStructure {

	private TreeMap<String, City> nameToCity;
	private TreeMap<City, String> cityToName;

	/**
	 * Default constructor.
	 */
	public TreeMapDictionary() {
		this.nameToCity = new TreeMap<>(new CityNameComparator());
		this.cityToName = new TreeMap<>(new CityCoordinateComparator());
	}

	@Override
	public void add(City city) {
		this.nameToCity.put(city.getName(), city);
		this.cityToName.put(city, city.getName());
	}

	@Override
	public boolean contains(City city) {
		return this.containsName(city.getName()) || this.containsCoordinates(city);
	}

	@Override
	public boolean containsName(String name) {
		return this.nameToCity.containsKey(name);
	}

	@Override
	public boolean containsCoordinates(City city) {
		// only checks coordinates (equals method is not overwritten)
		return this.cityToName.containsKey(city);
	}

	@Override
	public void remove(City city) {
		this.nameToCity.remove(city.getName());
		this.cityToName.remove(city);
	}

	@Override
	public void remove(String name) {
		this.cityToName.remove(this.nameToCity.get(name));
		this.nameToCity.remove(name);
	}

	@Override
	public int size() {
		return this.nameToCity.size();
	}

	@Override
	public DataStructure<City> reset() {
		return new TreeMapDictionary();
	}

	@Override
	public City getCity(String name) {
		return this.nameToCity.get(name);
	}

	@Override
	public List<City> listCities(SortType sortBy) {
		if (sortBy.equals(SortType.name)) {
			Iterator<String> iter = this.nameToCity.keySet().iterator();
			List<City> cities = new ArrayList<>();
			while (iter.hasNext()) {
				String name = iter.next();
				cities.add(this.nameToCity.get(name));
			}
			return cities;
		} else { // already lexicographically sorted
			return new ArrayList<City>(this.cityToName.keySet());
		}
	}
}
