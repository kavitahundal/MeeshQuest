package cmsc420.schema.adjacencylist;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class AdjacencyList<T> implements Iterable<T[]> {

	private TreeMap<T, TreeSet<T>> map;
	private Comparator<T> comp;
	private int size;

	public AdjacencyList(Comparator<T> comp) {
		this.size = 0;
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
		if (!destSet.contains(destination)) {
			destSet.add(destination);
			this.size++;
		}
	}

	public void clear() {
		this.map = new TreeMap<>(this.comp);
		this.size = 0;
	}

	public void removeUndirectedEdge(T first, T second) {
		this.removeDirectedEdge(first, second);
		this.removeDirectedEdge(second, first);
	}

	public void removeDirectedEdge(T source, T destination) {
		TreeSet<T> destSet = this.map.get(source);
		if (destSet != null && destSet.contains(destination)) {
			this.size--;
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
	
	public int size() {
		return this.size;
	}
	
	public boolean isIsolated(T ele) { // warning! make sure the city exists
		return this.neighbors(ele) == null || this.neighbors(ele).size() == 0;
	}

	@Override
	public Iterator<T[]> iterator() {
		return new Iterator<T[]>() {
			
			Iterator<T[]> wrapper;
			
			{
				List<T[]> elements = new LinkedList<>();
				Iterator<T> sources = AdjacencyList.this.map.keySet().iterator();
				while (sources.hasNext()) {
					T source = sources.next();
					Iterator<T> destinations = AdjacencyList.this.neighbors(source).iterator();
					while (destinations.hasNext()) {
						T destination = destinations.next();
						@SuppressWarnings("unchecked")
						T[] element = (T[]) new Object[2];
						element[0] = source;
						element[1] = destination;
						elements.add(element);
					}
				}
				this.wrapper = elements.iterator();
			}

			@Override
			public boolean hasNext() {
				return this.wrapper.hasNext();
			}

			@Override
			public T[] next() {
				return this.wrapper.next();
			}
			
		};
	}

}
