package monopoly7;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFrame;

import lombok.extern.flogger.Flogger;
import monopoly7.gui.GameBoard;
import monopoly7.gui.GameFrame;
import monopoly7.gui.Sticker;
import monopoly7.gui.StickerBook;
import monopoly7.io.ImageUtil;
import monopoly7.models.DiceDependantProperty;
import monopoly7.models.Environment;
import monopoly7.models.LinkedGradeProperty;
import monopoly7.models.MonopolizableProperty;
import monopoly7.models.Player;
import monopoly7.models.Property;
import monopoly7.utils.MultiTypeHashMap;

@Flogger
public class CommandLineInterface {
	
	private GameFrame frame;
	private GameBoard board;
	private StickerBook book;
	private Environment env;
	private List<LinkedGradeProperty> rails;
	private List<DiceDependantProperty> utils;
	private MultiTypeHashMap objects = new MultiTypeHashMap();
	private int objectCount = 0;
	
	public CommandLineInterface(){
		rails = new LinkedList<LinkedGradeProperty>();
		utils = new LinkedList<DiceDependantProperty>();
		
		frame = new GameFrame();
		String boardDir = ImageUtil.TEXTURES + ImageUtil.FILESEP + "default" + ImageUtil.FILESEP + "monopoly board.jpg";
		File boardImage = new File( boardDir );
		log.atInfo().log("image exists? "+boardImage.exists());
		board = new GameBoard( new File(boardDir), 500, 500);
		book = new StickerBook(500, 500);
		book.addNewPageAt(0);
		board.setOverlay(book);

		frame.add(board);
		
		env = new Environment();
		env.setBoard(board);
		env.setStickerBook(book);
		env.setHeight(500);
		env.setWidth(500);
		
		frame.getContentPane().addComponentListener( new ComponentAdapter() {
		    @Override
			public void componentResized(ComponentEvent componentEvent) {
				log.atInfo().log("resizing everything");
		    	int width = frame.getContentPane().getWidth();
				int height = frame.getContentPane().getHeight();
				board.setScales(width, height);
		    	book.setWidth(width);
		    	book.setHeight(height);
		    	env.setHeight(height);
		    	env.setWidth(width);
				frame.repaint();
		    }
		});
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public void irbRunner(){
		
		Scanner kin = new Scanner(System.in);
		while( true ){
			System.out.print("$: ");
			String command = kin.nextLine();
			String[] splitCommand = command.split(" ");
			if( splitCommand.length > 0 ){
				if( splitCommand[0].equals("exit") ){
					break;
				}
				switch( splitCommand[0] ){
				case "move":
					move( splitCommand );
					break;
				case "make":
					make( splitCommand );
					break;
				case "add":
					System.out.println(add(splitCommand));
					break;
				case "toggle":
					break;
				case "list":
					list();
					break;
				case "resize":
					resize( splitCommand );
					break;
				case "roll":
					break;
				case "remove":
					remove(splitCommand);
					break;
				case "help":
					help();
					break;
				case "fields":
					displayFields();
				default:
					System.out.println("Invalid command");
					break;
				}
			}else{
				System.out.println("Invalid Command");
			}
		}
		kin.close();
	}

	private int add( String[] cmd ){
		if( cmd.length < 4 ){
			System.out.println("Too few arguments");
			return -1;
		}
		if( cmd.length > 4 ){
			System.out.println("Too many arguments");
			return -1;
		}
		try{
			if( objects.containsKey(cmd[1]) ){
				String type = objects.keysToType(cmd[1]).getSimpleName();
				System.out.println("type of "+cmd[1]+": "+type);
				int val = Integer.parseInt(cmd[3]);
				switch( type ){
				case "Player":
					return addToPlayer( objects.get(Player.class, cmd[1]), cmd[2], val);
				case "LinkedGradeProperty":
					return addToProperty( objects.get(LinkedGradeProperty.class, cmd[1]), cmd[2], val );
				case "MonopolizableProperty":
					return addToProperty( objects.get(MonopolizableProperty.class, cmd[1]), cmd[2], val );
				case "DiceDependantProperty":
					return addToProperty( objects.get(DiceDependantProperty.class, cmd[1]), cmd[2], val );
				}
			}
		}catch( Exception e ){
			
		}
		return -1;
	}

	private int addToPlayer( Player p, String field, int val ){
		switch( field ){
		case "position":
			log.atInfo().log("advancing player");
			p.advancePosition(val);
			return p.getPosition();
		case "cash":
			log.atInfo().log("adding cash to player");
			p.addCash(val);
			return p.getCash();
		case "bails":
			log.atInfo().log("increasing bail bonds for player");
			p.addBailout(val);
			return p.getBails();
		}
		
		return -1;
	}
	
	private int addToProperty( Property p, String field, int val){
		if( field.equals("grade") ){
			log.atInfo().log("incrementing property grade");
			p.incGrade(val);
			return p.getGrade();
		}
		return -1;
	}
	
	private void displayFields() {
		Field[] fields = Player.class.getDeclaredFields();
		System.out.println("Player fields:");
		printFields(fields);
		System.out.println("\nLinkedGradeProperty");
		fields = LinkedGradeProperty.class.getDeclaredFields();
		printFields(fields);
		System.out.println("\nMonopolizableProperty");
		fields = MonopolizableProperty.class.getDeclaredFields();
		printFields(fields);
		System.out.println("\nDiceDependentProperty");
		fields = DiceDependantProperty.class.getDeclaredFields();
		printFields(fields);
		System.out.println("\nProperty");
		fields = Property.class.getDeclaredFields();
		printFields(fields);
	}

	private void help() {
		String output = "move [icon name] [x-coord] [y-coord]\n" +
						"resize [icon name] [width] [height]\n" +
						"make [type] (if icon[[\"path/to/icon\"] [x-coord] [y-coord] [width] [height] ([name])]\n" +
						"add [for noun] [add value] [to int field] - returns the new value\n" +
						"toggle [for noun] [boolean field] - returns new value\n" +
						"list [all:types:[type]] - depending on the arg, returns all nouns under their type, all types, or all nouns under a type\n" +
						"roll (dice -implied)\n" +
						"fields\n" +
						"remove [object name]";
		System.out.println(output);
	}
	
	@SuppressWarnings("unchecked")
	private <T> void list(){
		System.out.println("Disaplaying all objects\n"
						 + "=======================");
		for( Class<?> t : objects.typeSet() ){
			if( t == null ){
				continue;
			}
			System.out.println(t.getName()+":");
			Map<String, T> map = (Map<String, T>) objects.getMapOfType(t);
			for( String key : map.keySet() ){
				System.out.printf("  - %s: %s ofType %s\n", key, map.get(key).toString(), map.get(key).getClass().toString());
			}
		}
	}
	
	
	private void make( String[] cmd ){
		if( cmd.length < 2 ){
			System.out.println("Too few arguments");
			return;
		}
		if( !cmd[1].equals("icon") ){
			switch( cmd[1] ){
			case "player":
				Player p = new Player( "player"+objectCount, objectCount, 0, 1500, 0, false);
				env.getPlayers().put(p.getName(), p);
				objects.put(p.getName(), p);
				System.out.print(p.getName());
				break;
			case "street":
				// String n, String o, int[] r, int p, int c, int g, boolean m, String h, String color, int uc, int ul, int ml
				MonopolizableProperty s = new MonopolizableProperty( "property"+objectCount, "", new int[]{1,2,3,4,5,6,7}, objectCount, 200, 0, false, "0xFFFFFF", "red", 50, 7, 200 );
				env.getProperties().put(s.getName(), s);
				objects.put(s.getName(), s);
				System.out.print(s.getName());
				break;
			case "railroad":
				LinkedGradeProperty r = new LinkedGradeProperty( "rail"+objectCount, "", new int[]{50,100,150,200}, objectCount, 200, 0, false, "0xFFFFFF", rails );
				env.getProperties().put(r.getName(), r);
				objects.put(r.getName(), r);
				System.out.print(r.getName());
				break;
			case "utility":
				DiceDependantProperty u = new DiceDependantProperty( "util"+objectCount, "", new int[]{4,10}, objectCount, 150, 0, false, "0xFFFFFF", utils );
				env.getProperties().put(u.getName(), u);
				objects.put(u.getName(), u);
				System.out.print(u.getName());
				break;
			default:
				return;
			}
			
		}else{
			if( cmd.length != 7 && cmd.length != 8){
				System.out.println("Invalid number of arguments");
				return;
			}
			Sticker s = new Sticker( ImageUtil.TEXTURES, "default", cmd[2]);
			try{
				//Sticker( String... p )
				String iconName = book.addStickerToPage(0, s,
											Double.parseDouble(cmd[3]), //x coord
											Double.parseDouble(cmd[4]), //y coord
											Double.parseDouble(cmd[5]), //width
											Double.parseDouble(cmd[6]));//height
				
				objects.put( (cmd.length == 8 ? cmd[7] : iconName), iconName);
				book.render();
				board.repaint();
				frame.repaint();
				System.out.print(iconName);
			}catch( NumberFormatException nfe ){
				System.out.println("Invalid number format. Please fix.");
				return;
			}
		}
		
		System.out.println(" added");
		objectCount++;
	}
	
	private void move( String[] cmd ){
		if( cmd.length < 4 ){
			System.out.println("Too few arguments");
			return;
		}else if( cmd.length > 4 ){
			System.out.println("Too many arguments");
			return;
		}
		double x = Double.parseDouble(cmd[2]);
		double y = Double.parseDouble(cmd[3]);
		book.moveStickerAtPage(0, objects.get(String.class, cmd[1]), x, y);
		board.repaint();
		frame.repaint();
	}
	
	private void printFields(Field[] fields) {
		for( Field f : fields){
			System.out.println(f.getType() + ": " + f.getName());
		}
	}
	
	private boolean remove(String[] cmd){
		if( cmd.length > 2 ){
			System.out.println("Too many arguments");
		}else if( cmd.length < 2 ){
			System.out.println("Too few arguments");
		}
		Object gotten = objects.remove(cmd[1]);
		System.out.println("gotten != null: "+(gotten != null));
		if( gotten instanceof String ){
			log.atInfo().log("Trying to remove icon");
			//yes, its an instance of string, but that means we can assume it is an icon
			book.removeStickerAtPage((String)gotten, 0);
			book.render();
			board.repaint();
			frame.repaint();
		}
		return gotten != null;
	}
	
	private void resize( String[] cmd ){
		if( cmd.length < 4 ){
			System.out.println("Too few arguments");
			return;
		}else if( cmd.length > 4 ){
			System.out.println("Too many arguments");
			return;
		}
		double x = Double.parseDouble(cmd[2]);
		double y = Double.parseDouble(cmd[3]);
		log.atInfo().log("object at "+cmd[1]+": "+objects.get(String.class, cmd[1]));
		book.resizeStickerAtPage(0, objects.get(String.class, cmd[1]), x, y);
		
		
		book.render();
		board.repaint();
		frame.repaint();
	}

}
