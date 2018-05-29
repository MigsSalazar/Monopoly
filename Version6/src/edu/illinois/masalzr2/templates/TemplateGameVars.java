package edu.illinois.masalzr2.templates;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;

import edu.illinois.masalzr2.gui.Stamp;
import edu.illinois.masalzr2.models.*;

public class TemplateGameVars {
	
	private File saveFile;
	
	private HashMap<String, Property> properties;
	private HashMap<String, Suite> suites;
	
	private HashMap<Integer, Property> propertyPos;
	
	private PositionIndex propertyPositions;
	
	private Counter turn;
	private HashMap<String, Boolean> jailTable;
	private HashMap<String, Integer> jailTimes;
	
	private Counter railCount;
	private Counter utilCount;
	
	private ArrayList<GameCard> chance;
	private ArrayList<GameCard> commchest;
	
	private Dice gameDice;
	
	private int[][] paintByNumbers;
	private String[] icons;
	private Stamp[][] stampCollection;
	private HashMap<String, GameToken> playerTokens;
	
	private String currency;
	private String textureDir;
	private Counter houseCount;
	private Counter hotelCount;
	
	public TemplateGameVars(){
		Player player = new Player(1500);
		
		turn = new Counter(0,8,0);
		
		jailTable = new HashMap<String,Boolean>();
		jailTimes = new HashMap<String, Integer>();
		for(int i=0; i<8; i++){
			jailTable.put("player"+i, false);
			jailTimes.put("player"+i, 0);
			
		}

		currency = "$";
		textureDir = System.getProperty("user.dir")+"/textures/default/";
		houseCount = new Counter(0, 32, 0);
		hotelCount = new Counter(0, 12, 0);
		
		
		railCount = new Counter(0,4,0);
		utilCount = new Counter(0,2,0);
		
		gameDice = new Dice(6,2);
		
		playerTokens = new HashMap<String, GameToken>();
		
		// Rails(4) + utility(2) + 22(streets) = 28
		
		defineProps();
		definePropPositions();
		defineGameTokens();
		defineIcons();
		definePaintByNumbers();
	}
	
	private void definePaintByNumbers(){
		paintByNumbers= new int[][]
				{{0,0,0,0,0,0,		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		0,0,0,0,0,0}, //0
				{0,0,0,0,0,0,		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		0,0,0,0,0,0}, //1
				{0,0,7,24,0,0,		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		0,0,0,0,0,0}, //2
				{0,0,7,25,0,0,		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		0,0,0,0,0,0}, //3
				{0,0,7,0,0,0,		7,7,20,20,7,7,7,7,0,0,8,8,8,8,16,16,8,8,	0,0,0,0,0,0}, //4
				{0,0,7,0,0,0,		7,7,20,20,7,7,7,7,0,0,8,8,8,8,16,16,8,8,	0,0,0,0,0,0}, //5
				
				{0,0,0,0,6,6,		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		9,9,0,0,0,0}, //6
				{0,0,0,0,6,6,		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		9,9,0,0,0,0}, //7
				{0,0,0,0,6,6,		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		9,9,0,0,0,0}, //8
				{0,0,0,0,6,6,		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		9,9,0,0,0,0}, //9
				{0,0,0,0,18,18,		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		18,18,0,0,0,0}, //10
				{0,0,0,0,19,19,		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		19,19,0,0,0,0}, //11
				{0,0,0,0,6,6,		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		9,9,0,0,0,0}, //12
				{0,0,0,0,6,6,		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		9,9,0,0,0,0}, //13
				{0,0,0,0,0,0,		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		0,0,0,0,0,0}, //14
				{0,0,0,0,0,0,		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		0,0,0,0,0,0}, //15
				{0,0,0,0,5,5,		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		20,20,0,0,0,0}, //16
				{0,0,0,0,5,5,		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		20,20,0,0,0,0}, //17
				{0,0,0,0,5,5,		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		10,10,0,0,0,0}, //18
				{0,0,0,0,5,5,		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		10,10,0,0,0,0}, //19
				{0,0,0,0,15,15,		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		0,0,0,0,0,0}, //20
				{0,0,0,0,15,15,		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		0,0,0,0,0,0}, //21
				{0,0,0,0,5,5,		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		10,10,0,0,0,0}, //22
				{0,0,0,0,5,5,		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		10,10,0,0,0,0}, //23
					
				{0,17,17,17,17,17,	4,4,4,4,20,20,4,4,0,0,0,0,3,3,18,18,3,3,	0,0,0,0,0,0}, //24
				{0,17,0,0,0,17,		4,4,4,4,20,20,4,4,0,0,0,0,3,3,19,19,3,3,	0,0,0,0,0,0}, //25
				{0,17,0,17,0,17,	0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		0,0,0,0,0,0}, //26
				{0,17,0,0,0,17,		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		0,21,0,0,0,0}, //27
				{0,17,17,17,17,17,	0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		22,7,7,7,7,7}, //28
				{0,0,0,0,0,0,		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,		0,23,0,0,0,0}};//29
	}
	
	private void defineIcons(){
		String[] icons = {	"baseboard.png",			//0
				"dotdie.png",				//1
				"blankdie.png",				//2
				
				"purple.png",				//3
				"lightblue.png",			//4
				"pink.png",					//5
				"orange.png",				//6
				"red.png",					//7
				"yellow.png",				//8
				"green.png",				//9
				"blue.png",					//10

				"housing.png",				//11
				"hotelLeft",				//12
				"hotelRight",				//13
				"hotelBottom",				//14
				
				"eleccomp.png",				//15
				"waterworks.png",			//16
				
				"jail.png",					//17
				"chesttop.png",				//18
				"chestbottom.png",			//19
				"chance.png",				//20
				
				"gotop.png",				//21
				"gomid.png",				//22
				"gobot.png",				//23
				
				"parktop.png",				//24
				"parkbot.png"};				//25
		String parentDir = System.getProperty("user.dir");
		for(int i=0; i<icons.length; i++){
			icons[i] = parentDir+"/textures/default/"+icons[i];
		}
	}
	
	private void defineGameTokens(){
		GameToken p1 = new GameToken(0, "", new PositionIndex(
				new int[]{25,22,20,18,16,14,12,10,8,6,0,0,0,0,0,0,0,0,0,0,0,6,8,10,12,14,16,18,20,22,25,26,26,26,26,26,26,26,26,26,},
				new int[]{25,26,26,26,26,26,26,26,26,26,25,22,20,18,16,14,12,10,8,6,2,0,0,0,0,0,0,0,0,0,1,6,8,10,12,14,16,18,20,22,},
				new int[]{2},
				new int[]{25}));

		GameToken p2 = new GameToken(1, "", new PositionIndex(
			new int[]{26,23,21,19,17,15,13,11,9,7,0,1,1,1,1,1,1,1,1,1,1,7,9,11,13,15,17,19,21,23,26,27,27,27,27,27,27,27,27,27,},
			new int[]{25,26,26,26,26,26,26,26,26,26,26,22,20,18,16,14,12,10,8,6,2,0,0,0,0,0,0,0,0,0,1,6,8,10,12,14,16,18,20,22,},
			new int[]{2},
			new int[]{26}));

		GameToken p3 = new GameToken(2, "", new PositionIndex(
			new int[]{27,22,20,18,16,14,12,10,8,6,0,2,2,2,2,2,2,2,2,2,4,6,8,10,12,14,16,18,20,22,27,28,28,28,28,28,28,28,28,28,},
			new int[]{25,27,27,27,27,27,27,27,27,27,27,22,20,18,16,14,12,10,8,6,2,1,1,1,1,1,1,1,1,1,1,6,8,10,12,14,16,18,20,22,},
			new int[]{2},
			new int[]{27}));
	
		GameToken p4 = new GameToken(3, "", new PositionIndex(
			new int[]{28,23,21,19,17,15,13,11,9,7,0,3,3,3,3,3,3,3,3,3,5,7,9,11,13,15,17,19,21,23,28,29,29,29,29,29,29,29,29,29,},
			new int[]{25,27,27,27,27,27,27,27,27,27,28,22,20,18,16,14,12,10,8,6,2,1,1,1,1,1,1,1,1,1,1,6,8,10,12,14,16,18,20,22,}
			,
			new int[]{3},
			new int[]{27}));
	
		GameToken p5 = new GameToken(4, "", new PositionIndex(
			new int[]{25,22,20,18,16,14,12,10,8,6,1,0,0,0,0,0,0,0,0,0,0,6,8,10,12,14,16,18,20,22,25,26,26,26,26,26,26,26,26,26,},
			new int[]{26,28,28,28,28,28,28,28,28,28,29,23,21,19,17,15,13,11,9,7,3,2,2,2,2,2,2,2,2,2,2,7,9,11,13,15,17,19,21,23,},
			new int[]{4},
			new int[]{27}));
		
		GameToken p6 = new GameToken(5, "", new PositionIndex(
			new int[]{26,23,21,19,17,15,13,11,9,7,2,1,1,1,1,1,1,1,1,1,1,7,9,11,13,15,17,19,21,23,26,27,27,27,27,27,27,27,27,27,},
			new int[]{26,28,28,28,28,28,28,28,28,28,29,23,21,19,17,15,13,11,9,7,3,2,2,2,2,2,2,2,2,2,2,7,9,11,13,15,17,19,21,23,},
			new int[]{4},
			new int[]{26}));
	
		GameToken p7 = new GameToken(6, "", new PositionIndex(
			new int[]{27,22,20,18,16,14,12,10,8,6,3,2,2,2,2,2,2,2,2,2,4,6,8,10,12,14,16,18,20,22,27,28,28,28,28,28,28,28,28,28,},
			new int[]{26,29,29,29,29,29,29,29,29,29,29,23,21,19,17,15,13,11,9,7,3,3,3,3,3,3,3,3,3,3,2,7,9,11,13,15,17,19,21,23,},
			new int[]{4},
			new int[]{25}));
	
		GameToken p8 = new GameToken(7, "", new PositionIndex(
			new int[]{28,23,21,19,17,15,13,11,9,7,4,3,3,3,3,3,3,3,3,3,5,7,9,11,13,15,17,19,21,23,28,29,29,29,29,29,29,29,29,29,},
			new int[]{26,29,29,29,29,29,29,29,29,29,29,23,21,19,17,15,13,11,9,7,3,3,3,3,3,3,3,3,3,3,2,7,9,11,13,15,17,19,21,23,},
			new int[]{3},
			new int[]{25}));
	
	
		playerTokens.put("p1", p1);
		playerTokens.put("p2", p2);
		playerTokens.put("p3", p3);
		playerTokens.put("p4", p4);
		playerTokens.put("p5", p5);
		playerTokens.put("p6", p6);
		playerTokens.put("p7", p7);
		playerTokens.put("p8", p8);
	}
	
	private void definePropPositions(){
				//  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 31 32 33 34 35 36 37 38 39
		int[] x = {22,-1,18,-1,-1,12,-1, 8, 6,-1, 4,-1, 4, 4,-1, 4,-1, 4, 4,-1, 6,-1,10,12,-1,16,18,-1,22,24,24,-1,24,-1,-1,24,-1,24};
		int[] y = {24,-1,24,-1,-1,24,-1,24,24,-1,22,-1,18,16,-1,12,-1, 8, 6,-1, 4,-1, 4, 4,-1, 4, 4,-1, 4, 6, 8,-1,12,-1,-1,18,-1,22};
		
		propertyPositions = new PositionIndex();
	}
	
	private void defineProps(){
		Property[] props = new Property[28];
		props[0] = new Street("Mediterranean Ave.", 1, 60, "", false, null, 0, 30, new int[]{2, 10, 30, 90, 160, 250});
		props[1] = new Street("Baltic Ave.", 		3, 60, "", false, null, 0, 30, new int[]{4, 20, 60, 180, 320, 450});

		props[2] = new Railroad("Reading Railroad", 5, 200, "", false, null);

		props[3] = new Street("Oriental Ave.", 		6, 100, "", false, null, 0, 50, new int[]{6, 30, 90, 270, 400, 550});
		props[4] = new Street("Vermont Ave.", 		8, 100, "", false, null, 0, 50, new int[]{6, 30, 90, 270, 400, 550});
		props[5] = new Street("Connecticut Ave.", 	9, 120, "", false, null, 0, 50, new int[]{8, 40, 100, 300, 450, 600});

		props[6] = new Street("St. Charles Place", 	11, 140, "", false, null, 0, 100, new int[]{10, 50, 150, 450, 625, 750});
		props[7] = new Utility("Electric Company", 	12, 150, "", false, null);
		props[8] = new Street("States Ave.", 		13, 140, "", false, null, 0, 100, new int[]{10, 50, 150, 450, 625, 750});
		props[9] = new Street("Virginia Ave.", 		14, 160, "", false, null, 0, 100, new int[]{12, 60, 180, 500, 700, 900});
		
		props[10] = new Railroad("Pennsylvania Railroad", 15, 200, "", false, null);

		props[11] = new Street("St. James Place", 	16, 180, "", false, null, 0, 100, new int[]{14, 70, 200, 550, 750, 950});
		props[12] = new Street("Tennessee Ave.",	18, 180, "", false, null, 0, 100, new int[]{14, 70, 200, 550, 750, 950});
		props[13] = new Street("New York Ave.", 	19, 200, "", false, null, 0, 100, new int[]{16, 80, 220, 600, 800, 1000});

		props[14] = new Street("Kentucky Ave.", 	21, 220, "", false, null, 0, 150, new int[]{18, 90, 250, 700, 875, 1050});
		props[15] = new Street("Indiana Ave.", 		23, 220, "", false, null, 0, 150, new int[]{18, 90, 250, 700, 875, 1050});
		props[16] = new Street("Illinois Ave.", 	24, 240, "", false, null, 0, 150, new int[]{20, 100, 300, 750, 925, 1100});

		props[17] = new Railroad("B&O Railroad",	25, 200, "", false, null);

		props[18] = new Street("Atlantic Ave.", 	26, 260, "", false, null, 0, 150, new int[]{22, 110, 330, 800, 975, 1150});
		props[19] = new Street("Ventnor Ave.", 		27, 260, "", false, null, 0, 150, new int[]{22, 110, 330, 800, 975, 1150});
		props[20] = new Utility("Water Works", 		28, 150, "", false, null);
		props[21] = new Street("Marvin Gardens", 	29, 280, "", false, null, 0, 150, new int[]{24, 120, 360, 850, 1025, 1200});

		props[22] = new Street("Pacific Ave.", 		31, 300, "", false, null, 0, 200, new int[]{26, 130, 390, 900, 1100, 1275});
		props[23]= new Street("North Carolina Ave.",32, 300, "", false, null, 0, 200, new int[]{26, 130, 390, 900, 1100, 1275});
		props[24] = new Street("Pennsylvania Ave.", 34, 320, "", false, null, 0, 200, new int[]{28, 150, 450, 1000, 1200, 1400});

		props[25] = new Railroad("Short Line", 		35, 200, "", false, null);
		
		props[26] = new Street("Park Place", 		37, 350, "", false, null, 0, 200, new int[]{35, 175, 500, 1100, 1300, 1500});
		props[27] = new Street("Board Walk",		39, 400, "", false, null, 0, 200, new int[]{50, 200, 600, 1400, 1700, 2000});
		
		properties = new HashMap<String, Property>();
		propertyPos = new HashMap<Integer, Property>();
		
		for(Property p : props){
			properties.put(p.getName(), p);
			propertyPos.put(p.getPosition(), p);
		}
	}
	

}
