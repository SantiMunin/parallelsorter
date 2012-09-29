package es.udc.parallelmerge.utils;

/**
 * Contains sort methods.
 * 
 * @author Santiago Munín
 * 
 */
public class SortUtils {

	/**
	 * Merges two sorted arrays into another sorted which contains both.
	 * 
	 * @param left
	 *            A sorted array.
	 * @param right
	 *            A sorted array.
	 * @return A sorted array which length is left.length + right.length
	 */
	public static int[] merge(int[] left, int[] right) {
		int n1 = left.length;
		int n2 = right.length;
		int[] values = new int[n1 + n2];
		int i = 0, j = 0;
		for (int k = 0; k < n1 + n2; k++) {
			// Checks if left is already dumped.
			if (i >= n1) {
				values[k] = right[j++];
				continue;
			}
			if (j >= n2) {
				values[k] = left[i++];
				continue;
			}
			if (left[i] <= right[j]) {
				values[k] = left[i++];
			} else {
				values[k] = right[j++];
			}
		}
		return values;
	}

	public static int[] sort(int[] values) {
		QuickSort qs = new QuickSort(values);
		qs.sort();
		return values;
	}

	/**
	 * Checks if the given array is sorted (non-decreasing).
	 * 
	 * @param array
	 *            of integers.
	 * @return <b>true</b> if it's sorted, <b>false</b> if not.
	 */
	public static boolean checkSorted(int[] array) {
		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] > array[i + 1]) {
				return false;
			}
		}
		return true;
	}
}
