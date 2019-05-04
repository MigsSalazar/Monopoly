package monopoly7.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import monopoly7.models.RelativeDimensions;


/**
 * 
 * Tracks and maintains layers of StickerPages objects
 * storing each layer with a name and an index. Each StickerPage
 * object will be called to render their contents to the width and height
 * of the StickerBook
 * 
 * @author Miguel Salazar
 *
 */
@RequiredArgsConstructor
public class StickerBook extends BufferedRender{
	
	/**
	 * A list of "page" names, stored in the order in which they will be rendered
	 * in a FIFO style. That is, earlier pages listed are painted first and later
	 * pages are painted on top.
	 */
	private List<String> pageOrder = new LinkedList<String>();
	
	/**
	 * The map that records every String(name)-StickerPage pair
	 */
	private Map<String, StickerPage> titledPages = new HashMap<String, StickerPage>();
	
	/**
	 * Width, in pixels, by which to render the image
	 * 
	 */
	@NonNull @Getter
	private int width;
	/**
	 * Height, in pixels, by which to render the image
	 * 
	 */
	@NonNull @Getter
	private int height;
	
	/**
	 * Used for pages left unnamed by a developer. Such pages are titled
	 * "page#" where "#" is the current value of counter.
	 */
	private int counter=0;
	
	/**
	 * Sets the width to w and change marks the StickerBook as in need 
	 * of re-rendering if, and only if, w is not equal the current width
	 * @param w	the width, in pixels, of the StickerBook
	 */
	public void setWidth( int w ){
		if( w != width ){
			width = w;
			dirty = true;
		}
	}
	
	/**
	 * Sets the height to h and change marks the StickerBook as in need 
	 * of re-rendering if, and only if, h is not equal the current height
	 * @param h	the height, in pixels, of the StickerBook
	 */
	public void setHeight( int h ){
		height = h;
		dirty = true;
	}
	
	/**
	 * Removes the page from the StickerBook and returns both the name
	 * and StickerPage in a bare-bones Map.Entry object if the passed
	 * index is valid.
	 * <br>
	 * This method marks the StickerBook as in need of re-rendering
	 * @param index	number of the page layer
	 * @return	Bare-bones storing only the String name and the StickerPage
	 * object. Altering this object will NOT alter the StickerBook in any way 
	 */
	public Map.Entry<String, BufferedRender> removePage(int index){
		if( !titledPages.containsKey(index) ){
			return null;
		}
		int n = pageOrder.indexOf(index);
		dirty = true;
		return new FakeEntry<String,BufferedRender>( pageOrder.remove(n) , titledPages.remove(index) );
	}
	
	/**
	 * Removes the page from the StickerBook and returns both the index
	 * and StickerPage in a bare-bones Map.Entry object if the passed
	 * name is valid.
	 * <br>
	 * This method marks the StickerBook as in need of re-rendering
	 * @param name	String name of the page
	 * @return	Bare-bones storing only the index and the StickerPage
	 * object. Altering this object will NOT alter the StickerBook in any way 
	 */
	public Map.Entry<Integer, BufferedRender> removePage(String name){
		if( !titledPages.containsKey(name) ){
			return null;
		}
		int n = pageOrder.indexOf(name);
		pageOrder.remove(n);
		dirty = true;
		return new FakeEntry<Integer,BufferedRender>( n , titledPages.remove(name) );
	}
	
	/**
	 * Changes the render order by moving the page at oldLevel in
	 * the render order to newLevel
	 * @param oldLevel	current index of the page
	 * @param newLevel	desired new index of the page
	 * @return	true if the passed values were within the bounds of
	 * the render list, false if otherwise
	 */
	public boolean changePageIndex( int oldLevel, int newLevel ){
		if( oldLevel >= pageOrder.size() || oldLevel < 0 ||
			newLevel >= pageOrder.size() || newLevel < 0 ){
			return false;
		}
		String name = pageOrder.remove(oldLevel);
		pageOrder.add(newLevel, name);
		dirty = true;
		return true;
	}
	
	/**
	 * Changes the name of a page
	 * @param oldName	the current name of the page
	 * @param newName	the desired name
	 * @return	returns true if a page with oldName exists AND if
	 * no page with newName exists. Returns false if otherwise
	 */
	public boolean changePageName( String oldName, String newName ){
		if( !pageOrder.contains(oldName) || !titledPages.containsKey(oldName) ){
			return false;
		}
		if( pageOrder.contains(newName) || titledPages.containsKey(newName) ){
			return false;
		}
		
		StickerPage img = titledPages.remove(oldName);
		titledPages.put(newName, img);
		int index = pageOrder.indexOf(oldName);
		pageOrder.set(index, newName);
		return true;
	}
	
	/**
	 * Requests to resize the Sticker with the passed name if it exists in the page listed
	 * at the level-th  order of the render order.
	 * @param level	index of the page in the render order
	 * @param stickerName	the name of the sticker
	 * @param w	relative width of the sticker
	 * @param h	relative height of the sticker
	 * @return	true if the level is within the bounds of the render order and the page
	 * contains a sticker by the passed name, thus confirming the width and height can
	 * be set within the sticker. false if otherwise.
	 */
	public boolean resizeStickerAtPage( int level, String stickerName, double w, double h){
		if( level < 0 || level >= pageOrder.size() ){
			return false;
		}
		return resizeStickerAtPage( pageOrder.get(level), stickerName, w, h );
	}
	
	/**
	 * Requests to resize the Sticker with the passed name if it it exists in the page
	 * by the passed name
	 * @param pageName	name of the page by which to find the sticker
	 * @param stickerName	name of the sticker
	 * @param w	relative width of the sticker
	 * @param h	relative height of the sticker
	 * @return	true if the page exists and the page contains a sticker by the passed name, thus
	 * confirming the width and height can be set within the sticker. false if otherwise.
	 */
	public boolean resizeStickerAtPage( String pageName, String stickerName, double w, double h ){
		return resizeStickerAtPage( pageName, stickerName, new RelativeDimensions(w, h) );
	}
	
	/**
	 * Requests that the passed page changes the dimensions of a rendered sticker by
	 * page name, sticker name, and the x,y coordinates stored in the dimensions object.
	 * @param pageName	name of the page by which to find the sticker
	 * @param stickerName	name of the sticker
	 * @param dim	the desired relative dimensions to render the sticker
	 * @return	true if the page exists and the page contains a sticker by the passed name, thus
	 * confirming the RelativeDimensions can be set within the sticker. false if otherwise.
	 */
	public boolean resizeStickerAtPage( String pageName, String stickerName, RelativeDimensions dim ){
		if( titledPages.containsKey(pageName) ){
			StickerPage page = titledPages.get(pageName);
			if( page.containsSticker(stickerName) ){
				dirty = true;
				return page.resizeSticker(stickerName, dim);
			}
		}
		return false;
	}
	
	/**
	 * Moves the sticker located within the page at the level-th render order to the
	 * relative x,y coordinates
	 * @param level	index of the page in the render order
	 * @param stickerName	the sticker name
	 * @param x	desired relative horizontal coordinates
	 * @param y	desired relative vertical coordinates
	 * @return true if the index is within the bounds of the render order,
	 * the sticker exists on the page, and the page was able to update the
	 * coordinates. false if any part of this failed 
	 */
	public boolean moveStickerAtPage( int level, String stickerName, double x, double y ){
		if( level < 0 || level >= pageOrder.size() ){
			return false;
		}
		return moveStickerAtPage( pageOrder.get(level), stickerName, x, y );
	}
	
	/**
	 * Moves the sticker at the page associated with the given name to the relative x,y coordinates
	 * @param pageName	name of the page by which to find the sticker
	 * @param stickerName	the sticker name
	 * @param x	desired relative horizontal coordinate
	 * @param y	desired relative vertical coordinates
	 * @return	true if the page exists, the sticker exists on the page, and the page
	 * was able to update the coordinates. false if any part of this failed 
	 */
	public boolean moveStickerAtPage( String pageName, String stickerName, double x, double y ){
		return moveStickerAtPage( pageName, stickerName, new Point2D.Double(x,y) );
	}
	
	/**
	 * Moves the sticker at the page associated with the given name to the desired coordinates
	 * @param pageName	page name by which to find the sticker
	 * @param stickerName	the sticker name
	 * @param c	desired relative coordinates of the sticker
	 * @return	true if the page exists, the sticker exists on the page, and the page
	 * was able to update the coordinates. false if any part of this failed 
	 */
	public boolean moveStickerAtPage( String pageName, String stickerName, Point2D.Double c ){
		if( titledPages.containsKey(pageName) ){
			StickerPage page = titledPages.get(pageName);
			if( page.containsSticker(stickerName) ){
				dirty = true;
				return page.moveSticker(stickerName, c);
			}
		}
		return false;
	}
	
	/**
	 * Applies the given sticker to the index-th page in the render order with the
	 * given name at the passed in relative coordinates and with the size of the
	 * relative dimensions.
	 * @param pageIndex	index of the page within the render order
	 * @param s	the sticker to add
	 * @param c	the relative coordinates where the sticker's top left corder should be 
	 * @param d	the relative dimensions of the sticker
	 * @return	the name of the sticker that the page remembers the sticker by. If
	 * the returned value is "", then the sticker was not added because the page either
	 * failed to add it or the page index was beyond the bounds of the render order
	 */
	public String addStickerToPage( int pageIndex, Sticker s, Point2D.Double c, RelativeDimensions d ){
		if( pageIndex < 0 || pageIndex >= pageOrder.size() ){
			return "";
		}
		return addStickerToPage( pageOrder.get(pageIndex), s, c, d );
	}
	
	/**
	 * Applies the given sticker to the index-th page in the render order with the
	 * given name at the passed in relative coordinates and with the size of the
	 * relative dimensions.
	 * @param pageIndex	index of the page within the render order
	 * @param s	the sticker to add
	 * @param x	the relative horizontal position of the sticker's top left corner 
	 * @param y	the relative vertical position of the sticker's top left corner
	 * @param width	the relative width the sticker
	 * @param height	the relative height of the sticker
	 * @return	the name of the sticker that the page remembers the sticker by. If
	 * the returned value is "", then the sticker was not added because the page either
	 * failed to add it or the page index was beyond the bounds of the render order
	 */
	public String addStickerToPage( int pageIndex, Sticker s, double x, double y, double width, double height ){
		if( pageIndex < 0 || pageIndex >= pageOrder.size() ){
			return "";
		}
		return addStickerToPage( pageOrder.get(pageIndex), s, x, y, width, height );
	}
	
	/**
	 * Applies the given sticker to the page with the given name at the passed in
	 * relative coordinates and with the size of the relative dimensions
	 * @param pageName	name of the page where the sticker should be added
	 * @param s	the sticker to add
	 * @param x	the relative horizontal position of the sticker's top left corner 
	 * @param y	the relative vertical position of the sticker's top left corner
	 * @param width	the relative width the sticker
	 * @param height	the relative height of the sticker
	 * @return	the name of the sticker that the page remembers the sticker by. If
	 * the returned value is "", then the sticker was not added because the page either
	 * failed to add it or the page did not exist inside the StickerBook 
	 */
	public String addStickerToPage( String pageName, Sticker s, double x, double y, double width, double height ){
		return addStickerToPage( pageName, s, new Point2D.Double(x,y), new RelativeDimensions(width, height) );
	}
	
	/**
	 * Applies the given sticker to the page with the given name at the passed in
	 * relative coordinates and with the size of the relative dimensions
	 * @param pageName	name of the page where the sticker should be added
	 * @param s	the sticker to add
	 * @param c	the relative coordinates where the sticker's top left corder should be 
	 * @param d	the relative dimensions of the sticker
	 * @return	the name of the sticker that the page remembers the sticker by. If
	 * the returned value is "", then the sticker was not added because the page either
	 * failed to add it or the page did not exist inside the StickerBook 
	 */
	public String addStickerToPage( String pageName, Sticker s, Point2D.Double c, RelativeDimensions d ){
		if( !titledPages.containsKey(pageName) ){
			return "";
		}
		return titledPages.get(pageName).addSticker(s, c, d);
	}
	
	/**
	 * Pushes the passed StickerPage to the end of the render order.
	 * That is, it will be the last to render and will thus sit atop all other pages
	 * @param br	page to add to the render order
	 * @return	the name of the page as stored within the StickerBook. If "" is returned,
	 * then the page was NOT added
	 */
	public String pushBackPage( StickerPage br ){
		return addPage( "page"+(counter++), pageOrder.size(), br );
	}
	
	/**
	 * Pushes the passed StickerPage to the start of the render order.
	 * That is, it will be the first to render and will thus sit at the bottom of all other pages 
	 * @param br	page to add to the render order
	 * @return	the name of the page as stored within the StickerBook. If "" is returned,
	 * then the page was NOT added
	 */
	public String pushFrontPage( StickerPage br ){
		return addPage( "page"+(counter++), 0, br );
	}
	
	/**
	 * Pushes the passed StickerPage to the provided index of the render order,
	 * pushing any pages on or after that index further down the render order.
	 * @param index	layer away from the bottom from which the passed page will be rendered
	 * @param br	the page to add to the render order
	 * @return	the name of the page stored within the StickerBook. If "" is returned,
	 * then the page was NOT added
	 */
	public String pushPageAt( int index, StickerPage br ){
		return addPage( "page"+(counter++), index, br );
	}
	
	/**
	 * Adds a clean page to the render order at the provided index.
	 * Any pages at or after the index in the render order will be postponed
	 * until this new page is rendered and added.
	 * @param index	layer away from the bottom from which the passed page will be ordered
	 * @return	the name of the page stored within the StickerBook. If "" is returned,
	 * then the page was NOT added
	 */
	public String addNewPageAt( int index ){
		return addNewPageAt( "page"+(counter++), index );
	}
	
	/**
	 * Adds a clean page to the render order at the provided index and with the
	 * given name. Any pages at or after the index in the render order will be
	 * postponed until this new page is rendered and added.
	 * @param name	desired name of the page
	 * @param index	layer away from the bottom from which the new page will be ordered 
	 * @return	the actual name of the page stored within the StickerBook. THIS MAY BE
	 * DIFFERENT FROM THE PASSED PAGE. If "" is returned, then the page was NOT added
	 */
	public String addNewPageAt( String name, int index ){
		return addPage( name, index, new StickerPage(width, height) );
	}
	
	/**
	 * Adds a clean page to the end of the render order
	 * @return	the name of the page stored within the StickerBook. If "" is returned,
	 * then the page was NOT added
	 */
	public String addNewPageToEnd(){
		return addPage( "page"+(counter++), pageOrder.size(), new StickerPage(width,height) );
	}
	
	/**
	 * Attempts to add a clean page to the end of the render order with the given name
	 * @param name	desired name of the new page
	 * @return	the actual name of the page stored within the StickerBook. THIS MAY BE
	 * DIFFERENT FROM THE PASSED PAGE. If "" is returned, then the page was NOT added
	 */
	public String addNewPageToEnd( String name ){
		return addPage( name, pageOrder.size(), new StickerPage(width,height) );
	}
	
	/**
	 * Adds the given page to the render order at the index-th position and attempts
	 * to given the page the passed name
	 * @param name	the desired name for the page
	 * @param index	layer away from the bottom from which the passed page will be ordered
	 * @param p	the StickerPage to add to the StickerBook
	 * @return	the actual name of the page stored within the StickerBook. THIS MAY BE
	 * DIFFERENT FROM THE PASSED PAGE. If "" is returned,then the page was NOT added
	 */
	public String addPage(String name, int index, StickerPage p){
		if( index < 0 || index > titledPages.size() ){
			return "";
		}
		if( titledPages.containsKey(name) ){
			int c = 0;
			while( titledPages.containsKey(name + c) ){
				c++;
			}
			name += c;
		}
		
		titledPages.put( name, p );
		pageOrder.add(index, name);
		dirty = true;
		return name;
	}
	
	@Override
	public Image render() {
		if( isRenderNeeded() ){
			lastRender = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
			Graphics g = lastRender.getGraphics();
			for( String s : pageOrder ){
				StickerPage page = titledPages.get(s);
				page.setWidth(width);
				page.setHeight(height);
				g.drawImage( page.render() , 0, 0, null);
			}
			dirty = false;
		}
		return lastRender;
	}
	
	/**
	 * A basic implementation of the java.utils.Map.Entry used
	 * to return both the index and the string name of a StickerPage
	 * contained within the StickerBook
	 * @author Miguel Salazar
	 *
	 * @param <K>	Key type
	 * @param <V>	Value type
	 * @see java.util.Map.Entry
	 */
	@Data
	private class FakeEntry <K,V> implements Map.Entry<K, V>{
		
		/**
		 * Key associated with the value
		 */
		@Setter(AccessLevel.NONE) @NonNull
		private K key;
		
		/**
		 * Value associated with the key
		 */
		@Setter(AccessLevel.NONE) @NonNull
		private V value;
		
		public V setValue(V value) {
			V old = this.value;
			this.value = value;
			return old;
		}
		
	}
	
}
