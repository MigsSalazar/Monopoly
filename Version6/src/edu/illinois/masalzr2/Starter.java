package edu.illinois.masalzr2;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import edu.illinois.masalzr2.controllers.Environment;
import edu.illinois.masalzr2.controllers.MonopolyExceptionHandler;
import edu.illinois.masalzr2.gui.NewGameStartUp;
import edu.illinois.masalzr2.gui.PreGameFrame;
import edu.illinois.masalzr2.io.GameIo;
import edu.illinois.masalzr2.models.GameToken;
import edu.illinois.masalzr2.models.MonopolizedToken;
import edu.illinois.masalzr2.models.Player;

import lombok.extern.log4j.Log4j2;

/**
 * Starting point class for the monopoly game. Other main methods exist in this project
 *
 * edu.illinois.masalzr2.gui.TesterFrame - used for command line manipulation and control. Good for texting
 * edu.illinois.masalzr2.io.GameIO - a deprecated main used to generate Json and mns files and also import textures. NOT RECOMMENDED
 * edu.illinois.masalzr2.templates.Template - produces a clean mns of the game using the base/default texture. Use to bypass the the Settings gui
 * 
 * @author Miguel Salazar
 *
 */
@Log4j2
public class Starter {
	
	/**
	 * Begins before the main to load an Exception handler and a logger
	 * This is placed before anything in hopes that even without the main
	 * being run, the logger and handler will still catch everything they can
	 */
	static{
		//Creating and setting the exception handler
		MonopolyExceptionHandler masterCatcher = new MonopolyExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(masterCatcher);
		Configurator.setRootLevel(Level.INFO);
		//Beginning the log
		log.info("Starter: static: beginning log");
	
	}
	
	/**
	 * Main function. Doesn't deen much intro beyond that
	 * @param args - an array of one String, the directory of an mns file. If no file is given, the game will start a new
	 */
	public static void main( String[] args ){
		
		//Checking if any arguments were passed
		if(args.length > 0) {
			
			//Arguments confirmed
			log.info("found multiple arguments");
			int i=0;
			//Looking for mns files
			while( i<args.length && !args[i].endsWith(".mns") ) {
				i++;
			}
			
			//An mns file was found.
			if(i < args.length) {
				
				//Retrieving file
				File f = new File(args[i]);
				if(f.exists()) {
					
					//Creating game from the pased path
					try{
						GameIo.produceSavedGame(f).buildFrame();
					}catch(IOException ioe){
						MonopolyExceptionHandler.uncaughtException(ioe, Thread.currentThread());
						
					}
					return;
				}
			}
		}
		
		//No arguments found or no mns files found. Starting PreGameFrame
		log.info("creating PreGameFrame");
		PreGameFrame pgf = new PreGameFrame();
		log.info("Starting PreGameFrame");
		pgf.start();
		//myGame.buildFrame();
	}
	
	/**
	 * Displays some basic information about the project
	 * @param parent - The JOptionPane parent. Can be null
	 */
	public static void about(JFrame parent) {
		//Displays the following text into the JOptionPane
		String output =   "                          MONOPOLY V6"
						+ "\n=============================="
						+ "\nCAN'T GET THE GAME TO START? GO TO SETTINGS"
						+ "\nAND SELECT \"CLEAN\" AND RE-IMPORT ANY"
						+ "\nTEXTURES YOU WISH TO USE, THEN PLAY ON."
						+ "\nhis Monopoly was made my Miguel Salazar."
						+ "\nMonopoly is owned by Hasbro. No money was"
						+ "\nmade off this project. No donations of"
						+ "\nany sort will be accepted but criticism"
						+ "\nare always welcome. This project utilizes"
						+ "\nthe following frameworks projects:"
						+ "\n - Apache Maven"
						+ "\n - Project Lombok"
						+ "\n - Google Gson"
						+ "\n - JUnit"
						+ "\nIf you'd like to have the source code:"
						+ "\nhttps://github.com/MigsSalazar/MonopolyV6"
						+ "\nIf you'd like to see earlier (worse)"
						+ "\nversions:"
						+ "\nhttps://github.com/MigsSalazar/MonopolyV6";
		JOptionPane.showMessageDialog(parent, output);
	}
	
	/**
	 * Current functionality - tells users there's no instruction book
	 * Eventual functionality - opens a browser or file to the documentation or a more indept instruction guide
	 * @param parent A JFrame object used by any resulting containers as a reference point 
	 */
	public static void instructionBook(JFrame parent) {
		//TODO Create the instruction book
		//Instruction book to come. Until then, just got docs for all y'all
		try {
			if(Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open( new File(System.getProperty("user.dir") + File.separator + "doc" + File.separator + "index.html" ) );
			}
		}catch(IOException ioe) {
			JOptionPane.showMessageDialog(parent, "Could no open Logs file. To access logs, go to:\n"+System.getProperty("user.dir")+File.separator+"logs");
		}
		/*
		String output = "Currently working on an instruction book"
				+ "of some sort. Please be patient. These take"
				+ "some time ('^-^ )";
		JOptionPane.showMessageDialog(parent, output);
		*/
	}
	
	/**
	 * Prepares and refreshes a game Environment gotten from a saved mns file
	 * All new games start here as all games are converted into a mns file
	 * before being run. Here is where they go from static instance to new instance
	 * 
	 * @param parent - the parent for the NewGameSetUp dialog to rely on
	 * @param newerGame - An Environment variable clean mns file to be altered and fitted to the next games requirements
	 * @return - true - if the Environment is properly setup and all goes well. false - Something went wrong and the process cannot continue
	 */
	public static boolean gameSetup(JFrame parent, Environment newerGame) {
		log.info("NewGame was not null");
		log.info("Finding GameTokens");
		Map<String, MonopolizedToken> to = newerGame.getPlayerTokens();
		List<MonopolizedToken> tokens = new ArrayList<MonopolizedToken>(to.values());
		tokens.sort(GameToken.TEAM_ORDER);
		
		log.info("developing NewGameStartUp");
		NewGameStartUp ngsup = new NewGameStartUp(parent, tokens );
		
		log.info("Starting NewGameStartUp Dialog");
		ngsup.beginDialog();
		
		List<String> names = ngsup.getNames();
		if(!ngsup.isfinished()) {
			return false;
		}
		
		log.info("Dialog has ended, starting game");
		List<Player> playerByIds = newerGame.getPlayerID();
		Map<String, Player> playerByName = newerGame.getPlayers();
		newerGame.setParticipantSize(names.size());
		to.clear();
		
		log.info("Assets gotten");
		
		for(int i=0; i<names.size(); i++) {
			log.info("at name "+i + " is "+names.get(0));
			to.put(names.get(i), tokens.get(i));
			playerByIds.get(i).setName(names.get(i));
			playerByName.remove(""+i);
			playerByName.put(names.get(i), playerByIds.get(i));
		}
		log.info("Loading assets. sending");
		
		newerGame.refreshPlayerCollections();
		newerGame.refreshAllImages();
		newerGame.refreshPropertyCollections();
		newerGame.buildFrame();
		//newerGame.getTurn().setMax(names.size());
		
		return true;
	}
	
}
