package edu.illinois.masalzr2.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.Serializable;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import edu.illinois.masalzr2.controllers.Environment;
import edu.illinois.masalzr2.notices.AbstractNotice;
import edu.illinois.masalzr2.notices.ListEvent;
import edu.illinois.masalzr2.notices.ListListener;
import edu.illinois.masalzr2.notices.implentations.HomeMenuNotice;
import edu.illinois.masalzr2.notices.implentations.JailNotice;
import lombok.Getter;
import lombok.Setter;

/**
 * Controller class for the bottom panel of the main game frame. Manages the notice list,
 * the behavior when a chain of events ends, and utilizes the ListListener interface to
 * provide Notices support to end when they need to.
 */
public class Notices implements ListListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LinkedList<AbstractNotice> noticeList;
	private JPanel noticePanel;
	private AbstractNotice currNotice;
	private Environment gameVars;
	private JPanel components;
	private JLabel notification;
	@Getter @Setter private boolean gameOver = false;
	
	/**
	 * Initializes members and defines basic elements
	 * @param gv The Environment object that contains the current game
	 */
	public Notices(Environment gv){
		gameVars = gv;
		noticeList = new LinkedList<AbstractNotice>();
		noticePanel = new JPanel();
		noticePanel.setBorder(defineBorder());
		noticePanel.setVisible(true);
		currNotice = new HomeMenuNotice(this, gameVars);
		noticeList.add(currNotice);
		noticePanel = defineNoticePanel();
		paintNotice(currNotice);
		
	}
	
	/**
	 * Returns the JPanel controlled by this class
	 * @return The Notices controlled JPanel (I feel like I'm writting a half-assed essay with these descriptions)
	 */
	public JPanel getNoticePanel(){
		return noticePanel;
	}
	
	/**
	 * Builds a custom border for the JPanel. Meant to look like the border was etched into the frame surrounding the panel
	 * @return a compound border with a surrounding empty border
	 */
	private Border defineBorder(){
		Border bottomSpace = BorderFactory.createEmptyBorder(0, 10, 10, 10);
		Border up = BorderFactory.createRaisedBevelBorder();
		Border down = BorderFactory.createLoweredBevelBorder();
		Border bevel = BorderFactory.createCompoundBorder(up, down);
		Border temp = BorderFactory.createCompoundBorder(bottomSpace, bevel);
		return temp;
	}
	
	/**
	 * Meant to apply any updates to the text may have happened within
	 * the currently displayed notice. Calls for the panel to repaint itself
	 */
	private void softPaintNotice(){
		notification.setText(currNotice.getText());
		noticePanel.repaint();
	}
	
	/**
	 * Removes all components and replaces them with the components of
	 * the current notice. Does not cycle through the notices, only paints
	 * the passed notice
	 * @param an
	 */
	private void paintNotice(AbstractNotice an){
		
		components.removeAll();
		for(JComponent c : an.getActions()){
			components.add(c);
		}
		notification.setText(an.getText());
		noticePanel.repaint();
	}
	
	/**
	 * Defines the format and layout of the jpanel without anticipating
	 * any components yet
	 * @return The build JPanel. The return value of this method is not in any way linked to the JPanel Notices controlls
	 */
	private JPanel defineNoticePanel(){
		JPanel retval = new JPanel();
		retval.setLayout(new BorderLayout());
		components = new JPanel();
		notification = new JLabel();
		notification.setHorizontalAlignment(JLabel.CENTER);
		notification.setVerticalAlignment(JLabel.CENTER);
		retval.add(components, BorderLayout.SOUTH);
		retval.add(notification, BorderLayout.NORTH);
		retval.setPreferredSize(new Dimension(700,70));
		return retval;
	}
	
	/**
	 * Removes all notices within the list ignoring any and all chains of events
	 */
	public void flushNotices(){
		noticeList.removeAll(noticeList);
	}
	
	/**
	 * A push request made by a ListEvent to include a new AbstractNotice onto the chain of events
	 */
	@Override
	public void pushMe(ListEvent le) {
		Object src = le.getSource();
		if(src instanceof AbstractNotice){
			((AbstractNotice)src).setListListener(this);
			noticeList.add((AbstractNotice)src);
		}
		
	}
	
	/**
	 * A pop request, only pops the first element in the list and only pops it
	 * if the AbstractNotice stored in the ListEvent is the first in the list.
	 * This prevents mid chain pops and events popping anything but themselves.
	 * If the chain of events is at its end, Notices will generate it's own
	 * AbstractNotice picking from one of three choices: the main menu, the
	 * in jail menu, and the game over notice. 
	 */
	@Override
	public void popMe(ListEvent le) {
		Object src = le.getSource();
		if(src instanceof AbstractNotice){
			if(src.equals(noticeList.getFirst())){
				noticeList.pop();
			}
			//noticeList.pop();
		}
		if(noticeList.size() == 0){
			if(!gameVars.isInJail(gameVars.getCurrentPlayer()) && !gameOver ) {
				currNotice = new HomeMenuNotice(this, gameVars);
			}else if( gameVars.isInJail(gameVars.getCurrentPlayer()) && !gameOver ){
				currNotice = new JailNotice(this, gameVars);
			}else {
				currNotice = gameVars.getWinner();
			}
			
			noticeList.add(currNotice);
		}else{
			currNotice = noticeList.getFirst();
		}
		paintNotice(currNotice);
		
	}
	
	/**
	 * Perhaps a misnomer, The ListEvent tells the Notices class that the
	 * AbstractNotice source has an update that calls for a repaint.
	 */
	@Override
	public void pullMe(ListEvent le){
		Object src = le.getSource();
		if(src instanceof AbstractNotice){
			AbstractNotice an = (AbstractNotice)src;
			if(an.equals(noticeList.getFirst())){
				softPaintNotice();
			}
		}
	}
	
}
