package cmsc420.sortedmap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

public class AvlGTreeTests {

	@Test
	public void testSimpleRemove() {
		SortedMap<Integer, Integer> avl = new OldAvlGTree<>();
		SortedMap<Integer, Integer> rb = new TreeMap<>();

		avl.put(1, 1);
		rb.put(1, 1);

		assertEquals(avl, rb);
		assertTrue(avl.equals(rb));
		assertTrue(rb.equals(avl));

		avl.remove(1);
		rb.remove(1);

		assertEquals(avl, rb);
		assertTrue(avl.equals(rb));
		assertTrue(rb.equals(avl));
	}

	@Test
	public void testSimpleRemove2() {
		SortedMap<Integer, Integer> avl = new OldAvlGTree<>();
		SortedMap<Integer, Integer> rb = new TreeMap<>();

		for (int i = 1; i <= 10; i++) {
			avl.put(i, i);
			rb.put(i, i);
			assertEquals(avl, rb);
			assertTrue(avl.equals(rb));
			assertTrue(rb.equals(avl));
		}

		for (int i = 1; i <= 10; i++) {
			// System.out.println(i);
			avl.remove(i);
			rb.remove(i);
			System.out.println(avl);
			assertEquals(avl, rb);
			assertTrue(avl.equals(rb));
			assertTrue(rb.equals(avl));
		}
	}

	@Test
	public void testSimpleRemove3() {
		SortedMap<Integer, Integer> avl = new OldAvlGTree<>();
		SortedMap<Integer, Integer> rb = new TreeMap<>();

		for (int i = 1; i <= 10; i++) {
			avl.put(i, i);
			rb.put(i, i);
			assertEquals(avl, rb);
		}

		avl.remove(4);
		rb.remove(4);
		// System.out.println(avl);
		assertEquals(avl, rb);
		assertTrue(avl.equals(rb));
		assertTrue(rb.equals(avl));
	}

	@Test
	public void removeTest() {
		OldAvlGTree<Integer, String> aTree = new OldAvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();

		OldAvlGTree<Integer, String> aTree2 = new OldAvlGTree<Integer, String>(new IntComparator(), 3);

		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();
		int numEntries = 10;
		int iterations = 1;

		for (int i = 0; i < iterations; i++) {
			getRandomEntries(keys, values, numEntries);
			initialize(aTree, tMap, keys, values);
			initialize(aTree2, tMap, keys, values);

			int size = keys.size();
			System.out.println(aTree);
			aTree2.debugPrint();

			for (int j = 0; j < size; j++) {
				System.out.println("foo");
				Integer key = keys.get(j);
				System.out.println(key);
				String tVal = tMap.remove(key);
				assertEquals(aTree.remove(key), tVal);
				assertEquals(aTree2.remove(key), tVal);

				System.out.println(aTree2);
				System.out.println(tMap);
				aTree2.debugPrint();
				assertTrue(aTree2.equals(tMap));
				assertTrue(tMap.equals(aTree2));

				assertTrue(aTree.equals(tMap));
				assertTrue(tMap.equals(aTree));

				assertEquals(aTree.hashCode(), tMap.hashCode());
				assertEquals(aTree2.hashCode(), tMap.hashCode());

				assertEquals(aTree.toString(), tMap.toString());
				assertEquals(aTree2.toString(), tMap.toString());
			}
		}
	}

	private static void getRandomEntries(List<Integer> keys, List<String> values, int num) {
		keys.clear();
		values.clear();
		for (int i = 0; i < num; i++) {
			int val = (int) (Math.random() * 100);
			keys.add(val);
			values.add("" + val);
		}
	}

	private static void initialize(OldAvlGTree<Integer, String> aTree, TreeMap<Integer, String> tMap, List<Integer> keys,
			List<String> values) {
		aTree.clear();
		tMap.clear();
		int size = keys.size();
		for (int i = 0; i < size; i++) {
			aTree.put(keys.get(i), values.get(i));
			tMap.put(keys.get(i), values.get(i));
		}
	}

	private class IntComparator implements Comparator<Integer> {

		@Override
		public int compare(Integer arg0, Integer arg1) {
			return arg0.compareTo(arg1);
		}

	}

}
