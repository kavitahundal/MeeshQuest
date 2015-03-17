package cmsc420.sortedmap;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class AvlGTree<K, V> extends AbstractMap<K, V> implements SortedMap<K, V> {

	private final Comparator<? super K> comp;
	private final int g;
	private AvlNode root;
	private int size;

	public AvlGTree(final Comparator<? super K> comp, final int g) {
		this.comp = comp;
		this.g = g >= 1 ? g : 1;
		this.size = 0;
		this.root = null;
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
	public K firstKey() {
		if (this.root == null) {
			throw new NoSuchElementException();
		}
		return this.firstKeyAux(this.root);
	}
	
	private K firstKeyAux(AvlNode node) {
		return node.left == null ? node.key : firstKeyAux(node.left);
	}

	@Override
	public SortedMap<K, V> headMap(K toKey) {
		throw new UnsupportedOperationException();
	}

	@Override
	public K lastKey() {
		if (this.root == null) {
			throw new NoSuchElementException();
		}
		return this.lastKeyAux(this.root);
	}
	
	private K lastKeyAux(AvlNode node) {
		return node.right == null ? node.key : lastKeyAux(node.right);
	}

	@Override
	public SortedMap<K, V> subMap(K fromKey, K toKey) {
		return new SortedMap<K, V>() {

			@Override
			public void clear() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean containsKey(Object key) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean containsValue(Object value) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public V get(Object key) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean isEmpty() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public V put(K key, V value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void putAll(Map<? extends K, ? extends V> m) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public V remove(Object key) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int size() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Comparator<? super K> comparator() {
				// TODO Auto-generated method stub
				return null;
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
			public SortedMap<K, V> headMap(K toKey) {
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
			public SortedMap<K, V> subMap(K fromKey, K toKey) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public SortedMap<K, V> tailMap(K fromKey) {
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
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return new AbstractSet<java.util.Map.Entry<K, V>>() {

			@Override
			public boolean add(java.util.Map.Entry<K, V> entry) {
				return false;
				// TODO
			}

			@Override
			public Iterator<java.util.Map.Entry<K, V>> iterator() {
				return new Iterator<java.util.Map.Entry<K, V>>() {

					@Override
					public boolean hasNext() {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public java.util.Map.Entry<K, V> next() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public void remove() {
						// TODO
					}

				};
			}

			@Override
			public int size() {
				return AvlGTree.this.size;
			}

		};
	}

	@Override
	public V put(K key, V value) {
		// TODO
		return null;
	}

	public class AvlNode implements java.util.Map.Entry<K, V> {

		private K key;
		private V value;
		private AvlNode left;
		private AvlNode right;

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
