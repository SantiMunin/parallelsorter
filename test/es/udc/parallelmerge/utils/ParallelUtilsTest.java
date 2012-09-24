package es.udc.parallelmerge.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParallelUtilsTest {

	@Test
	public void testGetHeight() {
		assertEquals(ParallelUtils.getMaxHeight(2), 1);
		assertEquals(ParallelUtils.getMaxHeight(3), 1);
		assertEquals(ParallelUtils.getMaxHeight(4), 2);
		assertEquals(ParallelUtils.getMaxHeight(5), 2);
		assertEquals(ParallelUtils.getMaxHeight(6), 2);
		assertEquals(ParallelUtils.getMaxHeight(7), 2);
		assertEquals(ParallelUtils.getMaxHeight(8), 3);
		assertEquals(ParallelUtils.getMaxHeight(9), 3);
		assertEquals(ParallelUtils.getMaxHeight(10), 3);
		assertEquals(ParallelUtils.getMaxHeight(11), 3);
		assertEquals(ParallelUtils.getMaxHeight(12), 3);
		assertEquals(ParallelUtils.getMaxHeight(13), 3);
		assertEquals(ParallelUtils.getMaxHeight(14), 3);
		assertEquals(ParallelUtils.getMaxHeight(15), 3);
		assertEquals(ParallelUtils.getMaxHeight(16), 4);
		assertEquals(ParallelUtils.getMaxHeight(25), 4);
		assertEquals(ParallelUtils.getMaxHeight(32), 5);
		assertEquals(ParallelUtils.getMaxHeight(45), 5);
	}

	@Test
	public void testCheckParticipation() {
		int nproc = 4;
		assertEquals(ParallelUtils.checkParticipation(nproc, 0, 0), true);
		assertEquals(ParallelUtils.checkParticipation(nproc, 0, 1), true);
		assertEquals(ParallelUtils.checkParticipation(nproc, 0, 2), true);
		assertEquals(ParallelUtils.checkParticipation(nproc, 0, 3), true);
		assertEquals(ParallelUtils.checkParticipation(nproc, 1, 0), true);
		assertEquals(ParallelUtils.checkParticipation(nproc, 1, 1), false);
		assertEquals(ParallelUtils.checkParticipation(nproc, 1, 2), false);
		assertEquals(ParallelUtils.checkParticipation(nproc, 1, 3), false);
		assertEquals(ParallelUtils.checkParticipation(nproc, 2, 0), true);
		assertEquals(ParallelUtils.checkParticipation(nproc, 2, 1), true);
		assertEquals(ParallelUtils.checkParticipation(nproc, 2, 2), false);
		assertEquals(ParallelUtils.checkParticipation(nproc, 2, 3), false);
		assertEquals(ParallelUtils.checkParticipation(nproc, 3, 0), true);
		assertEquals(ParallelUtils.checkParticipation(nproc, 3, 1), false);
		assertEquals(ParallelUtils.checkParticipation(nproc, 3, 2), false);
		assertEquals(ParallelUtils.checkParticipation(nproc, 3, 3), false);
		assertEquals(ParallelUtils.checkParticipation(5, 3, 0), true);
		assertEquals(ParallelUtils.checkParticipation(5, 3, 1), false);
		assertEquals(ParallelUtils.checkParticipation(5, 3, 2), false);
		assertEquals(ParallelUtils.checkParticipation(5, 3, 3), false);
	}
	
	@Test
	public void testRightSon() {
		assertEquals(ParallelUtils.getRightSon(0, 1),1);
		assertEquals(ParallelUtils.getRightSon(0, 2),2);
		assertEquals(ParallelUtils.getRightSon(0, 3),4);
		assertEquals(ParallelUtils.getRightSon(2, 1),3);
		assertEquals(ParallelUtils.getRightSon(4, 1),5);
		assertEquals(ParallelUtils.getRightSon(4, 2),6);
		assertEquals(ParallelUtils.getRightSon(6, 1),7);
	}
	@Test
	public void testGetParent() {
		assertEquals(ParallelUtils.getParent(0, 0),0);
		assertEquals(ParallelUtils.getParent(0, 1),0);
		assertEquals(ParallelUtils.getParent(0, 2),0);
		assertEquals(ParallelUtils.getParent(1, 0),0);
		assertEquals(ParallelUtils.getParent(2, 0),2);
		assertEquals(ParallelUtils.getParent(2, 1),0);
		assertEquals(ParallelUtils.getParent(3, 0),2);

	}
}
