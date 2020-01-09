package monopoly7.models;

import java.util.Collection;

import monopoly7.event.PropertyChangeEvent;
import monopoly7.event.PropertyChangeListener;

public class LinkedGradeProperty extends Property implements PropertyChangeListener{
	
	public LinkedGradeProperty( String n, String o, int[] r, int p, int c, int g, boolean m, String h, Collection<LinkedGradeProperty> fellows ){
		super( n, o, r, p, c, g, m, h );
		for( LinkedGradeProperty f : fellows ){
			this.addPropertyChangeListener(f);
			f.addPropertyChangeListener(this);
		}
	}

	private boolean validEvent( PropertyChangeEvent pce ){
		return pce.getNewValue() instanceof Integer &&
				(int)pce.getNewValue() != this.getGrade() &&
				pce.getStatus() == PropertyChangeEvent.ChangeCode.GRADE &&
				pce.getSource() instanceof LinkedGradeProperty &&
				!pce.getSource().equals(this);
	}
	
	@Override
	public void propertyStateChanged(PropertyChangeEvent pce) {
		if( validEvent(pce) ){
			setGrade( (int)pce.getNewValue() );
		}
	}
	
}
