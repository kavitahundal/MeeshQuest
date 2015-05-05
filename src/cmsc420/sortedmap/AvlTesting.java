package cmsc420.sortedmap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

/*
 * These tests were made by Charlie Parker.
 */

public class AvlTesting {
	
	private static void getRandomEntries(List<Integer> keys,
			List<String> values, int num) {
		keys.clear();
		values.clear();
		for (int i = 0; i < num; i++) {
			int val = (int) (Math.random() * 100);
			keys.add(val);
			values.add("" + val);
		}
	}

	private static void initialize(AvlGTree<Integer, String> aTree,
			TreeMap<Integer, String> tMap, List<Integer> keys,
			List<String> values) {
		aTree.clear();
		tMap.clear();
		int size = keys.size();
		for (int i = 0; i < size; i++) {
			aTree.put(keys.get(i), values.get(i));
			tMap.put(keys.get(i), values.get(i));
		}
	}
	
	public static Collection<Map.Entry<Integer,String>> entryCollection(List<Integer> keys, List<String> values, int numEntries) {
		List<Map.Entry<Integer, String>> c = new ArrayList<Map.Entry<Integer, String>>();
		for (int i = 0; i < numEntries; i++) {
			c.add(new AbstractMap.SimpleEntry<Integer, String>(keys.get(i), values.get(i)));
		}		
		
		return c;
	}
	
	private class IntComparator implements Comparator<Integer> {

		@Override
		public int compare(Integer arg0, Integer arg1) {
			return arg0.compareTo(arg1);
		}
		
	}
	
/******** Avl Tree testing ********/
	
	@Test
	public void putDuplicateTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		
		AvlGTree<Integer,String> aTree2 = new AvlGTree<Integer,String>(new IntComparator(), 3);

		int max = 10;
		for (int i = 0; i < max; i++) {
			String tVal = tMap.put(i,  "" + i);
			assertSame(aTree.put(i, "" + i), tVal);
			assertSame(aTree2.put(i, "" + i), tVal);
			
		}

		for (int i = 0; i < max; i++) {
			String tVal = tMap.put(i,  "" + (i+10));

			assertEquals(aTree.put(i, "" + (i + 10)), tVal);
			assertEquals(aTree2.put(i, "" + (i + 10)), tVal);

		}
	}

	@Test
	public void putRandomTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		
		AvlGTree<Integer,String> aTree2 = new AvlGTree<Integer,String>(new IntComparator(), 3);
		
		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();
		int numEntries = 100;
		int iterations = 10;

		for (int i = 0; i < iterations; i++) {
			getRandomEntries(keys, values, numEntries);

			int size = keys.size();
			for (int j = 0; j < size; j++) {
				Integer key = keys.get(j);
				String val = values.get(j);
				String tVal = tMap.put(key, val);
				assertEquals(aTree.put(key, val), tVal);
				assertEquals(aTree2.put(key, val), tVal);

				assertTrue(aTree.equals(tMap));
				assertTrue(tMap.equals(aTree));
				
				assertTrue(aTree2.equals(tMap));
				assertTrue(tMap.equals(aTree2));
				
				assertEquals(aTree.hashCode(), tMap.hashCode());
				assertEquals(aTree2.hashCode(), tMap.hashCode());
				
				assertEquals(aTree.toString(), tMap.toString());
				assertEquals(aTree2.toString(), tMap.toString());


			}
		}

	}

	@Test
	public void firstKeyLastKeyTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();

		AvlGTree<Integer,String> aTree2 = new AvlGTree<Integer,String>(new IntComparator(), 3);

		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();
		int numEntries = 100;

		int iterations = 50;

		for (int i = 0; i < iterations; i++) {

			getRandomEntries(keys, values, numEntries);
			initialize(aTree, tMap, keys, values);
			initialize(aTree2, tMap, keys, values);

			assertEquals(aTree.firstKey(), tMap.firstKey());
			assertEquals(aTree.lastKey(), tMap.lastKey());
			
			assertEquals(aTree2.firstKey(), tMap.firstKey());
			assertEquals(aTree2.lastKey(), tMap.lastKey());

			assertTrue(aTree.equals(tMap));
			assertTrue(tMap.equals(aTree));
			
			assertTrue(aTree2.equals(tMap));
			assertTrue(tMap.equals(aTree2));
			
			assertEquals(aTree.hashCode(), tMap.hashCode());
			assertEquals(aTree2.hashCode(), tMap.hashCode());
			
			assertEquals(aTree.toString(), tMap.toString());
			assertEquals(aTree2.toString(), tMap.toString());
		}
	}

	@Test
	public void containsTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		
		AvlGTree<Integer,String> aTree2 = new AvlGTree<Integer,String>(new IntComparator(), 3);

		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();
		int numEntries = 100;

		int iterations = 50;

		for (int i = 0; i < iterations; i++) {

			getRandomEntries(keys, values, numEntries);
			initialize(aTree, tMap, keys, values);
			initialize(aTree2, tMap, keys, values);


			for (int j = 0; j < numEntries; j++) {
				Integer key = keys.get(j);
				assertEquals(aTree.containsKey(key), tMap.containsKey(key));
				assertEquals(aTree2.containsKey(key), tMap.containsKey(key));

			}
			for (int j = 0; j < numEntries; j++) {
				String val = values.get(j);
				assertEquals(aTree.containsValue(val), tMap.containsValue(val));
				assertEquals(aTree2.containsValue(val), tMap.containsValue(val));
			}

			assertTrue(aTree.equals(tMap));
			assertTrue(tMap.equals(aTree));
			
			assertTrue(aTree2.equals(tMap));
			assertTrue(tMap.equals(aTree2));
			
			assertEquals(aTree.hashCode(), tMap.hashCode());
			assertEquals(aTree2.hashCode(), tMap.hashCode());
			
			assertEquals(aTree.toString(), tMap.toString());
			assertEquals(aTree2.toString(), tMap.toString());
		}
	}

	@Test
	public void getTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		
		AvlGTree<Integer,String> aTree2 = new AvlGTree<Integer,String>(new IntComparator(), 3);

		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();
		int numEntries = 100;

		int iterations = 50;

		for (int i = 0; i < iterations; i++) {
			getRandomEntries(keys, values, numEntries);
			initialize(aTree, tMap, keys, values);
			initialize(aTree2, tMap, keys, values);


			for (int j = 0; j < numEntries; j++) {
				Integer key = keys.get(j);
				assertEquals(aTree.get(key), tMap.get(key));
				assertEquals(aTree2.get(key), tMap.get(key));

			}

			assertTrue(aTree.equals(tMap));
			assertTrue(tMap.equals(aTree));
			
			assertTrue(aTree2.equals(tMap));
			assertTrue(tMap.equals(aTree2));
			
			assertEquals(aTree.hashCode(), tMap.hashCode());
			assertEquals(aTree2.hashCode(), tMap.hashCode());
			
			assertEquals(aTree.toString(), tMap.toString());
			assertEquals(aTree2.toString(), tMap.toString());
		}
	}

	@Test
	public void putAllTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		
		AvlGTree<Integer,String> aTree2 = new AvlGTree<Integer,String>(new IntComparator(), 3);

		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();
		int numEntries = 100;

		int iterations = 50;

		for (int i = 0; i < iterations; i++) {
			getRandomEntries(keys, values, numEntries);
			initialize(aTree, tMap, keys, values);
			initialize(aTree2, tMap, keys, values);

			aTree.clear();
			aTree2.clear();


			aTree.putAll(tMap);
			aTree2.putAll(tMap);

			for (int j = 0; j < numEntries; j++) {
				Integer key = keys.get(j);
				assertEquals(aTree.get(key), tMap.get(key));
				assertEquals(aTree2.get(key), tMap.get(key));

			}

			assertTrue(aTree.equals(tMap));
			assertTrue(tMap.equals(aTree));
			
			assertTrue(aTree2.equals(tMap));
			assertTrue(tMap.equals(aTree2));
			
			assertEquals(aTree.hashCode(), tMap.hashCode());
			assertEquals(aTree2.hashCode(), tMap.hashCode());
			
			assertEquals(aTree.toString(), tMap.toString());
			assertEquals(aTree2.toString(), tMap.toString());
		}
	}

	@Test
	public void sizeTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		
		AvlGTree<Integer,String> aTree2 = new AvlGTree<Integer,String>(new IntComparator(), 3);

		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();

		int iterations = 50;

		for (int i = 0; i < iterations; i++) {
			int size = (int) (Math.random() * 1000);
			getRandomEntries(keys, values, size);
			initialize(aTree, tMap, keys, values);
			initialize(aTree2, tMap, keys, values);


			assertEquals(aTree.size(), tMap.size());
			assertEquals(aTree2.size(), tMap.size());

			assertTrue(aTree.equals(tMap));
			assertTrue(tMap.equals(aTree));
			
			assertTrue(aTree2.equals(tMap));
			assertTrue(tMap.equals(aTree2));
			
			assertEquals(aTree.hashCode(), tMap.hashCode());
			assertEquals(aTree2.hashCode(), tMap.hashCode());
			
			assertEquals(aTree.toString(), tMap.toString());
			assertEquals(aTree2.toString(), tMap.toString());
		}
	}
	
	@Test
	public void equalsTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();
		int numEntries = 100;

		int iterations = 50;

		for (int i = 0; i < iterations; i++) {

			getRandomEntries(keys, values, numEntries);
			initialize(aTree, tMap, keys, values);

			assertTrue(aTree.equals(tMap));
			assertTrue(tMap.equals(aTree));
			
			assertEquals(aTree.hashCode(), tMap.hashCode());
			
			assertEquals(aTree.toString(), tMap.toString());
		}
		
		tMap.put(10, "100");
		
		assertFalse(aTree.equals(tMap));
		assertFalse(tMap.equals(aTree));
		
		
	}
	
	@Test
	public void emptyTreeTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		AvlGTree<Integer,String> aTree2 = new AvlGTree<Integer,String>(new IntComparator(), 3);

		
		assertTrue(aTree.equals(tMap));
		assertTrue(tMap.equals(aTree));
		
		assertEquals(aTree.containsKey(5), tMap.containsKey(5));
		assertEquals(aTree.containsValue("5"), tMap.containsValue("5"));
		assertEquals(aTree.size(), tMap.size());
		assertEquals(aTree.get(10),tMap.get(10));
		assertEquals(aTree.isEmpty(), tMap.isEmpty());
		assertEquals(aTree.entrySet(), tMap.entrySet());
		
		assertEquals(aTree2.containsKey(5), tMap.containsKey(5));
		assertEquals(aTree2.containsValue("5"), tMap.containsValue("5"));
		assertEquals(aTree2.size(), tMap.size());
		assertEquals(aTree2.get(10),tMap.get(10));
		assertEquals(aTree2.isEmpty(), tMap.isEmpty());
		assertEquals(aTree2.entrySet(), tMap.entrySet());
		
		assertEquals(aTree.hashCode(), tMap.hashCode());
		assertEquals(aTree2.hashCode(), tMap.hashCode());
		
		assertEquals(aTree.toString(), tMap.toString());
		assertEquals(aTree2.toString(), tMap.toString());
	}

	/********** Entry Set tests ************/

	@Test
	public void eSetClearTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		AvlGTree<Integer,String> aTree2 = new AvlGTree<Integer,String>(new IntComparator(), 3);

		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		Set<Map.Entry<Integer, String>> aEntry = aTree.entrySet();
		Set<Map.Entry<Integer, String>> aEntry2 = aTree2.entrySet();

		Set<Map.Entry<Integer, String>> tEntry = tMap.entrySet();
		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();

		int numEntries = 100;

		int iterations = 50;

		for (int i = 0; i < iterations; i++) {
			getRandomEntries(keys, values, numEntries);
			initialize(aTree, tMap, keys, values);
			initialize(aTree2, tMap, keys, values);


			aEntry.clear();
			aEntry2.clear();

			tEntry.clear();

			assertEquals(aTree.size(), tMap.size());
			assertEquals(aTree2.size(), tMap.size());
		}
	}

	@Test
	public void eSetContainsTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		Set<Map.Entry<Integer, String>> aEntry = aTree.entrySet();
		Set<Map.Entry<Integer, String>> tEntry = tMap.entrySet();
		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();

		int numEntries = 100;

		int iterations = 50;

		for (int i = 0; i < iterations; i++) {
			getRandomEntries(keys, values, numEntries);
			initialize(aTree, tMap, keys, values);

			for (Map.Entry<Integer, String> entry : tEntry) {
				assertTrue(aEntry.contains(entry));
			}

			assertEquals(aEntry, tEntry);
			
			aTree.clear();
			tMap.clear();
		}
	}

	@Test
	public void eSetContainsAllTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		Set<Map.Entry<Integer, String>> aEntry = aTree.entrySet();
		Set<Map.Entry<Integer, String>> tEntry = tMap.entrySet();
		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();

		int numEntries = 100;

		int iterations = 50;

		for (int i = 0; i < iterations; i++) {
			getRandomEntries(keys, values, numEntries);
			initialize(aTree, tMap, keys, values);

			assertTrue(aEntry.containsAll(tEntry));

			aTree.clear();
			tMap.clear();
		}
	}

	@Test
	public void eSetIterHasNextTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		Set<Map.Entry<Integer, String>> aEntry = aTree.entrySet();
		Set<Map.Entry<Integer, String>> tEntry = tMap.entrySet();
		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();

		int max = 50;
		for (int i = 0; i < max; i++) {
			// assertSame(aTree.put(i, "" + i), tMap.put(i, "" + i));

		}

		getRandomEntries(keys, values, max);
		initialize(aTree, tMap, keys, values);

		Iterator<Map.Entry<Integer, String>> aIter = aEntry.iterator();
		Iterator<Map.Entry<Integer, String>> tIter = tEntry.iterator();
		/*
		 * while(aIter.hasNext()) { System.out.print(aIter.next().getValue() +
		 * " "); } System.out.println(); while(tIter.hasNext()) {
		 * System.out.print(tIter.next().getValue() + " "); }
		 */
		aIter = aEntry.iterator();
		tIter = tEntry.iterator();

		assertEquals(aIter.hasNext(), tIter.hasNext());
		while (tIter.hasNext()) {
			assertEquals(aIter.hasNext(), tIter.hasNext());

			assertEquals(aIter.next(), tIter.next());
		}
		assertEquals(aIter.hasNext(), tIter.hasNext());
	}

	@Test
	public void eSetIteratorTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		Set<Map.Entry<Integer, String>> aEntry = aTree.entrySet();
		Set<Map.Entry<Integer, String>> tEntry = tMap.entrySet();
		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();

		int numEntries = 100;

		int iterations = 50;

		for (int i = 0; i < iterations; i++) {
			getRandomEntries(keys, values, numEntries);
			initialize(aTree, tMap, keys, values);

			Iterator<Map.Entry<Integer, String>> aIter = aEntry.iterator();
			Iterator<Map.Entry<Integer, String>> tIter = tEntry.iterator();

			assertEquals(aIter.hasNext(), tIter.hasNext());
			while (tIter.hasNext()) {
				assertEquals(aIter.hasNext(), tIter.hasNext());
				assertEquals(aIter.next(), tIter.next());
			}
			assertEquals(aIter.hasNext(), tIter.hasNext());
		}
	}

	@Test
	public void eSetSizeTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		Set<Map.Entry<Integer, String>> aEntry = aTree.entrySet();
		Set<Map.Entry<Integer, String>> tEntry = tMap.entrySet();
		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();

		int iterations = 50;

		for (int i = 0; i < iterations; i++) {
			getRandomEntries(keys, values, (int) (Math.random() * 1000));
			initialize(aTree, tMap, keys, values);

			assertEquals(aEntry.size(), tEntry.size());

			aEntry.clear();
			tEntry.clear();
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void eSetToArrayTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		Set<Map.Entry<Integer, String>> aEntry = aTree.entrySet();
		Set<Map.Entry<Integer, String>> tEntry = tMap.entrySet();
		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();

		int numEntries = 100;

		int iterations = 50;

		for (int i = 0; i < iterations; i++) {
			getRandomEntries(keys, values, numEntries);
			initialize(aTree, tMap, keys, values);

			Object[] aA = aEntry.toArray();
			Object[] tA = tEntry.toArray();

			assertEquals(aA.length, tA.length);

			int length = aA.length;

			Map.Entry<Integer, String>[] aArr = new Map.Entry[length];
			Map.Entry<Integer, String>[] tArr = new Map.Entry[length];

			for (int j = 0; j < length; j++) {
				assertTrue(((Map.Entry<Integer, String>) aA[j]).equals(tA[j]));

				aArr = aEntry.toArray(aArr);
				tArr = tEntry.toArray(tArr);

				assertTrue(aArr[j].equals(tArr[j]));
			}
		}
	}

	/********** Submap Testing ********/
	
	@Test
	public void smPutTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		int lower = (int) (Math.random() * 20);
		int upper = (int) (lower + Math.random() * 100);

		SortedMap<Integer, String> sSub = aTree.subMap(lower, upper);
		SortedMap<Integer, String> tSub = tMap.subMap(lower, upper);

		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();

		int numEntries = 100;

		int iterations = 100;

		for (int i = 0; i < iterations; i++) {
			getRandomEntries(keys, values, numEntries);

			int size = keys.size();
			for (int j = 0; j < size; j++) {
				Integer key = keys.get(j);
				String val = values.get(j);
				if (lower <= key.intValue() && upper > key.intValue()) {
					assertEquals(sSub.put(key, val), tSub.put(key, val));
				}
			}
		}
	}

	@Test
	public void smContainsAndGetTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		int lower = (int) (Math.random() * 20);
		int upper = (int) (lower + Math.random() * 100);

		SortedMap<Integer, String> sSub = aTree.subMap(lower, upper);
		SortedMap<Integer, String> tSub = tMap.subMap(lower, upper);

		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();

		int numEntries = 100;

		int iterations = 100;

		for (int i = 0; i < iterations; i++) {
			getRandomEntries(keys, values, numEntries);
			initialize(aTree, tMap, keys, values);

			for (int j = 0; j < numEntries; j++) {
				Integer key = keys.get(j);
				String val = values.get(j);
				assertEquals(sSub.containsKey(key), tSub.containsKey(key));
				assertEquals(sSub.containsValue(val), tSub.containsValue(val));
				assertEquals(sSub.get(key), tSub.get(key));
			}
		}
	}
	
	@Test
	public void smDeterministicSizeTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();
		
		int lower = 12;
		int upper = 37;
		
		SortedMap<Integer, String> sSub = aTree.subMap(lower, upper);
		SortedMap<Integer, String> tSub = tMap.subMap(lower, upper);
		
		for (int i = 0; i < 50; i++) {
			keys.add(i);
			values.add("" + i);
		}		
		initialize(aTree, tMap, keys, values);		
		assertEquals(sSub.size(), tSub.size());		
	}
	
	@Test
	public void smSizeTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();
		
		int lower = (int) (Math.random() * 20);
		int upper = (int) (lower + Math.random() * 100);
		
		SortedMap<Integer, String> sSub = aTree.subMap(lower, upper);
		SortedMap<Integer, String> tSub = tMap.subMap(lower, upper);
		
		int iterations = 100;

		for (int i = 0; i < iterations; i++) {
			getRandomEntries(keys, values, (int) (Math.random() * 1000));
			initialize(aTree, tMap, keys, values);

			assertEquals(sSub.size(), tSub.size());
		}
	}
	
	@Test
	public void smFirstLastKeyTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();
		
		int lower = (int) (Math.random() * 20);
		int upper = (int) (lower + Math.random() * 100);
//		System.out.println(lower);
//		System.out.println(upper);
		
		SortedMap<Integer, String> sSub = aTree.subMap(lower, upper);
		SortedMap<Integer, String> tSub = tMap.subMap(lower, upper);
		
		int iterations = 100;
		int numEntries = 100;
//		int numEntries = 5;
		
		for (int i = 0; i < iterations; i++) {
			getRandomEntries(keys, values, numEntries);
			initialize(aTree, tMap, keys, values);

//			System.out.println(sSub);
//			aTree.debugPrint();
			assertEquals(sSub.firstKey(), tSub.firstKey());
			assertEquals(sSub.lastKey(), tSub.lastKey());
			
		}		
	}
	
	/******** Submap EntrySet() testing ********/
	
	@Test
	public void smESetTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();
		
		int lower = (int) (Math.random() * 20);
		int upper = (int) (lower + Math.random() * 100);
		
		SortedMap<Integer, String> sSub = aTree.subMap(lower, upper);
		SortedMap<Integer, String> tSub = tMap.subMap(lower, upper);
		
		Set<Map.Entry<Integer, String>> aEntry = sSub.entrySet();
		Set<Map.Entry<Integer, String>> tEntry = tSub.entrySet();
		
		int iterations = 100;
		
		int numEntries = 100;

		
		for (int i = 0; i < iterations; i++) {
			getRandomEntries(keys, values, numEntries);
			initialize(aTree, tMap, keys, values);

			assertTrue(aEntry.equals(tEntry));
		}	
	}
	
	@Test
	public void smESetContainsTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		
		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();
		
		int lower = (int) (Math.random() * 20);
		int upper = (int) (lower + Math.random() * 100);
		
		SortedMap<Integer, String> sSub = aTree.subMap(lower, upper);
		SortedMap<Integer, String> tSub = tMap.subMap(lower, upper);
		
		Set<Map.Entry<Integer, String>> aEntry = sSub.entrySet();
		Set<Map.Entry<Integer, String>> tEntry = tSub.entrySet();

		int numEntries = 100;

		int iterations = 50;

		for (int i = 0; i < iterations; i++) {
			getRandomEntries(keys, values, numEntries);
			initialize(aTree, tMap, keys, values);

			for (Map.Entry<Integer, String> entry : tEntry) {
				assertTrue(aEntry.contains(entry));
			}
		}
	}

	@Test
	public void smESetContainsAllTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		
		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();
		
		int lower = (int) (Math.random() * 20);
		int upper = (int) (lower + Math.random() * 100);
		
		SortedMap<Integer, String> sSub = aTree.subMap(lower, upper);
		SortedMap<Integer, String> tSub = tMap.subMap(lower, upper);
		
		Set<Map.Entry<Integer, String>> aEntry = sSub.entrySet();
		Set<Map.Entry<Integer, String>> tEntry = tSub.entrySet();

		int numEntries = 100;

		int iterations = 50;

		for (int i = 0; i < iterations; i++) {
			getRandomEntries(keys, values, numEntries);
			initialize(aTree, tMap, keys, values);

			assertTrue(aEntry.containsAll(tEntry));
		}
	}
	
	@Test
	public void smESetIteratorTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		
		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();
		
		int lower = (int) (Math.random() * 20);
		int upper = (int) (lower + Math.random() * 100);
		
		SortedMap<Integer, String> sSub = aTree.subMap(lower, upper);
		SortedMap<Integer, String> tSub = tMap.subMap(lower, upper);
		
		Set<Map.Entry<Integer, String>> aEntry = sSub.entrySet();
		Set<Map.Entry<Integer, String>> tEntry = tSub.entrySet();

		int numEntries = 100;

		int iterations = 50;

		for (int i = 0; i < iterations; i++) {
			getRandomEntries(keys, values, numEntries);
			initialize(aTree, tMap, keys, values);

			Iterator<Map.Entry<Integer, String>> aIter = aEntry.iterator();
			Iterator<Map.Entry<Integer, String>> tIter = tEntry.iterator();

			assertEquals(aIter.hasNext(), tIter.hasNext());
			while (tIter.hasNext()) {
				assertEquals(aIter.hasNext(), tIter.hasNext());
				assertEquals(aIter.next(), tIter.next());
			}
			assertEquals(aIter.hasNext(), tIter.hasNext());
		}
	}
	
/********* remove() and related functions tests *****/	
	
	@Test
	public void removeDeterministic() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();

		AvlGTree<Integer,String> aTree2 = new AvlGTree<Integer,String>(new IntComparator(), 1);
		int max = 100;
		
		for (int i = 0; i < max; i++) {
			aTree.put(i, "" + i);
			aTree2.put(i, "" + i);
			tMap.put(i, "" + i);

		}
		
		
		
		for (int i = max - 1; i >= 0; i--) {
			assertEquals(aTree.remove(i), "" + i);			
			assertEquals(aTree2.remove(i), "" + i);	
			assertEquals(tMap.remove(i), "" + i);	
		
			assertTrue(aTree.equals(tMap));
			assertTrue(aTree2.equals(tMap));
			assertTrue(tMap.equals(aTree));

		}
		
		for (int i = 0; i < max; i++) {
			aTree.put(i, "" + i);
			aTree2.put(i, "" + i);
			tMap.put(i, "" + i);

		}
		
		for (int i = 0; i < max; i++) {
			assertEquals(aTree.remove(i), "" + i);			
			assertEquals(aTree2.remove(i), "" + i);	
			assertEquals(tMap.remove(i), "" + i);	

			assertTrue(aTree.equals(tMap));
			assertTrue(aTree2.equals(tMap));
			assertTrue(tMap.equals(aTree));

		}
		assertTrue(aTree.isEmpty());
		assertTrue(aTree2.isEmpty());
	}
	
	@Test
	
	public void removeTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		
		AvlGTree<Integer,String> aTree2 = new AvlGTree<Integer,String>(new IntComparator(), 3);
		
		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();
		int numEntries = 100;
		int iterations = 10;

		for (int i = 0; i < iterations; i++) {
			getRandomEntries(keys, values, numEntries);
			initialize(aTree, tMap, keys, values);
			initialize(aTree2, tMap, keys, values);


			int size = keys.size();
//			System.out.println(aTree);
			for (int j = 0; j < size; j++) {
//				System.out.println("foo");
				Integer key = keys.get(j);
//				System.out.println(key);
				String tVal = tMap.remove(key);
				assertEquals(aTree.remove(key), tVal);
				assertEquals(aTree2.remove(key), tVal);

//				System.out.println(aTree2);
//				System.out.println(tMap);
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
	
	@Test
	public void eSetRemoveAllTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		
		AvlGTree<Integer,String> aTree2 = new AvlGTree<Integer,String>(new IntComparator(), 3);
		
		Set<Map.Entry<Integer, String>> aEntry = aTree.entrySet();
		Set<Map.Entry<Integer, String>> aEntry2 = aTree2.entrySet();

		Set<Map.Entry<Integer, String>> tEntry = tMap.entrySet();
		
		
		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();
		List<Integer> keySub = new ArrayList<Integer>();
		List<String> valueSub = new ArrayList<String>();
		int numEntries = 100;
		int iterations = 10;
		
		int numSubset = 20;
		
		Collection<Map.Entry<Integer,String>> c;

		for (int i = 0; i < iterations; i++) {
			getRandomEntries(keys, values, numEntries);
			getRandomEntries(keySub, valueSub, numSubset);

			initialize(aTree, tMap, keys, values);
			initialize(aTree2, tMap, keys, values);
			
			c = entryCollection(keys, values, numSubset);
			

			boolean b = tEntry.removeAll(c);
			assertEquals(b, aEntry.removeAll(c));
			assertEquals(b, aEntry2.removeAll(c));

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
	
	@Test
	public void eSetRetainAllTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		
		AvlGTree<Integer,String> aTree2 = new AvlGTree<Integer,String>(new IntComparator(), 3);
		
		Set<Map.Entry<Integer, String>> aEntry = aTree.entrySet();
		Set<Map.Entry<Integer, String>> aEntry2 = aTree2.entrySet();

		Set<Map.Entry<Integer, String>> tEntry = tMap.entrySet();
		
		
		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();
		List<Integer> keySub = new ArrayList<Integer>();
		List<String> valueSub = new ArrayList<String>();
		int numEntries = 100;
		int iterations = 10;
		
		int numSubset = 20;
		
		Collection<Map.Entry<Integer,String>> c;

		for (int i = 0; i < iterations; i++) {
			getRandomEntries(keys, values, numEntries);
			getRandomEntries(keySub, valueSub, numSubset);

			initialize(aTree, tMap, keys, values);
			initialize(aTree2, tMap, keys, values);
			
			c = entryCollection(keys, values, numSubset);			

			boolean b = tEntry.retainAll(c);
			assertEquals(b, aEntry.retainAll(c));
			assertEquals(b, aEntry2.retainAll(c));

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
	
	@Test
	public void smIterRemoveTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		
		AvlGTree<Integer, String> aTreeCopy = new AvlGTree<Integer, String>(null, 3);

		
		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();
		
		int lower = (int) (Math.random() * 20);
		int upper = (int) (lower + Math.random() * 100);
		
		SortedMap<Integer, String> sSub = aTree.subMap(lower, upper);
		SortedMap<Integer, String> tSub = tMap.subMap(lower, upper);
		
		Set<Map.Entry<Integer, String>> aEntry = sSub.entrySet();
		Set<Map.Entry<Integer, String>> tEntry = tSub.entrySet();
		
		int iterations = 100;
		int numEntries = 100;


		for (int i = 0; i < iterations; i++) {
			getRandomEntries(keys, values, numEntries);
			initialize(aTree, tMap, keys, values);
			initialize(aTreeCopy, tMap, keys, values);


			Iterator<Map.Entry<Integer, String>> aIter = aEntry.iterator();
			Iterator<Map.Entry<Integer, String>> tIter = tEntry.iterator();	
			
			while(aIter.hasNext()) {
				Map.Entry<Integer, String> m = aIter.next();
				aIter.remove();
				assertFalse(aTree.containsKey(m.getKey()));
				assertFalse(sSub.containsKey(m.getKey()));
			}
			
			assertTrue(sSub.isEmpty());
			assertEquals(aEntry.size(), 0);
			
			while(tIter.hasNext()) {
				tIter.next();
				tIter.remove();
			}
			
			assertTrue(sSub.equals(tSub));
			assertTrue(tSub.equals(sSub));
			
			assertTrue(tMap.equals(aTree));
			assertTrue(aTree.equals(tMap));
		}
	}
	
	@Test
	public void smClearTest() {
		AvlGTree<Integer, String> aTree = new AvlGTree<Integer, String>(null, 3);
		TreeMap<Integer, String> tMap = new TreeMap<Integer, String>();
		List<Integer> keys = new ArrayList<Integer>();
		List<String> values = new ArrayList<String>();
		
//		int numEntries = 100;
		int numEntries = 6;
		
		int lower = (int) (Math.random() * 20);
		int upper = (int) (lower + Math.random() * 100);
		System.out.println(lower);
		System.out.println(upper);
		
		SortedMap<Integer, String> sSub = aTree.subMap(lower, upper);
		SortedMap<Integer, String> tSub = tMap.subMap(lower, upper);
		
		int iterations = 100;

		for (int i = 0; i < iterations; i++) {
			getRandomEntries(keys, values, numEntries);
			initialize(aTree, tMap, keys, values);
			aTree.debugPrint();
			sSub.clear();
			tSub.clear();
			aTree.debugPrint();
			assertEquals(sSub.size(), tSub.size());
			
			assertTrue(sSub.equals(tSub));
			assertTrue(tSub.equals(sSub));

			System.out.println(aTree);
			System.out.println(tMap);
			assertTrue(tMap.equals(aTree));
			assertTrue(aTree.equals(tMap));
		}
	}
}
