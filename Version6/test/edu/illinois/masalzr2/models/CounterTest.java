package edu.illinois.masalzr2.models;

import static org.junit.Assert.*;

import org.junit.Test;

public class CounterTest {

	@Test
	public void testCounter() {
		//fail("Not yet implemented");
		Counter c = new Counter();
		assertEquals(0, c.getCount());
		assertEquals(0, c.getMax());
		assertEquals(0, c.getMin());
	}

	@Test
	public void testCounterInt() {
		Counter c = new Counter(1);
		assertEquals(1, c.getCount());
		assertEquals(0, c.getMax());
		assertEquals(0, c.getMin());
		
		c = new Counter(2);
		assertEquals(2, c.getCount());
		assertEquals(0, c.getMax());
		assertEquals(0, c.getMin());
		
		c = new Counter(-2);
		assertEquals(-2, c.getCount());
		assertEquals(0, c.getMax());
		assertEquals(0, c.getMin());
	}

	@Test
	public void testCounterIntIntInt() {
		Counter c = new Counter(0, 40, -5);
		assertEquals(0, c.getCount());
		assertEquals(40, c.getMax());
		assertEquals(-5, c.getMin());
		
		c = new Counter(10, 6, 0);
		assertEquals(4, c.getCount());
		assertEquals(6, c.getMax());
		assertEquals(0, c.getMin());
		
		c = new Counter(-1, 5, 0);
		assertEquals(0, c.getCount());
		assertEquals(5, c.getMax());
		assertEquals(0, c.getMin());
	}

	@Test
	public void testSetCount() {
		Counter c = new Counter();
		assertEquals(0, c.getCount());
		
		c.setCount(1);
		assertEquals(1, c.getCount());
		
		c.setCount(Integer.MAX_VALUE);
		assertEquals(Integer.MAX_VALUE, c.getCount());
		
		c.setCount(Integer.MIN_VALUE);
		assertEquals(Integer.MIN_VALUE, c.getCount());
		
		c = new Counter(0, 5, 0);
		assertEquals(0, c.getCount());
		
		c.setCount(8);
		assertEquals(3, c.getCount());
		
		c.setCount(-1);
		assertEquals(0, c.getCount());
		
	}

	@Test
	public void testAdd() {
		Counter c = new Counter();
		
		assertEquals(0, c.getCount());
		
		c.add(0);
		
		assertEquals(0, c.getCount());
		
		c.add(1);
		
		assertEquals(1, c.getCount());
		
		c.add(-3);
		
		assertEquals(-2, c.getCount());
		
	}

	@Test
	public void testSub() {
		Counter c = new Counter();
		
		assertEquals(0, c.getCount());
		
		c.sub(0);
		
		assertEquals(0, c.getCount());
		
		c.sub(1);
		
		assertEquals(-1, c.getCount());
		
		c.sub(-3);
		
		assertEquals(2, c.getCount());
	}

	@Test
	public void testSetMax() {
		Counter c = new Counter();
		assertEquals(0, c.getCount());
		c.setCount(10);
		assertEquals(10, c.getCount());
		c.setMax(7);
		assertEquals(3, c.getCount());
		assertEquals(7, c.getMax());
		
	}

	@Test
	public void testSetMin() {
		Counter c = new Counter(0, 2, 0);
		c.setCount(-3);
		assertEquals(c.getMin(), c.getCount());
		c.setMin(-4);
		assertEquals(-4, c.getMin());
		assertEquals(0, c.getCount());
		c.setCount(-4);
		assertEquals(-4, c.getCount());
		c.setCount(-8);
		assertEquals(-4, c.getCount());
	}

}
