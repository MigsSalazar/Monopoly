package monopoly7.event;

import java.util.EventObject;

public class DiceRollEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3412232362529884624L;
	
	public DiceRollEvent(Object source) {
		super(source);
	}

}
