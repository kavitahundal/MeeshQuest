package cmsc420.schema.dictionary;

import cmsc420.schema.City;
import cmsc420.schema.CityCoordinateComparator;
import cmsc420.schema.CityNameComparator;
import cmsc420.schema.DataStructure;
import cmsc420.sortedmap.OldAvlGTree;

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
		super.nameToCity = new OldAvlGTree<>(new CityNameComparator(), this.g);
		super.cityToName = new OldAvlGTree<>(new CityCoordinateComparator(), this.g);
	}
	
	public AvlGTreeDictionary generate(int g) {
		return new AvlGTreeDictionary(g);
	}

	@Override
	public DataStructure<City> reset() {
		return new AvlGTreeDictionary(this.g);
	}
	
	public OldAvlGTree<String, City> getPrintingTree() {
		return (OldAvlGTree<String, City>) this.nameToCity;
	}
}
