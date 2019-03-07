package edu.illinois.masalzr2.notices.implentations;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;

import edu.illinois.masalzr2.controllers.Environment;
import edu.illinois.masalzr2.models.GameCard;
import edu.illinois.masalzr2.models.MonopolizedToken;
import edu.illinois.masalzr2.models.Player;
import edu.illinois.masalzr2.models.Property;
import edu.illinois.masalzr2.models.Street;
import edu.illinois.masalzr2.notices.AbstractNotice;
import edu.illinois.masalzr2.notices.ListEvent;
import edu.illinois.masalzr2.notices.ListListener;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class GameCardNotice extends HighLevelNotice {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GameCard card;
	private Player player;
	private MonopolizedToken playerToken;
	private int step;
	
	private List<Player> players;
	private Map<String, Property> properties;
	
	public GameCardNotice(ListListener ppl, Environment gv, Player pl, boolean chance) {
		super(ppl, gv);
		
		players = new ArrayList<Player>( gameVars.getPlayers().values() );
		properties = gameVars.getProperties();
		
		card = cardPicker(chance);
		text = "<html>You landed on "+(chance?gv.getChanceName():gv.getCommChestName())+". Your card reads:<br>"
				+card.getText().substring(6,card.getText().length()).replace("<br>", "");
		player = pl;
		
		playerToken = gameVars.getPlayerTokens().get(player.getName());
		step = playerToken.getRelativePath().getStep();
		
		defineActions();
	}

	public String getText(){
		return text;
	}
	public void setText(String t){
		text = t;
	}
	
	public GameCard cardPicker(boolean chance){
		GameCard card = null;
		if(chance){
			card = gameVars.getRandomChance();
		}else{
			card = gameVars.getRandomCommChest();
		}
		
		return card;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(actions[0])){
			runCard(card);
			listener.popMe(new ListEvent(this));
		}
	}

	@Override
	protected void defineActions() {
		actions = new JComponent[1];
		actions[0] = new JButton("Ok");
		((JButton)actions[0]).addActionListener(this);
	}
	
	private void runCard(GameCard gc){
		moneyChange(gc);
		movePlayer(gc);
		giveJailCard(gc);
		sendToJail(gc);
		findNearestOnBoard(gc);
		findThisOnBoard(gc);
		renovateProperties(gc);
	}
	
	private void moneyChange( GameCard gc){
		AbstractNotice an = null;
		if(gc.getMoneyEarned() <= 0)
			return;
		
		if(gc.isGlobalFunds()){
			an = new PlayerPlayerNotice("Player transaction complete<br>Over seen by the bank.",
											listener, player, players, gc.getMoneyEarned());
		}else{
			an = new PlayerBankNotice("Bank transaction complete.",
										listener, player, gc.getMoneyEarned());
		}
		//NoticePanel p, String message, Player p1, ArrayList<Player> plays, int cost
		noticePushPop(an);
		
	}
	
	private void movePlayer(GameCard gc){
		if(gc.getBaseMovement() != 0){
			//gameVars.movePlayer(player, gc.getBaseMovement());
			//player.movePlayer(gc.getBaseMovement());
			AbstractNotice an = moveAndDo(player, gc.getBaseMovement());
			noticePushPop(an);
		}
	}
	
	private void giveJailCard(GameCard gc){
		if(gc.isGetOutOfJail()){
			player.addOneJailCard();
			MessageNotice an = new MessageNotice("Total Jail Cards: "+player.getJailCard(), listener);
			noticePushPop(an);
		}
	}
	
	private void sendToJail(GameCard gc){
		if(gc.isGoToJail()){
			gameVars.jailPlayer(player);
			MessageNotice an = new MessageNotice("You have been sent to Jail!", listener);
			noticePushPop(an);
		}
	}
	
	private void findNearestOnBoard(GameCard gc){
		AbstractNotice an = null;
		step = playerToken.getRelativePath().getStep();
		switch(gc.getFindNearest()){
		case "railroad":an = findRailroad(player);
			break;
		case "utility": an = findUtility(player);
			break;
		case "": return;
		default: return;
		}
		if(an == null){
			log.info("FindNearestOnBoard returned null");
			return;
		}
		noticePushPop(an);
	}
	
	private AbstractNotice findUtility(Player player){
		int moveBy = 0;
		
		if(step < 12 || step > 28){
			if(step < 12){
				moveBy = 12 - step;
			}else{
				moveBy = 52 - step;
			}
		}else{
			moveBy = 28 - step;
		}
		crossGo(moveBy);
		AbstractNotice event = moveAndDo(player, moveBy);
		return event;
	}

	private AbstractNotice findRailroad(Player player) {
		int moveBy = 0;
		if(step > 35 || step < 5){
			if(step < 5){
				moveBy = 5 - step;
			}else{
				moveBy = 45 - step;
				
			}
		}else if(step > 5 && step < 15){
			moveBy = 15 - step;
		}else if(step > 15 && step < 25){
			moveBy = 25 - step;
		}else if(step > 25 && step < 35){
			moveBy = 35 - step;
		}
		crossGo(moveBy);
		AbstractNotice event = moveAndDo(player, moveBy);
		return event;
	}
	
	private void findThisOnBoard(GameCard gc){
		AbstractNotice an = null;
		step = playerToken.getRelativePath().getStep();
		int moveBy = 0;
		switch(gc.getFindThis()){
		case "": return;
		case "go":	moveBy = 40 - step;
					an = moveAndDo(player, moveBy);
					break;
		case "parking": if(step > 20) {
							moveBy = 40 - (step - 20);
						}else {
							moveBy = (20 - step);
						}
					an = moveAndDo(player, moveBy);
		default:if(properties.containsKey(gc.getFindThis())){
					Property prop = properties.get(gc.getFindThis());
					step = playerToken.getRelativePath().getStep();
					if(step > prop.getPosition()){
						moveBy = (40+prop.getPosition()) - step;
					}else{
						moveBy = prop.getPosition() - step;
					}
					crossGo(moveBy);
					an = moveAndDo(player,moveBy);
				}
		}
		if(an == null){
			return;
		}
		noticePushPop(an);
	}
	
	private void renovateProperties(GameCard gc){
		if(gc.isPropRenovation()){
			Map<String, Property> props = player.getProps();
			int total = 0;
			int houses = 0;
			int housenum = 0;
			int hotels = 0;
			int hotelnum = 0;
			for(String s : props.keySet()){
				if(props.get(s) instanceof Street){
					if(((Street)props.get(s)).getGrade() == 5){
						hotels += gc.getHotelCost();
						total += hotels;
						hotelnum++;
					}else if(((Street)props.get(s)).getGrade() > 0){
						houses += gc.getHouseCost() * ((Street)props.get(s)).getGrade();
						total += houses;
						housenum++;
					}
				}
			}
			String currency = gameVars.getCurrency();
			String textOut = "<html>Renovation Costs:"
							+ "<br>Hotels: "+hotelnum+"    Cost: "+currency+hotels
							+ "<br>Houses: "+housenum+"    Cost: "+currency+houses
							+ "<br>Complete Total:          "+currency+total+"</html>";
			PlayerBankNotice an = new PlayerBankNotice( textOut, listener, player, (total * -1) );
			//player.subCash(total);
			noticePushPop(an);
		}
	}
	
}
