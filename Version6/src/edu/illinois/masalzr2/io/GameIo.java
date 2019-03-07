package edu.illinois.masalzr2.io;

import java.awt.Container;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;

import edu.illinois.masalzr2.controllers.Environment;
import edu.illinois.masalzr2.controllers.MonopolyExceptionHandler;
import edu.illinois.masalzr2.gui.Board;
import edu.illinois.masalzr2.models.Counter;
import edu.illinois.masalzr2.models.CyclicalPathModel;
import edu.illinois.masalzr2.models.Dice;
import edu.illinois.masalzr2.models.ListedPath;
import edu.illinois.masalzr2.models.ListedPathModel;
import edu.illinois.masalzr2.models.MonopolizedToken;
import edu.illinois.masalzr2.models.Property;
import edu.illinois.masalzr2.models.Railroad;
import edu.illinois.masalzr2.models.Router;
import edu.illinois.masalzr2.models.Street;
import edu.illinois.masalzr2.models.Utility;
import edu.illinois.masalzr2.templates.TemplateEnvironment;
import edu.illinois.masalzr2.templates.TemplateJson;

import lombok.extern.log4j.*;

@Log4j2
public class GameIo {

	private static String sep = File.separator;
	
	public static boolean boardJson(Board board){
		
		JFileChooser jfc = new JFileChooser();
		int success = jfc.showSaveDialog(null);
		
		if(success != JFileChooser.APPROVE_OPTION){
			return false;
		}	
		
		File f = jfc.getSelectedFile();
		
		FileWriter fout = null;
		
		try {
			fout = new FileWriter(new File(f.getPath()+".json"));

			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
			
			gson.toJson(board, fout);
			
			fout.close();
			
			return true;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static Environment newGame(String fileDir) {
		log.info("beginning");
		File f = new File(fileDir);
		log.info("File made with directory "+fileDir);
		//System.out.println(System.getProperty("user.dir") + "/resources/newgame.mns");
		if(!f.exists()) {
			log.info("File does not exists. Generating template");
			TemplateEnvironment.produceTemplate();
		}
		log.info("Producing saved game");
		Environment retval = null;
		try{
			retval = produceSavedGame(f);
		}catch(IOException ioe){
			retval = null;
		}
		if(retval == null) {
			log.info("Produced Game was found null. Creating Template");
			//System.out.println("GameIo.newGame(): retval found null");
			TemplateEnvironment.produceTemplate();
			try{
				retval = produceSavedGame(f);
			}catch(IOException ioe){
				retval = null;
			}
		}
		log.info("Returning produced game");
		return retval;
	}
	
	public static Environment newGame() {
		return newGame(System.getProperty("user.dir") + sep +"resources"+sep+"packages"+sep+"default.mns");
	}
	
	public static Environment produceSavedGame(String dir) {
		log.info("Opening file at directory "+dir);
		File f = new File(dir);
		//System.out.println(dir);
		if(!f.exists()) {
			//System.out.println("file doesn't exist");
			log.debug("File does not exists. Returning null");
			return null;
		}
		log.info("File found. Requesting game at file");
		try{
			return produceSavedGame(f);
		}catch(IOException ioe){
			return null;
		}
	}
	
	public static Environment produceSavedGame(File dir) throws IOException {
		log.info("File with directory "+dir.getPath());
		FileInputStream fin = null;
		Environment theGame = null;
		try {
			log.info("Beginning inputstream");
			fin = new FileInputStream(dir);
			log.info("Creating ObjectInputStream");
			ObjectInputStream objRead = new ObjectInputStream(fin);
			log.info("Reading object");
			Object obj = objRead.readObject();
			
			if(obj instanceof Environment) {
				log.debug("GameVariables object receieved");
				theGame = (Environment)obj;
			}
			log.info("Closing InputStreams");
			objRead.close();
			//fin.close();
		} catch (FileNotFoundException e) {
			MonopolyExceptionHandler.uncaughtException(e, Thread.currentThread());
		} catch (IOException e) {
			MonopolyExceptionHandler.uncaughtException(e, Thread.currentThread());
		} catch (ClassNotFoundException e) {
			MonopolyExceptionHandler.uncaughtException(e, Thread.currentThread());
		}finally{
			if(fin != null){
				fin.close();
			}
		}
		log.info("Returning the game");
		return theGame;
		
	}
	
	public static String findFile(Container parent, FileNameExtensionFilter filter) {
		return findFile(parent, filter, System.getProperty("user.dir"));
	}
	
	public static String findFile(Container parent, FileNameExtensionFilter filter, String baseDir){
		//System.out.println(System.getProperty("user.dir") + sep + "saves");
		log.info("Searching for games in saves directory");
		JFileChooser chooser = new JFileChooser(baseDir);
		log.info("Setting file filter");
	    //FileNameExtensionFilter filter = new FileNameExtensionFilter("Monopoly Saves","mns");
	    chooser.setFileFilter(filter);
	    log.info("Looking for games");
	    int returnVal = chooser.showOpenDialog(parent);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	log.info("Game was selected");
	       return chooser.getSelectedFile().getPath();
	    }else{
	    	log.info("Selection was canceled");
	    	return null;
	    }
	}

	public static boolean printCleanJson() {
		log.info("Creating clean GameVariables object");
		Environment gv = new Environment();
		gv.buildCleanGame();
		
		try {
			log.info("Preparing write out at directory: "+System.getProperty("user.dir") +sep+"textures"+sep+"default"+sep+"defaultgame.json");
			Writer writeOut = new FileWriter( new File(System.getProperty("user.dir") +sep+"textures"+sep+"default"+sep+"defaultgame.json" ) );
			log.info("Creating Json parser with Gson");
			Gson gson = new GsonBuilder()
							.setPrettyPrinting()
							.excludeFieldsWithoutExposeAnnotation()
							.create();
			log.info("Writing out");
			
			TemplateJson j = new TemplateJson(gv);
			
			gson.toJson(j, writeOut);
			log.info("Closing");
			writeOut.close();
			
		} catch (Exception e) {
			log.info("Exception occured. Writting out log");
			log.info(e.toString());
			//LogMate.LOG.finish();
			//MonopolyExceptionHandler.uncaughtException(e, Thread.currentThread());
			return false;
		}
		return true;
	}
	
	public static Environment varsFromJson(Container parent) {
		
		String gotten = findFile(parent, new FileNameExtensionFilter("Json", "json") );
		if(gotten == null)
			return null;
		Gson gson = new GsonBuilder()
		.registerTypeAdapter(ListedPathModel.class, new InstanceCreator<ListedPathModel>(){
			public ListedPathModel createInstance(Type type){
				return new CyclicalPathModel();
				
			}
		}).registerTypeAdapter(Router.class, new InstanceCreator<Router>(){
			public Router createInstance(Type type){
				return new ListedPath();
			}
		}).create();
		TemplateJson tempVars = null;
		Map<String, Property> props = new HashMap<String, Property>();
		Counter utilCount = new Counter(0,2,0);
		Counter railCount = new Counter(0,4,0);
		try {
			FileReader fin = new FileReader(gotten);
			tempVars = gson.fromJson(fin, TemplateJson.class);
			
			for(Street s : tempVars.getStreets().values()) {
				props.put(s.getName(), s);
			}
			
			for(Railroad r : tempVars.getRails().values()) {
				r.setRailsOwned(railCount);
				if(r.getOwner() != null && !r.getOwner().equals("")) {
					railCount.add(1);
				}
				props.put(r.getName(), r);
				
			}
			
			for(Utility u : tempVars.getUtils().values()) {
				u.setUtilityOwned(utilCount);
				u.setGameDice(tempVars.getDice());
				if(u.getOwner()!=null && !u.getOwner().equals("")) {
					utilCount.add(1);
				}
				props.put(u.getName(), u);
			}
			
			for(MonopolizedToken mt : tempVars.getPlayerTokens().values()){
				boolean temp = mt.isLocked();
				mt.getRelativePath().setListedPathModel(new CyclicalPathModel());
				mt.setLocked(!temp);
				mt.getRelativePath().setListedPathModel(new CyclicalPathModel());
				mt.setLocked(temp);
			}
			
		} catch (FileNotFoundException e) {
			//System.out.println("I failed");
			return null;
		}
		
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
		Environment vars = new Environment();
		vars.setPlayers(			tempVars.getPlayers());
		vars.setProperties(			props);
		vars.setSuites(				tempVars.getSuites());
		vars.setPropertyPositions(	tempVars.getPropertyPositions());
		vars.setChance(				tempVars.getChance());
		vars.setCommchest(			tempVars.getCommchest());
		vars.setPaintByNumbers(		tempVars.getPaintByNumbers());
		vars.setIcons(				tempVars.getIcons());
		vars.setStampCollection(	tempVars.getStampCollection());
		vars.setPlayerTokens(		tempVars.getPlayerTokens());
		vars.setStickerBook(		tempVars.getStickerBook());
		vars.setCurrency(			tempVars.getCurrency());
		vars.setTexture(			tempVars.getTexture());
		vars.setSaveFile(new File(	tempVars.getSaveFile()));
		vars.setHotelCount(			tempVars.getHotelCount());
		vars.setHouseCount(			tempVars.getHouseCount());
		vars.setTurn(				tempVars.getTurn());
		vars.setGameDice(			tempVars.getDice());
		vars.setRailCount(			railCount);
		vars.setUtilCount(			utilCount);
		vars.setLimitingTurns(		tempVars.isLimitingTurns());
		vars.setTurnsLimit(			tempVars.getTurnsLimit());
		vars.setFancyMoveEnabled(	tempVars.isFancyMoveEnabled());
		vars.setCommChestName(		tempVars.getCommChestName());
		vars.setChanceName(			tempVars.getChanceName());
		vars.refreshAllImages();
		vars.refreshPlayerCollections();
		vars.refreshPropertyCollections();
		vars.setGameDice(new Dice(6,2));
		//vars.setTurn(new Counter(0,8,0));
		writeOut(vars);
		return vars;
	}
	
	public static void writeOut(Environment me) {
		log.info("Beginning write out");
		FileOutputStream fout;
		try {
			log.info("Creating FileOutputStream");
			File f;
			if(me.getSaveFile().getParentFile().exists()) {
				f = me.getSaveFile();
			}else {
				f = new File(System.getProperty("user.dir")+me.getSaveFile().getPath());
				//System.out.println(f.getPath());
				if( !f.getParentFile().exists() ) {
					JFileChooser choose = new JFileChooser();
					choose.setDialogTitle("Select where to save");
					int answer = -1;
					while(answer != JFileChooser.APPROVE_OPTION){
						answer = choose.showSaveDialog(null);
					}
					f = choose.getSelectedFile();
				}
			}
			
			fout = new FileOutputStream(f);
			log.info("Creating ObjectOutputStream");
			ObjectOutputStream objWrite = new ObjectOutputStream(fout);
			log.info("Writing object");
			objWrite.writeObject(me);
			
			objWrite.close();
			
		} catch (FileNotFoundException e) {
			log.info("FileNotFouncException occured. Writting out log");
			MonopolyExceptionHandler.uncaughtException(e, Thread.currentThread());
		} catch (IOException e) {
			log.info("Exception occured. Writting out log");
			MonopolyExceptionHandler.uncaughtException(e, Thread.currentThread());
		}
		
	}
	
}
