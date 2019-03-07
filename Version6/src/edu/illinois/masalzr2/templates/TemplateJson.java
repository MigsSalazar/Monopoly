package edu.illinois.masalzr2.templates;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;

import edu.illinois.masalzr2.controllers.Environment;
import edu.illinois.masalzr2.gui.Stamp;
import edu.illinois.masalzr2.gui.StickerBook;
import edu.illinois.masalzr2.models.Counter;
import edu.illinois.masalzr2.models.Dice;
import edu.illinois.masalzr2.models.GameCard;
import edu.illinois.masalzr2.models.Player;
import edu.illinois.masalzr2.models.ListedPath;
import edu.illinois.masalzr2.models.MonopolizedToken;
import edu.illinois.masalzr2.models.Property;
import edu.illinois.masalzr2.models.Railroad;
import edu.illinois.masalzr2.models.Street;
import edu.illinois.masalzr2.models.Suite;
import edu.illinois.masalzr2.models.Utility;
import lombok.Data;

@Data
public class TemplateJson {
	
	@Expose private Map<String, Player> players;
	@Expose private Map<String, Street> streets;
	@Expose private Map<String, Railroad> rails;
	@Expose private Map<String, Utility> utils;
	private Map<String, Property> properties;
	@Expose private Map<String, Suite> suites;
	@Expose private ListedPath propertyPositions;
	@Expose private List<GameCard> chance;
	@Expose private List<GameCard> commchest;
	
	@Expose private int[][] paintByNumbers;
	@Expose private String[] icons;
	@Expose private Stamp[][] stampCollection;
	@Expose private Map<String, MonopolizedToken> playerTokens;
	@Expose private StickerBook stickerBook;
	
	@Expose private String currency;
	@Expose private String texture;
	@Expose private String saveFile;
	@Expose private Counter houseCount;
	@Expose private Counter hotelCount;
	
	@Expose private int turn;
	@Expose private boolean limitingTurns;
	@Expose private int turnsLimit;
	
	@Expose private Dice dice;
	
	@Expose private boolean fancyMoveEnabled;
	
	@Expose private String commChestName;
	@Expose private String chanceName;
	/*
	
	@Expose private Map<String, Player> players;
	@Expose private Map<String, Property> properties;
	@Expose private Map<String, Suite> suites;
	@Expose private PositionIndex propertyPositions;
	@Expose private ArrayList<GameCard> chance;
	@Expose private ArrayList<GameCard> commchest;
	@Expose private int[][] paintByNumbers;
	@Expose private String[] icons;
	@Expose private Stamp[][] stampCollection;
	@Expose private Map<String, GameToken> playerTokens;
	@Expose private int[][] stickerBook;
	@Expose private String[] stickers;
	@Expose private String currency;
	@Expose private String texture;
	 */
	
	public TemplateJson(Environment gv) {
		
		streets = new HashMap<String, Street>();
		rails = new HashMap<String, Railroad>();
		utils = new HashMap<String, Utility>();
		
		players 			= gv.getPlayers();
		properties 			= gv.getProperties();
		
		for(Property p : properties.values()) {
			if(p instanceof Street) {
				streets.put(p.getName(), (Street)p);
			}else if(p instanceof Railroad) {
				rails.put(p.getName(), (Railroad)p);
			}else if(p instanceof Utility) {
				utils.put(p.getName(), (Utility)p);
			}
		}
		
		suites 				= gv.getSuites();
		propertyPositions 	= gv.getPropertyPositions();
		chance 				= gv.getChance();
		commchest 			= gv.getCommchest();
		paintByNumbers 		= gv.getPaintByNumbers();
		icons 				= gv.getIcons();
		stampCollection 	= gv.getStampCollection();
		playerTokens 		= gv.getPlayerTokens();
		stickerBook 		= gv.getStickerBook();
		currency 			= gv.getCurrency();
		texture				= gv.getTexture();
		houseCount 			= gv.getHouseCount();
		hotelCount 			= gv.getHotelCount();
		turn 				= gv.getTurn();
		dice 				= gv.getGameDice();
		limitingTurns		= gv.isLimitingTurns();
		turnsLimit			= gv.getTurnsLimit();
		fancyMoveEnabled 	= gv.isFancyMoveEnabled();
		commChestName 		= gv.getCommChestName();
		chanceName			= gv.getChanceName();
		
		String path = gv.getSaveFile().getParentFile().getParentFile().getPath();
		//System.out.println("Parent path: "+path);
		saveFile 			= path+File.separator+texture+File.separator+texture+".mns";
		
	}
	
	public Map<String, Suite> getSuites(){
		
		for(Suite s : suites.values()) {
			if( s.getStreets().size() != s.getNames().size() && !s.getNames().isEmpty() ){
				List<Street> suiteStreets = s.getStreets();
				for(String name : s.getNames()) {
					//System.out.println("Getting "+name);
					suiteStreets.add(streets.get(name));
				}
				s.setStreets(suiteStreets);
			}
		}
		
		return suites;
	}
	
}
