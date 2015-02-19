package cmsc420.sortedmap;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class AvlGTree<K,V> implements SortedMap<K, V> {

	private int size;
	private Comparator<K> comp;

	public AvlGTree(Comparator<K> comp) {
		this.comp = comp;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean containsKey(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsValue(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public V get(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}

	@Override
	public V put(K arg0, V arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public V remove(Object arg0) {
		throw new UnsupportedOperationException(); // implement in part 3
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public Comparator<? super K> comparator() {
		return this.comp;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public K firstKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedMap<K, V> headMap(K arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<K> keySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public K lastKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedMap<K, V> subMap(K arg0, K arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedMap<K, V> tailMap(K arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<V> values() {
		throw new UnsupportedOperationException();
	}
	
	static class Entry<K, V> implements Map.Entry<K,V> {

		@Override
		public K getKey() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public V getValue() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public V setValue(V value) {
			// TODO Auto-generated method stub
			return null;
		}
		//
	}

}
