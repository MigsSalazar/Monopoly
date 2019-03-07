package edu.illinois.masalzr2.gui;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * A JButton extension with additional focus on icon management.
 * @author Miguel Salazar
 *
 */
public class GraphicsButton extends JButton {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<ImageIcon> icons;
	
	/**
	 * Applies an empty icon to the button. Otherwise, Graphics buttons build like regular default JButtons
	 */
	public GraphicsButton(){
		super(new ImageIcon());
	}
	
	/**
	 * Includes the icon to the list of icons stored by this button and then calls to be repainted
	 * @param i The ImageIcon to be included in the button
	 */
	public void addIcon(ImageIcon i){
		if(icons == null){
			icons = new ArrayList<ImageIcon>();
		}
		icons.add(i);
		this.repaint();
	}

	/**
	 * Searches for all instances of the passed ImageIcon and then removes them from the button
	 * @param i The ImageIcon to be removed from the button
	 */
	public void removeIcon(ImageIcon i) {
		while(icons.contains(i)) {
			icons.remove(i);
		}
		this.repaint();
	}
	
	/**
	 * Clears out all icons in the list and then repaints the clear button
	 */
	public void wipeIcons(){
		if(icons == null){
			icons = new ArrayList<ImageIcon>();
		}
		if(icons.isEmpty()){
			return;
		}
		icons = new ArrayList<ImageIcon>();
		this.repaint();
	}
	
	/**
	 * Overrides the JButton {@link javax.swing.JButton#paintComponents(Graphics)} method
	 * to paint each icon individually, each layered on top of the last according to the
	 * order of the list
	 */
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		if(icons != null){
			for(ImageIcon i : icons){
				g.drawImage(i.getImage(), 0, 0, null);
			}
		}
	}

}
