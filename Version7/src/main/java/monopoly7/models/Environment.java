package monopoly7.models;

import java.awt.Dimension;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.RefreshFailedException;
import javax.security.auth.Refreshable;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import lombok.Setter;
import monopoly7.gui.GameBoard;
import monopoly7.gui.StickerBook;
import monopoly7.io.ImageUtil;

public class Environment implements Refreshable {
	
	public static final String STICKER_PAGE_PLAYER_PIECES = "player pieces";
	public static final String STICKER_PAGE_HOUSES = "houses";
	public static final String STICKER_PAGE_DICE = "dice";
	
	public static final int STICKER_PAGE_INDEX_PLAYER_PIECES = 0;
	public static final int STICKER_PAGE_INDEX_HOUSES = 1;
	public static final int STICKER_PAGE_INDEX_DICE = 2;
	
	public static Dice dice = Dice.generate2d6();
	public static Environment currentGame = null;
	
	@Expose @Getter @Setter
	private Map<String, Player> players = new HashMap<String,Player>();
	@Expose @Getter @Setter
	private Map<String, Property> properties = new HashMap<String,Property>();
	@Expose @Getter @Setter
	private Map<String, Object> cards = new HashMap<String,Object>();
	
	@Expose @Getter @Setter
	private String currency = "$";
	@Expose @Getter @Setter
	private boolean fancyMove = true;
	
	@Expose @Getter
	private String baseBoardDir = "";
	@Expose @Getter @Setter
	private int height = 400;
	@Expose @Getter @Setter
	private int width = 400;
	@Getter
	private transient GameBoard board = new GameBoard( new File(baseBoardDir), new Dimension(width,height));
	@Getter
	private transient StickerBook stickerBook = new StickerBook(board.getSize());
	
	
	@Override
	public boolean equals( Object o ){
		if( o instanceof Environment ){
			Environment compare = (Environment) o;
			boolean ret = (players.equals(compare.getPlayers()));
			ret &= properties.equals(compare.getProperties());
			ret &= cards.equals(compare.getCards());
			return ret;
		}else{
			return false;
		}
	}
	
	public static void refreshDice(){
		if( dice == null ){
			dice = Dice.generate2d6();
		}
	}
	
	@Override
	public boolean isCurrent() {
		//TODO actually implement this
		//go to every object that can be refreshed
		//and call their isCurrent method
		//and also check that all non refreshables
		//are not null
		return true;
	}

	@Override
	public void refresh() throws RefreshFailedException {
		if( dice == null ){
			dice = Dice.generate2d6();
		}
		if( players == null ){
			players = new HashMap<String, Player>();
		}
		if( properties == null ){
			properties = new HashMap<String, Property>();
		}
		if( cards == null ){
			cards = new HashMap<String, Object>();
		}
		if( stickerBook == null ){
			stickerBook = new StickerBook( width, height );
			stickerBook.addNewPageAt(STICKER_PAGE_PLAYER_PIECES, STICKER_PAGE_INDEX_PLAYER_PIECES);
			stickerBook.addNewPageAt(STICKER_PAGE_HOUSES, STICKER_PAGE_INDEX_HOUSES);
			stickerBook.addNewPageAt(STICKER_PAGE_DICE, STICKER_PAGE_INDEX_DICE);
		}
		if( baseBoardDir == null ){
			baseBoardDir = ImageUtil.FILE404;
		}
		if( board == null ){
			board = new GameBoard( new File(baseBoardDir), new Dimension(400,400) );
			board.setOverlay(stickerBook);
		}
		
	}

}
