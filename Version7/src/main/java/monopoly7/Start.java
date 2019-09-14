package monopoly7;

import java.io.File;

import lombok.extern.flogger.Flogger;

@Flogger
public class Start {
	
	/**
	 * Determines if the project is running from a non-packaged maven project
	 * and updates the user.dir appropriately such that resources can be used 
	 */
	static{
		String FILESEP = System.getProperty("file.separator");
		String userDevDir = System.getProperty("user.dir")+FILESEP+"src"+FILESEP+"main"+FILESEP;
		File f = new File( userDevDir );
		if( f.isDirectory() ){
			//System.out.println("userDevDir exists");
			log.atWarning().log("Developer mode discovered. Resetting user.dir");
			System.setProperty("user.dir", userDevDir);
			System.setProperty("user.env", "development");	
		}else{
			System.setProperty("user.env", "production");
		}
		MonopolyExceptionHandler masterCatcher = new MonopolyExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(masterCatcher);
		//USEFUL CODE FOR SAFEKEEPING
		/*
		 * JFrame temp;
		 * temp.getContentPane().addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent componentEvent) {
				log.atInfo().log("resizing everything");
		    	b.setScales(temp.getContentPane().getWidth(), temp.getContentPane().getHeight());
		    	book.setWidth(temp.getContentPane().getWidth());
		    	book.setHeight(temp.getContentPane().getHeight());
		    	fek.repaint();
				temp.repaint();
		    }
				//temp.getContentPane().repaint();
		});*/
		
	}

	public static void main(String[] args) {
		log.atInfo().log("Starting program");
		for( String a : args ){
			log.atInfo().log(a);
		}
		if( args.length == 1 ){
			if( args[0].equals("irb") ){
				log.atInfo().log("Starting irb");
				CommandLineInterface cli = new CommandLineInterface();
				cli.irbRunner();
			}
		}
		
		log.atInfo().log("Ending program");
		System.exit(0);
	}

	
	
}
