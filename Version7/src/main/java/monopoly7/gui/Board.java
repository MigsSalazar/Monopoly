package monopoly7.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.flogger.Flogger;
import monopoly7.io.ImageUtil;

/**
 * The top level component for managing the board for the game.
 * The JPanel paints a fully rendered image of the current game
 * state according to the underlying board image as well as
 * any BufferedRender passed to paint over the board image.
 * This class was made with StickerBook objects in mind
 * but BufferedRender class will work for the purposes of the board.
 * So... go wild I guess.
 * 
 * @author Miguel Salazar
 *
 *
 */
@Flogger
public class Board extends JPanel{

	/**
	 * Added to remove IDE warning.
	 */
	private static final long serialVersionUID = 2069587844100683941L;
	
	/**
	 * The untouched, raw image gotten from the passed in file
	 */
	private Image unscaledBaseBoard;
	
	/**
	 * Contains all scaled versions of the {@link #unscaledBaseBoard}
	 * by width then height.
	 */
	private Map<Integer, Map<Integer,Image>> scaledBaseBoard;
	
	/**
	 * The current dimensions of the board
	 */
	private Dimension scales;
	

	/**
	 * The BufferedRender to paint over the board
	 * 
	 */
	@Getter @Setter private BufferedRender overlay;
	
	/**
	 * The tolerance for board resizing.
	 * To understand how it works, see
	 * {@link #setScales(int, int)}
	 * 
	 */
	@Getter @Setter private int tolerance = 10;
	
	/**
	 * Accepts a file to an image that will be the actual game board
	 * and a Dimensions object that defines the actual pixel width and height
	 * of the game board.
	 * @param base	a file to an image used for the main board
	 * @param scale	the width and height of the game in pixels
	 */
	public Board( File base, Dimension scale ){
		super();
		assert base.exists();
		scales = scale;
		overlay = null;
		initializeBaseBoards(base);
	}
	
	/**
	 * Accepts a file to an image that will be the actual game board
	 * as well as the width and height in pixels for how large to
	 * display the game
	 * @param base		a file to an image used for the main board
	 * @param width		how wide to display the game in pixels
	 * @param height	how tall to display the game in pixels
	 */
	public Board( File base, int width, int height ){
		super();
		assert base.exists();
		scales = new Dimension( width, height );
		overlay = null;
		initializeBaseBoards(base);
	}
	
	/**
	 * Sets the width and height of the game in pixels.
	 * <br>
	 * NOTE: the board does not copy the dimension changes directly.
	 * See {@link #setScales(int, int)} for more details
	 * @param s	Dimension object which the board should copy from
	 */
	public void setScales( Dimension s ){
		setScales( s.width, s.height );
	}
	
	/**
	 * Sets the width and height of the game in pixels.
	 * <br>
	 * NOTE: the dimension change does not take directly from the
	 * passed values but reduces the width and height to the highest
	 * number divisible by the set tolerance that is also lower than or equal
	 * to the dimensions passed. The actual math is below.
	 * <br>
	 * board.height = w - (w % tolerance)
	 * <br>
	 * board.width = h - (h % tolerance)
	 * @param w	The desired width in pixels for the board
	 * @param h The desired height in pixels for the board
	 */
	public void setScales( int w, int h ){
		w -= ( w % tolerance );
		h -= ( h % tolerance );
		if( w <= 0 ){
			w = 1;
		}
		if( h <= 0 ){
			h = 1;
		}
		scales.setSize(w, h);
	}
	
	/**
	 * This is used such that there's no repeated code within the constructors.
	 * It initializes all of the data structures and renders the first iteration
	 * of the board.
	 * @param base file of the desired board image
	 */
	private void initializeBaseBoards(File base) {
		scaledBaseBoard = new HashMap<Integer, Map<Integer, Image>>();
		HashMap<Integer, Image> widthMap = new HashMap<Integer, Image>(); 
		scaledBaseBoard.put(scales.width, widthMap);
		
		unscaledBaseBoard = ImageUtil.openImage(base);
		widthMap.put(scales.height, getScaledBaseBoard( Image.SCALE_AREA_AVERAGING ) );
		this.setPreferredSize(new Dimension( scales.width, scales.height ));
		//baseBoard.
		this.setOpaque(true);
	}
	
	/**
	 * Finds the rendered board with the current dimensions. One is made if it
	 * didn't originally exist.
	 * @param hint	The algorithm to use to properly resize
	 * the image as defined the awt.Image class 
	 * @return	the scaled image of the board
	 * @see java.awt.Image
	 */
	private Image getScaledBaseBoard( int hint ) {
		int width = scales.width;
		if( !scaledBaseBoard.containsKey(width) ){
			scaledBaseBoard.put(width, new HashMap<Integer, Image>());
		}
		int height = scales.height;
		if( !scaledBaseBoard.get(width).containsKey(height) ){
			scaledBaseBoard.get(width).put(height, unscaledBaseBoard.getScaledInstance(width, height, hint));
		}
		return scaledBaseBoard.get(width).get(height);
	}
	
	/**
	 * Gets the scaled board and also calls the stored BufferedRender
	 * objects to produce their own renders to be painted
	 */
	@Override
	public void paintComponent( Graphics g ){
		super.paintComponent(g);
		//this.prepareImage(baseBoard, null);
		g.drawImage(getScaledBaseBoard( Image.SCALE_AREA_AVERAGING ), 0, 0, null);
		if( overlay != null ){
			log.atFiner().atMostEvery(20, TimeUnit.SECONDS).log("overlay is not null");
			g.drawImage(overlay.render(), 0, 0, null);
		}
	}
	
	
}
