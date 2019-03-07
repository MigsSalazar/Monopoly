package edu.illinois.masalzr2.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.illinois.masalzr2.controllers.Environment;
import edu.illinois.masalzr2.models.Player;
import edu.illinois.masalzr2.models.Property;
import edu.illinois.masalzr2.models.Street;

/**
 * Called by the Trade Manager. This specialized JDialog manages the first player's offer for the second player's asset
 * @author Miguel Salazar
 *
 */
public class TOffer extends JDialog implements ListSelectionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7579917849501934789L;

	private TradeManager tManage;
	
	private Environment gameVars;
	
	private Player player1;
	
	private JPanel topPanel = new JPanel(new BorderLayout());
	private JPanel topCashPanel = new JPanel(new BorderLayout());
	private JLabel topFullCash;
	private JLabel topOfferCash;
	private JTextArea topTakeCash;
	//private JButton topCashOK = new JButton("OK");
	private int offeredCash=0;
	
	private DefaultListModel<String> aListModel;
	private JList<String> aList;
	private JScrollPane alistScroller;
	
	private JPanel cenPanel = new JPanel(new BorderLayout() );
	
	private JPanel buttonPanel = new JPanel(new BorderLayout());;
	private JButton include = new JButton("Add");
	private JButton exclude = new JButton("Remove");
	
	private JPanel botPanel = new JPanel(new BorderLayout());
	
	private JScrollPane olistScroller;
	private JList<String> oList;
	private DefaultListModel<String> oListModel = new DefaultListModel<String>();
	
	private JPanel botButtons  = new JPanel(new BorderLayout());
	private JButton next = new JButton("Next");
	private JButton question = new JButton("???");
	private JButton back = new JButton("Back");
	
	private int currentTI = 0;
	private String currentTS = "";
	
	private int currentBI = 0;
	private String currentBS = "";
	
	/**
	 * Default constructor. Requires the parent JFrame, a Player who
	 * wishes to make an offer, and the Trade Manager object that called this JDialog
	 * @param frame		parent JFrame used as a reference point for any resulting containers
	 * @param gv 		The Environment variable defining the game		
	 * @param p			Player who wishes to make an offer
	 * @param tm		Trade Manager object that called this JDialog
	 */
	public TOffer(JFrame frame, Environment gv, Player p, TradeManager tm){
		super(frame, "Trade Manager - Step 2", true);
		gameVars = gv;
		player1 = p;
		tManage = tm;
		setLayout( new BorderLayout() );
		
		formTopPanel();
		add(topPanel, BorderLayout.NORTH);
		
		formCenPanel();
		add(cenPanel, BorderLayout.CENTER);
		
		formBotPanel();
		add(botPanel, BorderLayout.SOUTH);
		
	}
	
	/**
	 * Declares and Initializes the top portion of the JDialog and all
	 * children components
	 * Does NOT add the component to the main JDialog
	 */
	public void formTopPanel(){
		JLabel label1 = new JLabel("Select the assets you wish to trade.\nOnly one at a time may be selected.");
		
		topFullCash = new JLabel("Your bank:     "+gameVars.getCurrency()+player1.getCash()+"   ");
		
		topOfferCash = new JLabel("Your cash offer:     "+gameVars.getCurrency());
		
		topTakeCash = new JTextArea(1,15);
		topTakeCash.setText("0");
		topTakeCash.setEditable(true);
		
		topTakeCash.getDocument().addDocumentListener(new DocumentListener(){

			public void changedUpdate(DocumentEvent de) {
				String updated = topTakeCash.getText();
				if( !updated.equals("") ){
					try{
						offeredCash = Integer.parseInt(topTakeCash.getText());
						if(offeredCash>=player1.getCash()){
							offeredCash=0;
							post("Invalid amount.\nThe money offered must\nbe less than your bank.");
							
						}else if(offeredCash<0){
							post("Having a negative number\nmeans your trade partner will\npay you cah back,\nMake sure this is okay.");
						}
						//System.out.println("DEBUG: offeredCash= "+offeredCash);
					}catch(Exception e){
						if(!topTakeCash.getText().equals("-")){
							String toSend = "Invalid input. Please\nenter an integer value";
							post(toSend);
							//topTakeCash.setText("0");
							offeredCash = 0;
						}
					}
				}else{
					offeredCash = 0;
				}
			}
			
			public void insertUpdate(DocumentEvent de) {
				String updated = topTakeCash.getText();
				if( !updated.equals("") ){
					try{
						offeredCash = Integer.parseInt(topTakeCash.getText());
						if(offeredCash>=player1.getCash()){
							offeredCash=0;
							post("Invalid amount.\nThe money offered must\nbe less than your bank.");
							
						}else if(offeredCash<0){
							post("Having a negative number\nmeans your trade partner will\npay you cah back,\nMake sure this is okay.");
						}
						//System.out.println("DEBUG: offeredCash= "+offeredCash);
					}catch(Exception e){
						String toSend = "Invalid input. Please\nenter an integer value";
						post(toSend);
						//topTakeCash.setText("0");
						offeredCash = 0;
					}
				}else{
					offeredCash = 0;
				}
			}

			public void removeUpdate(DocumentEvent de) {
				String updated = topTakeCash.getText();
				if( !updated.equals("") ){
					try{
						offeredCash = Integer.parseInt(topTakeCash.getText());
						if(offeredCash>=player1.getCash()){
							offeredCash=0;
							post("Invalid amount.\nThe money offered must\nbe less than your bank.");
							
						}else if(offeredCash<0){
							post("Having a negative number\nmeans your trade partner will\npay you cah back,\nMake sure this is okay.");
						}
						//System.out.println("DEBUG: offeredCash= "+offeredCash);
					}catch(Exception e){
						String toSend = "Invalid input. Please\nenter an integer value";
						post(toSend);
						//topTakeCash.setText("0");
						offeredCash = 0;
					}
				}else{
					offeredCash = 0;
				}
			}
			
		});
		
		/*
		topCashOK.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if( !topTakeCash.getText().equals("0") ){
					int cash = 0;
					try{
						cash = Integer.parseInt(topTakeCash.getText());
						if(cash>=player1.getBank()){
							post("Invalid amount!\nMust be less than your total cash");
						}else{
							offeredCash+=cash;
						}
					}catch(Exception ex){
						post("Invalid amount!\nMust be an integer value.");
					}
					
				}
			}
		});
		*/
		topCashPanel.add(topFullCash, BorderLayout.WEST);
		topCashPanel.add(topOfferCash, BorderLayout.CENTER);
		topCashPanel.add(topTakeCash, BorderLayout.EAST);
		//topCashPanel.add(topCashOK, BorderLayout.EAST);
		
		aListModel = findAssets();
		aList = new JList<String>(aListModel);
		aList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		aList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		aList.setVisibleRowCount(-1);
		
		
		aList.addListSelectionListener(this);
		
		aList.setSelectedIndex(0);
		
		alistScroller = new JScrollPane(aList);
		alistScroller.setPreferredSize(new Dimension(100, 150));
		
		topPanel.add(label1, BorderLayout.NORTH);
		topPanel.add(topCashPanel, BorderLayout.CENTER);
		topPanel.add(alistScroller, BorderLayout.SOUTH);
	}
	
	/**
	 * Declares and Initializes the center portion of the JDialog and all
	 * children components
	 * Does NOT add the component to the main JDialog
	 */
	public void formCenPanel(){
		
		include.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
			    if(currentTS.equals("") ) {
			    	return;
			    }
				exclude.setEnabled(true);
				
			    oListModel.addElement(currentTS);
			    oList.setModel(oListModel);
			    currentBI++;
			    oList.setSelectedIndex(currentBI);
		        oList.ensureIndexIsVisible(currentBI);
		        
		        aListModel.remove(currentTI);
		        
			    int size = aListModel.getSize();

			    if (size == 0) { //Nobody's left, disable firing.
			        include.setEnabled(false);

			    } else { //Select an index.
			    	include.setEnabled(true);
			        if (currentTI == aListModel.getSize()) {
			            //removed item in last position
			            currentTI--;
			        }
			        //System.out.println("DEBUG: currentTI= "+currentTI);
			        aList.setSelectedIndex(currentTI);
			        aList.ensureIndexIsVisible(currentTI);
			        
			        if(oList.getModel().getSize()==1){
				        currentBI = 0;
				        oList.setSelectedIndex(currentBI);
				        currentBS = oList.getSelectedValue();
			        }
			        
			    }
			}
		});
		
		exclude.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
			    
				include.setEnabled(true);
				
			    aListModel.addElement(currentBS);
			    aList.setModel(aListModel);
			    currentTI++;
			    aList.setSelectedIndex(currentTI);
		        aList.ensureIndexIsVisible(currentTI);
		        
		        oListModel.remove(currentBI);
		        
			    int size = oListModel.getSize();
			    
			    if (size == 0) { //Nobody's left, disable firing.
			        exclude.setEnabled(false);

			    } else { //Select an index.
			    	exclude.setEnabled(true);
			        if (currentBI == oListModel.getSize()) {
			            //removed item in last position
			            currentBI--;
			        }
			        
			        //System.out.println("DEBUG: currentBI= "+currentBI);
			        oList.setSelectedIndex(currentBI);
			        oList.ensureIndexIsVisible(currentBI);
			        
			        if(aList.getModel().getSize()==1){
				        currentTI = 0;
				        aList.setSelectedIndex(currentTI);
				        currentTS = aList.getSelectedValue();
			        }
			        
			    }
			}
		});
		
		exclude.setEnabled(false);
		
		buttonPanel.add(include, BorderLayout.EAST);
		buttonPanel.add(exclude, BorderLayout.WEST);
		
		
		JLabel label2 = new JLabel("These are your offered assets");
		
		
		cenPanel.add(buttonPanel, BorderLayout.CENTER);
		cenPanel.add(label2, BorderLayout.SOUTH);

	}
	
	/**
	 * Declares and Initializes the bottom portion of the JDialog and all
	 * children components
	 * Does NOT add the component to the main JDialog
	 */
	public void formBotPanel(){
		
		
		oList = new JList<String>(oListModel);
		oList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		oList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		oList.setVisibleRowCount(-1);
		
		oList.addListSelectionListener(this);
		
		olistScroller = new JScrollPane(oList);
		olistScroller.setPreferredSize(new Dimension(100, 150));


		next.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				tManage.setOfferedAsset(offeredList());
				tManage.setStep(2);
				imDoneToo();
			}
		});
		
		question.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String step2 = 
						"Here you may select the assets you"
					+ "\nwish to offer in trade for the asset"
					+ "\nyou selected in the first step. You"
					+ "\nmay only select one asset at a time"
					+ "\nand you must \"Add\" the asset for it to"
					+ "\nbe considered in the trade. If you"
					+ "\nwish to take away an asset from the"
					+ "\noffer, select it in the bottom list"
					+ "\nand press \"Remove.\" To add cash, simply"
					+ "\nchange the value in the text field"
					+ "\nnear the top. You must only enter"
					+ "\ninteger values. You will be told of"
					+ "\ninvalid values even though the text"
					+ "\nwon't change in the field. If the"
					+ "\nother player in the trade agreed to"
					+ "\npay cash back for the assets you"
					+ "\noffer, enter a negative number with"
					+ "\na \"-\" sign. Press the \"Next\" button to"
					+ "\ncontinue to the next step. Press the"
					+ "\n\"Back\" button to restart the previous"
					+ "\nstep. Closing this window will end"
					+ "\nthe manager without any changes to"
					+ "\nany player's assets. Nothing gained"
					+ "\nand nothing lost.";
				post(step2);
			}
		});
		
		back.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				tManage.setStep(0);
				imDoneToo();
			}
		});
		
		botButtons.add(next, BorderLayout.EAST);
		botButtons.add(question, BorderLayout.CENTER);
		botButtons.add(back, BorderLayout.WEST);
		
		botPanel.add(olistScroller, BorderLayout.CENTER);
		botPanel.add(botButtons, BorderLayout.SOUTH);
		
	}
	
	/**
	 * Searches for available assets from the passed player
	 * @return	a DefaultListModel of Strings containing all available assets
	 */
	public DefaultListModel<String> findAssets(){
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		
		
		for(int i=0; i<player1.getJailCard(); i++){
			listModel.addElement("Get out of Jail Free Card");
		}
		Map<String,Property> props = player1.getProps();
		for(String key : props.keySet()){
			if(props.get(key) instanceof Street){
				if(((Street)props.get(key)).getGrade()==0){
					listModel.addElement(key);
				}
			}else {
				listModel.addElement(key);
			}
			
		}
		
		return listModel;
	}
	
	/**
	 * Gathers all the selected assets offered and returns them as a List of Asset objects
	 * @return	ArrayList of Assets containing all offered assets
	 */
	public List<Object> offeredList(){
		List<Object> offers = new ArrayList<Object>();
		String a="";
		Object asset = null;
		offers.add(new Integer(offeredCash));
		for(int i=0; i<oList.getModel().getSize(); i++){
			a=oList.getModel().getElementAt(i);
			if(a.contains("cash")){
				asset = new Integer(0);
			}else if(a.equals("Get out of Jail Free Card")){
				asset = "gojf";
			}else{
				asset = player1.getProps().get(a);
			}
			offers.add( asset );
		}
		return offers;
	}
	
	/**
	 * Calls a JOptionPane.showMessageDialog to present the passed String
	 * Used in ActionListeners that cannot call JOptionPanes directly
	 * @param send	String to present to the players
	 */
	public void post(String send){
		JOptionPane.showMessageDialog(this, send);
	}
	
	/**
	 * Disposes itself
	 */
	public void imDoneToo(){
		this.dispose();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource().equals(aList)){
			currentTI = aList.getSelectedIndex();
			//System.out.println(""+jl.getSelectedIndex());
			currentTS = aList.getSelectedValue();
			//System.out.println(jl.getSelectedValue());
		}else if(e.getSource().equals(oList)){
			currentBI = oList.getSelectedIndex();
			currentBS = oList.getModel().getElementAt(oList.getSelectedIndex());
		
		}
	}
	
}
