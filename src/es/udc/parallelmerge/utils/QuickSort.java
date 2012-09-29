package es.udc.parallelmerge.utils;

/**
 * This class implements a version of the quicksort algorithm using a partition
 * algorithm due to C.A.R. Hoare (the inventor of quicksort) that does not rely
 * on the first element of the array being vacant, nor does it guarantee that
 * the chosen pivot value is at the split point of the partition.
 * 
 * This version uses median-of-three partitioning and stack space optimization
 * (smallest-partition-first recursion with removal of tail recursion).
 * 
 * It also halts recursion early and insertion-sorts the entire array at the
 * end.
 * 
 * @author Cay Horstmann, Jonathan Mohr
 */
public class QuickSort {
	// 500,000 elements on Pentium Celeron 2.4 GHz
	// 5 = 1030 ms
	// 10 = 955 ms
	// 20 = 906 ms
	// 30 = 901 ms
	// 35 = 906 ms
	// 40 = 865 ms; 901; 875
	// 45 = 861 ms; 914; 896
	// 50 = 873 ms; 872; 901
	// 60 = 850, 855, 920 ms
	// 75 = 893 ms
	// 100 = 968 ms
	//
	// 1,000,000 elements
	// 40 = 1798, 1806, 1812 ms
	// 50 = 1815, 1801, 1785 ms

	private static final int MIN_SIZE = 50;

	// For testing without use of insertion sort:
	// private static final int MIN_SIZE = 1;

	public QuickSort(int[] anArray) {
		a = anArray;
	}

	/**
	 * Sorts the array managed by this sorter
	 */
	public void sort() {
		sort(0, a.length - 1);
		if (MIN_SIZE > 1)
			insertSort();
	}

	public void sort(int low, int high) {
		// Halt recursion and iteration when the section of the
		// array to be sorted is less than MIN_SIZE long.
		while (high - low >= MIN_SIZE) {
			int p = partition(low, high);

			// Stack space optimization
			if (p - low < high - p) {
				sort(low, p);
				low = p + 1; // tail recursion
			} else {
				sort(p + 1, high);
				high = p; // tail recursion
			}
		}
	}

	private int partition(int low, int high) {
		// First element
		// int pivot = a[low];

		// Middle element
		// int middle = (low + high) / 2;
		// int pivot = a[middle];

		// Median of three - sort low, middle, and high
		int middle = (low + high) / 2;
		if (a[middle] < a[low])
			swap(low, middle);
		if (a[high] < a[low])
			swap(low, high);
		if (a[high] < a[middle])
			swap(middle, high);
		int pivot = a[middle];

		// If using the first or middle element as pivot,
		// initialize 'i' and 'j' as follows. (They will be
		// changed to 'low' and 'high' in the 'while' loop.)
		// int i = low - 1;
		// int j = high + 1;

		// If using median-of-three partitioning, initialize
		// 'i' and 'j' as follows. They will be changed to
		// low+1 and high-1 in the 'while' loop, which is
		// OK because the swaps done while finding the
		// median guarantee that a[low] <= pivot and
		// a[high] >= pivot.
		int i = low;
		int j = high;

		while (i < j) {
			i++;
			while (a[i] < pivot)
				i++;
			j--;
			while (a[j] > pivot)
				j--;
			if (i < j)
				swap(i, j);
		}
		return j;
	}

	/**
	 * Insertion sort
	 */
	private void insertSort() {
		int j, k, temp;

		for (int i = 1; i < a.length; i++) {
			j = i;
			k = j - 1;
			if (a[j] < a[k]) {
				// Shift all previous entries down by the current
				// increment until the proper place is found.
				temp = a[j];
				do {
					a[j] = a[k];
					j = k;
					k = j - 1;
				} while (j != 0 && a[k] > temp);
				a[j] = temp;
			}
		}
	}

	/**
	 * Swaps two entries of the array.
	 * 
	 * @param i
	 *            the first position to swap
	 * @param j
	 *            the second position to swap
	 */
	private void swap(int i, int j) {
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}

	private int[] a;
}
