package monopoly7.event;

import java.util.EventListener;

public interface PropertyChangeListener extends EventListener {
	public void propertyStateChanged( PropertyChangeEvent pce );
}
