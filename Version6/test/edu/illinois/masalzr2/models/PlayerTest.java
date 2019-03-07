package edu.illinois.masalzr2.models;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.ChangeListener;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class PlayerTest {
	
	List<ChangeListener> changers = new ArrayList<ChangeListener>();
	Map<String,Property> props = new HashMap<String, Property>();
	
	Street avenue = mock(Street.class);
	Railroad rail = mock(Railroad.class);
	Utility util = mock(Utility.class);
	Counter allCount = mock(Counter.class);
	ChangeListener environment = mock(ChangeListener.class);
	Player testMulti;
	Player testSingle;
	
	@Before
	public void beforeAll() {
		
		when(avenue.getName()).thenReturn("avenue");
		when(rail.getName()).thenReturn("rail");
		when(util.getName()).thenReturn("util");
		
		when(avenue.getLiquidationWorth()).thenReturn(1);
		when(rail.getLiquidationWorth()).thenReturn(10);
		when(util.getLiquidationWorth()).thenReturn(100);
		
		when(avenue.getWorth()).thenReturn(2);
		when(rail.getWorth()).thenReturn(20);
		when(util.getWorth()).thenReturn(200);
		
		doCallRealMethod().when(rail).setRailsOwned(any());
		doCallRealMethod().when(util).setUtilityOwned(any());
		
		rail.setRailsOwned(allCount);
		util.setUtilityOwned(allCount);

		when(rail.getRailsOwned()).thenReturn(allCount);
		when(util.getUtilityOwned()).thenReturn(allCount);
		
		doCallRealMethod().when(avenue).setOwner(any());
		doCallRealMethod().when(rail).setOwner(any());
		doCallRealMethod().when(util).setOwner(any());
		
		testSingle = new Player(1500);
		testSingle.addChangeListener(environment);
	}
	
	@Test
	public void testPlayerMultiContructor() {
		
		doCallRealMethod().when(avenue).addListener(any());
		doCallRealMethod().when(rail).addListener(any());
		doCallRealMethod().when(util).addListener(any());
		
		
		
		when(allCount.add(anyInt())).thenCallRealMethod();
		
		doNothing().when(environment).stateChanged(any());
		
		props.put("avenue", avenue);
		props.put("rail", rail);
		props.put("util", util);
		
		changers.add(environment);
		
		testMulti = new Player("name", 0, 100, 0, false, props, changers);
		
		verify(avenue).addListener(any());
		verify(rail).addListener(any());
		verify(util).addListener(any());
		verify(environment).stateChanged(any());
		verify(avenue).fireChange();
		verify(rail).fireChange();
		verify(util).fireChange();
	}

	@Test
	public void testPlayerSingle() {
		testSingle = new Player(1500);
		
		verify(avenue, never()).addListener(any());
		verify(rail, never()).addListener(any());
		verify(util, never()).addListener(any());
		verify(environment, never()).stateChanged(any());
		verify(avenue, never()).fireChange();
		verify(rail, never()).fireChange();
		verify(util, never()).fireChange();
	}

	@Test
	public void testAddCash() {
		testSingle.addCash(500);
		assertEquals(2000, testSingle.getCash());
		testSingle.addCash(-500);
		assertEquals(1500, testSingle.getCash());
	}

	@Test
	public void testSubCash() {
		testSingle.subCash(500);
		assertEquals(1000, testSingle.getCash());
		testSingle.subCash(-500);
		assertEquals(1500, testSingle.getCash());
	}

	@Test
	public void testAddJailCard() {
		testSingle.addJailCard(10);
		assertEquals(10, testSingle.getJailCard());
		testSingle.addJailCard(-10);
		assertEquals(0, testSingle.getJailCard());
	}

	@Test
	public void testAddOneJailCard() {
		assertEquals(0, testSingle.getJailCard());
		testSingle.addOneJailCard();
		assertEquals(1, testSingle.getJailCard());
	}

	@Test
	public void testSubJailCard() {
		testSingle.subJailCard(1);
		assertEquals(-1, testSingle.getJailCard());
		testSingle.subJailCard(-1);
		assertEquals(0, testSingle.getJailCard());
	}

	@Test
	public void testSubOneJailCard() {
		testSingle.subOneJailCard();
		assertEquals(-1, testSingle.getJailCard());
	}

	@Test
	public void testOwnsProp() {
		assertFalse(testSingle.ownsProp(avenue));
		assertFalse(testSingle.ownsProp(rail));
		assertFalse(testSingle.ownsProp(util));
		
		testSingle.addProp(avenue);
		testSingle.addProp(rail);
		testSingle.addProp(util);
		
		assertTrue(testSingle.ownsProp(avenue));
		assertTrue(testSingle.ownsProp(rail));
		assertTrue(testSingle.ownsProp(util));
		
	}

	@Test
	public void testAddProp() {
		testSingle.addProp(avenue);
		verify(environment).stateChanged(any());
		testSingle.addProp(rail);
		verify(environment, atMost(2)).stateChanged(any());
		testSingle.addProp(util);
		verify(environment, atMost(3)).stateChanged(any());
	}

	@Test
	public void testAddProperties() {
		
		
		props.put("avenue", avenue);
		props.put("rail", rail);
		props.put("util", util);
		
		testSingle.addProperties(props.values());
		
		assertTrue(testSingle.ownsProp(avenue));
		assertTrue(testSingle.ownsProp(rail));
		assertTrue(testSingle.ownsProp(util));
		
	}

	@Test
	public void testRemoveProp() {
		props.put("avenue", avenue);
		props.put("rail", rail);
		props.put("util", util);
		
		testSingle.addProperties(props.values());
		
		assertTrue(testSingle.ownsProp(avenue));
		assertTrue(testSingle.ownsProp(rail));
		assertTrue(testSingle.ownsProp(util));
		
		testSingle.removeProp(avenue);
		assertFalse(testSingle.ownsProp(avenue));
		
		testSingle.removeProp(rail);
		assertFalse(testSingle.ownsProp(rail));
		
		testSingle.removeProp(util);
		assertFalse(testSingle.ownsProp(util));
		
	}

	@Test
	public void testGetWealth() {
		assertEquals(1500, testSingle.getWealth());
		
		testSingle.addProp(avenue);
		assertEquals(1502, testSingle.getWealth());
		
		testSingle.addProp(rail);
		assertEquals(1522, testSingle.getWealth());
		
		testSingle.addProp(util);
		assertEquals(1722, testSingle.getWealth());
		
		testSingle.removeProp(rail);
		assertEquals(1702, testSingle.getWealth());
		
		testSingle.removeProp(avenue);
		assertEquals(1700, testSingle.getWealth());
		
		testSingle.removeProp(util);
		assertEquals(1500, testSingle.getWealth());
	}

	@Test
	public void testGetLiquidationWorth() {
		assertEquals(1500, testSingle.getLiquidationWorth());
		
		testSingle.addProp(avenue);
		assertEquals(1501, testSingle.getLiquidationWorth());
		
		testSingle.addProp(rail);
		assertEquals(1511, testSingle.getLiquidationWorth());
		
		testSingle.addProp(util);
		assertEquals(1611, testSingle.getLiquidationWorth());
		
		testSingle.removeProp(rail);
		assertEquals(1601, testSingle.getLiquidationWorth());
		
		testSingle.removeProp(avenue);
		assertEquals(1600, testSingle.getLiquidationWorth());
		
		testSingle.removeProp(util);
		assertEquals(1500, testSingle.getLiquidationWorth());
	}

	@Test
	public void testGetListeners() {
		assertTrue(testSingle.getListeners().contains(environment));
		assertEquals(1, testSingle.getListeners().size());
		testSingle.setListeners(null);
		assertFalse(testSingle.getListeners().contains(environment));
		assertEquals(0, testSingle.getListeners().size());
	}

	@Test
	public void testAddChangeListener() {
		assertTrue(testSingle.getListeners().contains(environment));
		assertEquals(1, testSingle.getListeners().size());
		
		testSingle.addChangeListener(environment);
		
		assertTrue(testSingle.getListeners().contains(environment));
		assertEquals(1, testSingle.getListeners().size());
		
		testSingle.addChangeListener(testMulti);
		
		assertTrue(testSingle.getListeners().contains(testMulti));
		assertEquals(2, testSingle.getListeners().size());
		
		testSingle.setListeners(null);
		testSingle.addChangeListener(environment);
		assertTrue(testSingle.getListeners().contains(environment));
		assertEquals(1, testSingle.getListeners().size());
		
	}

	@Test
	public void testRemoveListener() {
		assertTrue(testSingle.getListeners().contains(environment));
		assertEquals(1, testSingle.getListeners().size());
		
		testSingle.removeListener(environment);
		
		assertFalse(testSingle.getListeners().contains(environment));
		assertEquals(0, testSingle.getListeners().size());
		
	}

	@Test
	public void testStateChanged() {
		verify(environment, never()).stateChanged(any());
		
		testSingle.addCash(1);
		
		verify(environment).stateChanged(any());
		
		testSingle.setListeners(null);
		
		verify(environment).stateChanged(any());
		
		testSingle.addCash(1);
		
		verify(environment).stateChanged(any());
		
		testSingle.addChangeListener(environment);
		testSingle.stateChanged(null);
		
		verify(environment, atMost(2)).stateChanged(any());
	}

	@Test
	public void testGetName() {
		assertEquals("", testSingle.getName());
	}

	@Test
	public void testSetName() {
		testGetName();
		testSingle.setName("new name");
		assertEquals("new name", testSingle.getName());
	}

	@Test
	public void testGetId() {
		assertEquals(0, testSingle.getId());
	}

	@Test
	public void testSetId() {
		testGetId();
		testSingle.setId(1);
		assertEquals(1, testSingle.getId());
	}

	@Test
	public void testGetCash() {
		assertEquals(1500, testSingle.getCash());
	}

	@Test
	public void testSetCash() {
		testGetCash();
		testSingle.setCash(200);
		assertEquals(200, testSingle.getCash());
	}

	@Test
	public void testGetJailCard() {
		assertEquals(0, testSingle.getJailCard());
	}

	@Test
	public void testSetJailCard() {
		testGetJailCard();
		testSingle.setJailCard(10);
		assertEquals(10, testSingle.getJailCard());
	}

	@Test
	public void testIsBankrupt() {
		assertFalse(testSingle.isBankrupt());
	}

	@Test
	public void testSetBankrupt() {
		testIsBankrupt();
		testSingle.setBankrupt(true);
		assertTrue(testSingle.isBankrupt());
	}

	@Test
	public void testGetProps() {
		assertTrue(testSingle.getProps().isEmpty());
	}

	@Test
	public void testSetProps() {
		testGetProps();
		props.put("a", avenue);
		props.put("r", rail);
		props.put("u", util);
		
		testSingle.setProps(props);
		
		assertTrue(testSingle.ownsProp(avenue));
		assertTrue(testSingle.ownsProp(rail));
		assertTrue(testSingle.ownsProp(util));
	}

	@Test
	public void testSetListeners() {
		testGetListeners();
		testSingle.addChangeListener(environment);
		assertTrue(testSingle.getListeners().contains(environment));
		assertEquals(1, testSingle.getListeners().size());
	}
	
	@Test
	public void testWealthComparator() {
		
		testMulti = new Player("name", 0, 100, 0, false, props, changers);
		
		testMulti.setCash(2000);
		testSingle.setCash(1);
		
		List<Player> players = new ArrayList<Player>();
		
		players.add(testMulti);
		players.add(testSingle);
		
		players.sort(Player.WEALTH_ORDER);
		
		assertTrue(players.get(0).equals(testSingle));
		assertTrue(players.get(1).equals(testMulti));
		
		players.clear();
		
		players.add(testSingle);
		players.add(testMulti);
		
		assertTrue(players.get(0).equals(testSingle));
		assertTrue(players.get(1).equals(testMulti));
		
	}
	
	@Test
	public void testIdComparator() {
		testMulti = new Player("name", 1, 100, 0, false, props, changers);
		testSingle.setId(0);
		
		List<Player> players = new ArrayList<Player>();
		
		players.add(testMulti);
		players.add(testSingle);
		
		players.sort(Player.ID_ORDER);
		
		assertTrue(players.get(0).equals(testSingle));
		assertTrue(players.get(1).equals(testMulti));
		
		players.clear();
		
		players.add(testSingle);
		players.add(testMulti);
		
		assertTrue(players.get(0).equals(testSingle));
		assertTrue(players.get(1).equals(testMulti));
	}
	
	@Test
	public void testNameComparator() {
		testMulti = new Player("b", 1, 100, 0, false, props, changers);
		testSingle.setName("a");
		
		List<Player> players = new ArrayList<Player>();
		
		players.add(testMulti);
		players.add(testSingle);
		
		players.sort(Player.NAME_ORDER);
		
		assertTrue(players.get(0).equals(testSingle));
		assertTrue(players.get(1).equals(testMulti));
		
		players.clear();
		
		players.add(testSingle);
		players.add(testMulti);
		
		assertTrue(players.get(0).equals(testSingle));
		assertTrue(players.get(1).equals(testMulti));
	}

}
