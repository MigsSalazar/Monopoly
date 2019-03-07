package edu.illinois.masalzr2.models;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class SuiteTest {
	
	Street avenue = new Street("avenue", 1, 100, "", false, 0, null, 0, 10, new int[] {1,2,3,4,5});
	Street lane = new Street("lane", 2, 200, "", false, 0, null, 0, 20, new int[] {6,7,8,9,10});
	
	Street park = new Street("park", 3, 300, "", false, 0, null, 0, 30, new int[] {11,12,13,14,15});
	Street drive = new Street("drive", 4, 400, "", false, 0, null, 0, 40, new int[] {16,17,18,19,20});
	Street place = new Street("place", 5, 500, "", false, 0, null, 0, 10, new int[] {21,22,23,24,25});
	
	Suite small;
	Suite big;
	
	@Before
	public void beforeAll() {
		small = new Suite(avenue, lane, null, "themSmalls", 0);
		big = new Suite(park, drive, place, "themBigs", -1);
	}

	@Test
	public void testSuite() {
		assertEquals(0, avenue.getColor());
		assertEquals(0, lane.getColor());
		
		assertEquals(-1, park.getColor());
		assertEquals(-1, drive.getColor());
		assertEquals(-1, place.getColor());
	}

	@Test
	public void testGetStreets() {
		List<Street> smalls = small.getStreets();
		assertTrue(smalls.contains(avenue));
		assertTrue(smalls.contains(lane));
		
		List<Street> bigs = big.getStreets();
		assertTrue(bigs.contains(park));
		assertTrue(bigs.contains(drive));
		assertTrue(bigs.contains(place));
	}

	@Test
	public void testSetStreets() {
		testGetStreets();
		small.setStreets(null);
		assertTrue(small.getStreets().isEmpty());
		
		big.setStreets(null);
		assertTrue(big.getStreets().isEmpty());
		
		List<Street> yup = new ArrayList<Street>();
		yup.add(avenue);
		yup.add(drive);
		yup.add(lane);
		yup.add(place);
		yup.add(park);
		
		small.setStreets(yup);
	}

	@Test
	public void testGetNames() {
		List<String> smallNames = small.getNames();
		List<String> bigNames = big.getNames();
		
		assertEquals(2, smallNames.size());
		assertTrue(smallNames.contains("avenue"));
		assertTrue(smallNames.contains("lane"));
		
		assertEquals(3, bigNames.size());
		assertTrue(bigNames.contains("park"));
		assertTrue(bigNames.contains("drive"));
		assertTrue(bigNames.contains("place"));
		
	}
	
	@Test
	public void testRefreshNames() {
		testGetNames();
		
		List<Street> fresh = new ArrayList<Street>();
		
		fresh.add(avenue);
		fresh.add(drive);
		fresh.add(lane);
		fresh.add(place);
		fresh.add(park);
		
		small.setStreets(fresh);
		
		small.refreshNames();
		
		List<String> newNames = small.getNames();
		
		assertEquals("avenue", newNames.get(0));
		assertEquals("lane", newNames.get(1));
		assertEquals("park", newNames.get(2));
		assertEquals("drive", newNames.get(3));
		assertEquals("place", newNames.get(4));
		
	}

	@Test
	public void testIsMonopolized() {
		Player p = mock(Player.class);
		
		when(p.getName()).thenReturn("Player name");
		
		assertFalse(small.isMonopolized(p));
		assertFalse(big.isMonopolized(p));
		
		when(p.getName()).thenReturn("");
		
		assertTrue(small.isMonopolized(p));
		assertTrue(big.isMonopolized(p));
		
	}

	@Test
	public void testIsBuildable() {
		assertTrue(small.isBuildable());
		assertTrue(big.isBuildable());
		
		lane.setOwner("that noob");
		place.setOwner("this noob");
		
		assertFalse(small.isBuildable());
		assertFalse(big.isBuildable());
		
		lane.setOwner("");
		place.setOwner("");
		avenue.setMortgaged(true);
		drive.setMortgaged(true);
		
		assertFalse(small.isBuildable());
		assertFalse(big.isBuildable());
		
		
	}

	@Test
	public void testSortedByPosition() {
		List<Street> reordered = new ArrayList<Street>();
		
		reordered.add(lane);
		reordered.add(park);
		reordered.add(avenue);
		
		big.setStreets(reordered);
		
		List<Street> sorted = big.sortedByPosition();
		
		assertEquals(avenue, sorted.get(0));
		assertEquals(lane, sorted.get(1));
		assertEquals(park, sorted.get(2));
		
	}

	@Test
	public void testSortedByName() {
List<Street> reordered = new ArrayList<Street>();
		
		reordered.add(lane);
		reordered.add(park);
		reordered.add(avenue);
		reordered.add(drive);
		reordered.add(place);
		
		big.setStreets(reordered);
		
		List<Street> sorted = big.sortedByName();
		
		assertEquals(avenue, sorted.get(0));
		assertEquals(drive, sorted.get(1));
		assertEquals(lane, sorted.get(2));
		assertEquals(park, sorted.get(3));
		assertEquals(place, sorted.get(4));
		
	}

	@Test
	public void testHighestGrade() {
		assertEquals(0, big.highestGrade());
		assertEquals(0, small.highestGrade());
		
		avenue.setGrade(5);
		place.setGrade(4);
		
		assertEquals(5, small.highestGrade());
		assertEquals(4, big.highestGrade());
		
	}

	@Test
	public void testLowestGrade() {
		assertEquals(0, big.lowestGrade());
		assertEquals(0, small.lowestGrade());
		
		avenue.setGrade(5);
		place.setGrade(4);
		
		assertEquals(0, big.lowestGrade());
		assertEquals(0, small.lowestGrade());
		
		lane.setGrade(1);
		park.setGrade(1);
		drive.setGrade(1);
		
		assertEquals(1, big.lowestGrade());
		assertEquals(1, small.lowestGrade());
	}

	@Test
	public void testGradeDisparity() {
		assertEquals(0, small.gradeDisparity());
		
		lane.setGrade(5);
		
		assertEquals(5, small.gradeDisparity());
		
		park.setGrade(1);
		
		assertEquals(1, big.gradeDisparity());
		
		avenue.setGrade(4);
		
		assertEquals(1, small.gradeDisparity());
	}

	@Test
	public void testTotalGrade() {
		assertEquals(0, small.totalGrade());
		assertEquals(0, big.totalGrade());
		
		lane.setGrade(2);
		avenue.setGrade(5);
		
		assertEquals(7, small.totalGrade());
	}

	@Test
	public void testGetColorName() {
		assertEquals("themSmalls", small.getColorName());
		assertEquals("themBigs", big.getColorName());
	}

	@Test
	public void testSetColorName() {
		testGetColorName();
		
		small.setColorName("them new smalls!");
		big.setColorName("them new bigs!");
		
		assertEquals("them new smalls!", small.getColorName());
		assertEquals("them new bigs!", big.getColorName());
	}

	@Test
	public void testGetColorValue() {
		assertEquals(0, small.getColorValue());
		assertEquals(-1, big.getColorValue());
	}

	@Test
	public void testSetColorValue() {
		testGetColorValue();
		small.setColorValue(100);
		big.setColorValue(-100);
		assertEquals(100, small.getColorValue());
		assertEquals(-100, big.getColorValue());
	}

}
