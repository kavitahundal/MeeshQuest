package cmsc420.schema.dictionary;

import cmsc420.schema.City;
import cmsc420.schema.CityCoordinateComparator;
import cmsc420.schema.CityNameComparator;
import cmsc420.schema.DataStructure;
import cmsc420.sortedmap.AvlGTree;

/**
 * A dictionary that stores cities using TreeMaps as an internal mapping
 * structure.
 * 
 * @author Andrew Liu
 *
 */
public class AvlGTreeDictionary extends SortedMapDictionary {
	
	private final int g;

	/**
	 * Default constructor.
	 */
	public AvlGTreeDictionary(int g) {
		this.g = g;
		super.nameToCity = new AvlGTree<>(new CityNameComparator(), this.g);
		super.cityToName = new AvlGTree<>(new CityCoordinateComparator(), this.g);
	}

	@Override
	public DataStructure<City> reset() {
		return new AvlGTreeDictionary(this.g);
	}
	
	public AvlGTree<String, City> getPrintingTree() {
		return (AvlGTree<String, City>) this.nameToCity;
	}
}
