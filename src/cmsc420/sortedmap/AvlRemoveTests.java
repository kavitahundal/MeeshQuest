package cmsc420.sortedmap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

import cmsc420.sortedmap.OldAvlGTree;

/*
 * These tests were made by Anonymous.
 */

public class AvlRemoveTests {

	@Test
	public void testRemoveFromSubMapEntrySet() {
		OldAvlGTree<Integer, Integer> avl = new OldAvlGTree<Integer, Integer>();
		TreeMap<Integer, Integer> treemap = new TreeMap<Integer, Integer>();

		SortedMap<Integer, Integer> avlSub = avl.subMap(80, 567);
		SortedMap<Integer, Integer> treemapSub = treemap.subMap(80, 567);

		// put 1000 elements
		for (int i = 0; i < 1000; i++) {
			avl.put(i, i);
			treemap.put(i, i);
		}

		// check initial trees are equal
		assertTrue(treemap.equals(avl));
		assertTrue(treemapSub.equals(avlSub));
		assertTrue(avl.equals(treemap));
		assertTrue(avlSub.equals(treemapSub));

		// remove from submap using its entry set
		Set<Map.Entry<Integer, Integer>> entrySubA = avlSub.entrySet();
		Set<Map.Entry<Integer, Integer>> entrySubJ = treemapSub.entrySet();

		LinkedList<Map.Entry<Integer, Integer>> list = new LinkedList<Map.Entry<Integer, Integer>>();

		for (Map.Entry<Integer, Integer> e : entrySubA) {
			list.add(e);
		}

		// remove all elements in range of submap
		int numEntries = list.size();
		for (int i = 0; i < numEntries; i++) {
			Map.Entry<Integer, Integer> e = list.getFirst();
			entrySubA.remove(e);
			entrySubJ.remove(e);

			assertTrue(treemap.equals(avl));
			assertTrue(treemapSub.equals(avlSub));
			assertTrue(avl.equals(treemap));
			assertTrue(avlSub.equals(treemapSub));
		}

		// check final trees are equal
		assertTrue(treemap.equals(avl));
		assertTrue(treemapSub.equals(avlSub));
		assertTrue(avl.equals(treemap));
		assertTrue(avlSub.equals(treemapSub));
	}

	@Test
	public void testRemoveFromSubMapEntrySetIterator() {
		OldAvlGTree<Integer, Integer> avl = new OldAvlGTree<Integer, Integer>();
		TreeMap<Integer, Integer> treemap = new TreeMap<Integer, Integer>();
		SortedMap<Integer, Integer> avlSub = avl.subMap(80, 567);
		SortedMap<Integer, Integer> treemapSub = treemap.subMap(80, 567);

		// put 10000 elements
		for (int i = 0; i < 10000; i++) {
			avl.put(i, i);
			treemap.put(i, i);
		}

		assertTrue(treemap.equals(avl));
		assertTrue(treemapSub.equals(avlSub));
		assertTrue(avl.equals(treemap));
		assertTrue(avlSub.equals(treemapSub));

		Set<Map.Entry<Integer, Integer>> entrySubA = avlSub.entrySet();
		Set<Map.Entry<Integer, Integer>> entrySubJ = treemapSub.entrySet();

		Iterator<Map.Entry<Integer, Integer>> iterA = entrySubA.iterator();
		Iterator<Map.Entry<Integer, Integer>> iterJ = entrySubJ.iterator();

		while (iterA.hasNext() && iterJ.hasNext()) {
			Map.Entry<Integer, Integer> tempA = iterA.next();
			Map.Entry<Integer, Integer> tempJ = iterJ.next();

			// check that your iterators are returning the correct Entry
			assertTrue(tempA.getKey().equals(tempJ.getKey()));
			assertTrue(tempA.getValue().equals(tempJ.getValue()));

			iterA.remove();
			iterJ.remove();
			assertTrue(avl.equals(treemap));
			assertTrue(avlSub.equals(treemapSub));
		}

		assertTrue(treemap.equals(avl));
		assertTrue(avl.equals(treemap));
		assertTrue(treemapSub.equals(avlSub));
		assertTrue(avlSub.equals(treemapSub));
	}

	@Test
	public void testRemoveThroughEntrySet() {
		OldAvlGTree<Integer, Integer> avl = new OldAvlGTree<Integer, Integer>();
		TreeMap<Integer, Integer> treemap = new TreeMap<Integer, Integer>();

		// put 1000 elements
		for (int i = 0; i < 1000; i++) {
			avl.put(i, i);
			treemap.put(i, i);
		}

		assertTrue(treemap.equals(avl));
		assertTrue(avl.equals(treemap));

		Set<Map.Entry<Integer, Integer>> avlEntries = avl.entrySet();
		Set<Map.Entry<Integer, Integer>> treemapEntries = treemap.entrySet();

		for (Map.Entry<Integer, Integer> e : avlEntries) {
			avlEntries.remove(e);
			treemapEntries.remove(e);
			assertTrue(treemap.equals(avl));
			assertTrue(avl.equals(treemap));
		}

		assertTrue(treemap.equals(avl));
		assertTrue(avl.equals(treemap));
	}

	@Test
	public void testSequentialRemoveEntrySetIterator() {
		OldAvlGTree<Integer, Integer> avl = new OldAvlGTree<Integer, Integer>();
		TreeMap<Integer, Integer> treemap = new TreeMap<Integer, Integer>();

		for (int i = 0; i < 1000; i++) {
			avl.put(i, i);
			treemap.put(i, i);
		}

		assertTrue(treemap.equals(avl));
		assertTrue(avl.equals(treemap));

		for (int i = 0; i < 1000; i++) {
			avl.remove(i);
			treemap.remove(i);

			Set<Map.Entry<Integer, Integer>> entryA = avl.entrySet();
			Set<Map.Entry<Integer, Integer>> entryJ = treemap.entrySet();

			Iterator<Map.Entry<Integer, Integer>> iterA = entryA.iterator();
			Iterator<Map.Entry<Integer, Integer>> iterJ = entryJ.iterator();

			while (iterA.hasNext() && iterJ.hasNext()) {
				Map.Entry<Integer, Integer> tempA = iterA.next();
				Map.Entry<Integer, Integer> tempJ = iterJ.next();

				// make sure iterator is returning correct Entry
				assertTrue(tempA.getKey().equals(tempJ.getKey()));
				assertTrue(tempA.getValue().equals(tempJ.getValue()));
			}

			assertTrue(treemap.equals(avl));
			assertTrue(avl.equals(treemap));
		}
	}

	@Test
	public void testSequentialRemoveFromSubmap() {
		OldAvlGTree<Integer, Integer> avl = new OldAvlGTree<Integer, Integer>();
		TreeMap<Integer, Integer> treemap = new TreeMap<Integer, Integer>();
		SortedMap<Integer, Integer> avlSub = avl.subMap(80, 567);
		SortedMap<Integer, Integer> treemapSub = treemap.subMap(80, 567);

		// put 1000 elements
		for (int i = 0; i < 1000; i++) {
			avl.put(i, i);
			treemap.put(i, i);
		}

		assertTrue(treemap.equals(avl));
		assertTrue(avl.equals(treemap));

		for (int i = 82; i < 189; i++) {
			avlSub.remove(i);
			treemapSub.remove(i);

			assertTrue(treemap.equals(avl));
			assertTrue(avl.entrySet().size() == treemap.entrySet().size());
			assertTrue(treemapSub.entrySet().size() == avlSub.entrySet().size());

			Iterator<Map.Entry<Integer, Integer>> iterA = avl.entrySet().iterator();
			Iterator<Map.Entry<Integer, Integer>> iterJ = treemap.entrySet().iterator();

			while (iterA.hasNext() && iterJ.hasNext()) {
				Map.Entry<Integer, Integer> tempA = iterA.next();
				Map.Entry<Integer, Integer> tempJ = iterJ.next();

				// check iterators are returning correct Entry
				assertTrue(tempA.getKey().equals(tempJ.getKey()));
				assertTrue(tempA.getValue().equals(tempJ.getValue()));

				assertTrue(avl.equals(treemap));
				assertTrue(avlSub.equals(treemapSub));

			}

			assertTrue(avl.equals(treemap));
			assertTrue(treemapSub.entrySet().size() == avlSub.entrySet().size());
			assertTrue(treemapSub.entrySet().equals(avlSub.entrySet()));
			assertTrue(treemapSub.equals(avlSub));
			assertTrue(avlSub.equals(treemapSub));
			assertTrue(avl.entrySet().equals(treemap.entrySet()));
		}

		// put in some duplicate elements
		for (int i = 80; i < 567; i++) {
			assertEquals(avl.put(i, i), treemap.put(i, i));
		}

		assertTrue(treemap.equals(avl));
		assertTrue(treemapSub.equals(avlSub));
		assertTrue(avl.equals(treemap));
		assertTrue(avlSub.equals(treemapSub));

		Set<Map.Entry<Integer, Integer>> entrySubA = avlSub.entrySet();
		Set<Map.Entry<Integer, Integer>> entrySubJ = treemapSub.entrySet();

		LinkedList<Map.Entry<Integer, Integer>> list = new LinkedList<Map.Entry<Integer, Integer>>();

		for (Map.Entry<Integer, Integer> e : entrySubA) {
			list.add(e);
		}

		int numEntries = list.size();
		for (int i = 0; i < numEntries; i++) {
			Map.Entry<Integer, Integer> e = list.getFirst();
			entrySubA.remove(e);
			entrySubJ.remove(e);
		}

		assertTrue(avl.equals(treemap));
		assertTrue(treemapSub.entrySet().size() == avlSub.entrySet().size());
		assertTrue(treemapSub.entrySet().equals(avlSub.entrySet()));
		assertTrue(treemapSub.equals(avlSub));
		assertTrue(avlSub.equals(treemapSub));
		assertTrue(avl.entrySet().equals(treemap.entrySet()));
		assertTrue(avl.equals(treemap));
	}
}
