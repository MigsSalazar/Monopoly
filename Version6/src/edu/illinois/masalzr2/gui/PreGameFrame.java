/**
 * 
 */
package edu.illinois.masalzr2.gui;

import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.illinois.masalzr2.Starter;
import edu.illinois.masalzr2.controllers.Environment;
import edu.illinois.masalzr2.io.GameIo;
import lombok.extern.log4j.*;


/**
 * This GUI class presents the first frame you see in the application
 * holding the 4 bottom buttons and the large splash image. The focus
 * here is just to have a starting point for the application
 * @author Miguel Salazar
 *
 */
@Log4j2
public class PreGameFrame extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7859011893692247775L;
	
	private static String sep = File.separator;
	private JPanel innerPanel = new JPanel();
	private ImageIcon picture;
	private JButton newGame;
	private JButton loadGame;
	private JButton settings;
	private JButton about;
	private String fileDir = System.getProperty("user.dir") + sep +"resources"+sep+"packages"+sep+"default.mns";
	private boolean limitingTurns = false;
	private int turnsLimit = 20;
	private String currency = "$";
	private boolean fancyMoveEnabled = true;
	
	private Settings sets;
	
	/**
	 * Builds the JFrame, adding ActionListeners, and set the text/elements of the components.
	 * DOES NOT display or format the frame
	 */
	public PreGameFrame(){
		//c.setLayout(box);
		
		log.info("PreGameFrame: beginning");
		
		picture = new ImageIcon(System.getProperty("user.dir")+sep+"resources"+sep+"topintroimage.png" );
		newGame = new JButton("New Game");
		loadGame = new JButton("Load Game");
		settings = new JButton("Settings");
		about = new JButton("About");
		
		log.info("PreGameFrame: beginning: adding listeners and components");
		addListeners();
		JLabel pictureLabel = new JLabel(picture);
		innerPanel.add("picture", pictureLabel);
		innerPanel.add("new game button", newGame);
		innerPanel.add("load game button", loadGame);
		innerPanel.add("settings button", settings);
		innerPanel.add("about button", about);
		log.info("PreGameFrame: beginning: setup complete");
	}
	
	/**
	 * Formats the frame and then makes it visible
	 */
	public void start(){
		
		log.info("PreGameFrame: Start: beginning start method");
		
		Container c = this.getContentPane();
		c.add(innerPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(420,480);
		this.setTitle("Migs Monopoly!");
		//System.out.println(System.getProperty("user.dir")+"/resources/frameicon.png");
		log.info("PreGameFrame: Start: adding icon");
		Image icon = new ImageIcon(System.getProperty("user.dir")+sep+"resources"+sep+"frameicon.png").getImage();
		this.setIconImage(icon);
		log.info("PreGameFrame: Start: Making Visible");
		this.setVisible(true);
	}
	
	/**
	 * Adds the PreGameFrame object as an ActionListener to the components
	 */
	private void addListeners(){
		
		log.info("Adding pregameframe listeners");
		
		newGame.addActionListener(this);
		
		loadGame.addActionListener(this);
		
		settings.addActionListener(this);
		
		about.addActionListener(this);
	}
	
	/**
	 * Disposes the PreGameFrame object
	 */
	private void closeMe(){
		log.info("PreGameFrame: closing");
		this.dispose();
	}
	
	/**
	 * A quick method to call out bad files an what not.
	 * Thought it was cleaner to have a 7 character method call then
	 * include the entire string
	 */
	private void badFile() {
		JOptionPane.showMessageDialog(this, "The save file you entered is invalid.\nIt is either out of date, corrupted,\nor is not a save file at all.", "Bad file", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * The action performed method for the buttons.
	 * Each button get's their own else-if clause
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		log.info("PreGameFrame: ActionPerformed: ActionEven receieved");
		if(e.getSource().equals(newGame)){
			//the new game button was pressed
			log.info("PreGameFrame: ActionPerformed: NewGame was pressed");
			
			//Grabs the selected mns file, be-it the default or a texture grabbed by settings
			Environment newerGame = GameIo.newGame(fileDir);
			
			//Confirms the existence of a game
			if(newerGame !=null) {
				
				//Sets the game's rules according to the player's settings
				newerGame.setTurnsLimit(turnsLimit);
				newerGame.setLimitingTurns(limitingTurns);
				newerGame.setCurrency(currency);
				newerGame.setFancyMoveEnabled(fancyMoveEnabled);
				
				//Sends to the Starter to make any last touch ups that don't involve the settings. PreGameFrame disposes itself if successful
				if (Starter.gameSetup( (JFrame)this, newerGame))
					closeMe();
			}else {
				
				//Game was found non-existent, corrupt, incomplete, or had some error
				log.info("PreGameFrame: ActionPerformed: Bad file found.");
				badFile();
			}
		}else if(e.getSource().equals(loadGame)){
			//The player wishes to resume an old game
			log.info("PreGameFrame: ActionPerformed: Loading was pressed");
			
			//Begin the search for saved games
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Monopoly Saves", "mns");
			
			//Delegates operation to GameIO
			String dir = GameIo.findFile(this, filter, System.getProperty("user.dir")+sep+"saves");
			
			//Confirms the directory exists. Stops process if not
			if(dir==null) {
				log.info("PreGameFrame: ActionPerformed: Directory was found invalid");
				return;
			}
			
			//Generates an Environment object from the mns file
			log.info("PreGameFrame: ActionPerformed: Producing saved game");
			Environment loadedGame = GameIo.produceSavedGame(dir);
			
			//Confirms that the game was loaded properly. PreGameFrame disposes itself if confirmed.
			if(loadedGame !=null) {
				log.info("PreGameFrame: ActionPerformed: Game was successfully produced");
				loadedGame.buildFrame();
				closeMe();
			}else {
				log.info("PreGameFrame: ActionPerformed: Bad file found");
				badFile();
			}
		}else if(e.getSource().equals(settings)){
			
			//Player has requested to input their own settings
			if(sets == null) {
				
				//Player may have already defined their settings. This check ensures their changes are not overwritten
				sets = new Settings(this, limitingTurns, turnsLimit);
			}
			
			//Begins the settings dialog
			sets.start();
			
			//Stores the values of the settings locally
			fileDir = sets.getFileDir();
			limitingTurns = sets.isTurnsLimited();
			turnsLimit = sets.getTurnLimit();
			currency = sets.getCurrency();
			fancyMoveEnabled = sets.isFancyMoveEnabled();
			
		}else if(e.getSource().equals(about)) {
			//Player want's tp\o know what this game is all about
			Starter.about(this);
		}
		
	}
	
}
