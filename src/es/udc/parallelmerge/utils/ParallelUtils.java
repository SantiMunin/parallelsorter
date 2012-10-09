package es.udc.parallelmerge.utils;

import mpi.MPI;

/**
 * This class contains static methods which wrap the MPI functions.
 * 
 * @author Santiago Munín
 * 
 */
public class ParallelUtils {
	private final static int TAG_SEND_DATA = 1;
	private final static int TAG_SEND_DATA_PARENT = 2;

	/**
	 * Initializes MPI.
	 * 
	 * @param args
	 *            Program args (with FastMPJ data).
	 * @return Only program args (without FastMPJ data).
	 */
	public static String[] initializeParallelEnvironment(String[] args) {
		return MPI.Init(args);
	}

	/**
	 * Gets the process ID.
	 * 
	 * @return Process ID.
	 */
	public static int getMyID() {
		return MPI.COMM_WORLD.Rank();
	}

	/**
	 * Gets the number of processes.
	 * 
	 * @return Number of processes.
	 */
	public static int getNProc() {
		return MPI.COMM_WORLD.Size();
	}

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
		} else {
			// Receive own numbers
			result = new int[numberOfElements];
			MPI.COMM_WORLD.Recv(result, 0, numberOfElements, MPI.INT, 0,
					TAG_SEND_DATA);
		}
		return result;
	}

	/**
	 * Checks whether a process have to keep working or not.
	 * 
	 * @param nproc
	 *            Number of processes.
	 * @param me
	 *            Process ID.
	 * @param height
	 *            Current height in the tree.
	 * @return <b>true</b> if it has to keep working, <b>false</b> if not.
	 */
	public static boolean checkParticipation(int nproc, int me, int height) {
		if (nproc <= me) {
			return false;
		}
		for (int i = 0; i < nproc; i += (int) Math.pow(2, height)) {
			if (i == me) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Exits.
	 * 
	 * @param me
	 *            Number of process.
	 */
	public static void exit(int me) {
		log(me, "I'm no longer needed, bye bye :D");
		MPI.Finalize();
		System.exit(0);
	}

	/**
	 * Sends a sorted array to the parent.
	 * 
	 * @param me
	 *            Process ID.
	 * @param parent
	 *            Parent process ID.
	 * @param array
	 *            Array which will be sent.
	 * 
	 */
	public static void sendResultToParent(int me, int parent, int[] array) {
		log(me, "Sending result to parent (" + parent + "). Array of "
				+ array.length + " elements.");
		MPI.COMM_WORLD.Send(array, 0, array.length, MPI.INT, parent,
				TAG_SEND_DATA_PARENT);
	}

	/**
	 * Receives the result of a son.
	 * 
	 * @param me
	 *            Process which sent data.
	 * @param nproc
	 *            Number of processes.
	 * @param n
	 *            Number of elements.
	 * @param height
	 *            Current level in the sorting tree.
	 * @return A sorted array
	 */
	public static int[] parentGetResult(int me, int nproc, int n, int height) {
		int rightSon = getRightSon(me, height);
		int nfloor = (int) (Math.floor(n / nproc));
		int size = nfloor * ((int) Math.pow(2, height - 1));
		log(me, "Receiving result from " + rightSon + ", size = " + size);
		int[] result = new int[size];
		MPI.COMM_WORLD.Recv(result, 0, size, MPI.INT, rightSon,
				TAG_SEND_DATA_PARENT);
		return result;
	}

	/**
	 * Gets the max height of the tree.
	 * 
	 * @param nproc
	 * @return Sorting tree height.
	 */
	public static int getMaxHeight(int nproc) {
		return (int) Math.floor(Math.log(nproc) / Math.log(2));
	}

	/**
	 * Prints a message with the process ID. Example: <i>[me]: Message</i>
	 * 
	 * @param me
	 *            Process ID.
	 * @param message
	 *            Message.
	 */
	public static void log(int me, String message) {
		System.out.println("[" + me + "]: " + message);

	}

	/**
	 * Gets the parent in the given level.
	 * 
	 * @param me
	 *            Process ID.
	 * @param height
	 *            Current level in the tree.
	 * @return Parent process ID.
	 */
	public static int getParent(int me, int height) {
		return me & ~(1 << height);
	}

	/**
	 * Gets the right son (previous level).
	 * 
	 * @param me
	 *            Process ID.
	 * @param height
	 *            Current height
	 * @return Right son ID.
	 */
	public static int getRightSon(int me, int height) {
		if (height - 1 < 0) {
			return -1;
		}
		return me | (1 << (height - 1));
	}
}
