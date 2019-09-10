package monopoly7;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
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
					break;
				case "list":
					list();
					break;
				case "resize":
					resize( splitCommand );
					break;
				case "roll":
					break;
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
	
	@SuppressWarnings("unchecked")
	private <T> void list(){
		System.out.println("Disaplaying all objects\n"
						 + "=======================");
		for( Class<?> t : objects.typeSet() ){
			System.out.println(t.getName()+":");
			Map<String, T> map = (Map<String, T>) objects.keysToType(t);
			for( String key : map.keySet() ){
				System.out.printf("  - %s: %s\n", key, map.get(key).toString());
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
				DiceDependantProperty u = new DiceDependantProperty( "util"+objectCount, "", new int[]{4,10}, objectCount, 150, 0, false, "FFFFFF", utils );
				env.getProperties().put(u.getName(), u);
				objects.put(u.getName(), u);
				System.out.print(u.getName());
				break;
			default:
				return;
			}
			
		}else{
			Sticker s = new Sticker( ImageUtil.TEXTURES, "default", cmd[2]);
			try{
				//Sticker( String... p )
				String iconName = book.addStickerToPage(0, s,
											Double.parseDouble(cmd[3]), //x coord
											Double.parseDouble(cmd[4]), //y coord
											Double.parseDouble(cmd[5]), //width
											Double.parseDouble(cmd[6]));//height
				objects.put(iconName, s);
				//book.render();
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
	
	void resize( String[] cmd ){
		if( cmd.length < 4 ){
			System.out.println("Too few arguments");
			return;
		}else if( cmd.length > 4 ){
			System.out.println("Too many arguments");
			return;
		}
		double x = Double.parseDouble(cmd[2]);
		double y = Double.parseDouble(cmd[3]);
		book.resizeStickerAtPage(0, cmd[1], x, y);
		frame.repaint();
	}
	
	void move( String[] cmd ){
		if( cmd.length < 4 ){
			System.out.println("Too few arguments");
			return;
		}else if( cmd.length > 4 ){
			System.out.println("Too many arguments");
			return;
		}
		double x = Double.parseDouble(cmd[2]);
		double y = Double.parseDouble(cmd[3]);
		book.moveStickerAtPage(0, cmd[1], x, y);
		frame.repaint();
	}

}
