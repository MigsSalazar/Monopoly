package edu.illinois.masalzr2.notices.implentations;

import java.util.List;

import edu.illinois.masalzr2.models.Player;
import edu.illinois.masalzr2.notices.ListListener;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class PlayerPlayerNotice extends MessageNotice{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public PlayerPlayerNotice(String t, ListListener ppl, Player p1, Player p2, int c){
		super(t, ppl);
		log.debug("PlayerPlayerNotice: Beginning single: add to "+p1.getName()+" sub from "+p2.getName() + " the value of "+c);
		log.debug("PlayerPlayerNotice: Beginning single: Player "+p1.getName()+" before: cash="+p1.getCash()+" wealth="+p1.getWealth()+" liquidWealth="+p1.getLiquidationWorth());
		p1.addCash(c);
		log.debug("PlayerPlayerNotice: Beginning single: Player "+p1.getName()+" after: cash="+p1.getCash()+" wealth="+p1.getWealth()+" liquidWealth="+p1.getLiquidationWorth());
		log.debug("PlayerPlayerNotice: Beginning single: Player "+p2.getName()+" before: cash="+p2.getCash()+" wealth="+p2.getWealth()+" liquidWealth="+p2.getLiquidationWorth());
		p2.subCash(c);
		log.debug("PlayerPlayerNotice: Beginning single: Player "+p2.getName()+" after: cash="+p2.getCash()+" wealth="+p2.getWealth()+" liquidWealth="+p2.getLiquidationWorth());
		
	}
	
	public PlayerPlayerNotice(String t, ListListener ppl, Player p1, List<Player> players, int c){
		super(t, ppl);
		log.info("PlayerPlayerNotice: Beginning multi: add to "+p1.getName()+" from each player value of "+c);
		for(Player p : players){
			log.debug("PlayerPlayerNotice: Beginning single: Player "+p1.getName()+" before: cash="+p1.getCash()+" wealth="+p1.getWealth()+" liquidWealth="+p1.getLiquidationWorth());
			p1.addCash(c);
			log.debug("PlayerPlayerNotice: Beginning single: Player "+p1.getName()+" after: cash="+p1.getCash()+" wealth="+p1.getWealth()+" liquidWealth="+p1.getLiquidationWorth());
			log.debug("PlayerPlayerNotice: Beginning single: Player "+p.getName()+" before: cash="+p.getCash()+" wealth="+p.getWealth()+" liquidWealth="+p.getLiquidationWorth());
			p.subCash(c);
			log.debug("PlayerPlayerNotice: Beginning single: Player "+p.getName()+" after: cash="+p.getCash()+" wealth="+p.getWealth()+" liquidWealth="+p.getLiquidationWorth());
			
		}
	}
}
