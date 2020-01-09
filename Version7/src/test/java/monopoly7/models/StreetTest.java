package monopoly7.models;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import monopoly7.event.PropertyChangeEvent;
import monopoly7.event.PropertyChangeListener;
import monopoly7.event.PropertyChangeEvent.ChangeCode;

public class StreetTest {
	
	private MonopolizableProperty prop;
	private PropertyChangeListener changeMock;
	private PropertyChangeEvent pce = null;
	
	@Before
	public void setup(){
		int[] rents = {2,4,6,8,10,50,200};
		prop = new MonopolizableProperty( "mediteranean ave.", "", rents, 1, 100, 0, false, "0xFFFFFF", "purple", 50, 4, 2 );
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
	public void testIncGrade() {
		assertEquals(0, prop.getGrade());
		
		prop.incGrade(1);
		verify(changeMock).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "grade increased", ChangeCode.GRADE, prop, 0, 1 );
		assertEquals(1, prop.getGrade());
		
		prop.incGrade(0);
		verify(changeMock).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "grade increased", ChangeCode.GRADE, prop, 0, 1 );
		assertEquals(1, prop.getGrade());
		
		prop.incGrade(10);
		verify(changeMock).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "grade increased", ChangeCode.GRADE, prop, 0, 1 );
		assertEquals(1, prop.getGrade());
		
		prop.incGrade(-1);
		verify(changeMock).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "grade increased", ChangeCode.GRADE, prop, 0, 1 );
		assertEquals(1, prop.getGrade());
		
		prop.incGrade(1);
		verify(changeMock, times(2)).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "grade increased", ChangeCode.GRADE, prop, 1, 2 );
		assertEquals(2, prop.getGrade());
	}

	@Test
	public void testDecGrade() {
		assertEquals(0, prop.getGrade());
		
		prop.decGrade(1);
		verify(changeMock, never()).propertyStateChanged(anyObject());
		//propertyChangeEventAsserts( "grade increased", ChangeCode.GRADE, prop, 0, 1 );
		assertNull(pce);
		assertEquals(0, prop.getGrade());
		
		prop.incGrade(4);
		verify(changeMock).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "grade increased", ChangeCode.GRADE, prop, 0, 4 );
		assertEquals(4, prop.getGrade());
		
		prop.decGrade(1);
		verify(changeMock, times(2)).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "grade decreased", ChangeCode.GRADE, prop, 4, 3 );
		assertEquals(3, prop.getGrade());
		
		prop.decGrade(10);
		verify(changeMock, times(2)).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "grade decreased", ChangeCode.GRADE, prop, 4, 3 );
		assertEquals(3, prop.getGrade());
		
		
	}

	@Test
	public void testSetGrade() {
		assertEquals( 0, prop.getGrade() );
		
		prop.setGrade( 2 );
		verify(changeMock).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "grade changed", ChangeCode.GRADE, prop, 0, 2 );
		assertEquals( 2, prop.getGrade() );
		
		prop.setGrade( 0 );
		verify(changeMock, times(2)).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "grade changed", ChangeCode.GRADE, prop, 2, 0 );
		assertEquals( 0, prop.getGrade() );
		
		prop.setGrade( 20 );
		verify(changeMock, times(2)).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "grade changed", ChangeCode.GRADE, prop, 2, 0 );
		assertEquals( 0, prop.getGrade() );
		
		prop.setGrade( -2 );
		verify(changeMock, times(2)).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "grade changed", ChangeCode.GRADE, prop, 2, 0 );
		assertEquals( 0, prop.getGrade() );
		
		prop.setGrade( 4 );
		verify(changeMock, times(3)).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "grade changed", ChangeCode.GRADE, prop, 0, 4 );
		assertEquals( 4, prop.getGrade() );
		
		prop.setGrade( 0 );
		verify(changeMock, times(4)).propertyStateChanged(anyObject());
		propertyChangeEventAsserts( "grade changed", ChangeCode.GRADE, prop, 4, 0 );
		assertEquals( 0, prop.getGrade() );
		
	}

	@Test
	public void testGetLiquidWorth() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetters(){
		assertNotEquals("who the fuck","tests getters");
		assertNotEquals("that don't","contain any logic");
	}
	
}
