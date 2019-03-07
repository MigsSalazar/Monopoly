package edu.illinois.masalzr2.notices.implentations;

import edu.illinois.masalzr2.models.Player;
import edu.illinois.masalzr2.notices.ListListener;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class GoNotice extends PlayerBankNotice {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GoNotice(ListListener ppl, Player p) {
		super("<html>You passed or landed on Go!"
				+ "<br>Collect $200!</html>" , ppl, p, 200);
		log.info("GoNotice: Beginning: Player {} has landed/passed on Go", p.getName());
	}

}
