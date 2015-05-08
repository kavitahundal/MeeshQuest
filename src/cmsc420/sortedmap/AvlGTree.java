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
import java.util.Stack;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.schema.City;

public class AvlGTree<K, V> extends AbstractMap<K, V> implements SortedMap<K, V> {

	private final Comparator<? super K> comp;
	private final int g;
	private AvlNode<K, V> root;
	private int size;
	
	public void debugPrint() {
		this.debugAux(this.root);
		System.out.println();
	}
	
	private void debugAux(AvlNode<K, V> node) {
		if (node == null) {
			return;
		}
		System.out.print("(");
		this.debugAux(node.left);
		System.out.print(" ");
		System.out.print(node.key);
		System.out.print(" ");
		this.debugAux(node.right);
		System.out.print(")");
	}

	public AvlGTree() {
		this(null, 1);
	}

	public AvlGTree(final Comparator<? super K> comp) {
		this(comp, 1);
	}

	public AvlGTree(final int g) {
		this(null, 1);
	}

	public AvlGTree(final Comparator<? super K> comp, final int g) {
		if (comp == null) {
			this.comp = new Comparator<K>() {

				@SuppressWarnings("unchecked")
				@Override
				public int compare(K o1, K o2) {
					return ((Comparable<K>) o1).compareTo(o2);
				}
				
			};
		} else {
			this.comp = comp;
		}
		this.g = g < 1 ? 1 : g;
		this.root = null;
		this.size = 0;
	}

	public AvlGTree(java.util.Map<? extends K, ? extends V> m) {
		this();
		this.putAll(m);
	}

	public AvlGTree(java.util.SortedMap<? extends K, ? extends V> m) {
		this();
		this.putAll(m);
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

	private int heightAux(AvlNode<K, V> entry) {
		if (entry == null) {
			return 0;
		} else {
			return 1 + Math.max(heightAux(entry.left), heightAux(entry.right));
		}
	}

	@Override
	public void clear() {
		this.size = 0;
		this.root = null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean containsKey(Object key) {
		return this.find((K) key) != null;
	}

	private AvlNode<K, V> find(K key) {
		return this.findNodeAux(this.root, key);
	}
	
	private AvlNode<K, V> find(Stack<AvlNode<K, V>> stack, K key) {
		return this.findNodeAux(stack, this.root, key);
	}

	private AvlNode<K, V> findNodeAux(AvlNode<K, V> node, K key) {
		if (node == null) {
			return null;
		} else if (this.comp.compare(key, node.key) < 0) {
			return this.findNodeAux(node.left, key);
		} else if (this.comp.compare(key, node.key) > 0) {
			return this.findNodeAux(node.right, key);
		} else {
			return node;
		}
	}
	
	private AvlNode<K, V> findNodeAux(Stack<AvlNode<K, V>> stack, AvlNode<K, V> node, K key) {
		if (node == null) {
			return null;
		}
		stack.push(node);
		if (this.comp.compare(key, node.key) < 0) {
			return this.findNodeAux(stack, node.left, key);
		} else if (this.comp.compare(key, node.key) > 0) {
			return this.findNodeAux(stack, node.right, key);
		} else {
			return node;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean containsValue(Object value) {
		return this.containsValueAux(this.root, (V) value);
	}

	private boolean containsValueAux(AvlNode<K, V> node, V value) {
		if (node == null) {
			return false;
		} else if (node.value.equals(value)) {
			return true;
		} else {
			return this.containsValueAux(node.left, value) || this.containsValueAux(node.right, value);
		}
	}

	@Override
	public V get(Object key) {
		@SuppressWarnings("unchecked")
		AvlNode<K, V> node = this.find((K) key);
		return node == null ? null : node.value;
	}

	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}

	@Override
	public V put(K key, V value) {
		AvlNode<K, V> node = this.find(key);
		if (node != null) {
			V ret = node.value;
			node.value = value;
			return ret;
		} else {
			this.size++;
			AvlNode<K, V> add = new AvlNode<K, V>(key, value);
			if (this.root == null) {
				this.root = add;
			} else {
				this.putAux(this.root, add);
			}
			return null;
		}
	}

	private void putAux(AvlNode<K, V> node, AvlNode<K, V> add) {
		if (this.comp.compare(add.key, node.key) < 0) {
			if (node.left == null) {
				node.left = add;
			} else {
				this.putAux(node.left, add);
			}
		} else {
			if (node.right == null) {
				node.right = add;
			} else {
				this.putAux(node.right, add);
			}
		}
		this.balance(node);
	}
	
	public void balance(AvlNode<K, V> node) {
		int balanceFactor = this.balanceFactor(node);
		if (Math.abs(balanceFactor) > this.g) {
			if (balanceFactor > 0) {
				// left x case
				if (balanceFactor(node.left) < 0) {
					// left right case
					AvlNode<K, V> child = node.left;
					AvlNode<K, V> grandChild = child.right;
					child.right = grandChild.left;
					grandChild.left = child;
					node.left = grandChild;
				}
				// left left case
				AvlNode<K, V> child = node.left;
				AvlNode<K, V> grandChild = child.left;
				this.swapData(node, child);
				node.left = grandChild;
				child.left = child.right;
				child.right = node.right;
				node.right = child;
				this.updateHeight(node);
				this.updateHeight(child);
				this.updateHeight(grandChild);
			} else {
				// right x case
				if (balanceFactor(node.right) > 0) {
					// right left case
					AvlNode<K, V> child = node.right;
					AvlNode<K, V> grandChild = child.left;
					child.left = grandChild.right;
					grandChild.right = child;
					node.right = grandChild;
				}
				// right right case
				AvlNode<K, V> child = node.right;
				AvlNode<K, V> grandChild = child.right;
				this.swapData(node, child);
				node.right = grandChild;
				child.right = child.left;
				child.left = node.left;
				node.left = child;
				this.updateHeight(node);
				this.updateHeight(child);
				this.updateHeight(grandChild);
			}
		}
	}

	private void swapData(AvlNode<K, V> first, AvlNode<K, V> second) {
		K tempKey = first.key;
		V tempVal = first.value;
		first.key = second.key;
		first.value = second.value;
		second.key = tempKey;
		second.value = tempVal;
	}

	private int height(AvlNode<K, V> entry) {
		return entry == null ? 0 : entry.height;
	}

	private void updateHeight(AvlNode<K, V> entry) {
		if (entry != null) {
			entry.height = 1 + Math.max(height(entry.left), height(entry.right));
		}
	}

	private int balanceFactor(AvlNode<K, V> entry) {
		this.updateHeight(entry.left);
		this.updateHeight(entry.right);
		return height(entry.left) - height(entry.right);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (java.util.Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
			this.put(entry.getKey(), entry.getValue());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public V remove(Object key) {
		Stack<AvlNode<K, V>> stack = new Stack<>();
		AvlNode<K, V> toDelete = this.find(stack, (K) key);
		if (toDelete == null) {
			return null;
		}
		this.size--; // TODO what if we remove root?
		V ret = toDelete.getValue();
		if (this.size == 0) {
			this.clear();
			return ret;
		}
		
		if (toDelete.left == null && toDelete.right == null) {
			// if no children we need to find parent
//			for (AvlNode<K, V> i : stack) {
//				System.out.println(i.getKey());
//			}
			stack.pop();
			AvlNode<K, V> parent = stack.peek();
			if (parent.left == toDelete) {
				parent.left = null;
			} else {
				parent.right = null;
			}
		} else if (toDelete.left != null && toDelete.right != null) {
			// if 2 children find smallest in right subtree or largest in left subtree
			AvlNode<K, V> cur = toDelete.left;
//			stack.push(cur);
			while (cur.right != null) {
				stack.push(cur);
				cur = cur.right;
			}
//			AvlGTree<K, V> dumdum = new AvlGTree<>(this.comp, this.g);
//			dumdum.root = cur;
//			dumdum.remove(cur.key);
			toDelete.key = cur.key;
			toDelete.value = cur.value;
			AvlNode<K, V> parent = stack.peek();
//			parent.right = null;
			if (parent.left != null && parent.left.equals(cur)) {
				parent.left = cur.left; // case of immediate successor
			} else {
				parent.right = cur.left;
			}
		} else {
			// if 1 child we need to swap keys and replace children
			if (toDelete.left == null) {
				//
				toDelete.key = toDelete.right.key;
				toDelete.value = toDelete.right.value;
				toDelete.left = toDelete.right.left;
				toDelete.right = toDelete.right.right;
			} else {
				//
				toDelete.key = toDelete.left.key;
				toDelete.value = toDelete.left.value;
				toDelete.right = toDelete.left.right;
				toDelete.left = toDelete.left.left;
			}
		}
		
		// then we need to rebalance...
		// start at the killed node's parent
		while (stack.size() > 0) {
			this.balance(stack.pop());
		}
		return ret;
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public Comparator<? super K> comparator() {
		return this.comp;
	}

	class MySet extends AbstractSet<java.util.Map.Entry<K, V>> implements Set<java.util.Map.Entry<K, V>> {

		@Override
		public int size() {
			return AvlGTree.this.size();
		}

		@Override
		public boolean isEmpty() {
			return AvlGTree.this.isEmpty();
		}

		@Override
		public boolean contains(Object o) {
			@SuppressWarnings("unchecked")
			AvlNode<K, V> node = AvlGTree.this.find(((java.util.Map.Entry<K, V>) o).getKey());
			return node == null ? false : node.equals(o);
		}

		@Override
		public Iterator<java.util.Map.Entry<K, V>> iterator() {
			return new Iterator<java.util.Map.Entry<K, V>>() {

				private Iterator<java.util.Map.Entry<K, V>> wrapper;
				private java.util.Map.Entry<K, V> current = null;

				{
					List<java.util.Map.Entry<K, V>> entryList = new LinkedList<>();
					this.fillList(entryList, AvlGTree.this.root);
					this.wrapper = entryList.iterator();
				}

				private void fillList(List<java.util.Map.Entry<K, V>> list, AvlNode<K, V> node) {
					if (node == null) {
						return;
					}
					this.fillList(list, node.left);
					list.add(node);
//					System.out.println("I just added " + node.key); // TODO
					this.fillList(list, node.right);
				}

				@Override
				public boolean hasNext() {
					return wrapper.hasNext();
				}

				@Override
				public java.util.Map.Entry<K, V> next() {
					this.current = this.wrapper.next();
					return this.current;
				}
				
				@Override
				public void remove() {
					this.wrapper.remove();
					AvlGTree.this.remove(this.current.getKey());
				}

			};
		}

		@Override
		public Object[] toArray() {
			Object[] ret = new Object[this.size()];
			Iterator<Entry<K, V>> iter = this.iterator();
			int index = 0;
			while (iter.hasNext()) {
				ret[index++] = iter.next();
			}
			return ret;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			if (a.length < this.size()) {
				a = (T[]) new Object[this.size()];
			}
			Iterator<Entry<K, V>> iter = this.iterator();
			int index = 0;
			while (iter.hasNext()) {
				a[index++] = (T) iter.next();
			}
			return a;
		}

		@Override
		public boolean add(java.util.Map.Entry<K, V> e) {
			return !e.getValue().equals(AvlGTree.this.put(e.getKey(), e.getValue()));
		}

		@Override
		public boolean remove(Object o) {
			return AvlGTree.this.remove(o) != null;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			for (Object o : c) {
				if (!this.contains(o)) {
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean addAll(Collection<? extends java.util.Map.Entry<K, V>> c) {
			boolean ret = false;
			for (java.util.Map.Entry<K, V> e : c) {
				if (this.add(e)) {
					ret = true;
				}
			}
			return ret;
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			boolean ret = false;
			Iterator<java.util.Map.Entry<K, V>> iter = this.iterator();
			while (iter.hasNext()) {
				if (!c.contains(iter.next())) {
					iter.remove();
					ret = true;
				}
			}
			return ret;
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			boolean ret = false;
			for (Object o : c) {
				if (this.remove(o)) {
					ret = true;
				}
			}
			return ret;
		}

		@Override
		public void clear() {
			AvlGTree.this.clear();
		}

		@Override
		public boolean equals(Object other) {
			if (!(other instanceof Set)) {
				return false;
			}
			@SuppressWarnings("unchecked")
			Set<java.util.Map.Entry<K, V>> set = (Set<Entry<K, V>>) other;
			if (set.size() != this.size()) {
				return false;
			}
			Iterator<Entry<K, V>> iter = this.iterator();
			while (iter.hasNext()) {
				java.util.Map.Entry<K, V> next = iter.next();
				if (!set.contains(next)) {
					return false;
				}
			}
			return true;
		}

	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return new MySet();
	}

	@Override
	public K firstKey() {
		if (this.root == null) {
			throw new NoSuchElementException();
		}
		return this.firstKeyAux(this.root);
	}

	private K firstKeyAux(AvlNode<K, V> node) {
		return node.left == null ? node.key : this.firstKeyAux(node.left);
	}

	@Override
	public SortedMap<K, V> headMap(K toKey) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<K> keySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public K lastKey() {
		if (this.root == null) {
			throw new NoSuchElementException();
		}
		return this.lastKeyAux(this.root);
	}

	private K lastKeyAux(AvlNode<K, V> node) {
		return node.right == null ? node.key : this.lastKeyAux(node.right);
	}

	@Override
	public SortedMap<K, V> subMap(K fromKey, K toKey) {
		if (this.comp.compare(fromKey, toKey) > 0) {
			throw new IllegalArgumentException();
		}
		return new SubMap(fromKey, toKey);
	}

	@Override
	public SortedMap<K, V> tailMap(K fromKey) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<V> values() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if (!(other instanceof java.util.Map)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		Map<K, V> map = (Map<K, V>) other;
		if (map.size() != this.size()) {
			return false;
		}
		Iterator<Entry<K, V>> iter = this.entrySet().iterator();
		while (iter.hasNext()) {
			java.util.Map.Entry<K, V> next = iter.next();
			K key = next.getKey();
			V value = next.getValue();
			if (!value.equals(map.get(key))) {
				return false;
			}
		}
		return true;
	}

	public static class AvlNode<K, V> implements AbstractMap.Entry<K, V> {

		private K key;
		private V value;
		private AvlNode<K, V> left;
		private AvlNode<K, V> right;
		private int height;

		public AvlNode(K key, V value) {
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

		@Override
		public boolean equals(Object other) {
			if (!(other instanceof java.util.Map.Entry<?, ?>)) {
				return false;
			}
			@SuppressWarnings("unchecked")
			java.util.Map.Entry<K, V> e = (java.util.Map.Entry<K, V>) other;
			return this.key.equals(e.getKey()) && this.value.equals(e.getValue());
		}

		@Override
		public String toString() {
			return key + "=" + value;
		}

		@Override
		public int hashCode() {
			return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
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

	public class SubMap extends AbstractMap<K, V> implements SortedMap<K, V> {

		private final K low; // inclusive
		private final K high; // exclusive

		public SubMap(K fromKey, K toKey) {
			this.low = fromKey;
			this.high = toKey;
		}

		@Override
		public void clear() {
//			AvlGTree.this.clear(); // change in part 3
//			Iterator<java.util.Map.Entry<K, V>> iter = this.entrySet().iterator();//TODO fix
			Iterator<java.util.Map.Entry<K, V>> iter = AvlGTree.this.entrySet().iterator();
			while (iter.hasNext()) {
				K key = iter.next().getKey();
				System.out.println(key);
				if (!this.outOfBounds(key)) {
					System.out.println(key + " is out of bounds");
					iter.remove();
				}
			}
		}

		private boolean outOfBounds(K key) {
			return AvlGTree.this.comp.compare(key, this.low) < 0 || AvlGTree.this.comp.compare(key, this.high) >= 0;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean containsKey(Object key) {
			if (this.outOfBounds((K) key)) {
				throw new IllegalArgumentException();
			}
			return AvlGTree.this.containsKey(key);
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean containsValue(Object value) {
			AvlNode<K, V> root = this.getValidRoot(AvlGTree.this.root);
			return this.containsValueAux(root, (V) value);
		}

		private boolean containsValueAux(AvlNode<K, V> node, V value) {
			if (node == null || this.outOfBounds(node.key)) {
				return false;
			} else if (node.value.equals(value)) {
				return true;
			} else {
				return this.containsValueAux(node.left, value) || this.containsValueAux(node.right, value);
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public V get(Object key) {
			if (this.outOfBounds((K) key)) {
				throw new IllegalArgumentException();
			}
			return AvlGTree.this.get(key);
		}

		@Override
		public boolean isEmpty() {
			return this.size() == 0;
		}

		@Override
		public V put(K key, V value) {
			if (this.outOfBounds((K) key)) {
				throw new IllegalArgumentException();
			}
			return AvlGTree.this.put(key, value);
		}

		@Override
		public void putAll(Map<? extends K, ? extends V> m) {
			for (java.util.Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
				this.put(entry.getKey(), entry.getValue());
			}
		}

//		@SuppressWarnings("unchecked")
		@Override
		public V remove(Object key) {
//			if (this.outOfBounds((K) key)) {
//				throw new IllegalArgumentException();
//			}
			return AvlGTree.this.remove(key);
		}

		@Override
		public int size() {
			return sizeAux(AvlGTree.this.root);
		}

		private int sizeAux(AvlNode<K, V> node) {
			if (node == null) {
				return 0;
			}
			int size = this.outOfBounds(node.key) ? 0 : 1;
			if (AvlGTree.this.comp.compare(node.key, this.low) > 0) {
				size += this.sizeAux(node.left);
			}
			if (AvlGTree.this.comp.compare(node.key, this.high) < 0) {
				size += this.sizeAux(node.right);
			}
			return size;
		}

		@Override
		public Comparator<? super K> comparator() {
			return AvlGTree.this.comp;
		}

		@Override
		public Set<java.util.Map.Entry<K, V>> entrySet() {
			return new SubmapEntrySet();
		}

		class SubmapEntrySet extends AbstractSet<java.util.Map.Entry<K, V>> implements Set<java.util.Map.Entry<K, V>> {

			@Override
			public boolean add(java.util.Map.Entry<K, V> e) {
				return !e.getValue().equals(SubMap.this.put(e.getKey(), e.getValue()));
			}

			@Override
			public boolean addAll(Collection<? extends java.util.Map.Entry<K, V>> c) {
				boolean ret = false;
				for (java.util.Map.Entry<K, V> e : c) {
					if (this.add(e)) {
						ret = true;
					}
				}
				return ret;
			}

			@Override
			public void clear() {
				SubMap.this.clear();
			}

			@Override
			public boolean contains(Object o) {
				@SuppressWarnings("unchecked")
				AvlNode<K, V> node = AvlGTree.this.find(((java.util.Map.Entry<K, V>) o).getKey());
				return node == null ? false : node.equals(o);
			}

			@Override
			public boolean containsAll(Collection<?> c) {
				for (Object o : c) {
					if (!this.contains(o)) {
						return false;
					}
				}
				return true;
			}

			@Override
			public boolean isEmpty() {
				return SubMap.this.isEmpty();
			}

			@Override
			public Iterator<java.util.Map.Entry<K, V>> iterator() {
				return new Iterator<java.util.Map.Entry<K, V>>() {

					private Iterator<java.util.Map.Entry<K, V>> wrapper;
					private java.util.Map.Entry<K, V> current = null;

					{
						List<java.util.Map.Entry<K, V>> entryList = new LinkedList<>();
						this.fillList(entryList, AvlGTree.this.root);
						this.wrapper = entryList.iterator();
					}

					private void fillList(List<java.util.Map.Entry<K, V>> list, AvlNode<K, V> node) {
						if (node == null) {
							return;
						}
						this.fillList(list, node.left);
						if (!SubMap.this.outOfBounds(node.key)) {
							list.add(node);
						}
						this.fillList(list, node.right);
					}

					@Override
					public boolean hasNext() {
						return wrapper.hasNext();
					}

					@Override
					public java.util.Map.Entry<K, V> next() {
						this.current = wrapper.next();
						return this.current;
					}
					
					@Override
					public void remove() {
						AvlGTree.this.remove(this.current.getKey());
					}

				};
			}

			@Override
			public boolean remove(Object o) {
				return AvlGTree.this.remove(o) != null;
			}

			@Override
			public boolean removeAll(Collection<?> c) {
				boolean ret = false;
				for (Object o : c) {
					if (this.remove(o)) {
						ret = true;
					}
				}
				return ret;
			}

			@Override
			public boolean retainAll(Collection<?> c) {
				boolean ret = false;
				Iterator<java.util.Map.Entry<K, V>> iter = this.iterator();
				while (iter.hasNext()) {
					if (!c.contains(iter.next())) {
						iter.remove();
						ret = true;
					}
				}
				return ret;
			}

			@Override
			public int size() {
				return SubMap.this.size();
			}

			@Override
			public Object[] toArray() {
				Object[] ret = new Object[this.size()];
				Iterator<Entry<K, V>> iter = this.iterator();
				int index = 0;
				while (iter.hasNext()) {
					ret[index++] = iter.next();
				}
				return ret;
			}

			@SuppressWarnings("unchecked")
			@Override
			public <T> T[] toArray(T[] a) {
				if (a.length < this.size()) {
					a = (T[]) new Object[this.size()];
				}
				Iterator<Entry<K, V>> iter = this.iterator();
				int index = 0;
				while (iter.hasNext()) {
					a[index++] = (T) iter.next();
				}
				return a;
			}

			public boolean equals(Object other) {
				if (!(other instanceof Set)) {
					return false;
				}
				@SuppressWarnings("unchecked")
				Set<java.util.Map.Entry<K, V>> set = (Set<Entry<K, V>>) other;
				if (set.size() != this.size()) {
					return false;
				}
				Iterator<Entry<K, V>> iter = this.iterator();
				while (iter.hasNext()) {
					java.util.Map.Entry<K, V> next = iter.next();
					if (!set.contains(next)) {
						return false;
					}
				}
				return true;
			}

		}

		@Override
		public K firstKey() {
			AvlNode<K, V> root = this.getValidRoot(AvlGTree.this.root);
			if (root == null) {
				throw new NoSuchElementException();
			}
			return this.firstKeyAux(root);
		}

		private AvlNode<K, V> getValidRoot(AvlNode<K, V> node) {
			if (node == null) {
				return null;
			} else if (!this.outOfBounds(node.key)) {
				return node;
			} else if (AvlGTree.this.comp.compare(node.key, this.low) < 0) {
				return this.getValidRoot(node.right);
			} else {
				return this.getValidRoot(node.left);
			}
		}

		private K firstKeyAux(AvlNode<K, V> node) {
//			return node.left == null || this.outOfBounds(node.left.key) ? node.key : this.firstKeyAux(node.left);
			if (node == null) {
				return null;
			} else if (AvlGTree.this.comp.compare(node.key, this.low) < 0) {
				// if too low go right
				return this.firstKeyAux(node.right);
			} else { // if too high go left
				K ret = this.firstKeyAux(node.left);
				return ret == null ? node.key : ret;
			}
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
			AvlNode<K, V> root = this.getValidRoot(AvlGTree.this.root);
			if (root == null) {
				throw new NoSuchElementException();
			}
			return this.lastKeyAux(root);
		}

		private K lastKeyAux(AvlNode<K, V> node) {
//			return node.right == null || this.outOfBounds(node.right.key) ? node.key : this.firstKeyAux(node.right);
			if (node == null) {
				return null;
			} else if (AvlGTree.this.comp.compare(node.key, this.high) >= 0) {
				// if too high go left
				return this.lastKeyAux(node.left);
			} else { // if too low go right
				K ret = this.lastKeyAux(node.right);
				return ret == null ? node.key : ret;
			}
		}

		@Override
		public SortedMap<K, V> subMap(K arg0, K arg1) {
			if (this.outOfBounds(arg0) || this.outOfBounds(arg1)) {
				throw new IllegalArgumentException();
			}
			return AvlGTree.this.subMap(arg0, arg1);
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
		public boolean equals(Object other) {
			if (!(other instanceof java.util.Map)) {
				return false;
			}
			@SuppressWarnings("unchecked")
			Map<K, V> map = (Map<K, V>) other;
			if (map.size() != this.size()) {
				return false;
			}
			Iterator<Entry<K, V>> iter = this.entrySet().iterator();
			while (iter.hasNext()) {
				java.util.Map.Entry<K, V> next = iter.next();
				K key = next.getKey();
				V value = next.getValue();
				if (!value.equals(map.get(key))) {
					return false;
				}
			}
			return true;
		}

	}

}
