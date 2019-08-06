package edu.illinois.masalzr2.controllers;


import lombok.extern.log4j.Log4j2;

@Log4j2
public class MonopolyExceptionHandler implements Thread.UncaughtExceptionHandler {
	
	
	public static void uncaughtException(Throwable e, Thread t){
		MonopolyExceptionHandler meh = new MonopolyExceptionHandler();
		meh.uncaughtException(t, e);
	}
	
	public void uncaughtException(Thread t, Throwable e) {
		String msg = "NIGH GAME CRASHING ERROR. GAME MAY STOP WORKING PROPERLY IF AT ALL";
		log.error(msg, e);
		log.catching(e);
		//SimpleDateFormat today = new SimpleDateFormat("hh.mm.ss MM-dd-yyyy ");
		//String s = System.getProperty("file.separator");
		//FileAppender fa = FileAppender.newBuilder().withFileName(/*s+"logs"+s+"errorlogs"+s+*/"errorLog "+today.format(new Date())+".log").build();
		//fa.error(msg, e);
		//fa.stop();
		/*
		File errorLog = new File(System.getProperty("user.dir") + sep + "logs" + sep + "errorlogs");
		if( !errorLog.exists() ) {
			errorLog.mkdirs();
		}
		*/
		
		
		/*
		File finLog = new File(errorLog.getPath()+sep+"log "+today.format(new Date())+".log");
		
		PrintWriter writeout;
		try {
			writeout = new PrintWriter(finLog);
			e.printStackTrace(writeout);
			writeout.close();
			//JOptionPane.showMessageDialog(null, "Game has arrived at an exception\nApplication now closing\nLog can be found at\n"+finLog.getAbsolutePath());
			//System.exit(1);
		} catch (FileNotFoundException e1) {
			//JOptionPane.showMessageDialog(null, "You fucked up so badly that this exception is literally impossible to acheive"
			//								+ "\nI am both tremendously dissapointed and remarkably impressed at you."
			//								+ "\nCongradulations.");
		}
		*/
		
		
	}
}
