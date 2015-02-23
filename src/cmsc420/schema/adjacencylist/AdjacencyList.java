package cmsc420.schema.adjacencylist;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class AdjacencyList<T> {

	private TreeMap<T, TreeSet<T>> map;
	private Comparator<T> comp;

	public AdjacencyList(Comparator<T> comp) {
		this.comp = comp;
		this.map = new TreeMap<>(this.comp);
	}

	public void addUndirectedEdge(T first, T second) {
		this.addDirectedEdge(first, second);
		this.addDirectedEdge(second, first);
	}

	public void addDirectedEdge(T source, T destination) {
		TreeSet<T> destSet = this.map.get(source);
		if (destSet == null) {
			destSet = new TreeSet<>(this.comp);
			this.map.put(source, destSet);
		}
		destSet.add(destination);
	}

	public void clear() {
		this.map = new TreeMap<>(this.comp);
	}

	public void removeUndirectedEdge(T first, T second) {
		this.removeDirectedEdge(first, second);
		this.removeDirectedEdge(second, first);
	}

	public void removeDirectedEdge(T source, T destination) {
		TreeSet<T> destSet = this.map.get(source);
		if (destSet != null) {
			destSet.remove(destination);
		}
	}

	public boolean containsUndirectedEdge(T first, T second) {
		return this.containsDirectedEdge(first, second) && this.containsDirectedEdge(second, first);
	}

	public boolean containsDirectedEdge(T source, T destination) {
		TreeSet<T> destSet = this.map.get(source);
		return destSet == null ? false : destSet.contains(destination);
	}
	
	public Set<T> neighbors(T source) {
		return this.map.get(source);
	}
}
