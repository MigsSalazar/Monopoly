package edu.illinois.masalzr2.models;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class GameCardTest {
	
	GameCard gc;

	@Before
	public void beforeClass() {
		gc = new GameCard("I'm a card", 100, true, 5, true, true, "findnearest", "findthis", true, 25, 100);
	}
	
	@Test
	public void testGameCard() {
		assertEquals("I'm a card" ,gc.getText());
		assertEquals(100, gc.getMoneyEarned());
		assertEquals(5 ,gc.getBaseMovement());
		assertEquals("findthis", gc.getFindThis());
		assertEquals("findnearest", gc.getFindNearest());
		assertEquals(25, gc.getHouseCost());
		assertEquals(100, gc.getHotelCost());
		
		assertTrue(gc.isGlobalFunds());
		assertTrue(gc.isGetOutOfJail());
		assertTrue(gc.isGoToJail());
		assertTrue(gc.isPropRenovation());
		
		
	}

	@Test
	public void testSetText() {
		assertEquals("I'm a card", gc.getText());
		gc.setText("Hello, World!");
		assertEquals("Hello, World!", gc.getText());
	}

	@Test
	public void testSetMoneyEarned() {
		assertEquals(100, gc.getMoneyEarned());
		gc.setMoneyEarned(100);
		assertEquals(100, gc.getMoneyEarned());
	}

	@Test
	public void testSetGlobalFunds() {
		assertTrue(gc.isGlobalFunds());
		gc.setGlobalFunds(false);
		assertFalse(gc.isGlobalFunds());
	}

	@Test
	public void testSetBaseMovement() {
		assertEquals(5, gc.getBaseMovement());
		gc.setBaseMovement(-3);
		assertEquals(-3, gc.getBaseMovement());
	}

	@Test
	public void testSetGetOutOfJail() {
		assertTrue(gc.isGetOutOfJail());
		gc.setGetOutOfJail(false);
		assertFalse(gc.isGetOutOfJail());
	}

	@Test
	public void testSetGoToJail() {
		assertTrue(gc.isGoToJail());
		gc.setGoToJail(false);
		assertFalse(gc.isGoToJail());
	}

	@Test
	public void testSetFindNearest() {
		assertEquals("findnearest", gc.getFindNearest());
		gc.setFindNearest("not the nearest");
		assertEquals("not the nearest", gc.getFindNearest() );
	}

	@Test
	public void testSetFindThis() {
		assertEquals("findthis", gc.getFindThis());
		gc.setFindThis("not this");
		assertEquals("not this", gc.getFindThis());
	}

	@Test
	public void testSetPropRenovation() {
		assertTrue(gc.isPropRenovation());
		gc.setPropRenovation(false);
		assertFalse(gc.isPropRenovation());
	}

	@Test
	public void testSetHouseCost() {
		assertEquals(25, gc.getHouseCost());
		gc.setHouseCost(50);
		assertEquals(50, gc.getHouseCost());
	}

	@Test
	public void testSetHotelCost() {
		assertEquals(100, gc.getHotelCost());
		gc.setHotelCost(150);
		assertEquals(150, gc.getHotelCost());
	}

	
}
