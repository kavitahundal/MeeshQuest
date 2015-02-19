package cmsc420.sortedmap;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class AvlGTree<K, V> implements SortedMap<K, V> {

	private int size;
	private final Comparator<? super K> comp;
	private final int g;
	private Entry<K, V> root;

	public AvlGTree(final Comparator<? super K> comp, final int g) {
		this.size = 0;
		this.comp = comp;
		this.g = g;
		this.root = null;
	}

	@Override
	public void clear() {
		this.root = null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean containsKey(Object arg0) {
		if (arg0 == null) {
			throw new NullPointerException();
		}
		return this.findEntry((K) arg0) != null;
	}

	private Entry<K, V> findEntry(K key) {
		return this.findEntryAux(this.root, key);
	}

	private Entry<K, V> findEntryAux(Entry<K, V> entry, K key) {
		if (entry == null) {
			return null;
		}
		if (this.comp.compare(key, entry.key) > 0) {
			return this.findEntryAux(entry.left, key);
		} else if (this.comp.compare(key, entry.key) > 0) {
			return this.findEntryAux(entry.right, key);
		} else {
			return entry;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean containsValue(Object arg0) {
		if (arg0 == null) {
			throw new NullPointerException();
		}
		return this.containsValueAux(this.root, (V) arg0);
	}

	private boolean containsValueAux(Entry<K, V> entry, V value) {
		if (entry == null) {
			return false;
		} else if (entry.value.equals(value)) {
			return true;
		} else {
			return this.containsValueAux(entry.left, value) || this.containsValueAux(entry.right, value);
		}
	}

	@Override
	public V get(Object arg0) {
		if (arg0 == null) {
			throw new NullPointerException();
		}
		@SuppressWarnings("unchecked")
		Entry<K, V> entry = this.findEntry((K) arg0);
		return entry == null ? null : entry.value;
	}

	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}

	@Override
	public V put(K arg0, V arg1) {
		if (arg0 == null || arg1 == null) {
			throw new NullPointerException();
		}
		Entry<K, V> entry = this.findEntry(arg0);
		if (entry != null) {
			V ret = entry.value;
			entry.value = arg1;
			return ret;
		} else {
			this.size++;
			this.add(new Entry<K, V>(arg0, arg1));
			return null;
		}
	}

	private void add(Entry<K, V> toAdd) {
		if (this.root == null) {
			this.root = toAdd;
		} else {
			this.addAux(this.root, toAdd);
		}
	}

	private void addAux(Entry<K, V> entry, Entry<K, V> toAdd) {
		// TODO implement -- need to keep avl-ness
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> arg0) {
		if (arg0 == null) {
			throw new NullPointerException();
		}
		for (K key : arg0.keySet()) {
			this.put(key, arg0.get(key));
		}
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
		if (this.root == null) {
			throw new NoSuchElementException();
		}
		return this.firstKeyAux(this.root);
	}

	private K firstKeyAux(Entry<K, V> entry) {
		return entry.left == null ? entry.key : this.firstKeyAux(entry.left);
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
		if (this.size == 0) {
			throw new NoSuchElementException();
		}
		return this.lastKeyAux(this.root);
	}

	private K lastKeyAux(Entry<K, V> entry) {
		return entry.right == null ? entry.key : this.firstKeyAux(entry.right);
	}

	@Override
	public SortedMap<K, V> subMap(K arg0, K arg1) {
		if (arg0 == null || arg1 == null) {
			throw new NullPointerException();
		}
		if (comp.compare(arg0, arg1) > 0) {
			throw new IllegalArgumentException();
		}
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

	static class Entry<K, V> implements Map.Entry<K, V> {

		K key;
		V value;
		Entry<K, V> left;
		Entry<K, V> right;
		// add height variable?

		public Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() {
			return this.key;
		}

		@Override
		
		public V getValue() {
			return this.value;
		}

		@Override
		public V setValue(V value) {
			V oldValue = this.value;
			this.value = value;
			return oldValue;
		}

	}

}
