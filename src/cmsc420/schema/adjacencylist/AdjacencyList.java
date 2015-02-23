package cmsc420.schema.adjacencylist;

import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;


public class AdjacencyList<T> {
	
	private TreeMap<T, List<T>> map;
	private Comparator<T> comp;

	public void clear() {
		// TODO implement
	}
}
