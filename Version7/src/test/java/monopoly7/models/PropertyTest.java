package monopoly7.models;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import monopoly7.event.PropertyChangeEvent;
import monopoly7.event.PropertyChangeListener;
import monopoly7.event.PropertyChangeEvent.ChangeCode;

public class PropertyTest {
	
	private Property prop;
	private PropertyChangeListener changeMock;
	private PropertyChangeEvent pce = null;
	
	@Before
	public void setup(){
		prop = new Property();
		changeMock = mock(PropertyChangeListener.class);
		pce = null;
		doAnswer( (InvocationOnMock i) -> {
				for( Object o : i.getArguments() ){
					if( o instanceof PropertyChangeEvent )
						pce = (PropertyChangeEvent)o;
				}
				return null;
			}
		).when(changeMock).propertyStateChanged(anyObject());
		prop.addPropertyChangeListener(changeMock);
		pce = null;
	}
	
	public void propertyChangeEventAsserts( String msg, ChangeCode stat, Object src, Object ov, Object nv){
		assertNotNull( pce );
		assertEquals( msg, pce.getMessage() );
		assertEquals( stat, pce.getStatus() );
		assertEquals( src, pce.getSource() );
		assertEquals( ov, pce.getOldValue() );
		assertEquals( nv, pce.getNewValue() );
	}

	@Test
	public void textComparators(){
		Property greenSt = mock(Property.class);
		when(greenSt.getName()).thenReturn("jake");
		when(greenSt.getPosition()).thenReturn(0);
		
		Property springfieldAve = mock(Property.class);
		when(springfieldAve.getName()).thenReturn("trent");
		when(springfieldAve.getPosition()).thenReturn(1);
		
		Property chalmers = mock(Property.class);
		when(chalmers.getName()).thenReturn("drew");
		when(chalmers.getPosition()).thenReturn(2);
		
		ArrayList<Property> players = new ArrayList<>();
		
		players.add(greenSt);
		players.add(chalmers);
		players.add(springfieldAve);
		testComparatorByName( players, chalmers, greenSt, springfieldAve );
		testComparatorByPosition( players, greenSt, springfieldAve, chalmers );
	}
	
	public void testComparatorByPosition( ArrayList<Property> props, Property p1, Property p2, Property p3 ){
		props.sort(Property.POSITION_ORDER);
		
		assertEquals(p1, props.get(0));
		assertEquals(p2, props.get(1));
		assertEquals(p3, props.get(2));
	}
	
	public void testComparatorByName( ArrayList<Property> props, Property p1, Property p2, Property p3 ){
		props.sort(Property.NAME_ORDER);
		
		assertEquals(p1, props.get(0));
		assertEquals(p2, props.get(1));
		assertEquals(p3, props.get(2));
	}
	
	@Test
	public void testIncGrade() {
		assertEquals(0, prop.getGrade());
		prop.incGrade(2);
		verify(changeMock).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "grade increased", ChangeCode.GRADE, prop, 0, 2 );
		assertEquals(2, prop.getGrade());
		
		prop.incGrade(0);
		verify(changeMock, times(2)).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "grade increased", ChangeCode.GRADE, prop, 2, 2 );
		assertEquals(2, prop.getGrade());
		
		prop.incGrade(-2);
		verify(changeMock, times(3)).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "grade increased", ChangeCode.GRADE, prop, 2, 0 );
		assertEquals(0, prop.getGrade());
	}

	@Test
	public void testDecGrade() {
		assertEquals(0, prop.getGrade());
		prop.decGrade(2);
		verify(changeMock).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "grade decreased", ChangeCode.GRADE, prop, 0, -2 );
		assertEquals(-2, prop.getGrade());
		
		prop.decGrade(0);
		verify(changeMock, times(2)).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "grade decreased", ChangeCode.GRADE, prop, -2, -2 );
		assertEquals(-2, prop.getGrade());
		
		prop.decGrade(-2);
		verify(changeMock, times(3)).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "grade decreased", ChangeCode.GRADE, prop, -2, 0 );
		assertEquals(0, prop.getGrade());
	}

	@Test
	public void testSetGrade() {
		assertEquals(0, prop.getGrade());
		prop.setGrade(2);
		verify(changeMock).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "grade changed", ChangeCode.GRADE, prop, 0, 2 );
		assertEquals(2, prop.getGrade());
		
		assertEquals(2, prop.getGrade());
		prop.setGrade(2);
		verify(changeMock).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "grade changed", ChangeCode.GRADE, prop, 0, 2 );
		assertEquals(2, prop.getGrade());
		
		assertEquals(2, prop.getGrade());
		prop.setGrade(-2);
		verify(changeMock, times(2)).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "grade changed", ChangeCode.GRADE, prop, 2, -2 );
		assertEquals(-2, prop.getGrade());
		
	}

	@Test
	public void testSetMortgaged() {
		assertFalse(prop.isMortgaged());
		prop.setMortgaged(true);
		verify(changeMock).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "mortgaged changed", ChangeCode.MORTGAGE, prop, false, true );
		assertTrue(prop.isMortgaged());
		
		prop.setMortgaged(true);
		verify(changeMock).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "mortgaged changed", ChangeCode.MORTGAGE, prop, false, true );
		assertTrue(prop.isMortgaged());
		
		prop.setMortgaged(false);
		verify(changeMock, times(2)).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "mortgaged changed", ChangeCode.MORTGAGE, prop, true, false );
		assertFalse(prop.isMortgaged());
	}

	@Test
	public void testSetOwner() {
		assertEquals("", prop.getOwner());
		prop.setOwner("bart");
		verify(changeMock).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "owner changed", ChangeCode.OWNER, prop, "", "bart" );
		assertEquals("bart",prop.getOwner());
		
		prop.setOwner("bart");
		verify(changeMock).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "owner changed", ChangeCode.OWNER, prop, "", "bart" );
		assertEquals("bart",prop.getOwner());
		
		prop.setOwner("bort");
		verify(changeMock, times(2)).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "owner changed", ChangeCode.OWNER, prop, "bart", "bort" );
		assertEquals("bort",prop.getOwner());
	}

	@Test
	public void testAddPropertyChangeListener() {
		PropertyChangeListener mocked = mock(PropertyChangeListener.class);
		prop.addPropertyChangeListener(mocked);
		verify(mocked, never()).propertyStateChanged(anyObject());
		prop.setOwner("a");
		verify(mocked).propertyStateChanged(anyObject());
		prop.addPropertyChangeListener(mocked);
		prop.setOwner("b");
		verify(mocked, times(2)).propertyStateChanged(anyObject());
	}

	@Test
	public void testRemovePropertyChangeListener() {
		
		PropertyChangeListener mocked = mock(PropertyChangeListener.class);
		prop.addPropertyChangeListener(mocked);
		verify(mocked, never()).propertyStateChanged(anyObject());
		prop.setOwner("a");
		verify(mocked).propertyStateChanged(anyObject());
		prop.removePropertyChangeListener(mocked);
		prop.setOwner("b");
		verify(mocked).propertyStateChanged(anyObject());
	}

}
