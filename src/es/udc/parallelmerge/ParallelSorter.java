package es.udc.parallelmerge;

import java.util.Random;

import es.udc.parallelmerge.utils.ParallelUtils;
import es.udc.parallelmerge.utils.SortUtils;

/**
 * Executes a parallel environment and sorts an array.
 * 
 * @author Santiago Munín
 * 
 */
public class ParallelSorter {

	private int n;
	private int me;
	private int nproc;
	private long startTime;
	private int[] array;

	/**
	 * Generates randomly an array of numbers.
	 * 
	 * @param n
	 *            Array size.
	 * @return Generated array.
	 */
	private int[] generate(int n) {
		int[] result = new int[n];
		Random r = new Random();
		for (int i = 0; i < result.length; i++) {
			result[i] = r.nextInt(Integer.MAX_VALUE);
		}
		return result;
	}

	/**
	 * Initializes the parallel environment.
	 * 
	 * @param args
	 *            Program args (with FastMPJ data).
	 */
	private void initEnvironment(String[] args) {
		args = ParallelUtils.initializeParallelEnvironment(args);
		n = Integer.valueOf(args[0]);
		me = ParallelUtils.getMyID();
		nproc = ParallelUtils.getNProc();
		// Levels of the tree
		int logn = (int) Math.floor(Math.log(nproc) / Math.log(2));
		// If nproc isn't a power of two, reduce nproc.
		if ((int) Math.pow(2, logn) != nproc) {
			ParallelUtils.log(me, "Last nproc:" + nproc);
			nproc = (int) Math.pow(2, logn);
			ParallelUtils.log(me, "New nproc:" + nproc);
		}
		if (!ParallelUtils.checkParticipation(nproc, me, 0)) {
			ParallelUtils.exit(me);
		}
		array = new int[n];
		if (me == 0) {
			array = generate(n);
			startTime = System.currentTimeMillis();
		}
	}

	/**
	 * Performs the job (calculations).
	 */
	private void doJob() {
		array = ParallelUtils.initialDistribution(me, array, nproc);
		array = SortUtils.sort(array);
		int height = 0;
		// If nproc == 1 (sequential), it won't enter into the loop
		while (height < ParallelUtils.getMaxHeight(nproc)) {
			int parent = ParallelUtils.getParent(me, height);
			height++;
			if (me == parent) {
				int[] childArr = ParallelUtils.parentGetResult(me, nproc, n,
						height);
				array = SortUtils.merge(array, childArr);
			} else {
				ParallelUtils.sendResultToParent(me, parent, array);
			}
			if (!ParallelUtils.checkParticipation(nproc, me, height)) {
				ParallelUtils.exit(me);
			}
		}
	}

	/**
	 * Executes the calculation
	 * 
	 * @param args
	 *            Arguments of the main method (they should contain FastMPJ
	 *            data).
	 */
	public void execute(String[] args) {
		initEnvironment(args);
		doJob();
		if (me == 0) {
			// Print result
			printResult(me, startTime, array, n);
		}
		ParallelUtils.exit(me);
	}

	/**
	 * Prints the time it took and checks if the answer is correct.
	 * 
	 * @param me
	 *            Process ID.
	 * @param startTime
	 *            Start time (in ms).
	 * @param array
	 *            Sorted array.
	 * @param n
	 *            Number of elements.
	 */
	private static void printResult(int me, long startTime, int[] array, int n) {
		long endTime = System.currentTimeMillis();
		ParallelUtils.log(me, "Time: " + String.valueOf(endTime - startTime)
				+ " (ms).");
		ParallelUtils.log(me, "Checking if the result is correct...");
		if (SortUtils.checkSorted(array) && array.length == n) {
			// TODO improve
			ParallelUtils.log(me, "\t...It's right!");
		} else {
			ParallelUtils.log(me, "\tUgh...!");
		}
	}

	public static void main(String[] args) {
		ParallelSorter ps = new ParallelSorter();
		ps.execute(args);
	}
}
