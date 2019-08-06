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
		
	}

}
