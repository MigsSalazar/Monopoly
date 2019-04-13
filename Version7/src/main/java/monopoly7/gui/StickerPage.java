package monopoly7.gui;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.*;
import monopoly7.models.Coordinates;
import monopoly7.models.RelativeDimensions;

/**
 * A record of Stickers, their names, their relative positions, and their relative dimensions.
 * All relative values are calculated as percentages of the width and height of the page.
 * <br>
 * So a sticker with relative coordinates x=0.1 and y=0.1
 * <br>
 * and relative size of width=0.2 and height=0.3
 * <br>
 * on a page that is 300x150 pixels will have it's top left corner at pixel coordinates
 * (20,15) and be 60 pixels wide by 45 pixels tall.
 * 
 * @author Miguel Salazar
 * @see monopoly7.gui.Sticker
 */
@RequiredArgsConstructor
public class StickerPage extends BufferedRender{
	
	/**
	 * The relative coordinates of all the Stickers
	 */
	private Map<String, Coordinates> coords = new HashMap<String, Coordinates>();
	
	/**
	 * The relative dimensions of all the Stickers
	 */
	private Map<String, RelativeDimensions> sizes = new HashMap<String, RelativeDimensions>();
	
	/**
	 * Map of every Sticker and its name
	 */
	private Map<String, Sticker> stickers = new HashMap<String,Sticker>();
	
	/**
	 * Stores all the names of the stickers and the order in which they will be rendered.
	 * If the order is untouched, the stickers will be rendered in a FIFO fashion. That is,
	 * the earlier the sticker was added, the sooner it will be rendered, with later Stickers
	 * being added closer to the top layer if Stickers overlap
	 */
	private List<String> paintOrder = new ArrayList<String>();
	
	@NonNull @Getter
	private int width;
	@NonNull @Getter
	private int height;
	
	public void setWidth( int w ){
		if( width != w ){
			width = w;
			dirty = true;
		}
	}
	
	public void setHeight( int h ){
		if( height != h ){
			height = h;
			dirty = true;
		}
	}
	
	public void setPaintPriority( String s, int l ){
		if( l < 0 ){
			paintLast( s );
			return;
		}
		if( l >= paintOrder.size() ){
			paintFirst(s);
			return;
		}
		paintOrder.remove(s);
		paintOrder.add(l,s);
		dirty = true;
	}
	
	public void paintLast( String s ){
		if( !existsOnPage(s) ){
			return;
		}
		
		paintOrder.remove(s);
		paintOrder.add(0,  s);
		dirty = true;
	}
	
	public void paintFirst( String s ){
		if( !existsOnPage(s) ){
			return;
		}
		
		paintOrder.remove(s);
		paintOrder.add(s);
		dirty = true;
	}
	
	public boolean resizeSticker( String s, int w, int h ){
		return resizeSticker( s, new RelativeDimensions(w/100,h/100) );
	}
	
	public boolean resizeSticker( String s, double w, double h ){
		return resizeSticker( s, new RelativeDimensions(w,h) );
	}
	
	public boolean resizeSticker( String s, RelativeDimensions rd ){
		if( !existsOnPage(s) ){
			return false;
		}
		sizes.put(s, rd);
		dirty = true;
		return true;
	}
	
	public boolean containsSticker( String stickerName ){
		return stickers.containsKey(stickerName);
	}
	
	public boolean moveSticker( String s, double x, double y ){
		return moveSticker( s, new Coordinates(x,y) );
	}
	
	public boolean moveSticker( String s, int x, int y ){
		return moveSticker( s, new Coordinates((double)(x/100),(double)(y/100)) );
	}
	
	public boolean moveSticker( String s, Coordinates c ){
		if( !existsOnPage(s) ){
			return false;
		}
		
		coords.put(s, c);
		dirty = true;
		return true;
	}
	
	public String addSticker( Sticker s ){
		return addSticker( s, 0, 0, 10, 10 );
	}
	
	public String addSticker( Sticker s, int x, int y, int width, int height ){
		return addSticker( s, new Coordinates(x, y), new RelativeDimensions( width, height ) );
	}
	
	public String addSticker( Sticker s, Coordinates c, RelativeDimensions size ){
		dirty = true;
		String ret = s.toString();
		int code = 0;
		while( existsOnPage(ret+code) ){
			//ret += code;
			code++;
		}
		ret += code;
		coords.put(ret, c);
		sizes.put(ret, size);
		stickers.put(ret, s);
		paintOrder.add(ret);
		dirty = true;
		return ret;
	}
	
	public int removeAllStickerOf( Sticker s ){
		if( s == null ){
			return -1;
		}
		if( !stickers.containsValue(s) ){
			return 0;
		}
		int ret = 0;
		List<String> allNames = new ArrayList<String>( paintOrder );
		for( String name : allNames ){
			if( stickers.get(name).equals(s) ){
				paintOrder.remove(name);
				coords.remove(name);
				sizes.remove(name);
				stickers.remove(name);
				ret++;
			}
		}
		return ret;
	}
	
	public boolean existsOnPage(String s) {
		return coords.containsKey(s) &&
				sizes.containsKey(s) &&
				stickers.containsKey(s) &&
				paintOrder.contains(s);
	}
	
	public boolean existsOnPage( Sticker s ){
		return stickers.containsValue(s);
	}

	@Override
	public Image render(){
		
		if( isRenderNeeded() ){
			lastRender = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			for( String s : paintOrder ){
				Coordinates c = coords.get(s);
				RelativeDimensions rd = sizes.get(s);
				Sticker stick = stickers.get(s);
				
				int x = (int)(width*c.getX());
				int y = (int)(height*c.getY());
				int w = (int)(width*rd.getWidth());
				int h = (int)(height*rd.getHeight());
				
				Image scaled = stick.render().getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING);
				
				lastRender.getGraphics().drawImage(scaled, x, y, null);
				
			}
			dirty = false;
		}
		
		return lastRender;
	}
	
}
