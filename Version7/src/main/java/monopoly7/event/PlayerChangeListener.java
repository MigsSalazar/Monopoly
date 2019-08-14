package monopoly7.event;

public interface PlayerChangeListener extends EnvironmentChangeListener {
	public void playerStateChanged( PlayerChangeEvent pce );
}
