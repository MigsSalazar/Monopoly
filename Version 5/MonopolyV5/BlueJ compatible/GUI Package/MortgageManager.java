 

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;

import javax.swing.*;


public class MortgageManager extends JDialog implements ActionListener{

	private Runner gameVars;
	private Player player;
	private Map<String,Property> props;
	
	private JPanel topPanel;
	private JButton confirm;
	private JComboBox<String> propertyNames;
	private JButton question;
	
	private JPanel midPanel;
	private JLabel curStatusLabel;
	private JLabel butStatusLabel;
	
	private JPanel botPanel;
	private JLabel curStatus;
	private JButton setMort;
	private JButton setUnmort;
	
	private Property curProp;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8977446161365190615L;

	public MortgageManager(Runner gv, Player pl) {
		super(gv.getFrame(), "Mortgage Manager", true);
		gameVars = gv;
		player = pl;
		props = player.getProps();
	}
	
	public void beginManager(){
		if(developTop() && developMid() && developBot()){
			BorderLayout bl = new BorderLayout();
			bl.setHgap(5);
			bl.setVgap(5);
			this.setLayout(bl);
			this.add(topPanel, BorderLayout.NORTH);
			this.add(midPanel, BorderLayout.CENTER);
			this.add(botPanel, BorderLayout.SOUTH);
			this.setIconImage(gameVars.getFrame().getTitleIcon());
			setBounds(100, 100, 350, 140);
			setResizable(false);
			setVisible(true);
		}else{
			JOptionPane.showMessageDialog(gameVars.getFrame(), "Manager can't be launched\nfor the current player.");
		}
	}
	
	private boolean developTop(){
		try{
			topPanel = new JPanel(new BorderLayout());
			confirm = new JButton("Ok");
			confirm.addActionListener(this);
			
			Set<String> pNames = props.keySet();
			if(pNames.size() == 0){
				return false;
			}
			propertyNames = new JComboBox<String>( pNames.toArray(new String[pNames.size()]) );
			propertyNames.addActionListener(this);
			
			question = new JButton("?");
			question.addActionListener(this);
			
			topPanel.add(confirm, BorderLayout.WEST);
			topPanel.add(propertyNames, BorderLayout.CENTER);
			topPanel.add(question, BorderLayout.EAST);
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	private boolean developMid(){
		try{
			
			BorderLayout bl = new BorderLayout();
			bl.setHgap(5);
			bl.setVgap(5);
			
			midPanel = new JPanel(bl);
			
			curStatusLabel = new JLabel("Current Status:");
			curStatusLabel.setHorizontalAlignment(JLabel.CENTER);
			
			butStatusLabel = new JLabel("Set new status:");
			butStatusLabel.setHorizontalAlignment(JLabel.CENTER);
			
			midPanel.add(curStatusLabel, BorderLayout.WEST);
			midPanel.add(butStatusLabel, BorderLayout.CENTER);
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	private boolean developBot(){
		try{
			String curPropName = (String)propertyNames.getSelectedItem();
			curProp = props.get(curPropName);
			boolean isMort = curProp.isMortgaged();
			
			botPanel = new JPanel(new BorderLayout());
			
			curStatus = new JLabel(isMort ? "Mortgaged" : "Not Mortgaged");
			curStatus.setPreferredSize(new Dimension(100, 50));
			curStatus.setHorizontalAlignment(JLabel.CENTER);
			
			setMort = new JButton("Take Mortgage");
			setMort.setEnabled( !isMort );
			setMort.setToolTipText(""+curProp.getMortgageValue() );
			setMort.addActionListener(this);
			//setMort.setPreferredSize(new Dimension(100, 50));
			
			setUnmort = new JButton("Pay Mortgage");
			setUnmort.setEnabled(isMort);
			setUnmort.setToolTipText("" + (curProp.getMortgageValue() + (int)(curProp.getMortgageValue()*0.1) ));
			setUnmort.addActionListener(this);
			setUnmort.setPreferredSize(new Dimension(125, 50));
			
			botPanel.add(curStatus, BorderLayout.WEST);
			botPanel.add(setMort, BorderLayout.CENTER);
			botPanel.add(setUnmort, BorderLayout.EAST);
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(confirm)){
			if(curProp.isMortgaged()){
				if( !setUnmort.isEnabled() ){
					JOptionPane.showMessageDialog(this, "You have payed of the mortgage on:"+curProp.getName());
					curProp.setMortgage(false);
					int payout = (curProp.getMortgageValue() + (int)(curProp.getMortgageValue() * 0.1) );
					player.subCash(payout);
				}
			}else if(!curProp.isMortgaged()){
				if( !setMort.isEnabled() ){
					JOptionPane.showMessageDialog(this, "You have taken out a mortgage on:\n"+curProp.getName());
					curProp.setMortgage(true);
					player.addCash(curProp.getPrice()/2);
				}
			}
			curStatus.setText(curProp.isMortgaged() ? "Mortgaged" : "Not Mortgaged");
			gameVars.getFrame().getGameStats().updatePlayers();
		}else if(e.getSource().equals(propertyNames)){
			curProp = props.get((String)propertyNames.getSelectedItem());
			
			boolean isMort = curProp.isMortgaged();
			setMort.setEnabled(!isMort);
			setMort.setToolTipText(""+curProp.getMortgageValue());
			setUnmort.setEnabled(isMort);
			setUnmort.setToolTipText("" + (curProp.getMortgageValue() + (int)(curProp.getMortgageValue() * 0.1) ));
			curStatus.setText(curProp.isMortgaged() ? "Mortgaged" : "Not Mortgaged");
		}else if(e.getSource().equals(question)){
			JOptionPane.showMessageDialog(this, "MORTGAGE MANAGER"
											+"\n====================="
											+"\nHere you can select the property you want"
											+"\nto take out a mortgage on or pay off an"
											+"\nexisting mortgage to the bank. Taking a"
											+"\nwill net you half the property's original"
											+"\nvalue. Paying it back will cost you the"
											+"\nmortgage price plus 10% intrest. To confirm"
											+"\nchanges, press the ok button. Net earned and"
											+"\nnet payment can be gotten for a gotten for"
											+"\nthe current property by hovering over the"
											+"\ncorresponding button. Changing to another"
											+"\nproperty without confirming WILL SCRAP ANY"
											+"\nCHANGES MADE. ALL TRANSACTIONS ARE FINAL."
											+"\nTo leave, simply x out of the manager.");
		}else if(e.getSource().equals(setMort)){
			setMort.setEnabled(false);
			setUnmort.setEnabled(true);
		}else if(e.getSource().equals(setUnmort)){
			setMort.setEnabled(true);
			setUnmort.setEnabled(false);
		}
		
	}
	
}
