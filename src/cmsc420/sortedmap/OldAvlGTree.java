package cmsc420.sortedmap;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.schema.City;

public class OldAvlGTree<K, V> extends AbstractMap<K, V> implements SortedMap<K, V> {

	private int size;
	private final Comparator<? super K> comp;
	private final int g;
	private Entry<K, V> root;

	public OldAvlGTree(final Comparator<? super K> comp, final int g) {
		this.size = 0;
		this.comp = comp;
		this.g = g >= 1 ? g : 1;
		this.root = null;
	}

	public Element elementize(Document doc) {
		Element xmlRoot = doc.createElement("AvlGTree");
		xmlRoot.setAttribute("cardinality", "" + this.size);
		// add cardinality
		xmlRoot.setAttribute("height", "" + this.height());
		// height
		xmlRoot.setAttribute("maxImbalance", "" + this.g);
		// and maxImbalance
		xmlRoot.appendChild(this.root.elementize(doc));
		return xmlRoot;
	}

	public int height() {
		return this.heightAux(this.root);
	}

	private int heightAux(Entry<K, V> entry) {
		if (entry == null) {
			return 0;
		} else {
			return 1 + Math.max(heightAux(entry.left), heightAux(entry.right));
		}
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
		if (this.comp.compare(key, entry.key) < 0) {
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
		if (this.comp.compare(toAdd.key, entry.key) < 0) {
			if (entry.left == null) {
				entry.left = toAdd;
			} else {
				this.addAux(entry.left, toAdd);
			}
		} else { // this.comp.compare(toAdd.key, entry.key) > 0
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
					Entry<K, V> grandChild = child.left;
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
		if (entry != null) {
			entry.height = 1 + Math.max(height(entry.left), height(entry.right));
		}
	}

	private int balanceFactor(Entry<K, V> entry) {
		this.updateHeight(entry.left);
		this.updateHeight(entry.right);
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
		return new EntrySet(this);
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
		return new AvlSubMap(arg0, arg1);
	}

	@Override
	public SortedMap<K, V> tailMap(K arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<V> values() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean equals(Object o) {

		// Yes, this method was almost completely copied from AbstractMap...
		// I hope it's okay...
		if (o == this)
			return true;

		if (!(o instanceof Map))
			return false;
		@SuppressWarnings("unchecked")
		Map<K, V> m = (Map<K, V>) o;
		if (m.size() != size())
			return false;

		try {
			Iterator<java.util.Map.Entry<K, V>> i = entrySet().iterator();
			while (i.hasNext()) {
				java.util.Map.Entry<K, V> e = i.next();
				K key = e.getKey();
				V value = e.getValue();
				if (value == null) {
					if (!(m.get(key) == null && m.containsKey(key)))
						return false;
				} else {
					if (!value.equals(m.get(key)))
						return false;
				}
			}
		} catch (ClassCastException unused) {
			return false;
		} catch (NullPointerException unused) {
			return false;
		}

		return true;
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

		public Entry(Map.Entry<K, V> copy) {
			this.key = copy.getKey();
			this.value = copy.getValue();
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

		Element elementize(Document doc) {
			Element ele = doc.createElement("node");
			// set key attr to name
			ele.setAttribute("key", (String) this.key);
			// set valu attr to (x,y)
			City val = (City) this.value;
			String value = "(" + (int) val.x + "," + (int) val.y + ")";
			ele.setAttribute("value", value);
			// if left child is null add empty child else rec
			if (this.left == null) {
				ele.appendChild(doc.createElement("emptyChild"));
			} else {
				ele.appendChild(this.left.elementize(doc));
			}
			// if right child null add empty else rec
			if (this.right == null) {
				ele.appendChild(doc.createElement("emptyChild"));
			} else {
				ele.appendChild(this.right.elementize(doc));
			}
			return ele;
		}

	}

	class AvlSubMap extends AbstractMap<K, V> implements SortedMap<K, V> {

		private K lowerBound;
		private K upperBound;
		private int size;

		AvlSubMap(K lowerBound, K upperBound) {
			this.lowerBound = lowerBound;
			this.upperBound = upperBound;
			this.size = this.calculateSize(OldAvlGTree.this.root);
		}

		private int calculateSize(OldAvlGTree.Entry<K, V> entry) {
			if (entry == null) {
				return 0;
			}
			return this.calculateSize(entry.left) + this.calculateSize(entry.right) + 1;
		}

		private boolean inBounds(Object arg0) {
			@SuppressWarnings("unchecked")
			K key = (K) arg0;
			return OldAvlGTree.this.comp.compare(this.lowerBound, key) <= 0
					&& OldAvlGTree.this.comp.compare(key, this.upperBound) < 0;
		}

		@Override
		public void clear() {
			OldAvlGTree.this.clear(); // clear only the subset - for part 3
		}

		@Override
		public boolean containsKey(Object arg0) {
			return this.inBounds(arg0) ? OldAvlGTree.this.containsKey(arg0) : false;
		}

		@Override
		public boolean containsValue(Object arg0) {
			return this.inBounds(arg0) ? OldAvlGTree.this.containsValue(arg0) : false;
		}

		@Override
		public V get(Object arg0) {
			return this.inBounds(arg0) ? OldAvlGTree.this.get(arg0) : null;
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
			OldAvlGTree.Entry<K, V> entry = OldAvlGTree.this.findEntry(arg0);
			if (entry != null) {
				V ret = entry.value;
				entry.value = arg1;
				return ret;
			} else {
				this.size++;
				OldAvlGTree.this.put(arg0, arg1);
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
			return OldAvlGTree.this.comparator();
		}

		@Override
		public Set<java.util.Map.Entry<K, V>> entrySet() {
			return new EntrySet(this);
		}

		@Override
		public K firstKey() {
			if (OldAvlGTree.this.root == null) {
				throw new NoSuchElementException();
			}
			return this.firstKeyAux(OldAvlGTree.this.root);
		}

		private K firstKeyAux(OldAvlGTree.Entry<K, V> entry) {
			return entry.left == null || OldAvlGTree.this.comp.compare(entry.left.key, this.lowerBound) < 0 ? entry.key
					: this.firstKeyAux(entry.left);

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
			if (OldAvlGTree.this.root == null) {
				throw new NoSuchElementException();
			}
			return this.lastKeyAux(OldAvlGTree.this.root);
		}

		private K lastKeyAux(OldAvlGTree.Entry<K, V> entry) {
			return entry.right == null || OldAvlGTree.this.comp.compare(entry.right.key, this.upperBound) > 0 ? entry.key
					: this.lastKeyAux(entry.right);
		}

		@Override
		public SortedMap<K, V> subMap(K arg0, K arg1) {
			if (arg0 == null || arg1 == null) {
				throw new NullPointerException();
			}
			if (OldAvlGTree.this.comp.compare(arg0, arg1) > 0) {
				throw new IllegalArgumentException();
			}
			K lower = OldAvlGTree.this.comparator().compare(this.lowerBound, arg0) > 0 ? this.lowerBound : arg0;
			K upper = OldAvlGTree.this.comparator().compare(this.upperBound, arg1) < 0 ? this.upperBound : arg1;
			return OldAvlGTree.this.subMap(lower, upper);
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

	class EntrySet extends AbstractSet<Map.Entry<K, V>> implements Set<java.util.Map.Entry<K, V>> {

		private SortedMap<K, V> wrapper;

		EntrySet(SortedMap<K, V> wrapper) {
			this.wrapper = wrapper;
		}

		@Override
		public boolean add(java.util.Map.Entry<K, V> e) {
			if (this.contains(e)) {
				return false;
			} else {
				this.wrapper.put(e.getKey(), e.getValue());
				return true;
			}
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

		@SuppressWarnings("unchecked")
		@Override
		public boolean contains(Object o) {
			if (!(o instanceof java.util.Map.Entry<?, ?>)) {
				return false;
			}
			Object key = ((java.util.Map.Entry<?, ?>) o).getKey();
			Object value = ((java.util.Map.Entry<?, ?>) o).getValue();
			return value == null ? false : ((V) value).equals(this.wrapper.get(key));
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
			return new Iterator<java.util.Map.Entry<K, V>>() {

				Object[] entries = EntrySet.this.toArray();
				int index = 0;

				@Override
				public boolean hasNext() {
					return this.index < this.entries.length;
				}

				@SuppressWarnings("unchecked")
				@Override
				public java.util.Map.Entry<K, V> next() {
					return (java.util.Map.Entry<K, V>) this.entries[index++];
				}
			};
		}

		@Override
		public boolean remove(Object o) {
			return this.wrapper.remove(((Map.Entry<?, ?>) o).getKey(), ((Map.Entry<?, ?>) o).getValue());
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
			boolean ret = false;
			Iterator<java.util.Map.Entry<K, V>> iter = this.iterator();
			while (iter.hasNext()) {
				Map.Entry<K, V> next = iter.next();
				if (!c.contains(next)) {
					iter.remove();
					ret = true;
				}
			}
			return ret;
		}

		@Override
		public int size() {
			return this.wrapper.size();
		}

		@Override
		public Object[] toArray() {
			// to check if the AVL tree or subtree called this entry set
			List<Entry<K, V>> entries = new LinkedList<Entry<K, V>>();
			if (this.wrapper instanceof OldAvlGTree) {
				this.generateList(entries, OldAvlGTree.this.root);
			} else {
				this.generateList(entries, OldAvlGTree.this.root);
			}
			Object[] arr = new Object[this.size()];
			int index = 0;
			for (Entry<K, V> e : entries) {
				arr[index++] = e;
			}
			return arr;
		}

		private void generateList(List<Entry<K, V>> list, Entry<K, V> entry) {
			if (entry == null || list == null) {
				return;
			}
			this.generateList(list, entry.left);
			list.add(entry);
			this.generateList(list, entry.right);
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			if (a == null) {
				throw new NullPointerException();
			}
			if (a.length < this.size()) {
				a = (T[]) new Object[this.size()];
			}
			int index = 0;
			Iterator<java.util.Map.Entry<K, V>> iter = this.iterator();
			while (iter.hasNext()) {
				try {
					a[index++] = (T) new Entry<K, V>(iter.next());
				} catch (ClassCastException e) {
					throw new ArrayStoreException();
				}
			}
			return a;
		}

	}

}