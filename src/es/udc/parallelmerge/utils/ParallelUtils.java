package es.udc.parallelmerge.utils;

import mpi.MPI;

public class ParallelUtils {
	private final static int TAG_SEND_DATA = 1;

	/**
	 * Distributes the array between all the processes.
	 * 
	 * @param me
	 *            Process ID.
	 * @param array
	 *            Data.
	 * @param nproc
	 *            Number of process.
	 * @return The part regarding to each process.
	 */
	public static int[] initialDistribution(int me, int[] array, int nproc) {
		log(me, "Distributing.");
		int numberOfElements = array.length / nproc;
		int[] result;
		if (me == 0) {
			// 0 numbers
			result = new int[numberOfElements + array.length % nproc];
			for (int i = 0; i < result.length; i++) {
				result[i] = array[i];
			}
			// Send others' numbers
			int[] buffer = new int[numberOfElements];
			for (int i = 1; i < nproc; i++) {
				for (int j = 0; j < buffer.length; j++) {
					buffer[j] = array[(numberOfElements + array.length % nproc)
							+ (i - 1) * numberOfElements + j];
				}
				MPI.COMM_WORLD.Send(buffer, 0, numberOfElements, MPI.INT, i,
						TAG_SEND_DATA);
			}
			log(me, "Done");
		} else {
			// Receive own numbers
			result = new int[numberOfElements];
			MPI.COMM_WORLD.Recv(result, 0, numberOfElements, MPI.INT, 0,
					TAG_SEND_DATA);
		}
		log(me, "Distribution finished.");
		log(me, "Done");
		return result;
	}

	public static boolean checkParticipation(int nproc, int me, int height) { 
		if (nproc <= me) {
			return false;
		}
		for (int i = 0; i < nproc; i+=(int)Math.pow(2, height)) {
			if (i == me) {
				return true;
			}
		}
		return false;
	}
	
	public static void exit(int me) {
		log(me, "I'm no longer needed, bye bye :D");
		MPI.Finalize();
		System.exit(0);
	}

	public static void sendResultToParent(int me, int[] array) {
		// TODO
	}

	public static int[] parentGetResult(int me) {
		// TODO
		return new int[1];
	}

	public static int getMaxHeight(int nproc) {
		return (int) Math.floor(Math.log(nproc) / Math.log(2));
	}

	public static void log(int me, String message) {
		System.out.println("[" + me + "]: " + message);

	}

	public static int getParent(int me, int height) {
		return me & ~(1 << height);
	}

	public static int getRightSon(int me, int height) {
		if (height - 1 < 0) {
			return -1;
		}
		return me | (1 << (height - 1));

	}
}
