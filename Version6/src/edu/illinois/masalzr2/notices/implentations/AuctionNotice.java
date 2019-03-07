package edu.illinois.masalzr2.notices.implentations;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.illinois.masalzr2.models.Player;
import edu.illinois.masalzr2.models.Property;
import edu.illinois.masalzr2.notices.AbstractNotice;
import edu.illinois.masalzr2.notices.ListEvent;
import edu.illinois.masalzr2.notices.ListListener;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class AuctionNotice extends AbstractNotice implements ChangeListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Property prop;
	private int turn = 0;
	private int bid = 0;
	private int highestBidder = -1;
	private List<String> playerNames;
	private Map<String, Player> players;
	private String currency;
	private int money;
	private SpinnerNumberModel model;
	
	public AuctionNotice(ListListener ppl, Map<String,Player> pl, Property pr, String c){
		super(ppl);
		log.info("AuctionNotice: Beginning short: Calling Sub Constructor");
		subConstructor(pl, pr, c);
		log.info("AuctionNotice: Beginning short: Defining Actions");
		defineActions();
		bidInput(model);
	}
	
	public AuctionNotice(ListListener ppl, Map<String,Player> pl, Property pr, int t, int b, int hb, String c) {
		super(ppl);
		log.info("AuctionNotice: Beginning long: Calling Sub Constructor");
		subConstructor(pl, pr, c);
		log.info("AuctionNotice: Beginning long: setting basics");
		turn = t;
		bid = b;
		highestBidder = hb;
	}

	private void subConstructor(Map<String, Player> pl, Property pr, String c) {
		log.info("AuctionNotice: Sub Constructor: Setting basics");
		prop = pr;
		players = pl;
		List<Player> tempNames = new ArrayList<Player>(pl.values());
		tempNames.sort(Player.ID_ORDER);
		playerNames = new ArrayList<String>();
		
		for(Player p : tempNames) {
			if( !p.isBankrupt() ) {
				playerNames.add(p.getName());
			}
		}
		
		currency = c;
		text = "<html>Current bid on "+prop.getName() + (highestBidder == -1 ? " has yet to be set." : (" is " + playerNames.get(highestBidder) + " with a bid of "+currency+bid) )
				+"<br>"+playerNames.get(turn)+", will you raise or pass? Entering 0 means you pass."
				+"<br>Your offer:</html>";
		money = 0;
		for(Player p : tempNames) {
			if(p.getCash() > money) {
				money = p.getCash();
			}
		}
	}

	private void buttonPush(ActionEvent e) {
		log.info("AuctionNotice: Button Push: Called");
		//bidInput((JTextField)actions[0]);]
		int value = (Integer)model.getValue();
		if(e.getSource().equals(actions[1])){
			log.info("AuctionNotice: Button Push: Raised");
			if( value == 0 
				|| value == bid){
				log.info("AuctionNotice: Button Push: Bid not raised. Calling Pass insteead");
				((JButton)actions[2]).doClick();
			}else{
				log.info("AuctionNotice: Button Push: Taking new bid");
				bid = value;
				highestBidder = turn;
				turn = (turn+1)%playerNames.size();
				model.setValue(bid+1);
				setText();
				
				listener.pullMe(new ListEvent(this));
			
			}
		}else if(e.getSource().equals(actions[2])){
			log.info("AuctionNotice: Button Push: Passed. Moving on");
			turn = (turn+1)%playerNames.size();
			model.setValue(bid+1);
			setText();
			
			listener.pullMe(new ListEvent(this));
			
		}
		
	}
	
	private void setText(){
		if(actions != null){
			bidInput(model);
		}
		
		String person;
		if(highestBidder > -1){
			person = " by " + (String) playerNames.get(highestBidder);
		}else{
			person = "";
		}
		text = "<html>Current bid on "+prop.getName()+" is from " + person +" with a bid of "+currency+bid
				+"<br>"+playerNames.get(turn)+", will you raise or pass? Entering 0 means you pass."
				+"<br>Your offer:</html>";
	}
	
	private void bidInput(SpinnerNumberModel useme) {
		try{
			int offer = (Integer)useme.getValue();
			if(offer >=  players.get(playerNames.get(turn)).getCash() || (offer<=bid && bid !=0) || players.get(playerNames.get(turn)).getCash()==1){
				((JButton)actions[1]).setEnabled(false);
			}else if(offer > bid || offer==0){
				((JButton)actions[1]).setEnabled(true);
			}
		}catch(NumberFormatException nfe){
			model.setValue(0);
			//((JTextField)actions[0]).setText(""+(bid+1));
		}
	}
	
	private String whoWon(){
		return "<html>"+playerNames.get(highestBidder)+" has won the auction and bought<br>"+prop.getName()+" for "+currency+bid+"</html>";
	}
	
	private boolean fullCircle(){
		//System.out.println("highest cash = "+money);
		if(money == 1) {
			highestBidder = turn;
			return true;
		}
		
		if( (bid == 0 || highestBidder == -1)){
			return false;
		}
		return highestBidder==turn;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(actions[0])){
			bidInput(model);
		}else if(e.getSource().equals(actions[1]) || e.getSource().equals(actions[2])){
			buttonPush(e);
			if(fullCircle()){
				//BankPropertyActions.sellUnownedProperty(players.get(playerNames[highestBidder].toString()), prop, bid);
				MessageNotice an = new PlayerPropertyNotice(whoWon(), listener, players.get(playerNames.get(highestBidder)), prop, bid);
				noticePushPop(an);
			}
		}
		
	}
	
	@Override
	protected void defineActions() {
		actions = new JComponent[3];
		
		model = new SpinnerNumberModel();
		model.setMinimum(0);
		model.addChangeListener(this);
		
		actions[0] = new JSpinner(model);
		actions[0].setPreferredSize(new Dimension(75, 30));
		((JSpinner)actions[0]).addChangeListener(this);
		
		actions[1] = new JButton("Raise");
		((JButton)actions[1]).addActionListener(this);
		actions[2] = new JButton("Pass");
		((JButton)actions[2]).addActionListener(this);
		
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		bidInput(model);
	}

}
