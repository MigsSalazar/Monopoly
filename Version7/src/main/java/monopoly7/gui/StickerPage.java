package monopoly7.gui;

import java.awt.Image;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.extern.flogger.Flogger;
import monopoly7.models.RelativeDimension;

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
@Flogger
public class StickerPage extends BufferedRender{
	
	/**
	 * The relative coordinates of all the Stickers
	 */
	private Map<String, Double> coords = new HashMap<String, Double>();
	
	/**
	 * The relative dimensions of all the Stickers
	 */
	private Map<String, RelativeDimension> sizes = new HashMap<String, RelativeDimension>();
	
	/**
	 * Map of every Sticker and its name
	 */
	private Map<String, Sticker> stickers = new HashMap<String,Sticker>();
	
	/**
	 * Stores previously rendered stickers given a certain size.
	 */
	private Map<Integer, Map< Integer, Map<String, Image>>> preRender = new HashMap<Integer, Map<Integer, Map<String, Image>>>();
	
	/**
	 * Stores all the names of the stickers and the order in which they will be rendered.
	 * If the order is untouched, the stickers will be rendered in a FIFO fashion. That is,
	 * the earlier the sticker was added, the sooner it will be rendered, with later Stickers
	 * being added closer to the top layer if Stickers overlap
	 */
	private List<String> paintOrder = new ArrayList<String>();
	
	@Getter
	private int width;
	@Getter
	private int height;
	
	public StickerPage( int w, int h ){
		width = w;
		height = h;
	}
	
	public void setWidth( int w ){
		if( width != w ){
			width = w;
			setDirty( true );
		}
	}
	
	public void setHeight( int h ){
		if( height != h ){
			height = h;
			setDirty( true );
		}
	}
	
	public void setPaintPriority( String s, int l ){
		if( !existsOnPage(s) ){
			paintOrder.remove(s);
			paintOrder.add(l,s);
			setDirty( true );
		}
	}
	
	public void paintLast( String s ){
		setPaintPriority( s, 0 );
	}
	
	public void paintFirst( String s ){
		setPaintPriority(s, paintOrder.size()-1);
	}
	
	public boolean resizeSticker( String s, double w, double h ){
		return resizeSticker( s, new RelativeDimension(w,h) );
	}
	
	public boolean resizeSticker( String s, RelativeDimension dim ){
		if( !existsOnPage(s) ){
			return false;
		}
		sizes.put(s, dim);
		setDirty( true );
		log.atInfo().log( "Sticker %s has changed dimensions", s );
		return true;
	}
	
	public boolean containsSticker( String stickerName ){
		return stickers.containsKey(stickerName);
	}
	
	public boolean moveSticker( String s, double x, double y ){
		return moveSticker( s, new Double(x,y) );
	}
	
	public boolean moveSticker( String s, Double c ){
		if( !existsOnPage(s) ){
			log.atInfo().log("Icon does not exit on page");
			return false;
		}
		log.atInfo().log("moving sticker");
		coords.put(s, c);
		setDirty( true );
		return true;
	}
	
	public String addSticker( Sticker s ){
		return addSticker( s, 0, 0, 10, 10 );
	}
	
	public String addSticker( Sticker s, double x, double y, double width, double height ){
		return addSticker( s, new Double(x, y), new RelativeDimension( width, height ) );
	}
	
	public String addSticker( Sticker s, Double c, RelativeDimension size ){
		setDirty( true );
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
		setDirty( true );
		return ret;
	}
	
	public boolean removeSticker( String s ){
		if( existsOnPage(s) ){
			paintOrder.remove(s);
			coords.remove(s);
			sizes.remove(s);
			stickers.remove(s);
			setDirty(true);
			return true;
		}
		return false;
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
		
		if( isDirty() ){
			setLastRender(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
			
			for( String s : paintOrder ){
				Double c = coords.get(s);
				RelativeDimension rd = sizes.get(s);
				Sticker stick = stickers.get(s);
				
				int x = (int)(width*c.getX());
				int y = (int)(height*c.getY());
				int w = (int)(width*rd.getWidth());
				int h = (int)(height*rd.getHeight());
				
				if( !preRender.containsKey(w) ){
					preRender.put(w, new HashMap<Integer, Map<String, Image>>());
				}
				
				Map<Integer, Map<String, Image>> widthMap = preRender.get(w);
				if( !widthMap.containsKey(h) ){
					widthMap.put(h, new HashMap<String, Image>() );
				}
				
				Map<String, Image> heightGotten = widthMap.get(h);
				if( !heightGotten.containsKey(s) ){
					heightGotten.put(s, stick.render().getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING));
				}
				
				if( stick.isDirty() ){
					heightGotten.put(s, stick.render().getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING));
				}
				
				Image scaled = heightGotten.get(s);
				
				getLastRender().getGraphics().drawImage(scaled, x, y, null);
				
			}
			setDirty( false );
		}
		
		return getLastRender();
	}
	
}
