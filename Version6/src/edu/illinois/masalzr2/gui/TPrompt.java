package edu.illinois.masalzr2.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.util.ArrayList;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.illinois.masalzr2.controllers.Environment;
import edu.illinois.masalzr2.models.Player;
import edu.illinois.masalzr2.models.Property;

/**
 * A JDialog called by a Trade Manager
 * Purpose is to request an asset to trade for
 * and discover who owns said asset
 * @author Miguel Salazar
 *
 */
public class TPrompt extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6243394158913772220L;

	private Environment gameVars;
	
	private TradeManager tManager;
	
	private Player player1;
	private Map<String,Player> players;
	
	//private int currentI = 0;
	private Player currentP;
	private Object currentA;
	
	private JPanel topPanel = new JPanel(new BorderLayout());
	private JScrollPane plistScroller;
	private JList<String> pList;
	private DefaultListModel<String> pListModel;
	
	private JPanel cenPanel = new JPanel(new BorderLayout());
	private JScrollPane alistScroller;
	private JList<String> aList;
	private DefaultListModel<String> aListModel;
	
	private JPanel botPanel = new JPanel(new BorderLayout());
	private JButton okButton = new JButton("OK");
	private JButton question = new JButton("???");
	
	/**
	 * Only constructor. Requires a parent JFrame, a player 1 whom will make the offer,
	 * the fill list of active and inactive players, and the Trade Manager object that called this object
	 * @param gv		Environment object that defines the game
	 * @param pl		Player 1: player who will make an offer
	 * @param players2	Full ArrayList of all active and inactive players
	 * @param tm		Trade Manager object that called this object
	 */
	public TPrompt(Environment gv, Player pl, Map<String, Player> players2, TradeManager tm){
		super(gv.getFrame(), "Trade Manager - Step 1", true);
		gameVars = gv;
		tManager = tm;
		setLayout(new BorderLayout());
		
		player1 = pl;
		players = players2;
		
		formTopPanel();
		this.add(topPanel, BorderLayout.NORTH);
		//setVisible(true);
		//setBounds(100,100,200,200);
		formCenPanel();
		this.add(cenPanel, BorderLayout.CENTER);
		
		formBotPanel();
		this.add(botPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Defines and initializes all children components for the top
	 * portion of the JDialog component. Does NOT add children components
	 * to JDialog
	 */
	public void formTopPanel(){
		JLabel label1 = new JLabel("Please select a player to trade with");
		
		pListModel = findPlayers();
		pList = new JList<String>(pListModel);
		pList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		pList.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e){
				//System.out.println("DEBUG: Different item selected in players");
				@SuppressWarnings("unchecked")
				JList<String> jl = (JList<String>)e.getSource();
				//currentI = jl.getSelectedIndex();
				currentP = players.get(jl.getSelectedValue());
				findAssets(currentP);
				//System.out.println(aList.toString());
				aList.setModel(aListModel);
				//System.out.println("DEBUG: currentP: "+currentP);
				
			}
		});
		
		plistScroller = new JScrollPane(pList);
		plistScroller.setPreferredSize(new Dimension(50, 50));
		
		topPanel.add(label1, BorderLayout.NORTH);
		topPanel.add(plistScroller, BorderLayout.CENTER);
		
		
	}
	
	/**
	 * Defines and initializes all children components for the center
	 * portion of the JDialog component. Does NOT add children components
	 * to JDialog
	 */
	public void formCenPanel(){
		JLabel label2 = new JLabel("Please select an asset to trade for");
		
		aListModel = new DefaultListModel<String>();
		//aListModel.addElement("Select an Asset");
		
		aList = new JList<String>(aListModel);
		aList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		aList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		aList.setVisibleRowCount(-1);
		
		aList.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e){
				//System.out.println("DEBUG: Different item selected in assets");
				@SuppressWarnings("unchecked")
				JList<String> jl = (JList<String>)e.getSource();
				String a= jl.getModel().getElementAt(jl.getSelectedIndex());
				Object asset = null;
				if(a.equals("gojf")){
					asset = a;
				}else{
					asset = currentP.getProps().get(a);
				}
				currentA = asset;
				//System.out.println("DEBUG: currentA: "+currentA.toString() );
			}
		});
		
		alistScroller = new JScrollPane(aList);
		alistScroller.setPreferredSize(new Dimension(50, 50));
		
		cenPanel.add(label2, BorderLayout.NORTH);
		cenPanel.add(alistScroller, BorderLayout.CENTER);
		
	}
	
	/**
	 * Defines and initializes all children components for the bottom
	 * portion of the JDialog component. Does NOT add children components
	 * to JDialog
	 */
	public void formBotPanel(){
		okButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if( currentP != null && currentA!=null && aList.getSelectedIndex()!=-1){
					tManager.setPlayer1(player1);
					tManager.setPlayer2(currentP);
					tManager.setTradeAsset(currentA);
					tManager.setStep(1);
					imDone();
				}
			}
		});
		
		question.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String step1 = 
						"This is the first step in the"
					+ "\nTrade Manager. Here, you will"
					+ "\nselect the player you wish to"
					+ "\ntrade with and the asset you"
					+ "\ndesire. This manager assumes"
					+ "\nany negotiations have happened"
					+ "\nbetween any involved players."
					+ "\nThere's no way to check this"
					+ "\nand no way to undo it without"
					+ "\nperforming another trade. Press"
					+ "\nok to continue to the next step.";
				post(step1);
			}
		});
		
		botPanel.add(question, BorderLayout.WEST);
		botPanel.add(okButton, BorderLayout.EAST);
	}
	
	/**
	 * Discovers all eligible players in which to trade with
	 * @return	DefaultListModel of Strings containing the names of all eligible players
	 */
	public DefaultListModel<String> findPlayers(){
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		//listModel.addElement("");
		
		for(String s : players.keySet()){
			if( !players.get(s).equals(player1) && !players.get(s).isBankrupt()){
				listModel.addElement(players.get(s).getName());
			}
		}
		return listModel;
	}

	/**
	 * Calls a JOptionPane.showMessageDialog to present the passed String
	 * Used in ActionListeners that cannot call JOptionPanes directly
	 * @param send	String to present to the players
	 */
	public void post(String send){
		JOptionPane.showMessageDialog(gameVars.getFrame(), send);
	}
	
	/**
	 * Discovers any available assets owned by the selected player 2
	 * @param p	Player object that in which to discover assets
	 */
	public void findAssets(Player p){
		aListModel = new DefaultListModel<String>();
		
		for(int i=0; i<p.getJailCard(); i++){
			aListModel.addElement("Get out of Jail Free Card");
		}
		
		Map<String,Property> props = p.getProps();
		for(String prop : props.keySet()){
			aListModel.addElement(prop);
		}
	}
	
	/**
	 * Disposes itself
	 */
	public void imDone(){
		this.dispose();
	}
}
