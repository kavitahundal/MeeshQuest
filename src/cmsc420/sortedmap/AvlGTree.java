package cmsc420.sortedmap;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
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
			return this.containsValueAux(entry.left, value)
					|| this.containsValueAux(entry.right, value);
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
		if (this.comp.compare(toAdd.key, entry.key) < 0) {
			if (entry.left == null) {
				entry.left = toAdd;
			} else {
				this.addAux(entry.left, toAdd);
			}
		} else { // this.comp.compare(toAdd.key, entry.key) < 0
			if (entry.right == null) {
				entry.right = toAdd;
			} else {
				this.addAux(entry.right, toAdd);
			}
		}
		this.balanceCheck(entry);
	}

	private void balanceCheck(Entry<K, V> entry) {
		int balanceFactor = this.balanceFactor(entry);
		if (Math.abs(balanceFactor) > this.g) {
			if (balanceFactor > 0) {
				// left x case
				if (balanceFactor(entry.left) < 0) {
					// left right case
					Entry<K, V> child = entry.left;
					Entry<K, V> grandChild = child.right;
					child.right = grandChild.left;
					grandChild.left = child;
					entry.left = grandChild;
				}
				// left left case
				Entry<K, V> child = entry.left;
				Entry<K, V> grandChild = child.left;
				this.swapData(entry, child);
				entry.left = grandChild;
				child.left = child.right;
				child.right = entry.right;
				entry.right = child;
				this.updateHeight(entry);
				this.updateHeight(child);
				this.updateHeight(grandChild);
			} else {
				// right x case
				if (balanceFactor(entry.right) > 0) {
					// right left case
					Entry<K, V> child = entry.right;
					Entry<K, V> grandChild = child.right;
					child.left = grandChild.right;
					grandChild.right = child;
					entry.right = grandChild;
				}
				// right right case
				Entry<K, V> child = entry.right;
				Entry<K, V> grandChild = child.right;
				this.swapData(entry, child);
				entry.right = grandChild;
				child.right = child.left;
				child.left = entry.left;
				entry.left = child;
				this.updateHeight(entry);
				this.updateHeight(child);
				this.updateHeight(grandChild);
			}
		}
	}

	private void swapData(Entry<K, V> first, Entry<K, V> second) {
		K tempKey = first.key;
		V tempVal = first.value;
		first.key = second.key;
		first.value = second.value;
		second.key = tempKey;
		second.value = tempVal;
	}

	private int height(Entry<K, V> entry) {
		return entry == null ? 0 : entry.height;
	}

	private void updateHeight(Entry<K, V> entry) {
		entry.height = Math.max(height(entry.left), height(entry.right));
	}

	private int balanceFactor(Entry<K, V> entry) {
		return height(entry.left) - height(entry.right);
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
		return new EntrySet<K, V>(this);
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
		return new AvlSubMap<K, V>(this, arg0, arg1);
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
		int height;

		public Entry(K key, V value) {
			this.key = key;
			this.value = value;
			this.height = 1;
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

		public int yes() {
			return 45;
		}

	}

	static class AvlSubMap<K, V> implements SortedMap<K, V> {

		private AvlGTree<K, V> wrapper;
		private K lowerBound;
		private K upperBound;
		private int size;

		AvlSubMap(AvlGTree<K, V> wrapper, K lowerBound, K upperBound) {
			this.wrapper = wrapper;
			this.lowerBound = lowerBound;
			this.upperBound = upperBound;
			this.size = this.calculateSize(this.wrapper.root);
		}

		private int calculateSize(AvlGTree.Entry<K, V> entry) {
			if (entry == null) {
				return 0;
			}
			return this.calculateSize(entry.left)
					+ this.calculateSize(entry.right) + 1;
		}

		private boolean inBounds(Object arg0) {
			@SuppressWarnings("unchecked")
			K key = (K) arg0;
			return this.wrapper.comparator().compare(this.lowerBound, key) <= 0
					&& this.wrapper.comparator().compare(key, this.upperBound) < 0;
		}

		@Override
		public void clear() {
			this.wrapper.clear();
		}

		@Override
		public boolean containsKey(Object arg0) {
			return this.inBounds(arg0) ? this.wrapper.containsKey(arg0) : false;
		}

		@Override
		public boolean containsValue(Object arg0) {
			return this.inBounds(arg0) ? this.wrapper.containsValue(arg0)
					: false;
		}

		@Override
		public V get(Object arg0) {
			return this.inBounds(arg0) ? this.wrapper.get(arg0) : null;
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
			AvlGTree.Entry<K, V> entry = this.wrapper.findEntry(arg0);
			if (entry != null) {
				V ret = entry.value;
				entry.value = arg1;
				return ret;
			} else {
				this.size++;
				this.wrapper.put(arg0, arg1);
				return null;
			}
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
			return this.wrapper.comparator();
		}

		@Override
		public Set<java.util.Map.Entry<K, V>> entrySet() {
			return new EntrySet<K, V>(this);
		}

		@Override
		public K firstKey() {
			if (this.wrapper.root == null) {
				throw new NoSuchElementException();
			}
			return this.firstKeyAux(this.wrapper.root);
		}

		private K firstKeyAux(AvlGTree.Entry<K, V> entry) {
			return entry.left == null
					|| this.wrapper.comp.compare(entry.left.key,
							this.lowerBound) < 0 ? entry.key : this
					.firstKeyAux(entry.left);

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
			if (this.wrapper.root == null) {
				throw new NoSuchElementException();
			}
			return this.lastKeyAux(this.wrapper.root);
		}

		private K lastKeyAux(AvlGTree.Entry<K, V> entry) {
			return entry.right == null
					|| this.wrapper.comp.compare(entry.right.key,
							this.upperBound) > 0 ? entry.key : this
					.lastKeyAux(entry.right);
		}

		@Override
		public SortedMap<K, V> subMap(K arg0, K arg1) {
			if (arg0 == null || arg1 == null) {
				throw new NullPointerException();
			}
			if (this.wrapper.comp.compare(arg0, arg1) > 0) {
				throw new IllegalArgumentException();
			}
			K lower = this.wrapper.comparator().compare(this.lowerBound, arg0) > 0 ? this.lowerBound
					: arg0;
			K upper = this.wrapper.comparator().compare(this.upperBound, arg1) < 0 ? this.upperBound
					: arg1;
			return new AvlSubMap<K, V>(this.wrapper, lower, upper);
		}

		@Override
		public SortedMap<K, V> tailMap(K arg0) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Collection<V> values() {
			throw new UnsupportedOperationException();
		}

	}
	
	static class EntrySet<K, V> implements Set<java.util.Map.Entry<K, V>> {
		
		private SortedMap<K, V> wrapper;
		
		EntrySet(SortedMap<K, V> wrapper) {
			this.wrapper = wrapper;
		}

		@Override
		public boolean add(java.util.Map.Entry<K, V> e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean addAll(Collection<? extends java.util.Map.Entry<K, V>> c) {
			boolean ret = false;
			for (java.util.Map.Entry<K, V> entry : c) {
				ret = ret || this.add(entry);
			}
			return ret;
		}

		@Override
		public void clear() {
			this.wrapper.clear();
		}

		@Override
		public boolean contains(Object o) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			for (Object entry : c) {
				if (!this.contains(entry)) {
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean isEmpty() {
			return this.wrapper.size() == 0;
		}

		@Override
		public Iterator<java.util.Map.Entry<K, V>> iterator() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean remove(Object o) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			boolean ret = false;
			for (Object entry : c) {
				ret = ret || this.remove(entry);
			}
			return ret;
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int size() {
			return this.wrapper.size();
		}

		@Override
		public Object[] toArray() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> T[] toArray(T[] a) {
			// TODO Auto-generated method stub
			return null;
		}
		//
	}

}
