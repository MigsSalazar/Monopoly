
package edu.illinois.masalzr2.controllers;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.google.gson.annotations.Expose;

import edu.illinois.masalzr2.gui.Board;
import edu.illinois.masalzr2.gui.FrameMenu;
import edu.illinois.masalzr2.gui.MortgageManager;
import edu.illinois.masalzr2.gui.Notices;
import edu.illinois.masalzr2.gui.Scoreboard;
import edu.illinois.masalzr2.gui.Stamp;
import edu.illinois.masalzr2.gui.StickerBook;
import edu.illinois.masalzr2.gui.UpgradeManager;
import edu.illinois.masalzr2.models.Counter;
import edu.illinois.masalzr2.models.Dice;
import edu.illinois.masalzr2.models.GameCard;
import edu.illinois.masalzr2.models.MonopolizedToken;
import edu.illinois.masalzr2.models.Player;
import edu.illinois.masalzr2.models.ListedPath;
import edu.illinois.masalzr2.models.Property;
import edu.illinois.masalzr2.models.Street;
import edu.illinois.masalzr2.models.Suite;
import edu.illinois.masalzr2.notices.ListEvent;
import edu.illinois.masalzr2.notices.implentations.GameOverNotice;
import edu.illinois.masalzr2.notices.implentations.MessageNotice;
import edu.illinois.masalzr2.templates.TemplateEnvironment;

import lombok.*;
import lombok.extern.log4j.*;

/**
 * For all intents and purposes, this is the game instance.
 * This is both a massive model class for the game as well as a controller
 * class used as the go between for all objects. This is the central point
 * of contact for all objects and functions and stores all data for the game.
 * This central hub style of paradigm is to allow new/saved games to be loaded
 * independently and without interference from other prior game instances.
 * 
 * @author Miguel Salazar
 *
 */
@Log4j2
@Data
public class Environment implements Serializable, ChangeListener {

	private static final long serialVersionUID = 1L;

	private static final transient String sep = System.getProperty("file.separator");
	
	private transient JFrame frame;
	private transient FrameMenu menuBar;
	private transient Board board;
	private transient Scoreboard scores;
	private Notices notices;
	
	@Expose private File saveFile;
	
	@Expose private Map<String, Player> players;
	@Expose private Map<String, Property> properties;
	@Expose private Map<String, Suite> suites;
	
	private List<Player> playerID;
	private Map<Integer, Property> propertyPos;
	
	@Expose private ListedPath propertyPositions;
	
	@Expose private int turn;
	@Expose private boolean limitingTurns;
	@Expose private int turnsLimit;
	private List<Player> turnTable;
	private Map<String, Boolean> jailTable;
	private Map<String, Integer> jailTimes;
	
	@Expose private Counter railCount;
	@Expose private Counter utilCount;
	
	@Expose private List<GameCard> chance;
	@Expose private List<GameCard> commchest;
	
	@Expose private Dice gameDice;
	
	@Expose private int[][] paintByNumbers;
	@Expose private String[] icons;
	private transient ImageIcon[] paintedIcons;
	@Expose private Stamp[][] stampCollection;
	@Expose private Map<String, MonopolizedToken> playerTokens;
	
	@Expose private StickerBook stickerBook;
	
	private transient ImageIcon[] coloredStickers;
	
	@Expose private String currency;
	@Expose private String texture;
	@Expose private Counter houseCount;
	@Expose private Counter hotelCount;
	
	private Player goingBankrupt = null;
	@Expose boolean fancyMoveEnabled;

	@Expose private String commChestName;
	@Expose private String chanceName;
	
	/**
	 * A courtesy constructor to log the creation of the Environment
	 */
	public Environment() {
		log.info("GameVariables called");
	}
	
	/**
	 * Restructures and populates a JFrame and all required assets for the game.
	 * buildFrame also makes the JFrame visible thus begining the game
	 */
	public void buildFrame() {
		//Define the File separator. Hopefully this doesn't mess with cross platform compatability
		
		//Creating the jframe to be built upon
		frame = new JFrame();
		frame.setTitle("Monopoly!");
		frame.setIconImage( (new ImageIcon( System.getProperty("user.dir")+sep+"resources"+sep+"frameicon.png" )).getImage() );
		
		//Defining a BorderLayout for the frame
		BorderLayout bl = new BorderLayout();
		bl.setHgap(8);
		bl.setVgap(8);
		frame.setLayout(bl);
		
		//Creating the Monopoly Specific JMenuBar
		menuBar = new FrameMenu(this);
		frame.setJMenuBar(menuBar);
		
		//Calls to build the board, or at least refresh it
		buildBoard();
		
		//If this is a new game, notices is null. Otherwise, Notices and their chain of events are saved and restored in loaded files
		if(notices == null) {
			notices = new Notices(this);
		}
		
		//Finds the player's icons on the board and stores them in a separate array
		ImageIcon[] playerIcons = new ImageIcon[playerTokens.size()];
		for(int i=0; i<playerID.size(); i++) {
			playerIcons[i] = playerTokens.get(playerID.get(i).getName()).getPiece();
		}
		
		//Defines the score board. This builds itself separately
		scores = new Scoreboard(playerIcons, playerID, currency );
		
		//Adding all components and formatting the frame
		frame.add(board.getBoard(), BorderLayout.CENTER);
		frame.add(notices.getNoticePanel(), BorderLayout.SOUTH);
		frame.add(scores.getScoreboard(), BorderLayout.EAST);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new WindowListener() {
			@Override
			public void windowActivated(WindowEvent arg0) {}
			@Override
			public void windowClosed(WindowEvent arg0) {}
			@Override
			public void windowClosing(WindowEvent arg0) {
				log.info("Game frame is closing");
			}
			@Override
			public void windowDeactivated(WindowEvent arg0) {}
			@Override
			public void windowDeiconified(WindowEvent arg0) {}
			@Override
			public void windowIconified(WindowEvent arg0) {}
			@Override
			public void windowOpened(WindowEvent arg0) {}
		});
		
		//Frame is visible and the game has started!
		frame.setVisible(true);
	}
	
	/**
	 * Builds the largest JPanel in the frame, the game board by repopulating
	 * and refreshing the ImageIcons for the stickers and game tokens as well as
	 * applying the stamps to each board tile.
	 * The board is a very needy GUI that needs to be told rather verbosely what to do
	 * Such is the life of a controller class, but this allows the developer to micro
	 * manage the board as needed. @see edu.illinois.masalzr2.gui.Board
	 * 
	 */
	private void buildBoard() {
		
		//Defining a new board regardless if new or saved game in use
		log.info("Building game board");
		board = new Board();
		
		//Setting the indexes of the imageicons to be used. Think for a paint-by-number coloring page. The Board uses the same concept
		log.info("Passing icons and numbers");
		board.setIconNumbers(paintByNumbers);
		
		//Refreshes the ImageIcons used by the board
		refreshAllImages();
		
		//Sets the "colors" in the paint-by-number scheme. Each int in the paintByNumbers array is an index of the paintedIcons array
		board.setIcons(paintedIcons);
		
		log.info("Passing Stickers");
		//Refreshing the sticker ImageIcons
		coloredStickers = stickerBook.getPaintedIcons();
		//Gives the board all the stickers it needs
		board.setStickerBook(stickerBook);
		
		//Setting the stamps of the game. The paint-by-number scheme is NOT used here. Each board tile gets their own stamp object
		log.info("Passing stamps, dice, and dice assets");
		board.setStamps(stampCollection);
		
		//Defines the dice icons to be used
		board.setDiceIcons(paintedIcons[1], paintedIcons[2]);
		
		//Verbosely tells the board to turn the dice on
		board.activateDice();
		
		//Sets the dice locations
		board.setDiceLocations(7, 11, 7, 16);
		
		//Paints the display
		log.info("painting display and placing tokens");
		board.paintDisplay();
		
		//Places the player tokens. Read more below. It's the next method
		placeTokens();
	}
	
	/**
	 * Places each player icon on the board at the requested coordinates
	 * according to the player's respective GameToken
	 */
	public void placeTokens() {
		for(MonopolizedToken gt : playerTokens.values()) {
			gt.refreshIcon();
			ListedPath subPath = gt.getRelativePath();
			int[] coords = subPath.currentCoords();
			board.addPiece(gt.getPiece(), coords[0], coords[1]);
		}
	}
	
	/**
	 * Determines the grade of the property, where to place the house/hotel sticker(s),
	 * and how many to apply.
	 */
	public void paintHousing() {
		List<int[]> propPos = propertyPositions.findFullPath();
		//Searches through the suites
		for(Suite s : suites.values()) {
			//Goes through each property in each suite ordered by position
			for(Street st : s.sortedByPosition()) {
				//Grabs the propertie's position
				int[] coords = propPos.get(st.getPosition());
				log.debug("Street {} at position {} with coord-x={} and coord-y={}", st.getName(), st.getPosition(), coords[0], coords[1]);
				
				//Clears all instances of the house and hotel icons
				board.removePiece(new ImageIcon("house"+st.getName()));
				board.removePiece(new ImageIcon("hotel"+st.getName()));
				
				//Sets the grades on a case by case basis. The lack of break statements is intentional to allow the bleed through of cases where there would be redundant/repeated code
				switch(st.getGrade()) {
				case 5: board.addPiece(coloredStickers[1], "hotel"+st.getName()+"left", coords[1], coords[0]);
						board.addPiece(coloredStickers[2], "hotel"+st.getName()+"right", coords[1], coords[0]+1);
						board.addPiece(coloredStickers[3], "hotel"+st.getName()+"bot1", coords[1]+1, coords[0]);
						board.addPiece(coloredStickers[3], "hotel"+st.getName()+"bot2", coords[1]+1, coords[0]+1);
						break;
				case 4: board.addPiece(coloredStickers[0], "house"+st.getName()+"4", coords[1]+1, coords[0]+1);
				case 3: board.addPiece(coloredStickers[0], "house"+st.getName()+"3", coords[1]+1, coords[0]);
				case 2: board.addPiece(coloredStickers[0], "house"+st.getName()+"2", coords[1], coords[0]+1);
				case 1: board.addPiece(coloredStickers[0], "house"+st.getName()+"1", coords[1], coords[0]);
					break;
				}
			}
		}
	}
	
	/**
	 * Defines how many players are joining this game. The players maps are cropped until at the passed size
	 * @param num - max size of the players between 2 inclusive and 8 inclusive
	 */
	public void setParticipantSize(int num) {
		//Checks if the num is within the range of allowed players
		if( num < 2 || num > 8) {
			return;
		}
		
		//Truncates the lists to the desired size
		//Any list not included doesn't need to be truncated
		while(players.size() > num) {
			Player p = playerID.get(players.size()-1);
			players.remove(p.getName());
			playerID.remove(playerID.size()-1);
		}
	}
	
	/**
	 * Rolls the game dice saved within the Environment and paints them to the board
	 * @return int - the sum of the two rolled dice
	 */
	public int roll(){
		//Yup, this rolls the dice... specifics? sure!
		log.info("roll: rolling dice");
		//This rolls the dice!... The game dice... two dice get rolled... their sum is returned... the dice were rolled
		gameDice.roll();
		
		//Now we paint the dice!!!... On the board!... According to each single dice... now the players know what they rolled
		board.paintDice(gameDice.getDice()[0], gameDice.getDice()[1]);
		
		//We return the sum... and uh... yeah... we rolled the dice... There we go... long roll function...
		return gameDice.getLastRoll();
		
		
		//Thats a squiggly bracket that ends the method... the roll is over... stop reading... move on!
	}
	
	/**
	 * Finds the row-column coordinates of the top left corner of a game tile.
	 * This is different from a board tile as each board tile holds one image
	 * where as each game tile represents a property or action on the board. 
	 * 
	 * NOTE: each game tile is accounted for so property positions are taken with all
	 * game tiles taken into account. For example, GO is on game tile/position 0, 
	 * Community Chests are at game tiles/positions 2, 17, and 33, and the properties
	 * between GO and Jail are at 1, 3, 5, 6, 8, and 9
	 * @param p - property represented by the requested coordinates
	 * @return - int[] - of size 2 consisting of the row and column values in that order
	 */
	public int[] getPropCoords(Property p){
		return getPropCoords(p.getPosition());
	}
	
	/**
	 * Helper function to @see {@link #getPropCoords(Property)}
	 * @param p - int value of the property position relative to the board
	 * @return int[] - of size 2 consisting of the row and column values in that order
	 */
	public int[] getPropCoords(int p){
		return propertyPositions.findPath(p, p+1).get(0);
	}

	/**
	 * Jailing a player causes the fancy move to finish, moving the player token to a specialty case
	 * setting the player's position at position 10, and setting the boolean for the player's jail
	 * NOTE: the player does not keep track of their jail status nor their time spent in jail. That
	 * is recorded within the Environment
	 * @param p - the player to be jailed
	 * @return boolean - true if succeeds and the player is found and jailed. False if at all otherwise
	 */
	public boolean jailPlayer(Player p){
		
		//Checking to make sure the player exists in the jailTable
		log.info("jailPlayer: player " + p.getName() + " has been jailed");
		if(jailTable.get(p.getName()) == null ){
			return false;
		}
		
		//Sets the player's jail status to true
		jailTable.put(p.getName(), true);
		
		//Resets any timer on the player's jail time so that they spend the full 3 turns
		resetJail(p);
		
		//Places the player's token into jail
		MonopolizedToken jailMe = playerTokens.get(p.getName());
		
		//Sets the step of the player for when they get out of jail
		jailMe.getRelativePath().setStep(10);
		
		//"Locks" the player's game token. Note, the lock doesn't actually do/prevent anything within the object.
		//It is up to the developer to define locked behavior. In a MonopolizedToken, however, any call to the routing
		//will return the jail cell coordinates
		jailMe.setLocked(true);
		
		//Moves the player to their cell within jail
		int[] newCoords = jailMe.getJailCell().findStepAt(0);
		
		//Moves the player piece to their jail cell
		log.debug("jailPlayer: moving piece to jail cell");
		board.movePiece(jailMe.getPiece(), newCoords[0], newCoords[1]);
		
		//All succeeded so returning true.
		return true;
	}
	
	/**
	 * Finds the player whose turn it is in the game
	 * @return Player - object representing the current player
	 */
	public Player getCurrentPlayer(){
		//Looks for the current player defined by the turn
		return playerID.get(turn%players.size());
	}
	
	/**
	 * Selects a random Community Chest card from the list
	 * @return GameCard - representative object for the Community Chest card
	 */
	public GameCard getRandomCommChest(){
		Random rando = new Random();
		return commchest.get(rando.nextInt(commchest.size()));
		//return commchest.get(0);
	}
	
	/**
	 * Selects a random Chance card from the list
	 * @return GameCard - representative object for the Chance card
	 */
	public GameCard getRandomChance(){
		Random rando = new Random();
		return chance.get(rando.nextInt(chance.size()));
		//return chance.get(2);
	}
	
	/**
	 * Requests a player object with the passed id
	 * @param id - id of the requested player
	 * @return Player - the requested object
	 */
	public Player getPlayerByID(int id){
		return playerID.get(id);
	}
	
	/**
	 * Requests a property object at the position requested relative to the board
	 * @param p - The property's position on the board
	 * @return - Property at the passed position
	 */
	public Property getPropertyAt(int p){
		return propertyPos.get(p);
	}
	
	/**
	 * Sets the max number of turns each player has before the game ends on its own.
	 * This is ignore if the is not limiting the number of turns per player.
	 * NOTE: the turn limit is relative but the @see #turn value is absolute.
	 * To show the difference, if turns limit is set to 5 between 8 players,
	 * each player gets 5 turns, but the #turn value will reach 40, incrementing
	 * by 1 as every player's turn finishes or when they are found bankrupt and skipped
	 * @param t int - number of turns alloted to each player
	 */
	public void setTurnsLimit(int t) {
		turnsLimit = t;
	}
	
	/**
	 * Increments the turn until a non-bankrupt player is found
	 */
	public void nextTurn(){
		do{
			turn++;
		}while( turnTable.get(turn%players.size()).isBankrupt() );
		if(limitingTurns && turn >= (turnsLimit*players.size()) ) {
			//Checks to see if the turn limit is set and has been reached. Ends the game if so
			crownWealthiestWinner();
		}
	}
	
	/**
	 * @see #isInJail(String)
	 * @param p - the player whose jail status is in question
	 * @return boolean - the current jail status of the requested player
	 */
	public boolean isInJail(Player p){
		return isInJail(p.getName());
	}
	
	/**
	 * @see #isInJail(Player)
	 * @param p - the name of the player whose jail status is in question
	 * @return boolean - the current jail status of the requested player
	 */
	public boolean isInJail(String p){
		return jailTable.get(p);
	}

	/**
	 * Incrementally moves the player one game tile at a time according to a swing.Timer instance.
	 * Each fancy move takes 1 second to complete and each step takes 1sec/game tiles to travel.
	 * If fancy move is turned off, this method is not bypassed. Rather, it calls for a basic move
	 * @param p - Player to move
	 * @param move - how far along on the board to move
	 */
	public void fancyPlayerMove(Player p, int move) {
		//This method was an asshole. Be very Very VERY careful with this method. You want bugs? Look here
		if(isInJail(p)){
			return;
		}
		//Confirms the player wants fancy moves
		if( !fancyMoveEnabled ) {
			//NO FANCY MOVE. TELEPORTING GAME PIECES TIME
			
			//Moves the player token but only the icon
			MonopolizedToken current = playerTokens.get(p.getName());
			
			ListedPath path = current.getRelativePath();
			
			path.moveStep(move);
			
			visualMove(current.getPiece(), path.currentCoords());
			
			//Now we move the piece in the logic by setting the corresponding GameToken's step
			current.getRelativePath().moveStep(move);
			return;
		}
		
		//Tells the player what they rolled. This is here because if you have fancy move on, you want a slower game
		//Otherwise this increases the number of clicks per turn needed
		notices.pushMe(new ListEvent(new MessageNotice("You rolled a "+move, notices)));
		
		//Creates the TimerProcess which handles the actual move and starts it
		TimerProcess tp = new TimerProcess(move, p);
		tp.start();
	}
	
	/**
	 * Visually moves the player token on the board without the changing 
	 * the Player object's position value or the GameToken position
	 * @param current - GameToken containing the movement data and piece belonging to the player
	 * @param move - int value defining the distance to travel on the board
	 */
	private void visualMove(ImageIcon piece, int[] coords) {
		//Moves the piece on the board
		board.movePiece(piece, coords[1], coords[0]);
	}
	
	/**
	 * Moves the player's associated piece without changing the player's position value.
	 * Does change the GameToken's position
	 * @see #movePlayer(String, int)
	 * @param p - Player the associated player object of the board piece 
	 * @param move - int the distance the piece needs to travel
	 */
	public void movePlayer(Player p, int move){
		movePlayer(p.getName(), move);
	}
	
	/**
	 * Moves the player's associated piece without changing the player's position value.
	 * Does change the GameToken's position
	 * @see #movePlayer(Player, int)
	 * @param p - String the name of the associated player object of the board piece 
	 * @param move - int the distance the piece needs to travel
	 */
	public void movePlayer(String p, int move){
		
		//Finds the current GameToken
		MonopolizedToken current = playerTokens.get(p);
		
		//Sets the GameToken's new step/position
		ListedPath path = current.getRelativePath();
		path.moveStep(move);
		
		//Visually moves the piece
		visualMove(current.getPiece(), path.currentCoords());
	}
	
	/**
	 * Metaphorically spends the night in jail. 
	 * Increments the jailTimes table at the player's entry and returns the new value.
	 * @see #nightInJail(String)
	 * @param p Player - the player who spent the night in jail
	 * @return int - the number of nights spent in the jail
	 */
	public int nightInJail(Player p) {
		return nightInJail(p.getName());
	}
	
	/**
	 * Metaphorically spends the night in jail. 
	 * Increments the jailTimes table at the player's entry and returns the new value.
	 * @see #nightInJail(Player)
	 * @param p String - name of the player who spent the night in jail
	 * @return int - the number of nights spent in the jail
	 */
	public int nightInJail(String p) {
		//Increments the turns spent in jail and returns that new value
		jailTimes.put(p, jailTimes.get(p)+1);
		return jailTimes.get(p);
	}
	
	/**
	 * Refreshes the entry of the player within the jailTimes map.
	 * @see #resetJail(String)
	 * @param p Player - the player whose jailTime entry to reset
	 */
	public void resetJail(Player p) {
		resetJail(p.getName());
	}
	
	/**
	 * Refreshes the entry of the player within the jailTimes map.
	 * @see #resetJail(Player)
	 * @param p Player - the player whose jailTime entry to reset
	 */
	public void resetJail(String p) {
		jailTimes.put(p, 0);
	}
	
	/**
	 * Metaphorically releases the player passed player from jail
	 * @see #releaseJailedPlayer(String)
	 * @param p Player - the player who will be released from jail
	 */
	public void releaseJailedPlayer(Player p) {
		releaseJailedPlayer(p.getName());
	}
	
	/**
	 * On top of setting the player's jail status to false,
	 * the player piece is also painted as outside of the jail
	 * and calls #releaseJailedPlayer(String) to resent the player's jail time
	 * @param p the name of the player to be released as a String
	 */
	public void releaseJailedPlayer(String p) {
		
		//Sets the player's jail status to false
		jailTable.put(p, false);
		
		//Resets the player's jail timer
		resetJail(p);
		
		log.info("Release Jailed Player: player " + p+ " has been released from jail");
		//Unlocks the GameToken, sets the token's step to 10, and moves it by 0 to refresh the coordinates
		MonopolizedToken jailMe = playerTokens.get(p);
		
		jailMe.setLocked(false);
		ListedPath path = jailMe.getRelativePath();
		//jailMe.movePiece(0);
		
		//Moves the GameToken on the board
		log.info("Release Jailed Player: moving piece to visiting jail");
		board.movePiece(jailMe.getPiece(), path.currentCoords()[0], path.currentCoords()[1]);
	}
	

	/**
	 * Sets the player's bankruptcy status to true and sells all of their properties in an
	 * auction according to the Hasbro rules until all assets are sold. DOES NOT CHECK FOR
	 * BANKRUPTCY. ASSUMES THE PLAYER IS BANKRUPT
	 * @param p Player - player to bankrupt
	 * @return int - the number of players remaining in the game. 
	 */
	private int bankruptPlayer(Player p) {
		
		//Sets the player's bankruptcy status
		p.setBankrupt(true);
		int count = 0;
		int id = -1;
		
		//Looks for any players not bankrupt
		for(int i=0; i<playerID.size(); i++) 
			if(!playerID.get(i).isBankrupt()) {
				count++;
				id = i;
			}
		
		//If only one player remains, that player is the winnner. Otherwise, the passed in bankrupt player is liquidated
		//The chain of events is allowed to be finished but even if the player makes it out of debt, they are still bankrupt and still lost
		if( count > 1 ) {
			JOptionPane.showMessageDialog(frame, p.getName()+" has gone bankrupt! They have neither enough cash on hand or"
												+ "\nliquidizable wealth to pay their debts. Anything left in the chain of events"
												+ "\nwill be completed but any and all properties owned by "+p.getName()
												+ "\nwill be put to auction and all must be sold.");
			//Closes their bank account
			p.setCash(0);
			
			//Gets all the player's properties to sell
			Map<String, Property> props = p.getProps();
			for(Property pr : props.values()) {
				if(pr instanceof Street) {
					//All Streets lose any houses or hotels
					((Street) pr).setGrade(0);
				}
				//All properties are to be auctioned off
				notices.pushMe(new ListEvent(pr));
				//Removes the owner
				p.removeProp(pr);
			}
		}else {
			//Only one player is found remaining. Calling to end the game and returning the player's ID
			notices.setGameOver(true);
			return id;
		}
		//No winner found, player was bankrupt, and the game goes on
		return -1;
	}
	
	/**
	 * Returns the player with the highest wealth and stunts the continuation
	 * of the game once the current chain of events ends. 
	 * @return int - id of the player with the most wealth
	 */
	private int crownWealthiestWinner() {
		int wealth = 0;
		int id = -1;
		//Searches for the player with the greatest wealth
		for(Player p : players.values()) {
			if(wealth < p.getWealth()) {
				wealth = p.getWealth();
				id = p.getId();
			}
		}
		//Tells the notices that the game is over and to prepare for a game over notice
		notices.setGameOver(true);
		return id;
	}
	
	/**
	 * Determines how the game ended and parses the according GameOverNotice
	 * @return GameOverNotice - a dead end notice know who won and why
	 */
	public GameOverNotice getWinner() {
		if(turn >= turnsLimit*players.size()) {
			//A player limit is found. Finding wealthiest player
			return new GameOverNotice(notices, playerID.get(crownWealthiestWinner()), true);
		}else {
			//Game is over because of bankrupt players. The wealthiest player is the only one not bankrupt
			return new GameOverNotice(notices, playerID.get(crownWealthiestWinner()), false);
		}
	}

	/**
	 * Determines if the player lost the game or can sell assets until they are
	 * out of debt and then forces the player to do so.
	 * @param p The player who has either lost (gone bankrupt) or can survive after liquidation
	 */
	private void playerIsSalvageable(Player p) {
		//Assumes the player has the wealth to pay their debts
		while(p.getCash() <= 0) {
			
			//Cycles while player cash is not enough
			String[] options = {"Downgrade", "Mortgage"};
			
			//Asks player to liquidate their assets
			int picked = JOptionPane.showOptionDialog(frame, 
					p.getName()+" does not have enough cash on hand."
							+ "\nSell your assets until you can pay off your debt"
							+ "\nof "+currency+p.getCash(), 
					"Liquidate", 
					JOptionPane.DEFAULT_OPTION, 
					JOptionPane.WARNING_MESSAGE, 
					null, 
					options, 
					null);
			
			//Runs the selected manager
			if(picked != JOptionPane.CANCEL_OPTION) {
				switch(options[picked]) {
				case "Downgrade": UpgradeManager um = new UpgradeManager(this, p);
					um.beginManager();
					break;
				case "Mortgage": MortgageManager mm = new MortgageManager(this, p);
					mm.beginManager();
					break;
				}
			}
			
			//If not enough has been sold, the player is told so
			if(p.getCash() <= 0) {
				JOptionPane.showMessageDialog(null, "You have not sold enough assets!\nRemaining balance: "+currency+p.getCash());
			}
		}
	}
	
	/**
	 * Used when generating an mns, json, or a fresh clean game. Calls
	 * the static methods of @see edu.illinois.masalzr2.TemplateEnvironment
	 * to generate the more complex needed variables
	 */
	public void buildCleanGame() {
		//set the save file and
		saveFile = new File(System.getProperty("user.dir")+sep+"resources"+sep+"packages"+sep+"default.mns");
		
		//Requests fresh players and refreshes the ofhter collections
		players = TemplateEnvironment.definePlayers();
		refreshPlayerCollections();
		
		//Initializes the values manipulated by Settings
		fancyMoveEnabled = true;
		turn = 0;
		limitingTurns = false;
		turnsLimit = 10;
		currency = "$";
		texture = "default";
		commChestName = "Community Chest";
		chanceName = "Chance";
		
		//Defining the games Counter objects
		houseCount = new Counter(0, 33, 0);
		hotelCount = new Counter(0, 13, 0);
		railCount = new Counter(0,5,0);
		utilCount = new Counter(0,3,0);
		
		//Making the game dice
		gameDice = new Dice(6,2);
		
		//Requesting fresh properties and refreshing the collection
		properties = TemplateEnvironment.defineProps(railCount, utilCount, gameDice);
		refreshPropertyCollections();
		
		//Requesting the remaining collections
		suites = TemplateEnvironment.defineSuites(properties);
		propertyPositions = TemplateEnvironment.definePropPositions();
		playerTokens = TemplateEnvironment.definePlayerTokens(playerID);
		icons = TemplateEnvironment.defineIcons();
		
		paintByNumbers = TemplateEnvironment.definePaintByNumbers();
		commchest = TemplateEnvironment.defineCommChest();
		chance = TemplateEnvironment.defineChance();
		stampCollection = TemplateEnvironment.defineStamps();
		stickerBook = TemplateEnvironment.stickerBook();
		
		//Refreshing all ImageIcons
		refreshAllImages();
	}
	
	/**
	 * Calls all ImageIcons used to design the board to refresh their
	 * images from their paths
	 */
	public void refreshAllImages() {
		//Setting the sizes for paintedIcons and coloredStickers
		String root = System.getProperty("user.dir") + sep + "textures" + sep;
		String pathName = root + texture + sep;
		paintedIcons = new ImageIcon[icons.length];
		coloredStickers = stickerBook.getPaintedIcons();
		
		//Finding the Images according to the texture directory and files names
		for(int i=0; i< icons.length; i++) {
			if(icons[i].startsWith(sep+"default"+sep)){
				paintedIcons[i] = new ImageIcon(root+icons[i]);
				log.info("Image created with directory "+root+icons[i]);
			}else{
				paintedIcons[i] = new ImageIcon(pathName+icons[i]);
				log.info("Image created with directory "+pathName+icons[i]);
			}
			//log.atFinest().log(pathName+icons[i]);
			
			
			//System.out.println(pathName+icons[i]);
		}
	}
	
	/**
	 * Takes the {@link #properties} master Property map
	 * and populates the {@link #propertyPos} map
	 */
	public void refreshPropertyCollections() {
		//Populating the propertyPos map
		propertyPos = new HashMap<Integer, Property>();
		for(Property p : properties.values()){
			propertyPos.put(p.getPosition(), p);
			
		}
	}
	
	/**
	 * Takes the {@link #players} master Player map and
	 * populates the {@link #playerID}, {@link #turnTable}, {@link #jailTable}, and {@link #jailTimes} 
	 * maps as well calling {@link #playerID} and {@link #turnTable} to be sorted by ID
	 */
	public void refreshPlayerCollections() {
		//Initializing and populating all player collections
		playerID = new ArrayList<Player>();
		turnTable = new ArrayList<Player>();
		jailTable = new HashMap<String, Boolean>();
		jailTimes = new HashMap<String, Integer>();
		
		for(Player noob : players.values()) {
			playerID.add(noob);
			jailTable.put(noob.getName(), false);
			jailTimes.put(noob.getName(), 0);
			turnTable.add(noob);
			noob.addChangeListener(this);
		}
		//Sorts the two lists as needed
		playerID.sort(Player.ID_ORDER);
		turnTable.sort(Player.ID_ORDER);
		
	}
	
	/**
	 * @see javax.swing.event.ChangeListener#stateChanged(ChangeEvent)
	 * Listens whenever a player has changed and checks to see if they have
	 * gone bankrupt. If their cash is below 0 but they liquidizable wealth
	 * can compensate, the player is forced to liquidize assets until all
	 * debts are paid. If not, the player is bankrupt and looses the game
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		//Used for bankruptcy
		if(e.getSource() instanceof Player) {
			//Player was found to cause the stateChanged
			Player p = (Player)e.getSource();
			
			//The bankruptcy fires many stateChanged calls. This prevents a recursive loop
			//by checking if the passed player is currently being worked on
			if(p.equals(goingBankrupt)) {
				return;
			}
			
			//Gets cash
			int cash = p.getCash();
			if(cash <= 0 ) {
				
				//Sets goingBankrupt to the passed player to prevent recursive loop (remove this I dare you. See what happens)
				goingBankrupt = p;
				//Checks if the player is completely bankrupt or not
				if( p.getLiquidationWorth() + cash > 0 ) {
					playerIsSalvageable(p);
				}else {
					bankruptPlayer(p);
				}
			}
			
		}
		//Restarts the goingBankrupt pointer for future use
		goingBankrupt = null;
	}
	
	
	/**
	 * A wrapper subclass that micro-manages the fancy move process while also
	 * ensuring that other piece's fancy moves are neither interfered or interfered with.
	 * Keeps track of the direction, total move, and remaining moves and
	 * then stops the process when the player has arrived to their destination
	 * @author Miguel Salazar
	 *
	 */
	private final class TimerProcess implements ActionListener {
		private final int move;
		private int direction;
		private int count;
		private MonopolizedToken current;
		private Timer ticker;
		
		
		public TimerProcess(int move, Player p) {
			this.move = move;
			direction = (move > 0) ? 1 : -1;
			count = move * direction;
			current = playerTokens.get(p.getName());
			
			ticker = new Timer(1000/this.move, this);
		}
		
		public void start() {
			ticker.start();
		}
		
		/**
		 * Performs the single step visual move and changes the value of the player's current position accordingly
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			//Calls for a visual move
			log.debug("FancyPlayerMove: team={} count={} mod={} move={} position={}", current.getTeam(), count, direction, move, current.getRelativePath().getStep());
			
			ListedPath path = current.getRelativePath();
			int step = path.getStep();
			
			visualMove(current.getPiece(), path.findPath(step+1, step+2).get(0));
			//logically moves the piece
			path.moveStep(direction);
			//increments the number of steps remaining
			count--;
			if( count <= 0 ) {
				//kills timer once done
				ticker.stop();;
			}
		}
	}
	
}
