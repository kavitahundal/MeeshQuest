package cmsc420.sortedmap;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class AvlGTree<K, V> extends AbstractMap<K, V> implements SortedMap<K, V> {

	private final Comparator<? super K> comp;
	private final int g;
	private AvlNode<K, V> root;
	private int size;

	public AvlGTree(final Comparator<? super K> comp, final int g) {
		this.comp = comp;
		this.g = g;
		this.root = null;
		this.size = 0;
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
		// TODO balance check
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (java.util.Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
			this.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public V remove(Object key) {
		throw new UnsupportedOperationException();
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
		throw new UnsupportedOperationException("entrySet");
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
		return new SortedMap<K, V>() {

			private K low = fromKey; // inclusive
			private K high = toKey; // exclusive

			@Override
			public void clear() {
				throw new UnsupportedOperationException();
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

			@Override
			public V remove(Object key) {
				throw new UnsupportedOperationException();
			}

			@Override
			public int size() {
				return sizeAux(AvlGTree.this.root);
			}

			private int sizeAux(AvlNode<K, V> node) {
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
				// TODO Auto-generated method stub
				return null;
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
				return node.left == null || this.outOfBounds(node.left.key) ? node.key : this.firstKeyAux(node.left);
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
				return node.right == null || this.outOfBounds(node.right.key) ? node.key : this.firstKeyAux(node.right);
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

		};
	}

	@Override
	public SortedMap<K, V> tailMap(K fromKey) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<V> values() {
		throw new UnsupportedOperationException();
	}

	public static class AvlNode<K, V> implements java.util.Map.Entry<K, V> {

		private K key;
		private V value;
		private AvlNode<K, V> left;
		private AvlNode<K, V> right;

		public AvlNode(K key, V value) {
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
