package cmsc420.schema.dictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;

import cmsc420.schema.City;
import cmsc420.schema.SortType;

/**
 * A dictionary that stores cities using SortedMaps as an internal mapping
 * structure.
 * 
 * @author Andrew Liu
 *
 */
public abstract class SortedMapDictionary implements DictionaryStructure {

	protected SortedMap<String, City> nameToCity;
	protected SortedMap<City, String> cityToName;

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
	public City getCity(String name) {
		return this.nameToCity.get(name);
	}

	@Override
	public List<City> listCities(SortType sortBy) {
		if (sortBy.equals(SortType.name)) {
			Iterator<Entry<String, City>> iter = this.nameToCity.entrySet().iterator();
			List<City> cities = new ArrayList<>();
			while (iter.hasNext()) {
				String name = iter.next().getKey();
				cities.add(this.nameToCity.get(name));
			}
			return cities;
		} else { // already lexicographically sorted
			List<City> cities = new ArrayList<>();
			Iterator<Entry<City, String>> iter = this.cityToName.entrySet().iterator();
			while (iter.hasNext()) {
				cities.add(iter.next().getKey());
			}
			return cities;
		}
	}
}
