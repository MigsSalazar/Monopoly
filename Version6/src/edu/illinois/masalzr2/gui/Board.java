package edu.illinois.masalzr2.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.google.gson.annotations.Expose;

import lombok.extern.log4j.*;

@Log4j2
public class Board {
	private JPanel board;
	private GridLayout grid;
	@Expose
	private Dimension dim;
	
	@Expose
	private HashMap<String, Dimension> pieceCoords;
	
	@Expose
	private int pixSquare;
	@Expose
	private int gridWidth;
	@Expose
	private int gridHeight;
	@Expose
	private int pixWidth;
	@Expose
	private int pixHeight;
	@Expose
	private int[][] iconNumbers;
	
	private ImageIcon[] icons;
	
	private GraphicsButton[][] display;
	private StickerBook stickerBook;
	@Expose
	private Stamp[][] stampCollection;
	@Expose
	private boolean showDice;
	
	private GraphicsButton[][] dice1 = new GraphicsButton[3][3];
	
	private GraphicsButton[][] dice2 = new GraphicsButton[3][3];
	@Expose
	private int d1=1;
	@Expose
	private int d2=1;
	
	private ImageIcon dotDie;
	
	private ImageIcon blankDie;
	
	public Board(int w, int h, int p){
		buildBoard(w,h,p);
	}
	
	public Board(int w, int h){
		buildBoard(w,h, 20);
	}
	
	public Board(){
		buildBoard(30,30, 20);
	}

	private void buildBoard(int w, int h, int pix) {
		log.info("Creating new Baord");
		pieceCoords = new HashMap<String, Dimension>();

		gridWidth = w;
		gridHeight = h;
		
		pixSquare = pix;
		
		pixWidth = gridWidth*pixSquare;
		pixHeight = gridHeight*pixSquare;
		dim = new Dimension(pixWidth, pixHeight);
		
		grid = new GridLayout(gridWidth, gridHeight);
		log.debug("gridWidth={} gridHeight={} pixWidth={} pixHeight={}", gridWidth, gridHeight, pixWidth, pixHeight);
		board = new JPanel(grid);
		board.setMaximumSize(dim);
		log.info("Setting iconNumbers and stickerBook dimensions");
		iconNumbers = new int[gridWidth][gridHeight];
		
		log.info("Populating iconNumbers");
		for(int[] i : iconNumbers){
			for(int j=0; j<i.length; j++){
				i[j] = 0;
			}
		}
		
		icons = new ImageIcon[1];
		icons[0] = new ImageIcon();
		
		display = new GraphicsButton[gridWidth][gridHeight];
		stampCollection = new Stamp[gridWidth][gridHeight];
		log.info("Loading Graphics Buttons with empty placeholder icons");
		for(int x=0; x<gridWidth; x++){
			for(int y=0; y<gridHeight; y++){
				display[x][y] = new GraphicsButton();
				
				//display[x][y].setBorderPainted(false);
				display[x][y].setPreferredSize(new Dimension(pixSquare,pixSquare));
				stampCollection[x][y] = new Stamp();
				log.debug("Stamp at x:{} y:{} {}",x, y, stampCollection[x][y]);
			}
		}
		
		//paintDisplay();
		log.info("Adding JButtons to display");
		for(JButton[] jba : display){
			for(JButton jb : jba){
				board.add(jb);
			}
		}
		
		board.setPreferredSize(new Dimension(pixSquare*gridWidth,pixSquare*gridHeight));
	}
	
	public JPanel getBoard(){
		log.debug("Board: Get Board ");
		return board;
	}

	public void paintDisplay() {
		log.info("Beginning");
		
		for(int b=0; b<display.length; b++){
			for(int i=0; i<display[b].length; i++){
				
				display[b][i].setIcon(icons[ iconNumbers[b][i] ]);
				log.debug("page depth at b:{} i:{} is {}", b, i, stickerBook.pageDepthAt(b, i));
				if(stickerBook.pageDepthAt(b, i) > 0 ){
					log.debug("found a sticker! b={} i={}", b, i);
					for( ImageIcon icon : stickerBook.stackStickersAt(b, i) ){
						display[b][i].addIcon(icon);
					}
					
				}else{
					log.debug("no sticker at b={} i={}", b, i);
					display[b][i].wipeIcons();
				}
				
				if(stampCollection != null){
					if(stampCollection[b] != null){
						if(stampCollection[b][i] != null){
							display[b][i].setIconTextGap(-30);
							display[b][i].setOpaque(true);
							display[b][i].setLayout(null);
							
							stampCollection[b][i].engraveButton(display[b][i]);
							
							stampCollection[b][i].giveBorder(display[b][i]);
							
						}
					}
				}
				
			}
		}
		
		if(showDice){
			paintDice(d1,d2);
		}
		
	}
	
	public void setStamps(Stamp[][] collection){
		log.debug("Set Stamps");
		stampCollection = collection;
	}
	
	public void setIconNumbers(int[][] nums){
		log.debug("Set Icon Numbers");
		iconNumbers = nums;
	}
	
	public void setIcons(ImageIcon[] i){
		log.debug("Set Image Icons");
		icons = i;
		
		this.setDiceIcons(icons[1], icons[2]);
		
	}
	
	public void setStickerBook(StickerBook sb){
		log.debug("Set Sticker Book");
		stickerBook = sb;
	}
	
	public void addPiece(ImageIcon icon, int x, int y){
		log.debug("Adding icon to display");
		display[x][y].addIcon(icon);
		log.debug("Adding icon to registry");
		pieceCoords.put(icon.toString(), new Dimension(x,y));
	}
	
	public void addPiece(ImageIcon icon, String key, int x, int y){
		log.debug("adding icon to display");
		display[x][y].addIcon(icon);
		log.debug("Adding icon to registry with key: "+key);
		pieceCoords.put(key, new Dimension(x,y));
	}
	
	public void movePiece(ImageIcon icon, int x, int y){
		log.debug("Requesting movement based on image name");
		movePiece(icon, icon.toString(), x, y);
	}
	
	public void movePiece(ImageIcon icon, String key, int x, int y){
		log.debug("Moving piece of key: "+key+" to coords x="+x+" y="+y);
		if(pieceCoords.containsKey(key)){
			log.debug("Key was found, retrieving");
			Dimension dim = pieceCoords.get(key);
			log.debug("Wiping old icons");
			display[dim.width][dim.height].removeIcon(icon);
			log.debug("Setting coordinates");
			dim.setSize(x, y);
			log.debug("Adding Icon");
			display[dim.width][dim.height].addIcon(icon);
		}
	}
	
	public void removePiece(ImageIcon key){
		log.info("Removing piece of key: "+key);
		if(pieceCoords.containsKey(key.getDescription())){
			log.info("Key was found. TrueDelete");
			trueDelete(key);
		}else {
			log.info("No key was found. Removing all pieces with subnames of key");
			Set<String> keys = pieceCoords.keySet();
			String[] strKeys = new String[keys.size()];
			keys.toArray(strKeys);
			log.info("True Deleting pieces");
			for(String s : pieceCoords.keySet()) {
				if(s.contains(key.getDescription())) {
					trueDelete(new ImageIcon(s));
				}
			}
		}
	}

	private void trueDelete(ImageIcon key) {
		log.info("Finding coordinates");
		Dimension dim = pieceCoords.get(key.getDescription());
		log.info("removing piece and wiping icons");
		pieceCoords.remove(key.getDescription());
		display[dim.width][dim.height].removeIcon(key);
	}
	
	
	
	public void activateDice(){
		log.info("Beginning");
		if(!showDice){
			log.debug("Dice was not active. Activating and setting dice");
			for(int j=0, y=Math.floorDiv(gridHeight,2)-3; j<3; j++, y++){
				for(int i=0, x=Math.floorDiv(gridWidth, 2); i<3; i++, x++){
					log.debug("Activate Dice: inner for-loop: dice tile at ({},{})", i, j);
					dice1[i][j] = display[x][y];
					dice2[i][j] = display[x][y+3];
				}
			}
			showDice = true;
		}
		
		log.info("Painting dice");
		paintDice(d1,d2);
		
	}
	
	public void deactivateDice(){
		log.info("Deactivating if active");
		if(showDice){
			log.debug("Dice found active");
			for(int i=0; i<3; i++){
				for(int j=0; j<3; j++){
					dice1[i][j] = null;
					dice2[i][j] = null;
				}
			}
			showDice = false;
		}
		log.info("Painting display sans dice");
		paintDisplay();
	}
	
	public void setDiceLocations(int x1, int y1, int x2, int y2){
		log.info("Selecting 3x3 display for 2 dice for each upper-left corner coordinate given");
		for(int i=0; i<3; i++, x1++){
			for(int j=0; j<3; j++, y1++){
				dice1[i][j] = display[x1][y1];
			}
			y1 -= 3;
		}
		
		for(int i=0; i<3; i++, x2++){
			for(int j=0; j<3; j++, y2++){
				dice2[i][j] = display[x2][y2];
			}
			y2 -= 3;
		}
		log.info("Painting display");
		paintDisplay();
		
	}
	
	public void setDiceIcons(ImageIcon dDie, ImageIcon bDie){
		log.debug("Setting Icons for Dice");
		dotDie = dDie;
		blankDie = bDie;
	}
	
	public void paintDice(int dOne, int dTwo){
		log.debug("Updating dice: d1="+dOne+" d2="+dTwo);
		d1 = dOne;
		d2 = dTwo;
		if(!showDice){
			log.info("Dice are inactive. Ending");
			return;
		}
		
		if(dotDie == null){
			log.info("DotDie is null. Setting placeholder");
			dotDie = new ImageIcon();
		}
		
		if(blankDie == null){
			log.info("BlankDie is null. Setting placeholder");
			blankDie = new ImageIcon();
		}
		log.info("Setting all empty");
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				dice1[i][j].setIcon(blankDie);
				dice2[i][j].setIcon(blankDie);
			}
		}
		log.info("Painting first die");
		switch(d1){
		case 5:	dice1[0][0].setIcon(dotDie);
				dice1[2][2].setIcon(dotDie);
		case 3: dice1[2][0].setIcon(dotDie);
				dice1[0][2].setIcon(dotDie);
		case 1: dice1[1][1].setIcon(dotDie);
			break;
		case 6: dice1[0][1].setIcon(dotDie);
				dice1[2][1].setIcon(dotDie);
		case 4: dice1[0][0].setIcon(dotDie);
				dice1[2][2].setIcon(dotDie);
		case 2: dice1[0][2].setIcon(dotDie);
				dice1[2][0].setIcon(dotDie);
			break;
		}
		log.info("Painting second die");
		switch(d2){
		case 5:	dice2[0][0].setIcon(dotDie);
				dice2[2][2].setIcon(dotDie);
		case 3: dice2[2][0].setIcon(dotDie);
				dice2[0][2].setIcon(dotDie);
		case 1: dice2[1][1].setIcon(dotDie);
			break;
		case 6: dice2[0][1].setIcon(dotDie);
				dice2[2][1].setIcon(dotDie);
		case 4: dice2[0][0].setIcon(dotDie);
				dice2[2][2].setIcon(dotDie);
		case 2: dice2[0][2].setIcon(dotDie);
				dice2[2][0].setIcon(dotDie);
			break;
		}
		
		//dice1[0][0].setIcon(dotDie);
	}
	
}
