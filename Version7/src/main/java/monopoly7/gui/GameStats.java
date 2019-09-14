package monopoly7.gui;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import monopoly7.models.Player;

public class GameStats extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4190388742704453028L;

	private List<Player> players;
	private List<ExtendedStatsPanel> extendeds = new ArrayList<ExtendedStatsPanel>();
	private List<StatsPanel> smalls = new ArrayList<StatsPanel>();
	private boolean extended;
	
	public GameStats( Map<String, Player> p, boolean e ){
		super( new GridLayout( 0, 1 ) );
		players = new ArrayList<Player>( p.values() );
		players.sort(Player.ID_ORDER);
		extended = e;
	}
	
	private void populateExtendeds(){
		for( Player p : players ){
			//extendeds.add(new ExtendedPanel(  ));
		}
	}
	
}
