package monopoly7;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import lombok.extern.flogger.Flogger;
import monopoly7.gui.Board;
import monopoly7.gui.Sticker;
import monopoly7.gui.StickerBook;
import monopoly7.io.ImageUtil;

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
		}
	}
	
	//=======================THESE ARE ALL TEMPORARY=========================
	//DO NOT USE OR UPDATE WITH THESE. THEY SKELETON OBJECTS USED FOR TESTING
	static JFrame temp;
	static Board b;
	static StickerBook book;
	static String stickerName;
	static String pageName;
	//======================END OF TEMPORARY VARIABLES=======================

	public static void main(String[] args) {		
		
		//File f = new File("C:\\Users\\Unknown\\Desktop\\monopoly board.jpg");
		File f = new File(ImageUtil.TEXTURES + "default" + ImageUtil.FILESEP + "monopoly board.jpg" );
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//Board b = new Board(f, screenSize.width, screenSize.height);
		b = new Board(f, 500, 500);
		temp = new JFrame();
		Container c = temp.getContentPane();
		JPanel fek = new JPanel(new BorderLayout());
		fek.add(b, BorderLayout.CENTER);
		c.add(fek);
		temp.getContentPane().addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent componentEvent) {
				log.atInfo().log("resizing everything");
		    	b.setScales(temp.getContentPane().getWidth(), temp.getContentPane().getHeight());
		    	book.setWidth(temp.getContentPane().getWidth());
		    	book.setHeight(temp.getContentPane().getHeight());
		    	fek.repaint();
				temp.repaint();
		    }
				//temp.getContentPane().repaint();
		});
		
		
		
		temp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//temp.pack();
		
		book = new StickerBook( 500, 500 );
		book.addNewPageToEnd("base");
		
		
		b.setOverlay(book);
		/*
		 * USE THIS IF YOU WANT TO ADD TONS OF STICKERS
		JButton shitButton = new JButton("Add Sticker!");
		shitButton.addActionListener(
			(e) -> stickerUp()
		);
		
		fek.add(shitButton, BorderLayout.SOUTH);
		*/
		
		
		
		/*
		 * USE THIS TO MOVE ONE STICKER TO RANDOM PLACES
		Timer timeMe = new Timer( 1000, 
			(e) -> bootRunner()
		);
		//timeMe.start();
		*/
		
		
		//TEST USED TO FIGURE OUT FINE PLACEMENTS OF STICKERS
		double x = Math.random();
		double y = Math.random();
		
		Sticker boot = new Sticker("default","boot.png");
		boot.setBorderSize(10);
		boot.setBorder(2*3*5*7);
		boot.setFillColor(Color.BLUE);
		stickerName = book.addStickerToPage("base", boot, 0.0, 0.079*11, 0.04, 0.04);
		pageName = "base";
		//TEST ENDS
		
		
		temp.pack();
		//temp.setOpacity(1);
		//temp.setResizable(false);
		temp.setVisible(true);
		
	}

	/**
	 * TEMPORARY METHOD
	 * <br>
	 * NEVER USE FOR ANYTHING
	 */
	static void bootRunner(){
		double x = Math.random();
		double y = Math.random();
		//double w = Math.random();
		//double h = Math.random();
		
		log.atInfo().log("x="+x+" : y="+y+" : w="+0.20+" : h="+0.20);
		
		book.moveStickerAtPage(pageName, stickerName, x, y);
		//book.resizeStickerAtPage(pageName, stickerName, 0.20, 0.20);
		b.repaint();
		
	}
	
	/**
	 * TEMPORARY METHOD
	 * <br>
	 * NEVER USE FOR ANYTHING
	 */
	static void stickerUp(){
		Sticker boot = new Sticker("default","boot.png");
		double x = Math.random();
		double y = Math.random();
		double w = Math.random();
		double h = Math.random();
		
		log.atFiner().log("x="+x+" : y="+y+" : w="+w+" : h="+h);
		
		book.addStickerToPage("base", boot, x, y, w, h);
		b.repaint();
	}
	
}
