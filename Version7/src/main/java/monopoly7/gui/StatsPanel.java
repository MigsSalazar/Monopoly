package monopoly7.gui;

import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import monopoly7.event.PlayerChangeEvent;
import monopoly7.event.PlayerChangeListener;
import monopoly7.models.Environment;
import monopoly7.models.Player;

public class StatsPanel extends JPanel implements PlayerChangeListener {

	/**
	 * 
	 */

	private static final long serialVersionUID = 6160162439567376233L;
	private JLabel name;
	protected ImageIcon icon;
	private JLabel bails;
	private JLabel cash;
	//private PropPanel propPanel;
	private Player following;
	private String currency = Environment.mostRecentGame.getCurrency();
	
	public StatsPanel( Player p, ImageIcon i ){
		super( new GridLayout(0, 1) );
		following = p;
		name = new JLabel( p.getName() );
		icon = i;
		cash = new JLabel( cashString(p.getCash()) );
		
		this.add( new JLabel(icon) );
		this.add(name);
		this.add(cash);
	}
	
	public StatsPanel createExtendedPanel(){
		return new ExtendedStatsPanel( following, icon );
	}
	
	
	protected String cashString( int in ){
		return currency + in;
	}

	@Override
	public void playerStateChanged(PlayerChangeEvent pce) {
		switch( pce.getStatus() ){
			case CASH:
				cash.setText(cashString((int)pce.getNewValue()));
				break;
			default:
		}
	}

}
