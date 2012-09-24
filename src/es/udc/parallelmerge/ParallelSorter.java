package es.udc.parallelmerge;

import java.util.Random;

import mpi.MPI;
import es.udc.parallelmerge.utils.ParallelUtils;
import es.udc.parallelmerge.utils.SortUtils;

public class ParallelSorter {
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
	 * Executes the calculation
	 * 
	 * @param args
	 *            Args of the main method (they should contain FastMPJ data).
	 */
	public void execute(String[] args) {
		args = MPI.Init(args);
		int n = Integer.valueOf(args[0]);
		int me = MPI.COMM_WORLD.Rank();
		int nproc = MPI.COMM_WORLD.Size();
		ParallelUtils.checkParticipation(nproc, me, 0);
		long startTime = 0;
		int[] array = new int[n];
		if (me == 0) {
			array = generate(n);
			startTime = System.currentTimeMillis();
		}
		array = ParallelUtils.initialDistribution(me, array, nproc);
		array = SortUtils.sort(array);
		int height = 0;
		while (height < ParallelUtils.getMaxHeight(nproc)) {
			int parent = ParallelUtils.getParent(me, height);
			height++;
			if (me == parent) {
				int[] childArr = ParallelUtils.parentGetResult(me, nproc, n,
						height);
				array = SortUtils.merge(array, childArr);
			} else {
				ParallelUtils.sendResultToParent(me, parent, array);
				ParallelUtils.log(me, "Done");
			}
			if (!ParallelUtils.checkParticipation(nproc, me, height)) {
				ParallelUtils.exit(me);
			}
		}
		if (me == 0) {
			// Print result
			long endTime = System.currentTimeMillis();
			ParallelUtils.log(me,
					"Time: " + String.valueOf(endTime - startTime) + " (ms).");
		}
		ParallelUtils.exit(me);
	}

	public static void main(String[] args) {
		ParallelSorter ps = new ParallelSorter();
		ps.execute(args);
	}
}
