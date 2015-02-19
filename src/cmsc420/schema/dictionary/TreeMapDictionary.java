package cmsc420.schema.dictionary;

import java.util.TreeMap;

import cmsc420.schema.City;
import cmsc420.schema.CityCoordinateComparator;
import cmsc420.schema.CityNameComparator;
import cmsc420.schema.DataStructure;

/**
 * A dictionary that stores cities using TreeMaps as an internal mapping
 * structure.
 * 
 * @author Andrew Liu
 *
 */
public class TreeMapDictionary extends SortedMapDictionary {

	/**
	 * Default constructor.
	 */
	public TreeMapDictionary() {
		super.nameToCity = new TreeMap<>(new CityNameComparator());
		super.cityToName = new TreeMap<>(new CityCoordinateComparator());
	}

	@Override
	public DataStructure<City> reset() {
		return new TreeMapDictionary();
	}

}
