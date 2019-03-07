package edu.illinois.masalzr2.gui;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;

import edu.illinois.masalzr2.models.Player;

/**
 * A wrapping controller class for StatsTiles
 * Contains all the stats tiles of the game
 * but that's about it. Micromanaging the details
 * is left to the StatsTiles
 * @see edu.illinois.masalzr2.gui.StatsTile
 * @author Miguel Salazar
 *
 */
public class Scoreboard {
	
	private JPanel scores;
	
	private StatsTile[] tiles;
	
	public Scoreboard(ImageIcon[] playerIcons, List<Player> players, String currency) {
		
		rebuildScoreboard(playerIcons, players, currency);
		
	}
	
	/**
	 * Returns the JPanel built by this class.
	 * @return The JPanel containing the StatsTiles
	 */
	public JPanel getScoreboard() {
		return scores;
	}
	
	/**
	 * Accepts the images, players, and currency symbol needed to build each stats tile individually.
	 * Players are all assigned a stats tile for their change listeners as well
	 * @param playerIcons 	ImageIcons associated by the player's game piece on the board
	 * @param players 		The player objects used by the stats tiles
	 * @param currency		The currency symbol... like a $ or €... That string gets passed around a lot... cause... you know... Monopoly is a game about... monopolies... and money... so yeah... it needs a lot of money
	 */
	public void rebuildScoreboard( ImageIcon[] playerIcons, List<Player> players, String currency ) {
		scores = new JPanel( new GridLayout(2,4) );
		
		tiles = new StatsTile[players.size()];
		
		for(int i=0; i<tiles.length; i++) {
			tiles[i] = new StatsTile( playerIcons[i], players.get(i), currency );
			List<ChangeListener> listeners = players.get(i).getListeners();
			int j = 0;
			while(j<listeners.size()) {
				if(listeners.get(j) instanceof StatsTile) {
					players.get(i).removeListener(listeners.get(j));
				}else {
					j++;
				}
			}
			players.get(i).addChangeListener(tiles[i]);
			scores.add( tiles[i] );
		}
	}
	

}
