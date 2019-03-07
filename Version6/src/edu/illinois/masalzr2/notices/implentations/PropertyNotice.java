package edu.illinois.masalzr2.notices.implentations;

import java.awt.event.ActionEvent;

import javax.swing.JButton;

import edu.illinois.masalzr2.controllers.Environment;
import edu.illinois.masalzr2.models.Player;
import edu.illinois.masalzr2.models.Property;
import edu.illinois.masalzr2.notices.AbstractNotice;
import edu.illinois.masalzr2.notices.ListEvent;
import edu.illinois.masalzr2.notices.ListListener;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class PropertyNotice extends AbstractNotice {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Player player;
	private Property prop;
	private Environment gameVars;
	private String currency;
	
	public PropertyNotice(ListListener ppl, Environment gv, Player pl, Property pr) {
		super(ppl);
		log.info("player="
		+pl.getName()
		+" property="+pr.getName());
		player = pl;
		prop = pr;
		gameVars = gv;
		
		currency = gameVars.getCurrency();
		
		if(prop.getOwner() == null || prop.getOwner().equals("")) {
			log.info("Property is unowned");
			text = "<html>"+prop.getName() + " is unowned. Asking price: " + currency + prop.getPrice() +"<br/>What would you like to do?</html>";
		}else if( !prop.getOwner().equals(pl.getName()) ){
			log.info("Property is owned and doesn't belong to the player");
			text = "<html>"+pl.getName() + " has landed on " +prop.getOwner() + "'s property of " + prop.getName() + "<br/>Rent: " + currency + prop.getRent()+"</html>";
		}else {
			log.info("Property is owned by the player");
			text = "<html>You own this property.<br/>Nothing to be done</html>";
		}
		
		defineActions();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		log.info("Button pressed");
		if(prop.getOwner() == null || prop.getOwner().equals("")){
			log.info("Property is unowned. Buy or Auction?");
			if(e.getSource().equals(actions[0])){
				//BankPropertyActions.sellUnownedProperty(play, prop);
				log.info("Property bought");
				String texter = "You bought "+prop.getName()+"!";
				AbstractNotice an = new PlayerPropertyNotice(texter, listener, player, prop);
				noticePushPop(an);
			}else{
				log.info("Property auctioned");
				AbstractNotice an = new AuctionNotice(listener, gameVars.getPlayers(), prop, currency);
				noticePushPop(an);
			}
		}else if(!prop.getOwner().equals(player.getName())){
			//BankPropertyActions.rentOwnedProperty(play, prop);
			log.info("Property is owned but not by the player");
			log.info("PropOwner="+prop.getOwner()+" returned value: "+gameVars.getPlayers().get(prop.getOwner()));
			Player p2 = gameVars.getPlayers().get(prop.getOwner() );
			log.info("Paying {} {} for landing on {}", prop.getOwner(), prop.getRent(), prop.getName() );
			String outText = "You payed "+prop.getOwner()+" "+ currency + prop.getRent()+" for landing on "+prop.getName()+"!";
			
			AbstractNotice an = new PlayerPlayerNotice(outText, listener, player, p2, (-1)*prop.getRent());
			//AbstractNotice a = new MessageEvent(parent, "You payed "+play.getName()+" for landing on "+prop.getName()+"!");
			noticePushPop(an);
			//parent.jumpStartClean();
		}else{
			log.info("popping self");
			listener.popMe(new ListEvent(this));
		}
	}

	@Override
	protected void defineActions() {
		log.info("Starting definitions");
		if(prop.getOwner() == null || prop.getOwner().equals("")){
			log.info("Property is unowned");
			actions = new JButton[2];
			actions[0] = new JButton("Purchase");
			((JButton)actions[0]).addActionListener(this);
			actions[0].setEnabled(player.getCash()-prop.getPrice() > 0);
			actions[1] = new JButton("Auction Off");
			((JButton)actions[1]).addActionListener(this);
		}else{
			log.info("Property is owned");
			actions = new JButton[1];
			if(!prop.getOwner().equals(player.getName())){
				log.info("Property owned by competitor");
				actions[0] = new JButton("Pay Rent");
				((JButton)actions[0]).addActionListener(this);
			}else{
				log.info("Property owned by given player");
				actions[0] = new JButton("Move Along");
				((JButton)actions[0]).addActionListener(this);
			}
		}
		
	}

}
