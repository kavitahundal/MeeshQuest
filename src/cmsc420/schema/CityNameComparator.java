package cmsc420.schema;

import java.util.Comparator;

/**
 * A comparator that compares the lexicographical ranking of the names of
 * cities.
 * 
 * @author Andrew Liu
 *
 */
public class CityNameComparator implements Comparator<String> {

	@Override
	public int compare(String arg0, String arg1) {
		return arg0.compareTo(arg1);
	}

}
