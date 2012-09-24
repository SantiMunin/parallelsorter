package es.udc.parallelmerge.utils;

public class SortUtils {
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

	private static int[] mergeSort(int[] values, int p, int r) {
		int q;
		int[] result = null;
		if (p < r) {
			q = (p + r) / 2;
			int[] left = mergeSort(values, p, q);
			int[] right = mergeSort(values, q + 1, r);
			result = merge(left, right);
		} else {
			if (p == r && values.length > p) {
				result = new int[1];
				result[0] = values[p];
			}
		}
		return result;
	}

	public static int[] mergeSort(int[] values) {
		return mergeSort(values, 0, values.length - 1);
	}

	public static int[] quickSort(int[] values) {
		// TODO
		return mergeSort(values);
	}

}
