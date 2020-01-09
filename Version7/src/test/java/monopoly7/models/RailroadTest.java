package monopoly7.models;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import monopoly7.event.PropertyChangeEvent;
import monopoly7.event.PropertyChangeEvent.ChangeCode;

public class RailroadTest {

	@Test
	public void testPropertyStateChanged() {
		LinkedGradeProperty readingRail = mock(LinkedGradeProperty.class);
		LinkedGradeProperty bandoRail = mock(LinkedGradeProperty.class);
		LinkedGradeProperty pennRail = mock(LinkedGradeProperty.class);
		
		ArrayList<LinkedGradeProperty> rails = new ArrayList<>(4);
		ArrayList<PropertyChangeEvent> pces = new ArrayList<>(4);
		for( int i=0; i<3; i++ ){
			pces.add(null);
		}
		rails.add(readingRail);
		rails.add(bandoRail);
		rails.add(pennRail);

		for( int i=0; i<3; i++ ){
			final int tempi = i;
			doAnswer( (InvocationOnMock iom)
				-> {
					for( Object o : iom.getArguments() ){
						if( o instanceof PropertyChangeEvent )
							pces.set(tempi, (PropertyChangeEvent)o);
					}
					return null;
				}
			).when(rails.get(i)).propertyStateChanged(anyObject());
			doNothing().when(rails.get(i)).addPropertyChangeListener(anyObject());
			doCallRealMethod()
				.when(rails.get(i))
				.decGrade(anyInt());
		}
		int[] rents = {25,50,100,200};
		LinkedGradeProperty shortLine = new LinkedGradeProperty("short line", "", rents, 35, 200, 0, false, "0xFFFFFF", rails );
		
		rails.forEach( (LinkedGradeProperty lpg) -> {
			verify(lpg).addPropertyChangeListener(anyObject());
			verify(lpg, never()).propertyStateChanged(anyObject());
		});
		assertEquals( 0, shortLine.getGrade() );
		
		shortLine.incGrade(1);
		
		rails.forEach( (LinkedGradeProperty lpg) -> {
			verify(lpg).addPropertyChangeListener(anyObject());
			verify(lpg).propertyStateChanged(anyObject());
		});
		
		assertEquals(1, shortLine.getGrade());
		
		shortLine.decGrade(1);
		
		rails.forEach( (LinkedGradeProperty lpg) -> {
			verify(lpg).addPropertyChangeListener(anyObject());
			verify(lpg, times(2)).propertyStateChanged(anyObject());
		});
		
		PropertyChangeEvent temp = new PropertyChangeEvent( readingRail, "grade changed", ChangeCode.GRADE, 0, 3 );
		
		shortLine.propertyStateChanged(temp);
		
		rails.forEach( (LinkedGradeProperty lpg) -> {
			verify(lpg).addPropertyChangeListener(anyObject());
			verify(lpg, times(3)).propertyStateChanged(anyObject());
		});
		
	}

}
