package es.udc.parallelmerge;

import java.util.Random;

import es.udc.parallelmerge.utils.ParallelUtils;
import es.udc.parallelmerge.utils.SortUtils;
import mpi.MPI;

public class MergeSortUtils {

	private static int[] generate(int n) {
		int[] result = new int[n];
		Random r = new Random();
		for (int i = 0; i < result.length; i++) {
			result[i] = r.nextInt(Integer.MAX_VALUE);
		}
		return result;
	}

	public static void main(String[] args) {
		args = MPI.Init(args);
		int n = 5000000;
		int me = MPI.COMM_WORLD.Rank();
		int nproc = MPI.COMM_WORLD.Size();
		ParallelUtils.checkParticipation(nproc, me, 0);
		long startTime = 0;
		int[] array;
		if (me == 0) {
			array = generate(n);
			startTime = System.currentTimeMillis();
			array = ParallelUtils.initialDistribution(me, array, nproc);
		} else {
			array = ParallelUtils.initialDistribution(me, new int[n/nproc], nproc);
		}
		array = SortUtils.quickSort(array);
		int height = 0;
		while (height < ParallelUtils.getMaxHeight(nproc)) {
			int parent = ParallelUtils.getParent(me, height);
			if (parent == me) {
				int[] childArr = ParallelUtils.parentGetResult(me);
				array = SortUtils.merge(array, childArr);
			} else {
				ParallelUtils.sendResultToParent(me, array);
			}
			height++;
			if (!ParallelUtils.checkParticipation(nproc, me, height)) {
				ParallelUtils.exit(me);
			}
		}
		if (me == 0) {
			long endTime = System.currentTimeMillis();
			System.out.println("Time: " + String.valueOf(endTime - startTime)
					+ " (ms).");
			// Print results
		}
	}
}
