package monopoly7;

import lombok.extern.flogger.Flogger;

@Flogger
public class MonopolyExceptionHandler implements Thread.UncaughtExceptionHandler{

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		String msg = "NIGH GAME CRASHING ERROR. GAME MAY STOP WORKING PROPERLY IF AT ALL";
		log.atSevere().log(msg);
		log.atSevere().withCause(e);
		e.printStackTrace();
	}

}
