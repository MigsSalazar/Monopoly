package monopoly7.models;

import monopoly7.event.PropertyChangeEvent;
import monopoly7.event.PropertyChangeListener;

public class LinkedGradeProperty extends Property implements PropertyChangeListener{

	private boolean validEvent( PropertyChangeEvent pce ){
		return (int)pce.getNewValue() != getGrade() &&
				pce.getSource() == PropertyChangeEvent.ChangeCode.GRADE &&
				pce.getSource().getClass().equals(this.getClass());
	}
	
	@Override
	public void propertyStateChanged(PropertyChangeEvent pce) {
		if( validEvent(pce) ){
			setGrade( (int)pce.getNewValue() );
		}
	}
	
}
