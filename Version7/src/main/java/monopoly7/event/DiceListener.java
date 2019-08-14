package monopoly7.event;

import java.util.EventListener;

public interface DiceListener extends EventListener {
	public void diceRolled( DiceRollEvent pce );
}
