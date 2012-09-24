package es.udc.parallelmerge.utils;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test class of SortUtils.
 * 
 * @author Santiago Munín
 * 
 */
public class SortUtilsTest {
	/**
	 * Tests SortUtils.checkSorted(array).
	 */
	@Test
	public void testCheckSorted() {
		int[] sorted = { 0, 1, 2, 3, 4, 5, 6, 8, 9, 10 };
		assertTrue(SortUtils.checkSorted(sorted));
		int[] unsorted = { 1, 0, 2, 3, 4, 5, 6, 7 };
		assertFalse(SortUtils.checkSorted(unsorted));
		int[] unsorted2 = { 0, 1, 2, 3, 4, 5, 7, 6 };
		assertFalse(SortUtils.checkSorted(unsorted2));
	}

	/**
	 * Tests SortUtils.merge(left, right).
	 */
	@Test
	public void testMerge() {
		int[] sorted1 = { 0, 2, 3, 6, 8 };
		int[] sorted2 = { 1, 4, 5, 7, 9 };
		int[] expected = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		int[] merged = SortUtils.merge(sorted1, sorted2);
		assertEquals(merged.length, sorted1.length + sorted2.length);
		for (int i = 0; i < merged.length; i++) {
			assertEquals(merged[i], expected[i]);

		}
	}

	/**
	 * Tests SortUtils.sort(array).
	 */
	@Test
	public void testSort() {
		int[] unsorted = { 10, 100, 1, 2, 3, 4, 0 };
		int[] expected = { 0, 1, 2, 3, 4, 10, 100 };
		int[] sorted = SortUtils.sort(unsorted);
		assertEquals(sorted.length, expected.length);
		for (int i = 0; i < sorted.length; i++) {
			assertEquals(sorted[i], expected[i]);
		}
	}
}
