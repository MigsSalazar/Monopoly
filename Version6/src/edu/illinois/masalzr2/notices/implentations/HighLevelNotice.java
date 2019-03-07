package edu.illinois.masalzr2.notices.implentations;

import javax.swing.JOptionPane;

import edu.illinois.masalzr2.controllers.Environment;
import edu.illinois.masalzr2.gui.MortgageManager;
import edu.illinois.masalzr2.gui.TradeManager;
import edu.illinois.masalzr2.gui.UpgradeManager;
import edu.illinois.masalzr2.models.Dice;
import edu.illinois.masalzr2.models.MonopolizedToken;
import edu.illinois.masalzr2.models.Player;
import edu.illinois.masalzr2.models.Property;
import edu.illinois.masalzr2.notices.AbstractNotice;
import edu.illinois.masalzr2.notices.ListEvent;
import edu.illinois.masalzr2.notices.ListListener;
import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class HighLevelNotice extends AbstractNotice {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Player currentPlayer;
	protected MonopolizedToken playerToken;
	protected Environment gameVars;
	protected Dice gameDice;
	private String currency;
	protected int step;
	
	public HighLevelNotice(ListListener ppl, Environment gv) {
		super(ppl);
		gameVars = gv;
		currentPlayer = gameVars.getCurrentPlayer();
		gameDice = gameVars.getGameDice();
		currency = gameVars.getCurrency();
		playerToken = gameVars.getPlayerTokens().get(currentPlayer.getName());
		step = playerToken.getRelativePath().getStep();
	}
	
	
	protected AbstractNotice actionDone(Player p, int result) {
		AbstractNotice event;
		switch(result){
		case 0: event = new GoNotice(listener, currentPlayer);
				break;
		case 1:event = new MessageNotice( "<html>You are passing through jail.<br>Say hi to the jailbirds!</html>", listener);
			break;
		case 2:event = new MessageNotice("Free Parking! Nothing happens", listener);
			break;
		case 3:	gameVars.jailPlayer(p);
				//p.setPosition(10);
				event = new MessageNotice("<html>"+p.getName()+", you must go to jail!"
											+ "<br>Do not pass Go, do not collect "+currency+"200!</html>",
											listener);
			break;
		case 4:	event = new IncomeTaxNotice(listener, currency, p);
			break;
		case 5: event = new PlayerBankNotice("<html>"+p.getName()+", you must pay the "
											+currency+"75 luxury tax!</html>", 
											listener, 
											p, 
											-75);
			break;
		case 6:	//event = new CardNotice(listener, p, false);
				event = new GameCardNotice(listener, gameVars, p, false);
			break;
		case 7: //event = new CardNotice(listener, p, true);
				event = new GameCardNotice(listener, gameVars, p, true);
			break;
		case 8: //System.out.println("p position = "+ p.getPosition());
				Property passMe = gameVars.getPropertyAt(step);
				event = new PropertyNotice(listener, 
						gameVars, 
						p, 
						passMe);
			break;
		default: event = new MessageNotice("<html>Something went wrong. Defaulted</html>", listener);
			break;
		}
		return event;
	}
	
	protected void conductBusiness() {
		String[] options = {"Upgrade", "Trade", "Mortgage"};
		
		int gotten = JOptionPane.showOptionDialog(gameVars.getFrame(), "What would you like to do?", "Manager Picker", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
		if(gotten < 0) {
			return;
		}
		
		switch(options[gotten]) {
		case "Upgrade": UpgradeManager um = new UpgradeManager(gameVars, currentPlayer);
						um.beginManager();
						break;
		case "Trade":	TradeManager tm = new TradeManager(gameVars, currentPlayer, gameVars.getPlayers());
						tm.runManager();
						break;
		case "Mortgage":MortgageManager mm = new MortgageManager(gameVars, currentPlayer);
						mm.beginManager();
						break;
		default: return;
		}
	}
	
	protected void crossGo(int roll) {
		if( (step + roll) >= 40 || (step + roll)==0){
			GoNotice pbe = new GoNotice(listener, currentPlayer);
			log.info(this.getClass().getName() + ": crossGo: pushing self");
			listener.pushMe(new ListEvent(pbe));
		}
	}
	
	protected void updateTurn(){
		gameVars.nextTurn();
		currentPlayer = gameVars.getCurrentPlayer();
		text = currentPlayer.getName()+"'s turn.\nWhat would you like to do?";
	}
	
	protected AbstractNotice moveAndDo(int roll) {
		step = (playerToken.getRelativePath().getStep() + roll)%40;
		gameVars.fancyPlayerMove(currentPlayer, roll);
		//playerToken.movePiece(roll);
		//step = playerToken.getPath().getStep();
		int result = findAction(step);
		//System.out.println("current Player Name: "+currentPlayer.getName());
		return actionDone(currentPlayer, result);
	}
	
	protected AbstractNotice moveAndDo(Player player, int roll) {
		//System.out.println("player move and do roll: "+roll + " at position " + player.getPosition());
		step = (playerToken.getRelativePath().getStep() + roll)%40;
		gameVars.fancyPlayerMove(player, roll);
		//playerToken.movePiece(roll);
		//step = playerToken.getPath().getStep();
		int result = findAction(step);
		//System.out.println("current Player Name: "+currentPlayer.getName() + " result: "+result);
		return actionDone(player, result);
	}
	
	public static int findAction(int position){
		
		if(position <= 0){
			return 0;
		}else if(position == 10){
			return 1;
		}else if(position == 20){
			return 2;
		}else if(position == 30){
			return 3;
		}else if(position == 4){
			return 4;
		}else if(position == 38){
			return 5;
		}else if(position == 2 || position == 17 || position == 33){
			return 6;
		}else if(position == 7 || position == 22 || position == 36){
			return 7;
		}else{
			return 8;
		}
	}
}
