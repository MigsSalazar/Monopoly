package edu.illinois.masalzr2.models;

import static org.junit.Assert.*;

import javax.swing.event.ChangeListener;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

public class PropertyTest {

	Property simple;
	Property complex;
	
	ChangeListener listen;
	
	@Before
	public void beforeAll() {
		
		listen = mock(ChangeListener.class);
		doNothing().when(listen).stateChanged(any());
		
		simple = new Property();
		complex = new Property("prop", 2, 200, "owner", false, 0, null);
	}
	
	@Test
	public void testPropertyVerboseConstructor() {
		assertNotNull(complex);
		List<ChangeListener> meep = new ArrayList<ChangeListener>();
		complex = new Property("prop", 2, 200, "owner", false, 0, meep);
		assertNotNull(complex);
	}

	@Test
	public void testPropertyDefaultConstructor() {
		assertNotNull(simple);
	}
	
	@Test
	public void testIsMortgaged() {
		assertFalse(simple.isMortgaged());
		assertFalse(complex.isMortgaged());
	}

	@Test
	public void testSetMBool() {
		testIsMortgaged();
		
		simple.setMortgaged(true);
		assertTrue(simple.isMortgaged());
		
		simple.addListener(listen);
		simple.setMortgaged(false);
		
		assertFalse(simple.isMortgaged());
		verify(listen).stateChanged(any());
	}

	@Test
	public void testGetWorth() {
		assertEquals(200, complex.getWorth());
		complex.setMortgaged(true);
		assertEquals(100, complex.getWorth());
	}

	@Test
	public void testGetLiquidationWorth() {
		assertEquals(100, complex.getLiquidationWorth());
		complex.setMortgaged(true);
		assertEquals(0, complex.getLiquidationWorth());
	}
	
	@Test
	public void testGetOwner() {
		assertEquals("", simple.getOwner());
		assertEquals("owner", complex.getOwner());
	}

	@Test
	public void testSetOwner() {
		testGetOwner();
		simple.addListener(listen);
		simple.setOwner("new owner");
		verify(listen).stateChanged(any());
	}

	@Test
	public void testMortgageValue() {
		assertEquals(100, complex.mortgageValue());
		complex.setMortgaged(true);
		assertEquals(100, complex.mortgageValue());
	}

	@Test
	public void testGetRent() {
		assertEquals(0, simple.getRent());
		assertEquals(0, complex.getRent());
	}

	@Test
	public void testGetColor() {
		assertEquals(1, simple.getColor());
		assertEquals(0, complex.getColor());
	}
	
	@Test
	public void testSetColor() {
		testGetColor();
		
		simple.setColor(100);
		assertEquals(100, simple.getColor());
		
	}

	@Test
	public void testAddListener() {
		simple.addListener(listen);
		simple.setMortgaged(true);
		verify(listen).stateChanged(any());
		simple.addListener(listen);
		simple.setMortgaged(true);
		verify(listen, atMost(2)).stateChanged(any());
	}

	@Test
	public void testRemoveListener() {
		simple.addListener(listen);
		simple.setMortgaged(true);
		verify(listen).stateChanged(any());
		simple.removeListener(listen);
		simple.setMortgaged(false);
		verify(listen).stateChanged(any());
	}

	@Test
	public void testFireChange() {
		simple.addListener(listen);
		simple.fireChange();
		verify(listen).stateChanged(any());
	}

	@Test
	public void testGetName() {
		assertEquals("", simple.getName());
		assertEquals("prop", complex.getName());
	}

	@Test
	public void testSetName() {
		testGetName();
		
		simple.setName("simple");
		complex.setName("complex");
		
		assertEquals("simple", simple.getName());
		assertEquals("complex", complex.getName());
	}

	@Test
	public void testGetPosition() {
		assertEquals(-1, simple.getPosition());
		assertEquals(2, complex.getPosition());
	}

	@Test
	public void testSetPosition() {
		testGetPosition();
		
		simple.setPosition(100);
		complex.setPosition(200);
		
		assertEquals(100, simple.getPosition());
		assertEquals(200, complex.getPosition());
	}

	@Test
	public void testGetPrice() {
		assertEquals(0, simple.getPrice());
		assertEquals(200, complex.getPrice());
	}

	@Test
	public void testSetPrice() {
		testGetPrice();
		
		simple.setPrice(1000);
		
		assertEquals(1000, simple.getPrice());
	}

}
